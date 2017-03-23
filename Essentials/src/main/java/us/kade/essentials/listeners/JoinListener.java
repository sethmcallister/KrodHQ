package us.kade.essentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.kade.essentials.Essentials;
import us.kade.essentials.inventories.StaffmodeInventory;
import us.kade.essentials.util.MessageUtil;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 19/08/2016.
 */
public class JoinListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        event.setJoinMessage(null);
        if (user.getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
        {

            if (Essentials.getInstance().getHub().get())
            {
                return;
            }

            event.getPlayer().getInventory().clear();
            StaffmodeInventory.givePlayerStaffItems(event.getPlayer());
            event.getPlayer().updateInventory();
            for (Player player : Bukkit.getOnlinePlayers())
            {
                User user1 = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user1.getGroup().getPermission() <= Group.TRAIL_MOD.getPermission())
                {
                    if (player.canSee(event.getPlayer()))
                        player.hidePlayer(event.getPlayer());
                }
            }

            Essentials.getInstance().getStaffModeManager().getStaffModes().add(event.getPlayer());
            user.setStaffMode(true);
            user.setVansihed(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Staff Mode: &aEnabled"));
            MessageUtil.sendStaffMessage(event.getPlayer(), "&b[&9STAFF&b] &c" + event.getPlayer().getName() + "&7 has joined the server.");
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            Essentials.getInstance().setOnlineStaff(Essentials.getInstance().getOnlineStaff() + 1);
            StaffmodeInventory.uploadOnline();
        }

        if(user.isStaffMode() && user.getGroup().getPermission() <= Group.TRAIL_MOD.getPermission())
        {
            user.setVansihed(false);
            user.setStaffMode(false);
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!player.canSee(event.getPlayer()))
                    player.showPlayer(event.getPlayer());
            }
        }
    }
}
