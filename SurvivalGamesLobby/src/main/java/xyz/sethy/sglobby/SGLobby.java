package xyz.sethy.sglobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import xyz.sethy.sglobby.items.LobbyItems;
import xyz.sethy.sglobby.listeners.*;
import xyz.sethy.sglobby.queue.PlayerQueue;
import xyz.sethy.sglobby.scoreboard.ScoreboardHandler;
import xyz.sethy.sglobby.scoreboard.ScoreboardThread;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 22/12/2016.
 */
public class SGLobby
{
    private static SGLobby instance;
    private Plugin plugin;
    private ArrayList<Player> players;
    private ScoreboardHandler scoreboardHandler;
    private final AtomicInteger playingGame;
    private final AtomicInteger games;
    private PlayerQueue playerQueue;
    private LobbyItems lobbyItems;
    private Jedis jedis;
    private ConcurrentHashMap<String, Integer> sgServer;

    public SGLobby(Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.scoreboardHandler = new ScoreboardHandler();
        this.playingGame = new AtomicInteger(0);
        this.games = new AtomicInteger(0);
        this.playerQueue = new PlayerQueue();
        this.lobbyItems = new LobbyItems();
        this.jedis = new Jedis();
        this.sgServer = new ConcurrentHashMap<>();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        this.registerListeners();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new ScoreboardThread(), 1L, 1L);

        this.sgServer.put("sg1", 26690);
//        this.sgServer.put("sg2", 26691);
//        this.sgServer.put("sg3", 26692);
//        this.sgServer.put("sg4", 26693);
    }

    public void registerListeners()
    {
        Bukkit.getServer().getPluginManager().registerEvents(new UserLoggedInListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new DoubleJumpListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WeatherChangeListener(), plugin);
    }

    public static SGLobby getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public ScoreboardHandler getScoreboardHandler()
    {
        return scoreboardHandler;
    }

    public AtomicInteger getPlayingGame()
    {
        return playingGame;
    }

    public PlayerQueue getPlayerQueue()
    {
        return playerQueue;
    }

    public LobbyItems getLobbyItems()
    {
        return lobbyItems;
    }

    public ConcurrentHashMap<String, Integer> getSgServer()
    {
        return sgServer;
    }

    public AtomicInteger getGames()
    {
        return games;
    }
}
