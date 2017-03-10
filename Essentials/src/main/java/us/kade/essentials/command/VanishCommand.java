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
 * Created by Seth on 26/01/2017.
 */
public class VanishCommand extends CommandBase
{
    public VanishCommand()
    {
        super("vanish", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand("vanish").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (user.isVanished())
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!player.canSee(sender))
                    player.showPlayer(sender);
            }
            user.setVansihed(false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Vanish:&c Disabled"));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.canSee(sender))
                player.hidePlayer(sender);
        }
        user.setVansihed(true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Vanish:&a Enabled"));
    }
}
