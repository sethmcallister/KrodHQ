package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 31/01/2017.
 */
public class LogoutCommand extends CommandBase implements Listener
{
    private static ConcurrentHashMap<Player, Integer> teleporting = new ConcurrentHashMap<>();

    public LogoutCommand()
    {
        super("logout", Group.DEFAULT, true);
        Bukkit.getPluginCommand("logout").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    private void startLogout(Player sender)
    {
        DefaultTimer timer = new DefaultTimer(TimerType.LOGOUT, 30000 + System.currentTimeMillis(), sender);
        Factions.getInstance().getTimerHandler().addTimer(sender, timer);
        teleporting.put(sender, Bukkit.getScheduler().scheduleSyncRepeatingTask(Factions.getInstance().getPlugin(), new Runnable()
        {
            int i = 30;

            @Override
            public void run()
            {
                if (i != 0)
                {
                    sender.playSound(sender.getLocation(), Sound.NOTE_BASS_DRUM, 1f, 1f);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Logging out in &c" + i + "&7 seconds."));
                    i--;
                    return;
                }
                sender.setMetadata("SageLogout", new FixedMetadataValue(API.getPlugin(), true));
                sender.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou have been successfully logged out."));
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
            Timer timer = Factions.getInstance().getTimerHandler().getTimer(player, TimerType.LOGOUT);
            Factions.getInstance().getTimerHandler().getPlayerTimers(player).remove(timer);
        }
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f home"));
            return;
        }

        Timer timer = Factions.getInstance().getTimerHandler().getTimer(sender, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot /logout while in combat."));
            return;
        }
        startLogout(sender);
    }
}
