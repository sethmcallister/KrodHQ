package xyz.sethy.hub.queue;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

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
                            System.out.println("Players in queue = " + queue.toArray().toString());
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
            HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(player.getUniqueId());
            if(this.waitingForLives.containsKey(player) && this.waitingForLives.get(player) > System.currentTimeMillis())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Thank you for using a life; You are not longer death-banned."));
                hcfUser.setDeathbanTime(0L);
                hcfUser.setDeathbanMessage("");
                hcfUser.setLives(hcfUser.getLives() - 1);
                addToQueue(Server.HCF, player);
                return;
            }
            if(hcfUser.deathbanTime() > System.currentTimeMillis())
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are currently death-banned, You cannot join HCF for another &3" + getConvertedTime(hcfUser.deathbanTime() - System.currentTimeMillis()) + "&7."));
                if(hcfUser.getLives() > 0)
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have &3" + hcfUser.getLives() + "&7 rejoin the queue within 20 seconds to use a life!"));
                    this.waitingForLives.put(player, System.currentTimeMillis() + 20000);
                }
                else
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You appear to have not lives, You can purchase some @&3 store.KrodHQ.com&7 to bypass this deathban."));
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

    private String getConvertedTime(long i)
    {
        i = Math.abs(i);
        final int hours = (int) Math.floor(i / 3600L);
        final int remainder = (int) (i % 3600L);
        final int minutes = remainder / 60;
        final int seconds = remainder % 60;
        if (seconds == 0 && minutes == 0)
        {
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + "0 seconds";
        }
        if (minutes == 0)
        {
            if (seconds == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
        }
        else if (seconds == 0)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
        }
        else if (seconds == 1)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
        }
        else
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            final String toReturn = String.format("%sm %ss", minutes, seconds);
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + " " + toReturn;
        }
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