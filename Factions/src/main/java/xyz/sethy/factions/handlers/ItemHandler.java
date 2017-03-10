package xyz.sethy.factions.handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by sethm on 01/12/2016.
 */
public class ItemHandler
{
    public ItemStack selectionWand;
    public ItemStack wrench;

    public ItemHandler()
    {
        this.selectionWand = new ItemStack(Material.GOLD_HOE);
        ItemMeta sMeta = this.selectionWand.getItemMeta();
        sMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Claiming Wand"));

        this.wrench = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = this.wrench.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Spawner Wrench"));
        this.wrench.setItemMeta(meta);
    }
}
