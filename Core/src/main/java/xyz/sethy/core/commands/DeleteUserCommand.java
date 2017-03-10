package xyz.sethy.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 04/12/2016.
 */
public class DeleteUserCommand extends CommandBase
{
    public DeleteUserCommand()
    {
        super("deleteuser", Group.DEVELOPER, true);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /deleteuser [user]");
            return;
        }
        String id = args[0];
        API.getUserManager().deleteUser(id);
    }
}
