package xyz.sethy.factions.koth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.koth.dto.CoreKoth;

/**
 * Created by sethm on 26/12/2016.
 */
public class KothCreateListener implements Listener
{

    public KothCreateListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
        {
            if (player.getItemInHand() == null)
                return;

            if (!player.getItemInHand().hasItemMeta())
                return;

            if (player.getItemInHand().getItemMeta().getDisplayName() != null && !player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3Create a KoTH")))
                return;

            CoreKoth coreKoth = (CoreKoth) Factions.getInstance().getKothHandler().getCreationKoth(player);
            if (coreKoth == null)
                return;

            Location location = event.getClickedBlock().getLocation();

            coreKoth.getKoTHLocations().add(location);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have set KoTH location 1."));
            if (coreKoth.getKoTHLocations().size() == 2)
            {
                Factions.getInstance().getKothHandler().getCreatingKoth().remove(coreKoth);
                Factions.getInstance().getKothHandler().getKoths().add(coreKoth);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have successfully created the KoTH &7" + coreKoth.getName().get() + "&3."));
            }
            event.setCancelled(true);
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if (player.getItemInHand() == null)
                return;

            if (!player.getItemInHand().hasItemMeta())
                return;

            if (player.getItemInHand().getItemMeta().getDisplayName() != null && !player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3Create a KoTH")))
                return;

            CoreKoth coreKoth = (CoreKoth) Factions.getInstance().getKothHandler().getCreationKoth(player);
            if (coreKoth == null)
                return;

            Location location = event.getClickedBlock().getLocation();

            coreKoth.getKoTHLocations().add(location);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have set KoTH location 2."));
            if (coreKoth.getKoTHLocations().size() == 2)
            {
                Factions.getInstance().getKothHandler().getCreatingKoth().remove(coreKoth);
                Factions.getInstance().getKothHandler().getKoths().add(coreKoth);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You have successfully created the KoTH &7" + coreKoth.getName().get() + "&3."));
            }
            event.setCancelled(true);
        }
    }
}
