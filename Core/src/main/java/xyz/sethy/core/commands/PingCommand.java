package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 09/03/2017.
 */
public class PingCommand extends CommandBase
{
    public PingCommand()
    {
        super("ping", Group.DEFAULT, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);

    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        sender.sendMessage(ChatColor.GRAY + "Your ping is " + ChatColor.DARK_AQUA + ((CraftPlayer)sender).getHandle().ping);
    }
}
