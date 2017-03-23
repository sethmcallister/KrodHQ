package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.Claim;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.UUID;

/**
 * Created by sethm on 29/11/2016.
 */
public class FactionDisbandCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction disband"));
            return;
        }
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are currently not in a faction."));
            return;
        }
        if (!faction.isLeader(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must be a &cLeader&7 to disband this faction."));
            return;
        }

        for (UUID uuid : faction.getOnlineMembers())
        {
            Player player = Bukkit.getPlayer(uuid);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your faction has been disbanded by &c" + sender.getName() + "&7."));
        }
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The faction &r" + faction.getName() + "&7 has been disbanded by &c" + sender.getName() + "&7."));
        }
        int amount;
        if (faction.getClaims() != null)
            amount = faction.getBalance() + Claim.getPrice(faction.getClaims(), faction, false);
        else
            amount = faction.getBalance();

        Factions.getInstance().getLandBoard().clear(faction);
        Factions.getInstance().getFactionManager().removeFaction(faction);

        if (amount == 0)
            return;

        if (Factions.getInstance().isKitmap())
        {
            return;
        }
        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(sender.getUniqueId());
        hcfUser.setBalance(hcfUser.getBalance() + amount);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been given &c$" + amount + " because your faction was disbanded."));
    }
}
