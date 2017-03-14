package xyz.sethy.factions.listeners.fixes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.api.API;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.TimerType;

/**
 * Created by Seth on 12/03/2017.
 */
public class CombatWallFix implements Listener
{
    public CombatWallFix()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Block clicked = event.getClickedBlock();
            if(clicked != null)
            {
                if(clicked.getType().equals(Material.GLASS))
                {
                    if(Factions.getInstance().getTimerHandler().hasTimer(event.getPlayer(), TimerType.COMBAT_TAG))
                        event.setCancelled(true);
                }
            }
        }
    }
}
