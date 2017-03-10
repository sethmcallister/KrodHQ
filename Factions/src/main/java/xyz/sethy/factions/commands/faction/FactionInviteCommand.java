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
 * Created by sethm on 29/11/2016.
 */
public class FactionInviteCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are not in a faction."));
            return;
        }

        if (!faction.isLeader(sender) || !faction.isCaptain(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must at-least be a &cCaptain&7 in your faction to invite other players."));
            return;
        }

        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify what player you would like to invite."));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null || !player.isOnline())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player with the name &c" + args[1] + "&7 could not be found."));
            return;
        }
        faction.getInvites().add(player.getUniqueId());

        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + "&7 has been invited to your faction."));
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have have been invited to join the faction &c" + faction.getName() + "&3."));
    }
}
