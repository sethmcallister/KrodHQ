package xyz.sethy.factions.commands.faction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.Faction;
import xyz.sethy.factions.handlers.commands.ICommand;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 30/11/2016.
 */
public class FactionListCommand implements ICommand
{
    @Override
    public void onCommand(Player sender, Command command, String label, String[] args)
    {
        TreeSet<Faction> factions = new TreeSet<>(new sizeComparator());

        for (Faction faction : Factions.getInstance().getFactionManager().getFactions())
        {
            if(faction != null)
            {
                if (faction.getLeader() != null)
                {
                    if (faction.getOnlineMembers().size() > 0)
                        factions.add(faction);
                }
            }
        }

        Map<Faction, Integer> factionMap = new ConcurrentHashMap<>();
        int i = 0;
        for (Faction faction : factions)
        {
            factionMap.put(faction, i);
            i++;
        }
        int page;
        int maxPages = factionMap.size() / 10;
        maxPages++;
        if (args.length != 2)
        {
            page = 1;
        }
        else
        {
            page = Integer.parseInt(args[1]);
        }

        if (page < 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot view a page less than 1."));
            return;
        }
        if (page > maxPages)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot view faction pages larger than &f" + maxPages + "&3."));
            return;
        }
        final int start = (page - 1) * 10;
        int index = 0;
        if (maxPages == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere are no factions to view."));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&m------&7[&aOnline Faction List " + page + "/" + maxPages + "&7]&3&m------"));
        for (final Map.Entry<Faction, Integer> factionEntry : factionMap.entrySet())
        {
            if (index++ < start)
                continue;
            if (index > start + 10)
                break;

            StringBuilder message = new StringBuilder();
            message.append(factionEntry.getKey().getName(sender))
                    .append(" &7")
                    .append(factionEntry.getKey().getOnlineMembers().size())
                    .append("/")
                    .append(factionEntry.getKey().getAllMembers().size())
                    .append(" online");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.toString()));
        }
    }

    class sizeComparator implements Comparator<Faction>
    {
        @Override
        public int compare(Faction o1, Faction o2)
        {
            if (o1.getOnlineMembers().size() < o2.getOnlineMembers().size())
                return -1;
            else
                return 1;
        }
    }
}