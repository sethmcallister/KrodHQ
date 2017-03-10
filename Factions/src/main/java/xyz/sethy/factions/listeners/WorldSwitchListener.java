package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.sethy.api.API;

/**
 * Created by Seth on 09/03/2017.
 */
public class WorldSwitchListener implements Listener
{
    public WorldSwitchListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }


    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event)
    {
        if(event.getFrom().getEnvironment().equals(World.Environment.NORMAL))
        {
            if(event.getPlayer().getLocation().getWorld().getEnvironment().equals(World.Environment.THE_END))
            {
                Location location = new Location(event.getPlayer().getWorld(), 72.5, 60, 30.5);
                event.getPlayer().teleport(location);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(event.getPlayer().getWorld().getEnvironment().equals(World.Environment.THE_END))
        {
            if(event.getTo().getBlock().isLiquid())
            {
                Location location = new Location(Bukkit.getWorld("world"), 0.5, 69, -400.5);
                event.getPlayer().teleport(location);
            }
        }
    }
}
