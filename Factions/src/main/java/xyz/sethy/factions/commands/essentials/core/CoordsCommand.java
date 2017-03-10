package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 27/12/2016.
 */
public class CoordsCommand extends CommandBase
{
    public CoordsCommand()
    {
        super("coords", Group.DEFAULT, true);
        Bukkit.getPluginCommand("coords").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Map Coordinates"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSpawn &8- &c0, 65, 0 &7(Overworld)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNether Spawn &8- &c0, 32, 0 &7(Nether)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8End Portals &8- &c250, 70, 250 &7(Overworld - In Each Quadrant)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Mushroom Koth &8-&c 497, 79, 497 &7(Overworld)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Scenic Koth &8-&c -503, 68, -503 &7(Overworld)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3End Koth &8-&c -52, 66, 23 &7(End)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Heaven Koth &8-&c -3, 147, -3 &7(Heaven World)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Palace &8-&c -504, 66, -754 &7(Overworld)"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
    }
}
