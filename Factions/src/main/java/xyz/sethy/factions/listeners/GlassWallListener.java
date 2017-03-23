package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.Claim;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.support.Region;
import xyz.sethy.factions.support.RegionSupport;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerHandler;
import xyz.sethy.factions.timers.TimerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 17/12/2016.
 */
public class GlassWallListener
{
    private final ConcurrentHashMap<Player, List<Location>> map = new ConcurrentHashMap<>();
    private final TimerHandler timerHandler = Factions.getInstance().getTimerHandler();

    public GlassWallListener()
    {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    removeGlass(player);
                    Location location = player.getLocation();
                    if (timerHandler.hasTimer(player, TimerType.COMBAT_TAG))
                    {
                        Timer defaultTimer = timerHandler.getTimer(player, TimerType.COMBAT_TAG);
                        if (defaultTimer.getTime() > 0)
                        {
                            for (RegionSupport support : Factions.getInstance().getRegionSupports())
                            {
                                for (Region region : support.getNearByRegion(location))
                                {
                                    renderGlass(region, player, location);
                                }
                            }
                        }
                    }

                    if(timerHandler.hasTimer(player, TimerType.PVP_TIMER))
                    {
                        Timer defaultTimer = timerHandler.getTimer(player, TimerType.PVP_TIMER);
                        if(defaultTimer.getTime() > 0)
                        {
                            for(Map.Entry<Claim, Faction> claimMap : Factions.getInstance().getLandBoard().getRegionData(player.getLocation(), 10, 10, 10))
                            {
                                if(claimMap.getValue().hasDTRBitmask(DTRType.SAFEZONE) || claimMap.getValue().hasDTRBitmask(DTRType.ROAD))
                                    continue;

                                renderGlass(claimMap.getKey(), player, location);
                            }
                        }
                    }
                }
            }
        }, 2 * 20L, 10L);
    }


    public boolean isInBetween(int xone, int xother, int mid)
    {
        int distance = Math.abs(xone - xother);
        return distance == Math.abs(mid - xone) + Math.abs(mid - xother);
    }

    public int closestNumber(int from, int... numbers)
    {
        int distance = Math.abs(numbers[0] - from);
        int idx = 0;
        for (int c = 1; c < numbers.length; ++c)
        {
            int cdistance = Math.abs(numbers[c] - from);
            if (cdistance >= distance) continue;
            idx = c;
            distance = cdistance;
        }
        return numbers[idx];
    }

    private boolean renderGlass(Claim claim, Player player, Location to)
    {
        if(claim == null)
            return false;

        int x;
        int y;
        Location location;
        boolean updateZ;

        int closerx = closestNumber(to.getBlockX(), claim.getMinimumPoint().getBlockX(), claim.getMaximumPoint().getBlockX());
        int closerz = closestNumber(to.getBlockZ(), claim.getMinimumPoint().getBlockZ(), claim.getMaximumPoint().getBlockZ());
        boolean updateX = Math.abs(to.getX() - (double) closerx) < 10.0;
        boolean bl = updateZ = Math.abs(to.getZ() - (double) closerz) < 10.0;
        if (!updateX && !updateZ)
        {
            return false;
        }
        ArrayList<Location> toUpdate = new ArrayList<>();
        if (updateX)
        {
            for (y = -2; y < 3; ++y)
            {
                for (x = -4; x < 4; ++x)
                {
                    if (!isInBetween(claim.getMinimumPoint().getBlockZ(), claim.getMaximumPoint().getBlockZ(), to.getBlockZ() + x) || toUpdate.contains(location = new Location(to.getWorld(), (double) closerx, (double) (to.getBlockY() + y), (double) (to.getBlockZ() + x))) || location.getBlock().getType().isOccluding())
                        continue;

                    toUpdate.add(location);
                }
            }
        }
        if (updateZ)
        {
            for (y = -2; y < 3; ++y)
            {
                for (x = -4; x < 4; ++x)
                {
                    if (!isInBetween(claim.getMinimumPoint().getBlockX(), claim.getMaximumPoint().getBlockX(), to.getBlockX() + x) || toUpdate.contains((location = new Location(to.getWorld(), (double) (to.getBlockX() + x), (double) (to.getBlockY() + y), (double) closerz))) || location.getBlock().getType().isOccluding())
                        continue;
                    toUpdate.add(location);
                }
            }
        }
        this.update(player, toUpdate);
        return true;
    }

    private boolean renderGlass(Region spawn, Player player, Location to)
    {
        int x;
        int y;
        Location location;
        boolean updateZ;
        if (spawn == null)
        {
            return false;
        }
        int closerx = closestNumber(to.getBlockX(), spawn.getMinX(), spawn.getMaxX());
        int closerz = closestNumber(to.getBlockZ(), spawn.getMinZ(), spawn.getMaxZ());
        boolean updateX = Math.abs(to.getX() - (double) closerx) < 10.0;
        boolean bl = updateZ = Math.abs(to.getZ() - (double) closerz) < 10.0;
        if (!updateX && !updateZ)
        {
            return false;
        }
        ArrayList<Location> toUpdate = new ArrayList<>();
        if (updateX)
        {
            for (y = -2; y < 3; ++y)
            {
                for (x = -4; x < 4; ++x)
                {
                    if (!isInBetween(spawn.getMinZ(), spawn.getMaxZ(), to.getBlockZ() + x) || toUpdate.contains(location = new Location(to.getWorld(), (double) closerx, (double) (to.getBlockY() + y), (double) (to.getBlockZ() + x))) || location.getBlock().getType().isOccluding())
                        continue;
                    toUpdate.add(location);
                }
            }
        }
        if (updateZ)
        {
            for (y = -2; y < 3; ++y)
            {
                for (x = -4; x < 4; ++x)
                {
                    if (!isInBetween(spawn.getMinX(), spawn.getMaxX(), to.getBlockX() + x) || toUpdate.contains((location = new Location(to.getWorld(), (double) (to.getBlockX() + x), (double) (to.getBlockY() + y), (double) closerz))) || location.getBlock().getType().isOccluding())
                        continue;
                    toUpdate.add(location);
                }
            }
        }
        this.update(player, toUpdate);
        return true;
    }

    public void update(Player player, List<Location> toUpdate)
    {
        if (this.map.containsKey(player))
        {
            this.map.get(player).addAll(toUpdate);
            for (Location location1 : toUpdate)
            {
                player.sendBlockChange(location1, 95, (byte) 14);
            }
        }
        else
        {
            for (Location location1 : toUpdate)
            {
                player.sendBlockChange(location1, 95, (byte) 14);
            }
            this.map.put(player, toUpdate);
        }
    }

    public void removeGlass(Player player)
    {
        if (this.map.containsKey(player))
        {
            for (Location location : this.map.get(player))
            {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            this.map.remove(player);
        }
    }
}
