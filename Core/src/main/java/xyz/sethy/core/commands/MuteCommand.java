package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.mute.MuteType;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.core.framework.mute.CoreMute;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Seth on 22/01/2017.
 */
public class MuteCommand extends CommandBase
{
    public MuteCommand()
    {
        super("mute", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);

    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        User banner = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (args.length == 0 || args.length > 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/mute <player> <reason>"));
            return;
        }

        String reason = StringUtils.join(args, " ", 1, args.length);
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No player with that name or UUID has been found."));
            return;
        }
        CoreMute mute = new CoreMute(player.getUniqueId().toString(), MuteType.NORMAL_PERMANENT, reason, sender.getUniqueId().toString());
        API.getMuteManager().addMute(mute);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been muted by &3" + banner.getName() + "&7 for &3" + mute.getReason() + "&7."));
        try
        {
            String towrite = "&7The player &3" + player.getName() + "&7 has been permanently muted.";

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
