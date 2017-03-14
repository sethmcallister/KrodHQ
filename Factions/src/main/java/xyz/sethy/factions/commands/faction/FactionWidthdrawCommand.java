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
 * Created by Seth on 10/03/2017.
 */
public class FactionWidthdrawCommand implements ICommand
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

        if(!faction.isCaptain(sender) || !faction.isLeader(sender))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be atleast a Captain to widthdraw from your faction."));
            return;
        }

        HCFUser user = API.getUserManager().findByUniqueId(sender.getUniqueId()).getHCFUser();

        if(args[1].equalsIgnoreCase("all"))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have withdrawn &d" + user.getBalance() + "&7 to your faction's balance."));
            user.setBalance(faction.getBalance());
            faction.setBalance(0);
            faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " has withdrawn &d" + user.getBalance() + "&7 into your faction's balance.")));
            return;
        }
        if (!StringUtils.isNumeric(args[1]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only withdrawn money to the faction balance."));
            return;
        }
        int amount = Integer.parseInt(args[1]);
        if (amount <= 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only withdrawn $0.0 or more."));
            return;
        }
        if (faction.getBalance() < amount)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough money to do this."));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have withdrawn &d" + amount + "&7 to your faction's balance."));
        faction.setBalance(faction.getBalance() - amount);
        user.setBalance(user.getBalance() + amount);
        faction.getOnlineMembers().forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " has withdrawn &d" + amount + "&7 into your faction's balance.")));
    }
}
