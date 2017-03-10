package xyz.sethy.sg.map;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import xyz.sethy.sg.SG;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sethm on 22/12/2016.
 */
public class MapPopulator extends BlockPopulator
{
    private List<SchematicHolder> list;
    private List<Material> ok;

    public MapPopulator()
    {
        this.list = new ArrayList<>();
        this.ok = Arrays.asList(Material.AIR, Material.SNOW, Material.SNOW_BLOCK, Material.SNOW_BLOCK, Material.DIRT, Material.GRASS, Material.DEAD_BUSH, Material.RED_ROSE, Material.YELLOW_FLOWER, Material.LONG_GRASS, Material.SAND, Material.DOUBLE_PLANT);
        final File schematicFolder = new File(SG.getInstance().getPlugin().getDataFolder(), "schematic");
        schematicFolder.mkdirs();
        final File configuration = new File(schematicFolder, "config");
        configuration.mkdirs();
        File[] listFiles;
        for (int length = (listFiles = schematicFolder.listFiles()).length, i = 0; i < length; ++i)
        {
            final File schematic = listFiles[i];
            if (schematic.getName().endsWith(".schematic"))
            {
                final String name = schematic.getName().replace(".schematic", "");
                final File config = new File(configuration, String.valueOf(name) + ".yml");
                final SchematicHolder hold = new SchematicHolder();
                try
                {
                    hold.loadSchmeatic(schematic);
                    final YamlConfiguration yamlConfig = this.ensureCorrect(config);
                    hold.setYOffset(yamlConfig.getInt("YOffset"));
                    hold.setPercentage(yamlConfig.getInt("Percentage"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    continue;
                }
                this.list.add(hold);
            }
        }
    }

    public YamlConfiguration ensureCorrect(final File file) throws IOException
    {
        if (!file.exists())
        {
            file.createNewFile();
        }
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (!configuration.contains("YOffset"))
        {
            configuration.set("YOffset", (Object) (-1));
        }
        if (!configuration.contains("Percentage"))
        {
            configuration.set("Percentage", (Object) 2500);
        }
        configuration.save(file);
        return configuration;
    }

    public void populate(final World world, final Random rand, final Chunk chunk)
    {
        final int chunkX = chunk.getX();
        final int chunkZ = chunk.getZ();
        if (chunkX >= -1 && chunkX <= 1)
        {
            return;
        }
        if (chunkZ >= -1 && chunkZ <= 1)
        {
            return;
        }
        int skipZ = 0;
        int skipX = 0;
        for (int x = 0; x < 16; ++x)
        {
            if (skipX > 0)
            {
                --skipX;
            }
            else
            {
                for (int z = 0; z < 16; ++z)
                {
                    if (skipZ > 0)
                    {
                        --skipZ;
                    }
                    else
                    {
                        Collections.shuffle(this.list);
                        for (final SchematicHolder man2 : this.list)
                        {
                            if (this.canPlace(man2, chunk, x, z) && rand.nextInt(man2.getPercentage()) == 0)
                            {
                                this.paste(man2, chunk.getWorld(), chunk.getX() * 16 + x, chunk.getZ() * 16 + z, rand);
                                skipZ = man2.getLength() + 1;
                                skipX = man2.getWidth() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void paste(final SchematicHolder man, final World world, final int blockX, final int blockZ, final Random rand)
    {
        man.rotateRandomly();
        final int height = man.getHeight();
        final int starty = world.getHighestBlockAt(blockX, blockZ).getY() + man.getYOffset();
        for (int x = 0; x < man.getWidth(); ++x)
        {
            for (int z = 0; z < man.getLength(); ++z)
            {
                final int realX = x + blockX;
                final int realZ = z + blockZ;
                for (int y = 0; y < height; ++y)
                {
                    final int id = man.getBlockIDAt(x, y, z);
                    final int data = man.getDataAt(x, y, z);
                    final int realY = starty + y;
                    if (id > 0)
                    {
                        final Block block = world.getBlockAt(realX, realY, realZ);
                        if (id == 63)
                        {
                            block.setTypeId(0);
                            block.setData((byte) 0);
                        }
                        else
                        {
                            block.setTypeId(id);
                            block.setData((byte) data);
                            if (id == 52)
                            {
                                final BlockState spawner = block.getState();
                                if (spawner instanceof CreatureSpawner)
                                {
                                    final CreatureSpawner creatureSpawner = (CreatureSpawner) spawner;
                                    if (block.getRelative(BlockFace.DOWN).getType() == Material.WOOD)
                                    {
                                        creatureSpawner.setSpawnedType(EntityType.CREEPER);
                                    }
                                    else
                                    {
                                        creatureSpawner.setSpawnedType(EntityType.ZOMBIE);
                                    }
                                }
                            }
                            else if (world.getBlockAt(realX, realY, realZ).getState() instanceof Chest)
                            {
                                final Chest chest = (Chest) world.getBlockAt(realX, realY, realZ).getState();
                                chest.getInventory().clear();
                                for (ItemStack itemStack : getChestItems())
                                {
                                    chest.getInventory().addItem(itemStack);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ItemStack> getChestItems()
    {
        Random rand = new Random();
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        int w = 4;
        while (w > 0)
        {
            int i = rand.nextInt(25) + 1;
            if (i == 1)
            {
                int a = rand.nextInt(5) + 1;
                Potion potion = new Potion(PotionType.INSTANT_HEAL);
                potion.setLevel(2);
                potion.setSplash(true);
                itemStacks.add(potion.toItemStack(a));
            }
            else if (i == 2 || i == 18)
            {
                itemStacks.add(new ItemStack(Material.DIAMOND_SWORD));
            }
            else if (i == 3)
            {
                itemStacks.add(new ItemStack(Material.CAKE));
            }
            else if (i == 4)
            {
                int a = rand.nextInt(11) + 4;
                itemStacks.add(new ItemStack(Material.COOKED_FISH, a));
            }
            else if (i == 5)
            {
                int a = rand.nextInt(5) + 1;
                itemStacks.add(new ItemStack(Material.GLOWSTONE_DUST, a));
            }
            else if (i == 6)
            {
                int a = rand.nextInt(5) + 1;
                itemStacks.add(new ItemStack(Material.SULPHUR, a));
            }
            else if (i == 7)
            {
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 1, true);
                itemStack.setItemMeta(meta);
                itemStacks.add(itemStack);
            }
            else if (i == 8)
            {
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                itemStack.setItemMeta(meta);
                itemStacks.add(itemStack);
            }
            else if (i == 9)
            {
                int a = rand.nextInt(9) + 5;
                itemStacks.add(new ItemStack(Material.WEB, a));
            }
            else if (i == 10)
            {
                int a = rand.nextInt(7) + 3;
                itemStacks.add(new ItemStack(Material.ENDER_PEARL, a));
            }
            else if (i == 11)
            {
                itemStacks.add(new ItemStack(Material.FISHING_ROD, 1));
            }
            else if (i == 12)
            {
                int a = rand.nextInt(5) + 1;
                Potion potion = new Potion(PotionType.INSTANT_HEAL);
                potion.setLevel(1);
                potion.setSplash(true);
                itemStacks.add(potion.toItemStack(a));
            }
            else if (i == 13)
            {
                int a = rand.nextInt(5) + 1;
                Potion potion = new Potion(PotionType.INSTANT_HEAL);
                potion.setLevel(1);
                potion.setSplash(true);
                itemStacks.add(potion.toItemStack(a));
            }
            else if (i == 15)
            {
                int a = rand.nextInt(3) + 1;
                Potion potion = new Potion(PotionType.SPEED);
                potion.setLevel(2);
                itemStacks.add(potion.toItemStack(a));
            }
            else if (i == 16)
            {
                int a = rand.nextInt(5) + 1;
                Potion potion = new Potion(PotionType.SPEED);
                potion.setLevel(1);
                itemStacks.add(potion.toItemStack(a));
            }
            else if (i == 17 || i == 19 || i == 20)
            {
                int a = rand.nextInt(7) + 3;
                itemStacks.add(new ItemStack(Material.DIAMOND, a));
            }
            else if (i == 21 || i == 22 || i == 14)
            {
                int a = rand.nextInt(7) + 2;
                itemStacks.add(new ItemStack(Material.EXP_BOTTLE, a));
            }
            w--;
        }
        return itemStacks;
    }

    public boolean canPlace(final SchematicHolder schematic, final Chunk chunk, final int placeX, final int placeZ)
    {
        int foundY = -1;
        for (int x = 0; x < schematic.getWidth(); ++x)
        {
            for (int z = 0; z < schematic.getLength(); ++z)
            {
                final Block block = chunk.getWorld().getHighestBlockAt(chunk.getX() * 16 + placeX + x, chunk.getZ() * 16 + placeZ + z);
                if (!this.ok.contains(block.getType()) || !this.ok.contains(block.getRelative(BlockFace.DOWN).getType()))
                {
                    return false;
                }
                if (foundY != -1 && block.getY() != foundY)
                {
                    return false;
                }
                foundY = block.getY();
            }
        }
        return true;
    }
}
