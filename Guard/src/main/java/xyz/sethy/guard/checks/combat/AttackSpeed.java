package xyz.sethy.guard.checks.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketUseEntityEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sethm on 16/11/2016.
 */
public class AttackSpeed extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> attackTicks = new HashMap<>();

    public AttackSpeed(Plugin plugin)
    {
        super("AttackSpeed", "Auto Clicker", plugin);

        setAutobanTimer(true);
        setMaxViolations(20);
        setBannable(true);
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent event)
    {
        if (event.getAction() != EnumWrappers.EntityUseAction.ATTACK)
            return;

        if (!(event.getAttacked() instanceof Player))
            return;

        Player player = event.getAttacker();

        int count = 0;
        long time = System.currentTimeMillis();
        if (this.attackTicks.containsKey(player.getUniqueId()))
        {
            count = (Integer) ((Map.Entry) this.attackTicks.get(player.getUniqueId())).getKey();
            time = (Long) ((Map.Entry) this.attackTicks.get(player.getUniqueId())).getValue();
        }
        count++;
        if ((this.attackTicks.containsKey(player.getUniqueId())) && (UtilTime.elapsed(time, 1000L)))
        {
            if (count > 19)
            {
                Guard.getInstance().logCheat(this, player, null, count + " ap/s");
            }
            count = 0;
            time = UtilTime.nowlong();
        }
        this.attackTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(count, time));
    }
}
