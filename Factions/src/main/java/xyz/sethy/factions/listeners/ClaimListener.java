package xyz.sethy.factions.listeners;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.region.RegionData;
import xyz.sethy.factions.dto.region.RegionType;
import xyz.sethy.factions.handlers.ServerHandler;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.koth.dto.Koth;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerHandler;
import xyz.sethy.factions.timers.TimerType;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 02/12/2016.
 */
public class ClaimListener implements Listener
{
    private final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private final ServerHandler serverHandler;
    private final TimerHandler timerHandler;
    private final int BORDER;
    private final LinkedList<Material> blockedInteractList;

    public ClaimListener()
    {
        this.BORDER = Factions.getInstance().isKitmap() ? 500 : 2000;
        this.serverHandler = Factions.getInstance().getServerHandler();
        this.timerHandler = Factions.getInstance().getTimerHandler();
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
        this.blockedInteractList = new LinkedList<>();
        this.blockedInteractList.add(Material.WOODEN_DOOR);
        this.blockedInteractList.add(Material.TRAP_DOOR);
        this.blockedInteractList.add(Material.CHEST);
        this.blockedInteractList.add(Material.TRAPPED_CHEST);
        this.blockedInteractList.add(Material.FENCE_GATE);
        this.blockedInteractList.add(Material.STONE_BUTTON);
        this.blockedInteractList.add(Material.WOOD_BUTTON);
        this.blockedInteractList.add(Material.LEVER);
        this.blockedInteractList.add(Material.WOOD_PLATE);
        this.blockedInteractList.add(Material.STONE_PLATE);
        this.blockedInteractList.add(Material.GOLD_PLATE);
        this.blockedInteractList.add(Material.IRON_PLATE);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        final RegionData from = serverHandler.getRegion(event.getFrom());
        final RegionData to = serverHandler.getRegion(event.getTo());

        if (!from.equals(to))
        {
            if (to.getData() != null && to.getData().hasDTRBitmask(DTRType.CAP_ZONE))
            {
                Koth koth = Factions.getInstance().getKothHandler().getKothByName(to.getData().getName());
                if (Factions.getInstance().getKothHandler().getActiveKoths().contains(koth))
                {
                    if (koth.getCapper().get() == null)
                    {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The KoTH &3" + koth.getName().get() + "&7 is now being contested."));
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are now controlling the KoTH&3 " + koth.getName().get() + "&7."));
                        koth.getCapper().set(event.getPlayer().getName());
                        koth.getTimeRemaining().set((koth.getTimeRemaining().get() + System.currentTimeMillis()));
                    }
                }
                return;
            }

            if (from.getData() != null && from.getData().hasDTRBitmask(DTRType.CAP_ZONE))
            {
                Koth koth = Factions.getInstance().getKothHandler().getKothByName(from.getData().getName());
                if (Factions.getInstance().getKothHandler().getActiveKoths().contains(koth))
                {
                    if (koth.getCapper().get() != null)
                    {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The KoTH &3" + koth.getName().get() + "&7 is no longer contested."));
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are no longer controlling the KoTH&3 " + koth.getName().get() + "&7."));
                        koth.getCapper().set(null);
                        koth.getTimeRemaining().set(TimeUnit.MINUTES.toMillis(5L));
                    }
                }
                return;
            }

            if (from.getRegionType().equals(RegionType.SPAWN) && timerHandler.hasTimer(event.getPlayer(), TimerType.PVP_TIMER))
            {
                Timer defaultTimer = timerHandler.getTimer(event.getPlayer(), TimerType.PVP_TIMER);
                defaultTimer.unfreeze();
                String time = setLongFormat(defaultTimer.getTime());
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Your &aPvP Timer&3 has been unfrozen."));
            }
            if (to.getRegionType().equals(RegionType.SPAWN) && timerHandler.hasTimer(event.getPlayer(), TimerType.PVP_TIMER))
            {
                Timer defaultTimer = timerHandler.getTimer(event.getPlayer(), TimerType.PVP_TIMER);
                defaultTimer.freeze();
                String time = setLongFormat(defaultTimer.getTime());
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Your &aPvP Timer&3 has been frozen."));
            }

            boolean fromDeathban = from.getData() != null && from.getData().hasDTRBitmask(DTRType.SAFEZONE);
            boolean toDeathban = to.getData() != null && to.getData().hasDTRBitmask(DTRType.SAFEZONE);

            final String fromString = ChatColor.translateAlternateColorCodes('&', "&7Now Leaving&7: " + from.getName(event.getPlayer()) + (fromDeathban ? " &7(&aNon-Deathban&7)" : " &7(&cDeathban&7)"));
            final String toString = ChatColor.translateAlternateColorCodes('&', "&7Now Entering&7: " + to.getName(event.getPlayer()) + (toDeathban ? " &7(&aNon-Deathban&7)" : " &7(&cDeathban&7)"));
            event.getPlayer().sendMessage(fromString);
            event.getPlayer().sendMessage(toString);
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

    @EventHandler
    public void onPlayerMove1(final PlayerMoveEvent event)
    {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if ((from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) && (Math.abs(event.getTo().getBlockX()) > BORDER || Math.abs(event.getTo().getBlockZ()) > BORDER))
        {
            if (event.getPlayer().getVehicle() != null)
                event.getPlayer().getVehicle().eject();
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BORDER)
            {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BORDER)
            {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "You have hit the border!");
        }
    }

    @EventHandler
    public void onBlockPlace1(final BlockPlaceEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (Math.abs(event.getBlock().getX()) > BORDER || Math.abs(event.getBlock().getZ()) > BORDER)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak1(final BlockBreakEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (Math.abs(event.getBlock().getX()) > BORDER || Math.abs(event.getBlock().getZ()) > BORDER)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(final PlayerPortalEvent event)
    {
        if (Math.abs(event.getTo().getBlockX()) > BORDER || Math.abs(event.getTo().getBlockZ()) > BORDER)
        {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BORDER)
            {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BORDER)
            {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That portal's location is past the border. It has been moved inwards.");
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event)
    {
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld()))
            return;
        if (event.getTo().distance(event.getFrom()) < 0.0 || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN)
            return;
        if (Math.abs(event.getTo().getBlockX()) > BORDER || Math.abs(event.getTo().getBlockZ()) > BORDER)
        {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BORDER)
            {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BORDER)
            {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That location is past the border.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(final BlockIgniteEvent event)
    {
        if (event.getPlayer() != null)
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getBlock().getLocation()))
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation()) != null)
        {
            final Faction owner = Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation());
            if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL && owner.getOnlineMembers().contains(event.getPlayer().getUniqueId()))
            {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (event.isCancelled())
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getBlock().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation());
        if (faction != null && !faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + faction.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (event.isCancelled())
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getBlock().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation());
        if (faction == null || faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            return;
        }
        event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + faction.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
        event.setCancelled(true);
        if (!Arrays.asList(ServerHandler.NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS).contains(event.getBlock().getType()) && (event.getBlock().isEmpty() || event.getBlock().getType().isTransparent() || !event.getBlock().getType().isSolid()))
        {
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event)
    {


        if (event.isCancelled() || !event.isSticky())
        {
            return;
        }
        final Block retractBlock = event.getRetractLocation().getBlock();
        if (retractBlock.isEmpty() || retractBlock.isLiquid())
        {
            return;
        }
        final Faction pistonFaction = Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation());
        final Faction targetFaction = Factions.getInstance().getLandBoard().getFaction(retractBlock.getLocation());
        if (pistonFaction == targetFaction)
        {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        final Faction pistonFaction = Factions.getInstance().getLandBoard().getFaction(event.getBlock().getLocation());
        int i = 0;
        for (final Block block : event.getBlocks())
        {
            ++i;
            final Block targetBlock = event.getBlock().getRelative(event.getDirection(), i + 1);
            final Faction targetFaction = Factions.getInstance().getLandBoard().getFaction(targetBlock.getLocation());
            if (targetFaction != pistonFaction && targetFaction != null)
            {
                if (targetFaction.isRaidable())
                {
                    continue;
                }
                if (!targetBlock.isEmpty() && !targetBlock.isLiquid())
                {
                    continue;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getEntity().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getEntity().getLocation());
        if (faction != null && !faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event)
    {
        if (!(event.getRemover() instanceof Player))
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getEntity().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getEntity().getLocation());
        if (faction != null && !faction.getAllMembers().contains(event.getRemover().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (event.isCancelled() || event.getRightClicked().getType() != EntityType.ITEM_FRAME)
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getRightClicked().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getRightClicked().getLocation());
        if (faction == null || faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(final PlayerInteractEvent event)
    {
        if (event.getPlayer().isOp())
        {
            return;
        }

        if (event.isCancelled() || (event.getClickedBlock() != null && !this.blockedInteractList.contains(event.getClickedBlock().getType())))
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getClickedBlock().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getClickedBlock().getLocation());
        if (faction == null || faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
    {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || event.getEntity().getType() != EntityType.ITEM_FRAME)
        {
            return;
        }
        if (Factions.getInstance().getLandBoard().isUnclaimedOrRaidable(event.getEntity().getLocation()))
        {
            return;
        }
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getEntity().getLocation());
        if (faction != null && !faction.getAllMembers().contains(event.getDamager().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

//    @EventHandler(priority = EventPriority.HIGH)
//    public void onEntityDamageByEntity2(final EntityDamageByEntityEvent event)
//    {
//        if (!(event.getEntity() instanceof Player) || event.isCancelled())
//        {
//            return;
//        }
//        Player damager = null;
//        if (event.getDamager() instanceof Player)
//        {
//            damager = (Player) event.getDamager();
//        } else if (event.getDamager() instanceof Projectile)
//        {
//            final Projectile projectile = (Projectile) event.getDamager();
//            if (projectile.getShooter() instanceof Player)
//            {
//                damager = (Player) projectile.getShooter();
//            }
//        }
//        if (damager != null)
//        {
//            final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(damager.getKiller());
//            final Player victim = (Player) event.getEntity();
//            if (faction == null && faction.getAllMembers().contains(event.getDamager().getUniqueId()) && event.getCause() != EntityDamageEvent.DamageCause.FALL)
//            {
//                damager.sendMessage(ChatColor.GREEN + "You cannot hurt " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + ".");
//                event.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event)
    {
        final Faction faction = Factions.getInstance().getLandBoard().getFaction(event.getBlockClicked().getLocation());
        if (faction != null && !faction.getAllMembers().contains(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
            event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
            event.setItemStack(new ItemStack(event.getBucket()));
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + faction.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
        }
    }
}
