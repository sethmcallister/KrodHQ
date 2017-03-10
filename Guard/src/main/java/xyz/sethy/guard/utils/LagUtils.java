package xyz.sethy.guard.utils;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import xyz.sethy.guard.Guard;

/**
 * Created by sethm on 16/11/2016.
 */
public class LagUtils implements Listener
{
    public Plugin plugin;
    private double tps;

    public LagUtils(Plugin plugin)
    {
        this.plugin = plugin;

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin,
                new Runnable()
                {
                    long sec;
                    long currentSec;
                    int ticks;

                    public void run()
                    {
                        this.sec = (System.currentTimeMillis() / 1000L);
                        if (this.currentSec == this.sec)
                        {
                            this.ticks += 1;
                        }
                        else
                        {
                            this.currentSec = this.sec;
                            tps = (LagUtils.this.tps == 0.0D ? this.ticks : (tps + this.ticks) / 2.0D);
                            this.ticks = 0;
                        }
                    }
                }, 0L, 1L);

        Guard.getInstance().RegisterListener(this);
    }

    public double getTPS()
    {
        return this.tps + 1.0D > 20.0D ? 20.0D : this.tps + 1.0D;
    }

    public int getPing(Player player)
    {
        CraftPlayer cp = (CraftPlayer) player;
        EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }
}
