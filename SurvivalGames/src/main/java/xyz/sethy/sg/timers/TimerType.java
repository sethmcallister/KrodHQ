package xyz.sethy.sg.timers;

/**
 * Created by sethm on 21/12/2016.
 */
public enum TimerType
{
    PVP_TIMER("&a&lPvP Timer: "),
    TIME("&b&lTime Left: "),
    ENDERPEARL("&3&lEnderpearl: "),
    BORDER("&a&lBorder: "),
    FEAST("&d&lFeast: ");

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
