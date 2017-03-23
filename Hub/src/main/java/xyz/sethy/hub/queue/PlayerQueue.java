package xyz.sethy.hub.queue;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.hub.Hub;
import xyz.sethy.hub.server.Server;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 22/12/2016.
 */
public class PlayerQueue
{
    private final ConcurrentHashMap<Player, Long> waitingForLives;
    private final ConcurrentHashMap<Player, Long> waitingSince;
    private final ConcurrentHashMap<Server, PriorityQueue<Player>> queues;

    public PlayerQueue()
    {
        this.waitingForLives = new ConcurrentHashMap<>();
        this.waitingSince = new ConcurrentHashMap<>();
        this.queues = new ConcurrentHashMap<>();


        for (Server server : Server.values())
        {
            PriorityQueue<Player> queue = new PriorityQueue<>(new PrioirtyCompator());
            this.queues.put(server, queue);
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hub.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {

                for (PriorityQueue<Player> queue : queues.values())
                {
                    if (!queue.isEmpty())
                    {
                        Server server = getServerFromQueue(queue);
                        Player player = queue.peek();
                        if (player == null || !player.isOnline() || server == null)
                        {
                            queue.remove(player);
                            continue;
                        }
                        sendToServer(player, server);
                        for (Player player1 : queue)
                        {
                            player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are currently waiting for to join &r" + server.getDisplayName() + ". \n&7You are in position &3#" + getPos(player1) + "/" + getQueueSize(server) + "&7."));
                            if (!player1.isOnline())
                            {
                                queue.remove(player);
                                continue;
                            }
                            User user = API.getUserManager().findByUniqueId(player1.getUniqueId());
                            if (user != null)
                            {
                                if (user.getGroup().equals(Group.DEFAULT))
                                {
                                    player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You can purchase &3Krod&7 to obtain highest queue priority. &3http://store.KrodHQ.com/"));
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 2 * 20L);
    }


    private void sendToServer(Player player, Server server)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server.getBungeeName());
        player.sendPluginMessage(Hub.getInstance().getPlugin(), "BungeeCord", out.toByteArray());
    }


    private Server getServerFromQueue(PriorityQueue queue)
    {
        for (Server server : queues.keySet())
        {
            if (queue.equals(queues.get(server)))
            {
                return server;
            }
        }
        return null;
    }

    public boolean isQueueing(Player player)
    {
        for (PriorityQueue queue : queues.values())
        {
            return queue.contains(player);
        }
        return false;
    }

    public Server whatServer(Player player)
    {
        for (PriorityQueue queue : queues.values())
        {
            if (queue.contains(player))
            {
                for (Server server : queues.keySet())
                {
                    if (queues.get(server).equals(queue))
                    {
                        return server;
                    }
                }
            }
        }
        return null;
    }

    public int getPos(Player player)
    {
        int i = 1;
        PriorityQueue<Player> queue = queues.get(whatServer(player));
        for (Player player1 : queue)
        {
            if (player1.equals(player))
            {
                break;
            }
            i++;
        }
        return i;
    }

    public void addToQueue(Server server, Player player)
    {
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());

        if(server.equals(Server.HCF))
        {
            HCFUser hcfUser = API.getUserManager().getTempHCFUser(player.getUniqueId());
            if(this.waitingForLives.containsKey(player) && this.waitingForLives.get(player) > System.currentTimeMillis())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Thank you for using a life; You are not longer death-banned."));
                hcfUser.setDeathbanTime(0L);
                hcfUser.setDeathbanMessage("");
                hcfUser.setLives(hcfUser.getLives() - 1);
                hcfUser.setForceUndeathbanned(true);
                addToQueue(Server.HCF, player);
                return;
            }
        }

        if (user.getGroup().getQueuePriority() > 5)
        {
            sendToServer(player, server);
            return;
        }
        PriorityQueue<Player> queue = this.queues.get(server);
        queue.add(player);
        this.waitingSince.put(player, System.currentTimeMillis());
    }

    private final DecimalFormat FORMAT = new DecimalFormat("0.0");

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private String formatTime(long time)
    {
        if (time > 60000L)
        {
            return setLongFormat(time);
        }
        else
        {
            return format(time);
        }
    }

    private String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
        {
            return FORMAT.format(paramMilliseconds);
        }
        return DurationFormatUtils.formatDuration(paramMilliseconds, (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }

    public void removeFromQueue(Player player)
    {
        PriorityQueue<Player> queue = queues.get(whatServer(player));
        queue.remove(player);
    }

    public int getQueueSize(Server server)
    {
        return queues.get(server).size();
    }
}

class PrioirtyCompator implements Comparator<Player>
{
    @Override
    public int compare(Player o1, Player o2)
    {
        if (o1 == null)
            return -1;

        if (o2 == null)
            return -1;

        User u1 = API.getUserManager().getTempUser(o1.getUniqueId());
        User u2 = API.getUserManager().getTempUser(o2.getUniqueId());

        if (u1.getGroup().getQueuePriority() > u2.getGroup().getQueuePriority())
            return -1;
        if (u1.getGroup().getQueuePriority() < u2.getGroup().getQueuePriority())
            return 1;
        if (u1.getGroup().getQueuePriority() == u2.getGroup().getQueuePriority())
            return 0;
        return -1;
    }
}