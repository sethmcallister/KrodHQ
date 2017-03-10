package xyz.sethy.factions.listeners.crate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.api.API;
import xyz.sethy.factions.managers.CrateManager;

/**
 * Created by Seth on 07/03/2017.
 */
public class CrateKeyPlaceListener implements Listener
{
    public CrateKeyPlaceListener()
    {
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onCrateKeyPlace(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().equals(CrateManager.getKrodKey()) || event.getBlockPlaced().equals(CrateManager.getElaph()) || event.getBlockPlaced().equals(CrateManager.getKothKey()) || event.getBlockPlaced().equals(CrateManager.getStarterKey()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place crate keys.");
        }
    }

    @EventHandler
    public void onEnderchestInteract(PlayerInteractEvent event)
    {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(event.getClickedBlock() != null)
                if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST))
                    event.setCancelled(true);
        }
    }
}
