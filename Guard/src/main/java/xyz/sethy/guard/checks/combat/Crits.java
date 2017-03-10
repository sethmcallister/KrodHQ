package xyz.sethy.guard.checks.combat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.utils.CheatUtil;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Created by sethm on 16/11/2016.
 */
public class Crits extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> CritTicks = new HashMap();
    private Map<UUID, Double> FallDistance = new HashMap();

    public Crits(Plugin plugin)
    {
        super("Crits", "Critical", plugin);

        setAutobanTimer(true);
        setMaxViolations(8);
        setBannable(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() == null)
            return;
        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();
        if (player.getAllowFlight())
            return;

        if (CheatUtil.slabsNear(player.getLocation()))
            return;

        Location pL = player.getLocation().clone();
        pL.add(0.0D, player.getEyeHeight() + 1.0D, 0.0D);
        if (CheatUtil.blocksNear(pL))
        {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.CritTicks.containsKey(player.getUniqueId()))
        {
            Count = (Integer) ((Entry) this.CritTicks.get(player.getUniqueId())).getKey();
            Time = (Long) ((Entry) this.CritTicks.get(player.getUniqueId())).getValue();
        }
        if (!this.FallDistance.containsKey(player.getUniqueId()))
        {
            return;
        }
        double realFallDistance = this.FallDistance.get(player.getUniqueId());
        if ((player.getFallDistance() > 0.0D) && (!player.isOnGround()) && (realFallDistance == 0.0D))
        {
            Count++;
        }
        else
        {
            Count = 0;
        }
        if ((this.CritTicks.containsKey(player.getUniqueId())) &&
                (UtilTime.elapsed(Time, 10000L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 2)
        {
            Count = 0;

            Guard.getInstance().logCheat(this, player, null, new String[0]);
        }
        this.CritTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry(Integer.valueOf(Count), Long.valueOf(Time)));
    }

    @EventHandler
    public void Move(PlayerMoveEvent e)
    {
        Player Player = e.getPlayer();

        double Falling = 0.0D;
        if ((!Player.isOnGround()) && (e.getFrom().getY() > e.getTo().getY()))
        {
            if (this.FallDistance.containsKey(Player.getUniqueId()))
            {
                Falling = this.FallDistance.get(Player.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        this.FallDistance.put(Player.getUniqueId(), Falling);
    }
}
