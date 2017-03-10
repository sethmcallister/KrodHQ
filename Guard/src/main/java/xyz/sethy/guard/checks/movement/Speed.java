package xyz.sethy.guard.checks.movement;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.utils.CheatUtil;
import xyz.sethy.guard.utils.UtilMath;
import xyz.sethy.guard.utils.UtilPlayer;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 24/11/2016.
 */
public class Speed extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    private final Map<UUID, Map.Entry<Integer, Long>> tooFastTicks;

    public Speed(Plugin plugin)
    {
        super("Speed", "Speed", plugin);
        setAutobanTimer(true);
        this.speedTicks = new ConcurrentHashMap<>();
        this.tooFastTicks = new ConcurrentHashMap<>();

        setMaxViolations(6);
        setBannable(true);
    }

    public boolean isOnIce(Player player)
    {
        Location a = player.getLocation();
        a.setY(a.getY() - 1.0D);
        if (a.getBlock().getType().equals(Material.ICE))
        {
            return true;
        }
        a.setY(a.getY() - 1.0D);
        if (a.getBlock().getType().equals(Material.ICE))
        {
            return true;
        }
        return false;
    }

    @EventHandler
    public void CheckSpeed(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if ((event.getFrom().getX() == event.getTo().getX()) && (event.getFrom().getY() == event.getTo().getY()) &&
                (event.getFrom().getZ() == event.getFrom().getZ()))
        {
            return;
        }
        if (player.getAllowFlight())
            return;

        if (player.getVehicle() != null)
            return;

        if (Guard.getInstance().getLastVelocity().containsKey(player.getUniqueId()))
        {
            return;
        }
        int Count = 0;
        long Time = UtilTime.nowlong();
        if (this.speedTicks.containsKey(player.getUniqueId()))
        {
            Count = this.speedTicks.get(player.getUniqueId()).getKey();
            Time = this.speedTicks.get(player.getUniqueId()).getValue();
        }
        int TooFastCount = 0;
        if (this.tooFastTicks.containsKey(player.getUniqueId()))
        {
            double OffsetXZ = UtilMath.offset(UtilMath.getHorizontalVector(event.getFrom().toVector()), UtilMath.getHorizontalVector(event.getTo().toVector()));
            double LimitXZ;
            if ((UtilPlayer.isOnGround(player)) && (player.getVehicle() == null))
                LimitXZ = 0.33D;
            else
                LimitXZ = 0.4D;
            if (CheatUtil.slabsNear(player.getLocation()))
                LimitXZ += 0.05D;
            Location b = UtilPlayer.getEyeLocation(player);
            b.add(0.0D, 1.0D, 0.0D);
            if ((b.getBlock().getType() != Material.AIR) && (!CheatUtil.canStandWithin(b.getBlock())))
                LimitXZ = 0.69D;
            if (isOnIce(player))
            {
                if ((b.getBlock().getType() != Material.AIR) && (!CheatUtil.canStandWithin(b.getBlock())))
                    LimitXZ = 1.0D;
                else
                    LimitXZ = 0.75D;
            }
            float speed = player.getWalkSpeed();
            LimitXZ += (speed > 0.2F ? speed * 10.0F * 0.33F : 0.0F);
            for (PotionEffect effect : player.getActivePotionEffects())
            {
                if (effect.getType().equals(PotionEffectType.SPEED))
                {
                    if (player.isOnGround())
                        LimitXZ += 0.06D * (effect.getAmplifier() + 1);
                    else
                        LimitXZ += 0.02D * (effect.getAmplifier() + 1);
                }
            }
            if ((OffsetXZ > LimitXZ) && (!UtilTime.elapsed((Long) ((Map.Entry) this.tooFastTicks.get(player.getUniqueId())).getValue(), 150L)))
                TooFastCount = (Integer) ((Map.Entry) this.tooFastTicks.get(player.getUniqueId())).getKey() + 1;
            else
                TooFastCount = 0;
        }
        if (TooFastCount > 6)
        {
            TooFastCount = 0;
            Count++;
        }
        if ((this.speedTicks.containsKey(player.getUniqueId())) && (UtilTime.elapsed(Time, 60000L)))
        {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 3)
        {
            Count = 0;
            Guard.getInstance().logCheat(this, player, null);
        }
        this.tooFastTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(TooFastCount, System.currentTimeMillis()));
        this.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }

}
