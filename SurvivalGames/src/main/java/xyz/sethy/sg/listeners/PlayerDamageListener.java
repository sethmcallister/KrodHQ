package xyz.sethy.sg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.sethy.api.events.ASyncCombatEvent;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;
import xyz.sethy.sg.timers.TimerHandler;
import xyz.sethy.sg.timers.TimerType;

/**
 * Created by sethm on 26/12/2016.
 */
public class PlayerDamageListener implements Listener
{
    @EventHandler
    public void onPlayerDamage(ASyncCombatEvent event)
    {
        if (SG.getInstance().getGameState().equals(GameState.PREGAME))
        {
            event.setCancelled(true);
        }

        if (event.getEntity() instanceof Player)
        {
            Player damager = event.getDamager();
            Player damaged = (Player) event.getEntity();

            if (SG.getInstance().getSpectatorHandler().isSpectator(damager))
            {
                event.setCancelled(true);
            }
            if (SG.getInstance().getSpectatorHandler().isSpectator(damaged))
            {
                event.setCancelled(true);
            }

            TimerHandler timerHandler = SG.getInstance().getTimerHandler();
            if (timerHandler.hasTimer(damager, TimerType.PVP_TIMER))
            {
                damager.sendMessage(ChatColor.YELLOW + "You have PvP Timer.");
                event.setCancelled(true);
            }
            if (timerHandler.hasTimer(damaged, TimerType.PVP_TIMER))
            {
                damager.sendMessage(ChatColor.YELLOW + damaged.getName() + " has PvP Timer.");
                event.setCancelled(true);
            }
        }
    }
}
