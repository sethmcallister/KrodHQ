package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.sg.SG;

/**
 * Created by sethm on 23/12/2016.
 */
public class ASyncPlayerChatListner implements Listener
{
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (SG.getInstance().getSpectatorHandler().isSpectator(player))
        {
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[Spectator]&r" + user.getGroup().getName() + player.getName() + " &r" + event.getMessage()));
            event.setCancelled(true);
        }
    }
}