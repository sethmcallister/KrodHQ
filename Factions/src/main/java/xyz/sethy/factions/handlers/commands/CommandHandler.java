package xyz.sethy.factions.handlers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by sethm on 29/11/2016.
 */
public class CommandHandler implements CommandExecutor
{
    private static HashMap<String, ICommand> commands = new HashMap<>();

    public void register(String name, ICommand cmd)
    {
        commands.put(name, cmd);
    }

    private boolean exists(String name)
    {
        return commands.containsKey(name);
    }

    private ICommand getExecutor(String name)
    {
        return commands.get(name);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member Help: &7/f help member"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR Help: &7/f help dtr"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Captain/Leader Help: &7/f help captain"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return true;
        }
        if (args.length > 0)
        {
            if (exists(args[0]))
            {
                getExecutor(args[0]).onCommand((Player) sender, command, label, args);
                return true;
            }
            else
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member Help: &7/f help member"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR Help: &7/f help dtr"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Captain/Leader Help: &7/f help captain"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
                return true;
            }
        }
        return false;
    }
}
