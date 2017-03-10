package xyz.sethy.factions.handlers.dtr;

import org.bukkit.Location;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

/**
 * Created by sethm on 02/12/2016.
 */
public enum DTRType
{
    SAFEZONE(1, "Safe-Zone", "Clear from PvP."),
    ROAD(2, "Road", "A road faction."),
    KOTH(4, "KoTH", "A KoTH faction."),
    CAP_ZONE(8, "Cap-Zone", "A KoTH capzone.");

    private int bitmask;
    private String name;
    private String description;

    DTRType(final int bitmask, final String name, final String description)
    {
        this.bitmask = bitmask;
        this.name = name;
        this.description = description;
    }

    public boolean appliesAt(final Location location)
    {
        final Faction ownerTo = Factions.getInstance().getLandBoard().getFaction(location);
        return ownerTo != null && ownerTo.getLeader() == null && ownerTo.hasDTRBitmask(this);
    }

    public int getBitmask()
    {
        return this.bitmask;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }
}
