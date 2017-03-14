package xyz.sethy.guard;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Seth on 12/03/2017.
 */
public class Settings
{
    private final Set<Material> passable = new HashSet<>();
    private final Set<Material> doors = new HashSet<>();
    private final Set<Material> trapdoors = new HashSet<>();
    private final Set<Material> gates = new HashSet<>();
    private final Set<Material> fences = new HashSet<>();

    public Settings()
    {
        this.doors.add(Material.WOODEN_DOOR);
        this.doors.add(Material.IRON_DOOR);
        this.doors.add(Material.IRON_DOOR_BLOCK);

        this.gates.add(Material.FENCE_GATE);

        this.fences.add(Material.FENCE);
        this.fences.add(Material.STAINED_GLASS_PANE);
        this.fences.add(Material.THIN_GLASS);
        this.fences.add(Material.NETHER_FENCE);

        this.passable.add(Material.STATIONARY_WATER);
        this.passable.add(Material.ACTIVATOR_RAIL);
        this.passable.add(Material.AIR);
        this.passable.add(Material.ANVIL);
        this.passable.add(Material.BEACON);
        this.passable.add(Material.BIRCH_WOOD_STAIRS);
        this.passable.add(Material.TRAP_DOOR);
        this.passable.add(Material.BREWING_STAND);
        this.passable.add(Material.BRICK_STAIRS);
        this.passable.add(Material.BROWN_MUSHROOM);
        this.passable.add(Material.CACTUS);
        this.passable.add(Material.CAKE_BLOCK);
        this.passable.add(Material.CARPET);
        this.passable.add(Material.CARROT);
        this.passable.add(Material.CAULDRON);
        this.passable.add(Material.CHEST);
        this.passable.add(Material.COBBLESTONE_STAIRS);
        this.passable.add(Material.COCOA);
        this.passable.add(Material.CROPS);
        this.passable.add(Material.DARK_OAK_STAIRS);
        this.passable.add(Material.DAYLIGHT_DETECTOR);
        this.passable.add(Material.DEAD_BUSH);
        this.passable.add(Material.DETECTOR_RAIL);
        this.passable.add(Material.DIODE_BLOCK_OFF);
        this.passable.add(Material.DIODE_BLOCK_ON);
        this.passable.add(Material.DOUBLE_PLANT);
        this.passable.add(Material.DRAGON_EGG);
        this.passable.add(Material.ENCHANTMENT_TABLE);
        this.passable.add(Material.ENDER_CHEST);
        this.passable.add(Material.ENDER_PORTAL);
        this.passable.add(Material.ENDER_PORTAL_FRAME);
        this.passable.add(Material.FIRE);
        this.passable.add(Material.FLOWER_POT);
        this.passable.add(Material.GOLD_PLATE);
        this.passable.add(Material.HOPPER);
        this.passable.add(Material.IRON_PLATE);
        this.passable.add(Material.JUNGLE_WOOD_STAIRS);
        this.passable.add(Material.LADDER);
        this.passable.add(Material.LAVA);
        this.passable.add(Material.LEVER);
        this.passable.add(Material.LONG_GRASS);
        this.passable.add(Material.MELON_STEM);
        this.passable.add(Material.NETHER_BRICK_STAIRS);
        this.passable.add(Material.NETHER_WARTS);
        this.passable.add(Material.PISTON_BASE);
        this.passable.add(Material.PISTON_EXTENSION);
        this.passable.add(Material.PISTON_MOVING_PIECE);
        this.passable.add(Material.PISTON_STICKY_BASE);
        this.passable.add(Material.PORTAL);
        this.passable.add(Material.POTATO);
        this.passable.add(Material.POWERED_RAIL);
        this.passable.add(Material.PUMPKIN_STEM);
        this.passable.add(Material.QUARTZ_STAIRS);
        this.passable.add(Material.RAILS);
        this.passable.add(Material.RED_MUSHROOM);
        this.passable.add(Material.RED_ROSE);
        this.passable.add(Material.REDSTONE_COMPARATOR_OFF);
        this.passable.add(Material.REDSTONE_COMPARATOR_ON);
        this.passable.add(Material.REDSTONE_TORCH_OFF);
        this.passable.add(Material.REDSTONE_TORCH_ON);
        this.passable.add(Material.REDSTONE_WIRE);
        this.passable.add(Material.SANDSTONE_STAIRS);
        this.passable.add(Material.SAPLING);
        this.passable.add(Material.SEEDS);
        this.passable.add(Material.SIGN);
        this.passable.add(Material.SIGN_POST);
        this.passable.add(Material.SKULL);
        this.passable.add(Material.SMOOTH_STAIRS);
        this.passable.add(Material.SNOW);
        this.passable.add(Material.SOUL_SAND);
        this.passable.add(Material.SPRUCE_WOOD_STAIRS);
        this.passable.add(Material.STATIONARY_LAVA);
        this.passable.add(Material.STEP);
        this.passable.add(Material.STONE_BUTTON);
        this.passable.add(Material.STONE_PLATE);
        this.passable.add(Material.SUGAR_CANE_BLOCK);
        this.passable.add(Material.TORCH);
        this.passable.add(Material.TRAPPED_CHEST);
        this.passable.add(Material.TRIPWIRE);
        this.passable.add(Material.TRIPWIRE_HOOK);
        this.passable.add(Material.VINE);
        this.passable.add(Material.WALL_SIGN);
        this.passable.add(Material.WATER);
        this.passable.add(Material.WATER_LILY);
        this.passable.add(Material.WEB);
        this.passable.add(Material.WOOD_BUTTON);
        this.passable.add(Material.WOOD_PLATE);
        this.passable.add(Material.WOOD_STAIRS);
        this.passable.add(Material.WOOD_STEP);
        this.passable.add(Material.YELLOW_FLOWER);
        this.passable.add(Material.BED_BLOCK);
    }

    public boolean correctEnderpearlTeleportation()
    {
        return true;
    }

    public boolean correctGroundTeleportation()
    {
        return true;
    }

    public boolean lenient()
    {
        return true;
    }

    public boolean updateMaterialLists()
    {
        return true;
    }

    public Set<Material> getPassable()
    {
        return this.passable;
    }

    public Set<Material> getDoors()
    {
        return this.doors;
    }

    public Set<Material> getTrapdoors()
    {
        return this.trapdoors;
    }

    public Set<Material> getGates()
    {
        return this.gates;
    }

    public Set<Material> getFences()
    {
        return this.fences;
    }
}
