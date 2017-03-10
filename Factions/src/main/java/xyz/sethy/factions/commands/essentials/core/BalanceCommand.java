package xyz.sethy.factions.commands.essentials.core;

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
public class BalanceCommand extends CommandBase
{
    public BalanceCommand()
    {
        super("balance", Group.DEFAULT, true);
        Bukkit.getPluginCommand("balance").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        HCFUser hcfUser = API.getUserManager().findByUniqueId(sender.getUniqueId()).getHCFUser();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lBalance&7: &f" + hcfUser.getBalance()));
    }
}
