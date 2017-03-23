package xyz.sethy.factions.commands.essentials.core.admin;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.hcf.HCFUser;

/**
 * Created by Seth on 08/03/2017.
 */
public class AddLivesCommand implements CommandExecutor
{
    public AddLivesCommand()
    {
        Bukkit.getPluginCommand("addlives").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        Integer amount = NumberUtils.createInteger(args[1]);
        if (target == null)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            HCFUser user = API.getUserManager().findHCFByUniqueId(offlinePlayer.getUniqueId());
            user.setLives(user.getLives() + amount);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have sent &c" + offlinePlayer.getName() + " " + amount + "&7 lives."));
            return true;
        }
        HCFUser user = API.getUserManager().findHCFByUniqueId(target.getUniqueId());
        user.setLives(user.getLives() + amount);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have sent &c" + target.getName() + " " + amount + "&7 lives."));
        return true;
    }
}
