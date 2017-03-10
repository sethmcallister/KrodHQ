package xyz.sethy.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedInEvent;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.SimplifiedBanType;
import xyz.sethy.core.framework.user.CoreUser;
import xyz.sethy.core.framework.user.UserManager;

/**
 * Created by sethm on 04/12/2016.
 */
public class AsyncPlayerPreLoginListener implements Listener
{
    @EventHandler
    public void onPlayerPreLogin(PlayerJoinEvent event)
    {
        UserManager manager = (UserManager) API.getUserManager();

        CoreUser user = manager.loadFromRedis(event.getPlayer().getUniqueId());

        if (user == null)
        {
            user = new CoreUser(event.getPlayer().getUniqueId());
            manager.createUser(user);
            return;
        }

        Ban ban = API.getBanManager().getBan(event.getPlayer().getUniqueId());
        if (ban != null)
        {
            if (ban.getSimplifiedType().equals(SimplifiedBanType.BLACKLIST))
            {
                StringBuilder message = new StringBuilder();
                message.append("\n").append(ChatColor.YELLOW).append("You have been blacklisted from ").append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("KrodHQ").append("\n");
                message.append(ChatColor.RED).append("This blacklist was subject to admin discretion and cannot be lifted.");
                event.getPlayer().kickPlayer(message.toString());
                return;
            }
            StringBuilder message = new StringBuilder();
            message.append("\n").append(ChatColor.YELLOW).append("Your account has been banned from ").append(ChatColor.GOLD).append(ChatColor.BOLD).append("KrodHQ").append("\n");
            if (ban.getSimplifiedType().equals(SimplifiedBanType.PERMANENT))
            {
                message.append(ChatColor.YELLOW).append("This ban never expires");
            }
            if (ban.getSimplifiedType().equals(SimplifiedBanType.TEMPORARILY))
            {
                if (!ban.isActive())
                {
                    manager.loadUser(user);
                    return;
                }
                if (ban.getExpireDate().after(ban.getBanDate()))
                    message.append(ChatColor.YELLOW).append("This suspension will be automatically lifted at \n").append(ChatColor.GOLD).append(getConvertedTime(ban.getExpireDate().getTime() - System.currentTimeMillis()));
                else
                    manager.loadUser(user);

            }
            message.append("\n").append("\n");
            message.append(ChatColor.RED).append("If you think that this ban was unjustified, or you want to appeal, please vist reddit.com/r/KrodHQ");
            event.getPlayer().kickPlayer(message.toString());
            return;
        }
        manager.loadUser(user);
        user.setName(event.getPlayer().getName());
        user.setLastIP(event.getPlayer().getAddress().toString());
        Bukkit.getServer().getPluginManager().callEvent(new UserLoggedInEvent(user, event.getPlayer()));
    }

    private String getConvertedTime(long i)
    {
        i = Math.abs(i);
        final int hours = (int) Math.floor(i / 3600L);
        final int remainder = (int) (i % 3600L);
        final int minutes = remainder / 60;
        final int seconds = remainder % 60;
        if (seconds == 0 && minutes == 0)
        {
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + "0 seconds";
        }
        if (minutes == 0)
        {
            if (seconds == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
        }
        else if (seconds == 0)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
        }
        else if (seconds == 1)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
        }
        else
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            final String toReturn = String.format("%sm %ss", minutes, seconds);
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + " " + toReturn;
        }
    }
}
