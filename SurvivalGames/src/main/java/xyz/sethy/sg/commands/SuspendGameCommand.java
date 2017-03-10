package xyz.sethy.sg.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.sg.SG;

/**
 * Created by sethm on 29/12/2016.
 */
public class SuspendGameCommand extends CommandBase
{
    public SuspendGameCommand()
    {
        super("suspendgame", Group.ADMIN, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);

    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The game has been suspended by an &cAdminastor&7."));
        SG.getInstance().disable();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(SG.getInstance().getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                Bukkit.shutdown();
            }
        }, 4 * 20L);
    }
}
