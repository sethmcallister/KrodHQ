package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.sethy.sg.SG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sethm on 23/12/2016.
 */
public class PlayerInteractListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Player player = event.getPlayer();

            ItemStack hand = player.getItemInHand();
            if (hand.equals(SG.getInstance().getGameItems().getCompass()))
            {
                boolean found = false;
                for (int i = 0; i < 200; i++)
                {
                    List<Entity> entities = player.getNearbyEntities(i, 64, i);
                    for (Entity e : entities)
                    {
                        if (e.getType().equals(EntityType.PLAYER))
                        {
                            player.setCompassTarget(e.getLocation());
                            player.sendMessage(ChatColor.YELLOW + "Pointing at " + ((Player) e).getName() + "!");
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
            if (hand.equals(SG.getInstance().getGameItems().getDeathCompass()))
            {
                ArrayList<Player> players = new ArrayList<>();
                for (Player player1 : SG.getInstance().getPlayers())
                {
                    if (!SG.getInstance().getSpectatorHandler().isSpectator(player1))
                    {
                        players.add(player1);
                    }
                }
                Inventory inventory = Bukkit.createInventory(null, getInventorySize(players.size()), ChatColor.translateAlternateColorCodes('&', "&7Click a player to teleport to."));
                for (Player player1 : players)
                {
                    ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(player1.getName());
                    itemStack.setItemMeta(itemMeta);
                    inventory.addItem(itemStack);
                }
                player.openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();
        if (inventory.getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&7Click a player to teleport to.")))
        {
            int slot = event.getRawSlot();
            ItemStack slotItem = inventory.getItem(slot);
            if (slotItem != null)
            {
                String name = slotItem.getItemMeta().getDisplayName();
                for (Player player : SG.getInstance().getPlayers())
                {
                    if (player.getName().equalsIgnoreCase(name))
                    {
                        event.getWhoClicked().teleport(player);
                        ((Player) event.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have teleported to " + player.getName() + "."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event)
    {
        if (SG.getInstance().getSpectatorHandler().isSpectator(event.getPlayer()))
            event.setCancelled(true);
    }

    private int getInventorySize(int max)
    {
        if (max <= 0) return 9;
        int quotient = (int) Math.ceil(max / 9.0);
        return quotient > 5 ? 54 : quotient * 9;
    }
}
