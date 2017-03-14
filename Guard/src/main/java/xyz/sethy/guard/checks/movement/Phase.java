package xyz.sethy.guard.checks.movement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.guard.Guard;
import xyz.sethy.guard.checks.Check;

import java.util.*;

/**
 * Created by Seth on 12/03/2017.
 */
public class Phase extends Check implements Listener
{
    private AntiPhaseTask task;

    public Phase(Plugin plugin)
    {
        super("Phase", "Phase", plugin);
        setMaxViolations(30);

        this.task = new AntiPhaseTask(API.getPlugin());
        this.task.runTaskTimer(API.getPlugin(), 1L, 1L);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void corretEnderpearlTeleportation(PlayerTeleportEvent event)
    {
        if (!Guard.getInstance().getSettings().correctEnderpearlTeleportation()) {
            return;
        }
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }
        Location to = event.getTo();
        Block block = to.getBlock();
        double excess = to.getY() - (int)to.getY();
        if (Guard.getInstance().getSettings().getPassable().contains(block.getType())) {
            event.setTo(event.getTo().subtract(0.0D, excess, 0.0D));
        }
        if (!Guard.getInstance().getSettings().getPassable().contains(block.getRelative(BlockFace.UP).getType())) {
            event.setTo(to.subtract(0.0D, 1.0D, 0.0D));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void preventLongMoves(PlayerMoveEvent event)
    {
        if (event.getFrom().distance(event.getTo()) > 8.0D) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void setTeleported(PlayerTeleportEvent event)
    {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            this.task.addTeleportedPlayer(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void setTeleported(PlayerRespawnEvent event)
    {
        this.task.addTeleportedPlayer(event.getPlayer().getUniqueId());
    }

    private class AntiPhaseTask extends BukkitRunnable
    {
        private final Plugin plugin;
        private final Map<UUID, Location> validLocations = new HashMap<>();
        private Set<UUID> teleported = new HashSet<>();

        public void addTeleportedPlayer(UUID playerId)
        {
            this.teleported.add(playerId);
        }

        AntiPhaseTask(Plugin plugin)
        {
            this.plugin = plugin;
        }

        public void run()
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                UUID playerId = player.getUniqueId();
                Location loc1 = this.validLocations.containsKey(playerId) ? (Location)this.validLocations.get(playerId) : player.getLocation();
                Location loc2 = player.getLocation();
                if ((loc1.getWorld() == loc2.getWorld()) && (!this.teleported.contains(playerId)) && (loc1.distance(loc2) > 10.0D))
                {
                    player.teleport((Location)this.validLocations.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
                else if (isValidMove(playerId, loc1, loc2))
                {
                    this.validLocations.put(playerId, loc2);
                }
                else if ((player.hasPermission("antiphase.bypass")) || (this.validLocations.containsKey(playerId)))
                {
                    Guard.getInstance().logCheat(Phase.this, player, null);
                    player.teleport(this.validLocations.get(playerId), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                }
            }
        }

        public boolean isValidMove(UUID playerId, Location loc1, Location loc2)
        {
            if (loc1.getWorld() != loc2.getWorld()) {
                return true;
            }
            if (this.teleported.remove(playerId)) {
                return true;
            }
            int moveMaxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
            int moveMinX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int moveMaxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
            int moveMinY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int moveMaxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
            int moveMinZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            if (moveMaxY > 256) {
                moveMaxX = 256;
            }
            if (moveMinY > 256) {
                moveMinY = 256;
            }
            for (int x = moveMinX; x <= moveMaxX; x++) {
                for (int z = moveMinZ; z <= moveMaxZ; z++) {
                    for (int y = moveMinY; y <= moveMaxY; y++)
                    {
                        Block block = loc1.getWorld().getBlockAt(x, y, z);
                        if ((y != moveMinY) || (loc1.getBlockY() == loc2.getBlockY())) {
                            if (hasPhased(block, loc1, loc2)) {
                                Guard.getInstance().logCheat(Phase.this, Bukkit.getPlayer(playerId), null);
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }

        private boolean hasPhased(Block block, Location loc1, Location loc2)
        {
            if (Guard.getInstance().getSettings().getPassable().contains(block.getType())) {
                return false;
            }
            double moveMaxX = Math.max(loc1.getX(), loc2.getX());
            double moveMinX = Math.min(loc1.getX(), loc2.getX());
            double moveMaxY = Math.max(loc1.getY(), loc2.getY()) + 1.8D;
            double moveMinY = Math.min(loc1.getY(), loc2.getY());
            double moveMaxZ = Math.max(loc1.getZ(), loc2.getZ());
            double moveMinZ = Math.min(loc1.getZ(), loc2.getZ());

            double blockMaxX = block.getLocation().getBlockX() + 1;
            double blockMinX = block.getLocation().getBlockX();
            double blockMaxY = block.getLocation().getBlockY() + 2;
            double blockMinY = block.getLocation().getBlockY();
            double blockMaxZ = block.getLocation().getBlockZ() + 1;
            double blockMinZ = block.getLocation().getBlockZ();
            if (blockMinY > moveMinY) {
                blockMaxY -= 1.0D;
            }
            if (Guard.getInstance().getSettings().getDoors().contains(block.getType()))
            {
                Door door = (Door)block.getType().getNewData(block.getData());
                if (door.isTopHalf()) {
                    return false;
                }
                BlockFace facing = door.getFacing();
                if (door.isOpen())
                {
                    Block up = block.getRelative(BlockFace.UP);
                    boolean hinge;
                    if (Guard.getInstance().getSettings().getDoors().contains(up.getType())) {
                        hinge = (up.getData() & 0x1) == 1;
                    } else {
                        return false;
                    }
                    if (facing == BlockFace.NORTH) {
                        facing = hinge ? BlockFace.WEST : BlockFace.EAST;
                    } else if (facing == BlockFace.EAST) {
                        facing = hinge ? BlockFace.NORTH : BlockFace.SOUTH;
                    } else if (facing == BlockFace.SOUTH) {
                        facing = hinge ? BlockFace.EAST : BlockFace.WEST;
                    } else {
                        facing = hinge ? BlockFace.SOUTH : BlockFace.NORTH;
                    }
                }
                if (facing == BlockFace.WEST) {
                    blockMaxX -= 0.8D;
                }
                if (facing == BlockFace.EAST) {
                    blockMinX += 0.8D;
                }
                if (facing == BlockFace.NORTH) {
                    blockMaxZ -= 0.8D;
                }
                if (facing == BlockFace.SOUTH) {
                    blockMinZ += 0.8D;
                }
            }
            else if (Guard.getInstance().getSettings().getGates().contains(block.getType()))
            {
                if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                    return false;
                }
                BlockFace face = ((Directional)block.getType().getNewData(block.getData())).getFacing();
                if ((face == BlockFace.NORTH) || (face == BlockFace.SOUTH))
                {
                    blockMaxX -= 0.2D;
                    blockMinX += 0.2D;
                }
                else
                {
                    blockMaxZ -= 0.2D;
                    blockMinZ += 0.2D;
                }
            }
            else if (Guard.getInstance().getSettings().getTrapdoors().contains(block.getType()))
            {
                TrapDoor door = (TrapDoor)block.getType().getNewData(block.getData());
                if (door.isOpen()) {
                    return false;
                }
                if (door.isInverted()) {
                    blockMinY += 0.85D;
                } else {
                    blockMaxY -= (blockMinY > moveMinY ? 0.85D : 1.85D);
                }
            }
            else if (Guard.getInstance().getSettings().getFences().contains(block.getType()))
            {
                blockMaxX -= 0.2D;
                blockMinX += 0.2D;
                blockMaxZ -= 0.2D;
                blockMinZ += 0.2D;
                if (((moveMaxX > blockMaxX) && (moveMinX > blockMaxX) && (moveMaxZ > blockMaxZ) && (moveMinZ > blockMaxZ)) || ((moveMaxX < blockMinX) && (moveMinX < blockMinX) && (moveMaxZ > blockMaxZ) && (moveMinZ > blockMaxZ)) || ((moveMaxX > blockMaxX) && (moveMinX > blockMaxX) && (moveMaxZ < blockMinZ) && (moveMinZ < blockMinZ)) || ((moveMaxX < blockMinX) && (moveMinX < blockMinX) && (moveMaxZ < blockMinZ) && (moveMinZ < blockMinZ))) {
                    return false;
                }
                if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                    blockMaxX += 0.2D;
                }
                if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                    blockMinX -= 0.2D;
                }
                if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                    blockMaxZ += 0.2D;
                }
                if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                    blockMinZ -= 0.2D;
                }
            }
            boolean x = loc1.getX() < loc2.getX();
            boolean y = loc1.getY() < loc2.getY();
            boolean z = loc1.getZ() < loc2.getZ();

            return ((moveMinX != moveMaxX) && (moveMinY <= blockMaxY) && (moveMaxY >= blockMinY) && (moveMinZ <= blockMaxZ) && (moveMaxZ >= blockMinZ) && (((x) && (moveMinX <= blockMinX) && (moveMaxX >= blockMinX)) || ((!x) && (moveMinX <= blockMaxX) && (moveMaxX >= blockMaxX)))) || ((moveMinY != moveMaxY) && (moveMinX <= blockMaxX) && (moveMaxX >= blockMinX) && (moveMinZ <= blockMaxZ) && (moveMaxZ >= blockMinZ) && (((y) && (moveMinY <= blockMinY) && (moveMaxY >= blockMinY)) || ((!y) && (moveMinY <= blockMaxY) && (moveMaxY >= blockMaxY)))) || ((moveMinZ != moveMaxZ) && (moveMinX <= blockMaxX) && (moveMaxX >= blockMinX) && (moveMinY <= blockMaxY) && (moveMaxY >= blockMinY) && (((z) && (moveMinZ <= blockMinZ) && (moveMaxZ >= blockMinZ)) || ((!z) && (moveMinZ <= blockMaxZ) && (moveMaxZ >= blockMaxZ))));
        }
    }
}
