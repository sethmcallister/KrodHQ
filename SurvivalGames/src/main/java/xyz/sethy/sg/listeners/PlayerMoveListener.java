package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.sethy.sg.SG;

/**
 * Created by sethm on 22/12/2016.
 */
public class PlayerMoveListener implements Listener
{
    public PlayerMoveListener()
    {
        Bukkit.getPluginManager().registerEvents(this, SG.getInstance().getPlugin());
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        final Location location = player.getLocation();
        final int BORDER = SG.getInstance().getBorderSize();


        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())
        {
            Location newLocation = location.clone();
            while (Math.abs(location.getX()) > BORDER)
            {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BORDER)
            {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            if (!newLocation.equals(location))
            {
                player.teleport(newLocation);
            }
        }
    }
}
