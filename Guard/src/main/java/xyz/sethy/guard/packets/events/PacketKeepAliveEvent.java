package xyz.sethy.guard.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by sethm on 16/11/2016.
 */
public class PacketKeepAliveEvent extends Event
{
    public Player Player;
    private static final HandlerList handlers = new HandlerList();

    public PacketKeepAliveEvent(Player Player)
    {
        this.Player = Player;
    }

    public Player getPlayer()
    {
        return this.Player;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
