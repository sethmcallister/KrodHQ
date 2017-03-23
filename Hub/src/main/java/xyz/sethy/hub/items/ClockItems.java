package xyz.sethy.hub.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.hub.Hub;
import xyz.sethy.hub.server.Server;

import java.util.ArrayList;


/**
 * Created by sethm on 23/12/2016.
 */
public class ClockItems
{
    private ItemStack hcf;
    private ItemStack sgLobby;
    private ItemStack practice;
    private ItemStack kitmap;
    private ItemStack soup;

    public ClockItems()
    {
        this.hcf = new ItemStack(Material.FISHING_ROD, 1, (byte) 16421);
        ItemMeta hcf = this.hcf.getItemMeta();
        hcf.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Click to join our &7HCF&3 server."));
        this.hcf.setItemMeta(hcf);

        this.practice = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta practice = this.practice.getItemMeta();
        practice.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Click to browse our &7Practice&3 servers."));
        this.practice.setItemMeta(practice);

        this.sgLobby = new ItemStack(346, 1);
        ItemMeta h = this.sgLobby.getItemMeta();
        h.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Click to queue for &7The Survival Games Lobby&5."));
        this.sgLobby.setItemMeta(h);

        this.kitmap = new ItemStack(373, 1, (byte) 37);
        ItemMeta k = this.kitmap.getItemMeta();
        k.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Click to join our &7Kits&3 server."));
        this.kitmap.setItemMeta(k);

        this.soup = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta soup = this.soup.getItemMeta();
        soup.setDisplayName("&3Click to browse our &7Soup&3 Servers.");
        this.soup.setItemMeta(soup);
    }


    public ItemStack getKitmap(Player player)
    {
        ItemStack itemStack = kitmap.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7§m-----------------------");
        lore.add("§310§7 man factions.");
        lore.add("§3No§7 allies.");
        lore.add("§7Protection 1, Sharpness 2.");
        lore.add("§7§m-----------------------");
        KitmapUser kitmapUser = API.getUserManager().getTempKitsUser(player.getUniqueId());
        lore.add("§3Your Stats§7:");
        if(kitmapUser == null)
        {
            lore.add(" §3Kills§7:§f " + 0);
            lore.add(" §3Deaths§7:§f " + 0);
            lore.add(" §3Current Kill Streak§7:§f " + 0);
        }
        else
        {
            lore.add(" §3Kills§7:§f " + kitmapUser.getKills());
            lore.add(" §3Deaths§7:§f " + kitmapUser.getDeaths());
            lore.add(" §3Current Kill Streak§7:§f " + kitmapUser.getCurrentKillStreak());
        }

        lore.add("§7§m-----------------------");
        lore.add("§3" + Hub.getInstance().getPlayerQueue().getQueueSize(Server.KITMAP) + "§7 players are currently queueing.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getHcf(Player player)
    {
        ItemStack itemStack = hcf.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7§m-----------------------");
        lore.add("§310§7 man factions.");
        lore.add("§3No§7 allies.");
        lore.add("§7Protection 1, Sharpness 2.");
        lore.add("§7§m-----------------------");
        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(player.getUniqueId());
        lore.add("§3Your Stats§7:");
        if(hcfUser == null)
        {
            lore.add(" §3Kills§7:§f " + 0);
            lore.add(" §3Deaths§7:§f " + 0);
            lore.add("");
        }
        else
        {
            if (hcfUser.deathbanTime() > System.currentTimeMillis())
            {
                lore.add("§cYou are currently death-banned.");
                lore.add("§cYou can rejoin the server by using one of your §6" + hcfUser.getLives() + "§c lives.");
            }
            lore.add("§cKills§7:§f " + hcfUser.getKills());
            lore.add("§cDeaths§7:§f " + hcfUser.getDeaths());
        }
        lore.add("§7§m-----------------------");
        lore.add("§3" + Hub.getInstance().getPlayerQueue().getQueueSize(Server.HCF) + "§7 players are currently queueing.");
        lore.add("§7§m-----------------------");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getPractice()
    {
        ItemStack itemStack = practice.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§cStats Coming Soon.");
        lore.add("");
        lore.add("§e" + Hub.getInstance().getPlayerQueue().getQueueSize(Server.PRACTICE) + "§5 players are currently queueing.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getSgLobby(Player player)
    {
        SGUser sgUser = API.getUserManager().findByUniqueId(player.getUniqueId()).getSGUser();
        ItemStack itemStack = sgLobby.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§5Wins§7: §f" + sgUser.getWins());
        lore.add("§5Loses§7: §f" + sgUser.getLoses());
        lore.add("");
        lore.add("§e" + Hub.getInstance().getPlayerQueue().getQueueSize(Server.SG) + "§5 players are currently queueing.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
