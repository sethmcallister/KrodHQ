package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 12/03/2017.
 */
public class AlertCommand extends CommandBase
{
    public AlertCommand()
    {
        super("alert", Group.MOD, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        String broadcast = StringUtils.join(args, " ", 0, args.length);
        API.sendBungeeMessage("&7(&cAlert&7)&f" + broadcast);
    }
}
