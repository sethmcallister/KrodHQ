package xyz.sethy.api.framework.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;


/**
 * Created by sethm on 30/12/2016.
 */
public abstract class CommandBase implements CommandExecutor
{
    private String command;
    private Group group;
    private boolean isPlayerOnly;

    public CommandBase(String command, Group group, boolean playerOnly)
    {
        this.command = command;
        this.group = group;
        this.isPlayerOnly = playerOnly;
    }

    public boolean onCommand(CommandSender sender, Command command1, String label, String[] args)
    {
        if (command1.getName().equals(this.command))
        {
            if ((this.isPlayerOnly) && (!(sender instanceof Player)))
            {
                sender.sendMessage(ChatColor.RED + "Only players can execute the command '" + label + "'.");
                return true;
            }
            Player player = (Player) sender;
            if (player.isOp())
            {
                execute(player, command1, label, args);
                return true;
            }
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            if (user.getGroup().getPermission() < this.group.getPermission())
            {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
            execute(player, command1, label, args);
            return true;
        }
        return false;
    }

    public abstract void execute(Player paramPlayer, Command paramCommand, String paramString, String[] paramArrayOfString);

    public String getCommand()
    {
        return this.command;
    }

    public boolean isPlayerOnly()
    {
        return this.isPlayerOnly;
    }

    public Group getGroup()
    {
        return this.group;
    }
}
