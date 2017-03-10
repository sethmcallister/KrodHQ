package xyz.sethy.sg.timers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 21/12/2016.
 */
public class TimerHandler
{
    private final ConcurrentHashMap<Player, List<Timer>> timers;
    private long timeUntilBorder;

    public TimerHandler()
    {
        this.timers = new ConcurrentHashMap<>(10000);
    }

    public List<Timer> getActiveTimers(Player player)
    {
        List<Timer> activeTimers = new ArrayList<>();
        if (timers.get(player) == null)
        {
            timers.put(player, activeTimers);
            return activeTimers;
        }
        for (Timer timer : timers.get(player))
        {
            if (timer.getTime() > 0)
            {
                activeTimers.add(timer);
            }
        }
        return activeTimers;
    }

    public void addTimer(Timer timer, Player player)
    {
        this.timers.get(player).add(timer);
    }

    public Timer getTimerByType(Player player, TimerType timerType)
    {
        List<Timer> activeTimers = this.getActiveTimers(player);
        for (Timer timer : activeTimers)
        {
            if (timer.getTimerType().equals(timerType))
            {
                return timer;
            }
        }
        return null;
    }

    public boolean hasTimer(Player player, TimerType timerType)
    {
        List<Timer> activeTimers = this.getActiveTimers(player);
        for (Timer timer : activeTimers)
        {
            if (timer.getTimerType().equals(timerType))
            {
                return true;
            }
        }
        return false;
    }

    public long getTimeUntilBorder()
    {
        if (timeUntilBorder == Integer.MAX_VALUE)
        {
            return timeUntilBorder;
        }
        return timeUntilBorder - System.currentTimeMillis();
    }

    public void setTimeUntilBorder(long timeUntilBorder)
    {
        this.timeUntilBorder = timeUntilBorder;
    }

    public ConcurrentHashMap<Player, List<Timer>> getTimers()
    {
        return timers;
    }
}
