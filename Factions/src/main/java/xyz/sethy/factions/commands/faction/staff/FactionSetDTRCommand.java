package xyz.sethy.factions.commands.faction.staff;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
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
 * Created by Seth on 12/03/2017.
 */
public class FactionSetDTRCommand implements ICommand
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
        if(args.length != 3)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Usage: /f setdtr <faction> <dtr>"));
            return;
        }
        Faction faction = Factions.getInstance().getFactionManager().findByAttribute(args[1]);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3No faction with with the name or member '" + args[1] + "' has been found."));
            return;
        }
        if(!StringUtils.isNumeric(args[2]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The argument &3'" + args[2] + "'&7 is not a number."));
            return;
        }
        float newDtr = Float.valueOf(args[2]);
        if(newDtr > faction.getMaxDTR())
            faction.setDtr((double) newDtr);
        else
            faction.setDtr((double) newDtr);

        faction.setDeathCooldown(0L);

        for (UUID uuid : faction.getOnlineMembers())
        {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your faction's DTR was set to &3" + newDtr + "&7."));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have set the faction &3" + faction.getName() + "&7 to &3" + newDtr + "&7."));
    }
}
