package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by Seth on 22/01/2017.
 */
public class InformationCommand extends CommandBase
{
    public InformationCommand()
    {
        super("information", Group.OWNER, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /information [user]");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        User user = API.getUserManager().findByUniqueId(target.getUniqueId());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInformation for User: &7" + user.getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3 Last Username&7:&7 " + user.getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3 UUID&7:&7 " + user.getUniqueId()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3 Last IP&7:&7 " + user.getLastIP()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3 Group&7:&7 " + user.getGroup()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
    }
}
