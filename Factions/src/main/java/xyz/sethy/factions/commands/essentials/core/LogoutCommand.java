package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 31/01/2017.
 */
public class LogoutCommand extends CommandBase
{
    public LogoutCommand()
    {
        super("logout", Group.DEFAULT, true);
    }

    @Override
    public void execute(Player paramPlayer, Command paramCommand, String paramString, String[] paramArrayOfString)
    {

    }
}
