package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import us.kade.essentials.Essentials;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by Seth on 22/01/2017.
 */
public class StaffChatCommand extends CommandBase
{
    public StaffChatCommand()
    {
        super("staffchat", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand("staffchat").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] paramArrayOfString)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (Essentials.getInstance().getStaffChatManager().getStaffModes().contains(sender))
        {
            Essentials.getInstance().getStaffChatManager().getStaffModes().remove(sender);
            user.setStaffChat(false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Staff Chat:&c Disabled"));
            return;
        }
        user.setStaffChat(true);
        Essentials.getInstance().getStaffChatManager().getStaffModes().add(sender);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Staff Chat:&a Enabled"));
    }
}
