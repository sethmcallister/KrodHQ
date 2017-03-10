package xyz.sethy.guard.checks.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketEntityActionEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 24/11/2016.
 */
public class Sneak extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> sneakTicks;

    public Sneak(Plugin plugin)
    {
        super("Sneak", "Sneak", plugin);
        this.sneakTicks = new ConcurrentHashMap<>();
        setAutobanTimer(true);
    }

    @EventHandler
    public void EntityAction(PacketEntityActionEvent event)
    {
        if (event.getAction() != 1)
            return;

        Player player = event.getPlayer();

        int Count = 0;
        long Time = -1L;
        if (this.sneakTicks.containsKey(player.getUniqueId()))
        {
            Count = (Integer) ((Map.Entry) this.sneakTicks.get(player.getUniqueId())).getKey();
            Time = (Long) ((Map.Entry) this.sneakTicks.get(player.getUniqueId())).getValue();
        }
        Count++;
        if (this.sneakTicks.containsKey(player.getUniqueId()))
        {
            if (UtilTime.elapsed(Time, 100L))
            {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            else
                Time = System.currentTimeMillis();
        }
        if (Count > 50)
        {
            Count = 0;

            Guard.getInstance().logCheat(this, player, null);
        }
        this.sneakTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry(Count, Time));
    }
}
