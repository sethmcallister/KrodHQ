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
 * Created by Seth on 23/01/2017.
 */
public class FactionForceDisbandCommand implements ICommand
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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage:&7 /faction disband <factionName>"));
            return;
        }
        Faction faction = Factions.getInstance().getFactionManager().findByName(args[1]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No faction with with the name '" + args[1] + "' has been found."));
            return;
        }
        for (UUID uuid : faction.getOnlineMembers())
        {
            Player player = Bukkit.getPlayer(uuid);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your faction has been disbanded by &c" + sender.getName() + "&7."));
        }

        Factions.getInstance().getLandBoard().clear(faction);
        Factions.getInstance().getFactionManager().removeFaction(faction);
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The faction &r" + faction.getName() + "&7 has been forcefully disbanded by &c" + sender.getName() + "&7."));
        }
    }
}
