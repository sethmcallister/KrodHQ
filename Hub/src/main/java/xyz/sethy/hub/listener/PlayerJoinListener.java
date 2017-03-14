package xyz.sethy.hub.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.sethy.hub.Hub;


/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.getInventory().clear();
        player.getInventory().setItem(4, Hub.getInstance().getHubItems().getServerClock());
        player.getInventory().setItem(8, Hub.getInstance().getHubItems().getHidePlayers());
        player.updateInventory();

        Hub.getInstance().getPlayers().add(player);
        player.teleport(new Location(Bukkit.getWorld("world"), 20.5, 31, -21.5));

        for (Player player1 : PlayerInteractListener.hidingPlayers)
        {
            player1.hidePlayer(player);
        }
        event.setJoinMessage(null);
    }
}
