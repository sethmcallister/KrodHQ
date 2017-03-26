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
<<<<<<< HEAD:RegularFactions/src/main/java/xyz/sethy/regularfactions/RegularFactions.java

        regularFactions = this;
=======
        regularFacitons = this;
>>>>>>> d4faab182280b8c37e6da0d998c4b96901e2959c:RegularFactions/src/main/java/xyz/sethy/regularfactions/RegularFacitons.java
        this.scoreboardHandler = new ScoreboardHandler();
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
