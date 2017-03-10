package xyz.sethy.sg.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by sethm on 23/12/2016.
 */
public class GameItems
{
    private ItemStack compass;
    private ItemStack deathCompass;

    public GameItems()
    {
        this.compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = this.compass.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Tracker");
        this.compass.setItemMeta(meta);

        this.deathCompass = new ItemStack(Material.COMPASS, 1);
        ItemMeta c = this.compass.getItemMeta();
        c.setDisplayName(ChatColor.YELLOW + "Teleport to player.");
        this.deathCompass.setItemMeta(c);
    }

    public ItemStack getCompass()
    {
        return compass;
    }

    public ItemStack getDeathCompass()
    {
        return deathCompass;
    }
}
