package xyz.sethy.sg.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;
import xyz.sethy.sg.tasks.PreGameTask;

/**
 * Created by sethm on 29/12/2016.
 */
public class ForceStartCommand extends CommandBase
{
    public ForceStartCommand()
    {
        super("forcestart", Group.ADMIN, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (SG.getInstance().getGameState().equals(GameState.INGAME))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe game has already started."));
            return;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7This game has been forcefully started by &3" + sender.getName() + "&7."));
        PreGameTask.forceStart = true;
    }
}
