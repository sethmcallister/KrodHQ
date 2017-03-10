package xyz.sethy.factions.handlers.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.timers.ArcherTag;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.TimerType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 16/01/2017.
 */
public class ClassHandler extends BukkitRunnable implements Listener
{
    private ConcurrentHashMap<Player, ClassType> classes;
    private ConcurrentHashMap<ClassType, ArrayList<PotionEffect>> classEffects;
    private ConcurrentHashMap<Player, ArrayList<PotionEffect>> cachedEffects;
    private ConcurrentHashMap<Player, Long> timers;

    public ClassHandler()
    {
        this.classes = new ConcurrentHashMap<>();
        this.classEffects = new ConcurrentHashMap<>();
        ArrayList<PotionEffect> archer = new ArrayList<>();
        archer.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
        archer.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        archer.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
        this.classEffects.put(ClassType.ARCHER, archer);

        ArrayList<PotionEffect> miner = new ArrayList<>();
        miner.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        miner.add(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
        this.classEffects.put(ClassType.MINER, miner);

        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
        this.timers = new ConcurrentHashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(API.getPlugin(), this, 0L, 7L);
    }

    private void checkArmor(final Player player)
    {
        final PlayerInventory inventory = player.getInventory();
        if (inventory == null)
            return;

        if (inventory.getHelmet() != null && inventory.getChestplate() != null && inventory.getLeggings() != null && inventory.getBoots() != null)
        {
            if (inventory.getHelmet().getType().equals(Material.IRON_HELMET) && inventory.getChestplate().getType().equals(Material.IRON_CHESTPLATE) && inventory.getLeggings().getType().equals(Material.IRON_LEGGINGS) && inventory.getBoots().getType().equals(Material.IRON_BOOTS))
            {

                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
                    return;

                this.classes.put(player, ClassType.MINER);
                giveEffects(player, ClassType.MINER);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Miner class: &aEnabled"));
            }
            else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
            {
                removeEffects(player, ClassType.ARCHER);
                this.classes.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Archer class: &4Disabled"));
            }
        }
        else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
        {
            removeEffects(player, ClassType.ARCHER);
            this.classes.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Archer class: &4Disabled"));
        }
        else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
        {
            removeEffects(player, ClassType.MINER);
            this.classes.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Miner class: &4Disabled"));
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event)
    {
        ItemStack hand = event.getPlayer().getItemInHand();
        if (hand == null)
            return;

        if (hand.getType().equals(Material.SUGAR))
        {
            Player archer = event.getPlayer();

            if (this.timers.containsKey(archer) && this.timers.get(archer) > System.currentTimeMillis())
            {
                long millisLeft = this.timers.get(archer) - System.currentTimeMillis();
                double value = millisLeft / 1000.0D;
                double sec = Math.round(10.0D * value) / 10.0D;
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Effect cooldown: &c" + sec + " seconds&7."));
                return;
            }

            this.timers.put(archer, 16000 + System.currentTimeMillis());
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4);
            event.getPlayer().addPotionEffect(potionEffect);
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(event.getPlayer());
            DefaultTimer defaultTimer = new DefaultTimer(TimerType.ARCHER_COOLDOWN, 16000 + System.currentTimeMillis(), archer);
            Factions.getInstance().getTimerHandler().addTimer(archer, defaultTimer);
            if (faction != null)
            {
                for (UUID uuid : faction.getOnlineMembers())
                {
                    Player member = Bukkit.getPlayer(uuid);
                    if (member.equals(archer))
                        continue;

                    if (archer.getLocation().distance(member.getLocation()) <= 20)
                    {
                        member.addPotionEffect(potionEffect);
                        member.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have been given &7Speed 5&3 for &75&3 seconds."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArcherTag(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Arrow))
            return;

        if (!(((((Arrow) event.getDamager()).getShooter())) instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();

        if (Factions.getInstance().getLandBoard().getFaction(damaged.getLocation()) != null)
        {
            Faction faction1 = Factions.getInstance().getLandBoard().getFaction(damaged.getLocation());
            if (faction1.hasDTRBitmask(DTRType.SAFEZONE))
            {
                event.setCancelled(true);
                return;
            }
        }

        Player damager = (Player) ((Arrow) event.getDamager()).getShooter();

        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(damager);
        if (faction != null && faction.getOnlineMembers().contains(damaged.getUniqueId()))
        {
            return;
        }

        ArcherTag archerTag = (ArcherTag) Factions.getInstance().getTimerHandler().getTimer(damaged, TimerType.ARCHER_TAG);
        if (archerTag != null && archerTag.getTime() > 0L)
        {
            if (archerTag.getTagLevel() == 1)
            {
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 1));
                archerTag.setTagLevel(archerTag.getTagLevel() + 1);
                archerTag.setTime(System.currentTimeMillis() + 30000L);
                damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have been archer tagged with level&7 2&3."));
                damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have archer tagged &7" + damaged.getName() + "&3 at level&7 2&3."));
                return;
            }
            else if (archerTag.getTagLevel() == 2)
            {
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 3 * 20, 1));
                archerTag.setTagLevel(archerTag.getTagLevel() + 1);
                archerTag.setTime(System.currentTimeMillis() + 30000L);
                damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have been archer tagged with level&7 3&3."));
                damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have archer tagged &7" + damaged.getName() + "&3 at level&7 3&3."));
                return;
            }
            return;
        }
        ArcherTag archerTagadd = new ArcherTag(TimerType.ARCHER_TAG, System.currentTimeMillis() + 30000L, damaged, 1);
        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 1));
        Factions.getInstance().getTimerHandler().addTimer(damaged, archerTagadd);
        damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have been archer tagged with level&7 1&3."));
        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have archer tagged &7" + damaged.getName() + "&3 at level&7 1&3."));
    }

    private void giveEffects(final Player player, final ClassType classType)
    {
        player.addPotionEffects(classEffects.get(classType));
    }

    private void removeEffects(final Player player, final ClassType classType)
    {
        for (PotionEffect potionEffect : classEffects.get(classType))
        {
            if (player.hasPotionEffect(potionEffect.getType()))
            {
                player.removePotionEffect(potionEffect.getType());
            }
        }
    }

    public ClassType getClassType(final Player player)
    {
        return classes.get(player);
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            checkArmor(player);
        }
    }
}
