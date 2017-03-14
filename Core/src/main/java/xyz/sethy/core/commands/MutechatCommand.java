package xyz.sethy.core.commands;

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
public class MutechatCommand extends CommandBase
{
    public MutechatCommand()
    {
        super("mutechat", Group.MOD_PLUS, false);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (Core.getInstance().getChatMuted().get())
        {
            Core.getInstance().getChatMuted().set(false);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The chat has un-muted, you may speak again."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have un-muted the chat."));
            return;
        }
        Core.getInstance().getChatMuted().set(true);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The chat has muted, you cannot speak."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have muted the chat."));
        return;
    }
}