package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import xyz.sethy.api.API;

/**
 * Created by Seth on 31/01/2017.
 */
public class UserLogoutListener implements Listener
{
    public UserLogoutListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }
}
