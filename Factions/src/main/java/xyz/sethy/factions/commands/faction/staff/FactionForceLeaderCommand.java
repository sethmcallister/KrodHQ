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
 * Created by Seth on 30/01/2017.
 */
public class FactionForceLeaderCommand implements ICommand
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
        if (args.length > 2 || args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage:&7 /faction forceleader [player]"));
            return;
        }
        if (args.length == 1)
        {
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
            if (faction == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in a faction."));
                return;
            }
            if (faction.getLeader() != null)
                faction.getCaptains().add(faction.getLeader());
            faction.setLeader(sender.getUniqueId());
            for (UUID uuid : faction.getOnlineMembers())
            {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + sender.getName() + "&7 has &3forcefully&7 made himself the leader of the faction."));
            }
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo faction with the name or UUID of '" + args[1] + "' was found."));
            return;
        }

        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in a faction."));
            return;
        }

        if (!faction.getAllMembers().contains(target.getUniqueId()))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe player '" + args[1] + "' is not in your faction."));
            return;
        }

        if (faction.getLeader() != null)
            faction.getCaptains().add(faction.getLeader());
        faction.setLeader(target.getUniqueId());
        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + sender.getName() + "&7 has &3forcefully&7 made &3" + target.getName() + "&7 the leader of the faction."));
        }
    }
}
