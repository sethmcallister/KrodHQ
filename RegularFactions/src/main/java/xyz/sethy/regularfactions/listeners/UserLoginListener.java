package xyz.sethy.regularfactions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.sethy.api.events.UserLoggedInEvent;

/**
 * Created by Seth on 26/03/2017.
 */
public class UserLoginListener implements Listener
{
    @EventHandler
    public void onUserLogin(UserLoggedInEvent event)
    {
        System.out.println("user logged in -> " + event.getUser().getName());
    }
}
