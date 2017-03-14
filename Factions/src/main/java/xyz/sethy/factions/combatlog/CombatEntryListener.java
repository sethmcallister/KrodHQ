package xyz.sethy.factions.combatlog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.handlers.dtr.DTRType;

/**
 * Created by Seth on 11/03/2017.
 */
public class CombatEntryListener implements Listener
{
    public CombatEntryListener()
    {
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if(!(event.getEntity() instanceof Villager))
            return;

        if(event.getEntity().getKiller() == null)
            return;

        Villager villager = (Villager) event.getEntity();
        CombatEntry entry = Factions.getInstance().getCombatLoggerManager().findByEntity(villager);
        if(entry == null)
            return;

        Player killer = event.getEntity().getKiller();
        HCFUser fKiller = API.getUserManager().findHCFByUniqueId(killer.getUniqueId());

        String message = "&7(Combat Logger)&c" + entry.getPlayerName() + "&7 was slain by &c" + killer.getName() + "&4[" + fKiller.getKills() + "]";

        for(ItemStack itemStack : entry.getInventory())
        {
            if(itemStack != null && !itemStack.getType().equals(Material.AIR))
                villager.getLocation().getWorld().dropItemNaturally(villager.getLocation(), itemStack);
        }

        for(ItemStack itemStack : entry.getArmor())
        {
            if(itemStack != null && !itemStack.getType().equals(Material.AIR))
                villager.getLocation().getWorld().dropItemNaturally(villager.getLocation(), itemStack);
        }

        if(!Factions.getInstance().isKitmap())
        {
            API.getUserManager().getTempUser(entry.getUuid()).getHCFUser().setDeathbanTime(Factions.getInstance().getDeathbanManager().getDeathbanTime(entry.getGroup()));
            API.getUserManager().getTempUser(entry.getUuid()).getHCFUser().setDeathbanMessage(ChatColor.translateAlternateColorCodes('&', message));
            API.getUserManager().getTempUser(entry.getUuid()).forceSave();
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Villager))
            return;

        Villager villager = (Villager) event.getEntity();
        CombatEntry entry = Factions.getInstance().getCombatLoggerManager().findByEntity(villager);
        if(entry == null)
            return;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                villager.setVelocity(new Vector());
            }
        }.runTask(API.getPlugin());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        CombatEntry entry = Factions.getInstance().getCombatLoggerManager().findByPlayer(event.getPlayer());
        if(entry == null)
            return;

        Villager villager = entry.getVillager();
        villager.remove();
        Factions.getInstance().getCombatLoggerManager().getCombatEntries().remove(entry);
    }

    @EventHandler
    public void onPlayerQuit(UserLoggedOutEvent event)
    {
        if(event.getPlayer().hasMetadata("SageLogout"))
            return;

        if(Factions.getInstance().getLandBoard().getFaction(event.getPlayer().getLocation()).hasDTRBitmask(DTRType.SAFEZONE))
            return;

        Villager villager = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Villager.class);
        villager.setCustomName(event.getPlayer().getName());
        villager.setCustomNameVisible(true);
        villager.setMaxHealth(50.0D);
        villager.setHealth(villager.getMaxHealth());
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));

        CombatEntry entry = new CombatEntry(villager, event.getPlayer().getUniqueId(), event.getPlayer().getInventory().getContents(), event.getPlayer().getInventory().getArmorContents(), event.getPlayer().getName(), event.getUser().getGroup());
        Factions.getInstance().getCombatLoggerManager().getCombatEntries().add(entry);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                villager.remove();
                Factions.getInstance().getCombatLoggerManager().getCombatEntries().remove(entry);
            }
        }.runTaskLater(API.getPlugin(), 20 * 20L);
    }
}
