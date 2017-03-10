package xyz.sethy.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.core.framework.user.CoreUser;
import xyz.sethy.core.framework.user.UserManager;

/**
 * Created by sethm on 04/12/2016.
 */
public class PlayerQuitListner implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        UserManager manager = (UserManager) API.getUserManager();
        CoreUser user = (CoreUser) manager.findByUniqueId(event.getPlayer().getUniqueId());
        manager.unloadUser(user);

        event.setQuitMessage(null);
        Bukkit.getPluginManager().callEvent(new UserLoggedOutEvent(user, event.getPlayer()));
    }
}
