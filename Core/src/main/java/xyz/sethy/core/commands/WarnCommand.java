package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 12/03/2017.
 */
public class WarnCommand extends CommandBase
{
    public WarnCommand()
    {
        super("warn", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/tempmute <player> <time> [reason]"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        String reason = StringUtils.join(args, " ", 1, args.length);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been warned for &3" + reason + "&7."));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &3" + target.getName() + "&7 has been warned for &3" + reason + "&7."));
    }
}
