package xyz.sethy.factions.support;

import org.bukkit.Location;

import java.util.List;

/**
 * Created by sethm on 06/01/2017.
 */
public interface RegionSupport
{
    List<Region> getRegionsAt(Location var1);

    List<Region> getNearByRegion(Location var1);
}
