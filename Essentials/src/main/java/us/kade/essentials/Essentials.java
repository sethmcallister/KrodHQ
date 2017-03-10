package us.kade.essentials;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.kade.essentials.command.*;
import us.kade.essentials.listeners.JoinListener;
import us.kade.essentials.listeners.PlayerInteractListener;
import us.kade.essentials.listeners.QuitListener;
import us.kade.essentials.managers.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Sethm on 19/08/2016.
 */
public class Essentials
{
    private static Essentials instance;
    private Plugin plugin;
    public Integer onlineStaff = 0;
    private StaffModeManager staffModeManager;
    private FreezeManager freezeManager;
    private AtomicBoolean hub;
    private StaffChatManager staffChatManager;

    public Essentials(Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        setupListeners();
        setupCommands();
        new ConversationManager();
        this.freezeManager = new FreezeManager();
        this.staffModeManager = new StaffModeManager();
        this.staffChatManager = new StaffChatManager();
        this.hub = new AtomicBoolean(false);
    }

    private void setupCommands()
    {
        new ListCommand();
        new MessageCommand();
        new FreezeCommand();
        new GamemodeCommand();
        new StaffModeCommand();
        new TeleportCommand();
        new StaffChatCommand();
        new VanishCommand();
        new SoundsCommand();
        new RequestCommand();

        plugin.getServer().getPluginCommand("message").setExecutor(new MessageCommand());
        plugin.getServer().getPluginCommand("reply").setExecutor(new ReplyCommand());
    }

    private void setupListeners()
    {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BuildManager(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), plugin);

    }

    public static Essentials getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public void setOnlineStaff(Integer onlineStaff)
    {
        this.onlineStaff = onlineStaff;
    }

    public Integer getOnlineStaff()
    {
        return onlineStaff;
    }

    public StaffModeManager getStaffModeManager()
    {
        return staffModeManager;
    }

    public FreezeManager getFreezeManager()
    {
        return freezeManager;
    }

    public AtomicBoolean getHub()
    {
        return hub;
    }

    public void setHub(AtomicBoolean hub)
    {
        this.hub = hub;
    }

    public StaffChatManager getStaffChatManager()
    {
        return staffChatManager;
    }
}
