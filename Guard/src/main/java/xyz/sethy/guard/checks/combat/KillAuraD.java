package xyz.sethy.guard.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 15/01/2017.
 */
public class KillAuraD extends Check
{
    private Map<Player, Map.Entry<Integer, Long>> lastAttack = new ConcurrentHashMap<>();

    public KillAuraD(Plugin plugin)
    {
        super("KillAuraE", "Kill Aura", plugin);
        setBannable(true);
        setMaxViolations(7);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void Damage(EntityDamageByEntityEvent e)
    {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
        {
            return;
        }
        if (!(e.getDamager() instanceof Player))
        {
            return;
        }
        if (!(e.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) e.getDamager();
        if (this.lastAttack.containsKey(player))
        {
            Integer entityid = (Integer) ((Map.Entry) this.lastAttack.get(player)).getKey();
            Long time = (Long) ((Map.Entry) this.lastAttack.get(player)).getValue();
            if ((entityid != e.getEntity().getEntityId()) && (System.currentTimeMillis() - time < 5L))
            {
                Guard.getInstance().logCheat(this, player, "MultiAura");
            }
            this.lastAttack.remove(player);
        }
        else
        {
            this.lastAttack.put(player, new AbstractMap.SimpleEntry<>(e.getEntity().getEntityId(), System.currentTimeMillis()));
        }
    }
}
