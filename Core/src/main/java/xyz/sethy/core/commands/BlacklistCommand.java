package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.BanType;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.core.framework.ban.CoreBan;

/**
 * Created by sethm on 01/01/2017.
 */
public class BlacklistCommand implements CommandExecutor
{
    public BlacklistCommand()
    {
//        super("blacklist", Group.SR_ADMIN, false);
        Bukkit.getPluginCommand("blacklist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            if(API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup().getPermission() >= Group.PLATFORM_ADMIN.getPermission())
            {
                handleBlacklist(sender, args);
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
            return true;
        }

        handleBlacklist(sender, args);
        return true;
    }

    private void handleBlacklist(CommandSender sender, String[] args)
    {
        if (args.length == 0 || args.length > 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/blacklist <player> <reason>"));
            return;
        }

        String reason = StringUtils.join(args, " ", 1, args.length);
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if(offlinePlayer != null)
            {
                CoreBan ban = new CoreBan(offlinePlayer.getUniqueId().toString(), BanType.NORMAL_PERMANENT, reason, sender.getName());
                API.getBanManager().addBan(ban);
                API.sendBungeeMessage("&7The player &7" + offlinePlayer.getName() + "&7 has been permanently banned from &3KrodHQ&7.");
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No player with that name or UUID has been found."));
            return;
        }
        CoreBan ban = new CoreBan(player.getUniqueId().toString(), BanType.BLACKLIST, reason, sender.getName());
        API.getBanManager().addBan(ban);
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou have been blacklisted \n&fReason&7: " + reason + " \n&fBy&7: &3" + sender.getName()));
        API.sendBungeeMessage("&7The player &3" + player.getName() + "&7 has been blacklisted from &3KrodHQ&7.");
    }
}
