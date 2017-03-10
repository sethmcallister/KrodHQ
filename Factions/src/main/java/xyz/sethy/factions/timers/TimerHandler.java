package xyz.sethy.factions.timers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 05/12/2016.
 */
public class TimerHandler
{
    private final ConcurrentHashMap<Player, ArrayList<Timer>> timers;
    private boolean preSOTW;
    private boolean sotw;
    private long sotwTime;

    public TimerHandler()
    {
        this.timers = new ConcurrentHashMap<>();
    }

    public ArrayList<Timer> getPlayerTimers(Player player)
    {
        return this.timers.get(player);
    }

    public boolean hasTimer(Player player, TimerType timerType)
    {
        if (timers.get(player) == null)
            return false;
        for (Timer timer : timers.get(player))
        {
            if (timer.getTimerType().equals(timerType))
            {
                return true;
            }
        }
        return false;
    }

    public void addTimer(Player player, Timer defaultTimer)
    {
        if (timers.get(player) == null)
        {
            ArrayList<Timer> timersList = new ArrayList<>();
            timersList.add(defaultTimer);
            timers.put(player, timersList);
            return;
        }
        ArrayList<Timer> timersList = new ArrayList<>();
        timersList.addAll(timers.get(player));
        timersList.add(defaultTimer);
        timers.remove(player);
        timers.put(player, timersList);
    }

    public boolean hasActiveTimers(Player player)
    {
        if (this.timers.containsKey(player))
            return false;

        for (Timer timer : this.timers.get(player))
        {
            if (this.isSotw())
            {
                return true;
            }

            if (timer.getTime() > 0)
                return true;
        }
        return false;
    }

    public Timer getTimer(Player player, TimerType timerType)
    {
        if (timers.get(player) == null)
            return null;

        for (Timer defaultTimer : timers.get(player))
        {
            if (defaultTimer.getTimerType().equals(timerType))
            {
                return defaultTimer;
            }
        }
        return null;
    }

    public ConcurrentHashMap<Player, ArrayList<Timer>> getTimers()
    {
        return this.timers;
    }

    public boolean isSotw()
    {
        return sotw;
    }

    public void setSotw(boolean sotw)
    {
        this.sotw = sotw;
    }

    public long getSotwTime()
    {
        return sotwTime;
    }

    public void setSotwTime(long sotwTime)
    {
        this.sotwTime = sotwTime;
    }

    public boolean isPreSOTW()
    {
        return preSOTW;
    }

    public void setPreSOTW(boolean preSOTW)
    {
        this.preSOTW = preSOTW;
    }
}
