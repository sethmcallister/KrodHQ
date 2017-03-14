package us.kade.essentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.API;

import java.util.LinkedList;

/**
 * Created by Seth on 22/01/2017.
 */
public class StaffChatManager implements Listener
{
    private LinkedList<Player> staffChat;

    public StaffChatManager()
    {
        this.staffChat = new LinkedList<>();
        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event)
    {
        if (staffChat.contains(event.getPlayer()))
        {
            MessageUtil.sendStaffMessage("&7[&bSC&7] " + event.getPlayer().getName() + ": &b" + event.getMessage());
            event.setCancelled(true);
        }
    }

    public LinkedList<Player> getStaffModes()
    {
        return staffChat;
    }
}
