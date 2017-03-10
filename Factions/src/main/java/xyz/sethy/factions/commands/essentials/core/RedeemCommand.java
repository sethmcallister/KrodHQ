package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.managers.CrateManager;

/**
 * Created by Seth on 10/03/2017.
 */
public class RedeemCommand extends CommandBase
{
    public RedeemCommand()
    {
        super("redeem", Group.DEFAULT, true);
        Bukkit.getPluginCommand("redeem").setExecutor(this);
    }
    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] paramArrayOfString)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(sender.getUniqueId());
        if(hcfUser.hasRedeemedRank())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have already redeemed your rank this map."));
            return;
        }

        if(user.getGroup().equals(Group.ANT))
        {
            hcfUser.setLives(hcfUser.getLives() + 2);

            ItemStack elaphKey = CrateManager.getStarterKey().clone();
            elaphKey.setAmount(2);

            sender.getInventory().addItem(elaphKey);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &6Ant&7 rank has been redeemed."));
        }
        else if(user.getGroup().equals(Group.ELAPH))
        {
            hcfUser.setLives(hcfUser.getLives() + 5);

            ItemStack elaphKey = CrateManager.getElaph().clone();
            elaphKey.setAmount(3);
            sender.getInventory().addItem(elaphKey);

            ItemStack krodKey = CrateManager.getKrodKey().clone();
            krodKey.setAmount(1);
            sender.getInventory().addItem(krodKey);

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &7Elaph&7 rank has been redeemed."));
        }
        else if(user.getGroup().equals(Group.KROD))
        {
            hcfUser.setLives(hcfUser.getLives() + 20);

            ItemStack elaphKey = CrateManager.getElaph().clone();
            elaphKey.setAmount(5);
            sender.getInventory().addItem(elaphKey);

            ItemStack krodKey = CrateManager.getKrodKey().clone();
            krodKey.setAmount(3);
            sender.getInventory().addItem(krodKey);

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Krod&7 rank has been redeemed."));
        }
        else if(user.getGroup().equals(Group.YOUTUBE))
        {
            hcfUser.setLives(hcfUser.getLives() + 15);

            ItemStack elaphKey = CrateManager.getElaph().clone();
            elaphKey.setAmount(5);
            sender.getInventory().addItem(elaphKey);

            ItemStack krodKey = CrateManager.getKrodKey().clone();
            krodKey.setAmount(3);
            sender.getInventory().addItem(krodKey);

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Famous&7 rank has been redeemed."));
        }
        else if(user.getGroup().equals(Group.FAMOUS))
        {
            hcfUser.setLives(hcfUser.getLives() + 30);

            ItemStack elaphKey = CrateManager.getElaph().clone();
            elaphKey.setAmount(3);
            sender.getInventory().addItem(elaphKey);

            ItemStack krodKey = CrateManager.getKrodKey().clone();
            krodKey.setAmount(6);
            sender.getInventory().addItem(krodKey);

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your &3Famous&7 rank has been redeemed."));
        }
        sender.updateInventory();
    }
}
