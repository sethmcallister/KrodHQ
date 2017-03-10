package xyz.sethy.guard.checks.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketBlockPlacementEvent;
import xyz.sethy.guard.packets.events.PacketHeldItemChangeEvent;
import xyz.sethy.guard.packets.events.PacketSwingArmEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 16/11/2016.
 */
public class Crash extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> faggotTicks = new ConcurrentHashMap<>();
    private Map<UUID, Map.Entry<Integer, Long>> faggot2Ticks = new ConcurrentHashMap<>();
    private Map<UUID, Map.Entry<Integer, Long>> faggot3Ticks = new ConcurrentHashMap<>();

    public Crash(Plugin plugin)
    {
        super("Crash", "Crash", plugin);

        setMaxViolations(0);
        setBannable(true);

    }

    public List<UUID> faggots = new ArrayList<>();

    @EventHandler
    public void Swing(PacketSwingArmEvent event)
    {
        Player faggot = event.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId()))
        {
            event.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggotTicks.containsKey(faggot.getUniqueId()))
        {
            Count = (Integer) ((Entry) this.faggotTicks.get(faggot.getUniqueId())).getKey();
            Time = (Long) ((Entry) this.faggotTicks.get(faggot.getUniqueId())).getValue();
        }
        Count++;
        if ((this.faggotTicks.containsKey(faggot.getUniqueId())) && (UtilTime.elapsed(Time, 100L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000)
        {
            Guard.getInstance().logCheat(this, faggot, null);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggotTicks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }

    @EventHandler
    public void Switch(PacketHeldItemChangeEvent event)
    {
        Player faggot = event.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId()))
        {
            event.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggot2Ticks.containsKey(faggot.getUniqueId()))
        {
            Count = (Integer) ((Entry) this.faggot2Ticks.get(faggot.getUniqueId())).getKey();
            Time = (Long) ((Entry) this.faggot2Ticks.get(faggot.getUniqueId())).getValue();
        }
        Count++;
        if ((this.faggot2Ticks.containsKey(faggot.getUniqueId())) && (UtilTime.elapsed(Time, 100L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000)
        {
            Guard.getInstance().logCheat(this, faggot, null);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggot2Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }

    @EventHandler
    public void BlockPlace(PacketBlockPlacementEvent event)
    {
        Player faggot = event.getPlayer();
        if (this.faggots.contains(faggot.getUniqueId()))
        {
            event.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.faggot3Ticks.containsKey(faggot.getUniqueId()))
        {
            Count = (Integer) ((Entry) this.faggot3Ticks.get(faggot.getUniqueId())).getKey();
            Time = (Long) ((Entry) this.faggot3Ticks.get(faggot.getUniqueId())).getValue();
        }
        Count++;
        if ((this.faggot3Ticks.containsKey(faggot.getUniqueId())) &&
                (UtilTime.elapsed(Time, 100L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000)
        {
            Guard.getInstance().logCheat(this, faggot, null);
            this.faggots.add(faggot.getUniqueId());
        }
        this.faggot3Ticks.put(faggot.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }
}
