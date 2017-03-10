package xyz.sethy.factions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerHandler;

import java.util.ArrayList;

/**
 * Created by Seth on 29/01/2017.
 */
public class GarbageCollection extends BukkitRunnable
{
    private final TimerHandler timerHandler;

    public GarbageCollection()
    {
        this.timerHandler = Factions.getInstance().getTimerHandler();
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (timerHandler.getPlayerTimers(player) == null)
                continue;

            ArrayList<Timer> timers = timerHandler.getPlayerTimers(player);

            for (Timer timer : timers)
                if (timer.getTime() < 0)
                    timers.remove(timer);

            timerHandler.getTimers().put(player, timers);
        }
    }
}
