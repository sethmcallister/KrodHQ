package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.TimerType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 31/01/2017.
 */
public class UserLogoutListener implements Listener
{
    private final ConcurrentHashMap<UUID, Skeleton> villagerEntities;

    public UserLogoutListener()
    {
        this.villagerEntities = new ConcurrentHashMap<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onUserLogout(final UserLoggedOutEvent event)
    {
        if (event.getPlayer() == null)
            return;

        if (event.getPlayer().hasMetadata("SafeLogout"))
            return;

        final Skeleton villager = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Skeleton.class);

        villager.setCustomName(event.getPlayer().getName());
        villager.setCustomNameVisible(true);
        villager.setMetadata("VillagerFor", new FixedMetadataValue(API.getPlugin(), event.getPlayer().getUniqueId()));
        villager.setMetadata("Inventory", new FixedMetadataValue(API.getPlugin(), event.getPlayer().getInventory().getContents()));
        villager.setMetadata("Armor", new FixedMetadataValue(API.getPlugin(), event.getPlayer().getInventory().getArmorContents()));
        villager.setMaxHealth(50.0D);
        villager.setHealth(villager.getMaxHealth());

        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(event.getUser().getUniqueId());
        if(Factions.getInstance().getTimerHandler().hasTimer(event.getPlayer(), TimerType.PVP_TIMER))
            hcfUser.setPvPTimer(Factions.getInstance().getTimerHandler().getTimer(event.getPlayer(), TimerType.PVP_TIMER).getTime());

        this.villagerEntities.put(event.getPlayer().getUniqueId(), villager);

        Bukkit.getScheduler().scheduleSyncDelayedTask(API.getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                villager.removeMetadata("VillagerFor", API.getPlugin());
                villager.removeMetadata("Inventory", API.getPlugin());
                villager.removeMetadata("Armor", API.getPlugin());
                villagerEntities.remove(event.getPlayer().getUniqueId());
                villager.remove();
            }
        }, 30 * 20L);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();

        if(event.getEntity() instanceof Skeleton && this.villagerEntities.containsValue(event.getEntity()))
        {
            Player player = entity.getKiller();
            if (player != null)
            {
                KitmapUser killed = API.getUserManager().findKitmapByUniqueId(player.getUniqueId());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(Combat Logger)&c" + entity.getCustomName() + "&7 was slain by &c" + player.getName() + "&4[" + killed.getKills() + "]&7"));
            }
            else
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(Combat Logger)&c" + entity.getCustomName() + "&7 was slain."));

            ItemStack[] inventory = (ItemStack[]) entity.getMetadata("Inventory").get(0).value();
            ItemStack[] armor = (ItemStack[]) entity.getMetadata("Armor").get(0).value();

            int i = 0;
            while (i < inventory.length)
            {
                entity.getWorld().dropItemNaturally(entity.getLocation(), inventory[i]);
                i++;
            }
            int k = 0;
            while (k < inventory.length)
            {
                entity.getWorld().dropItemNaturally(entity.getLocation(), armor[k]);
                k++;
            }

            HCFUser hcfUser = API.getUserManager().findHCFByUniqueId((UUID) entity.getMetadata("VillagerFor").get(0).value());
            hcfUser.setDeathbanTime(Factions.getInstance().getDeathbanManager().getDeathbanTime(API.getUserManager().findByUniqueId(hcfUser.getUUID())) + System.currentTimeMillis());
            hcfUser.setDeathbanTime(hcfUser.getDeaths() + 1);
            entity.remove();
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        if(this.villagerEntities.containsKey(event.getPlayer().getUniqueId()))
        {
            Skeleton villager = this.villagerEntities.get(event.getPlayer().getUniqueId());
            villager.remove();
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof Skeleton && this.villagerEntities.containsValue(event.getRightClicked()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Skeleton && this.villagerEntities.containsValue(event.getEntity()))
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    event.getEntity().setVelocity(new Vector());
                }
            }.runTask(API.getPlugin());
        }
    }
}
