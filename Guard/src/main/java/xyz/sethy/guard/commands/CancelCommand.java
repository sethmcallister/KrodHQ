package xyz.sethy.guard.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.guard.Guard;

/**
 * Created by Seth on 15/01/2017.
 */
public class CancelCommand extends CommandBase
{
    public CancelCommand()
    {
        super("cancel", Group.MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/cancel <player>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3A player with that name or UUID was not found."));
            return;
        }
        Guard.getInstance().getToCancel().add(target);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have canceled &3" + target + "&7's auto ban."));
    }
}
