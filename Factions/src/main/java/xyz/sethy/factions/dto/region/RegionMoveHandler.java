package xyz.sethy.factions.dto.region;

import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by sethm on 06/12/2016.
 */
public interface RegionMoveHandler
{
    boolean handleMove(PlayerMoveEvent event);
}
