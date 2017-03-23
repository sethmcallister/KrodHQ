package xyz.sethy.factions.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 07/03/2017.
 */
public class InventoryManager
{
    private final ConcurrentHashMap<Player, ItemStack[]> playerInventory;
    private final ConcurrentHashMap<Player, ItemStack[]> playerArmor;

    public InventoryManager()
    {
        this.playerArmor = new ConcurrentHashMap<>();
        this.playerInventory = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Player, ItemStack[]> getPlayerInventory()
    {
        return playerInventory;
    }

    public ConcurrentHashMap<Player, ItemStack[]> getPlayerArmor()
    {
        return playerArmor;
    }

    public ItemStack[] getPlayerInventory(Player player)
    {
        return this.playerInventory.get(player);
    }

    public ItemStack[] getPlayerArmor(Player player)
    {
        return this.playerArmor.get(player);
    }
}
