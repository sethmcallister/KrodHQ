package xyz.sethy.guard.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketUseEntityEvent;
import xyz.sethy.guard.utils.CheatUtil;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 15/01/2017.
 */
public class KillAuraB extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> AuraTicks = new ConcurrentHashMap<>();

    public KillAuraB(Plugin plugin)
    {
        super("KillAuraB", "Kill Aura", plugin);

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
        Player player = (Player) e.getAttacked();
        if (damager.getAllowFlight())
        {
            return;
        }
        if (player.getAllowFlight())
        {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.AuraTicks.containsKey(damager.getUniqueId()))
        {
            Count = (Integer) ((Map.Entry) this.AuraTicks.get(damager.getUniqueId())).getKey();
            Time = (Long) ((Map.Entry) this.AuraTicks.get(damager.getUniqueId())).getValue();
        }
        double OffsetXZ = CheatUtil.getAimbotoffset(damager.getLocation(), damager.getEyeHeight(), player);

        double LimitOffset = 200.0D;
        if ((damager.getVelocity().length() > 0.08D) || (Guard.getInstance().getLastVelocity().containsKey(damager.getUniqueId())))
        {
            LimitOffset += 200.0D;
        }
        int Ping = Guard.getInstance().getLagUtils().getPing(damager);
        if ((Ping >= 100) && (Ping < 200))
        {
            LimitOffset += 50.0D;
        }
        else if ((Ping >= 200) && (Ping < 250))
        {
            LimitOffset += 75.0D;
        }
        else if ((Ping >= 250) && (Ping < 300))
        {
            LimitOffset += 150.0D;
        }
        else if ((Ping >= 300) && (Ping < 350))
        {
            LimitOffset += 300.0D;
        }
        else if ((Ping >= 350) && (Ping < 400))
        {
            LimitOffset += 400.0D;
        }
        else if (Ping > 400)
        {
            return;
        }
        if (OffsetXZ > LimitOffset * 4.0D)
        {
            Count += 12;
        }
        else if (OffsetXZ > LimitOffset * 3.0D)
        {
            Count += 10;
        }
        else if (OffsetXZ > LimitOffset * 2.0D)
        {
            Count += 8;
        }
        else if (OffsetXZ > LimitOffset)
        {
            Count += 4;
        }
        if ((this.AuraTicks.containsKey(damager.getUniqueId())) &&
                (UtilTime.elapsed(Time, 60000L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 16)
        {
            Count = 0;
            Guard.getInstance().logCheat(this, damager, "Hit Miss Ratio", "Experimental");
        }
        this.AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }
}
