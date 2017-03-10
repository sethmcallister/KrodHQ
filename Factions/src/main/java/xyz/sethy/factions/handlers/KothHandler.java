package xyz.sethy.factions.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.sethy.factions.koth.dto.Koth;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 26/12/2016.
 */
public class KothHandler
{
    private final LinkedList<Koth> koths;
    private final LinkedList<Koth> activeKoths;
    private final ConcurrentHashMap<Player, Koth> creatingKoth;

    public KothHandler()
    {
        this.koths = new LinkedList<>();
        this.activeKoths = new LinkedList<>();
        this.creatingKoth = new ConcurrentHashMap<>();
    }

    public boolean isInCapZone(final Location location)
    {
        boolean result = false;
        for (Koth koth : koths)
        {
            Location min = koth.getKoTHLocations().get(0);
            Location max = koth.getKoTHLocations().get(1);
            if (location.getWorld().getName().equals(min.getWorld().getName()))
            {
                if ((location.getBlockX() >= min.getBlockX()) && (location.getBlockY() >= min.getBlockY()) && (location.getBlockZ() >= min.getBlockZ()))
                {
                    if ((location.getBlockX() <= max.getBlockX()) && (location.getBlockY() <= max.getBlockY()) && (location.getBlockZ() <= max.getBlockZ()))
                    {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public Koth getKothByName(final String name)
    {
        for (Koth koth : koths)
        {
            if (koth.getName().get().equalsIgnoreCase(name))
                return koth;
        }
        return null;
    }

    public Koth getKothAtLocation(final Location location)
    {
        for (Koth koth : koths)
        {
            Location min = koth.getKoTHLocations().get(0);
            Location max = koth.getKoTHLocations().get(1);
            if (location.getWorld().getName().equals(min.getWorld().getName()))
            {
                if ((location.getBlockX() >= min.getBlockX()) && (location.getBlockY() >= min.getBlockY()) && (location.getBlockZ() >= min.getBlockZ()))
                {
                    if ((location.getBlockX() <= max.getBlockX()) && (location.getBlockY() <= max.getBlockY()) && (location.getBlockZ() <= max.getBlockZ()))
                    {
                        return koth;
                    }
                }
            }
        }
        return null;
    }

    public ConcurrentHashMap<Player, Koth> getCreatingKoth()
    {
        return creatingKoth;
    }

    public Koth getCreationKoth(final Player player)
    {
        return creatingKoth.get(player);
    }

    public LinkedList<Koth> getKoths()
    {
        return koths;
    }

    public LinkedList<Koth> getActiveKoths()
    {
        return activeKoths;
    }
}
