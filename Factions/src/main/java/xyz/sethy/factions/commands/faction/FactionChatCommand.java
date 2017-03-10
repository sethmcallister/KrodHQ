package xyz.sethy.factions.commands.faction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.dto.ChatMode;
import xyz.sethy.factions.handlers.commands.ICommand;
import xyz.sethy.factions.listeners.ASyncChatEventListener;

/**
 * Created by sethm on 02/01/2017.
 */
public class FactionChatCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/faction chat <faction|public|ally>"));
            return;
        }
        switch (args[1].toLowerCase())
        {
            case "team":
            case "t":
            case "f":
            case "faction":
            {
                ASyncChatEventListener.getChatmodes().put(sender, ChatMode.FACTION);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now speaking in &cFaction&7 chat."));
                break;
            }
            case "p":
            case "public":
            case "global":
            {
                ASyncChatEventListener.getChatmodes().put(sender, ChatMode.PUBLIC);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now speaking in &cGlobal&7 chat."));
                break;
            }
        }
    }
}
