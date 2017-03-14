package xyz.sethy.hub.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.hub.Hub;
import xyz.sethy.hub.queue.PlayerQueue;
import xyz.sethy.hub.server.Server;

import java.util.ArrayList;

;

/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerInteractListener implements Listener
{
    public static ArrayList<Player> hidingPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Player player = event.getPlayer();
            ItemStack hand = player.getItemInHand();
            if (hand.equals(Hub.getInstance().getHubItems().getHidePlayers()))
            {
                hidingPlayers.add(player);
                Hub.getInstance().getPlayers().stream().filter(player::canSee).forEach(player::hidePlayer);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can no longer see players."));
                player.getInventory().setItem(8, Hub.getInstance().getHubItems().getShowPlayers());
                player.updateInventory();
                event.setCancelled(true);
            }
            if (hand.equals(Hub.getInstance().getHubItems().getShowPlayers()))
            {
                hidingPlayers.remove(player);
                Hub.getInstance().getPlayers().stream().filter((player1) -> !player.canSee(player1)).forEach(player::showPlayer);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can now see players."));
                player.getInventory().setItem(8, Hub.getInstance().getHubItems().getHidePlayers());
                player.updateInventory();
                event.setCancelled(true);
            }
            if (hand.equals(Hub.getInstance().getHubItems().getServerClock()))
            {
                Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&3Select a server to join."));
                inventory.setItem(3, Hub.getInstance().getClockItems().getHcf(player));
                inventory.setItem(5, Hub.getInstance().getClockItems().getKitmap(player));

//                inventory.setItem(4, Hub.getInstance().getClockItems().getPractice());
//                inventory.setItem(6, Hub.getInstance().getClockItems().getSgLobby(player));
//                inventory.setItem(13, Hub.getInstance().getClockItems().getKitmap(player));
                player.openInventory(inventory);
                event.setCancelled(true);
            }
            if (hand.equals(Hub.getInstance().getHubItems().getLeaveQueue()))
            {
                Server server = Hub.getInstance().getPlayerQueue().whatServer(player);
                Hub.getInstance().getPlayerQueue().removeFromQueue(player);
                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getServerClock());
                player.updateInventory();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are no longer queueing for &3" + server.getDisplayName() + "&7."));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        if (inventory.getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3Select a server to join.")))
        {
            if (event.getRawSlot() == 3)
            {
                PlayerQueue queue = Hub.getInstance().getPlayerQueue();
                queue.addToQueue(Server.HCF, player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have joined the queue for &3Hardcore Factions&7 in position &3#" + queue.getPos(player) + "/" + queue.getQueueSize(Server.HCF) + "&7."));
                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user.getGroup().equals(Group.DEFAULT))
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You can purchase &3Krod&7 to obtain highest queue priority. &3http://store.KrodHQ.com/"));
                }
                event.setCancelled(true);
                player.closeInventory();
                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getLeaveQueue());
                player.updateInventory();
            }
            if (event.getRawSlot() == 5)
            {
                PlayerQueue queue = Hub.getInstance().getPlayerQueue();
                queue.addToQueue(Server.KITMAP, player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have joined the queue for &3Kits&7 in position &3#" + queue.getPos(player) + "/" + queue.getQueueSize(Server.KITMAP) + "&7."));
                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user.getGroup().equals(Group.DEFAULT))
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You can purchase &3Krod&7 to obtain highest queue priority. &3http://store.KrodHQ.com/"));
                }
                event.setCancelled(true);
                player.closeInventory();
                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getLeaveQueue());
                player.updateInventory();
            }
//            if (event.getRawSlot() == 4)
//            {
//                PlayerQueue queue = Hub.getInstance().getPlayerQueue();
//                queue.addToQueue(Server.PRACTICE, player);
//                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have joined the queue for &bPractice&5 in position &d#" + queue.getPos(player) + "/" + queue.getQueueSize(Server.PRACTICE) + "&5."));
//                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
//                if (user.getGroup().equals(Group.DEFAULT))
//                {
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You can purchase &3Platinum&5 to obtain highest queue priority."));
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Platinum&5 is purchasable from our store &dhttp://store.kihar.net/"));
//                }
//                event.setCancelled(true);
//                player.closeInventory();
//                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getLeaveQueue());
//                player.updateInventory();
//            }
//            if (event.getRawSlot() == 6)
//            {
//                PlayerQueue queue = Hub.getInstance().getPlayerQueue();
//                queue.addToQueue(Server.SG, player);
//                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have joined the queue for &7The Survival Games Lobby&5 in position &d#" + queue.getPos(player) + "/" + queue.getQueueSize(Server.SG) + "&5."));
//                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
//                if (user.getGroup().equals(Group.DEFAULT))
//                {
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You can purchase &3Platinum&5 to obtain highest queue priority."));
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Platinum&5 is purchasable from our store &dhttp://store.kihar.net/"));
//                }
//                event.setCancelled(true);
//                player.closeInventory();
//                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getLeaveQueue());
//                player.updateInventory();
//            }
//            if (event.getRawSlot() == 13)
//            {
//                PlayerQueue queue = Hub.getInstance().getPlayerQueue();
//                queue.addToQueue(Server.KITMAP, player);
//                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have joined the queue for &3Kit Map&5 in position &d#" + queue.getPos(player) + "/" + queue.getQueueSize(Server.KITMAP) + "&5."));
//                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
//                if (user.getGroup().equals(Group.DEFAULT))
//                {
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You can purchase &3Platinum&5 to obtain highest queue priority."));
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Platinum&5 is purchasable from our store &dhttp://store.kihar.net/"));
//                }
//                event.setCancelled(true);
//                player.closeInventory();
//                player.getInventory().setItem(4, Hub.getInstance().getHubItems().getLeaveQueue());
//                player.updateInventory();
//            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event)
    {
        event.setFoodLevel(20);
    }
}
