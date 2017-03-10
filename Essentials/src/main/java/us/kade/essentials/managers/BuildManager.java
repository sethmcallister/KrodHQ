package us.kade.essentials.managers;

import org.bukkit.event.Listener;
import xyz.sethy.api.framework.user.User;

import java.util.LinkedHashSet;

/**
 * Created by sethm on 28/10/2016.
 */
public class BuildManager implements Listener
{
    private static BuildManager instance;
    private final LinkedHashSet<User> building;

    public BuildManager()
    {
        instance = this;
        this.building = new LinkedHashSet<>();
    }

    public static BuildManager getInstance()
    {
        return instance;
    }

    public LinkedHashSet<User> getBuilding()
    {
        return building;
    }
}
