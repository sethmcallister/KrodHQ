package xyz.sethy.guard.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.utils.UtilMath;
import xyz.sethy.guard.utils.UtilPlayer;
import xyz.sethy.guard.utils.UtilTime;

import java.util.*;

/**
 * Created by sethm on 16/11/2016.
 */
public class Reach extends Check
{
    public class ReachEntry
    {
        public Long lastTime;
        public List<Double> reachs = new ArrayList<>();

        public ReachEntry(Long aLong, List<Double> lastTime)
        {
            this.lastTime = aLong;
            this.reachs = lastTime;
        }

        public Long getLastTime()
        {
            return this.lastTime;
        }

        public List<Double> getReachs()
        {
            return this.reachs;
        }

        public void setLastTime(Long LastTime)
        {
            this.lastTime = LastTime;
        }

        public void setReachs(List<Double> Reachs)
        {
            this.reachs = Reachs;
        }

        public void addReach(Double Reach)
        {
            this.reachs.add(Reach);
        }
    }

    private Map<UUID, ReachEntry> ReachTicks = new HashMap<>();

    public Reach(Plugin plugin)
    {
        super("Reach", "Reach", plugin);

        setAutobanTimer(true);
        setMaxViolations(13);
        setBannable(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() == null)
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Player))
        {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();
        if (damager.getAllowFlight())
            return;

        if (player.getAllowFlight())
            return;

        long Time = System.currentTimeMillis();
        ArrayList<Double> Reachs = new ArrayList<>();
        if (this.ReachTicks.containsKey(damager.getUniqueId()))
        {
            Time = (this.ReachTicks.get(damager.getUniqueId())).getLastTime();
            Reachs = new ArrayList<>((this.ReachTicks.get(damager.getUniqueId())).getReachs());
        }
        double MaxReach = 4.0D;
        if (damager.hasPotionEffect(PotionEffectType.SPEED))
        {
            int Level = 0;
            for (PotionEffect Effect : damager.getActivePotionEffects())
            {
                if (Effect.getType().equals(PotionEffectType.SPEED))
                {
                    Level = Effect.getAmplifier();
                    break;
                }
            }
            switch (Level)
            {
                case 0:
                    MaxReach = 4.1D;
                    break;
                case 1:
                    MaxReach = 4.2D;
                    break;
                case 2:
                    MaxReach = 4.3D;
                    break;
                default:
                    return;
            }
        }

        double Reach = UtilPlayer.getEyeLocation(damager).distance(player.getLocation());

        int Ping = Guard.getInstance().getLagUtils().getPing(damager);
        if ((Ping >= 100) && (Ping < 200))
        {
            MaxReach += 0.2D;
        }
        else if ((Ping >= 200) && (Ping < 250))
        {
            MaxReach += 0.4D;
        }
        else if ((Ping >= 250) && (Ping < 300))
        {
            MaxReach += 0.8D;
        }
        else if ((Ping >= 300) && (Ping < 350))
        {
            MaxReach += 1.2D;
        }
        else if ((Ping >= 350) && (Ping < 400))
        {
            MaxReach += 1.5D;
        }
        else if (Ping > 400)
        {
            return;
        }

        if (damager.getLocation().getY() > player.getLocation().getY())
        {
            double Difference = damager.getLocation().getY() - player.getLocation().getY();

            MaxReach += Difference / 4.0D;
        }
        else if (player.getLocation().getY() > damager.getLocation().getY())
        {
            double Difference = player.getLocation().getY() - damager.getLocation().getY();

            MaxReach += Difference / 4.0D;
        }
        if (Reach > MaxReach)
        {
            Reachs.add(Reach);
            Time = System.currentTimeMillis();
        }
        if ((this.ReachTicks.containsKey(damager.getUniqueId())) && (UtilTime.elapsed(Time, 25000L)))
        {
            Reachs.clear();
            Time = System.currentTimeMillis();
        }
        if (Reachs.size() > 3)
        {
            Double AverageReach = UtilMath.averageDouble(Reachs);

            Double A = 6.0D - MaxReach;
            if (A < 0.0D)
            {
                A = 0.0D;
            }
            Double B = AverageReach - MaxReach;
            if (B < 0.0D)
            {
                B = 0.0D;
            }
            int Level = (int) Math.round(B / A * 100.0D);

            Reachs.clear();

            Guard.getInstance().logCheat(this, damager, null, Level + "%");
        }
        this.ReachTicks.put(damager.getUniqueId(), new ReachEntry(Time, Reachs));
    }
}
