package xyz.sethy.guard.checks.other;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.packets.events.PacketPlayerEvent;
import xyz.sethy.guard.utils.UtilTime;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 24/11/2016.
 */
public class MorePackets extends Check
{
    private Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    private Map<UUID, Long> lastPacket;
    private LinkedList<UUID> blacklist;

    public MorePackets(Plugin plugin)
    {
        super("Too many packets / Fake Lag", "Fake Lag", plugin);

        this.packetTicks = new ConcurrentHashMap<>();
        this.lastPacket = new ConcurrentHashMap<>();
        this.blacklist = new LinkedList<>();

        setBannable(true);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event)
    {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent event)
    {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent event)
    {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent event)
    {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE))
            return;

        if (Guard.getInstance().getLagUtils().getTPS() > 21.0D)
            return;

        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.packetTicks.containsKey(player.getUniqueId()))
        {
            Count = (Integer) ((Map.Entry) this.packetTicks.get(player.getUniqueId())).getKey();
            Time = (Long) ((Map.Entry) this.packetTicks.get(player.getUniqueId())).getValue();
        }
        if (this.lastPacket.containsKey(player.getUniqueId()))
        {
            long MS = System.currentTimeMillis() - (Long) this.lastPacket.get(player.getUniqueId());
            if (MS >= 100L)
                this.blacklist.add(player.getUniqueId());
            else if ((MS > 1L) && (this.blacklist.contains(player.getUniqueId())))
                this.blacklist.remove(player.getUniqueId());
        }
        if (!this.blacklist.contains(player.getUniqueId()))
        {
            Count++;
            if ((this.packetTicks.containsKey(player.getUniqueId())) && (UtilTime.elapsed(Time, 1000L)))
            {
                int maxPackets = 49;
//                if (Count > maxPackets)
//                    Guard.getInstance().logCheat(this, player, null);
                Count = 0;
                Time = UtilTime.nowlong();
            }
        }
        this.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
