package xyz.sethy.hub.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sethy.hub.Hub;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 21/12/2016.
 */
public class ScoreboardHandler implements Listener
{
    private ConcurrentHashMap<Player, Scoreboard> scoreboards;

    public ScoreboardHandler()
    {
        this.scoreboards = new ConcurrentHashMap<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, Hub.getInstance().getPlugin());
    }

    public Scoreboard getScoreboard(Player player)
    {
        return scoreboards.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        event.getPlayer().setScoreboard(scoreboard);

        Scoreboard scoreboard1 = new Scoreboard(scoreboard, "&3&lKrodHQ");
        scoreboards.put(event.getPlayer(), scoreboard1);
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
