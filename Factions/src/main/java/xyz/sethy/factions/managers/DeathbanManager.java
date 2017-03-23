package xyz.sethy.factions.managers;

import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 07/03/2017.
 */
public class DeathbanManager
{
    private ConcurrentHashMap<Group, Long> deathbanTimes;

    public DeathbanManager()
    {
        this.deathbanTimes = new ConcurrentHashMap<>();

        this.deathbanTimes.put(Group.DEFAULT, 5400000L);
        this.deathbanTimes.put(Group.ANT, 3600000L);
        this.deathbanTimes.put(Group.ELAPH, 1800000L);
        this.deathbanTimes.put(Group.KROD, 900000L);
    }

    public long getDeathbanTime(Group group)
    {
        if(!this.deathbanTimes.containsKey(group))
            return 0L;

        return this.deathbanTimes.get(group) + System.currentTimeMillis();
    }

    public long getDeathbanTime(User user)
    {
        if(!this.deathbanTimes.containsKey(user.getGroup()))
            return 0L;

        return this.deathbanTimes.get(user.getGroup()) + System.currentTimeMillis();
    }
}
