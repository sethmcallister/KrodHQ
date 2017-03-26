package xyz.sethy.regularfactions.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;

/**
 * Created by Alex on 3/26/2017.
 */
public class WarpCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (command.getName().equalsIgnoreCase("setwarp")){
                if (!(commandSender instanceof Player)){
                    commandSender.sendMessage("§cConsole isn't eligable to set warps.");
                    return true;
                }
                Player player = (Player)commandSender;
                if (!player.hasPermission("warp.set")){
                    player.sendMessage("§cYou don't have enough permission to execute this command!");
                    return true;
                }
                if (strings.length == 0){
                    player.sendMessage("§cSyntax: /setwarp <warp>");
                    return true;
                }
                Location location = player.getLocation();
                API.getPlugin().getConfig().createSection(strings[0].toLowerCase());
                //Creates the section of the warp in config.
                ConfigurationSection cs = API.getPlugin().getConfig().getConfigurationSection(strings[0].toLowerCase());
                cs.set("X", location.getX());
                cs.set("Y", location.getY());
                cs.set("Z", location.getZ());
                cs.set("YAW", location.getYaw());
                cs.set("PITCH", location.getPitch());
                cs.set("world", location.getWorld().getName());
                API.getPlugin().saveConfig();
                // Save is completed and warp is created
                player.sendMessage("§aWarp §2" + strings[0] + "§a has been created at your location.");
            }
        return false;
    }
}
