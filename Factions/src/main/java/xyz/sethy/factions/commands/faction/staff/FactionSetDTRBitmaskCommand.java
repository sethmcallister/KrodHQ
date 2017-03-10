package xyz.sethy.factions.commands.faction.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;
import xyz.sethy.factions.handlers.dtr.DTRType;

/**
 * Created by sethm on 05/12/2016.
 */
public class FactionSetDTRBitmaskCommand implements ICommand
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

        if (args.length < 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction type <add|info|remove|> <faction>"));
            return;
        }
        switch (args[1].toLowerCase())
        {
            case "add":
                handleAdd(sender, args);
                break;
            case "info":
                handleInformation(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            default:
                return;
        }
    }

    private void handleAdd(final Player sender, final String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByName(args[2]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction with the name &7" + args[2] + "&3 could not be found."));
            return;
        }
        if (faction.getLeader() != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You can only do this with null factions."));
            return;
        }
        DTRType dtrType = DTRType.valueOf(args[3].toUpperCase());
        if (faction.hasDTRBitmask(dtrType))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction &7" + faction.getName() + "&3 already has the faction type of &7" + dtrType.getName() + "&3."));
            return;
        }
        int dtrInt = (int) Math.floor(faction.getDTR());
        dtrInt += dtrType.getBitmask();
        faction.setDtr((double) dtrInt);
    }

    private void handleInformation(final Player sender, final String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByName(args[2]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction with the name &7" + args[2] + "&3 could not be found."));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction &7" + faction.getName() + "&3 has the follow faction types:"));
        for (final DTRType dtrType : DTRType.values())
        {
            if (faction.hasDTRBitmask(dtrType))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l * &7" + dtrType.getName()));
        }
    }

    private void handleRemove(final Player sender, final String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByName(args[2]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction with the name &7" + args[2] + "&3 could not be found."));
            return;
        }
        if (faction.getLeader() != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You can only do this with null factions."));
            return;
        }
        DTRType dtrType = DTRType.valueOf(args[3].toUpperCase());
        if (!faction.hasDTRBitmask(dtrType))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The faction &7" + faction.getName() + "&3 doesn't has the faction type of &7" + dtrType.getName() + "&3."));
            return;
        }
        int dtrInt = (int) Math.floor(faction.getDTR());
        dtrInt -= dtrType.getBitmask();
        faction.setDtr((double) dtrInt);
    }
}
