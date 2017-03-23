package xyz.sethy.factions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.sethy.factions.combatlog.CombatLoggerManager;
import xyz.sethy.factions.commands.essentials.core.*;
import xyz.sethy.factions.commands.essentials.core.admin.*;
import xyz.sethy.factions.commands.essentials.koth.KothCommand;
import xyz.sethy.factions.commands.faction.*;
import xyz.sethy.factions.commands.faction.staff.*;
import xyz.sethy.factions.dto.claim.LandBoard;
import xyz.sethy.factions.handlers.InventoryManager;
import xyz.sethy.factions.handlers.ItemHandler;
import xyz.sethy.factions.handlers.KothHandler;
import xyz.sethy.factions.handlers.ServerHandler;
import xyz.sethy.factions.handlers.classes.ClassHandler;
import xyz.sethy.factions.handlers.commands.CommandHandler;
import xyz.sethy.factions.handlers.dtr.DTRHandler;
import xyz.sethy.factions.koth.listeners.KothCreateListener;
import xyz.sethy.factions.listeners.*;
import xyz.sethy.factions.listeners.crate.CrateKeyPlaceListener;
import xyz.sethy.factions.listeners.crate.ElaphKeyListener;
import xyz.sethy.factions.listeners.crate.KrodKeyListener;
import xyz.sethy.factions.listeners.crate.StarterKeyListener;
import xyz.sethy.factions.managers.CrateManager;
import xyz.sethy.factions.managers.DeathbanManager;
import xyz.sethy.factions.managers.EnchantmentManager;
import xyz.sethy.factions.managers.FactionManager;
import xyz.sethy.factions.scoreboard.ScoreHandler;
import xyz.sethy.factions.scoreboard.ScorebordTicker;
import xyz.sethy.factions.support.RegionSupport;
import xyz.sethy.factions.support.wg.WorldGuardRegionSupport;
import xyz.sethy.factions.tasks.BroadcastMessageTask;
import xyz.sethy.factions.tasks.KrodBroadcastTask;
import xyz.sethy.factions.tasks.TabUpdaterTask;
import xyz.sethy.factions.timers.TimerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sethm on 26/11/2016.
 */
public class Factions
{
    private static Factions instance;
    private Plugin plugin;
    private FactionManager factionManager;
    private LandBoard landBoard;
    private ItemHandler itemHandler;
    private ScoreHandler scoreHandler;
    private TimerHandler timerHandler;
    private ServerHandler serverHandler;
    private DTRHandler dtrHandler;
    private ClassHandler classHandler;
    private EnchantmentManager enchantmentManager;
    private GlassWallListener glassWallListener;
    private KothHandler kothHandler;
    private CombatLoggerManager combatLoggerManager;
    private CrateManager crateManager;
    private DeathbanManager deathbanManager;
    private final int map = 1;
    private boolean kitmap;
    private List<RegionSupport> regionSupports;
    private InventoryManager inventoryManager;
    private Integer maxFactionSize;

    public Factions(Plugin plugin, boolean kitmap)
    {
        this.kitmap = kitmap;
        instance = this;
        this.plugin = plugin;
        this.landBoard = new LandBoard();
        this.itemHandler = new ItemHandler();
        this.factionManager = new FactionManager();
        this.scoreHandler = new ScoreHandler();
        this.timerHandler = new TimerHandler();
        this.serverHandler = new ServerHandler();
        this.dtrHandler = new DTRHandler();
        this.classHandler = new ClassHandler();
        new GlassWallListener();
        this.kothHandler = new KothHandler();
        this.loadCommands();
        this.setupListeners();
        this.enchantmentManager = new EnchantmentManager();
        this.deathbanManager = new DeathbanManager();
        this.crateManager = new CrateManager();
        this.inventoryManager = new InventoryManager();
        this.combatLoggerManager = new CombatLoggerManager();

        this.regionSupports = new ArrayList<>();
        this.regionSupports.add(new WorldGuardRegionSupport());
        this.maxFactionSize = 10;

        new ClaimListener();
        new BroadcastMessageTask();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new ScorebordTicker(), 1L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TabUpdaterTask(), 5L, 5L);
//        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new GarbageCollection(), 50 * 20L, 50 * 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new KrodBroadcastTask(), 50 * 20L, 50 * 20L);
    }

    public static Factions getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public FactionManager getFactionManager()
    {
        return factionManager;
    }

    public void onDisable()
    {
        this.factionManager.saveFactions();
    }

    private void setupListeners()
    {
        new PlayerDeathEventListener();
        new EnderpearlListener();
        new RoadListener();
        new SpawnListener();
        new FoundDiamondsListener();
        new ASyncChatEventListener();
        new CombatListener();
        new PlayerInteractListener();
        new KitSignListener();
        new KothCreateListener();
        new TntMinecraftFixListener();
        new EnchantmentDisallowListener();
        new CobblestoneWallGenerationListener();
        new KrodKeyListener();
        new ElaphKeyListener();
        new CrateKeyPlaceListener();
        new StarterKeyListener();
        new FastTileListener();
        new WorldSwitchListener();
        new BuySignListener();
        new WeatherListener();
        new UserLogoutListener();
        new UserLoggedInListener();

        Bukkit.getServer().getPluginManager().registerEvents(new FactionHomeCommand(), plugin);
    }


    private void loadCommands()
    {
        CommandHandler handler = new CommandHandler();
        handler.register("help", new FactionBaseCommand());
        handler.register("create", new FactionCreateCommand());
        handler.register("claim", new FactionClaimCommand());
        handler.register("disband", new FactionDisbandCommand());
        handler.register("invite", new FactionInviteCommand());
        handler.register("show", new FactionShowCommand());
        handler.register("who", new FactionShowCommand());
        handler.register("join", new FactionJoinCommand());
        handler.register("help", new FactionBaseCommand());
        handler.register("list", new FactionListCommand());
        handler.register("claim", new FactionClaimCommand());
        handler.register("map", new FactionMapCommand());
        handler.register("home", new FactionHomeCommand());
        handler.register("sethome", new FactionSetHomeCommand());
        handler.register("unclaimall", new FactionUnclaimCommand());
        handler.register("unclaim", new FactionUnclaimCommand());
        handler.register("deposit", new FactionDepositCommand());
        handler.register("d", new FactionDepositCommand());
        handler.register("leave", new FactionLeaveCommand());
        handler.register("chat", new FactionChatCommand());
        handler.register("c", new FactionChatCommand());
        handler.register("kick", new FactionKickCommand());
        handler.register("stuck", new FactionStuckCommand());
        handler.register("widthdraw", new FactionWidthdrawCommand());
        handler.register("w", new FactionWidthdrawCommand());
        handler.register("leader", new FactionLeaderCommand());
        handler.register("promote", new FactionPromoteCommand());

        handler.register("forceleave", new FactionForceLeaveCommand());
        handler.register("type", new FactionSetDTRBitmaskCommand());
        handler.register("forceclaim", new FactionForceClaimCommand());
        handler.register("forcedisband", new FactionForceDisbandCommand());
        handler.register("createkoth", new FactionCreateKoTHCommand());
        handler.register("forcejoin", new FactionForceJoinCommand());
        handler.register("forceleader", new FactionForceLeaderCommand());
        handler.register("setdtr", new FactionSetDTRCommand());

        Bukkit.getPluginCommand("faction").setExecutor(handler);

        new SOTWCommand();
        new KothHandler();
        new HelpCommand();
        new CoordsCommand();
        new PayCommand();
        new PvPCommand();
        new KothCommand();
        new BalanceCommand();
        new SetBalanceCommand();
        new RefundInventoryCommand();
        new AddLivesCommand();
        new CrateCommand();
        new RedeemCommand();
        new GiveAllMoneyCommand();
        new SpawnCommand();
        new LogoutCommand();
    }

    public LandBoard getLandBoard()
    {
        return landBoard;
    }

    public ItemHandler getItemHandler()
    {
        return itemHandler;
    }

    public ScoreHandler getScoreHandler()
    {
        return scoreHandler;
    }

    public TimerHandler getTimerHandler()
    {
        return timerHandler;
    }

    public ServerHandler getServerHandler()
    {
        return serverHandler;
    }

    public int getMap()
    {
        return map;
    }

    public GlassWallListener getGlassWallListener()
    {
        return glassWallListener;
    }

    public KothHandler getKothHandler()
    {
        return kothHandler;
    }

    public boolean isKitmap()
    {
        return kitmap;
    }

    public List<RegionSupport> getRegionSupports()
    {
        return regionSupports;
    }

    public DTRHandler getDtrHandler()
    {
        return dtrHandler;
    }

    public ClassHandler getClassHandler()
    {
        return classHandler;
    }

    public EnchantmentManager getEnchantmentManager()
    {
        return enchantmentManager;
    }

    public DeathbanManager getDeathbanManager()
    {
        return deathbanManager;
    }

    public CrateManager getCrateManager()
    {
        return crateManager;
    }

    public InventoryManager getInventoryManager()
    {
        return inventoryManager;
    }

    public Integer getMaxFactionSize()
    {
        return maxFactionSize;
    }

    public CombatLoggerManager getCombatLoggerManager()
    {
        return combatLoggerManager;
    }
}
