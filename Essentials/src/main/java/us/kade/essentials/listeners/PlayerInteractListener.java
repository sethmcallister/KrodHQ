package us.kade.essentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.kade.essentials.inventories.StaffmodeInventory;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

import java.util.Random;

/**
 * Created by sethm on 02/01/2017.
 */
public class PlayerInteractListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            ItemStack hand = event.getItem();
            User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
            if (hand != null && hand.equals(StaffmodeInventory.getMatchTeleportTool()))
            {
                if (user.getGroup().getPermission() < Group.TRAIL_MOD.getPermission())
                {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do this."));
                    event.setCancelled(true);
                    return;
                }
                int random = new Random().nextInt(Bukkit.getOnlinePlayers().length);
                Player player = (Player) Bukkit.getOnlinePlayers()[random];
                event.getPlayer().teleport(player);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been randomly teleported to &c" + player.getName() + "&7."));
                MessageUtil.sendStaffMessage(event.getPlayer(), "&7[&7&o" + event.getPlayer().getName() + ": &7You have randomly teleported to &c" + player.getName() + "&7.");
                return;
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event)
    {
        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        if (user.isStaffMode())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerPickupItemEvent event)
    {
        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        if (user.isStaffMode())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent event)
    {
        User user = API.getUserManager().findByUniqueId(event.getWhoClicked().getUniqueId());
        if (user.isStaffMode())
        {
            if (user.getGroup().getPermission() <= Group.ADMIN.getPermission())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStaffFreezeInteract(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() instanceof Player)
        {
            final ItemStack hand = event.getPlayer().getItemInHand();
            User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
            final Player touched = (Player) event.getRightClicked();
            if (hand.equals(StaffmodeInventory.getInventoryInspectTool()))
            {
                if (user.getGroup().getPermission() < Group.TRAIL_MOD.getPermission())
                {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do this."));
                    event.setCancelled(true);
                    return;
                }
                Inventory inventory = touched.getInventory();
                event.getPlayer().openInventory(inventory);
                event.getPlayer().sendMessage(ChatColor.GOLD + "Now inspecting " + ChatColor.AQUA + touched.getName() + ChatColor.GOLD + "'s Inventory.");
                return;
            }
            if (hand.equals(StaffmodeInventory.getFreezePlayerTool()))
            {
                if (user.getGroup().getPermission() < Group.TRAIL_MOD.getPermission())
                {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do this."));
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getServer().dispatchCommand(event.getPlayer(), "freeze " + touched.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event)
    {
        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        if (user.isStaffMode())
            event.setCancelled(true);
    }
}
