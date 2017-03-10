package us.kade.essentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import us.kade.essentials.Essentials;

import java.util.LinkedList;

/**
 * Created by sethm on 05/11/2016.
 */
public class FreezeManager implements Listener
{
    private static FreezeManager instance;

    private LinkedList<Player> players;

    public FreezeManager()
    {
        instance = this;
        this.players = new LinkedList<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, Essentials.getInstance().getPlugin());
    }

    public static FreezeManager getInstance()
    {
        return instance;
    }

    public LinkedList<Player> getPlayers()
    {
        return players;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (this.players.contains(player))
        {
            event.setTo(event.getFrom());
            player.sendMessage(ChatColor.WHITE + "▆▆▆▆▆▆▆▆▆");
            player.sendMessage(ChatColor.WHITE + "▇▇▇▇" + ChatColor.RED + "▇" + ChatColor.WHITE + "▇▇▇▇");
            player.sendMessage(ChatColor.WHITE + "▇▇▇" + ChatColor.RED + "▇" + ChatColor.GOLD + ChatColor.BLACK + "▇" + ChatColor.GOLD + ChatColor.RED + "▇" + ChatColor.WHITE + "▇▇▇");
            player.sendMessage(ChatColor.WHITE + "▇▇" + ChatColor.RED + "▇" + ChatColor.GOLD + "▇" + ChatColor.BLACK + "▇" + ChatColor.GOLD + "▇" + ChatColor.RED + "▇" + ChatColor.WHITE + "▇▇");
            player.sendMessage(ChatColor.WHITE + "▇▇" + ChatColor.RED + "▇" + ChatColor.GOLD + "▇" + ChatColor.BLACK + "▇" + ChatColor.GOLD + "▇" + ChatColor.RED + "▇" + ChatColor.WHITE + "▇▇ " + ChatColor.GOLD + "You have been frozen");
            player.sendMessage(ChatColor.WHITE + "▇▇" + ChatColor.RED + "▇" + ChatColor.GOLD + "▇" + ChatColor.BLACK + "▇" + ChatColor.GOLD + "▇" + ChatColor.RED + "▇" + ChatColor.WHITE + "▇▇ " + ChatColor.GOLD + "Please join our teamspeak");
            player.sendMessage(ChatColor.WHITE + "▇" + ChatColor.RED + "▇" + ChatColor.GOLD + "▇▇▇" + ChatColor.BLACK + ChatColor.GOLD + "▇▇" + ChatColor.RED + "▇" + ChatColor.WHITE + "▇ " + ChatColor.AQUA.toString() + "ts.KrodHQ.com");
            player.sendMessage(ChatColor.RED + "▇" + ChatColor.GOLD + "▇▇▇" + ChatColor.BLACK + "▇" + ChatColor.GOLD + "▇▇▇" + ChatColor.RED + "▇" + ChatColor.WHITE);
            player.sendMessage(ChatColor.RED + "▇▇▇▇▇▇▇▇▇");
            player.sendMessage(ChatColor.WHITE + "▇▇▇▇▇▇▇▇▇");
        }
    }
}
