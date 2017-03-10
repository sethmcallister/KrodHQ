package xyz.sethy.sglobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

/**
 * Created by sethm on 23/12/2016.
 */
public class DoubleJumpListener implements Listener
{
    @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event)
    {
        if (!event.isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(0, 0.25, 0)));
            event.setCancelled(true);
        }
    }
}
