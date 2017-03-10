package xyz.sethy.factions.support.wg;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import xyz.sethy.factions.support.Region;
import xyz.sethy.factions.support.RegionSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sethm on 06/01/2017.
 */
public class WorldGuardRegionSupport implements RegionSupport
{
    @Override
    public List<Region> getRegionsAt(Location location)
    {
        ArrayList<Region> regions = new ArrayList<>();
        for (ProtectedRegion region : WGBukkit.getPlugin().getRegionManager(location.getWorld()).getApplicableRegions(location))
        {
            if (region == null)
                continue;

            regions.add(new WorldGuardRegion(location.getWorld().getName(), region));
        }
        return regions;
    }

    @Override
    public List<Region> getNearByRegion(Location location)
    {
        ArrayList<Region> regions = new ArrayList<>();
        for (ProtectedRegion region : WGBukkit.getPlugin().getRegionManager(location.getWorld()).getRegions().values())
        {
            if (region == null) continue;
            regions.add(new WorldGuardRegion(location.getWorld().getName(), region));
        }
        return regions;
    }
}
