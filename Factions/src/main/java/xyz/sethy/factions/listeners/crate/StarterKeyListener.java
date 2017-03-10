package xyz.sethy.factions.listeners.crate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.factions.managers.CrateManager;

import java.util.Random;

/**
 * Created by Seth on 07/03/2017.
 */
public class StarterKeyListener implements Listener
{
    public StarterKeyListener()
    {
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onStartKeyRedemption(PlayerInteractEvent event)
    {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST))
            {
                ItemStack hand = event.getPlayer().getItemInHand();
                if (hand.isSimilar(CrateManager.getStarterKey()))
                {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND, 1, 0);
                    handleCrateRedeem(event.getPlayer());
                    if(hand.getAmount() > 1)
                        hand.setAmount(hand.getAmount() - 1);
                    else
                        event.getPlayer().getInventory().remove(hand);

                    event.getPlayer().updateInventory();
                    event.setCancelled(true);
                }
            }
        }
    }

    private void handleCrateRedeem(Player player)
    {
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 0);
        Random items = new Random();
        for (int i = 0; i < 3; i++)
        {
            int item = items.nextInt(10) + 1;
            if(item == 1)
            {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 4));
                player.updateInventory();
            }
            else if (item == 2)
            {
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 7));
                player.updateInventory();
            }
            else if(item == 3)
            {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 3));
                player.updateInventory();
            }
            else if(item == 4)
            {
                player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));
                player.updateInventory();
            }
            else if(item == 5)
            {
                player.getInventory().addItem(new ItemStack(Material.SLIME_BALL, 2));
                player.updateInventory();
            }
            else if(item == 6)
            {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 2));
                player.updateInventory();
            }
            else if(item == 7)
            {
                player.getInventory().addItem(new ItemStack(Material.POTATO, 7));
                player.updateInventory();
            }
            else if(item == 8)
            {
                player.getInventory().addItem(new ItemStack(Material.CARROT, 8));
                player.updateInventory();
            }
            else if(item == 9)
            {
                player.getInventory().addItem(new ItemStack(Material.WHEAT, 10));
                player.updateInventory();
            }
            else if(item == 10)
            {
                player.getInventory().addItem(new ItemStack(Material.SEEDS, 13));
                player.updateInventory();
            }
            else if(item == 11)
            {
                player.getInventory().addItem(new ItemStack(Material.LOG, 8));
                player.updateInventory();
            }
        }
    }
}
