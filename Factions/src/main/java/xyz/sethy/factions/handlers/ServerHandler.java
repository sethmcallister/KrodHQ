package xyz.sethy.factions.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.region.RegionData;
import xyz.sethy.factions.dto.region.RegionType;
import xyz.sethy.factions.handlers.dtr.DTRType;

/**
 * Created by sethm on 06/12/2016.
 */
public class ServerHandler
{
    private boolean EOTW;
    private boolean preEOTW;

    public static final PotionEffectType[] DEBUFFS;
    public static final Material[] NO_INTERACT_WITH;
    public static final Material[] NO_INTERACT_WITH_SPAWN;
    public static final Material[] NO_INTERACT;
    public static final Material[] NO_INTERACT_IN_SPAWN;
    public static final Material[] NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS;

    public RegionData getRegion(final Location location)
    {
        return this.getRegion(Factions.getInstance().getLandBoard().getFaction(location), location);
    }

    public RegionData getRegion(final Faction to, final Location location)
    {
        if (to != null && to.getLeader() == null)
        {
            if (to.hasDTRBitmask(DTRType.SAFEZONE))
            {
                return new RegionData(RegionType.SPAWN, to);
            }
            if (to.hasDTRBitmask(DTRType.ROAD))
            {
                return new RegionData(RegionType.ROAD, to);
            }
        }
        if (to != null)
        {
            return new RegionData(RegionType.CLAIMED_LAND, to);
        }
        if (Factions.getInstance().getLandBoard().isWarzone(location))
        {
            return new RegionData(RegionType.WARZONE, null);
        }
        return new RegionData(RegionType.WILDERNESS, null);
    }

    public boolean isEOTW()
    {
        return EOTW;
    }

    public boolean isPreEOTW()
    {
        return preEOTW;
    }

    public void setPreEOTW(boolean preEOTW)
    {
        this.preEOTW = preEOTW;
    }

    public void setEOTW(boolean EOTW)
    {
        this.EOTW = EOTW;
    }

    static
    {
        DEBUFFS = new PotionEffectType[]{PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.WEAKNESS, PotionEffectType.HARM, PotionEffectType.WITHER};
        NO_INTERACT_WITH = new Material[]{Material.LAVA_BUCKET, Material.WATER_BUCKET, Material.BUCKET};
        NO_INTERACT_WITH_SPAWN = new Material[]{Material.SNOW_BALL, Material.ENDER_PEARL, Material.EGG, Material.FISHING_ROD};
        NO_INTERACT = new Material[]{Material.FENCE_GATE, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.CHEST, Material.HOPPER, Material.DISPENSER, Material.WOODEN_DOOR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.TRAPPED_CHEST, Material.TRAP_DOOR, Material.LEVER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.WORKBENCH, Material.BED_BLOCK, Material.ANVIL};
        NO_INTERACT_IN_SPAWN = new Material[]{Material.FENCE_GATE, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.CHEST, Material.HOPPER, Material.DISPENSER, Material.WOODEN_DOOR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.TRAPPED_CHEST, Material.TRAP_DOOR, Material.LEVER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.BED_BLOCK, Material.ANVIL};
        NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS = new Material[]{Material.GLASS, Material.WOOD_DOOR, Material.IRON_DOOR, Material.FENCE_GATE};
    }
}
