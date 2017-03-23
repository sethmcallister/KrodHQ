package xyz.sethy.factions.listeners;

import net.minecraft.util.com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.factions.Factions;

/**
 * Created by Seth on 22/01/2017.
 */
public class BuySignListener implements Listener
{
    public BuySignListener()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.WALL_SIGN)
            {
                BlockState state = event.getClickedBlock().getState();
                if (state instanceof Sign)
                {

                    HCFUser user = API.getUserManager().findHCFByUniqueId(event.getPlayer().getUniqueId());
                    Sign s = (Sign) state;
                    if (s.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[Buy]"))
                    {
                        ItemStack item;
                        int amount = Integer.valueOf(s.getLine(2));
                        if (s.getLine(1).equals("END_PORTAL"))
                        {
                            item = new ItemStack(Material.ENDER_PORTAL_FRAME, amount);
                        }
                        else if(s.getLine(1).equals("WRENCH"))
                        {
                            ItemStack is = Factions.getInstance().getItemHandler().wrench;
                            is.setAmount(amount);
                            item = is;
                        }
                        else if (Material.getMaterial(s.getLine(1)) != null)
                        {
                            item = new ItemStack(Material.getMaterial(s.getLine(1)), amount);
                        }
                        else
                        {
                            String[] parts = s.getLine(1).split(":");
                            int id = Integer.parseInt(parts[0]);
                            byte data = (byte) (parts.length > 1 ? Integer.parseInt(parts[1]) : 0);
                            item = new ItemStack(id, amount, data);
                        }
                        int price = Integer.valueOf(s.getLine(3).replace("$", ""));
                        if (user.getBalance() < price)
                        {
                            player.sendMessage(ChatColor.RED + "Insufficient funds!");
                            return;
                        }
                        if (player.getInventory().firstEmpty() == -1)
                        {
                            player.sendMessage(ChatColor.RED + "Your inventory is full!");
                            return;
                        }
                        player.getInventory().addItem(item);
                        player.updateInventory();
                        user.setBalance(user.getBalance() - price);
                        player.sendMessage(ChatColor.GREEN + "Purchase successful! New balance: $" + user.getBalance());
                        return;
                    }
                    if (s.getLine(0).equalsIgnoreCase(ChatColor.RED + "[Sell]"))
                    {
                        ItemStack item;
                        int amount = Integer.valueOf(s.getLine(2));
                        if (Material.getMaterial(s.getLine(1)) != null)
                        {
                            item = new ItemStack(Material.getMaterial(s.getLine(1)), amount);
                        }
                        else
                        {
                            String[] parts = s.getLine(1).split(":");
                            int id = Integer.parseInt(parts[0]);
                            byte data = (byte) (parts.length > 1 ? Integer.parseInt(parts[1]) : 0);
                            item = new ItemStack(id, amount, data);
                        }
                        int price = Integer.valueOf(s.getLine(3).replace("$", ""));
                        int pricePI = price / amount;
                        if (countAmount(player.getInventory(), item.getType()) <= 0)
                        {
                            player.sendMessage(ChatColor.RED + "Your inventory does not contain that item.");
                            return;
                        }
                        if (countAmount(player.getInventory(), item.getType()) < amount)
                        {
                            player.sendMessage(ChatColor.GREEN + "Successfully sold " + countAmount(player.getInventory(), item.getType()) + " items for $" + pricePI * countAmount(player.getInventory(), item.getType())
                                    + "! New balance: $" + user.getBalance());
                            player.getInventory().removeItem(new ItemStack(item.getType(), amount));
                            int i = price * countAmount(player.getInventory(), item.getType());
                            user.setBalance(user.getBalance() + i);
                            player.updateInventory();
                            return;
                        }
                        player.getInventory().removeItem(new ItemStack(item.getType(), amount));
                        player.updateInventory();
                        user.setBalance(user.getBalance() + price);
                        player.sendMessage(ChatColor.GREEN + "Successfuly sold " + amount + " items for $" + price
                                + "! New balance: $" + user.getBalance());
                        player.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event)
    {
        Player player = event.getPlayer();
        if(API.getUserManager().findByUniqueId(player.getPlayer().getUniqueId()).getGroup().getPermission() < Group.ADMIN.getPermission())
        {
            event.setCancelled(true);
            return;
        }
        if (event.getLine(0).equalsIgnoreCase("[Buy]"))
        {
            event.setLine(0, ChatColor.GREEN + "[Buy]");
            if (Material.getMaterial(event.getLine(1)) == null && Material.getMaterial(Ints.tryParse(event.getLine(1))) == null && !event.getLine(1).equals("WRENCH") && !event.getLine(1).equals("END_PORTAL"))
            {
                player.sendMessage(ChatColor.RED + "Invalid material!");
                return;
            }
            try
            {
                int amount = Ints.tryParse(event.getLine(2).replace("$", ""));
            }
            catch (Exception ex)
            {
                player.sendMessage(ChatColor.RED + event.getLine(2) + " is not a valid number!");
                return;
            }
            if (!event.getLine(3).contains("$"))
            {
                player.sendMessage(ChatColor.RED + "Line 4 must contain '$'");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "Successfully created a shop sign!");
            player.sendMessage(ChatColor.GREEN + "Type: Buy");
            player.sendMessage(ChatColor.GREEN + "Material: " + event.getLine(1));
            player.sendMessage(ChatColor.GREEN + "Number: " + event.getLine(2));
            player.sendMessage(ChatColor.GREEN + "Price: " + event.getLine(3));
        }

        if (event.getLine(0).equalsIgnoreCase("[Sell]"))
        {
            event.setLine(0, ChatColor.RED + "[Sell]");
            if (Material.getMaterial(event.getLine(1)) == null && Material.getMaterial(Ints.tryParse(event.getLine(1))) == null && !event.getLine(1).equals("CROWBAR"))
            {
                player.sendMessage(ChatColor.RED + "Invalid material!");
                return;
            }
            try
            {
                int amount = Ints.tryParse(event.getLine(2).replace("$", ""));
            }
            catch (Exception ex)
            {
                player.sendMessage(ChatColor.RED + event.getLine(2) + " is not a valid number!");
                return;
            }
            if (!event.getLine(3).contains("$"))
            {
                player.sendMessage(ChatColor.RED + "Line 4 must contain '$'");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "Successfully created a shop sign!");
            player.sendMessage(ChatColor.GREEN + "Type: Sell");
            player.sendMessage(ChatColor.GREEN + "Material: " + event.getLine(1));
            player.sendMessage(ChatColor.GREEN + "Number: " + event.getLine(2));
            player.sendMessage(ChatColor.GREEN + "Price: " + event.getLine(3));
        }
    }

    private int countAmount(Inventory inventory, Material type)
    {
        ItemStack[] contents = inventory.getContents();
        int counter = 0;
        for (ItemStack item : contents)
        {
            if (item == null || item.getType() != type) continue;
            counter += item.getAmount();
        }
        return counter;
    }

    private int countAmount(CraftInventory inventory, Material type)
    {
        ItemStack[] contents = inventory.getContents();
        int counter = 0;
        for (ItemStack item : contents)
        {
            if (item == null || item.getType() != type) continue;
            counter += item.getAmount();
        }
        return counter;
    }
}
