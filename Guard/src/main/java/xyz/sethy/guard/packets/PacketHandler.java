package xyz.sethy.guard.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.packets.events.*;
import xyz.sethy.guard.utils.UtilServer;

/**
 * Created by sethm on 16/11/2016.
 */
public class PacketHandler
{
    private Plugin plugin;

    public PacketHandler(Plugin plugin)
    {
        this.plugin = plugin;

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.USE_ENTITY)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                if ((packet.getHandle() instanceof PacketPlayInUseEntity))
                {
                    PacketPlayInUseEntity packetNMS = (PacketPlayInUseEntity) packet.getHandle();
                    if (packetNMS == null)
                        return;
                }
                if (packet.getEntityUseActions().size() < 0)
                    return;

                EntityUseAction type = packet.getEntityUseActions().read(0);
                int entityId = packet.getIntegers().read(0);
                Entity entity = null;
                for (Entity entityentity : UtilServer.getEntities(player.getWorld()))
                {
                    if (entityentity.getEntityId() == entityId)
                    {
                        entity = entityentity;
                    }
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketUseEntityEvent(type, player, entity));
            }
        });
//        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.POSITION_LOOK)
//        {
//            public void onPacketReceiving(PacketEvent event)
//            {
//                Player player = event.getPlayer();
//                if (player == null)
//                {
//                    return;
//                }
//                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
//                        event.getPacket().getDoubles().read(0), event.getPacket().getDoubles().read(1),
//                        event.getPacket().getDoubles().read(2),
//                        event.getPacket().getFloat().read(0),
//                        event.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
//            }
//        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.LOOK)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                        player.getLocation().getX(), player.getLocation().getY(),
                        player.getLocation().getZ(), event.getPacket().getFloat().read(0),
                        event.getPacket().getFloat().read(1), PacketPlayerType.LOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.POSITION)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                        event.getPacket().getDoubles().read(0), event.getPacket().getDoubles().read(1),
                        event.getPacket().getDoubles().read(2), player.getLocation().getYaw(),
                        player.getLocation().getPitch(), PacketPlayerType.POSITION));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.ENTITY_ACTION)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketEntityActionEvent(player, packet.getIntegers().read(1)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.KEEP_ALIVE)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketKeepAliveEvent(player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.ARM_ANIMATION)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketSwingArmEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.HELD_ITEM_SLOT)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketHeldItemChangeEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.BLOCK_PLACE)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketBlockPlacementEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.FLYING)
        {
            public void onPacketReceiving(PacketEvent event)
            {
                Player player = event.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                        player.getLocation().getX(), player.getLocation().getY(),
                        player.getLocation().getZ(), player.getLocation().getYaw(),
                        player.getLocation().getPitch(), PacketPlayerType.FLYING));
            }
        });
    }
}
