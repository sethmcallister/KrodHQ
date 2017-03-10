package xyz.sethy.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 30/12/2016.
 */
public class UserLoggedInEvent extends Event
{
    private static final HandlerList list = new HandlerList();
    private final User user;
    private final Player player;

    public UserLoggedInEvent(User user, Player player)
    {
        this.user = user;
        this.player = player;
    }

    public User getUser()
    {
        return user;
    }

    public Player getPlayer()
    {
        return player;
    }

    @Override
    public HandlerList getHandlers()
    {
        return list;
    }

    public static HandlerList getHandlerList()
    {
        return list;
    }
}
