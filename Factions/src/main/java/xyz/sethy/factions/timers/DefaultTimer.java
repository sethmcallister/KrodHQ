package xyz.sethy.factions.timers;

import org.bukkit.entity.Player;

/**
 * Created by sethm on 05/12/2016.
 */
public class DefaultTimer implements Timer
{
    private TimerType timerType;
    private Long time;
    private Player player;
    private boolean frozen;
    private Integer tag = 1;
    private Long frozenAt;

    public DefaultTimer(TimerType timerType, Long time, Player player)
    {
        this.timerType = timerType;
        this.time = time;
        this.player = player;
        frozen = false;
    }

    @Override
    public TimerType getTimerType()
    {
        return timerType;
    }

    @Override
    public Long getTime()
    {
        return time - System.currentTimeMillis();
    }

    @Override
    public Player getPlayer()
    {
        return player;
    }

    @Override
    public Integer getTagLevel()
    {
        return tag;
    }

    @Override
    public void setTime(Long time)
    {
        this.time = time;
    }

    @Override
    public void freeze()
    {
        this.frozen = true;
        this.frozenAt = System.currentTimeMillis();
    }

    @Override
    public void unfreeze()
    {
        this.frozen = false;
        long add = System.currentTimeMillis() - frozenAt;
        this.time = time + add;
    }

    @Override
    public boolean isFrozen()
    {
        return frozen;
    }

    @Override
    public String toString()
    {
        return this.time.toString() + ":" + this.player.getUniqueId().toString() + ":" + this.frozen + ":" + this.timerType.toString();
    }
}
