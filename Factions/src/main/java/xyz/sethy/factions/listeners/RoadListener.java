package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.handlers.dtr.DTRType;

/**
 * Created by sethm on 07/12/2016.
 */
public class RoadListener implements Listener
{

    public RoadListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }


    private boolean isRoad(final Location loc)
    {
        return DTRType.ROAD.appliesAt(loc);
    }

    @EventHandler
    public void onPistonRetract(final BlockPistonRetractEvent event)
    {
        if (event.isSticky() && this.isRoad(event.getRetractLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(final BlockPistonExtendEvent event)
    {
        if (this.isRoad(event.getBlock().getRelative(event.getDirection(), event.getLength() + 1).getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent event)
    {
        if (this.isRoad(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event)
    {
        if (this.isRoad(event.getBlock().getLocation()))
        {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event)
    {
        if (this.isRoad(event.getBlock().getLocation()))
        {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event)
    {
        if (this.isRoad(event.getBlock().getLocation()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null && this.isRoad(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation()) && (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL || event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET || (event.getPlayer().getItemInHand().getType() == Material.INK_SACK && event.getPlayer().getItemInHand().getData().getData() == 15)))
        {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }
}
