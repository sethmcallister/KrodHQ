package xyz.sethy.factions.listeners;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.timers.TimerType;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by sethm on 07/12/2016.
 */
public class PlayerDeathEventListener implements Listener
{

    public PlayerDeathEventListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e)
    {
        if (e.getEntity().getKiller() != null)
        {
            Player player = e.getEntity().getKiller();
            if (player.getItemInHand() != null)
            {
                ItemStack stack = player.getItemInHand();
                if (stack.getEnchantments().containsKey(Enchantment.LOOT_BONUS_MOBS))
                {
                    int level = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                    if (level == 1)
                        e.setDroppedExp(e.getDroppedExp() * 3);
                    else if (level == 2)
                        e.setDroppedExp(e.getDroppedExp() * 6);
                    else if (level == 3)
                        e.setDroppedExp(e.getDroppedExp() * 10);
                    else
                        e.setDroppedExp(level * 2 * e.getDroppedExp());
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player killer = event.getEntity().getKiller();
        Player killed = event.getEntity();

        String itemName;
        if (killer.getItemInHand().getType() == Material.AIR)
        {
            itemName = "their fists";
        }
        else
        {
            itemName = getItemName(killer.getItemInHand());
        }

        if (Factions.getInstance().isKitmap())
        {
            KitmapUser fKiller = API.getUserManager().findKitmapByUniqueId(killer.getUniqueId());
            KitmapUser fKilled = API.getUserManager().findKitmapByUniqueId(killed.getUniqueId());
            fKiller.setKills(fKiller.getKills() + 1);
            fKilled.setDeaths(fKilled.getDeaths() + 1);
            fKiller.setBalance(fKiller.getBalance() + 75);

            Bukkit.getServer().getWorld(killed.getWorld().getUID()).strikeLightningEffect(killed.getLocation());
            killed.teleport(new Location(Bukkit.getWorld("world"), 0.5, 72, 0.5));

            if (Factions.getInstance().getTimerHandler().hasTimer(killed, TimerType.COMBAT_TAG))
            {
                Factions.getInstance().getTimerHandler().getPlayerTimers(killed).remove(Factions.getInstance().getTimerHandler().getTimer(killed, TimerType.COMBAT_TAG));
            }
            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(killed);
            if (faction != null)
            {
                double oldDTR = faction.getDTR();
                faction.setDtr(oldDTR - 0.25);
                if (faction.isRaidable())
                {
                    faction.setRaidableCooldown(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(9L));
                }
                Factions.getInstance().getDtrHandler().setCooldown(faction);
                faction.setDeathCooldown(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4L));
                for (UUID uuid : faction.getOnlineMembers())
                {
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member death: &4" + killed.getName()));
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR change: &4" + oldDTR + " &3-> &4" + faction.getDTR()));
                }
            }

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + killed.getName() + "&4[" + fKilled.getKills() + "]&7 was slain by &c" + killer.getName() + "&4[" + fKiller.getKills() + "]&7 using &c" + itemName + "&7."));
        }
        else
        {
            HCFUser fKilled = API.getUserManager().findHCFByUniqueId(killed.getUniqueId());
            HCFUser fKiller = API.getUserManager().findByUniqueId(killer.getUniqueId()).getHCFUser();
            fKiller.setKills(fKiller.getKills() + 1);
            fKilled.setDeaths(fKilled.getDeaths() + 1);

            Faction faction = Factions.getInstance().getFactionManager().findByPlayer(killed);
            if (faction != null)
            {
                double oldDTR = faction.getDTR();
                faction.setDtr(oldDTR - 1.0);
                if (faction.isRaidable())
                {
                    faction.setRaidableCooldown(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(90L));
                }
                Factions.getInstance().getDtrHandler().setCooldown(faction);
                faction.setDeathCooldown(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60L));
                for (UUID uuid : faction.getOnlineMembers())
                {
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member death: &4" + killed.getName()));
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR change: &4" + oldDTR + " &3-> &4" + faction.getDTR()));
                }
            }

            long deathbanTime = Factions.getInstance().getDeathbanManager().getDeathbanTime(API.getUserManager().findByUniqueId(fKilled.getUUID()));

            fKilled.setDeathbanTime(deathbanTime + System.currentTimeMillis());
            String deathMessage = ChatColor.translateAlternateColorCodes('&', "&c" + killed.getName() + "&4[" + fKilled.getKills() + "]&7 was slain by &c" + killer.getName() + "&4[" + fKiller.getKills() + "]&7 using &c" + itemName + "&7.");
            fKilled.setDeathbanMessage(deathMessage);
            killed.kickPlayer(ChatColor.translateAlternateColorCodes('&', deathMessage + "\n" + "&cYour deathban expires in &7" + formatTime(deathbanTime) + "&c." + "\n" + "&cThank you for playing &3KrodHQ.com&c."));
            Bukkit.broadcastMessage(deathMessage);
        }
        event.setDeathMessage(null);
    }

    private static String getItemName(final ItemStack i)
    {
        if (i.getItemMeta().hasDisplayName())
            return ChatColor.stripColor(i.getItemMeta().getDisplayName());

        return WordUtils.capitalizeFully(i.getType().name().replace('_', ' '));
    }

    private final DecimalFormat FORMAT = new DecimalFormat("0.0");

    private String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private String formatTime(long time)
    {
        if (time > 60000L)
        {
            return setLongFormat(time);
        }
        else
        {
            return format(time);
        }
    }

    private String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
        {
            return FORMAT.format(paramMilliseconds);
        }
        return DurationFormatUtils.formatDuration(paramMilliseconds, (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }
}
