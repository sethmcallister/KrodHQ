package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 15/01/2017.
 */
public class TeleportCommand extends CommandBase
{
    public TeleportCommand()
    {
        super("teleport", Group.MOD, true);
        Bukkit.getPluginCommand("teleport").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length == 0 || args.length > 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/teleport <player> [player]"));
            return;
        }
        if (args.length == 1)
        {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline())
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player with the name or UUID of '" + args[0] + "' was found."));
                return;
            }
            sender.teleport(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have teleported to &3" + target.getName() + "&7."));
            MessageUtil.sendStaffMessage(sender, "&7[&7&o" + sender.getName() + ": &7You have teleported to &3" + target.getName() + "&7.");
            return;
        }
        Player target1 = Bukkit.getPlayer(args[0]);
        if (target1 == null || !target1.isOnline())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player with the name or UUID of '" + args[0] + "' was found."));
            return;
        }
        Player target2 = Bukkit.getPlayer(args[1]);
        if (target2 == null || !target2.isOnline())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player with the name or UUID of '" + args[0] + "' was found."));
            return;
        }
        target1.teleport(target2);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have teleported &3" + target1.getName() + "&7 to &3" + target2.getName() + "&7."));
        MessageUtil.sendStaffMessage(sender, "&7[&7&o" + sender.getName() + ": &7You have teleported &3" + target1.getName() + "&7 to &3" + target2.getName() + "&7.");
    }
}
