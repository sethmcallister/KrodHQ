package xyz.sethy.sglobby.scoreboard;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.LinkedList;

/**
 * Created by sethm on 21/12/2016.
 */
public class Scoreboard
{
    public static class ScoreboardText
    {
        private String left;
        private String middle;
        private String right;

        public ScoreboardText(String left, String middle, String right)
        {
            this.left = left;
            this.middle = middle;
            this.right = right;
        }

        public ScoreboardText(String left, String right)
        {
            this.left = left;
            this.right = right;
        }


        public String getLeft()
        {
            return this.left;
        }

        public void setLeft(String left)
        {
            this.left = left;
        }

        public String getRight()
        {
            return this.right;
        }

        public void setRight(String right)
        {
            this.right = right;
        }

        public String getMiddle()
        {
            return this.middle;
        }

        public void setMiddle(String middle)
        {
            this.middle = middle;
        }
    }

    private LinkedList<ScoreboardText> texts = new LinkedList<>();
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;
    private String tag = "PlaceHolder";
    private int lastSentCount = -1;


    public Scoreboard(org.bukkit.scoreboard.Scoreboard scoreBoard)
    {
        this.scoreboard = scoreBoard;
        this.objective = getOrCreateObjective(this.tag);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard(org.bukkit.scoreboard.Scoreboard scoreBoard, String title)
    {
        Preconditions.checkState(title.length() <= 32, "title can not be more than 32");

        this.tag = ChatColor.translateAlternateColorCodes('&', title);
        this.scoreboard = scoreBoard;
        this.objective = getOrCreateObjective(this.tag);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void add(String left, String right)
    {
        Preconditions.checkState(left.length() <= 16, "left can not be more than 16");
        Preconditions.checkState(right.length() <= 16, "right can not be more than 16");
        this.texts.add(new ScoreboardText(left, right));
    }

    public void add(String left, String middle, String right)
    {
        Preconditions.checkState(left.length() <= 16, "left can not be more than 16");
        Preconditions.checkState(middle.length() <= 16, "left cannot be more than 16");
        Preconditions.checkState(right.length() <= 16, "right can not be more than 16");
        this.texts.add(new ScoreboardText(left, middle, right));
    }

    public void set(int index, String left, String right)
    {
        Preconditions.checkState(left.length() <= 16, "left can not be more than 16");
        Preconditions.checkState(right.length() <= 16, "right can not be more than 16");
        this.texts.set(index, new ScoreboardText(left, right));
    }

    public void clear()
    {
        this.texts.clear();
    }

    public void remove(int index)
    {
        String name = getNameForIndex(index);
        this.scoreboard.resetScores(name);
        Team team = getOrCreateTeam(ChatColor.stripColor(this.tag) + index, index);
        team.unregister();
    }

    public void update()
    {
        for (int i = 0; i < this.texts.size(); i++)
        {
            Team team = getOrCreateTeam(ChatColor.stripColor(this.tag) + i, i);
            ScoreboardText str = this.texts.get(this.texts.size() - i - 1);
            team.setPrefix(str.getLeft());
            team.setSuffix(str.getRight());
            this.objective.getScore(getNameForIndex(i)).setScore(i + 1);
        }
        if (this.lastSentCount != -1)
        {
            int sentCount = this.texts.size();
            for (int i = 0; i < this.lastSentCount - sentCount; i++)
            {
                remove(sentCount + i);
            }
        }
        this.lastSentCount = this.texts.size();
    }

    public Team getOrCreateTeam(String team, int i)
    {
        Team value = this.scoreboard.getTeam(team);
        if (value == null)
        {
            value = this.scoreboard.registerNewTeam(team);
            value.addEntry(getNameForIndex(i));
        }
        return value;
    }

    public Objective getOrCreateObjective(String objective)
    {
        Objective value = this.scoreboard.getObjective("dummyhubobj");
        if (value == null)
        {
            value = this.scoreboard.registerNewObjective("dummyhubobj", "dummy");
        }
        value.setDisplayName(objective);
        return value;
    }

    public String getNameForIndex(int index)
    {
        return ChatColor.values()[index].toString() + ChatColor.RESET;
    }
}
