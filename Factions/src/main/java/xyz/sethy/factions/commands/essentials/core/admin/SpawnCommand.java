package xyz.sethy.factions.commands.essentials.core.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 16/03/2017.
 */
public class SpawnCommand extends CommandBase
{
    public SpawnCommand()
    {
        super("spawn", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand("spawn").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {

        sender.teleport(new Location(Bukkit.getWorld("world"), 0.5, 77, 0.5));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been teleported to spawn."));
    }
}
