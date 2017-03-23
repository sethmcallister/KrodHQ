package xyz.sethy.factions.timers;

/**
 * Created by sethm on 05/12/2016.
 */
public enum TimerType
{
    COMBAT_TAG("&c&lSpawn Tag"),
    ENDERPEARL("&e&lEnderpearl"),
    PVP_TIMER("&aPvP Timer"),
    TELEPORT("&9Teleport"),
    ARCHER_TAG("&6&lArcher Mark"),
    ARCHER_COOLDOWN("&4Speed"),
    F_STUCK("&9Stuck"),
    LOGOUT("&4Logout");

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