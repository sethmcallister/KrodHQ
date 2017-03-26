package xyz.sethy.regularfactions;


import xyz.sethy.regularfactions.commands.SetWarpCommand;
import xyz.sethy.regularfactions.commands.WarpCommand;
import xyz.sethy.regularfactions.scoreboard.ScoreboardHandler;

/**
 * Created by Seth on 26/03/2017.
 */
public class RegularFactions
{
    private static RegularFactions regularFactions;
    private final ScoreboardHandler scoreboardHandler;

    public RegularFactions()

    {

        regularFactions = this;
this.scoreboardHandler = new ScoreboardHandler();
this.registerCommands();
    }

    public static RegularFactions getInstance()
    {
        return regularFactions;
    }

    public ScoreboardHandler getScoreboardHandler()
    {
        return scoreboardHandler;
    }

    public void registerCommands(){
        new WarpCommand();
        new SetWarpCommand();
    }
}
