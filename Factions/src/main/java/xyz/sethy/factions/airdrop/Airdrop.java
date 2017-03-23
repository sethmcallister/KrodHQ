package xyz.sethy.factions.airdrop;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftChest;

/**
 * Created by Seth on 23/03/2017.
 */
public class Airdrop
{
    private Location location;
    private Chest chest;

    public Airdrop(Location location)
    {
        this.chest = new CraftChest(location.getBlock());
    }

    public Location getLocation()
    {
        return location;
    }

    public Chest getChest()
    {
        return chest;
    }
}
