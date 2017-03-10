package xyz.sethy.api.framework.group;

public enum Group
{
    OWNER(7, "&7[&3Owner&7]&4", 11, "Owner"),
    CO_OWNER(7, "&7[&3Co Owner&7]&4", 10, "Co Owner"),
    DEVELOPER(7, "&7[&bDeveloper&7]&b", 10, "Developer"),
    PLATFORM_ADMIN(6, "&7[&cPlatform Admin&7]&c", 9, "Platform Admin"),
    ADMIN(6, "&7[&cAdmin&7]&c", 8, "Admin"),
    MOD(6, "&7[&5Moderator&7]&5", 6, "Moderator"),
    TRAIL_MOD(6, "&7[&eTrial Moderator&7]&e", 5, "Trail Moderator"),
    FAMOUS(5, "&5\u262F&2", 3, "Famous"),
    YOUTUBE(5, "&d", 3, "YouTube"),
    KROD(3, "&3", 3, "Kord"),
    ELAPH(2, "&7", 2, "Elaph"),
    POWER_FACTION(2, "&f", 0, "Power Faction"),
    ANT(1, "&6", 1, "Ant"),
    DEFAULT(0, "&f", 0, "Member");

    private int queuePriority;
    private String name;
    private int permission;
    private String scoreboard;

    Group(int queuePriority, String name, int permission, String scoreboard)
    {
        this.queuePriority = queuePriority;
        this.name = name;
        this.permission = permission;
        this.scoreboard = scoreboard;
    }

    public String getName()
    {
        return this.name;
    }

    public int getQueuePriority()
    {
        return this.queuePriority;
    }

    public int getPermission()
    {
        return this.permission;
    }

    public static Group getByName(String name)
    {
        for (Group group : values())
        {
            if (group.name().equalsIgnoreCase(name))
            {
                return group;
            }
        }
        return null;
    }

    public String getScoreboard()
    {
        return scoreboard;
    }
}
