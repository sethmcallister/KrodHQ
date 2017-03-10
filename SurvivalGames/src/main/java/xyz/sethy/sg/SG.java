package xyz.sethy.sg;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import redis.clients.jedis.Jedis;
import xyz.sethy.sg.commands.ForceStartCommand;
import xyz.sethy.sg.commands.SuspendGameCommand;
import xyz.sethy.sg.items.GameItems;
import xyz.sethy.sg.listeners.*;
import xyz.sethy.sg.map.MapPopulator;
import xyz.sethy.sg.map.RoadProcessor;
import xyz.sethy.sg.scoreboard.ScoreboardHandler;
import xyz.sethy.sg.scoreboard.ScoreboardThread;
import xyz.sethy.sg.spectating.SpectatorHandler;
import xyz.sethy.sg.states.GameState;
import xyz.sethy.sg.tasks.GameTask;
import xyz.sethy.sg.tasks.PreGameTask;
import xyz.sethy.sg.timers.TimerHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 21/12/2016.
 */
public class SG implements Listener
{
    private static SG instance;
    private Plugin plugin;
    private GameState gameState;
    private ScoreboardHandler scoreboardHandler;
    private TimerHandler timerHandler;
    private int borderSize;
    private final AtomicInteger online = new AtomicInteger(0);
    private final Collection<Player> players = new ArrayList<>();
    private GameTask gameTask;
    private PreGameTask preGameTask;
    private GameItems gameItems;
    private SpectatorHandler spectatorHandler;
    private Jedis jedis = new Jedis();

    public SG(Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        this.registerListeners();
        this.gameState = GameState.PREGAME;
        this.scoreboardHandler = new ScoreboardHandler();
        this.timerHandler = new TimerHandler();
        this.borderSize = 500;
        this.gameItems = new GameItems();
        this.spectatorHandler = new SpectatorHandler();
        jedis.connect();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        this.registerCommands();

        Bukkit.getPluginManager().registerEvents(this, plugin);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new ScoreboardThread(), 1L, 1L);
        this.preGameTask = new PreGameTask();
        this.gameTask = null;
    }


    private void registerCommands()
    {
        new ForceStartCommand();
        new SuspendGameCommand();
    }

    private void registerListeners()
    {
        Bukkit.getServer().getPluginManager().registerEvents(new EnderpearlListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestHitListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ASyncPlayerChatListner(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamageListener(), plugin);
        new PlayerMoveListener();
        new BlockBreakListener();
    }

    public static void load()
    {
        delete(new File("world"));
    }


    public static void delete(final File f)
    {
        if (f.isDirectory())
        {
            File[] listFiles = new File[0];
            for (int length = f.listFiles().length, i = 0; i < length; ++i)
            {
                final File c = listFiles[i];
                delete(c);
            }
        }
        f.delete();
    }

    public void renderFeast()
    {
        for (int x = -8; x < 8; ++x)
        {
            for (int z = -8; z < 8; ++z)
            {
                if (RandomUtils.nextInt(20) == 0)
                {
                    final Block block = Bukkit.getWorlds().get(0).getBlockAt(x + 8, 64, z + 8);
                    if (block.getRelative(BlockFace.NORTH).getType() != Material.CHEST)
                    {
                        if (block.getRelative(BlockFace.SOUTH).getType() != Material.CHEST)
                        {
                            if (block.getRelative(BlockFace.EAST).getType() != Material.CHEST)
                            {
                                if (block.getRelative(BlockFace.WEST).getType() != Material.CHEST)
                                {
                                    block.setType(Material.CHEST);
                                    final Chest chest = (Chest) block.getState();
                                    for (final ItemStack item : getFeastItems())
                                    {
                                        chest.getInventory().addItem(item);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final Block block2 = Bukkit.getWorlds().get(0).getBlockAt(8, 64, 8);
        block2.setType(Material.ENCHANTMENT_TABLE);
    }


    private ArrayList<ItemStack> getFeastItems()
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
                meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);
                itemStack.setItemMeta(meta);
                itemStacks.add(itemStack);
            }
            else if (i == 8)
            {
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
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
            else if (i == 12 || i == 13)
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

    public void disable()
    {
        for (Player player : players)
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("sglobby");
            player.sendPluginMessage(SG.getInstance().getPlugin(), "BungeeCord", out.toByteArray());
            if (players.isEmpty())
            {
                stopServer();
            }
        }
    }

    private void stopServer()
    {
        plugin.getServer().getScheduler().cancelAllTasks();
        plugin.getServer().shutdown();
    }

    @EventHandler
    public void onWorldInit(final WorldInitEvent event)
    {
        System.out.println("============================================= world init event");
        event.getWorld().setSpawnLocation(0, 65, 0);
        event.getWorld().getPopulators().add(new MapPopulator());
    }

    @EventHandler
    public void onWorldLoadEvent(final WorldLoadEvent event)
    {
        event.getWorld().loadChunk(new Location(event.getWorld(), 7, 0, 7).getChunk());
        new RoadProcessor(plugin, event.getWorld().getSpawnLocation(), 15000, 2).run();
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public static SG getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public ScoreboardHandler getScoreboardHandler()
    {
        return scoreboardHandler;
    }

    public TimerHandler getTimerHandler()
    {
        return timerHandler;
    }

    public int getBorderSize()
    {
        return borderSize;
    }

    public void setBorderSize(int BORDER_SIZE)
    {
        this.borderSize = BORDER_SIZE;
    }

    public AtomicInteger getOnline()
    {
        return online;
    }

    public Collection<Player> getPlayers()
    {
        return players;
    }

    public GameTask getGameTask()
    {
        return gameTask;
    }

    public void setGameTask(GameTask gameTask)
    {
        this.gameTask = gameTask;
    }

    public PreGameTask getPreGameTask()
    {
        return preGameTask;
    }

    public GameItems getGameItems()
    {
        return gameItems;
    }

    public SpectatorHandler getSpectatorHandler()
    {
        return spectatorHandler;
    }

    public Jedis getJedis()
    {
        return jedis;
    }
}
