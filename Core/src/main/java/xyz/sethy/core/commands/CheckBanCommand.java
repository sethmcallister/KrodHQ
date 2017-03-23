package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

import java.util.concurrent.ExecutionException;

/**
 * Created by Seth on 20/03/2017.
 */
public class CheckBanCommand extends CommandBase
{
    public CheckBanCommand()
    {
        super("checkban", Group.MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if(args.length != 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /checkban <player>"));
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            Ban ban;
            try
            {
                ban = API.getBanManager().getBan(offlinePlayer.getUniqueId());
            }
            catch (ExecutionException | InterruptedException e)
            {
                ban = null;
                e.printStackTrace();
            }
            if(ban == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player has not been banned."));
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + offlinePlayer.getName() + "&&'s ban"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Active: &f" + ban.isActive()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Banner: &f" + Bukkit.getOfflinePlayer(ban.getBannedBy()).getName()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Ban Reason: &f" + ban.getReason()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Ban Date: &f" + ban.getBanDate().toGMTString()));
            return;
        }
        Ban ban;
        try
        {
            ban = API.getBanManager().getBan(player.getUniqueId());
        }
        catch (ExecutionException | InterruptedException e)
        {
            ban = null;
            e.printStackTrace();
        }
        if(ban == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player has not been banned."));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + player.getName() + "&&'s ban"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Active: &f" + ban.isActive()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Banner: &f" + ban.getBannedBy()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Ban Reason: &f" + ban.getReason()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8* &7Ban Date: &f" + ban.getBanDate().toGMTString()));
    }
}
