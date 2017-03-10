package xyz.sethy.hub.server;

/**
 * Created by sethm on 23/12/2016.
 */
public enum Server
{
    HCF("&3Hardcore Factions", "hcf", "&3HCFactions"),
    SG("&7Survival Games Lobby", "sglobby", "&7SG Lobby"),
    PRACTICE("&bPractice", "practice", "&bPractice"),
    KITMAP("&3Kit Map", "kitmap", "&3Kit Map");

    private String displayName;
    private String bungeeName;
    private String scoreboardName;

    Server(String displayName, String bungeeName, String scoreboardName)
    {
        this.displayName = displayName;
        this.bungeeName = bungeeName;
        this.scoreboardName = scoreboardName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getBungeeName()
    {
        return bungeeName;
    }

    public String getScoreboardName()
    {
        return scoreboardName;
    }
}