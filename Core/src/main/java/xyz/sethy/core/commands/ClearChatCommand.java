package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 15/01/2017.
 */
public class ClearChatCommand extends CommandBase
{
    public ClearChatCommand()
    {
        super("clearchat", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player paramPlayer, Command paramCommand, String paramString, String[] paramArrayOfString)
    {
        int i = 0;
        while (i < 150)
        {
            i++;
            Bukkit.broadcastMessage(" ");
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7Chat has been cleared."));
    }
}
