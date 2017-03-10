package xyz.sethy.factions.dto.region;

import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.TimerType;

/**
 * Created by sethm on 06/12/2016.
 */
public enum RegionType
{
    SPAWN(event ->
    {
        return !Factions.getInstance().getTimerHandler().hasTimer(event.getPlayer(), TimerType.COMBAT_TAG);
    }),
    WARZONE(event -> true),
    WILDERNESS(event -> true),
    ROAD(event -> true),
    CLAIMED_LAND(event ->
    {
        return !Factions.getInstance().getTimerHandler().hasTimer(event.getPlayer(), TimerType.PVP_TIMER);
    });

    private RegionMoveHandler moveHandler;

    RegionType(final RegionMoveHandler moveHandler)
    {
        this.moveHandler = moveHandler;
    }

    public RegionMoveHandler getMoveHandler()
    {
        return this.moveHandler;
    }
}
