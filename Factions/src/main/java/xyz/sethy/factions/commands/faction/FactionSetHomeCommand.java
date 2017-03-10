package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 09/12/2016.
 */
public class FactionSetHomeCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f sethome"));
            return;
        }
        final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are currently not in a faction."));
            return;
        }
        if (faction.isLeader(sender) || faction.isCaptain(sender))
        {
            if (Factions.getInstance().getLandBoard().getFaction(sender.getLocation()) != faction)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only set your faction home in your own territory."));
                return;
            }
            faction.setHome(sender.getLocation());
            faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + sender.getName() + "&7 has updated your faction's home.")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have updated your faction home."));
        }
        else
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must be at-least a&c captain&7 in your faction to set the home."));
    }
}
