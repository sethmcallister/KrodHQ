package xyz.sethy.factions.listeners.crate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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
public class ElaphKeyListener implements Listener
{
    public ElaphKeyListener()
    {
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onElaphKeyRedemption(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if (event.getClickedBlock().getType().equals(Material.ENDER_CHEST))
            {
                ItemStack hand = event.getPlayer().getItemInHand();
                if (hand.isSimilar(CrateManager.getElaph()))
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
        Random itemscoms = new Random();
        for (int i = 0; i < 3; i++)
        {
            int itemscom = itemscoms.nextInt(14) + 1;
            if (itemscom == 1)
            {
                player.getInventory().addItem(new ItemStack(Material.SULPHUR, 12));
            }
            else if (itemscom == 2)
            {
                player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 4));
            }
            else if (itemscom == 3)
            {
                player.getInventory().addItem(new ItemStack(Material.NETHER_STALK, 5));
            }
            else if (itemscom == 4)
            {
                player.getInventory().addItem(new ItemStack(Material.SLIME_BALL, 16));
            }
            else if (itemscom == 5)
            {
                player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, 5));
            }
            else if (itemscom == 6)
            {
                player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, 5));
            }
            else if (itemscom == 7)
            {
                player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, 5));
            }
            else if (itemscom == 8)
            {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 5));
            }
            else if (itemscom == 9)
            {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PORTAL_FRAME, 2));
            }
            else if (itemscom == 10)
            {
                ItemStack sharp1 = new ItemStack(Material.DIAMOND_SWORD, 1);
                sharp1.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                player.getInventory().addItem(sharp1);
            }
            else if (itemscom == 11)
            {
                player.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 12));
            }
            else if (itemscom == 12)
            {
                ItemStack sharp1 = new ItemStack(Material.DIAMOND_SWORD, 1);
                sharp1.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                player.getInventory().addItem(sharp1);
            }
            else if (itemscom == 13)
            {
                ItemStack sharp1 = new ItemStack(Material.DIAMOND_SWORD, 1);
                sharp1.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
                player.getInventory().addItem(sharp1);
            }
            else if (itemscom == 14)
            {
                ItemStack sharp1 = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                sharp1.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
                player.getInventory().addItem(sharp1);
            }
        }
    }
}
