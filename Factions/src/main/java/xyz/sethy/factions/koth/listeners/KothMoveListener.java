package xyz.sethy.factions.koth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.handlers.KothHandler;
import xyz.sethy.factions.koth.dto.Koth;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 26/12/2016.
 */
public class KothMoveListener implements Runnable
{
    private final Map<Player, Location> oldLocation;
    private LinkedList<Player> inKoth;
    private KothHandler kothHandler;

    public KothMoveListener()
    {
        this.oldLocation = new ConcurrentHashMap<>();
        this.inKoth = new LinkedList<>();
        this.kothHandler = Factions.getInstance().getKothHandler();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), this, 4L, 4L);
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            final Location location = player.getLocation();

            if (this.inKoth.contains(player))
            {
                if (!kothHandler.isInCapZone(location))
                    this.inKoth.remove(player);
            }

            if (kothHandler.isInCapZone(location))
            {
                Koth koth = kothHandler.getKothAtLocation(location);
                if (koth != null)
                {
                    if (kothHandler.getActiveKoths().contains(koth))
                    {
                        if (koth.getCapper().get() == null)
                        {
                            koth.getCapper().getAndSet(player.getName());
                            this.inKoth.add(player);
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&3&lKoth&8] &aSomeone &9is now controlling &3" + koth.getName().get() + " &9."));
                        }
                    }
                }
            }
            oldLocation.put(player, location);
        }
    }
}
