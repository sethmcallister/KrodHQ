package xyz.sethy.guard.checks.combat;


import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketUseEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sethm on 16/11/2016.
 */
public class DoubleClick extends Check
{
    public DoubleClick(Plugin plugin)
    {
        super("DoubleClick", "Double Click", plugin);

        setBannable(true);
        setViolationsToNotify(2);
        setMaxViolations(50);
        setBannable(true);

    }

    private Map<UUID, Long[]> LastMSCPS = new HashMap();

    @EventHandler
    public void UseEntity(PacketUseEntityEvent event)
    {
        if (event.getAction() != EnumWrappers.EntityUseAction.ATTACK)
            return;

        if (!(event.getAttacked() instanceof Player))
            return;

        Player damager = event.getAttacker();

        Long first = 0L;
        Long second = 0L;
        if (this.LastMSCPS.containsKey(damager.getUniqueId()))
        {
            first = ((Long[]) this.LastMSCPS.get(damager.getUniqueId()))[0];
            second = ((Long[]) this.LastMSCPS.get(damager.getUniqueId()))[1];
        }
        if (first == 0L)
        {
            first = System.currentTimeMillis();
        }
        else if (second == 0L)
        {
            second = System.currentTimeMillis();
            first = System.currentTimeMillis() - first;
        }
        else
        {
            second = System.currentTimeMillis() - second;
            if ((first > 50L) && (second == 0L))
            {
                Guard.getInstance().logCheat(this, damager, null);
            }
            first = 0L;
            second = 0L;
        }
        this.LastMSCPS.put(damager.getUniqueId(), new Long[]{first, second});
    }
}
