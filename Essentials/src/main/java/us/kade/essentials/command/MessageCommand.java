package us.kade.essentials.command;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import us.kade.essentials.managers.ConversationManager;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 25/09/2016.
 */
public class MessageCommand extends CommandBase
{
    public MessageCommand()
    {
        super("message", Group.DEFAULT, true);
        Bukkit.getPluginCommand("message").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        if (args.length == 0 || args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /message <player> <message>");
            return;
        }
        if (Bukkit.getPlayer(args[0]) != null)
        {
            Player target = Bukkit.getPlayer(args[0]);
            String message = StringUtils.join(args, " ", 1, args.length);

            ConversationManager.getInstance().setConversations(sender, target, message);
            return;
        }
        sender.sendMessage(ChatColor.RED + "That player is not online.");
        return;
    }
}
