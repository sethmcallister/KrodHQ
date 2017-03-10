package xyz.sethy.factions.commands.faction.staff;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.dto.claim.VisualClaim;
import xyz.sethy.factions.dto.claim.VisualClaimType;
import xyz.sethy.factions.handlers.commands.ICommand;
import xyz.sethy.factions.handlers.dtr.DTRType;
import xyz.sethy.factions.koth.dto.CoreKoth;
import xyz.sethy.factions.koth.dto.Koth;

import java.util.regex.Pattern;

/**
 * Created by Seth on 26/01/2017.
 */
public class FactionCreateKoTHCommand implements ICommand
{
    private final Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        User user = API.getUserManager().findByUniqueId(sender.getUniqueId());
        if (user.getGroup().getPermission() < Group.ADMIN.getPermission())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You do not have permission to execute this command."));
            return;
        }
        if (ALPHA_NUMERIC.matcher(args[1]).find())
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFaction name must be alphanumeric."));
            return;
        }
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Please specify a faction name."));
            return;
        }
        if (StringUtils.length(args[1]) > 16)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour faction name cannot be longer than 16 characters."));
            return;
        }
        if (StringUtils.length(args[1]) < 3)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour faction name must be at-least 3 characters."));
            return;
        }

        Faction current = Factions.getInstance().getFactionManager().findByPlayer(sender);
        if (current != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already in a faction."));
            return;
        }

        if (Factions.getInstance().getFactionManager().findByName(args[1]) != null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cA faction with that name already exists."));
            return;
        }
        Faction faction = new Faction();
        faction.setName(args[1]);
        int dtrInt = (int) Math.floor(0);
        dtrInt += DTRType.CAP_ZONE.getBitmask();
        faction.setDtr((double) dtrInt);
        faction.getAllMembers().add(sender.getUniqueId());
        faction.getOnlineMembers().add(sender.getUniqueId());
        faction.setLeader(sender.getUniqueId());
        faction.flagSave();
        Factions.getInstance().getFactionManager().getFactions().add(faction);

        Koth koth = new CoreKoth();
        koth.getName().set(args[1]);
        Factions.getInstance().getKothHandler().getKoths().add(koth);

        sender.getInventory().addItem(Factions.getInstance().getItemHandler().selectionWand);
        new VisualClaim(sender, VisualClaimType.KOTH, true).draw(false);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have created the KoTH &3" + args[1] + "."));
    }
}
