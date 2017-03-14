package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 12/03/2017.
 */
public class BroadcastCommand extends CommandBase
{
    public BroadcastCommand()
    {
        super("broadcast", Group.STAFF_MANAGER, true);
        Bukkit.getPluginCommand("broadcast").setExecutor(this);
    }

    @Override
    public void execute(Player player, Command paramCommand, String paramString, String[] args)
    {
        String broadcast = StringUtils.join(args, " ", 0, args.length);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(&cKrodHQ&7)&f" + broadcast));
    }
}
