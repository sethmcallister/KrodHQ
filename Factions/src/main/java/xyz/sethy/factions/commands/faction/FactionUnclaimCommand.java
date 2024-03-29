package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.Claim;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 09/12/2016.
 */
public class FactionUnclaimCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f unclaimall"));
            return;
        }
        final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You are currently not in a faction."));
            return;
        }
        if (!faction.isLeader(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Only faction leaders can do this."));
            return;
        }
        if (faction.isRaidable())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot unclaim land while your faction is raidable."));
            return;
        }
        final int claims = faction.getClaims() == null ? 0 : 1;
        int refund = 0;
        refund += Claim.getPrice(faction.getClaims(), faction, false);
        final int finalRefund = refund;
        faction.setBalance(faction.getBalance() + refund);
        faction.setClaim(null);

        Factions.getInstance().getLandBoard().setFactionAt(null, faction);

        faction.setHome(null);
        faction.flagSave();

        faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your faction has been refunded &3$" + finalRefund + "&7.")));
        faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " has unclaimed all of your faction's land.")));
    }
}
