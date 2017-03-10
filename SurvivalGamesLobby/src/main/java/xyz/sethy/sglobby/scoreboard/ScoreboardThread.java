package xyz.sethy.sglobby.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.sglobby.SGLobby;
import xyz.sethy.sglobby.queue.PlayerQueue;

import java.text.DecimalFormat;

/**
 * Created by sethm on 21/12/2016.
 */
public class ScoreboardThread extends BukkitRunnable
{
    private final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private final PlayerQueue playerQueue = SGLobby.getInstance().getPlayerQueue();

    @Override
    public void run()
    {
        for (Player player : SGLobby.getInstance().getPlayers())
        {
            Scoreboard scoreboard = SGLobby.getInstance().getScoreboardHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            scoreboard.clear();
            SGUser user = API.getUserManager().findByUniqueId(player.getUniqueId()).getSGUser();

            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"), translateString("&7&m-----------"));
            scoreboard.add(translateString("&3Playing&7: "), Integer.toString(SGLobby.getInstance().getPlayingGame().get()));
            scoreboard.add(translateString("&3Games&7: "), Integer.toString(SGLobby.getInstance().getGames().get()));
            scoreboard.add(translateString("&3In Queue&7: "), Integer.toString(playerQueue.getQueueSize()));
            if (SGLobby.getInstance().getPlayerQueue().isQueueing(player))
            {
                scoreboard.add("", "");

                long waitingSince = playerQueue.getWaitingSince(player);
                long time = System.currentTimeMillis() - waitingSince;

                double avarageWait = playerQueue.getPos(player) / 2.0;

                scoreboard.add(translateString("&aSearching... "), translateString("&f(" + format(time) + ")"));
                scoreboard.add(translateString("&aWait Time&7: "), translateString(Double.toString(avarageWait)));
            }
            scoreboard.add("", "");
            scoreboard.add(translateString("&cWins&7: "), Integer.toString(user.getWins()));
            scoreboard.add(translateString("&cLoses&7: "), Integer.toString(user.getLoses()));
            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"), translateString("&7&m-----------"));
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
