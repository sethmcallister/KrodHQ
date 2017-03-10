package xyz.sethy.guard.utils;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sethm on 16/11/2016.
 */
public class UtilServer
{
    public static List<Entity> getEntities(World world)
    {
        List<org.bukkit.entity.Entity> entities = new ArrayList<Entity>();

        net.minecraft.server.v1_7_R4.World nmsworld = ((CraftWorld) world).getHandle();
        for (Object o : new ArrayList<>(nmsworld.entityList))
        {
            if ((o instanceof Entity))
            {
                net.minecraft.server.v1_7_R4.Entity mcEnt = (net.minecraft.server.v1_7_R4.Entity) o;
                org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
                if (bukkitEntity != null)
                {
                    entities.add(bukkitEntity);
                }
            }
        }
        return entities;
    }
}
