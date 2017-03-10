package us.kade.essentials.command;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.kade.essentials.managers.ConversationManager;

/**
 * Created by sethm on 25/09/2016.
 */
public class ReplyCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        Player player = (Player) sender;
        if (args.length == 0)
        {
            player.sendMessage(ChatColor.RED + "Usage: /reply <message>");
            return true;
        }

        String message = StringUtils.join(args, " ", 0, args.length);
        ConversationManager.getInstance().reply(player, message);
        return true;
    }
}
