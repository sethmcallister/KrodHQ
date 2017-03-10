package us.kade.essentials.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.kade.essentials.Essentials;

/**
 * Created by sethm on 19/08/2016.
 */
public class StaffmodeInventory
{
    public static ItemStack getMatchTeleportTool()
    {
        ItemStack is = new ItemStack(Material.RECORD_3);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Teleport to Random Player " + ChatColor.GRAY + ChatColor.ITALIC + "(Right Click)");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getFreezePlayerTool()
    {
        ItemStack is = new ItemStack(Material.PACKED_ICE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Freeze Player " + ChatColor.GRAY + ChatColor.ITALIC + "(Right Click)");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getInventoryInspectTool()
    {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Inspect Inventory " + ChatColor.GRAY + ChatColor.ITALIC + "(Right Click)");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getStaffOnlineTool()
    {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, Essentials.getInstance().getOnlineStaff());
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Online Staff");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getCarpetTool()
    {
        ItemStack is = new ItemStack(Material.CARPET);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Please use this while recording. " + ChatColor.GRAY + ChatColor.ITALIC + "(Better Viewing)");
        is.setItemMeta(im);
        return is;
    }

    public static void givePlayerStaffItems(Player player)
    {
        player.getInventory().clear();
        player.getInventory().setItem(0, getCarpetTool());
        player.getInventory().setItem(1, getInventoryInspectTool());
        player.getInventory().setItem(4, getFreezePlayerTool());
        player.getInventory().setItem(7, getStaffOnlineTool());
        player.getInventory().setItem(8, getMatchTeleportTool());
        player.updateInventory();
    }

    public static void uploadOnline()
    {
        for (Player player : Essentials.getInstance().getStaffModeManager().getStaffModes())
        {
            player.getInventory().setItem(7, getStaffOnlineTool());
        }
    }
}
