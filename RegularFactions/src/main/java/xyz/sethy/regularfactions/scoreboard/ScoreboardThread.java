package xyz.sethy.regularfactions.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.hub.Hub;
import xyz.sethy.hub.queue.PlayerQueue;
import xyz.sethy.hub.server.Server;

import java.text.DecimalFormat;

/**
 * Created by sethm on 21/12/2016.
 */
public class ScoreboardThread extends BukkitRunnable
{
    private final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private final PlayerQueue playerQueue = Hub.getInstance().getPlayerQueue();

    @Override
    public void run()
    {
        for (Player player : Hub.getInstance().getPlayers())
        {
            Scoreboard scoreboard = Hub.getInstance().getScoreboardHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            scoreboard.clear();
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());

            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));



            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
            scoreboard.update();
        }
    }

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private String translateString(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
