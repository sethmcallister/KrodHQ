package xyz.sethy.guard.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketUseEntityEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 15/01/2017.
 */
public class KillAuraC extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> AimbotTicks = new ConcurrentHashMap<>();
    private Map<UUID, Double> Differences = new ConcurrentHashMap<>();
    private Map<UUID, Location> LastLocation = new ConcurrentHashMap<>();

    public KillAuraC(Plugin plugin)
    {
        super("KillAuraC", "Kill Aura", plugin);
        setBannable(true);
        setMaxViolations(7);
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e)
    {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK)
        {
            return;
        }
        if (!(e.getAttacked() instanceof Player))
        {
            return;
        }
        Player damager = e.getAttacker();
        if (damager.getAllowFlight())
        {
            return;
        }
        Location from = null;
        Location to = damager.getLocation();
        if (this.LastLocation.containsKey(damager.getUniqueId()))
        {
            from = this.LastLocation.get(damager.getUniqueId());
        }
        this.LastLocation.put(damager.getUniqueId(), damager.getLocation());

        int Count = 0;
        long Time = System.currentTimeMillis();

        double LastDifference = -111111.0D;
        if (this.Differences.containsKey(damager.getUniqueId()))
        {
            LastDifference = this.Differences.get(damager.getUniqueId());
        }
        if (this.AimbotTicks.containsKey(damager.getUniqueId()))
        {
            Count = (Integer) ((Map.Entry) this.AimbotTicks.get(damager.getUniqueId())).getKey();
            Time = (Long) ((Map.Entry) this.AimbotTicks.get(damager.getUniqueId())).getValue();
        }
        if ((from == null) || ((to.getX() == from.getX()) && (to.getZ() == from.getZ())))
        {
            return;
        }
        double Difference = Math.abs(to.getYaw() - from.getYaw());
        if (Difference == 0.0D)
        {
            return;
        }
        if (Difference > 2.0D)
        {
            double diff = Math.abs(LastDifference - Difference);
            if (diff < 3.0D)
            {
                Count++;
            }
        }
        this.Differences.put(damager.getUniqueId(), Difference);
        if ((this.AimbotTicks.containsKey(damager.getUniqueId())) &&
                (UtilTime.elapsed(Time, 5000L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 10)
        {
            Count = 0;

            Guard.getInstance().logCheat(this, damager, "Aimbot");
        }
        this.AimbotTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }
}
