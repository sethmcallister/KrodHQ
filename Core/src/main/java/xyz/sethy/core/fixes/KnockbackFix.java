package xyz.sethy.core.fixes;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import xyz.sethy.api.API;
import xyz.sethy.core.Core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Seth on 22/01/2017.
 */
public class KnockbackFix implements Listener
{
    private Field fieldPlayerConnection;
    private Method sendPacket;
    private Constructor<?> packetVelocity;

    public KnockbackFix()
    {
        try
        {
            Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + Core.getInstance().getVerMultiplier() + ".EntityPlayer");
            Class<?> packetVelocityClass = Class.forName("net.minecraft.server." + Core.getInstance().getVerMultiplier() + ".PacketPlayOutEntityVelocity");
            Class<?> playerConnectionClass = Class.forName("net.minecraft.server." + Core.getInstance().getVerMultiplier() + ".PlayerConnection");

            // Get the fields here to improve performance later on
            this.fieldPlayerConnection = entityPlayerClass.getField("playerConnection");
            this.sendPacket = playerConnectionClass.getMethod("sendPacket", packetVelocityClass.getSuperclass());
            this.packetVelocity = packetVelocityClass.getConstructor(int.class, double.class, double.class, double.class);
        }
        catch (ClassNotFoundException | NoSuchFieldException | SecurityException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, API.getPlugin());

    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event)
    {
        Player damaged = event.getPlayer();
        EntityDamageEvent lastDamage = damaged.getLastDamageCause();

        if (lastDamage == null || !(lastDamage instanceof EntityDamageByEntityEvent))
            return;

        // Cancel the vanilla knockback
        if (((EntityDamageByEntityEvent) lastDamage).getDamager() instanceof Player)
        {
            Player damager = (Player) ((EntityDamageByEntityEvent) lastDamage).getDamager();
            double horMultiplier = Core.getInstance().getHorMultiplier();
            double verMultiplier = Core.getInstance().getVerMultiplier();
            double sprintMultiplier = damager.isSprinting() ? 0.8D : 0.5D;
            double kbMultiplier = damager.getItemInHand() == null ? 0 : damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2D;
            double airMultiplier = damaged.isOnGround() ? 1 : 0.5;

            Vector knockback = damaged.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();
            knockback.setX((knockback.getX() * sprintMultiplier + kbMultiplier) * horMultiplier);
            knockback.setY(0.35D * airMultiplier * verMultiplier);
            knockback.setZ((knockback.getZ() * sprintMultiplier + kbMultiplier) * horMultiplier);
            event.setVelocity(knockback);
        }
    }

//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
//    {
//        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
//            return;
//
//        if (event.isCancelled())
//            return;
//
//        Player damaged = (Player) event.getEntity();
//        Player damager = (Player) event.getDamager();
//
//        if (damaged.getNoDamageTicks() > damaged.getMaximumNoDamageTicks() / 2D)
//            return;
//
//        double horMultiplier = Core.getInstance().getHorMultiplier();
//        double verMultiplier = Core.getInstance().getVerMultiplier();
//        double sprintMultiplier = damager.isSprinting() ? 0.8D : 0.5D;
//        double kbMultiplier = damager.getItemInHand() == null ? 0 : damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2D;
//        double airMultiplier = damaged.isOnGround() ? 1 : 0.5;
//
//        Vector knockback = damaged.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();
//        knockback.setX((knockback.getX() * sprintMultiplier + kbMultiplier) * horMultiplier);
//        knockback.setY(0.35D * airMultiplier * verMultiplier);
//        knockback.setZ((knockback.getZ() * sprintMultiplier + kbMultiplier) * horMultiplier);
//
//        try
//        {
//            // Send the velocity packet immediately instead of using setVelocity, which fixes the 'relog bug'
//            Object entityPlayer = damaged.getClass().getMethod("getHandle").invoke(damaged);
//            Object playerConnection = fieldPlayerConnection.get(entityPlayer);
//            Object packet = packetVelocity.newInstance(damaged.getEntityId(), knockback.getX(), knockback.getY(), knockback.getZ());
//            sendPacket.invoke(playerConnection, packet);
//        }
//        catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e)
//        {
//            e.printStackTrace();
//        }
//    }
}
