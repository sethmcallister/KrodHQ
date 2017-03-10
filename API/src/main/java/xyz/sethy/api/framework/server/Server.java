package xyz.sethy.api.framework.server;

/**
 * Created by sethm on 31/12/2016.
 */
public enum Server
{
    HCF("&6Hardcore Factions", "hcf", "&6HCFactions", 0),
    SG("&eSurvival Games Lobby", "sglobby", "&eSG Lobby", 0),
    PRACTICE("&bPractice", "practice", "&bPractice", 0);

    private String displayName;
    private String bungeeName;
    private String scoreboardName;
    private Integer queueSize;

    Server(String displayName, String bungeeName, String scoreboardName, Integer queueSize)
    {
        this.displayName = displayName;
        this.bungeeName = bungeeName;
        this.scoreboardName = scoreboardName;
        this.queueSize = queueSize;
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

    public Integer getQueueSize()
    {
        return queueSize;
    }
}
