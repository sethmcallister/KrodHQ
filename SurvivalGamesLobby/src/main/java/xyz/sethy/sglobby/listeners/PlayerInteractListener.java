package xyz.sethy.sglobby.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.sglobby.SGLobby;
import xyz.sethy.sglobby.queue.PlayerQueue;

import java.util.LinkedList;

/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerInteractListener implements Listener
{
    public static LinkedList<Player> hidingPlayers = new LinkedList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Player player = event.getPlayer();

            ItemStack hand = player.getItemInHand();
            if (hand.equals(SGLobby.getInstance().getLobbyItems().getJoinQueue()))
            {
                if (SGLobby.getInstance().getPlayerQueue().isQueueing(player))
                {
                    player.getInventory().setItem(4, SGLobby.getInstance().getLobbyItems().getLeaveQueue());
                    return;
                }
                PlayerQueue playerQueue = SGLobby.getInstance().getPlayerQueue();
                playerQueue.addToQueue(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have joined the queue for a &7Survival-Games&5 game in position &d#" + playerQueue.getPos(player) + "/#" + playerQueue.getQueueSize() + "&5."));
                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user.getGroup().equals(Group.DEFAULT))
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You can purchase &3platinum&5 to obtain highest queue priority. \n&dhttp://store.kihar.net/"));
                }
                player.getInventory().setItem(4, SGLobby.getInstance().getLobbyItems().getLeaveQueue());
            }
            if (hand.equals(SGLobby.getInstance().getLobbyItems().getLeaveQueue()))
            {
                if (!SGLobby.getInstance().getPlayerQueue().isQueueing(player))
                {
                    player.getInventory().setItem(4, SGLobby.getInstance().getLobbyItems().getJoinQueue());
                    return;
                }
                PlayerQueue playerQueue = SGLobby.getInstance().getPlayerQueue();
                playerQueue.removeFromQueue(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have left the queue for a &7Survival-Games&5 game."));
                player.getInventory().setItem(4, SGLobby.getInstance().getLobbyItems().getJoinQueue());
            }
            if (hand.equals(SGLobby.getInstance().getLobbyItems().getShowPlayers()))
            {
                SGLobby.getInstance().getPlayers().forEach(player::showPlayer);
                hidingPlayers.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You are no longer hiding players."));
                player.getInventory().setItem(8, SGLobby.getInstance().getLobbyItems().getHidePlayers());
            }
            if (hand.equals(SGLobby.getInstance().getLobbyItems().getHidePlayers()))
            {
                SGLobby.getInstance().getPlayers().forEach(player::hidePlayer);
                hidingPlayers.add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You are now hiding players."));
                player.getInventory().setItem(8, SGLobby.getInstance().getLobbyItems().getShowPlayers());
            }
        }
    }
}
