package xyz.sethy.factions.managers;

import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Seth on 07/03/2017.
 */
public class DeathbanManager
{
    private ConcurrentHashMap<Group, Long> deathbanTimes;

    public DeathbanManager()
    {
        this.deathbanTimes = new ConcurrentHashMap<>();

        this.deathbanTimes.put(Group.DEFAULT, TimeUnit.MINUTES.toMillis(90L));
        this.deathbanTimes.put(Group.ANT, TimeUnit.MINUTES.toMillis(60L));
        this.deathbanTimes.put(Group.ELAPH, TimeUnit.MINUTES.toMillis(30L));
        this.deathbanTimes.put(Group.KROD, TimeUnit.MINUTES.toMillis(15L));
    }

    public long getDeathbanTime(User user)
    {
        return this.deathbanTimes.get(user.getGroup());
    }
}
