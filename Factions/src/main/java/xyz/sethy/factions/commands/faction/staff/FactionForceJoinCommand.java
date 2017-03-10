package xyz.sethy.factions.commands.faction.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.UUID;

/**
 * Created by Seth on 26/01/2017.
 */
public class FactionForceJoinCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (user.getGroup().getPermission() < Group.ADMIN.getPermission())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You do not have permission to execute this command."));
            return;
        }
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage:&7 /faction join <factionName>"));
            return;
        }
        Faction faction = Factions.getInstance().getFactionManager().findByName(args[1]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No faction with with the name '" + args[1] + "' has been found."));
            return;
        }
        faction.getOnlineMembers().add(sender.getUniqueId());
        faction.getAllMembers().add(sender.getUniqueId());
        faction.getMembers().add(sender.getUniqueId());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have forcefully joined the faction &c" + faction.getName() + "&7."));
        for (UUID u : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + sender.getName() + "&7 has forcefully joined your faction."));
        }
    }
}
