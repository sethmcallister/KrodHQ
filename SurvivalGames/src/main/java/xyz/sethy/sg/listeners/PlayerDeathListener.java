package xyz.sethy.sg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.tasks.GameTask;

/**
 * Created by sethm on 22/12/2016.
 */
public class PlayerDeathListener implements Listener
{
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage(null);
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        User uKilled = API.getUserManager().findByUniqueId(killed.getUniqueId());

        SG.getInstance().getSpectatorHandler().addSpectator(killed);
        killed.setHealthScale(20.0);

        if (killer == null)
        {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3" + killed.getName() + "&4[&c" + uKilled.getSGUser().getKills() + "&4]&7 has died."));
            uKilled.getSGUser().setDeaths(uKilled.getSGUser().getDeaths() + 1);

            int online = SG.getInstance().getOnline().decrementAndGet();
            if (online == 1 || online == 0)
            {
                GameTask gameTask = SG.getInstance().getGameTask();
                if (!gameTask.hasBeenWon())
                {
                    gameTask.setWinner(killed);
                    uKilled.getSGUser().setWins(uKilled.getSGUser().getWins() + 1);
                    gameTask.setGameWon(true);
                }
            }
            return;
        }

        killed.teleport(killer);
        User uKiller = API.getUserManager().findByUniqueId(killer.getUniqueId());

        uKilled.getSGUser().setDeaths(uKilled.getSGUser().getDeaths() + 1);
        uKiller.getSGUser().setKills(uKiller.getSGUser().getKills() + 1);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3" + killed.getName() + "&4[&c" + uKilled.getSGUser().getKills() + "&4]&7 was slain by &3" + killer.getName() + "&4[&c" + uKiller.getSGUser().getKills() + "&4]&7."));


        int online;
        if (!SG.getInstance().getSpectatorHandler().isSpectator(killer))
        {
            online = SG.getInstance().getOnline().decrementAndGet();
        }
        else
        {
            online = SG.getInstance().getOnline().get();

        }

        if (online == 1)
        {
            GameTask gameTask = SG.getInstance().getGameTask();
            uKiller.getSGUser().setWins(uKiller.getSGUser().getWins() + 1);
            gameTask.setWinner(killer);
            gameTask.setGameWon(true);
        }
    }
}
