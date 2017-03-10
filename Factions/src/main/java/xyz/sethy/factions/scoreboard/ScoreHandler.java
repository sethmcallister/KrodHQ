package xyz.sethy.factions.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import xyz.sethy.factions.Factions;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 05/12/2016.
 */
public class ScoreHandler implements Listener
{
    private static ConcurrentHashMap<Player, FScoreboard> scoreboards = new ConcurrentHashMap<>();

    public ScoreHandler()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    public FScoreboard getScoreboard(Player player)
    {
        return scoreboards.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        event.getPlayer().setScoreboard(scoreboard);

        FScoreboard fScoreboard = Factions.getInstance().isKitmap() ? new FScoreboard(scoreboard, "&3&lKrodHQ &7Kits") : new FScoreboard(scoreboard, "&3KrodHQ &7(Map 1)");
        scoreboards.put(event.getPlayer(), fScoreboard);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        scoreboards.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        scoreboards.remove(event.getPlayer());
    }
}
