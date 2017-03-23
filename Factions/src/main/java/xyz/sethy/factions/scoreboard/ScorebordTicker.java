package xyz.sethy.factions.scoreboard;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.koth.dto.Koth;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerHandler;
import xyz.sethy.factions.timers.TimerType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 05/12/2016.
 */
public class ScorebordTicker implements Runnable
{
    private final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private TimerHandler timerHandler = Factions.getInstance().getTimerHandler();
    private ConcurrentHashMap<Player, Long> lastPvPTime = new ConcurrentHashMap<>();

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private ConcurrentSkipListSet<String> splitEqually(final String text, final int size)
    {
        ConcurrentSkipListSet<String> ret = new ConcurrentSkipListSet<>();

        for (int start = 0; start < text.length(); start += size)
        {
            if (ret.size() > 0)
            {
                ret.add(text.substring(start, Math.min(text.length(), start + size)));
            }
            else
                ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    private String formatTime(long time)
    {
        if (time > 60000L)
        {
            return setLongFormat(time);
        }
        else
        {
            return format(time);
        }
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

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            FScoreboard scoreboard = Factions.getInstance().getScoreHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            scoreboard.clear();

            if (timerHandler.getPlayerTimers(player) == null)
            {
                timerHandler.getTimers().put(player, new ArrayList<>());
                scoreboard.update();
                continue;
            }

            if(hasAnyTimers(player))
            {
                scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user.isStaffMode())
                {
                    scoreboard.add(translateString("&9Staff Mode: "), translateString(user.isStaffMode() ? "&aEnabled" : "&cDisabled"));
                    scoreboard.add(translateString(" &9Vanish: "), translateString(user.isVanished() ? "&aEnabled" : "&cDisabled"));
                    scoreboard.add(translateString(" &9Chat Mode: "), translateString(user.isStaffChat() ? "&cStaff Chat" : "&cPublic Chat"));
                }

                if(Factions.getInstance().isKitmap())
                {
                    KitmapUser kitmapUser = API.getUserManager().findKitmapByUniqueId(player.getUniqueId());
                    int kills = kitmapUser.getKills();
                    int deaths = kitmapUser.getDeaths();
                    int ks = kitmapUser.getCurrentKillStreak();

                    scoreboard.add(translateString("&7Kills&7: "), String.valueOf(kills));
                    scoreboard.add(translateString("&7Deaths&7: "), String.valueOf(deaths));
                    scoreboard.add(translateString("&7Kill Streak"), translateString("&7:&f ") + String.valueOf(ks));
                }

                if(timerHandler.getSotwTime() > System.currentTimeMillis())
                {
                    String right = translateString("&3&lSOTW&7: ");
                    String left = formatTime(timerHandler.getSotwTime() - System.currentTimeMillis());
                    scoreboard.add(right, left);
                }

                for (Koth koth : Factions.getInstance().getKothHandler().getActiveKoths())
                {
                    long time;
                    if (koth.getCapper().get() == null)
                        time = koth.getTimeRemaining().get();
                    else
                        time = koth.getTimeRemaining().get() - System.currentTimeMillis();

                    if (time <= 100L)
                    {
                        koth.captured();
                        continue;
                    }

                    ConcurrentSkipListSet<String> lol1 = this.splitEqually(koth.getName().get().replace("Capzone", ""), 15);
                    if (lol1.size() == 1)
                    {
                        String left = translateString("" + lol1.toArray()[0]);
                        scoreboard.add(translateString("&3&l") + left, translateString("&7:&f ") + formatTime(time));
                    }
                    else if (lol1.size() == 2)
                    {
                        String left = translateString("" + lol1.toArray()[0]);
                        String right = ChatColor.getLastColors(left) + translateString((String) lol1.toArray()[1]);

                        scoreboard.add(translateString("&3&l") + left, right + translateString("&7:&f ") + formatTime(time));
                    }
                }
                ArrayList<Timer> defaultTimers = timerHandler.getPlayerTimers(player);
                for (Timer timer : defaultTimers)
                {
                    if (timer.getTime() > 0)
                    {
                        String left = translateString(timer.getTimerType().getScore());
                        String right;

                        if(timer.getTimerType().equals(TimerType.PVP_TIMER))
                        {
                            if(!timer.isFrozen())
                            {
                                if(this.lastPvPTime.containsKey(player))
                                    this.lastPvPTime.put(player, timer.getTime());
                                else
                                    this.lastPvPTime.put(player, 1800000L + System.currentTimeMillis());
                            }
                        }

                        if(timer.isFrozen())
                        {
                            if(!this.lastPvPTime.containsKey(player))
                                continue;

                            right = translateString("&7:&c ") + formatTime(this.lastPvPTime.get(player));
                        }
                        else
                            right = translateString("&7:&c ") + formatTime(timer.getTime());

                        scoreboard.add(left, right);
                    }
                }
                scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
            }
            scoreboard.update();
        }
    }


    private boolean hasAnyTimers(Player player)
    {
        if(Factions.getInstance().getKothHandler().getActiveKoths().size() > 0)
            return true;

        if(Factions.getInstance().isKitmap())
            return true;

        ArrayList<Timer> defaultTimers = timerHandler.getPlayerTimers(player);

        User user = API.getUserManager().findByUniqueId(player.getUniqueId());

        if(user.isStaffMode())
            return true;

        int i = 0;
        for (Timer timer : defaultTimers)
        {
            if (timer.getTime() > 0)
            {
                i++;
            }
        }
        if(i > 0)
            return true;

        if(timerHandler.getSotwTime() > System.currentTimeMillis())
            return true;

        return false;
    }
}
