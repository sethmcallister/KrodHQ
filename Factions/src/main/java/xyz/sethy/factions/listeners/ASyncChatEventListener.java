package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.ChatMode;
import xyz.sethy.factions.dto.Faction;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 07/12/2016.
 */
public class ASyncChatEventListener implements Listener
{
    private static ConcurrentHashMap<Player, ChatMode> chatmodes;

    public ASyncChatEventListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
        chatmodes = new ConcurrentHashMap<>();
    }

    public static ConcurrentHashMap<Player, ChatMode> getChatmodes()
    {
        return chatmodes;
    }


    @EventHandler
    public void onASyncChatEvent(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
            return;

        ChatMode chatMode = chatmodes.get(event.getPlayer());
        if (chatMode == null)
            chatMode = ChatMode.PUBLIC;

        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(event.getPlayer());
        event.setCancelled(true);
        if (chatMode.equals(ChatMode.PUBLIC))
        {
            for (Player player : event.getRecipients())
            {
                if (faction == null)
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', user.getGroup().getName() + user.getName() + "&f: ") + event.getMessage());
                else
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[" + faction.getName(player) + "&6]" + user.getGroup().getName() + user.getName() + "&f: ") + event.getMessage());
            }

            if (!event.getRecipients().contains(event.getPlayer()))
            {
                if (faction == null)
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',user.getGroup().getName() + user.getName() + "&f: ") + event.getMessage());
                else
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[" + faction.getName(event.getPlayer()) + "&6]" + user.getGroup().getName() + user.getName() + "&f: ") + event.getMessage());
            }

            return;
        }
        if (chatMode.equals(ChatMode.FACTION))
        {
            if (faction == null)
            {
                for (Player player : event.getRecipients())
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', user.getGroup().getName() + user.getName() + "&f: ") + event.getMessage());
                return;
            }
            for (UUID uuid : faction.getOnlineMembers())
            {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&f<&2" + faction.getName() + " " + event.getPlayer().getName() + "&f>&2") + event.getMessage());
            }
        }
    }
}