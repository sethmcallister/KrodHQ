package xyz.sethy.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.mute.Mute;
import xyz.sethy.api.framework.mute.MuteType;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.core.Core;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 23/12/2016.
 */
public class AsyncPlayerChatListener implements Listener
{
    private final ConcurrentHashMap<Player, Long> cooldowns = new ConcurrentHashMap<>();

    @EventHandler
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event)
    {
        if ((event.getMessage().toLowerCase().startsWith("/me")) || (event.getMessage().toLowerCase().startsWith("/bukkit:me")) || (event.getMessage().toLowerCase().startsWith("/bukkit:me")) || (event.getMessage().toLowerCase().startsWith("/kill") || event.getMessage().toLowerCase().startsWith("/slay") || event.getMessage().toLowerCase().startsWith("/bukkit:kill") || event.getMessage().toLowerCase().startsWith("/bukkit:slay") || event.getMessage().toLowerCase().startsWith("/suicide")) && !event.getPlayer().isOp())
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event)
    {
        Mute mute = API.getMuteManager().getMute(event.getPlayer().getUniqueId());
        if (mute != null)
        {
            if (mute.getType().equals(MuteType.NORMAL_PERMANENT))
            {
                final Player player = event.getPlayer();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been muted, this mute never expires!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You where muted for the reason: &3" + mute.getReason() + "&7."));
                event.setCancelled(true);
                return;
            }
            if (mute.getType().equals(MuteType.NORMAL_TEMPORARILY))
            {
                Long expire = mute.getExpireDate();
                Date now = new Date();
                if (expire < System.currentTimeMillis())
                {
                    final Player player = event.getPlayer();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have been muted. "));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You where muted for the reason: &3" + mute.getReason() + "&7."));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if(Core.getInstance().getChatMuted().get())
        {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot chat while chat is muted."));
            event.setCancelled(true);
            return;
        }

        if (this.cooldowns.containsKey(event.getPlayer()) && this.cooldowns.get(event.getPlayer()) > System.currentTimeMillis())
        {
            long millisLeft = this.cooldowns.get(event.getPlayer()) - System.currentTimeMillis();
            double value = millisLeft / 1000.0D;
            double sec = Math.round(10.0D * value) / 10.0D;
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot chat for another &3" + sec + "&7 seconds."));
            event.setCancelled(true);
            return;
        }

        User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
        event.setFormat(ChatColor.translateAlternateColorCodes('&', user.getGroup().getName() + event.getPlayer().getName() + "&r: ") + event.getMessage());
        Long time = Long.valueOf(Core.getInstance().getSlowTime().get() + "000") + System.currentTimeMillis();
        cooldowns.put(event.getPlayer(), time);
    }

    @EventHandler
    public void onPlayerClickTab(PlayerChatTabCompleteEvent event)
    {
        event.getTabCompletions().clear();
        event.getChatMessage().charAt(0);
    }
}
