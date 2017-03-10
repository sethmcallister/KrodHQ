package xyz.sethy.factions.commands.faction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 29/11/2016.
 */
public class FactionShowCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction show [faction]"));
            return;
        }
        if (args.length == 1)
        {
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
            if (faction == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are currently not in a faction."));
                return;
            }
            faction.getInformation(sender);
            return;
        }

        Faction faction = Factions.getInstance().getFactionManager().findByAttribute(args[1]);

        if (args.length == 2)
        {
            if (faction == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo faction with the name or member of '" + args[1] + "' was found."));
                return;
            }
            faction.getInformation(sender);
        }
    }
}
