package xyz.sethy.sg.timers;

import org.bukkit.entity.Player;

/**
 * Created by sethm on 21/12/2016.
 */
public class Timer
{
    private TimerType timerType;
    private Long time;
    private Player player;
    private boolean frozen;

    public Timer(TimerType timerType, Long time, Player player)
    {
        this.timerType = timerType;
        this.time = time;
        this.player = player;
        frozen = false;
    }

    public TimerType getTimerType()
    {
        return timerType;
    }

    public Long getTime()
    {
        if (frozen)
            return time;

        return time - System.currentTimeMillis();
    }

    public Player getPlayer()
    {
        return player;
    }

    public void freeze()
    {
        this.frozen = true;
    }

    public void unfreeze()
    {
        this.frozen = false;
    }

    public boolean isFrozen()
    {
        return frozen;
    }

}
