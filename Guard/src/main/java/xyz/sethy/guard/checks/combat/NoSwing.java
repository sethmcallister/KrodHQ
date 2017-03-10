package xyz.sethy.guard.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketSwingArmEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sethm on 16/11/2016.
 */
public class NoSwing extends Check
{
    public NoSwing(Plugin plugin)
    {
        super("NoSwing", "No Swing", plugin);

        setAutobanTimer(true);
        setMaxViolations(12);
        setBannable(true);

    }

    private Map<UUID, Long> LastArmSwing = new HashMap();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() == null)
            return;

        if (Guard.getInstance().getLagUtils().getTPS() < 17.0D)
            return;

        if (!(event.getDamager() instanceof Player))
        {
            return;
        }

        Player player = (Player) event.getDamager();

        final Player fplayer = player;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Guard.getInstance().getPlugin(), new Runnable()
        {
            public void run()
            {
                if (!hasSwung(fplayer, 1500L))
                {
                    Guard.getInstance().logCheat(NoSwing.this, fplayer, null);
                }
            }
        }, 10L);
    }

    public boolean hasSwung(Player player, Long time)
    {
        if (!this.LastArmSwing.containsKey(player.getUniqueId()))
        {
            return false;
        }
        return UtilTime.nowlong() < this.LastArmSwing.get(player.getUniqueId()) + time;
    }

    @EventHandler
    public void ArmSwing(PacketSwingArmEvent event)
    {
        this.LastArmSwing.put(event.getPlayer().getUniqueId(), UtilTime.nowlong());
    }
}
