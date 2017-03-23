package xyz.sethy.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.factions.Factions;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 06/01/2017.
 */
public class KitSignListener implements Listener
{
    private final ConcurrentHashMap<Player, Long> cooldowns;

    public KitSignListener()
    {
        this.cooldowns = new ConcurrentHashMap<>();
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance().getPlugin());
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event)
    {
        if (event.getLine(1).equalsIgnoreCase("kit"))
        {
            if (event.getLine(2).equalsIgnoreCase("pvp"))
            {
                event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&3Click for kit"));
                event.setLine(2, ChatColor.translateAlternateColorCodes('&', "&bDiamond"));
            }
            if (event.getLine(2).equalsIgnoreCase("build"))
            {
                event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&3Click for kit"));
                event.setLine(2, ChatColor.translateAlternateColorCodes('&', "&7Builder"));
            }
            if (event.getLine(2).equalsIgnoreCase("archer"))
            {
                event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&3Click for kit"));
                event.setLine(2, ChatColor.translateAlternateColorCodes('&', "&4Archer"));
            }
            if (event.getLine(2).equalsIgnoreCase("bard"))
            {
                event.setLine(1, ChatColor.translateAlternateColorCodes('&', "&3Click for kit"));
                event.setLine(2, ChatColor.translateAlternateColorCodes('&', "&6Bard"));
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Sign)
        {
            User user = API.getUserManager().findByUniqueId(event.getPlayer().getUniqueId());
            if (user.isStaffMode())
            {
                event.getPlayer().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&cYou cannot apply kits while in staff mode."));
                return;
            }

            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3Click for kit")))
            {
                if (sign.getLine(2).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&bDiamond")))
                {
                    Player player = event.getPlayer();
                    if (this.cooldowns.containsKey(player) && this.cooldowns.get(player) > System.currentTimeMillis())
                    {
                        long millisLeft = this.cooldowns.get(player) - System.currentTimeMillis();
                        double value = millisLeft / 1000.0D;
                        double sec = Math.round(10.0D * value) / 10.0D;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot choose another kit for &l" + sec + " seconds&7."));
                        return;
                    }
                    player.getInventory().clear();
                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

                    ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
                    helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    helm.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    leggings.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
                    boots.addEnchantment(Enchantment.DURABILITY, 3);

                    player.getInventory().setItem(0, sword);
                    player.getInventory().setHelmet(helm);
                    player.getInventory().setChestplate(chestplate);
                    player.getInventory().setLeggings(leggings);
                    player.getInventory().setBoots(boots);
                    player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                    ItemStack speed = new ItemStack(373, 1, (byte) 34);
                    Potion potion = new Potion(PotionType.INSTANT_HEAL);
                    potion.setSplash(true);
                    potion.setLevel(2);
                    player.getInventory().setItem(2, speed);
                    player.getInventory().setItem(9, speed);
                    player.getInventory().setItem(18, speed);
                    player.getInventory().setItem(27, speed);
                    player.getInventory().setItem(8, new ItemStack(Material.GOLDEN_CARROT, 64));
                    for (int i = 0; i < player.getInventory().getSize(); ++i)
                    {
                        if (player.getInventory().getItem(i) != null)
                            continue;

                        player.getInventory().addItem(potion.toItemStack(1));
                    }
                    player.updateInventory();
                    this.cooldowns.put(player, 15000L + System.currentTimeMillis());
                }
                else if (sign.getLine(2).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&6Bard")))
                {
                    Player player = event.getPlayer();
                    if (this.cooldowns.containsKey(player) && this.cooldowns.get(player) > System.currentTimeMillis())
                    {
                        long millisLeft = this.cooldowns.get(player) - System.currentTimeMillis();
                        double value = millisLeft / 1000.0D;
                        double sec = Math.round(10.0D * value) / 10.0D;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot choose another kit for &l" + sec + " seconds&7."));
                        return;
                    }
                    player.getInventory().clear();
                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

                    ItemStack helm = new ItemStack(Material.GOLD_HELMET);
                    helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    helm.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack leggings = new ItemStack(Material.GOLD_LEGGINGS);
                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    leggings.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack boots = new ItemStack(Material.GOLD_BOOTS);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
                    boots.addEnchantment(Enchantment.DURABILITY, 3);

                    player.getInventory().setItem(0, sword);
                    player.getInventory().setHelmet(helm);
                    player.getInventory().setChestplate(chestplate);
                    player.getInventory().setLeggings(leggings);
                    player.getInventory().setBoots(boots);
                    player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                    Potion potion = new Potion(PotionType.INSTANT_HEAL);
                    potion.setSplash(true);
                    potion.setLevel(2);
                    ItemStack speed = new ItemStack(Material.SUGAR, 16);
                    player.getInventory().setItem(2, speed);

                    ItemStack blazePowder = new ItemStack(Material.BLAZE_POWDER, 1);
                    player.getInventory().setItem(3, blazePowder);

                    ItemStack ironIngot = new ItemStack(Material.IRON_INGOT, 1);
                    player.getInventory().setItem(4, ironIngot);

                    ItemStack feather = new ItemStack(Material.FEATHER, 16);
                    player.getInventory().setItem(5, feather);

                    player.getInventory().setItem(8, new ItemStack(Material.GOLDEN_CARROT, 64));
                    for (int i = 0; i < player.getInventory().getSize(); ++i)
                    {
                        if (player.getInventory().getItem(i) != null)
                            continue;

                        player.getInventory().addItem(potion.toItemStack(1));
                    }
                    player.updateInventory();
                    this.cooldowns.put(player, 15000L + System.currentTimeMillis());
                }
                else if (sign.getLine(2).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&7Builder")))
                {
                    Player player = event.getPlayer();
                    if (this.cooldowns.containsKey(player) && this.cooldowns.get(player) > System.currentTimeMillis())
                    {
                        long millisLeft = this.cooldowns.get(player) - System.currentTimeMillis();
                        double value = millisLeft / 1000.0D;
                        double sec = Math.round(10.0D * value) / 10.0D;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot choose another kit for &l" + sec + " seconds&7."));
                        return;
                    }
                    player.getInventory().clear();

                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

                    ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
                    helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    helm.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    leggings.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
                    boots.addEnchantment(Enchantment.DURABILITY, 3);

                    player.getInventory().setItem(0, sword);
                    player.getInventory().setHelmet(helm);
                    player.getInventory().setChestplate(chestplate);
                    player.getInventory().setLeggings(leggings);
                    player.getInventory().setBoots(boots);

                    ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
                    pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
                    pickaxe.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE);
                    shovel.addEnchantment(Enchantment.DIG_SPEED, 5);
                    shovel.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
                    axe.addEnchantment(Enchantment.DIG_SPEED, 5);
                    axe.addEnchantment(Enchantment.DURABILITY, 3);

                    player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                    player.getInventory().setItem(2, pickaxe);
                    player.getInventory().setItem(3, shovel);
                    player.getInventory().setItem(4, axe);
                    player.getInventory().setItem(5, new ItemStack(Material.STONE, 64));
                    player.getInventory().setItem(6, new ItemStack(Material.GRASS, 64));
                    player.getInventory().setItem(7, new ItemStack(Material.LOG, 64));
                    player.getInventory().setItem(8, new ItemStack(Material.GOLDEN_CARROT, 64));
                    player.getInventory().setItem(9, new ItemStack(Material.REDSTONE_BLOCK, 64));
                    player.getInventory().setItem(10, new ItemStack(356, 64));
                    player.getInventory().setItem(11, new ItemStack(Material.PISTON_BASE, 64));
                    player.getInventory().setItem(12, new ItemStack(Material.PISTON_STICKY_BASE, 64));
                    player.getInventory().setItem(13, new ItemStack(Material.GLASS, 64));
                    player.getInventory().setItem(14, new ItemStack(Material.STRING, 64));
                    player.getInventory().setItem(15, new ItemStack(Material.WATER_BUCKET, 4));

                    this.cooldowns.put(player, 15000L + System.currentTimeMillis());
                    player.updateInventory();
                    return;
                }
                else if (sign.getLine(2).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&4Archer")))
                {
                    Player player = event.getPlayer();
                    if (this.cooldowns.containsKey(player) && this.cooldowns.get(player) > System.currentTimeMillis())
                    {
                        long millisLeft = this.cooldowns.get(player) - System.currentTimeMillis();
                        double value = millisLeft / 1000.0D;
                        double sec = Math.round(10.0D * value) / 10.0D;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You cannot choose another kit for &l" + sec + " seconds&7."));
                        return;
                    }
                    player.getInventory().clear();
                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

                    ItemStack bow = new ItemStack(Material.BOW);
                    bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
                    bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

                    ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
                    helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    helm.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    leggings.addEnchantment(Enchantment.DURABILITY, 3);

                    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
                    boots.addEnchantment(Enchantment.DURABILITY, 3);

                    player.getInventory().setItem(0, sword);
                    player.getInventory().setItem(2, bow);
                    player.getInventory().setHelmet(helm);
                    player.getInventory().setChestplate(chestplate);
                    player.getInventory().setLeggings(leggings);
                    player.getInventory().setBoots(boots);
                    player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
                    Potion potion = new Potion(PotionType.INSTANT_HEAL);
                    potion.setSplash(true);
                    potion.setLevel(2);
                    player.getInventory().setItem(9, new ItemStack(Material.ARROW));
                    player.getInventory().setItem(8, new ItemStack(Material.GOLDEN_CARROT, 64));
                    for (int i = 0; i < player.getInventory().getSize(); ++i)
                    {
                        if (player.getInventory().getItem(i) != null)
                            continue;

                        player.getInventory().addItem(potion.toItemStack(1));
                    }
                    player.updateInventory();
                    this.cooldowns.put(player, 15000L + System.currentTimeMillis());
                }
            }
        }
    }
}
