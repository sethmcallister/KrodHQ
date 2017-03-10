package us.kade.essentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.kade.essentials.Essentials;
import us.kade.essentials.inventories.StaffmodeInventory;
import xyz.sethy.api.events.UserLoggedOutEvent;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by sethm on 19/08/2016.
 */
public class QuitListener implements Listener
{
    @EventHandler
    public void onQuit(UserLoggedOutEvent event)
    {
        if (event.getUser().getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
        {
            StaffmodeInventory.uploadOnline();
            Essentials.getInstance().setOnlineStaff(Essentials.getInstance().getOnlineStaff() - 1);
        }
    }
}