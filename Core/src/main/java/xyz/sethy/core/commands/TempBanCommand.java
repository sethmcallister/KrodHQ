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
import xyz.sethy.core.Core;
import xyz.sethy.core.framework.ban.CoreBan;

import java.util.Date;

/**
 * Created by sethm on 01/01/2017.
 */
public class TempBanCommand extends CommandBase
{
    public TempBanCommand()
    {
        super("tempban", Group.TRAIL_MOD, false);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        User banner = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (args.length < 3)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/tempban <player> <time> [reason]"));
            return;
        }

        String reason = StringUtils.join(args, " ", 2, args.length);
        Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No player with that name or UUID has been found."));
            return;
        }

        String timeVariable = args[1].replaceAll("[0-9]", "");
        if (!(timeVariable.equalsIgnoreCase("s")) && (!timeVariable.equalsIgnoreCase("m")) && (!timeVariable.equalsIgnoreCase("h")) && (!timeVariable.equalsIgnoreCase("d")))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/tempban <s/m/h/d> <player> [reason]"));
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

        CoreBan ban = new CoreBan(player.getUniqueId().toString(), BanType.NORMAL_TEMPORARILY, reason, sender.getName(), expire);
        API.getBanManager().addBan(ban);

        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou're account has been temporarily suspended \n&fReason&7: &3" + reason + " \n&fBy&7: &3" + banner.getName()));
        API.sendBungeeMessage("&7The player &3" + player.getName() + "&7 has been temporarily banned from &3KrodHQ&7.");
        return;
    }
}