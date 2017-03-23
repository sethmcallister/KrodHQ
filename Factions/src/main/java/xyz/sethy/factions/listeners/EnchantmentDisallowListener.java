package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.factions.Factions;

/**
 * Created by Seth on 31/01/2017.
 */
public class EnchantmentDisallowListener implements Listener
{
    public EnchantmentDisallowListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event)
    {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null)
            return;

        if (item.getItemMeta() == null)
            return;

        if (item.containsEnchantment(Enchantment.DAMAGE_ALL))
        {
            int sharpness = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            if (sharpness > Factions.getInstance().getEnchantmentManager().getMaxLevel(Enchantment.DAMAGE_ALL))
            {
                item.removeEnchantment(Enchantment.DAMAGE_ALL);
                item.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            }
        }
        else if(item.containsEnchantment(Enchantment.FIRE_ASPECT))
        {
            int fireAspect = item.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
            if(fireAspect > Factions.getInstance().getEnchantmentManager().getMaxLevel(Enchantment.FIRE_ASPECT))
            {
                item.removeEnchantment(Enchantment.FIRE_ASPECT);
                item.addEnchantment(Enchantment.FIRE_ASPECT, 0);
            }
        }
    }
}
