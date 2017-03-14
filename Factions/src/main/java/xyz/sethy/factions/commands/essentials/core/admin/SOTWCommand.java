package xyz.sethy.factions.commands.essentials.core.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.TimerHandler;

/**
 * Created by sethm on 17/12/2016.
 */
public class SOTWCommand extends CommandBase
{
    public SOTWCommand()
    {
        super("sotw", Group.STAFF_MANAGER, true);
        Bukkit.getPluginCommand("sotw").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if(Factions.getInstance().isKitmap())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is disabled during kitmap."));
            return;
        }
        if (args.length != 1)
        {
            handleUsage(sender);
            return;
        }
        switch (args[0].toLowerCase())
        {
            case "start":
                handleStart();
                break;
            case "end":
                handleEnd();
                break;
            default:
                handleUsage(sender);
                break;
        }
    }

    private void handleUsage(CommandSender sender)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/sotw <start|end>"));
    }

    private void handleStart()
    {
        final TimerHandler timerHandler = Factions.getInstance().getTimerHandler();
        timerHandler.setSotw(true);
        timerHandler.setSotwTime(7200000L + System.currentTimeMillis());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███████&c█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█████████"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&7█&3█&7█&3█&7█&3█&7█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█ &3[Start Of The World]"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&b█&3█&b█&3█&b█&3█&7█ &3SOTW has just started!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7███&7███&7███"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7████&7█&7████"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███&7█&7███&c█"));
    }

    private void handleEnd()
    {
        final TimerHandler timerHandler = Factions.getInstance().getTimerHandler();
        timerHandler.setSotw(false);
        timerHandler.setSotwTime(0L);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███████&c█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█████████"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&7█&3█&7█&3█&7█&3█&7█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█ &3[Start Of The World]"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&b█&3█&b█&3█&b█&3█&7█ &3SOTW has just been stopped!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7███&7███&7███"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7████&7█&7████"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███&7█&7███&c█"));
    }
}
