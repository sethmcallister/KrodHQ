package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by sethm on 23/12/2016.
 */
public class ChestHitListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!SG.getInstance().getGameState().equals(GameState.INGAME))
        {
            event.setCancelled(true);
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
        {
            if (event.getClickedBlock() != null)
            {
                Block block = event.getClickedBlock();
                if (block.getType().equals(Material.CHEST))
                {
                    Chest chest = (Chest) block.getState();
                    ItemStack[] items = chest.getInventory().getContents();
                    int i = 0;
                    while (i < items.length)
                    {
                        if (items[i] != null)
                        {
                            Bukkit.getServer().getWorld("world").dropItem(chest.getLocation(), items[i]);
                        }
                        i++;
                    }
                    chest.getInventory().clear();
                    chest.getBlock().setType(Material.AIR);
                    chest.getWorld().playSound(chest.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.2f, 1.0f);
                }
            }
        }
    }

    private Collection<ItemStack> getChestDrops()
    {
        Random rand = new Random();
        Collection<ItemStack> collection = new ArrayList<>();
        int w = 4;
        while (w > 0)
        {
            int i = rand.nextInt(14) + 1;
            if (i == 1)
            {
                int a = rand.nextInt(5) + 1;
                collection.add(new ItemStack(373, a, (byte) 16421));
            }
            else if (i == 2)
            {
                collection.add(new ItemStack(Material.DIAMOND_SWORD));
            }
            else if (i == 3)
            {
                collection.add(new ItemStack(Material.COOKED_FISH));
            }
            else if (i == 4)
            {
                collection.add(new ItemStack(Material.CAKE, 1));
            }
            else if (i == 5)
            {
                int a = rand.nextInt(5) + 1;
                collection.add(new ItemStack(Material.GLOWSTONE_DUST, a));
            }
            else if (i == 6)
            {
                int a = rand.nextInt(5) + 1;
                collection.add(new ItemStack(Material.SULPHUR, a));
            }
            else if (i == 7)
            {
                ItemStack itemStack = new ItemStack(Material.BOOK, 1);
                ItemMeta meta = itemStack.getItemMeta();
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                collection.add(itemStack);
            }
            else if (i == 8)
            {
                ItemStack itemStack = new ItemStack(Material.BOOK, 1);
                ItemMeta meta = itemStack.getItemMeta();
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                collection.add(itemStack);
            }
            else if (i == 9)
            {
                int a = rand.nextInt(9) + 5;
                collection.add(new ItemStack(Material.WEB, a));
            }
            else if (i == 10)
            {
                int a = rand.nextInt(7) + 3;
                collection.add(new ItemStack(Material.ENDER_PEARL, a));
            }
            else if (i == 11)
            {
                collection.add(new ItemStack(Material.FISHING_ROD, 1));
            }
            w--;
        }
        return collection;
    }


    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event)
    {
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
        {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().getItemInHand().setType(Material.AIR);
            return;
        }
    }
}
