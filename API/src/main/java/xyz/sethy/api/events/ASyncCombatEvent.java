package xyz.sethy.api.events;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by sethm on 30/12/2016.
 */
public class ASyncCombatEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player damager;
    private Damageable entity;
    private double damage;

    public ASyncCombatEvent(Player dmgr, Damageable ent, double dmg)
    {
        super(true);

        this.damager = dmgr;
        this.entity = ent;
        this.damage = dmg;
    }

    public void setDamage(double dmg)
    {
        this.damage = dmg;
    }

    public Player getDamager()
    {
        return this.damager;
    }

    public Damageable getEntity()
    {
        return this.entity;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public EntityType getEntityType()
    {
        return this.entity.getType();
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

    public boolean isCancelled()
    {
        return this.cancelled;
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
