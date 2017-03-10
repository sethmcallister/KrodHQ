package xyz.sethy.factions.commands.faction;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.API;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.claim.LandBoard;
import xyz.sethy.factions.handlers.commands.ICommand;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.Timer;
import xyz.sethy.factions.timers.TimerType;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by Seth on 26/01/2017.
 */
public class FactionStuckCommand implements ICommand, Listener
{
    private final double MAX_DISTANCE = 5.0;
    private final Set<Integer> warn;
    private List<UUID> warping = new LinkedList<>();
    private List<UUID> damaged = new LinkedList<>();

    public FactionStuckCommand()
    {
        this.warn = new HashSet<>();
        warn.add(270);
        warn.add(240);
        warn.add(210);
        warn.add(180);
        warn.add(150);
        warn.add(120);
        warn.add(90);
        warn.add(60);
        warn.add(30);
        warn.add(10);
        warn.add(5);
        warn.add(4);
        warn.add(3);
        warn.add(2);
        warn.add(1);

        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }


    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/f stuck"));
            return;
        }
        if (sender.getWorld().getEnvironment() != World.Environment.NORMAL)
        {
            sender.sendMessage(ChatColor.RED + "You can only use this command from the overworld.");
            return;
        }

        Timer timer = Factions.getInstance().getTimerHandler().getTimer(sender, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot /f stuck while in combat."));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You will be teleported to safety in &c5 minuets&7."));

        Timer timer1 = new DefaultTimer(TimerType.F_STUCK, TimeUnit.MINUTES.toMillis(5L), sender);
        Factions.getInstance().getTimerHandler().addTimer(sender, timer1);

        new BukkitRunnable()
        {
            private int seconds = (sender.isOp() && sender.getGameMode() == GameMode.CREATIVE) ? 5 : 300;
            private Location loc = sender.getLocation();
            private Location prevLoc;
            private int xStart = (int) this.loc.getX();
            private int yStart = (int) this.loc.getY();
            private int zStart = (int) this.loc.getZ();
            private Location nearest;

            public void run()
            {
                if (damaged.contains(sender.getUniqueId()))
                {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your teleportation has been cancelled."));
                    damaged.remove(sender.getUniqueId());
                    warping.remove(sender.getUniqueId());
                    this.cancel();
                    return;

                }
                if (!sender.isOnline())
                {
                    this.cancel();
                    return;
                }
                if (this.seconds == 5)
                {
                    nearest = nearestSafeLocation(sender.getLocation());
                }
                if (this.seconds <= 0)
                {
                    if (this.nearest == null)
                        kick(sender);
                    else
                    {
                        sender.teleport(this.nearest);
                        sender.sendMessage(ChatColor.YELLOW + "Teleported you to the nearest safe area!");
                    }
                    warping.remove(sender.getUniqueId());
                    this.cancel();
                    return;
                }
                final Location loc = sender.getLocation();
                if (loc.getX() >= this.xStart + 5.0 || loc.getX() <= this.xStart - 5.0 || loc.getY() >= this.yStart + 5.0 || loc.getY() <= this.yStart - 5.0 || loc.getZ() >= this.zStart + 5.0 || loc.getZ() <= this.zStart - 5.0)
                {
                    sender.sendMessage(ChatColor.RED + "You moved more than " + 5.0 + " blocks, teleport cancelled!");
                    warping.remove(sender.getUniqueId());
                    this.cancel();
                    return;
                }
                if (warn.contains(this.seconds))
                    sender.sendMessage(ChatColor.YELLOW + "You will be teleported in " + ChatColor.GOLD + this.seconds + ChatColor.YELLOW + " seconds.");
                --this.seconds;
            }
        }.runTaskTimer(API.getPlugin(), 0L, 20L);
    }

    private static void kick(final Player player)
    {
        player.setMetadata("loggedout", new FixedMetadataValue(API.getPlugin(), true));
        player.kickPlayer(ChatColor.RED + "We couldn't find a safe location, so we safely logged you out for now. Contact a staff member before logging back on! " + ChatColor.BLUE + "TeamSpeak: ts.kihar.net");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            if (this.damaged.contains(event.getEntity().getUniqueId()))
            {
                this.damaged.add(event.getEntity().getUniqueId());
            }
        }
    }

    private static Location nearestSafeLocation(final Location origin)
    {
        final LandBoard landBoard = Factions.getInstance().getLandBoard();
        if (landBoard.getClaim(origin) == null)
            return getActualHighestBlock(origin.getBlock()).getLocation().add(0.0, 1.0, 0.0);

        for (int xPos = 0, xNeg = 0; xPos < 250; ++xPos, --xNeg)
        {
            for (int zPos = 0, zNeg = 0; zPos < 250; ++zPos, --zNeg)
            {
                final Location atPos = origin.clone().add((double) xPos, 0.0, (double) zPos);
                final Location atNeg = origin.clone().add((double) xNeg, 0.0, (double) zNeg);
                if (landBoard.getClaim(atPos) == null)
                {
                    return getActualHighestBlock(atPos.getBlock()).getLocation().add(0.0, 1.0, 0.0);
                }
                if (landBoard.getClaim(atNeg) == null)
                {
                    return getActualHighestBlock(atNeg.getBlock()).getLocation().add(0.0, 1.0, 0.0);
                }
            }
        }
        return null;
    }

    private static Block getActualHighestBlock(Block block)
    {
        for (block = block.getWorld().getHighestBlockAt(block.getLocation()); block.getType() == Material.AIR && block.getY() > 0; block = block.getRelative(BlockFace.DOWN))
        {
        }
        return block;
    }
}
