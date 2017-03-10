package xyz.sethy.hub.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by seth on 28/12/16.
 */
public class PlayerCombatListener implements Listener
{
    private final ConcurrentHashMap<Player, ConcurrentHashMap<Player, Long>> playerCooldowns = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerCombat(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

}
