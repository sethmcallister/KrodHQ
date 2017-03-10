package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.factions.Factions;

/**
 * Created by sethm on 02/01/2017.
 */
public class PlayerInteractListener implements Listener
{
    public PlayerInteractListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {

    }
}
