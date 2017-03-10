package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.events.UserLoggedInEvent;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.managers.CrateManager;
import xyz.sethy.factions.timers.DefaultTimer;
import xyz.sethy.factions.timers.TimerType;

/**
 * Created by sethm on 30/12/2016.
 */
public class UserLoggedInListener implements Listener
{
    public UserLoggedInListener()
    {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onUserLogIn(UserLoggedInEvent event)
    {
        if(event.getPlayer() == null)
        {
            return;
        }

        if(!event.getPlayer().isOnline())
        {
            return;
        }

        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(event.getPlayer().getUniqueId());
        if (hcfUser.deathbanTime() > System.currentTimeMillis())
        {
            event.getPlayer().kickPlayer(hcfUser.getDeathbanMessage() + "\n" + "&cYour deathban expires in &7" + getConvertedTime(hcfUser.deathbanTime()) + "&c." + "\n" + "&cThank you for playing &3KrodHq.com&c.");
        }

        if (event.getUser().getHCFUser().getPvPTimer() > System.currentTimeMillis())
        {
            DefaultTimer defaultTimer = new DefaultTimer(TimerType.PVP_TIMER, event.getUser().getHCFUser().getPvPTimer(), event.getPlayer());
            Factions.getInstance().getTimerHandler().addTimer(event.getPlayer(), defaultTimer);

            if (defaultTimer.isFrozen())
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your PvP Timer is currently frozen, leave &aSpawn&7 to resume."));
        }

        if(!event.getPlayer().hasPlayedBefore())
        {
            DefaultTimer timer = new DefaultTimer(TimerType.PVP_TIMER, 1800000L + System.currentTimeMillis(), event.getPlayer());
            Factions.getInstance().getTimerHandler().addTimer(event.getPlayer(), timer);
            timer.freeze();
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your PvP Timer is currently frozen, leave &aSpawn&7 to resume."));

            ItemStack starterKey = CrateManager.getStarterKey().clone();
            starterKey.setAmount(3);

            hcfUser.setBalance(250);
            hcfUser.setKills(0);
            hcfUser.setDeaths(0);
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().addItem(new ItemStack(Material.FISHING_ROD, 3));
            event.getPlayer().getInventory().addItem(starterKey);
            if(event.getUser().getGroup().equals(Group.ANT))
            {
                hcfUser.setLives(hcfUser.getLives() + 2);

                ItemStack elaphKey = CrateManager.getStarterKey().clone();
                elaphKey.setAmount(2);

                event.getPlayer().getInventory().addItem(elaphKey);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &6Ant&7 rank has been automatically redeemed."));
            }
            else if(event.getUser().getGroup().equals(Group.ELAPH))
            {
                hcfUser.setLives(hcfUser.getLives() + 5);

                ItemStack elaphKey = CrateManager.getElaph().clone();
                elaphKey.setAmount(3);
                event.getPlayer().getInventory().addItem(elaphKey);

                ItemStack krodKey = CrateManager.getKrodKey().clone();
                krodKey.setAmount(1);
                event.getPlayer().getInventory().addItem(krodKey);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &7Elaph&7 rank has been automatically redeemed."));
            }
            else if(event.getUser().getGroup().equals(Group.KROD))
            {
                hcfUser.setLives(hcfUser.getLives() + 20);

                ItemStack elaphKey = CrateManager.getElaph().clone();
                elaphKey.setAmount(5);
                event.getPlayer().getInventory().addItem(elaphKey);

                ItemStack krodKey = CrateManager.getKrodKey().clone();
                krodKey.setAmount(3);
                event.getPlayer().getInventory().addItem(krodKey);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Krod&7 rank has been automatically redeemed."));
            }
            else if(event.getUser().getGroup().equals(Group.YOUTUBE))
            {
                hcfUser.setLives(hcfUser.getLives() + 15);

                ItemStack elaphKey = CrateManager.getElaph().clone();
                elaphKey.setAmount(5);
                event.getPlayer().getInventory().addItem(elaphKey);

                ItemStack krodKey = CrateManager.getKrodKey().clone();
                krodKey.setAmount(3);
                event.getPlayer().getInventory().addItem(krodKey);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Famous&7 rank has been redeemed."));
            }
            else if(event.getUser().getGroup().equals(Group.FAMOUS))
            {
                hcfUser.setLives(hcfUser.getLives() + 30);

                ItemStack elaphKey = CrateManager.getElaph().clone();
                elaphKey.setAmount(3);
                event.getPlayer().getInventory().addItem(elaphKey);

                ItemStack krodKey = CrateManager.getKrodKey().clone();
                krodKey.setAmount(6);
                event.getPlayer().getInventory().addItem(krodKey);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Famous&7 rank has been redeemed."));
            }
            hcfUser.setRedeemedRank(true);
            event.getPlayer().updateInventory();
        }
    }

    private String getConvertedTime(long i)
    {
        i = Math.abs(i);
        final int hours = (int) Math.floor(i / 3600L);
        final int remainder = (int) (i % 3600L);
        final int minutes = remainder / 60;
        final int seconds = remainder % 60;
        if (seconds == 0 && minutes == 0)
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + "0 seconds";

        if (minutes == 0)
        {
            if (seconds == 1)
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);

            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
        }
        else if (seconds == 0)
        {
            if (minutes == 1)
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);

            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
        }
        else if (seconds == 1)
        {
            if (minutes == 1)
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);

            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
        }
        else
        {
            if (minutes == 1)
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            final String toReturn = String.format("%sm %ss", minutes, seconds);
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + " " + toReturn;
        }
    }
}
