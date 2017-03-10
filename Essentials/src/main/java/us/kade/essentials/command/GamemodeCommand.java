package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 20/08/2016.
 */
public class GamemodeCommand extends CommandBase
{
    public GamemodeCommand()
    {
        super("gamemode", Group.ADMIN, true);
        Bukkit.getPluginCommand("gamemode").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {

        if (args.length == 0)
        {
            showUsage(sender);
            return;
        }

        switch (args[0].toLowerCase())
        {
            case "0":
            case "s":
            case "survival":
                handleSurvival(sender);
                break;
            case "1":
            case "creative":
            case "c":
                handleCreative(sender);
                break;
            default:
                showUsage(sender);
                break;
        }

        return;
    }

    private void handleCreative(CommandSender sender)
    {
        Player p = (Player) sender;
        p.setGameMode(GameMode.CREATIVE);
        p.sendMessage(ChatColor.YELLOW + "You now have gamemode " + ChatColor.AQUA + "CREATIVE" + ChatColor.YELLOW + ".");
        MessageUtil.sendStaffMessage(p, "&7[&7&o" + p.getName() + ": &7You now have gamemode &bCREATIVE&7.");
    }

    private void handleSurvival(CommandSender sender)
    {
        Player p = (Player) sender;
        p.setGameMode(GameMode.SURVIVAL);
        p.sendMessage(ChatColor.YELLOW + "You now have gamemode " + ChatColor.AQUA + "SURVIVAL" + ChatColor.YELLOW + ".");
        MessageUtil.sendStaffMessage(p, "&7[&7&o" + p.getName() + ": &7You now have gamemode &bSURVIVAL&7.");
    }

    private void showUsage(CommandSender sender)
    {
        sender.sendMessage(ChatColor.RED + "Usage: /gamemode <creative, survival>");
    }
}
