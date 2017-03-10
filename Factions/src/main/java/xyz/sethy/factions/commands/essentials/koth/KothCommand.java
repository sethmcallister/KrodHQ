package xyz.sethy.factions.commands.essentials.koth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.koth.dto.CoreKoth;
import xyz.sethy.factions.koth.dto.Koth;

import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 27/12/2016.
 */
public class KothCommand extends CommandBase
{
    public KothCommand()
    {
        super("koth", Group.ADMIN, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (args.length == 0)
        {
            handleUsage(sender);
            return;
        }
        switch (args[0].toLowerCase())
        {
            case "create":
                handleCreate(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            case "start":
                handleStart(sender, args);
                break;
            case "stop":
                handleStop(sender, args);
                break;
            default:
                handleUsage(sender);
                break;
        }
    }

    private void handleStop(Player sender, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please supply a koth name."));
            return;
        }
        String name = args[1];
        for (Koth koth : Factions.getInstance().getKothHandler().getActiveKoths())
        {
            if (koth.getName().get().equalsIgnoreCase(name))
            {
                Factions.getInstance().getKothHandler().getActiveKoths().remove(koth);
                koth.getCapper().set(null);
                koth.getTimeRemaining().set(0);

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&9&lKoTH&7] " + koth.getName() + "&3 has been suspended."));
            }
        }
    }

    private void handleStart(Player sender, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please supply a koth name."));
            return;
        }
        String name = args[1];
        Factions.getInstance().getKothHandler().getKoths().stream().filter(koth -> koth.getName().get().equalsIgnoreCase(name)).forEach(koth ->
        {
            koth.getStartTime().set(System.currentTimeMillis());
            koth.getTimeRemaining().set(600000);
            koth.getCapper().set(null);
            Factions.getInstance().getKothHandler().getActiveKoths().add(koth);

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███████&c█"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█████████"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&7█&3█&7█&3█&7█&3█&7█"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█ &3[King Of The Hill]"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3█&b█&3█&b█&3█&b█&3█&7█ "));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7█&3███████&7█ &3 KoTH " + koth.getName() + " can now be contested!"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7███&7███&7███ &3Capture time is 10:00!"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7████&7█&7████"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&7███&7█&7███&c█"));
        });
    }

    private void handleRemove(Player sender, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please supply a koth name."));
            return;
        }
        String name = args[1];
        Factions.getInstance().getKothHandler().getKoths().stream().filter(koth -> koth.getName().get().equalsIgnoreCase(name)).forEach(koth ->
        {
            Factions.getInstance().getKothHandler().getKoths().remove(koth);
        });

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have successfully removed the koth &7" + name + "&3."));
    }


    private void handleCreate(Player sender, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please supply a koth name. Must be the same as the corresponding faction name"));
            return;
        }
        String name = args[1];
        CoreKoth coreKoth = new CoreKoth();
        coreKoth.getName().set(name);
        Factions.getInstance().getKothHandler().getKoths().add(coreKoth);
        coreKoth.getTimeRemaining().set(TimeUnit.MINUTES.toMillis(5L));
        return;
    }


    private void handleUsage(Player sender)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/koth <create|delete|start|end|> <name>"));
        return;
    }
}
