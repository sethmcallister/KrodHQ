package us.kade.essentials.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import us.kade.essentials.Essentials;
import us.kade.essentials.util.TPSUtil;

/**
 * Created by sethm on 06/11/2016.
 */
public class LagCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        long before = System.currentTimeMillis();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Current TPS: " + (TPSUtil.getTPS() > 17 ? "&a" + TPSUtil.getTPS() : "&c" + TPSUtil.getTPS())));
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                long after = System.currentTimeMillis();
                long time = after - before;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Full Server Tick (MS): " + (time > 50 ? "&c" + time : "&a" + time)));
            }
        }.runTaskLater(Essentials.getInstance().getPlugin(), 1L);
//        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Current Players: &b" + Bukkit.getOnlinePlayers().size() + "&3/&b" + Bukkit.getMaxPlayers()));
        return true;
    }
}
