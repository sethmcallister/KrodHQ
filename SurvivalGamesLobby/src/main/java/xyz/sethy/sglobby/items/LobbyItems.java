package xyz.sethy.sglobby.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by sethm on 23/12/2016.
 */
public class LobbyItems
{
    private ItemStack joinQueue;
    private ItemStack leaveQueue;
    private ItemStack showPlayers;
    private ItemStack hidePlayers;

    public LobbyItems()
    {
        this.joinQueue = new ItemStack(Material.WATCH);
        ItemMeta j = this.joinQueue.getItemMeta();
        j.setDisplayName(ChatColor.GREEN + "Queue for a match.");
        this.joinQueue.setItemMeta(j);

        this.leaveQueue = new ItemStack(351, 1, (byte) 14);
        ItemMeta l = this.joinQueue.getItemMeta();
        l.setDisplayName(ChatColor.GREEN + "Leave the match queue.");
        this.leaveQueue.setItemMeta(l);

        this.showPlayers = new ItemStack(351, 1, (byte) 8);
        ItemMeta s = this.showPlayers.getItemMeta();
        s.setDisplayName(ChatColor.GREEN + "Show Players.");
        this.showPlayers.setItemMeta(s);

        this.hidePlayers = new ItemStack(351, 1, (byte) 10);
        ItemMeta h = this.hidePlayers.getItemMeta();
        h.setDisplayName(ChatColor.RED + "Hide Players.");
        this.hidePlayers.setItemMeta(h);
    }

    public ItemStack getJoinQueue()
    {
        return joinQueue;
    }

    public ItemStack getLeaveQueue()
    {
        return leaveQueue;
    }

    public ItemStack getShowPlayers()
    {
        return showPlayers;
    }

    public ItemStack getHidePlayers()
    {
        return hidePlayers;
    }
}
