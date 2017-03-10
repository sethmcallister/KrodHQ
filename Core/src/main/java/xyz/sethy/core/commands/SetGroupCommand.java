package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 23/12/2016.
 */
public class SetGroupCommand implements CommandExecutor
{
    public SetGroupCommand()
    {
//        super("setrank", Group.MANAGER, true);
        Bukkit.getPluginCommand("setrank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            if(API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup().getPermission() >= Group.PLATFORM_ADMIN.getPermission())
            {
                handleGroupSet(sender, args);
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
            return true;
        }

        handleGroupSet(sender, args);
        return false;
    }

    private void handleGroupSet(CommandSender sender, String[] args)
    {
        if (args.length == 0 || args.length == 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /setrank [user] [group]");
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is not online."));
            return;
        }
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Group group = Group.getByName(args[1].toUpperCase());

        if (group == null)
        {
            sender.sendMessage(ChatColor.RED + "Please use on of the listed groups:");

            StringBuilder builder = new StringBuilder();
            for (Group g : Group.values())
            {
                builder.append(g.name()).append(", ");
            }

            String result = builder.toString();

            if (result.charAt(result.length() - 1) == ',')
            {
                result = result.substring(0, result.length() - 1);
            }

            sender.sendMessage(ChatColor.RED + result);

            return;
        }

        user.setGroup(group);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set &3" + player.getName() + "&7's rank to &3" + group.getScoreboard() + "&7."));
    }
}
