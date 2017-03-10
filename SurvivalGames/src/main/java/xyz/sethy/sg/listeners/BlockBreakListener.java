package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;

/**
 * Created by sethm on 29/12/2016.
 */
public class BlockBreakListener implements Listener
{
    public BlockBreakListener()
    {
        Bukkit.getPluginManager().registerEvents(this, SG.getInstance().getPlugin());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (SG.getInstance().getGameState().equals(GameState.PREGAME) || SG.getInstance().getSpectatorHandler().isSpectator(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (SG.getInstance().getGameState().equals(GameState.PREGAME) || SG.getInstance().getSpectatorHandler().isSpectator(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }
}
