package xyz.sethy.factions.managers;

import org.bukkit.enchantments.Enchantment;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 31/01/2017.
 */
public class EnchantmentManager
{
    private final ConcurrentHashMap<Enchantment, Integer> maxLevels;

    public EnchantmentManager()
    {
        this.maxLevels = new ConcurrentHashMap<>();
        this.maxLevels.put(Enchantment.DAMAGE_ALL, 2);
        this.maxLevels.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        this.maxLevels.put(Enchantment.DURABILITY, 3);
        this.maxLevels.put(Enchantment.FIRE_ASPECT, 0);
    }

    public int getMaxLevel(Enchantment enchantment)
    {
        return maxLevels.get(enchantment);
    }
}
