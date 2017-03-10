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
 * Created by sethm on 05/12/2016.
 */
public class FactionForceLeaveCommand implements ICommand
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
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You are not in a faction."));
            return;
        }
        if (faction.isRaidable())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You cannot claim land while your faction is raidable."));
            return;
        }
        faction.setLeader(null);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have forcefully left the faction &7" + faction.getName() + "&3."));
        faction.getOnlineMembers().remove(sender.getUniqueId());
        faction.getMembers().remove(sender.getUniqueId());
        faction.getAllMembers().remove(sender.getUniqueId());

        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3The player &7" + sender.getName() + "&3 has forcefully left your faction."));
        }
    }
}
