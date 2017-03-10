package xyz.sethy.factions.listeners;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.InventoryHolder;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.TimerType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 07/12/2016.
 */
public class EnderpearlListener implements Listener
{
    private final ImmutableSet blockedPearlTypes;

    public EnderpearlListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
        this.blockedPearlTypes = Sets.immutableEnumSet(Material.THIN_GLASS, Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS);
    }

    private ConcurrentHashMap<Player, Long> timers = new ConcurrentHashMap<>();

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))
        {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
            {
                if (this.timers.containsKey(player) && this.timers.get(player) > System.currentTimeMillis())
                {
                    long millisLeft = this.timers.get(player) - System.currentTimeMillis();
                    double value = millisLeft / 1000.0D;
                    double sec = Math.round(10.0D * value) / 10.0D;
                    event.setCancelled(true);
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Pearl cooldown: &c" + sec + " seconds&7."));
                    return;
                }
                Factions.getInstance().getTimerHandler().addTimer(player, new DefaultTimer(TimerType.ENDERPEARL, 16000 + System.currentTimeMillis(), player));
                this.timers.put(player, 16000 + System.currentTimeMillis());
            }
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract1(PlayerInteractEvent event)
    {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.hasItem()) && (event.getItem().getType() == Material.ENDER_PEARL))
        {
            Block block = event.getClickedBlock();
            if ((block.getType().isSolid()) && (!(block.getState() instanceof InventoryHolder)))
            {
                event.setCancelled(true);
                Player player = event.getPlayer();
                player.setItemInHand(event.getItem());
            }
        }
    }

    @EventHandler
    public void onMove(PlayerInteractEvent e)
    {
        if ((e.getPlayer().getLocation().getBlock() != null) && (e.getPlayer().getLocation().getBlock().getType() == Material.TRAP_DOOR))
        {
            e.getPlayer().teleport(e.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D));
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPearlClip(PlayerTeleportEvent event)
    {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
        {
            Location to = event.getTo();
            if (this.blockedPearlTypes.contains(to.getBlock().getType()))
            {
                Player player = event.getPlayer();
                event.setCancelled(true);
                return;
            }
            to.setX(to.getBlockX() + 0.5D);
            to.setZ(to.getBlockZ() + 0.5D);
            event.setTo(to);
        }
    }

    public boolean clippingThrough(final Location target, final Location from, final double thickness)
    {
        return (from.getX() > target.getX() && from.getX() - target.getX() < thickness) || (target.getX() > from.getX() && target.getX() - from.getX() < thickness) || (from.getZ() > target.getZ() && from.getZ() - target.getZ() < thickness) || (target.getZ() > from.getZ() && target.getZ() - from.getZ() < thickness);
    }
}
