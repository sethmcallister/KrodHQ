package xyz.sethy.factions.commands.essentials.core;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.hcf.HCFUser;

/**
 * Created by sethm on 27/12/2016.
 */
public class PayCommand extends CommandBase
{
    public PayCommand()
    {
        super("pay", Group.DEFAULT, true);
        Bukkit.getPluginCommand("pay").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/pay <player> <amount>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3A player with that name or UUID could not be found."));
            return;
        }

        if (!NumberUtils.isNumber(args[1]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[1] + " is not a number."));
            return;
        }
        double amount = NumberUtils.createNumber(args[1]).doubleValue();
        HCFUser hcfUser1 = API.getUserManager().findHCFByUniqueId(sender.getUniqueId());
        HCFUser hcfUser2 = API.getUserManager().findHCFByUniqueId(target.getUniqueId());

        if (hcfUser1.getBalance() < amount)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You do not have enough money to do this."));
            return;
        }

        hcfUser1.setBalance(hcfUser1.getBalance() - amount);
        hcfUser2.setBalance(hcfUser2.getBalance() + amount);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have paid&7 " + target.getName() + " &3$&7" + amount + "&3."));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + sender.getName() + " &3has sent you $&7" + amount + "&3."));
    }
}
