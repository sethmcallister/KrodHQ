package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by Seth on 29/01/2017.
 */
public class SoundsCommand extends CommandBase
{
    public SoundsCommand()
    {
        super("sounds", Group.DEFAULT, true);
        Bukkit.getPluginCommand("sounds").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (user.hasPMSounds())
        {
            user.setPMSounds(false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Message Sounds:&c Disabled"));
            return;
        }
        user.setPMSounds(true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Message Sounds:&a Enabled"));
    }
}
