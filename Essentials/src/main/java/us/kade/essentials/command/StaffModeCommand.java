package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.kade.essentials.Essentials;
import us.kade.essentials.inventories.StaffmodeInventory;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 02/01/2017.
 */
public class StaffModeCommand extends CommandBase
{
    private ConcurrentHashMap<Player, ItemStack[]> armor;
    private ConcurrentHashMap<Player, ItemStack[]> items;

    public StaffModeCommand()
    {
        super("staffmode", Group.TRAIL_MOD, true);
        this.armor = new ConcurrentHashMap<>();
        this.items = new ConcurrentHashMap<>();
        Bukkit.getPluginCommand("staffmode").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (Essentials.getInstance().getStaffModeManager().getStaffModes().contains(sender))
        {
            sender.getInventory().clear();
            if (armor.get(sender) != null && items.get(sender) != null)
            {
                sender.getInventory().setArmorContents(armor.get(sender));
                sender.getInventory().setContents(items.get(sender));
            }
            Essentials.getInstance().getStaffModeManager().getStaffModes().remove(sender);
            for (Player player : Bukkit.getOnlinePlayers())
            {
                User user1 = API.getUserManager().findByUniqueId(player.getUniqueId());
                if (user1.getGroup().getPermission() <= Group.TRAIL_MOD.getPermission())
                {
                    if (!player.canSee(sender))
                        player.showPlayer(sender);
                }
            }
            sender.setGameMode(GameMode.SURVIVAL);
            user.setVansihed(false);
            user.setStaffMode(false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Staff Mode: &cDisabled"));
            return;
        }
        armor.put(sender, sender.getInventory().getArmorContents());
        items.put(sender, sender.getInventory().getContents());
        sender.getInventory().clear();
        StaffmodeInventory.givePlayerStaffItems(sender);
        sender.updateInventory();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            User user1 = API.getUserManager().findByUniqueId(player.getUniqueId());
            if (user1.getGroup().getPermission() <= Group.TRAIL_MOD.getPermission())
            {
                if (player.canSee(sender))
                    player.hidePlayer(sender);
            }
        }
        Essentials.getInstance().getStaffModeManager().getStaffModes().add(sender);
        sender.setGameMode(GameMode.CREATIVE);
        user.setVansihed(true);
        user.setStaffMode(true);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Staff Mode: &aEnabled"));
    }

}