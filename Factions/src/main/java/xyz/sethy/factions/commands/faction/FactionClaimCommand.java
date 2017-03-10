package xyz.sethy.factions.commands.faction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.VisualClaim;
import xyz.sethy.factions.dto.claim.VisualClaimType;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 29/11/2016.
 */
public class FactionClaimCommand implements ICommand, Listener
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        Faction faction = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (faction == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You are not in a faction."));
            return;
        }
        if (faction.isRaidable())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You cannot claim land while your faction is raidable."));
            return;
        }
        if (!faction.isCaptain(sender))
        {
            if (faction.isLeader(sender))
            {
                sender.getInventory().addItem(Factions.getInstance().getItemHandler().selectionWand);
                new VisualClaim(sender, VisualClaimType.CREATE, false).draw(false);
                if (!VisualClaim.getCurrentMaps().containsKey(sender.getName()))
                {
                    new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
                }
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3You must at-least be a &cCaptain&3 to claim land for your faction."));
            return;
        }
        sender.getInventory().addItem(Factions.getInstance().getItemHandler().selectionWand);
        new VisualClaim(sender, VisualClaimType.CREATE, false).draw(false);
        if (!VisualClaim.getCurrentMaps().containsKey(sender.getName()))
        {
            new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
        }
        return;
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event)
    {
        if (event.getItemDrop().getItemStack().equals(Factions.getInstance().getItemHandler().selectionWand))
        {
            final VisualClaim vc = VisualClaim.getVisualClaim(event.getPlayer().getName());
            if (vc != null)
            {
                event.setCancelled(true);
                vc.cancel(false);
            }
            Bukkit.getServer().getScheduler().runTaskLater(Factions.getInstance().getPlugin(), () -> event.getItemDrop().remove(), 1L);
        }
    }

    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent event)
    {
        event.getPlayer().getInventory().remove(Factions.getInstance().getItemHandler().selectionWand);
    }
}
