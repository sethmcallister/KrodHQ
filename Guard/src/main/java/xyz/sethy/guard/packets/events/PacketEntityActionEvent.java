package xyz.sethy.guard.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * Created by sethm on 16/11/2016.
 */
public class PacketEntityActionEvent extends Event
{
    public int Action;
    public Player Player;
    private static final HandlerList handlers = new HandlerList();

    public PacketEntityActionEvent(Player Player, int Action)
    {
        this.Player = Player;
        this.Action = Action;
    }

    public Player getPlayer()
    {
        return this.Player;
    }

    public int getAction()
    {
        return this.Action;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public class PlayerAction
    {
        public PlayerAction()
        {
        }
    }
}
