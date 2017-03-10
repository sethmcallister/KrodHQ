package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 09/12/2016.
 */
public class FactionHomeCommand implements ICommand, Listener
{
    public static ConcurrentHashMap<Player, Integer> teleporting = new ConcurrentHashMap<>();

    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f home"));
            return;
        }
        final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are currently not in a faction."));
            return;
        }
        if (!sender.getWorld().getEnvironment().equals(World.Environment.NORMAL))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can only to go your faction's home in the over-world."));
            return;
        }

        Timer timer = Factions.getInstance().getTimerHandler().getTimer(sender, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot /f home while in combat."));
            return;
        }

        if (faction.getHome() == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour faction does not have a home set."));
            return;
        }
        Faction at = Factions.getInstance().getLandBoard().getFaction(sender.getLocation());
        if (at != null)
        {
            if (at.hasDTRBitmask(DTRType.SAFEZONE) || at.hasDTRBitmask(DTRType.ROAD) || at.hasDTRBitmask(DTRType.KOTH) || at.hasDTRBitmask(DTRType.CAP_ZONE))
            {
                startTeleprotation(sender, faction);
                return;
            }

            if (at.getOnlineMembers().contains(sender.getUniqueId()))
            {
                startTeleprotation(sender, faction);
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot f home in an enemies claim, please use /f stuck instead."));
            return;
        }
        startTeleprotation(sender, faction);
    }

    private void startTeleprotation(Player sender, Faction faction)
    {
        DefaultTimer timer = new DefaultTimer(TimerType.TELEPORT, 10000 + System.currentTimeMillis(), sender);
        Factions.getInstance().getTimerHandler().addTimer(sender, timer);
        teleporting.put(sender, Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new Runnable()
        {
            int i = 10;

            @Override
            public void run()
            {
                if (i != 0)
                {
                    sender.playSound(sender.getLocation(), Sound.NOTE_BASS_DRUM, 1f, 1f);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Teleporting to faction home in &c" + i + "&7 seconds."));
                    i--;
                    return;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Teleporting you to your faction's home."));
                sender.playSound(sender.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
                sender.teleport(faction.getHome());
                Bukkit.getScheduler().cancelTask(teleporting.get(sender));
                teleporting.remove(sender);
            }
        }, 0, 20));
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        if (teleporting.containsKey(player))
        {
            int runnable = teleporting.get(player);
            Bukkit.getScheduler().cancelTask(runnable);
            teleporting.remove(player);
            for (Timer defaultTimer : Factions.getInstance().getTimerHandler().getPlayerTimers(player))
            {
                if (defaultTimer.getTimerType().equals(TimerType.TELEPORT))
                {
                    Factions.getInstance().getTimerHandler().getPlayerTimers(player).remove(defaultTimer);
                }
            }
        }
    }
}
