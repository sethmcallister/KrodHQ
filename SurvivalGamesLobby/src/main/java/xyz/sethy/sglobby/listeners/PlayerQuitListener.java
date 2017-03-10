package xyz.sethy.sglobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.sglobby.SGLobby;

/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void onPlayerQuit(UserLoggedOutEvent event)
    {
        if (SGLobby.getInstance().getPlayerQueue().isQueueing(event.getPlayer()))
        {
            SGLobby.getInstance().getPlayerQueue().removeFromQueue(event.getPlayer());
        }
        SGLobby.getInstance().getPlayers().remove(event.getPlayer());
    }
}
