package xyz.sethy.factions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

/**
 * Created by sethm on 05/01/2017.
 */
public class TabUpdaterTask extends BukkitRunnable
{
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            this.updateTab(player);
        }
    }

    private void updateTab(Player player)
    {
        Scoreboard scoreboard;
        boolean newScoreboard = false;
        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard())
        {
            scoreboard = player.getScoreboard();
        }
        else
        {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            newScoreboard = true;
        }
        Team friendly = this.getExistingOrCreateNewTeam(player, "friendly", scoreboard, ChatColor.DARK_GREEN);
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);

        if(faction == null)
            return;

        friendly.addEntry(player.getName());
        for (Player player1 : Bukkit.getOnlinePlayers())
        {
            if (!player.canSee(player1))
                continue;

            Faction faction1 = Factions.getInstance().getFactionManager().findByPlayer(player1);

            if (faction1 == null || !faction.equals(faction1) || player == player1)
                continue;

            friendly.addEntry(player1.getName());
        }

        if (newScoreboard)
        {
            player.setScoreboard(scoreboard);
        }
    }

    private Team getExistingOrCreateNewTeam(Player player, String string, Scoreboard scoreboard, ChatColor prefix)
    {
        Team toReturn = scoreboard.getTeam(string);
        if (toReturn == null)
        {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(String.valueOf((Object) prefix));
            if (string.equalsIgnoreCase("friendly") || string.equalsIgnoreCase("vanished"))
            {
                toReturn.setCanSeeFriendlyInvisibles(true);
            }
        }
        return toReturn;
    }

}
