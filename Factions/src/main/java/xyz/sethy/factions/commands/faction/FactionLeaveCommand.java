package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.UUID;

/**
 * Created by sethm on 02/01/2017.
 */
public class FactionLeaveCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f leave"));
            return;
        }

        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in a faction."));
            return;
        }
        if (faction.isLeader(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot leave a faction while you are leader."));
            return;
        }

        faction.getMembers().remove(sender.getUniqueId());
        faction.getAllMembers().remove(sender.getUniqueId());
        faction.getOnlineMembers().remove(sender.getUniqueId());
        if (faction.getCaptains().contains(sender.getUniqueId()))
        {
            faction.getCaptains().remove(sender.getUniqueId());
        }
        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + sender.getName() + "&7 has left your faction."));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have left the faction &c" + faction.getName() + "&7."));
    }
}
