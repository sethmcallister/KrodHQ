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
import xyz.sethy.core.Core;
import xyz.sethy.core.framework.mute.CoreMute;

import java.util.Date;

/**
 * Created by Seth on 12/03/2017.
 */
public class TempMuteCommand extends CommandBase
{
    public TempMuteCommand()
    {
        super("tempmute", Group.TRAIL_MOD, false);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length < 3)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/tempmute <player> <time> [reason]"));
            return;
        }

        String reason = StringUtils.join(args, " ", 3, args.length);
        Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No player with that name or UUID has been found."));
            return;
        }

        String timeVariable = args[1].replaceAll("[0-9]", "");
        if (!(timeVariable.equalsIgnoreCase("s")) && (!timeVariable.equalsIgnoreCase("m")) && (!timeVariable.equalsIgnoreCase("h")) && (!timeVariable.equalsIgnoreCase("d")))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/tempmute <s/m/h/d> <player> [reason]"));
            return;
        }

        Date now = new Date();

        long expire;

        try
        {
            expire = Core.getInstance().getDateUtils().parseDateDiff(args[1], false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            expire = now.getTime();
        }

        CoreMute coreMute = new CoreMute(player.getUniqueId().toString(), MuteType.NORMAL_TEMPORARILY, reason, sender.getName(), expire);
        API.getMuteManager().addMute(coreMute);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're account has been temporarily muted for &3" + reason + "&7."));
        API.sendBungeeMessage("&7The player &3" + player.getName() + "&7 has been temporarily muted from &3KrodHQ&7.");
    }
}
