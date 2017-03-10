package xyz.sethy.factions.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Seth on 07/03/2017.
 */
public class CrateManager
{
    public static ItemStack getStarterKey()
    {
        ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Starter " + ChatColor.YELLOW + "Key");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getElaph()
    {
        ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GRAY + "Elaph " + ChatColor.YELLOW + "Key");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getKrodKey()
    {
        ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.DARK_AQUA + "Krod " + ChatColor.YELLOW + "Key");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getKothKey()
    {
        ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "KoTH " + ChatColor.YELLOW + "Key");
        is.setItemMeta(im);
        return is;
    }
}
