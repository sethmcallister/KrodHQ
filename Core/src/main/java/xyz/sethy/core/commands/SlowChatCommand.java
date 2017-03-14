package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.core.Core;

/**
 * Created by Seth on 15/01/2017.
 */
public class SlowChatCommand extends CommandBase
{
    public SlowChatCommand()
    {
        super("slowchat", Group.MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage:&7 /slowchat <timeSeconds>"));
            return;
        }
        if (!StringUtils.isNumeric(args[0]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe argument '" + args[0] + "' is not a number."));
            return;
        }
        Integer time = Integer.valueOf(args[0]);
        Core.getInstance().getSlowTime().set(time);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The chat is now slowed for &3" + time + "&7 seconds."));
    }
}