package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.sethy.factions.Factions;

/**
 * Created by sethm on 07/12/2016.
 */
public class FoundDiamondsListener implements Listener
{
    public FoundDiamondsListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent event)
    {
        if (!event.isCancelled() && event.getBlock().getType() == Material.DIAMOND_ORE && !event.getBlock().hasMetadata("DiamondPlaced"))
        {
            int diamonds = 0;
            for (int x = -5; x < 5; ++x)
            {
                for (int y = -5; y < 5; ++y)
                {
                    for (int z = -5; z < 5; ++z)
                    {
                        final Block block = event.getBlock().getLocation().add((double) x, (double) y, (double) z).getBlock();
                        if (block.getType() == Material.DIAMOND_ORE && !block.hasMetadata("DiamondPlaced"))
                        {
                            ++diamonds;
                            block.setMetadata("DiamondPlaced", new FixedMetadataValue(Factions.getInstance().getPlugin(), true));
                        }
                    }
                }
            }
            Bukkit.getServer().broadcastMessage("[FD] " + ChatColor.AQUA + event.getPlayer().getName() + " found " + diamonds + " diamond" + ((diamonds == 1) ? "" : "s") + ".");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        ItemStack is = event.getPlayer().getItemInHand();
        if ((is.hasItemMeta()) && (is.getItemMeta().hasDisplayName()) && (is.getItemMeta().getDisplayName().equals(Factions.getInstance().getItemHandler().wrench.getItemMeta().getDisplayName())))
        {
            if (event.getBlock().getType().equals(Material.MOB_SPAWNER))
            {

                ItemStack spawnerStack = new ItemStack(event.getBlock().getType());
                ItemMeta im = spawnerStack.getItemMeta();
                im.setDisplayName(((CreatureSpawner)event.getBlock().getState()).getCreatureTypeName().toUpperCase() + " Spawner");
                spawnerStack.setItemMeta(im);
                spawnerStack.setDurability(((CreatureSpawner)event.getBlock().getState()).getSpawnedType().getTypeId());
                event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), spawnerStack);
                event.getPlayer().getInventory().setItemInHand(null);
                event.getPlayer().updateInventory();
            }
            else
            {
                event.getPlayer().sendMessage("The power of the wrench can only break spawners!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        if ((event.getBlockPlaced().getType().equals(Material.MOB_SPAWNER)) && (event.getItemInHand().hasItemMeta()) && (event.getItemInHand().getItemMeta().hasDisplayName()) && (EntityType.fromName(event.getItemInHand().getItemMeta().getDisplayName().replace(" Spawner", "")) != null))
        {
            ((CreatureSpawner)event.getBlockPlaced().getState()).setCreatureTypeByName(event.getItemInHand().getItemMeta().getDisplayName().replace(" Spawner", ""));
        }
    }
}
