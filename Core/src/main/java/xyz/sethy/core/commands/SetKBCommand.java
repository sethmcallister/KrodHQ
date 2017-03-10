package xyz.sethy.core.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.core.Core;

/**
 * Created by Seth on 22/01/2017.
 */
public class SetKBCommand extends CommandBase
{
    public SetKBCommand()
    {
        super("setkb", Group.ADMIN, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String label, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage("Usage: /" + label + " <horizontal multiplier> <vertical multiplier>.");
            return;
        }

        double horMultiplier = NumberUtils.toDouble(args[0], -1D);
        double verMultiplier = NumberUtils.toDouble(args[1], -1D);

        if (horMultiplier < 0D || verMultiplier < 0D)
        {
            sender.sendMessage(ChatColor.RED + "Invalid horizontal/vertical multiplier!");
            return;
        }

        Core.getInstance().setHorMultiplier(horMultiplier);
        Core.getInstance().setVerMultiplier(verMultiplier);

        sender.sendMessage(ChatColor.GREEN + "Successfully updated the knockback multipliers!");
    }
}
