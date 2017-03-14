package xyz.sethy.factions.combatlog;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;

/**
 * Created by Seth on 11/03/2017.
 */
public class CombatLoggerManager
{
    private ArrayList<CombatEntry> combatEntries;

    public CombatLoggerManager()
    {
        this.combatEntries = new ArrayList<>();
    }

    public CombatEntry findByPlayer(Player player)
    {
        for(CombatEntry combatEntry : combatEntries)
        {
            if(combatEntry.getUuid().equals(player.getUniqueId()))
            {
                return combatEntry;
            }
        }
        return null;
    }

    public CombatEntry findByEntity(Villager entity)
    {
        for(CombatEntry combatEntry : combatEntries)
        {
            if(combatEntry.getVillager().equals(entity))
            {
                return combatEntry;
            }
        }
        return null;
    }

    public ArrayList<CombatEntry> getCombatEntries()
    {
        return combatEntries;
    }
}
