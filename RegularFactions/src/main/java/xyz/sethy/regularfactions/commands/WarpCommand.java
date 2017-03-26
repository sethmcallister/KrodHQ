package xyz.sethy.regularfactions.commands;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Alex on 3/26/2017.
 */
public class WarpCommand extends CommandBase{

        public WarpCommand(){
            super("warp", Group.DEFAULT, true);
        }

     @Override
    public void execute(Player sender, Command command, String label, String[] args){
            if (args.length == 0){
                sender.sendMessage("§cSyntax: /warp <warp> \n§fValidated warps:");
                for (String key : API.getPlugin().getConfig().getKeys(false))
                {
                    sender.sendMessage(key.split(","));
                }
                return;
            }
            if (API.getPlugin().getConfig().getConfigurationSection(args[0].toLowerCase()) == null){
                sender.sendMessage("§2" + args[0] + "§a is not a existing warp.");
                return;
            }
         Configuration config = API.getPlugin().getConfig();
         ConfigurationSection cs = API.getPlugin().getConfig().getConfigurationSection(args[0].toLowerCase());
         World w = Bukkit.getWorld(API.getPlugin().getConfig().getString("world"));
         double x = config.getDouble("X");
         double y = config.getDouble("Y");
         double z = config.getDouble("Z");
         Location location = new Location(w, x, y, z);
         sender.teleport(location);
     }
}
