package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.core.framework.mute.MuteManager;

/**
 * Created by Seth on 22/01/2017.
 */
public class UnmuteCommand extends CommandBase
{
    public UnmuteCommand()
    {
        super("unmute", Group.MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/unmute <player> <reason>"));
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        MuteManager muteManager = (MuteManager) API.getMuteManager();
        if (muteManager.getMute(target.getUniqueId()) == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player has not been banned."));
            return;
        }
        muteManager.removeMute(target.getUniqueId());
        API.sendBungeeMessage("&7The player &3" + target.getName() + "&7 has been un-muted.");
    }
}
