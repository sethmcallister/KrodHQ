package xyz.sethy.factions.timers;

/**
 * Created by sethm on 05/12/2016.
 */
public enum TimerType
{
    COMBAT_TAG("&cCombat Tag"),
    ENDERPEARL("&3Enderpearl"),
    PVP_TIMER("&aPvP Timer"),
    TELEPORT("&9Teleport"),
    ARCHER_TAG("&4Archer Tag"),
    ARCHER_COOLDOWN("&4Speed"),
    F_STUCK("&9Stuck");

    private String score;

    TimerType(String score)
    {
        this.score = score;
    }

    public String getScore()
    {
        return score;
    }
}