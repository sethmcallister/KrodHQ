package us.kade.essentials.command;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 07/03/2017.
 */
public class RequestCommand extends CommandBase
{
    public RequestCommand()
    {
        super("request", Group.DEFAULT, true);
        Bukkit.getPluginCommand("request").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        String message = StringUtils.join(args, " ", 0, args.length);
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(Request) &f" + sender.getName() + ": ") + message);
            }
        }
    }
}
