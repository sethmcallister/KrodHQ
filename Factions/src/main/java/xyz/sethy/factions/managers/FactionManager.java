package xyz.sethy.factions.managers;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.Claim;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sethm on 26/11/2016.
 */
public class FactionManager implements Listener
{
    private LinkedList<Faction> factions;
    private final RedisClient redisClient = new RedisClient("localhost");
    private final String factionKey = Factions.getInstance().isKitmap() ? "network.kitmap." : "network.factions.";
    private final String claimKey = Factions.getInstance().isKitmap() ? "network.claims.kitmap." : "network.claims.factions.";
    private final Gson gson = new Gson();

    public FactionManager()
    {
        this.factions = new LinkedList<>();
        Bukkit.getServer().getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
        this.loadFactions();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Factions.getInstance().getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                RedisAsyncConnection<String, String> connection = redisClient.connectAsync();
                int i = 0;
                long start = System.currentTimeMillis();
                if (!factions.isEmpty())
                {
                    for (Faction faction : factions)
                    {
                        if (faction.needsSave())
                        {
                            String factionJson = gson.toJson(faction);
                            String claimJson = gson.toJson(faction.getClaims());

                            Future future = connection.set(factionKey + faction.getName().toLowerCase(), factionJson);
                            Future claimFuture = connection.set(claimKey + faction.getName().toLowerCase(), claimJson);

                            connection.awaitAll(future, claimFuture);
                            System.out.println(faction.getName() + "---->>>" + factionJson);
                            System.out.println("faction claim ---->>>" + claimJson);
                            faction.setNeedsSave(false);
                            i++;
                        }
                    }
                }
                long finish = System.currentTimeMillis();
                long time = finish - start;
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                    if (user.getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
                    {
                        player.sendMessage(ChatColor.DARK_PURPLE + "[Factions] Saved " + i + " Factions to Redis in " + time + "ms.");
                    }
                }
                connection.close();
            }
        }, 20L, 30 * 20L);
    }

    private void loadFactions()
    {

        RedisAsyncConnection<String, String> connection = redisClient.connectAsync();
        Future<List<String>> allFactions = connection.keys(factionKey + "*");
        connection.awaitAll(allFactions);

        try
        {
            for (final String key : allFactions.get())
            {
                Future<String> factionJson = connection.get(key);
                connection.awaitAll(factionJson);

                final Faction faction = gson.fromJson(factionJson.get(), Faction.class);
                if(faction != null)
                {
                    faction.getOnlineMembers().clear();
                    factions.add(faction);
                    System.out.println(faction.getName() + " loaded ---->>>" + factionJson.get());

                    Future<String> claimJson = connection.get(claimKey + faction.getName().toLowerCase());
                    connection.awaitAll(claimJson);
                    final Claim claim = gson.fromJson(claimJson.get(), Claim.class);
                    faction.setClaim(claim);
                    Factions.getInstance().getLandBoard().setFactionAt(claim, faction);
                    System.out.println(faction.getName() + " loaded claim ---->>>" + claimJson.get());
                }
            }
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        connection.close();
    }

    public void saveFactions()
    {
        RedisAsyncConnection<String, String> connection = redisClient.connectAsync();
        for (Faction faction : factions)
        {
            String factionJson = gson.toJson(faction);
            String claimJson = gson.toJson(faction.getClaims());

            Future future = connection.set(factionKey + faction.getName().toLowerCase(), factionJson);
            Future claimFuture = connection.set(claimKey + faction.getName().toLowerCase(), claimJson);

            connection.awaitAll(future, claimFuture);
            System.out.println(faction.getName() + "---->>>" + factionJson);
            System.out.println("faction claim ---->>>" + claimJson);
            System.out.println("Saved faction: " + faction.getName());
            faction.setNeedsSave(false);
        }
        connection.close();
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
        RedisAsyncConnection<String, String> connection = redisClient.connectAsync();
        connection.awaitAll(connection.set(factionKey + faction.getName().toLowerCase(), "null"));
        connection.awaitAll(connection.set(claimKey + faction.getName().toLowerCase(), "null"));
        connection.close();

        this.factions.remove(faction);
        faction.setLeader(null);
        faction.setDtr(null);
        faction.getMembers().clear();
        faction.getOnlineMembers().clear();
        faction.getCaptains().clear();
        faction.setClaim(null);
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
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMember Offline: &f" + event.getPlayer().getName()));
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
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMember Online: &f" + event.getPlayer().getName()));
            }
            faction.getInformation(event.getPlayer());
        }
    }
}
