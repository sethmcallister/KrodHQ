package xyz.sethy.factions.dto.claim;

import org.bukkit.Location;
import org.bukkit.World;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

import java.util.*;

/**
 * Created by sethm on 01/12/2016.
 */
public class LandBoard
{
    private final int WARZONE_RADIUS;
    private Map<Claim, Faction> boardMap;

    public LandBoard()
    {
        this.WARZONE_RADIUS = Factions.getInstance().isKitmap() ? 150 : 500;
        this.boardMap = new HashMap<>();
    }

    public void loadFromFactions()
    {
        for (final Faction faction : Factions.getInstance().getFactionManager().getFactions())
        {
            for (final Claim claim : faction.getClaims())
            {
                this.setFactionAt(claim, faction);
            }
        }
    }

    public Set<Map.Entry<Claim, Faction>> getRegionData(final Location center, final int xDistance, final int yDistance, final int zDistance)
    {
        final Location loc1 = new Location(center.getWorld(), (double) (center.getBlockX() - xDistance), (double) (center.getBlockY() - yDistance), (double) (center.getBlockZ() - zDistance));
        final Location loc2 = new Location(center.getWorld(), (double) (center.getBlockX() + xDistance), (double) (center.getBlockY() + yDistance), (double) (center.getBlockZ() + zDistance));
        return this.getRegionData(loc1, loc2);
    }

    public Set<Map.Entry<Claim, Faction>> getRegionData(final Location min, final Location max)
    {
        final Set<Map.Entry<Claim, Faction>> regions = new HashSet<>();
        for (final Map.Entry<Claim, Faction> regionEntry : this.boardMap.entrySet())
        {
            if (!regions.contains(regionEntry) && max.getWorld().getName().equals(regionEntry.getKey().getWorld()) && max.getBlockX() >= regionEntry.getKey().getX1() && min.getBlockX() <= regionEntry.getKey().getX2() && max.getBlockZ() >= regionEntry.getKey().getZ1() && min.getBlockZ() <= regionEntry.getKey().getZ2() && max.getBlockY() >= regionEntry.getKey().getY1() && min.getBlockY() <= regionEntry.getKey().getY2())
            {
                if(regionEntry.getValue().getClaims().contains(regionEntry.getKey()))
                    regions.add(regionEntry);
            }
        }
        return regions;
    }

    public Map.Entry<Claim, Faction> getRegionData(final Location location)
    {
        for (final Map.Entry<Claim, Faction> entry : this.boardMap.entrySet())
        {
            if (entry.getKey().contains(location))
            {
                if(entry.getValue().getClaims().contains(entry.getKey()))
                    return entry;
            }
        }
        return null;
    }

    public Claim getClaim(final Location location)
    {
        final Map.Entry<Claim, Faction> regionData = this.getRegionData(location);
        return (regionData == null) ? null : regionData.getKey();
    }

    public Faction getFaction(final Location location)
    {
        final Map.Entry<Claim, Faction> regionData = this.getRegionData(location);
        return (regionData == null) ? null : regionData.getValue();
    }

    public void setFactionAt(final Claim claim, final Faction faction)
    {
        if (faction == null)
        {
            this.boardMap.remove(claim);
        }
        else
        {
            this.boardMap.put(claim, faction);
        }
        this.updateClaim(claim);
    }

    public void updateClaim(final Claim modified)
    {
        final ArrayList<VisualClaim> visualClaims = new ArrayList<VisualClaim>();
        visualClaims.addAll(VisualClaim.getCurrentMaps().values());
        for (final VisualClaim visualClaim : visualClaims)
        {
            if (modified.isWithin(visualClaim.getPlayer().getLocation().getBlockX(), visualClaim.getPlayer().getLocation().getBlockZ(), 50, modified.getWorld()))
            {
                visualClaim.draw(true);
                visualClaim.draw(true);
            }
        }
    }

    public void clear(final Faction faction)
    {
        for (final Claim claim : faction.getClaims())
        {
            this.boardMap.remove(claim);
        }
    }

    public boolean isUnclaimed(final Location location)
    {
        return getClaim(location) == null && !this.isWarzone(location);
    }

    public boolean isWarzone(final Location loc)
    {
        return loc.getWorld().getEnvironment() == World.Environment.NORMAL && Math.abs(loc.getBlockX()) <= WARZONE_RADIUS && Math.abs(loc.getBlockZ()) <= WARZONE_RADIUS;
    }

    public boolean isSpawnBufferZone(final Location loc)
    {
        if (loc.getWorld().getEnvironment() != World.Environment.NORMAL)
        {
            return false;
        }
        final int radius = 300;
        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();
        return x < radius && x > -radius && z < radius && z > -radius;
    }

    public boolean isNetherBufferZone(final Location loc)
    {
        if (loc.getWorld().getEnvironment() != World.Environment.NETHER)
        {
            return false;
        }
        final int radius = 150;
        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();
        return x < radius && x > -radius && z < radius && z > -radius;
    }

    public boolean isUnclaimedOrRaidable(final Location loc)
    {
        final Faction owner = getFaction(loc);
        return owner == null || owner.isRaidable();
    }
}
