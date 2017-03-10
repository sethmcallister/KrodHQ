package xyz.sethy.factions.dto.region;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.factions.dto.Faction;

/**
 * Created by sethm on 06/12/2016.
 */
public class RegionData
{
    private RegionType regionType;
    private Faction data;

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null || !(obj instanceof RegionData))
            return false;
        final RegionData other = (RegionData) obj;
        return other.regionType == this.regionType && (this.data == null || other.data.equals(this.data));
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    public String getName(final Player player)
    {
        if (this.data != null)
            return this.data.getName(player);
        switch (this.regionType)
        {
            case WARZONE:
                return ChatColor.RED + "Warzone";
            case WILDERNESS:
                return ChatColor.GRAY + "The Wilderness";
            default:
                return ChatColor.DARK_RED + "N/A";
        }
    }

    public RegionData(final RegionType regionType, final Faction data)
    {
        super();
        this.regionType = regionType;
        this.data = data;
    }

    public RegionType getRegionType()
    {
        return this.regionType;
    }

    public Faction getData()
    {
        return this.data;
    }

    public void setRegionType(final RegionType regionType)
    {
        this.regionType = regionType;
    }

    public void setData(final Faction data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "RegionData(regionType=" + this.getRegionType() + ", data=" + this.getData() + ")";
    }

}
