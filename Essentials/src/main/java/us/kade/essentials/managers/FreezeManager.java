package us.kade.essentials.managers;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.kade.essentials.Essentials;
import us.kade.essentials.util.MessageUtil;

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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if(this.players.contains(event.getPlayer()))
        {
            MessageUtil.sendStaffMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &3" + event.getPlayer().getName() + "&7 has logged out while frozen."));
            FancyMessage fancyMessage = new FancyMessage();
            fancyMessage.then("Click ").color(ChatColor.GRAY)
                    .then("here ").color(ChatColor.DARK_AQUA).command("/ban MrLeafeh logging out while frozen.").tooltip("Click here to ban MrLeafeh for logging out while frozen").color(ChatColor.GOLD)
                    .then("to ban").color(ChatColor.GRAY)
                    .then(event.getPlayer().getName()).color(ChatColor.DARK_AQUA)
                    .then(" for logging out while frozen.").color(ChatColor.GRAY);
            MessageUtil.sendStaffMessage(fancyMessage);
        }
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            if(this.players.contains(event.getEntity()))
                event.setCancelled(true);
        }
        if(event.getDamager() instanceof Player)
        {
            if(this.players.contains(event.getDamager()))
                event.setCancelled(true);
        }
    }
}
