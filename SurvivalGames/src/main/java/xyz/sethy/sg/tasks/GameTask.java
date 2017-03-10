package xyz.sethy.sg.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.scoreboard.ScoreboardThread;
import xyz.sethy.sg.states.GameState;
import xyz.sethy.sg.timers.Timer;
import xyz.sethy.sg.timers.TimerType;

/**
 * Created by sethm on 22/12/2016.
 */
public class GameTask
{
    private int timeLeft;
    private boolean gameWon;
    private Player winner;
    int k = 0;
    int l = 0;

    public GameTask()
    {
        this.timeLeft = 900;
        l = Bukkit.getScheduler().scheduleSyncRepeatingTask(SG.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                SG.getInstance().setGameState(GameState.INGAME);
                if (gameWon)
                {
                    Bukkit.getScheduler().cancelTask(l);
                }
                if (timeLeft == 816)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP enabled in &75&c seconds."));
                }
                if (timeLeft == 815)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP enabled in &74&c seconds."));
                }
                if (timeLeft == 814)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP enabled in &73&c seconds."));
                }
                if (timeLeft == 813)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP enabled in &72&c seconds."));
                }
                if (timeLeft == 812)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP enabled in &71&c seconds."));
                }
                if (timeLeft == 811)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPvP has been enabled."));
                    for (Player player : SG.getInstance().getPlayers())
                    {
                        SG.getInstance().getTimerHandler().addTimer(new Timer(TimerType.FEAST, 600000 + System.currentTimeMillis(), player), player);
                    }
                }
                if (timeLeft == 216)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast will spawn at 0,0 in &35 seconds."));
                }
                if (timeLeft == 215)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast will spawn at 0,0 in &34 seconds."));
                }
                if (timeLeft == 214)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast will spawn at 0,0 in &33 seconds."));
                }
                if (timeLeft == 213)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast will spawn at 0,0 in &32 seconds."));
                }
                if (timeLeft == 212)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast will spawn at 0,0 in &31 seconds."));
                }
                if (timeLeft == 211)
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The feast has spawned at 0,0."));
                    SG.getInstance().renderFeast();
                    SG.getInstance().setBorderSize(500);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3The world border will be shrinking by 50 blocks every 30 seconds."));
                    for (Player player : SG.getInstance().getPlayers())
                    {
                        SG.getInstance().getTimerHandler().addTimer(new Timer(TimerType.BORDER, Integer.MAX_VALUE + System.currentTimeMillis(), player), player);
                        SG.getInstance().getTimerHandler().setTimeUntilBorder(30000 + System.currentTimeMillis());
                    }
                    ScoreboardThread.startBorderTask();
                }
                timeLeft--;
            }
        }, 20L, 20L);


    }

    public void setGameWon(boolean gameWon)
    {
        this.gameWon = gameWon;
        User user = API.getUserManager().findByUniqueId(winner.getUniqueId());
        user.getSGUser().setWins(user.getSGUser().getWins() + 1);
        if (gameWon)
        {
            k = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SG.getInstance().getPlugin(), new BukkitRunnable()
            {
                int i = 10;

                @Override
                public void run()
                {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3" + winner.getName() + "&c has won!"));
                    if (i == 0)
                    {
                        SG.getInstance().disable();
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(SG.getInstance().getPlugin(), new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Bukkit.shutdown();
                            }
                        }, 4 * 20L);
                    }
                    i--;
                }
            }, 0L, 20L);
        }
    }

    public boolean hasBeenWon()
    {
        return gameWon;
    }

    public void setWinner(Player winner)
    {
        this.winner = winner;
    }
}
