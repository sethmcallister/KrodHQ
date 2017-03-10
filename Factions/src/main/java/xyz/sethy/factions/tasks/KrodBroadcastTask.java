package xyz.sethy.factions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 04/01/2017.
 */
public class KrodBroadcastTask implements Runnable
{
    @Override
    public void run()
    {
        StringBuilder message = new StringBuilder();
        message.append("&3Online Krods&7: ");
        for(Player player : Bukkit.getOnlinePlayers())
        {
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            if(user.getGroup().equals(Group.KROD))
            {
                message.append("&3").append(player.getName()).append("&f, ");
            }
        }
        String broadcast = message.toString();
        broadcast = broadcast.trim();
        if (broadcast.endsWith(","))
        {
            broadcast = broadcast.substring(0, broadcast.length() - 1);
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast));
    }
}
