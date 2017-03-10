package xyz.sethy.factions.commands.faction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 29/11/2016.
 */
public class FactionBaseCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Faction &7Help &f#1"));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f create [name] &7- &3Create a faction."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f leave &7- &3Leave Your current faction."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f show <faction> &7- &3Gets the information about a faction."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f chat &7- &3Toggle faction chat mode."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f map &7- &3Show all claims within 50 blocks."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f deposit [amount] &7- &3Deposit money to your faction."));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7/f home &7- &3Teleport to your factions base."));
//            sender.sendMessage(" ");
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3To view additional faction help pages, type &f/f help page#"));
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member Help: &7/f help member"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR Help: &7/f help dtr"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Captain/Leader Help: &7/f help captain"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return;
        }

        if (args[1].equalsIgnoreCase("member"))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFaction Member Help"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f show:&7 Display information about your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f list:&7 List all other factions online."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f home:&7 Teleport to your faction home."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f map:&7 Show all faction claims within 50 blocks."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f leave:&7 Leave your current faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f stuck:&7 Used to escape from an enemy claim."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f create:&7 Create a new faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f disposit&7: Deposit money into your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return;
        }
        else if (args[1].equalsIgnoreCase("dtr"))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFaction DTR Help"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f show:&7 Display information about your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Every player death will loose 1.0 DTR, once your faction reaches 0 DTR it will become raidable, meaning players can interact with your claim as-if it was unclaimed."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7DTR will start to regenerate by 0.2 every minute one hour after the most recent member death."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return;
        }
        else if (args[1].equalsIgnoreCase("captain"))
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFaction Captain/Leader Help"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f kick:&7 Kick a player from your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f claim:&7 Claim more land for your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f promote:&7 Promote a member to a faction captain."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f unclaim:&7 Unclaim all your faction's land."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f demote:&7 Demote a captain to a member."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f announce:&7 Set the faction's new announcement."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f invite:&7 Invite a player to your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f uninvite:&7 Un-invite a player from your faction."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/f sethome:&7 Set your faction's home."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return;
        }
        else
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Member Help: &7/f help member"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR Help: &7/f help dtr"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Captain/Leader Help: &7/f help captain"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------------"));
            return;
        }
    }
}
