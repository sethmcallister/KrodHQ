package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 27/12/2016.
 */
public class HelpCommand extends CommandBase
{
    public HelpCommand()
    {
        super("help", Group.DEFAULT, true);
        Bukkit.getPluginCommand("help").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInformation"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3World Border: &72000 Blocks"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Warzone: &7500 Blocks"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Teamspeak: &7ts.KrodHQ.com"));
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer Commands"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3View Kit:&7 /kit"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Factions Help:&7 /factions"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Koth Help:/f&7 /koth"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Lives Help:&7 /pvp"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Balance Help:&7 /balance : /pay"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Coords Help:&7 /coords"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Message Help:&7 /msg : /reply : /togglepms"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Staff Help:&7 /report : /request"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3List Help:&7 /who"));

        if (API.getUserManager().findByUniqueId(sender.getUniqueId()).getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
        {
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAdmin Commands"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Teleport Help:&7 /tp : /tphere"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Punishments Help:&7 /ipban : /ban : /tempban : /kick : /mute :/tempmute : /info : /ipcheck"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Chest Help&7: /clearchat : /mutechat : /slowchat"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Freeze Help&7: /freeze"));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
    }
}
