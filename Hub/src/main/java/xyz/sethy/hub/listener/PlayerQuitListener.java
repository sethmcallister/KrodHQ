package xyz.sethy.hub.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sethy.hub.Hub;

/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (Hub.getInstance().getPlayerQueue().isQueueing(event.getPlayer()))
        {
            Hub.getInstance().getPlayerQueue().removeFromQueue(event.getPlayer());
        }

        if (PlayerInteractListener.hidingPlayers.contains(event.getPlayer()))
        {
            PlayerInteractListener.hidingPlayers.remove(event.getPlayer());
        }

        Hub.getInstance().getPlayers().remove(event.getPlayer());
        event.setQuitMessage(null);
    }
}
