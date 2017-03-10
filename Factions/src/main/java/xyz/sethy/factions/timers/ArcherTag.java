package xyz.sethy.factions.timers;

import org.bukkit.entity.Player;

/**
 * Created by Seth on 16/01/2017.
 */
public class ArcherTag implements Timer
{
    private TimerType timerType;
    private Long time;
    private Player player;
    private boolean frozen;
    private Integer tag;

    public ArcherTag(TimerType timerType, Long time, Player player, Integer level)
    {
        this.timerType = timerType;
        this.time = time;
        this.player = player;
        frozen = false;
        this.tag = level;
    }

    @Override
    public TimerType getTimerType()
    {
        return timerType;
    }

    @Override
    public Long getTime()
    {
        if (frozen)
            return time;

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
    public void freeze()
    {
        this.frozen = true;
    }

    @Override
    public void unfreeze()
    {
        this.frozen = false;
    }

    @Override
    public boolean isFrozen()
    {
        return frozen;
    }

    public void setTagLevel(Integer level)
    {
        this.tag = level;
    }

    @Override
    public void setTime(Long time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return this.time.toString() + ":" + this.player.getUniqueId().toString() + ":" + this.frozen + ":" + this.timerType.toString();
    }
}
