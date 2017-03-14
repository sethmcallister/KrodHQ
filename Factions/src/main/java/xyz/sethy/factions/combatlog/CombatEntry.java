package xyz.sethy.factions.combatlog;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.framework.group.Group;

import java.util.UUID;

/**
 * Created by Seth on 11/03/2017.
 */
public class CombatEntry
{
    private final Villager villager;
    private final UUID uuid;
    private final ItemStack[] inventory;
    private final ItemStack[] armor;
    private final String playerName;
    private final Group group;

    public CombatEntry(Villager villager, UUID uuid, ItemStack[] inventory, ItemStack[] armor, String playerName, Group group)
    {
        this.villager = villager;
        this.uuid = uuid;
        this.inventory = inventory;
        this.armor = armor;
        this.group = group;
        this.playerName = playerName;
    }

    public Villager getVillager()
    {
        return villager;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public ItemStack[] getInventory()
    {
        return inventory;
    }

    public ItemStack[] getArmor()
    {
        return armor;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public Group getGroup()
    {
        return group;
    }
}
