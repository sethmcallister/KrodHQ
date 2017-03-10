package xyz.sethy.sglobby.queue;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.sglobby.SGLobby;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 22/12/2016.
 */
public class PlayerQueue
{
    private final PriorityQueue<Player> queue;
    private final ConcurrentHashMap<Player, Long> waitingSince;
    String nextFreeServer = "sg1";
    int k = 0;

    public PlayerQueue()
    {
        this.queue = new PriorityQueue<>(new PrioirtyCompator());
        this.waitingSince = new ConcurrentHashMap<>();

        k = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SGLobby.getInstance().getPlugin(), new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!queue.isEmpty())
                {
                    Player player = queue.peek();
                    if (player == null || !player.isOnline())
                    {
                        removeFromQueue(player);
                        return;
                    }
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(nextFreeServer);
                    player.sendPluginMessage(SGLobby.getInstance().getPlugin(), "BungeeCord", out.toByteArray());
                    nextFreeServer = workoutNextServer();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You are being connected to &7" + nextFreeServer + "&5."));
                    for (Player player1 : queue)
                    {
                        player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You are current waiting for a &7Survival Game&5 match. \n&5You are in position &d#" + getPos(player1) + "/#" + getQueueSize() + "&5."));
                        if (!player1.isOnline())
                        {
                            removeFromQueue(player);
                            continue;
                        }
                        User user = API.getUserManager().findByUniqueId(player1.getUniqueId());
                        if (user != null)
                        {
                            if (user.getGroup().equals(Group.DEFAULT))
                            {
                                player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You can purchase &3platinum&5 to obtain highest queue priority. &dhttp://store.kihar.net/"));
                            }
                        }
                    }
                }
            }
        }, 0L, 2 * 20L);
    }

    public boolean isQueueing(Player player)
    {
        return queue.contains(player);
    }

    public int getPos(Player player)
    {
        int i = 1;
        for (Player p : queue)
        {
            if (p.equals(player))
            {
                return i;
            }
            i++;
        }
        return 0;
    }

    public void addToQueue(Player player)
    {
        this.queue.add(player);
        this.waitingSince.put(player, System.currentTimeMillis());
    }

    public void removeFromQueue(Player player)
    {
        this.queue.remove(player);
    }

    public int getQueueSize()
    {
        return queue.size();
    }

    public long getWaitingSince(Player player)
    {
        return this.waitingSince.get(player);
    }


    public String workoutNextServer()
    {
        String server = "sg";
        int i = 1;
        for (Integer port : SGLobby.getInstance().getSgServer().values())
        {
            try
            {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", port), 1000);

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.write(0xFE);

                StringBuilder str = new StringBuilder();

                int b;
                while ((b = in.read()) != -1)
                {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24)
                    {
                        str.append((char) b);
                    }
                }

                String[] data = str.toString().split("§");
                int online = Integer.parseInt(data[1]);
                if (online < 20)
                {
                    return "sg" + i;
                }
                i++;
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("[Info] HCF Server is not online.");
            }
        }
        return server + i;
    }
}

class PrioirtyCompator implements Comparator<Player>
{

    @Override
    public int compare(Player o1, Player o2)
    {
        User u1 = API.getUserManager().findByUniqueId(o1.getUniqueId());
        User u2 = API.getUserManager().findByUniqueId(o2.getUniqueId());
        if (u1.getGroup().getQueuePriority() < u2.getGroup().getQueuePriority())
            return 1;
        if (u1.getGroup().getQueuePriority() == u2.getGroup().getQueuePriority())
            return 0;

        return -1;
    }
}

