package xyz.sethy.factions.commands.faction;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.dto.claim.VisualClaim;
import xyz.sethy.factions.dto.claim.VisualClaimType;
import xyz.sethy.factions.handlers.commands.ICommand;

/**
 * Created by sethm on 06/12/2016.
 */
public class FactionMapCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        new VisualClaim(sender, VisualClaimType.MAP, false).draw(false);
    }
}
