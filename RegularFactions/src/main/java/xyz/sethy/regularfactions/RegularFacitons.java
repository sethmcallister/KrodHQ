package xyz.sethy.regularfactions;

import xyz.sethy.regularfactions.scoreboard.ScoreboardHandler;

/**
 * Created by Seth on 26/03/2017.
 */
public class RegularFacitons
{
    private static RegularFacitons regularFacitons;
    private final ScoreboardHandler scoreboardHandler;

    public RegularFacitons()
    {
        regularFacitons = this;
        this.scoreboardHandler = new ScoreboardHandler();
    }

    public static RegularFacitons getInstance()
    {
        return regularFacitons;
    }

    public ScoreboardHandler getScoreboardHandler()
    {
        return scoreboardHandler;
    }
}
