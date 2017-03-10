package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import us.kade.essentials.managers.FreezeManager;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 05/11/2016.
 */
public class FreezeCommand extends CommandBase
{
    public FreezeCommand()
    {
        super("freeze", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand("freeze").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /freeze <player>");
            return;
        }
        if (Bukkit.getPlayer(args[0]) == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player with the name or UUID of '" + args[0] + "' was found."));
            return;
        }
        if (Bukkit.getPlayer(args[0]) != null)
        {
            Player target = Bukkit.getPlayer(args[0]);
            ;
            if (!FreezeManager.getInstance().getPlayers().contains(target))
            {
                FreezeManager.getInstance().getPlayers().add(target);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have frozen &3" + target.getName() + "&7."));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You been frozen by&3 " + sender.getName() + "&7."));
                MessageUtil.sendStaffMessage(sender, "&7[&7&o" + sender.getName() + ": &7You have frozen &3" + target.getName() + "&7.");
                return;
            }
            FreezeManager.getInstance().getPlayers().remove(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have unfrozen &3" + target.getName() + "&7."));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You been unfrozen by&3 " + sender.getName() + "&7."));
            MessageUtil.sendStaffMessage(sender, "&7[&7&o" + sender.getName() + ": &7You have unfrozen &3" + target.getName() + "&7.");

            return;
        }

        return;
    }
}
