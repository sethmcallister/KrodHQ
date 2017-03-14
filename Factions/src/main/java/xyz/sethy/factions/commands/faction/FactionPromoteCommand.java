package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.UUID;

/**
 * Created by Seth on 12/03/2017.
 */
public class FactionPromoteCommand implements ICommand
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

        if (!faction.isLeader(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must at-least be a &cLeader&7 in your faction to promote players."));
            return;
        }

        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify what player you would like to promote."));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null)
        {
            OfflinePlayer player1 = Bukkit.getOfflinePlayer(args[1]);
            if (player1 == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player with the name &c" + args[1] + "&7 could not be found."));
                return;
            }

            if(faction.getOnlineMembers().contains(player1.getUniqueId()))
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7That player is ready an officer in your faction."));
                return;
            }

            faction.getAllMembers().remove(player1.getUniqueId());
            faction.getOnlineMembers().remove(player1.getUniqueId());
            faction.getMembers().remove(player1.getUniqueId());
            faction.getCaptains().add(player1.getUniqueId());

            for (UUID uuid : faction.getOnlineMembers())
            {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + player1.getName() + "&7 was promoted to an officer in your faction."));
            }
            return;
        }

        if(faction.getOnlineMembers().contains(player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7That player is ready an officer in your faction."));
            return;
        }

        faction.getAllMembers().remove(player.getUniqueId());
        faction.getOnlineMembers().remove(player.getUniqueId());
        faction.getMembers().remove(player.getUniqueId());
        faction.getCaptains().add(player.getUniqueId());

        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + "&7 was promoted to an officer in your faction."));
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have have been promoted to an officer in the faction &c" + faction.getName() + "&3."));
    }
}
