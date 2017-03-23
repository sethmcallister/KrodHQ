package xyz.sethy.factions.commands.faction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 09/12/2016.
 */
public class FactionDepositCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 2 || args.length == 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction deposit <amount>"));
            return;
        }
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You are currently not in a faction."));
            return;
        }
        HCFUser user = API.getUserManager().findHCFByUniqueId(sender.getUniqueId());

        if(args[1].equalsIgnoreCase("all"))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have deposited &d" + user.getBalance() + "&7 to your faction's balance."));
            faction.setBalance((int) (faction.getBalance() + user.getBalance()));
            user.setBalance(0);
            faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " has deposited &d" + user.getBalance() + "&7 into your faction's balance.")));
            return;
        }
        if (!StringUtils.isNumeric(args[1]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only deposit money to the faction balance."));
            return;
        }
        int amount = Integer.parseInt(args[1]);
        if (amount <= 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only deposit $0.0 or more."));
            return;
        }
        if (user.getBalance() < amount)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough money to do this."));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have deposited &d" + amount + "&7 to your faction's balance."));
        faction.setBalance(faction.getBalance() + amount);
        user.setBalance(user.getBalance() - amount);
        faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " has deposited &d" + amount + "&7 into your faction's balance.")));
    }
}
