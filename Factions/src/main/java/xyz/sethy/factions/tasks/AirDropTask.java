package xyz.sethy.factions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.factions.airdrop.Airdrop;

/**
 * Created by Seth on 23/03/2017.
 */
public class AirDropTask extends BukkitRunnable
{
    private Airdrop nextAirdrop;
    private Airdrop prevAirdrop;
    private int i = 7;

    @Override
    public void run()
    {
        if(i != 0)
        {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7An air-drop will spawn in &3" + (7 - i) + "&7 minutes."));
            if(i == 2)
            {
                Location bombLoc = prevAirdrop.getLocation();
                bombLoc.getWorld().spawn(bombLoc.add(3, 30, 3), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(2, 30, 2), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(1, 30, 1), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(0, 30, 0), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(3, 30, 3), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(2, 30, 2), TNTPrimed.class);
                bombLoc.getWorld().spawn(bombLoc.add(1, 30, 1), TNTPrimed.class);
            }
            i--;
        }
    }
}
