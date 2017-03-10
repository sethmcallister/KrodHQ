package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by Seth on 22/01/2017.
 */
public class SBTypeCommand extends CommandBase
{
    public SBTypeCommand()
    {
        super("sbtype", Group.DEFAULT, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/sbtype <1|2>"));
            return;
        }
        if (args[0].equalsIgnoreCase("1"))
        {
            User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
            user.setSBType(1);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set your scoreboard type to 1."));

        }
        else if (args[0].equalsIgnoreCase("2"))
        {
            User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
            user.setSBType(2);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set your scoreboard type to 2."));
        }
        else
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/sbtype <1|2>"));
        }
    }
}
