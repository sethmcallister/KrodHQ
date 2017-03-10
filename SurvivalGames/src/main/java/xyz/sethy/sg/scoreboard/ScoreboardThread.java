package xyz.sethy.sg.scoreboard;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.sg.SG;
import xyz.sethy.sg.timers.Timer;
import xyz.sethy.sg.timers.TimerHandler;
import xyz.sethy.sg.timers.TimerType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 21/12/2016.
 */
public class ScoreboardThread extends BukkitRunnable
{
    private final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private final DecimalFormat secondFormat = new DecimalFormat("0");
    private final TimerHandler timerHandler = SG.getInstance().getTimerHandler();
    private static int k = 0;

    @Override
    public void run()
    {
        for (Player player : SG.getInstance().getPlayers())
        {
            Scoreboard scoreboard = SG.getInstance().getScoreboardHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            if (timerHandler.getActiveTimers(player) == null)
            {
                timerHandler.getTimers().put(player, new ArrayList<>());
                continue;
            }

            scoreboard.clear();
            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
            scoreboard.add(translateString(SG.getInstance().getGameState().getDisplayName()), "");
            if (SG.getInstance().getSpectatorHandler().isSpectator(player))
            {
                scoreboard.add(translateString("&7You are curren"), translateString("&7ly spectating"));
            }
            scoreboard.add(translateString("&9&lPlayers: "), SG.getInstance().getOnline().get() + "");


            if (timerHandler.hasTimer(player, TimerType.PVP_TIMER))
            {
                Timer timer = timerHandler.getTimerByType(player, TimerType.PVP_TIMER);
                if (timer != null)
                {
                    if (timer.getTime() < 60000)
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), format(timer.getTime()));
                    }
                    else
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), setLongFormat(timer.getTime()));
                    }
                }
            }
            if (timerHandler.hasTimer(player, TimerType.TIME))
            {
                Timer timer = timerHandler.getTimerByType(player, TimerType.TIME);
                if (timer != null)
                {
                    if (timer.getTime() < 60000)
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), format(timer.getTime()));
                    }
                    else
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), setLongFormat(timer.getTime()));
                    }
                }
            }
            if (timerHandler.hasTimer(player, TimerType.BORDER))
            {
                Timer timer = timerHandler.getTimerByType(player, TimerType.BORDER);

                long timeLeft = timerHandler.getTimeUntilBorder();

                if (timer != null)
                {
                    if (timeLeft == Integer.MAX_VALUE)
                    {
                        scoreboard.add(translateString(TimerType.BORDER.getScore()), SG.getInstance().getBorderSize() + "");
                    }
                    else
                    {
                        scoreboard.add(translateString(TimerType.BORDER.getScore()), SG.getInstance().getBorderSize() + "(" + formatSecond(timeLeft) + ")");
                    }
                }
            }
            if (timerHandler.hasTimer(player, TimerType.FEAST))
            {
                Timer timer = timerHandler.getTimerByType(player, TimerType.FEAST);
                if (timer != null)
                {
                    if (timer.getTime() < 60000)
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), format(timer.getTime()));
                    }
                    else
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), setLongFormat(timer.getTime()));
                    }
                }
            }
            if (timerHandler.hasTimer(player, TimerType.ENDERPEARL))
            {
                Timer timer = timerHandler.getTimerByType(player, TimerType.ENDERPEARL);
                if (timer != null)
                {
                    if (timer.getTime() < 60000)
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), format(timer.getTime()));
                    }
                    else
                    {
                        scoreboard.add(translateString(timer.getTimerType().getScore()), setLongFormat(timer.getTime()));
                    }
                }
            }
            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
            scoreboard.update();
        }
    }

    public static void startBorderTask()
    {
        k = Bukkit.getScheduler().scheduleSyncRepeatingTask(SG.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                long timeLeft = SG.getInstance().getTimerHandler().getTimeUntilBorder();
                if (timeLeft >= -100 && timeLeft <= 100)
                {
                    int border = SG.getInstance().getBorderSize() - 50;
                    SG.getInstance().setBorderSize(border);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3The world border radius is now &7" + border + "&3."));
                    SG.getInstance().getTimerHandler().setTimeUntilBorder(30000 + System.currentTimeMillis());
                    if (border == 100)
                    {
                        SG.getInstance().getTimerHandler().setTimeUntilBorder(Integer.MAX_VALUE);
                        Bukkit.getScheduler().cancelTask(k);
                    }
                }
            }
        }, 1L, 1L);
    }


    private String formatSecond(long millisecond)
    {
        return secondFormat.format(millisecond / 1000.0D);
    }

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
        {
            return FORMAT.format(paramMilliseconds);
        }
        return DurationFormatUtils.formatDuration(paramMilliseconds, (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }

    private String translateString(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
