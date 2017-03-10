package us.kade.essentials.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 19/08/2016.
 */
public class MessageUtil
{
    public static void sendStaffMessage(Player sender, String messsage)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (sender.equals(player))
                continue;

            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            if (user.getGroup().getPermission() > Group.TRAIL_MOD.getPermission())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', messsage)));
            }
        }
    }

    public static void sendStaffMessage(String messsage)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            if (user.getGroup().getPermission() > Group.TRAIL_MOD.getPermission())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', messsage)));
            }
        }
    }
}
