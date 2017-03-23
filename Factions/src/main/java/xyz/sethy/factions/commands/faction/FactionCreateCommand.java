package xyz.sethy.factions.commands.faction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.regex.Pattern;

/**
 * Created by sethm on 29/11/2016.
 */
public class FactionCreateCommand implements ICommand
{
    private final Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You are already in a faction."));
            return;
        }
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Please specify a faction name."));
            return;
        }
        if (ALPHA_NUMERIC.matcher(args[1]).find())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFaction name must be alphanumeric."));
            return;
        }
        if (StringUtils.length(args[1]) > 16)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour faction name cannot be longer than 16 characters."));
            return;
        }
        if (StringUtils.length(args[1]) < 3)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour faction name must be at-least 3 characters."));
            return;
        }
        if (Factions.getInstance().getFactionManager().findByName(args[1]) != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cA faction with that name already exists."));
            return;
        }
        faction = new Faction(args[1], sender.getUniqueId());
        faction.flagSave();
        Factions.getInstance().getFactionManager().getFactions().add(faction);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c**" + sender.getName() + "&7 has created the faction &c" + faction.getName() + "&7."));
    }
}
