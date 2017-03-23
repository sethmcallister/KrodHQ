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

        ArrayList<PotionEffect> bard = new ArrayList<>();
        bard.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        bard.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        bard.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
        this.classEffects.put(ClassType.BARD, bard);

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
                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.BARD))
                {
                    removeEffects(player, ClassType.BARD);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class: &4Disabled"));
                }
                else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
                {
                    removeEffects(player, ClassType.ARCHER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class: &4Disabled"));
                }

                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
                    return;

                classes.put(player, ClassType.MINER);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class will be enabled in &310&7 seconds."));
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        giveEffects(player, ClassType.MINER);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class: &aEnabled"));
                    }
                }.runTaskLater(API.getPlugin(), 10 * 20L);
            }
            else if (inventory.getHelmet().getType().equals(Material.LEATHER_HELMET) && inventory.getChestplate().getType().equals(Material.LEATHER_CHESTPLATE) && inventory.getLeggings().getType().equals(Material.LEATHER_LEGGINGS) && inventory.getBoots().getType().equals(Material.LEATHER_BOOTS))
            {
                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.BARD))
                {
                    removeEffects(player, ClassType.BARD);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class: &4Disabled"));
                }
                else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
                {
                    removeEffects(player, ClassType.MINER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class: &4Disabled"));
                }

                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
                    return;

                classes.put(player, ClassType.ARCHER);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class will be enabled in &310&7 seconds."));
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        giveEffects(player, ClassType.ARCHER);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class: &aEnabled"));
                    }
                }.runTaskLater(API.getPlugin(), 10 * 20L);
            }
            else if (inventory.getHelmet().getType().equals(Material.DIAMOND_HELMET) && inventory.getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE) && inventory.getLeggings().getType().equals(Material.DIAMOND_LEGGINGS) && inventory.getBoots().getType().equals(Material.DIAMOND_BOOTS))
            {
                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
                {
                    removeEffects(player, ClassType.ARCHER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class: &4Disabled"));
                }
                else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
                {
                    removeEffects(player, ClassType.MINER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class: &4Disabled"));
                }
            }
            else if (inventory.getHelmet().getType().equals(Material.GOLD_HELMET) && inventory.getChestplate().getType().equals(Material.GOLD_CHESTPLATE) && inventory.getLeggings().getType().equals(Material.GOLD_LEGGINGS) && inventory.getBoots().getType().equals(Material.GOLD_BOOTS))
            {
                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.BARD))
                {
                    removeEffects(player, ClassType.BARD);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class: &4Disabled"));
                }
                else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
                {
                    removeEffects(player, ClassType.ARCHER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class: &4Disabled"));
                }
                else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
                {
                    removeEffects(player, ClassType.MINER);
                    this.classes.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class: &4Disabled"));
                }

                if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.BARD))
                {
                    ItemStack hand = player.getItemInHand();
                    if(hand == null)
                        return;

                    if(hand.getType().equals(Material.BLAZE_POWDER))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);

                            if(player.getLocation().distanceSquared(player.getLocation()) >= 20)
                                return;

                            if(member.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                                member.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0));
                        }
                        return;
                    }

                    if(hand.getType().equals(Material.IRON_INGOT))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);

                            if(player.getLocation().distanceSquared(player.getLocation()) >= 20)
                                return;

                            if(member.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                                member.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 0));
                        }
                        return;
                    }

                    if(hand.getType().equals(Material.SUGAR))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);

                            if(player.getLocation().distanceSquared(player.getLocation()) >= 20)
                                return;

                            if(member.hasPotionEffect(PotionEffectType.SPEED))
                                member.removePotionEffect(PotionEffectType.SPEED);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
                        }
                        return;
                    }

                    if(hand.getType().equals(Material.GHAST_TEAR))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);

                            if(player.getLocation().distanceSquared(player.getLocation()) >= 20)
                                return;

                            if(member.hasPotionEffect(PotionEffectType.REGENERATION))
                                member.removePotionEffect(PotionEffectType.REGENERATION);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 0));
                        }
                        return;
                    }

                    if(hand.getType().equals(Material.FEATHER))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);
                            if(member.hasPotionEffect(PotionEffectType.JUMP))
                                member.removePotionEffect(PotionEffectType.JUMP);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 0));
                        }
                        return;
                    }

                    if(hand.getType().equals(Material.MAGMA_CREAM))
                    {
                        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                        if(faction == null)
                            return;

                        for(UUID memberUUID : faction.getOnlineMembers())
                        {
                            if(memberUUID.equals(player.getUniqueId()))
                                continue;

                            Player member = Bukkit.getPlayer(memberUUID);
                            if(member.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                                member.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);

                            member.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20, 0));
                        }
                        return;
                    }
                    return;
                }

                classes.put(player, ClassType.BARD);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class will be enabled in &310&7 seconds."));
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        giveEffects(player, ClassType.BARD);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class: &aEnabled"));
                    }
                }.runTaskLater(API.getPlugin(), 10 * 20L);
            }
        }
        else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.BARD))
        {
            removeEffects(player, ClassType.BARD);
            this.classes.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Bard class: &4Disabled"));
        }
        else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.ARCHER))
        {
            removeEffects(player, ClassType.ARCHER);
            this.classes.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Archer class: &4Disabled"));
        }
        else if (this.classes.containsKey(player) && this.classes.get(player).equals(ClassType.MINER))
        {
            removeEffects(player, ClassType.MINER);
            this.classes.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Miner class: &4Disabled"));
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

            if(!this.classes.containsKey(archer) && !this.classes.get(archer).equals(ClassType.ARCHER))
                return;

            if (this.timers.containsKey(archer) && this.timers.get(archer) > System.currentTimeMillis())
            {
                long millisLeft = this.timers.get(archer) - System.currentTimeMillis();
                double value = millisLeft / 1000.0D;
                double sec = Math.round(10.0D * value) / 10.0D;
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Effect cooldown: &c" + sec + " seconds&7."));
                return;
            }

            this.timers.put(archer, 16000 + System.currentTimeMillis());
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 5 * 20, 4);
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
                        member.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been given &3Speed 5&7 for &35&7 seconds."));
                        archer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have given &3" + member.getName() + " Speed 5&7 for &55&7 seconds."));
                    }
                }
            }
        }

        if(hand.getType().equals(Material.FEATHER))
        {
            Player bard = event.getPlayer();

            if(!this.classes.containsKey(bard) && !this.classes.get(bard).equals(ClassType.BARD))
                return;

            if (this.timers.containsKey(bard) && this.timers.get(bard) > System.currentTimeMillis())
            {
                long millisLeft = this.timers.get(bard) - System.currentTimeMillis();
                double value = millisLeft / 1000.0D;
                double sec = Math.round(10.0D * value) / 10.0D;
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Effect cooldown: &c" + sec + " seconds&7."));
                return;
            }

            this.timers.put(bard, 16000 + System.currentTimeMillis());
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2);
            event.getPlayer().addPotionEffect(potionEffect);
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(event.getPlayer());
            DefaultTimer defaultTimer = new DefaultTimer(TimerType.ARCHER_COOLDOWN, 16000 + System.currentTimeMillis(), bard);
            Factions.getInstance().getTimerHandler().addTimer(bard, defaultTimer);
            if (faction != null)
            {
                for (UUID uuid : faction.getOnlineMembers())
                {
                    Player member = Bukkit.getPlayer(uuid);
                    if (member.equals(bard))
                        continue;

                    if (bard.getLocation().distance(member.getLocation()) <= 20)
                    {
                        member.addPotionEffect(potionEffect);
                        member.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been given &3Jump Boost 3&7 for &35&7 seconds."));
                        bard.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have given &3" + member.getName() + " Speed 5&7 for &55&7 seconds."));
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

        if(Factions.getInstance().getTimerHandler().hasTimer(damaged, TimerType.ARCHER_TAG))
        {
            final ArcherTag archerTag = (ArcherTag) Factions.getInstance().getTimerHandler().getTimer(damaged, TimerType.ARCHER_TAG);
            archerTag.setTime(30000  + System.currentTimeMillis());
            damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lMarked! &eAn archer has shot you and marked you (+25% damage) for 10 seconds"));
            damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have archer tagged &3" + damaged.getName() + "&7 for &330 seconds&7."));
            return;
        }

        final ArcherTag archerTag = (ArcherTag) Factions.getInstance().getTimerHandler().getTimer(damaged, TimerType.ARCHER_TAG);
        archerTag.setTime(30000  + System.currentTimeMillis());
        damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lMarked! &eAn archer has shot you and marked you (+25% damage) for 10 seconds"));
        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have archer tagged &3" + damaged.getName() + "&7 for &330 seconds&7."));
        Factions.getInstance().getTimerHandler().addTimer(damaged, archerTag);
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
