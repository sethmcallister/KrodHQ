package xyz.sethy.hub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.sethy.hub.items.ClockItems;
import xyz.sethy.hub.items.HubItems;
import xyz.sethy.hub.listener.*;
import xyz.sethy.hub.queue.PlayerQueue;
import xyz.sethy.hub.scoreboard.ScoreboardHandler;
import xyz.sethy.hub.scoreboard.ScoreboardThread;
import xyz.sethy.hub.tasks.PlayersOnlineTask;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 23/12/2016.
 */
public class Hub
{
    private Plugin plugin;
    private static Hub instance;
    private PlayerQueue playerQueue;
    private HubItems hubItems;
    private ClockItems clockItems;
    private LinkedList<Player> players;
    private ScoreboardHandler scoreboardHandler;
    private final AtomicInteger hcfOnline = new AtomicInteger();
    private final AtomicInteger kitmapOnline = new AtomicInteger();

    public Hub(Plugin plugin)
    {
        this.plugin = plugin;
        instance = this;
        this.playerQueue = new PlayerQueue();
        this.hubItems = new HubItems();
        this.clockItems = new ClockItems();
        this.players = new LinkedList<>();
        this.scoreboardHandler = new ScoreboardHandler();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new ScoreboardThread(), 3L, 3L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new PlayersOnlineTask(), 5L, 5L);

        registerListeners();

        hcfOnline.set(0);
    }

    private void registerListeners()
    {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerCombatListener(), plugin);
    }

    public static Hub getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public PlayerQueue getPlayerQueue()
    {
        return playerQueue;
    }

    public HubItems getHubItems()
    {
        return hubItems;
    }

    public LinkedList<Player> getPlayers()
    {
        return players;
    }

    public ClockItems getClockItems()
    {
        return clockItems;
    }

    public ScoreboardHandler getScoreboardHandler()
    {
        return scoreboardHandler;
    }

    public AtomicInteger getHcfOnline()
    {
        return hcfOnline;
    }

    public AtomicInteger getKitmapOnline()
    {
        return kitmapOnline;
    }
}