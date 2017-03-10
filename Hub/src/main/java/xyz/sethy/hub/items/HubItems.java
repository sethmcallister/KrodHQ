package xyz.sethy.hub.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by sethm on 23/12/2016.
 */
public class HubItems
{
    private ItemStack serverClock;
    private ItemStack showPlayers;
    private ItemStack hidePlayers;
    private ItemStack leaveQueue;

    public HubItems()
    {
        this.serverClock = new ItemStack(Material.WATCH);
        ItemMeta sc = this.serverClock.getItemMeta();
        sc.setDisplayName(ChatColor.YELLOW + "Server Selector");
        this.serverClock.setItemMeta(sc);

        this.showPlayers = new ItemStack(351, 1, (byte) 8);
        ItemMeta s = this.showPlayers.getItemMeta();
        s.setDisplayName(ChatColor.GREEN + "Show Players");
        this.showPlayers.setItemMeta(s);

        this.hidePlayers = new ItemStack(351, 1, (byte) 10);
        ItemMeta h = this.hidePlayers.getItemMeta();
        h.setDisplayName(ChatColor.RED + "Hide Players");
        this.hidePlayers.setItemMeta(h);

        this.leaveQueue = new ItemStack(351, 1, (byte) 14);
        ItemMeta q = this.leaveQueue.getItemMeta();
        q.setDisplayName(ChatColor.RED + "Leave Queue");
        this.leaveQueue.setItemMeta(q);
    }

    public ItemStack getServerClock()
    {
        return serverClock;
    }

    public ItemStack getShowPlayers()
    {
        return showPlayers;
    }

    public ItemStack getHidePlayers()
    {
        return hidePlayers;
    }

    public ItemStack getLeaveQueue()
    {
        return leaveQueue;
    }
}
