package xyz.sethy.factions.commands.essentials.core.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.factions.Factions;

/**
 * Created by Seth on 07/03/2017.
 */
public class RefundInventoryCommand extends CommandBase
{
    public RefundInventoryCommand()
    {
        super("refund", Group.TRAIL_MOD, true);
        Bukkit.getPluginCommand("refund").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command1, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/refund <player>"));
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3A player with that name or UUID could not be found."));
            return;
        }
        player.getInventory().setContents(Factions.getInstance().getInventoryManager().getPlayerInventory(player));
        player.getInventory().setArmorContents(Factions.getInstance().getInventoryManager().getPlayerArmor(player));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have successfully refunded " + player.getName() + "'s Inventory."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your Inventory has successfully been restored."));
    }
}
