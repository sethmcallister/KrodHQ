package xyz.sethy.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.kade.essentials.Essentials;
import xyz.sethy.api.API;
import xyz.sethy.core.commands.*;
import xyz.sethy.core.files.FileHandler;
import xyz.sethy.core.framework.Framework;
import xyz.sethy.core.listeners.AsyncPlayerChatListener;
import xyz.sethy.core.listeners.AsyncPlayerPreLoginListener;
import xyz.sethy.core.listeners.PlayerQuitListner;
import xyz.sethy.core.utils.DateUtils;
import xyz.sethy.factions.Factions;
import xyz.sethy.guard.Guard;
import xyz.sethy.hub.Hub;
import xyz.sethy.sg.SG;
import xyz.sethy.sglobby.SGLobby;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 04/12/2016.
 * A of the code has been produced my Seth M, and therefore all work affiliated and submitted to this code base are subject to UK law.
 * All code and submittions are owned by Seth M, under UK Law, section intellectual property, any and all offences will be prosecuted under UK law and by using any/all of this codebase grants permission to do so.
 * Copying or Redistributing this codebase in either sections or in its entirety will be treated as intellectual property theft, and therefore will be prosecuted for it.
 */
public class Core extends JavaPlugin implements Listener
{
    private Framework framework;
    private FileHandler fileHandler;
    private static Plugin plugin;
    private static Core instance;
    private static String version;
    private double horMultiplier = 1D;
    private double verMultiplier = 1D;
    private double potMultipilerX = 1D;
    private double potMultipilerY = 1D;
    private AtomicBoolean chatMuted;
    private AtomicInteger slowTime;
    private DateUtils dateUtils;

    public static String getVersion()
    {
        return version;
    }

    public static Core getInstance()
    {
        return instance;
    }

    public void onLoad()
    {
        this.fileHandler = new FileHandler(this);
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.SG"))
        {
            SG.load();
        }
    }

    public void onEnable()
    {
        instance = this;
        plugin = this;
        this.chatMuted = new AtomicBoolean(false);
        this.slowTime = new AtomicInteger(0);
        this.framework = new Framework(this);
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        API.setFramework(framework);

//        new KnockbackFix();

        this.setupListeners();
        this.setupCommands();

        this.dateUtils = new DateUtils();

        final Essentials essentials = new Essentials(this);
        new Guard(this);

        if (this.fileHandler.getModulesFile().getBoolean("MODULES.SG_LOBBY"))
        {
            new SGLobby(this);
            getLogger().info("Enabling server as SG Lobby instance.");
        }
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.SG"))
        {
            new SG(this);
            getLogger().info("Enabling server as SG Game instance.");
        }
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.HCF"))
        {
            new Factions(this, false);
            getLogger().info("Enabling server as HCF instance.");
        }
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.HUB"))
        {
            new Hub(this);
            getLogger().info("Enabling server as a Hub instance.");
            essentials.setHub(new AtomicBoolean(true));
        }
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.KITMAP"))
        {
            new Factions(this, true);
            getLogger().info("Enabling server as Kitmap instance.");
        }
    }

    public void onDisable()
    {
        if (this.fileHandler.getModulesFile().getBoolean("MODULES.HCF") || this.fileHandler.getModulesFile().getBoolean("MODULES.KITMAP"))
        {
            Factions.getInstance().onDisable();
        }
    }

    private void setupListeners()
    {
        Bukkit.getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListner(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "SendMessage");
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void setupCommands()
    {
        DeleteUserCommand deleteUser = new DeleteUserCommand();
        getCommand(deleteUser.getCommand()).setExecutor(deleteUser);

        new SetGroupCommand();

        new MutechatCommand();
        new BlacklistCommand();
        new BanCommand();
        new SlowChatCommand();
        new ClearChatCommand();
        new UnbanCommand();
        new MuteCommand();
        new UnmuteCommand();
        new InformationCommand();
        new SetKBCommand();
        new TempBanCommand();
        new SafeShutdownCommand();
        new PingCommand();
        new BroadcastCommand();
        new AlertCommand();
        new WarnCommand();
        new TempMuteCommand();
    }

    public Framework getFramework()
    {
        return framework;
    }

    public static Plugin getPlugin()
    {
        return plugin;
    }

    public double getHorMultiplier()
    {
        return horMultiplier;
    }

    public double getVerMultiplier()
    {
        return verMultiplier;
    }

    public void setHorMultiplier(double horMultiplier)
    {
        this.horMultiplier = horMultiplier;
    }

    public void setVerMultiplier(double verMultiplier)
    {
        this.verMultiplier = verMultiplier;
    }

    public AtomicBoolean getChatMuted()
    {
        return chatMuted;
    }

    public AtomicInteger getSlowTime()
    {
        return slowTime;
    }

    public double getPotMultipilerY()
    {
        return potMultipilerY;
    }

    public void setPotMultipilerY(double potMultipiler)
    {
        this.potMultipilerY = potMultipiler;
    }

    public double getPotMultipilerX()
    {
        return potMultipilerX;
    }

    public void setPotMultipilerX(double potMultipilerX)
    {
        this.potMultipilerX = potMultipilerX;
    }

    public DateUtils getDateUtils()
    {
        return dateUtils;
    }
}