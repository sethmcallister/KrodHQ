package xyz.sethy.factions.support.wg;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import xyz.sethy.factions.support.Region;

/**
 * Created by sethm on 06/01/2017.
 */
public class WorldGuardRegion implements Region
{
    private ProtectedRegion protectedRegion;
    private String world;

    public WorldGuardRegion(String world, ProtectedRegion protectedRegion)
    {
        this.protectedRegion = protectedRegion;
        this.world = world;
    }

    @Override
    public int getMinX()
    {
        return this.protectedRegion.getMinimumPoint().getBlockX();
    }

    @Override
    public int getMaxX()
    {
        return this.protectedRegion.getMaximumPoint().getBlockX();
    }

    @Override
    public int getMinZ()
    {
        return this.protectedRegion.getMinimumPoint().getBlockZ();
    }

    @Override
    public int getMaxZ()
    {
        return this.protectedRegion.getMaximumPoint().getBlockZ();
    }

    @Override
    public int getMinY()
    {
        return this.protectedRegion.getMinimumPoint().getBlockY();
    }

    @Override
    public int getMaxY()
    {
        return this.protectedRegion.getMaximumPoint().getBlockY();
    }

    @Override
    public String getWorld()
    {
        return this.world;
    }

    @Override
    public boolean canCombatTagedPlayerEnter()
    {
        return this.protectedRegion.getFlag(DefaultFlag.PVP) != StateFlag.State.DENY;
    }

    @Override
    public boolean shouldPvPTimerFreeze()
    {
        return this.protectedRegion.getFlag(DefaultFlag.PVP) != StateFlag.State.DENY;
    }


    @Override
    public boolean canPvPTimedPlayerEnter(Player player)
    {
        return true;
    }
}
