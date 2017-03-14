package xyz.sethy.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.SimplifiedBanType;
import xyz.sethy.core.framework.user.CoreUser;
import xyz.sethy.core.framework.user.UserManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by sethm on 04/12/2016.
 */
public class AsyncPlayerPreLoginListener implements Listener
{
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event)
    {
        Ban ban;
        try
        {
            ban = API.getBanManager().getBan(event.getUniqueId());
        }
        catch (ExecutionException | InterruptedException e)
        {
            ban = null;
            e.printStackTrace();
        }
        if (ban != null)
        {
            if(!ban.isActive())
            {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
                return;
            }
            if (ban.getSimplifiedType().equals(SimplifiedBanType.BLACKLIST))
            {
                String message = "\n" + ChatColor.YELLOW + "You have been blacklisted from " + ChatColor.DARK_AQUA + ChatColor.BOLD + "KrodHQ" + "\n" +
                        ChatColor.RED + "This blacklist was subject to admin discretion and cannot be lifted.";
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(message);
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
                if (ban.getExpireDate() < System.currentTimeMillis())
                {
                    message.append(ChatColor.YELLOW).append("This suspension will be automatically lifted at \n").append(ChatColor.GOLD).append(getConvertedTime(ban.getExpireDate()));

                }
                else
                {
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
                    return;
                }

            }
            message.append("\n").append("\n");
            message.append(ChatColor.RED).append("If you think that this ban was unjustified, or you want to appeal, please vist reddit.com/r/KrodHQ");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(message.toString());
            return;
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event)
    {
        UserManager manager = (UserManager) API.getUserManager();

        CoreUser user;
        try
        {
            user = manager.loadFromRedis(event.getPlayer().getUniqueId());
        }
        catch (ExecutionException | InterruptedException e)
        {
            user = null;
            e.printStackTrace();
        }

        manager.loadUser(user);
        assert user != null;
        user.setName(event.getPlayer().getName());
        user.setLastIP(event.getPlayer().getAddress().toString());
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
