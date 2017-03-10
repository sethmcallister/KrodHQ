package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.BanType;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.core.framework.ban.CoreBan;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by sethm on 01/01/2017.
 */
public class BanCommand extends CommandBase
{
    public BanCommand()
    {
        super("ban", Group.MOD, false);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        User banner = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (args.length == 0 || args.length > 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/ban <player> <reason>"));
            return;
        }

        String reason = StringUtils.join(args, " ", 1, args.length);
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No player with that name or UUID has been found."));
            return;
        }
        CoreBan ban = new CoreBan(player.getUniqueId().toString(), BanType.NORMAL_PERMANENT, reason, sender.getUniqueId().toString());
        API.getBanManager().addBan(ban);

        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou're account has been permanently suspended \n&fReason&7: " + reason + "\n&fBy&7: &3" + banner.getName()));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try
        {
            String towrite = "&7The player &7" + player.getName() + "&7 has been permanently banned from &3KrodHQ&7.";

            out.writeUTF("SendMessage");
            out.writeUTF(towrite);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        sender.sendPluginMessage(API.getPlugin(), "SendMessage", stream.toByteArray());
        return;
    }
}
