package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import xyz.sethy.api.API;

/**
 * Created by Seth on 31/01/2017.
 */
public class TntMinecraftFixListener implements Listener
{
    public TntMinecraftFixListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onTntMinecart(EntityExplodeEvent event)
    {
        event.blockList().clear();
        event.setCancelled(true);
    }
}
