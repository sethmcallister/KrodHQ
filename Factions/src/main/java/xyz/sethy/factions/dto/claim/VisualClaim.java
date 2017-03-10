package xyz.sethy.factions.dto.claim;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 01/12/2016.
 */
public class VisualClaim implements Listener
{
    public static final int MAP_RADIUS = 50;
    public static final Material[] MAP_MATERIALS;
    private static Map<String, VisualClaim> currentMaps;
    private static Map<String, VisualClaim> visualClaims;
    private static Map<String, List<Location>> packetBlocksSent;
    private static Map<String, List<Location>> mapBlocksSent;
    private Player player;
    private VisualClaimType type;
    private boolean bypass;
    private Location corner1;
    private Location corner2;

    public void draw(final boolean silent)
    {
        if (VisualClaim.currentMaps.containsKey(this.player.getName()) && this.type == VisualClaimType.MAP)
        {
            VisualClaim.currentMaps.get(this.player.getName()).cancel(true);
            return;
        }
        if (VisualClaim.visualClaims.containsKey(this.player.getName()) && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE || this.type == VisualClaimType.RESIZE))
        {
            VisualClaim.visualClaims.get(this.player.getName()).cancel(true);
        }
        switch (this.type)
        {
            case CREATE:
            case RESIZE:
            case KOTH:
            {
                VisualClaim.visualClaims.put(this.player.getName(), this);
                break;
            }
            case MAP:
            {
                VisualClaim.currentMaps.put(this.player.getName(), this);
                break;
            }
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());

        switch (this.type)
        {
            case KOTH:
            case CREATE:
            {
                break;
            }
            case MAP:
            {
                int claimIteration = 0;
                final Map<Map.Entry<Claim, Faction>, Material> sendMaps = new ConcurrentHashMap<>();
                for (final Map.Entry<Claim, Faction> regionData : Factions.getInstance().getLandBoard().getRegionData(this.player.getLocation(), 50, 256, 50))
                {
                    final Material mat = this.getMaterial(claimIteration);
                    ++claimIteration;
                    if (regionData.getKey() != null)
                    {
                        this.drawClaim(regionData.getKey(), mat);
                        sendMaps.put(regionData, mat);
                    }
                }
                if (sendMaps.isEmpty())
                {
                    if (!silent)
                    {
                        this.player.sendMessage(ChatColor.GRAY + "There are no claims within " + ChatColor.RED + 50 + ChatColor.GRAY + " blocks of you!");
                    }
                    this.cancel(true);
                }
                if (!silent)
                {
                    for (final Map.Entry<Map.Entry<Claim, Faction>, Material> claim : sendMaps.entrySet())
                    {
                        if (claim.getKey().getValue() != null)
                            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The faction &c" + claim.getKey().getValue().getName(player) + "&7 owns the land &c(" + claim.getValue().name() + "&c)"));
                    }
                    break;
                }
                break;
            }
            default:
                break;
        }
    }

    public boolean containsOtherClaim(final Claim claim)
    {
        if (!Factions.getInstance().getLandBoard().isUnclaimed(claim.getMaximumPoint()))
        {
            return true;
        }
        if (!Factions.getInstance().getLandBoard().isUnclaimed(claim.getMinimumPoint()))
        {
            return true;
        }
        if (Math.abs(claim.getX1() - claim.getX2()) == 0 || Math.abs(claim.getZ1() - claim.getZ2()) == 0)
        {
            return false;
        }
        for (final Coordinate location : claim)
        {
            if (!Factions.getInstance().getLandBoard().isUnclaimed(new Location(Bukkit.getServer().getWorld(claim.getWorld()), (double) location.getX(), 80.0, (double) location.getZ())))
            {
                return true;
            }
        }
        return false;
    }

    public Set<Claim> touchesOtherClaim(final Claim claim)
    {
        final Set<Claim> touchingClaims = new HashSet<>();
        for (final Coordinate coordinate : claim.outset(Claim.CuboidDirection.Horizontal, 1))
        {
            final Location loc = new Location(Bukkit.getServer().getWorld(claim.getWorld()), (double) coordinate.getX(), 80.0, (double) coordinate.getZ());
            final Claim cc = Factions.getInstance().getLandBoard().getClaim(loc);
            if (cc != null)
            {
                touchingClaims.add(cc);
            }
        }
        return touchingClaims;
    }

    public void setLoc(final int locationId, final Location clicked)
    {
        final Faction playerTeam = Factions.getInstance().getFactionManager().findByPlayer(this.player);
        if (playerTeam == null)
        {
            this.player.sendMessage(ChatColor.RED + "You have to be in a faction to claim land.");
            this.cancel(true);
            return;
        }
        if (locationId == 1)
        {
            if (this.corner2 != null && this.isIllegal(new Claim(clicked, this.corner2)))
            {
                return;
            }
            this.clearPillarAt(this.corner1);
            this.corner1 = clicked;
        }
        else if (locationId == 2)
        {
            if (this.corner1 != null && this.isIllegal(new Claim(this.corner1, clicked)))
            {
                return;
            }
            this.clearPillarAt(this.corner2);
            this.corner2 = clicked;
        }
        this.player.sendMessage(ChatColor.GRAY + "Set claim's location " + ChatColor.LIGHT_PURPLE + locationId + ChatColor.GRAY + " to " + ChatColor.GREEN + "(" + ChatColor.WHITE + clicked.getBlockX() + ", " + clicked.getBlockY() + ", " + clicked.getBlockZ() + ChatColor.GREEN + ")" + ChatColor.GRAY + ".");
        Bukkit.getScheduler().runTaskLater(Factions.getInstance().getPlugin(), () -> this.erectPillar(clicked, Material.EMERALD_BLOCK), 1L);
        final int price = this.getPrice();
        if (price != -1)
        {
            final int x = Math.abs(this.corner1.getBlockX() - this.corner2.getBlockX());
            final int z = Math.abs(this.corner1.getBlockZ() - this.corner2.getBlockZ());

            if (Factions.getInstance().isKitmap())
            {
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Claims are free during KitMap, with a maximum land claim of 40x40."));
                return;
            }

            if (price > playerTeam.getBalance() && !this.bypass)
                this.player.sendMessage(ChatColor.GRAY + "Claim cost: " + ChatColor.RED + "$" + price + ChatColor.GRAY + ", Current size: (" + ChatColor.WHITE + x + ", " + z + ChatColor.GRAY + "), " + ChatColor.WHITE + x * z + ChatColor.GRAY + " blocks");
            else
                this.player.sendMessage(ChatColor.GRAY + "Claim cost: " + ChatColor.GREEN + "$" + price + ChatColor.GRAY + ", Current size: (" + ChatColor.WHITE + x + ", " + z + ChatColor.GRAY + "), " + ChatColor.WHITE + x * z + ChatColor.GRAY + " blocks");
        }
    }

    public void cancel(final boolean complete)
    {
        if (complete && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE))
        {
            this.clearPillarAt(this.corner1);
            this.clearPillarAt(this.corner2);
        }
        if (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE)
        {
            this.player.getInventory().remove(Factions.getInstance().getItemHandler().selectionWand);
        }
        HandlerList.unregisterAll(this);
        switch (this.type)
        {
            case MAP:
            {
                VisualClaim.currentMaps.remove(this.player.getName());
                if (VisualClaim.mapBlocksSent.containsKey(this.player.getName()))
                {
                    VisualClaim.mapBlocksSent.get(this.player.getName()).forEach(l -> this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData()));
                }
                VisualClaim.mapBlocksSent.remove(this.player.getName());
                break;
            }
            case KOTH:
            case CREATE:
            case RESIZE:
            {
                VisualClaim.visualClaims.remove(this.player.getName());
                break;
            }
        }
        if (VisualClaim.packetBlocksSent.containsKey(this.player.getName()))
        {
            VisualClaim.packetBlocksSent.get(this.player.getName()).forEach(l -> this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData()));
        }
        VisualClaim.packetBlocksSent.remove(this.player.getName());
    }

    private void purchaseClaim()
    {
        if (Factions.getInstance().getFactionManager().findByPlayer(player) == null)
        {
            this.player.sendMessage(ChatColor.RED + "You have to be in a faction to claim land!");
            this.cancel(true);
            return;
        }
        if (this.corner1 != null && this.corner2 != null)
        {
            final int price = this.getPrice();
            final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(player);
            if (!this.bypass && faction.getClaims().size() >= 1)
            {
                this.player.sendMessage(ChatColor.RED + "Your faction has the maximum amount of claims, which is " + 1 + ".");
                return;
            }
            if (!this.bypass && !faction.isLeader(this.player))
            {
                this.player.sendMessage(ChatColor.RED + "Only faction captains can claim land.");
                return;
            }
            if (!this.bypass && !Factions.getInstance().isKitmap() && faction.getBalance() < price)
            {
                this.player.sendMessage(ChatColor.RED + "Your faction does not have enough money to do this!");
                return;
            }
            if (!this.bypass && faction.isRaidable())
            {
                this.player.sendMessage(ChatColor.RED + "You cannot claim land while raidable.");
                return;
            }

            final Claim claim = new Claim(this.corner1, this.corner2);

            final int x = Math.abs(claim.getX1() - claim.getX2());
            final int z = Math.abs(claim.getZ1() - claim.getZ2());

            if (Factions.getInstance().isKitmap() && !this.bypass && (x > 40 || z > 40))
            {
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Max faction claim size during kitmap is 40x40."));
                return;
            }

            if (this.isIllegal(claim))
            {
                return;
            }
            claim.setName(faction.getName() + "_" + (100 + new Random().nextInt(800)));
            if (type == VisualClaimType.KOTH)
            {
                claim.setY1(this.corner1.getBlockY());
                claim.setY2(this.corner2.getBlockY());
            }
            else
            {
                claim.setY1(0);
                claim.setY2(256);
            }
            Factions.getInstance().getLandBoard().setFactionAt(claim, faction);
            faction.getClaims().add(claim);
            faction.flagSave();
            this.player.sendMessage(ChatColor.GRAY + "You have claimed this land for your faction!");
            if (Factions.getInstance().isKitmap())
            {
                this.cancel(true);
                return;
            }
            if (!this.bypass)
            {
                faction.setBalance(faction.getBalance() - price);
                this.player.sendMessage(ChatColor.GRAY + "Your factions now has " + ChatColor.RED + "$" + faction.getBalance() + ChatColor.GRAY + " (Price: " + "$" + price);
            }
            this.cancel(true);
        }
        else
        {
            this.player.sendMessage(ChatColor.RED + "You have not selected both corners of your claim yet!");
        }
    }

    public int getPrice()
    {
        if (this.corner1 == null || this.corner2 == null)
        {
            return -1;
        }
        return Claim.getPrice(new Claim(this.corner1, this.corner2), Factions.getInstance().getFactionManager().findByPlayer(this.player), true);
    }

    private void drawClaim(final Claim claim, final Material material)
    {
        for (final Location loc : claim.getCornerLocations())
        {
            this.erectPillar(loc, material);
        }
    }

    private void erectPillar(final Location loc, final Material mat)
    {
        final Location set = loc.clone();
        List<Location> locs = new ArrayList<>();
        if (this.type == VisualClaimType.MAP)
        {
            if (VisualClaim.mapBlocksSent.containsKey(this.player.getName()))
            {
                locs = VisualClaim.mapBlocksSent.get(this.player.getName());
            }
        }
        else if (VisualClaim.packetBlocksSent.containsKey(this.player.getName()))
        {
            locs = VisualClaim.packetBlocksSent.get(this.player.getName());
        }
        for (int i = 0; i < 256; ++i)
        {
            set.setY((double) i);
            if (set.getBlock().getType() == Material.AIR || set.getBlock().getType().isTransparent())
            {
                if (i % 5 == 0)
                {
                    this.player.sendBlockChange(set, mat, (byte) 0);
                }
                else
                {
                    this.player.sendBlockChange(set, Material.GLASS, (byte) 0);
                }
                locs.add(set.clone());
            }
        }
        if (this.type == VisualClaimType.MAP)
        {
            VisualClaim.mapBlocksSent.put(this.player.getName(), locs);
        }
        else
        {
            VisualClaim.packetBlocksSent.put(this.player.getName(), locs);
        }
    }

    private void clearPillarAt(final Location loc)
    {
        if (VisualClaim.packetBlocksSent.containsKey(this.player.getName()) && loc != null)
        {
            VisualClaim.packetBlocksSent.get(this.player.getName()).removeIf(l ->
            {
                if (l.getBlockX() == loc.getBlockX() && l.getBlockZ() == loc.getBlockZ())
                {
                    this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData());
                    return true;
                }
                return false;
            });
        }
    }

    public boolean isIllegal(final Claim claim)
    {
        final Faction faction = Factions.getInstance().getFactionManager().findByPlayer(this.player);
        if (!this.bypass && this.containsOtherClaim(claim))
        {
            this.player.sendMessage(ChatColor.RED + "This claim contains unclaimable land!");
            return true;
        }
        if (!this.bypass && this.player.getWorld().getEnvironment() != World.Environment.NORMAL)
        {
            this.player.sendMessage(ChatColor.RED + "Land can only be claimed in the overworld.");
            return true;
        }
        final Set<Claim> touching = this.touchesOtherClaim(claim);
        final Set<Claim> cloneCheck = new HashSet<>();
        touching.forEach(tee -> cloneCheck.add(tee.clone()));
        final boolean contains = cloneCheck.removeIf(faction::ownsClaim);
        if (!this.bypass && faction.getClaims().size() > 0 && !contains)
        {
            this.player.sendMessage(ChatColor.RED + "All of your claims must be touching each other!");
            return true;
        }
        if (!this.bypass && (touching.size() > 1 || (touching.size() == 1 && !contains)))
        {
            this.player.sendMessage(ChatColor.RED + "Your claim must be at least 1 block away from enemy claims!");
            return true;
        }
        final int x = Math.abs(claim.getX1() - claim.getX2());
        final int z = Math.abs(claim.getZ1() - claim.getZ2());
        if (!this.bypass && (x < 4 || z < 4))
        {
            this.player.sendMessage(ChatColor.GRAY + "Your claim is too small! The claim has to be at least (" + ChatColor.RED + "5 x 5" + ChatColor.GRAY + ")");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e)
    {
        if (e.getPlayer() == this.player && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE || this.type == VisualClaimType.KOTH) && this.player.getItemInHand() != null && this.player.getItemInHand().getType() == Material.GOLD_HOE)
        {
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (!this.bypass && !Factions.getInstance().getLandBoard().isUnclaimed(e.getClickedBlock().getLocation()))
                {
                    this.player.sendMessage(ChatColor.RED + "You can only claim land in the Wilderness!");
                    return;
                }
                this.setLoc(2, e.getClickedBlock().getLocation());
            }
            else if (e.getAction() == Action.LEFT_CLICK_BLOCK)
            {
                if (!this.bypass && !Factions.getInstance().getLandBoard().isUnclaimed(e.getClickedBlock().getLocation()))
                {
                    this.player.sendMessage(ChatColor.RED + "You can only claim land in the Wilderness!");
                    return;
                }
                if (this.player.isSneaking())
                {
                    this.purchaseClaim();
                }
                else
                {
                    this.setLoc(1, e.getClickedBlock().getLocation());
                }
            }
            else if (e.getAction() == Action.LEFT_CLICK_AIR && this.player.isSneaking())
            {
                this.purchaseClaim();
            }
            else if (e.getAction() == Action.RIGHT_CLICK_AIR)
            {
                this.cancel(false);
                this.player.sendMessage(ChatColor.RED + "You have unset your first and second locations!");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e)
    {
        if (this.player == e.getPlayer())
        {
            this.cancel(true);
        }
    }

    public Material getMaterial(int iteration)
    {
        if (iteration == -1)
        {
            return Material.IRON_BLOCK;
        }
        while (iteration >= VisualClaim.MAP_MATERIALS.length)
        {
            iteration -= VisualClaim.MAP_MATERIALS.length;
        }
        return VisualClaim.MAP_MATERIALS[iteration];
    }

    public static VisualClaim getVisualClaim(final String name)
    {
        return VisualClaim.visualClaims.get(name);
    }

    public VisualClaim(final Player player, final VisualClaimType type, final boolean bypass)
    {
        super();
        if (player == null)
            throw new NullPointerException("player");
        if (type == null)
            throw new NullPointerException("type");

        this.player = player;
        this.type = type;
        this.bypass = bypass;
    }

    public static Map<String, VisualClaim> getCurrentMaps()
    {
        return VisualClaim.currentMaps;
    }

    public static Map<String, VisualClaim> getVisualClaims()
    {
        return VisualClaim.visualClaims;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    static
    {
        MAP_MATERIALS = new Material[]{Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.LOG, Material.BRICK, Material.WOOD, Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK, Material.CHEST, Material.MELON_BLOCK, Material.STONE, Material.COBBLESTONE, Material.COAL_BLOCK, Material.DIAMOND_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.FURNACE};
        VisualClaim.currentMaps = new ConcurrentHashMap<>();
        VisualClaim.visualClaims = new ConcurrentHashMap<>();
        VisualClaim.packetBlocksSent = new ConcurrentHashMap<>();
        VisualClaim.mapBlocksSent = new ConcurrentHashMap<>();
    }
}
