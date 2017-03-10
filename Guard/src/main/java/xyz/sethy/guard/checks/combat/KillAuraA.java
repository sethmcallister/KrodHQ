package xyz.sethy.guard.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketUseEntityEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by sethm on 16/11/2016.
 */
public class KillAuraA extends Check
{
    public KillAuraA(Plugin plugin)
    {
        super("KillAuraA", "Kill Aura", plugin);

        setBannable(true);
        setMaxViolations(7);
    }

    private Map<UUID, Long> LastMS = new HashMap<>();
    private Map<UUID, List<Long>> Clicks = new HashMap<>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<>();

    @EventHandler
    public void UseEntity(PacketUseEntityEvent event)
    {
        if (event.getAction() != EnumWrappers.EntityUseAction.ATTACK)
            return;

        if (!(event.getAttacked() instanceof Player))
            return;

        Player damager = event.getAttacker();

        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.ClickTicks.containsKey(damager.getUniqueId()))
        {
            Count = (Integer) ((Entry) this.ClickTicks.get(damager.getUniqueId())).getKey();
            Time = (Long) ((Entry) this.ClickTicks.get(damager.getUniqueId())).getValue();
        }
        if (this.LastMS.containsKey(damager.getUniqueId()))
        {
            long MS;
            MS = UtilTime.nowlong() - (Long) this.LastMS.get(damager.getUniqueId());
            if ((MS > 500L) || (MS < 5L))
            {
                this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (this.Clicks.containsKey(damager.getUniqueId()))
            {
                List Clicks = (List) this.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 10)
                {
                    this.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);

                    long Range = (Long) Clicks.get(Clicks.size() - 1) - (Long) Clicks.get(0);

                    if (Range < 30L)
                    {
                        Count++;
                        Time = System.currentTimeMillis();
                    }
                }
                else
                {
                    Clicks.add(MS);
                    this.Clicks.put(damager.getUniqueId(), Clicks);
                }
            }
            else
            {
                List<Long> Clicks = new ArrayList();
                Clicks.add(MS);
                this.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if ((this.ClickTicks.containsKey(damager.getUniqueId())) &&
                (UtilTime.elapsed(Time, 5000L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 0)
        {
            Count = 0;

            Guard.getInstance().logCheat(this, damager, "Click Pattern", "Experimental");
        }
        this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
        this.ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry(Integer.valueOf(Count), Long.valueOf(Time)));
    }
}
