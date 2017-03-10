package xyz.sethy.guard.checks.combat;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by sethm on 16/11/2016.
 */
public class FastBow extends Check
{
    public Map<UUID, Long> bowPull = new HashMap();

    public FastBow(Plugin plugin)
    {
        super("FastBow", "Fast Bow", plugin);

        setViolationsToNotify(2);
        setMaxViolations(10);

        setBannable(true);
        setMaxViolations(6);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void Interact(PlayerInteractEvent event)
    {
        Player Player = event.getPlayer();
        if ((Player.getItemInHand() != null) && (Player.getItemInHand().getType().equals(Material.BOW)))
        {
            this.bowPull.put(Player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onShoot(ProjectileLaunchEvent e)
    {
        if ((e.getEntity() instanceof Arrow))
        {
            Arrow arrow = (Arrow) e.getEntity();
            if ((arrow.getShooter() != null) && ((arrow.getShooter() instanceof Player)))
            {
                Player player = (Player) arrow.getShooter();
                if (this.bowPull.containsKey(player.getUniqueId()))
                {
                    Long time = Long.valueOf(System.currentTimeMillis() - ((Long) this.bowPull.get(player.getUniqueId())).longValue());
                    double power = arrow.getVelocity().length();
                    Long timeLimit = Long.valueOf(300L);

                    int ping = Guard.getInstance().getLagUtils().getPing(player);
                    if (ping > 400)
                    {
                        timeLimit = Long.valueOf(150L);
                    }
                    if ((power > 2.5D) && (time.longValue() < timeLimit.longValue()))
                    {
                        Guard.getInstance().logCheat(this, player, null, new String[0]);
                    }
                }
            }
        }
    }
}
