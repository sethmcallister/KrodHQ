package xyz.sethy.factions.support;

import org.bukkit.entity.Player;

/**
 * Created by sethm on 06/01/2017.
 */
public interface Region
{
    int getMinX();

    int getMaxX();

    int getMinZ();

    int getMaxZ();

    int getMinY();

    int getMaxY();

    String getWorld();

    boolean canCombatTagedPlayerEnter();

    boolean shouldPvPTimerFreeze();

    boolean canPvPTimedPlayerEnter(Player var1);
}
