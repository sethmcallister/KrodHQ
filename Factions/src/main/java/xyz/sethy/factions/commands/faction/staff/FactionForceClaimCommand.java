package xyz.sethy.factions.commands.faction.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.VisualClaim;
import xyz.sethy.factions.dto.claim.VisualClaimType;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 02/12/2016.
 */
public class FactionForceClaimCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (user.getGroup().getPermission() < Group.STAFF_MANAGER.getPermission())
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
        sender.getInventory().addItem(Factions.getInstance().getItemHandler().selectionWand);
        new VisualClaim(sender, VisualClaimType.CREATE, true).draw(false);
        if (!VisualClaim.getCurrentMaps().containsKey(sender.getName()))
        {
            new VisualClaim(sender, VisualClaimType.MAP, true).draw(true);
        }
    }
}
