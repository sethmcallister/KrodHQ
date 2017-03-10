package xyz.sethy.factions.timers;

import org.bukkit.entity.Player;

/**
 * Created by Seth on 16/01/2017.
 */
public interface Timer
{
    Long getTime();

    void freeze();

    void unfreeze();

    boolean isFrozen();

    TimerType getTimerType();

    Player getPlayer();

    Integer getTagLevel();

    void setTime(Long time);
}
