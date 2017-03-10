package xyz.sethy.sg.states;

/**
 * Created by sethm on 21/12/2016.
 */
public enum GameState
{
    PREGAME("&a&lPre-Game"),
    INGAME("&a&lIn-Game"),
    DEATHMATCH("&c&lDeath Match"),
    CLOSING("&4&lClosing"),
    GENERATING("&4&lGenerating");

    private String displayName;

    GameState(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
