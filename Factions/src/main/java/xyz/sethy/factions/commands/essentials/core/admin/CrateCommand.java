package xyz.sethy.factions.commands.essentials.core.admin;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.factions.managers.CrateManager;

/**
 * Created by Seth on 08/03/2017.
 */
public class CrateCommand implements CommandExecutor
{
    public CrateCommand()
    {
        Bukkit.getPluginCommand("crate").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            if(API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup().getPermission() >= Group.MOD.getPermission())
            {
                handleArguments(sender, args);
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
            return true;
        }

        handleArguments(sender, args);
        return true;
    }

    private void handleArguments(CommandSender sender, String[] args)
    {
        switch (args[0].toLowerCase())
        {
            case "giveall":
            {
                handleGiveAll(sender, args);
                break;
            }
            case "give":
            {
                handleGiveKey(sender, args);
                break;
            }
            default:
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /crate <give|giveall>"));
                break;
            }
        }
    }

    private void handleGiveKey(CommandSender sender, String[] args)
    {
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player with that Name or UUID has been found."));
            return;
        }
        switch (args[2].toLowerCase())
        {
            case "elaph":
            {
                if (!NumberUtils.isNumber(args[3]))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[2] + " is not a number."));
                    return;
                }
                int amount = NumberUtils.createNumber(args[3]).intValue();
                ItemStack key = CrateManager.getElaph();
                key.setAmount(amount);
                target.getInventory().addItem(key);
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been given &3" + amount + "x Elaph&7 key."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have given &3" + target.getName() + " " + amount + "x Elaph&7 key."));                break;
            }
            case "krod":
            {
                if (!NumberUtils.isNumber(args[3]))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[2] + " is not a number."));
                    return;
                }
                int amount = NumberUtils.createNumber(args[3]).intValue();
                ItemStack key = CrateManager.getKrodKey();
                key.setAmount(amount);
                target.getInventory().addItem(key);
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been given &3" + amount + "x Krod&7 key."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have given &3" + target.getName() + " " + amount + "x Krod&7 key."));
                break;
            }
        }
    }

    private void handleGiveAll(CommandSender sender, String[] args)
    {
        switch (args[1].toLowerCase())
        {
            case "elaph":
            {
                if (!NumberUtils.isNumber(args[2]))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[2] + " is not a number."));
                    return;
                }
                int amount = NumberUtils.createNumber(args[2]).intValue();
                ItemStack key = CrateManager.getElaph();
                key.setAmount(amount);
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    player.getInventory().addItem(key);
                }
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(&3&lKrodHQ&7)&7 Everybody has received &3" + amount + "x Elaph&7 key."));
                break;
            }
            case "krod":
            {
                if (!NumberUtils.isNumber(args[2]))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + args[2] + " is not a number."));
                    return;
                }
                int amount = NumberUtils.createNumber(args[2]).intValue();
                ItemStack key = CrateManager.getKrodKey();
                key.setAmount(amount);
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    player.getInventory().addItem(key);
                }
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(&3&lKrodHQ&7)&7 Everybody has received &3" + amount + "x Krod&7 key."));
                break;
            }
        }
    }
}
