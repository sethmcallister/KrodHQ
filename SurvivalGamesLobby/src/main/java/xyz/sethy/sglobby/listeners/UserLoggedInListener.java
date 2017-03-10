package xyz.sethy.sglobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.sethy.api.events.UserLoggedInEvent;
import xyz.sethy.sglobby.SGLobby;
import xyz.sethy.sglobby.items.LobbyItems;

/**
 * Created by sethm on 23/12/2016.
 */
public class UserLoggedInListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(UserLoggedInEvent event)
    {
        Player player = event.getPlayer();
        SGLobby.getInstance().getPlayers().add(player);

        player.getInventory().clear();
        LobbyItems lobbyItems = SGLobby.getInstance().getLobbyItems();

        player.getInventory().setItem(4, lobbyItems.getJoinQueue());
        player.getInventory().setItem(8, lobbyItems.getShowPlayers());

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        for (Player player1 : PlayerInteractListener.hidingPlayers)
        {
            player1.hidePlayer(player);
        }

        player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 52, 0.5));
    }

    @EventHandler
    public void onHungerChangeEvent(FoodLevelChangeEvent event)
    {
        event.setCancelled(true);
    }
}
