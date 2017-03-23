package xyz.sethy.api.framework.group;

public enum Group
{
    OWNER(12, "&7[&4Owner&7]&4", 11, "Owner"),
    DEVELOPER(11, "&7[&bDeveloper&7]&b", 10, "Developer"),
    OPERRATOR(10, "&f", 9, "Operator"),
    STAFF_MANAGER(10, "&7[&4Manager&7]&4", 9, "Manager"),
    PLATFORM_ADMIN(9, "&7[&cPlatform Admin&7]&c", 9, "Platform Admin"),
    ADMIN(8, "&7[&cAdmin&7]&c", 8, "Admin"),
    SR_MOD(7, "&7[&5Senior Mod&7]&5", 8, "Senior Mod"),
    MOD_PLUS(7, "&7[&5Mod+&7]&5", 8, "Mod+"),
    MOD(6, "&7[&5Moderator&7]&5", 6, "Moderator"),
    TRAIL_MOD(6, "&7[&eTrial Moderator&7]&e", 5, "Trial Moderator"),
    PARTNER(6, "&7[&4Partnet&7]&4", 3, "Partner"),
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
