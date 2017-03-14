package xyz.sethy.factions.commands.essentials.core.admin;

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
public class SetBalanceCommand extends CommandBase
{
    public SetBalanceCommand()
    {
        super("setbalance", Group.SR_MOD, true);
        Bukkit.getPluginCommand("setbalance").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/setbalance <amount>"));
            return;
        }
        if (!NumberUtils.isNumber(args[0]))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[0] + " is not a number."));
            return;
        }
        double amount = NumberUtils.createNumber(args[0]).doubleValue();
        HCFUser user = API.getUserManager().findByUniqueId(sender.getUniqueId()).getHCFUser();
        user.setBalance(amount);
    }
}
