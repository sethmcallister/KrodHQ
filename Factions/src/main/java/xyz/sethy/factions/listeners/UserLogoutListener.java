package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerType;

/**
 * Created by Seth on 31/01/2017.
 */
public class UserLogoutListener implements Listener
{
    public UserLogoutListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onUserLogout(UserLoggedOutEvent event)
    {
        if(Factions.getInstance().isKitmap())
            return;

        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(event.getPlayer().getUniqueId());
        if(Factions.getInstance().getTimerHandler().hasTimer(event.getPlayer(), TimerType.PVP_TIMER))
        {
            Timer timer = Factions.getInstance().getTimerHandler().getTimer(event.getPlayer(), TimerType.PVP_TIMER);
            hcfUser.setPvPTimer(timer.getTime());
        }
    }
}
