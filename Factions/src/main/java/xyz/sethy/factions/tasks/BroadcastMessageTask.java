package xyz.sethy.factions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.factions.Factions;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Seth on 15/01/2017.
 */
public class BroadcastMessageTask
{
    private final ConcurrentLinkedQueue<String> broadcasts;

    public BroadcastMessageTask()
    {
        this.broadcasts = new ConcurrentLinkedQueue<>();
        this.broadcasts.add("&7(&3&lKrodHQ&7)&7 There is currently a &350%&7 off SOTW sale.");
        this.broadcasts.add("&7(&3&lKrodHQ&7)&7 To obtain perks and ranks check our store; store.KrodHQ.com");
//        this.broadcasts.add("&7(&3&lKrodHQ&7)&7 Subscribe to our Reddit; &bhttps://reddit.com/r/KrodHQ&7 to stay up-to-date.");
        this.broadcasts.add("&7(&3&lKrodHQ&7)&7 Follow our Twitter; &bhttps://twitter.com/KrodHQ&7 to stay up-to-date.");

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Random random = new Random();
                int i = random.nextInt(broadcasts.size());
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', (String) broadcasts.toArray()[i]));
            }
        }, 0L, 200 * 20L);
    }
}
