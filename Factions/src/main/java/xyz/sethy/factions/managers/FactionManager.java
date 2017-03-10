package xyz.sethy.factions.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by sethm on 26/11/2016.
 */
public class FactionManager implements Listener
{
    private LinkedList<Faction> factions;
    private Jedis jedis;

    public FactionManager()
    {
        this.factions = new LinkedList<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());

        this.jedis = new Jedis("localhost");
        this.jedis.connect();


        this.loadFactions();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                int i = 0;
                long start = System.currentTimeMillis();
                if (!factions.isEmpty())
                {
                    for (Faction faction : factions)
                    {
                        if (faction.needsSave())
                        {
                            jedis.set("faction." + faction.getName().toLowerCase(), faction.saveString(true));
                            System.out.println("Saved faction: " + faction.getName());
                            i++;
                        }
                    }
                }
                long finish = System.currentTimeMillis();
                long time = finish - start;
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                    if (user.getGroup().getPermission() <= Group.TRAIL_MOD.getPermission())
                    {
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Factions] Saved " + i + " Factions to Redis in " + time + "ms.");
                    }
                }
            }
        }, 20L, 30 * 20L);
    }

    private void loadFactions()
    {
        for (final String key : jedis.keys("faction.*"))
        {
            final String loadString = jedis.get(key);
            final Faction faction = new Faction();
            faction.loadFromString(loadString);
            factions.add(faction);
            System.out.println("Loaded faction: " + faction.getName());
        }
    }

    public void saveFactions()
    {
        for (final Faction faction : factions)
        {
            jedis.set("faction." + faction.getName().toLowerCase(), faction.saveString(true));
            System.out.println("Saved faction: " + faction.getName());
        }
    }

    public LinkedList<Faction> getFactions()
    {
        return factions;
    }

    public Faction findByPlayer(Player player)
    {
        if (player == null || player.getUniqueId() == null)
        {
            return null;
        }
        for (final Faction faction : factions)
        {
            if (faction != null)
            {
                if (faction.getAllMembers().contains(player.getUniqueId()))
                {
                    return faction;
                }
            }
        }
        return null;
    }

    public Faction findByAttribute(String string)
    {
        for (final Faction faction : factions)
        {
            if (faction.getName().equalsIgnoreCase(string))
            {
                return faction;
            }
            for (UUID uuid : faction.getOnlineMembers())
            {
                if (Bukkit.getPlayer(uuid).getName().equalsIgnoreCase(string))
                {
                    return faction;
                }
            }
        }
        return null;
    }

    public Faction findByName(String name)
    {
        for (final Faction faction : factions)
        {
            if (faction.getName().equalsIgnoreCase(name))
            {
                return faction;
            }
        }
        return null;
    }

    public void removeFaction(Faction faction)
    {
        this.factions.remove(faction);
        jedis.del("faction." + faction.getName().toLowerCase());
        faction.setLeader(null);
        faction.setDtr(null);
        faction.getMembers().clear();
        faction.getOnlineMembers().clear();
        faction.getCaptains().clear();
        faction.getClaims().clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Faction faction = this.findByPlayer(event.getPlayer());
        if (faction != null)
        {
            faction.getOnlineMembers().remove(event.getPlayer().getUniqueId());
            for (UUID uuid : faction.getOnlineMembers())
            {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member offline: &c" + event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Faction faction = this.findByPlayer(event.getPlayer());
        if (faction != null)
        {
            faction.getOnlineMembers().add(event.getPlayer().getUniqueId());
            for (UUID uuid : faction.getOnlineMembers())
            {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member online: &a" + event.getPlayer().getName()));
            }
            faction.getInformation(event.getPlayer());
        }
        if (Factions.getInstance().isKitmap())
        {
            if (!event.getPlayer().hasPlayedBefore())
                event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0.5, 72, 0.5));
        }
    }

    public Jedis getJedis()
    {
        return jedis;
    }
}
