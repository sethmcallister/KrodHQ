package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.handlers.dtr.DTRType;

/**
 * Created by sethm on 07/12/2016.
 */
public class SpawnListener implements Listener
{
    public SpawnListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(final BlockIgniteEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodHunger(final FoodLevelChangeEvent event)
    {
        if(((Player) event.getEntity()).getFoodLevel() > event.getFoodLevel())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        }
        else if (Factions.getInstance().getLandBoard().isSpawnBufferZone(event.getBlock().getLocation()) || Factions.getInstance().getLandBoard().isNetherBufferZone(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        }
        else if (Factions.getInstance().getLandBoard().isSpawnBufferZone(event.getBlock().getLocation()) || Factions.getInstance().getLandBoard().isNetherBufferZone(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            if (event.getBlock().getType() != Material.LONG_GRASS && event.getBlock().getType() != Material.GRASS)
            {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getEntity().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event)
    {
        if (!(event.getRemover() instanceof Player))
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getEntity().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event)
    {
        if (event.isCancelled() || event.getRightClicked().getType() != EntityType.ITEM_FRAME)
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getRightClicked().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
    {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || event.getEntity().getType() != EntityType.ITEM_FRAME)
        {
            return;
        }
        if (DTRType.SAFEZONE.appliesAt(event.getEntity().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event)
    {
        if (Factions.getInstance().getLandBoard().isSpawnBufferZone(event.getBlockClicked().getLocation()))
        {
            event.setCancelled(true);
            event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
            event.setItemStack(new ItemStack(event.getBucket()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(final EntityDamageEvent event)
    {
        if (event.isCancelled() || Factions.getInstance().getServerHandler().isEOTW())
        {
            return;
        }
        if ((event.getEntity() instanceof Player || event.getEntity() instanceof Horse) && DTRType.SAFEZONE.appliesAt(event.getEntity().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity2(final EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player) || event.isCancelled())
        {
            return;
        }
        Player damager = null;
        if (event.getDamager() instanceof Player)
        {
            damager = (Player) event.getDamager();
        }
        else if (event.getDamager() instanceof Projectile)
        {
            final Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player)
            {
                damager = (Player) projectile.getShooter();
            }
        }
        if (damager != null)
        {
            final Player victim = (Player) event.getEntity();
            if (!Factions.getInstance().getServerHandler().isEOTW() && (DTRType.SAFEZONE.appliesAt(victim.getLocation()) || DTRType.SAFEZONE.appliesAt(damager.getLocation())))
            {
                event.setCancelled(true);
            }
        }
    }
}
