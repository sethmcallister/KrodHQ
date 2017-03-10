package xyz.sethy.sg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.timers.Timer;
import xyz.sethy.sg.timers.TimerType;

/**
 * Created by sethm on 21/12/2016.
 */
public class EnderpearlListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))
        {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
            {
                if (SG.getInstance().getTimerHandler().hasTimer(player, TimerType.ENDERPEARL))
                {
                    Timer timer = SG.getInstance().getTimerHandler().getTimerByType(player, TimerType.ENDERPEARL);
                    if (timer.getTime() > 0)
                    {
                        long millisLeft = timer.getTime();
                        double value = millisLeft / 1000.0D;
                        double sec = Math.round(10.0D * value) / 10.0D;
                        event.setCancelled(true);
                        event.getPlayer().updateInventory();
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot use this for another &l" + sec + " seconds&c."));
                        return;
                    }
                }
                SG.getInstance().getTimerHandler().addTimer(new Timer(TimerType.ENDERPEARL, 16000 + System.currentTimeMillis(), player), player);
            }
        }
    }
}
