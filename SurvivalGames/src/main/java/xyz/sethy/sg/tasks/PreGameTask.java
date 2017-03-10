package xyz.sethy.sg.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.states.GameState;
import xyz.sethy.sg.timers.Timer;
import xyz.sethy.sg.timers.TimerType;

/**
 * Created by sethm on 21/12/2016.
 */
public class PreGameTask
{
    int k;
    int l;
    public static boolean forceStart;

    public PreGameTask()
    {
        k = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SG.getInstance().getPlugin(), () ->
        {
            int online = SG.getInstance().getOnline().get();
            if (online > 20 || forceStart)
            {
                l = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SG.getInstance().getPlugin(), new Runnable()
                {
                    int i = 10;

                    @Override
                    public void run()
                    {
                        if (i != 0)
                        {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aGame starting in &7" + i + "&a seconds."));
                            i--;
                            return;
                        }
                        for (Player player : SG.getInstance().getPlayers())
                        {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.getInventory().setItem(0, SG.getInstance().getGameItems().getCompass());
                            player.updateInventory();

                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 1));

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have &71 minute 30 seconds&c of PvP Protection!"));
                            player.setHealthScale(20);
                            player.setFoodLevel(20);
                            player.teleport(new Location(Bukkit.getWorld("world"), 7, 66, 7));
                            Timer timer = new Timer(TimerType.PVP_TIMER, 90000 + System.currentTimeMillis(), player);
                            SG.getInstance().getTimerHandler().addTimer(timer, player);
                            SG.getInstance().setGameState(GameState.INGAME);
                        }
                        GameTask gameTask = new GameTask();
                        SG.getInstance().setGameTask(gameTask);
                        Bukkit.getScheduler().cancelTask(l);
                    }
                }, 0L, 20L);
                Bukkit.getScheduler().cancelTask(k);
                return;
            }
            for (Player player : SG.getInstance().getPlayers())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAnother &7" + (20 - online) + "&c need to join for the game to start."));
            }
        }, 0L, 5 * 20L);
    }
}
