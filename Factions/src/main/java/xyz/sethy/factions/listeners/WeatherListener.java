package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;

/**
 * Created by Seth on 12/03/2017.
 */
public class WeatherListener implements Listener
{
    public WeatherListener()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                World world = Bukkit.getWorld("world");
                if(world.hasStorm())
                {
                    world.setStorm(false);
                }
            }
        }.runTaskTimer(API.getPlugin(), 100 * 20L, 100 * 20L);
    }
}
