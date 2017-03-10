package xyz.sethy.factions.koth.dto;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sethm on 26/12/2016.
 */
public class CoreKoth implements Koth
{
    private final AtomicLong timeRemaining;
    private final AtomicReference<String> capper;
    private final AtomicReference<String> name;
    private final AtomicLong startTime;
    private final LinkedList<Location> kothLocations;

    public CoreKoth()
    {
        this.timeRemaining = new AtomicLong(0L);
        this.capper = new AtomicReference<>();
        this.name = new AtomicReference<>();
        this.startTime = new AtomicLong(0L);
        this.kothLocations = new LinkedList<>();
    }


    @Override
    public AtomicLong getTimeRemaining()
    {
        return timeRemaining;
    }

    @Override
    public AtomicReference<String> getCapper()
    {
        return capper;
    }

    @Override
    public AtomicReference<String> getName()
    {
        return name;
    }

    @Override
    public AtomicLong getStartTime()
    {
        return startTime;
    }

    @Override
    public LinkedList<Location> getKoTHLocations()
    {
        return kothLocations;
    }

    @Override
    public void captured()
    {
        Factions.getInstance().getKothHandler().getActiveKoths().remove(this);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(" ");
        Player capped = Bukkit.getPlayer(capper.get());
        for (Player player : Bukkit.getOnlinePlayers())
        {
            StringBuilder builder = new StringBuilder();
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(capped);
            if (faction != null)
            {
                builder.append("&7[")
                        .append(faction.getName(player))
                        .append("&7]");
            }
            builder.append("&3")
                    .append(capped.getName());

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The KoTH&3 " + this.getName().get() + "&7 was successfully captured by &3" + builder.toString() + "&7."));
        }
    }
}
