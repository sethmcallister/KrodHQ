package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerType;

/**
 * Created by sethm on 01/01/2017.
 */
public class CombatListener implements Listener
{
    public CombatListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if(Factions.getInstance().getTimerHandler().isSotw())
        {
            if(event.getEntity() instanceof Player)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent event)
    {
        if(Factions.getInstance().getTimerHandler().isSotw())
        {
            return;
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
        {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(damager);

            if (Factions.getInstance().getLandBoard().getFaction(event.getDamager().getLocation()) != null)
            {
                Faction faction1 = Factions.getInstance().getLandBoard().getFaction(event.getDamager().getLocation());
                if (faction1.hasDTRBitmask(DTRType.SAFEZONE))
                {
                    event.setDamage(0);
                    event.setCancelled(true);
                    return;
                }
            }

            if (faction != null && faction.getAllMembers().contains(damaged.getUniqueId()))
            {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }

            if (Factions.getInstance().getTimerHandler().hasTimer(damaged, TimerType.COMBAT_TAG))
            {
                Timer toremove = Factions.getInstance().getTimerHandler().getTimer(damaged, TimerType.COMBAT_TAG);
                Factions.getInstance().getTimerHandler().getPlayerTimers(damaged).remove(toremove);
            }

            if (Factions.getInstance().getTimerHandler().hasTimer(damager, TimerType.COMBAT_TAG))
            {
                Timer toremove = Factions.getInstance().getTimerHandler().getTimer(damager, TimerType.COMBAT_TAG);
                Factions.getInstance().getTimerHandler().getPlayerTimers(damager).remove(toremove);
            }

            Factions.getInstance().getTimerHandler().getPlayerTimers(damaged).add(new DefaultTimer(TimerType.COMBAT_TAG, 30000 + System.currentTimeMillis(), damaged));
            Factions.getInstance().getTimerHandler().getPlayerTimers(damager).add(new DefaultTimer(TimerType.COMBAT_TAG, 30000 + System.currentTimeMillis(), damager));
        }
    }
}
