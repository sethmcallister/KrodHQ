package xyz.sethy.regularfactions.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Alex on 3/26/2017.
 */
public class SetWarpCommand extends CommandBase{

    public SetWarpCommand(){
        super("setwarp", Group.DEVELOPER, true);
    }

    @Override
    public void execute(Player commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("setwarp"))
        {
            if (strings.length == 0){
                commandSender.sendMessage("§cSyntax: /setwarp <warp>");
                return;
            }
            Location location = commandSender.getLocation();
            API.getPlugin().getConfig().createSection(strings[0].toLowerCase());
            //Creates the section of the warp in config.
            ConfigurationSection cs = API.getPlugin().getConfig().getConfigurationSection(strings[0].toLowerCase());
            cs.set("X", location.getX());
            cs.set("Y", location.getY());
            cs.set("Z", location.getZ());
            cs.set("world", location.getWorld().getName());
            API.getPlugin().saveConfig();
            // Save is completed and warp is created
            commandSender.sendMessage("§aWarp §2" + strings[0] + "§a has been created at your location.");
        }
    }
}
