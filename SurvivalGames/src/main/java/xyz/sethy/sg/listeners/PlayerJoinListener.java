package xyz.sethy.sg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;

/**
 * Created by sethm on 21/12/2016.
 */
public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        SG SG = xyz.sethy.sg.SG.getInstance();
        Player player = event.getPlayer();
        if (SG.getGameState().equals(GameState.GENERATING))
        {
            player.kickPlayer("The world is still generating, please come back later.");
            return;
        }
        if (SG.getGameState().equals(GameState.PREGAME))
        {
            xyz.sethy.sg.SG.getInstance().getPlayers().add(player);
            xyz.sethy.sg.SG.getInstance().getOnline().incrementAndGet();
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 7, 100, 7));
        }
        if (SG.getGameState().equals(GameState.INGAME) || SG.getGameState().equals(GameState.DEATHMATCH))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7This game has already started, You have been placed into spectator mode."));
            SG.getSpectatorHandler().addSpectator(player);
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 7, 100, 7));
            SG.getPlayers().add(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(UserLoggedOutEvent event)
    {
        SG.getInstance().getPlayers().remove(event.getPlayer());
        SG.getInstance().getOnline().decrementAndGet();
        if (SG.getInstance().getSpectatorHandler().isSpectator(event.getPlayer()))
        {
            SG.getInstance().getSpectatorHandler().removeSpectator(event.getPlayer());
            return;
        }
    }
}
