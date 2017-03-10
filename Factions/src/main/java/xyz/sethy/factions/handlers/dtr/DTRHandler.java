package xyz.sethy.factions.handlers.dtr;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

import java.util.*;
import java.util.logging.Level;

/**
 * Created by sethm on 01/12/2016.
 */
public class DTRHandler
{
    private final double[] BASE_DTR_INCREMENT;
    private final double[] MAX_DTR;
    private Set<String> wasOnCooldown;

    public DTRHandler()
    {
        BASE_DTR_INCREMENT = new double[]{1.5, 0.5, 0.45, 0.4, 0.36, 0.33, 0.3, 0.27, 0.24, 0.22, 0.21, 0.2, 0.19, 0.18, 0.175, 0.17, 0.168, 0.166, 0.164, 0.162, 0.16, 0.158, 0.156, 0.154, 0.152, 0.15, 0.148, 0.146, 0.144, 0.142, 0.142, 0.142, 0.142, 0.142, 0.142};
        MAX_DTR = new double[]{1.01, 1.8, 2.2, 2.7, 3.2, 3.4, 3.6, 3.8, 3.9, 4.18, 4.23, 4.36, 4.42, 4.59, 4.67, 4.72, 4.89, 4.92, 5.04, 5.15, 5.29, 5.37, 5.48, 5.52, 5.6, 5.73, 5.81, 5.96, 6.08, 6.16, 6.16, 6.16, 6.16, 6.16, 6.16};
        wasOnCooldown = new HashSet<>();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                final Map<Faction, Integer> playerOnlineMap = new HashMap<>();
                for (final Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
                    if (faction != null)
                    {
                        if (playerOnlineMap.containsKey(faction))
                            playerOnlineMap.put(faction, playerOnlineMap.get(faction) + 1);
                        else
                            playerOnlineMap.put(faction, 1);
                    }
                }
                for (final Map.Entry<Faction, Integer> factionEntry : playerOnlineMap.entrySet())
                {
                    if (factionEntry.getKey().getLeader() != null)
                    {
                        try
                        {
                            if (isOnCooldown(factionEntry.getKey()))
                                wasOnCooldown.add(factionEntry.getKey().getName().toLowerCase());
                            else
                            {
                                if (wasOnCooldown.contains(factionEntry.getKey().getName().toLowerCase()))
                                {
                                    wasOnCooldown.remove(factionEntry.getKey().getName().toLowerCase());
                                    for (final UUID uuid : factionEntry.getKey().getOnlineMembers())
                                    {
                                        Player player = Bukkit.getPlayer(uuid);
                                        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Your faction is now regenerating DTR!");
                                    }
                                }
                                factionEntry.getKey().setDtr(Math.min(factionEntry.getKey().getDTR() + factionEntry.getKey().getDTRIncrement(factionEntry.getValue()).doubleValue(), factionEntry.getKey().getMaxDTR()));
                            }
                        }
                        catch (Exception e)
                        {
                            Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
                        }
                    }
                }
            }
        }, 1L, 20L);
    }


    public double getBaseDTRIncrement(final int teamsize)
    {
        return (teamsize == 0) ? 0.0 : (BASE_DTR_INCREMENT[teamsize - 1] * 3.0);
    }

    public double getMaxDTR(final int teamsize)
    {
        return (teamsize == 0) ? 100.0 : MAX_DTR[teamsize - 1];
    }

    public boolean isOnCooldown(final Faction faction)
    {
        return faction.getDeathCooldown() > System.currentTimeMillis() || faction.getRaidableCooldown() > System.currentTimeMillis();
    }

    public boolean isRegenerating(final Faction faction)
    {
        return !isOnCooldown(faction) && faction.getDTR() != faction.getMaxDTR();
    }

    public void setCooldown(final Faction faction)
    {
        wasOnCooldown.add(faction.getName().toLowerCase());
    }
}
