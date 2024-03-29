package xyz.sethy.factions.commands.essentials.core;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerHandler;
import xyz.sethy.factions.timers.TimerType;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 27/12/2016.
 */
public class PvPCommand extends CommandBase
{
    private DecimalFormat FORMAT = new DecimalFormat("0");
    private final TimerHandler timerHandler;

    public PvPCommand()
    {
        super("pvp", Group.DEFAULT, true);
        this.timerHandler = Factions.getInstance().getTimerHandler();
        Bukkit.getPluginCommand("pvp").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if(Factions.getInstance().isKitmap())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is disabled during kitmap."));
            return;
        }

        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (args.length == 0)
        {
            handleHelp(sender, user);
            return;
        }
        switch (args[0].toLowerCase())
        {
            case "lives":
                handleLives(sender, user);
                break;
            case "time":
                handleTime(sender);
                break;
            case "revive":
                handleRevive(sender, user, args);
                break;
            case "sendlives":
                handleSendLives(sender, user, args);
                break;
            case "enable":
                handleEnable(sender, user);
                break;
            case "setlives":
                handleSetLives(sender, user, args);
            default:
                handleHelp(sender, user);
                break;
        }
    }

    private void handleEnable(Player sender, User user)
    {
        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(user.getUniqueId());
        if (hcfUser.getPvPTimer() > 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have PvP Protection."));
            return;
        }
        hcfUser.setPvPTimer(0L);
        Factions.getInstance().getTimerHandler().getPlayerTimers(sender).remove(Factions.getInstance().getTimerHandler().getTimer(sender, TimerType.PVP_TIMER));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have disabled your PvP Protection."));
    }

    private void handleSetLives(Player sender, User user, String[] args)
    {
        if (user.getGroup().getPermission() < Group.MOD.getPermission())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command."));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (!NumberUtils.isNumber(args[2]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c'" + args[2] + "' is not a number."));
            return;
        }
        Integer amount = NumberUtils.createInteger(args[2]);
        if (target == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            User user1 = API.getUserManager().getTempUser(offlinePlayer.getUniqueId());
            API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setLives(amount);
            user1.forceSave();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set &7" + user1.getName() + "&7's lives to &c" + amount + "&7."));
            return;
        }
        User user1 = API.getUserManager().findByUniqueId(target.getUniqueId());
        API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setLives(amount);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set &7" + user1.getName() + "&7's lives to &c" + amount + "&7."));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your lives have been set to &c" + amount + "&7."));
    }

    private void handleSendLives(Player sender, User user, String[] args)
    {
        Player target = Bukkit.getPlayer(args[1]);
        if (!NumberUtils.isNumber(args[2]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c'" + args[2] + "' is not a number."));
            return;
        }
        Integer amount = NumberUtils.createInteger(args[2]);
        if (API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() < amount)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough lives to do this."));
            return;
        }
        if (target == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            User user1 = API.getUserManager().getTempUser(offlinePlayer.getUniqueId());
            API.getUserManager().findHCFByUniqueId(user.getUniqueId()).setLives(API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() - amount);
            API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setLives(API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).getLives() + amount);
            user1.forceSave();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have sent &c" + user1.getName() + " " + amount + "&7 lives."));
            return;
        }
        User user1 = API.getUserManager().findByUniqueId(target.getUniqueId());
        API.getUserManager().findHCFByUniqueId(user.getUniqueId()).setLives(API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() - amount);
        API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setLives(API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).getLives() + amount);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have sent &c" + user1.getName() + " " + amount + "&7 lives."));
    }


    private void handleRevive(Player sender, User user, String[] args)
    {
        Player target = Bukkit.getPlayer(args[1]);
        if (args[2].equalsIgnoreCase("force"))
        {
            if (user.getGroup().getPermission() < Group.TRAIL_MOD.getPermission())
            {
                handleHelp(sender, user);
                return;
            }
            if (target == null)
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                User user1 = API.getUserManager().getTempUser(offlinePlayer.getUniqueId());
                if (API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() < 1)
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You do not have enough lives to revive this player."));
                    return;
                }
                API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setDeathbanTime(0L);
                API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).forceSave();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have successfully revived &7" + user1.getName() + "&3."));
                return;
            }
        }
        if (target == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            User user1 = API.getUserManager().getTempUser(offlinePlayer.getUniqueId());
            if (API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() < 1)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You do not have enough lives to revive this player."));
                return;
            }
            API.getUserManager().findHCFByUniqueId(user.getUniqueId()).setLives(API.getUserManager().findHCFByUniqueId(user.getUniqueId()).getLives() - 1);
            API.getUserManager().findHCFByUniqueId(user1.getUniqueId()).setDeathbanTime(0L);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have successfully revived &7" + user1.getName() + "&3."));
            return;
        }
    }

    private void handleTime(Player sender)
    {
        if (!timerHandler.hasTimer(sender, TimerType.PVP_TIMER))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You do not have PvP Timer."));
            return;
        }
        Timer defaultTimer = timerHandler.getTimer(sender, TimerType.PVP_TIMER);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You currently have &3" + this.formatTime(defaultTimer.getTime()) + "&7 left on your PvP Timer."));
        if (defaultTimer.isFrozen())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Your PvP Timer is currently frozen."));
        }
    }

    private void handleLives(Player player, User user)
    {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You currently have &7" + API.getUserManager().findHCFByUniqueId(player.getUniqueId()) + "&3."));
        return;
    }


    private void handleHelp(Player sender, User user)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3PvP Help"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp lives - check your lives."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp sendlives - send lives to another player."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp revive - revive another player (Uses a life.)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp time - check the remaining time on your PvP Timer."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp enable - remove your pvp timer."));
        if (user.getGroup().getPermission() > Group.ADMIN.getPermission())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3PvP Admin Help"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp setlives - set player's lives."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/pvp revive - forcefully revive a player."));
        }
    }

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private String formatTime(long time)
    {
        if (time > 60000L)
        {
            return setLongFormat(time);
        }
        else
        {
            return format(time);
        }
    }

    private String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
        {
            return FORMAT.format(paramMilliseconds);
        }
        return DurationFormatUtils.formatDuration(paramMilliseconds, (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }
}
