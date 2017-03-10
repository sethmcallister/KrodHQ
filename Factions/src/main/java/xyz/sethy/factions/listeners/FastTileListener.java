package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;

import java.util.LinkedList;

/**
 * Created by Seth on 07/03/2017.
 */
public class FastTileListener implements Listener
{
    private LinkedList<Furnace> furnaces = new LinkedList<>();
    private LinkedList<BrewingStand> stands = new LinkedList<>();

    public FastTileListener()
    {
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());

        new BukkitRunnable()
        {
            public void run()
            {
                for (Furnace furnace : furnaces)
                {
                    if (furnace.getInventory().getItem(0) != null)
                    {
                        furnace.setCookTime((short)(furnace.getCookTime() + 10));
                        furnace.setBurnTime((short)(furnace.getBurnTime() + 10));
                    }
                    else
                    {
                        furnace.setCookTime((short)0);
                        furnace.setBurnTime((short)0);
                    }
                }
                for (BrewingStand stand : stands)
                {
                    if ((stand.getLocation().getChunk().isLoaded()) && (stand.getBrewingTime() > 1))
                        stand.setBrewingTime(Math.max(1, stand.getBrewingTime() - 10));
                }
            }
        }.runTaskTimer(API.getPlugin(), 2L, 2L);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent e)
    {
        Furnace furnace = (Furnace)e.getFurnace().getState();
        if (!this.furnaces.contains(furnace))
            this.furnaces.add(furnace);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e)
    {
        Furnace furnace = (Furnace)e.getFurnace().getState();
        if (!this.furnaces.contains(furnace))
            this.furnaces.add(furnace);
    }

    @EventHandler
    public void onBrew(BrewEvent e)
    {
        BrewingStand brewingStand = (BrewingStand)e.getBlock().getState();
        if (!this.stands.contains(brewingStand))
            this.stands.add(brewingStand);
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (e.getClickedBlock().getType() == Material.BREWING_STAND)
            {
                BrewingStand brewingStand = (BrewingStand)e.getClickedBlock().getState();
                if (!this.stands.contains(brewingStand))
                    this.stands.add(brewingStand);
            }
            if ((e.getClickedBlock().getType() == Material.FURNACE) || (e.getClickedBlock().getType() == Material.BURNING_FURNACE))
            {
                Furnace furnace = (Furnace)e.getClickedBlock().getState();
                if (!this.furnaces.contains(furnace))
                    this.furnaces.add(furnace);
            }
        }
    }
}
