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
public class FactionJoinCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction join [faction]"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        Faction factionName = Factions.getInstance().getFactionManager().findByName(args[1].toLowerCase());
        Faction factionPlayer = Factions.getInstance().getFactionManager().findByPlayer(player);


        if (factionName != null)
        {
            if (factionName.getInvites().contains(sender.getUniqueId()))
            {
                if(factionName.getAllMembers().size() == Factions.getInstance().getMaxFactionSize())
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The faction &c" + factionName.getName() + " is currently full.&7."));
                    return;
                }

                factionName.getInvites().remove(sender.getUniqueId());
                factionName.getOnlineMembers().add(sender.getUniqueId());
                factionName.getAllMembers().add(sender.getUniqueId());
                factionName.getMembers().add(sender.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have joined the faction &c" + factionName.getName() + "&7."));
                for (UUID u : factionName.getOnlineMembers())
                {
                    Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + sender.getName() + "&7 has joined your faction."));
                }
                return;
            }
        }

        if (factionPlayer != null)
        {
            if (factionPlayer.getInvites().contains(sender.getUniqueId()))
            {
                if(factionPlayer.getAllMembers().size() == Factions.getInstance().getMaxFactionSize())
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The faction &c" + factionName.getName() + " is currently full.&7."));
                    return;
                }
                factionPlayer.getInvites().remove(sender.getUniqueId());
                factionPlayer.getOnlineMembers().add(sender.getUniqueId());
                factionPlayer.getAllMembers().add(sender.getUniqueId());
                factionPlayer.getMembers().add(sender.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have joined the faction &c" + factionPlayer.getName() + "&7."));
                for (UUID u : factionPlayer.getOnlineMembers())
                {
                    Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The player &c" + sender.getName() + "&7 has joined your faction."));
                }
                return;
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a faction with the name or member of '" + args[1] + "'"));
    }
}
