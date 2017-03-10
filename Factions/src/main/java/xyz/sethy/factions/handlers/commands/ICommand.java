package xyz.sethy.factions.handlers.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by sethm on 29/11/2016.
 */
public interface ICommand
{
    void onCommand(Player sender, Command command, String label, String[] args);
}