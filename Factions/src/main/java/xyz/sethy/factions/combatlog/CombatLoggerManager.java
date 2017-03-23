package xyz.sethy.factions.combatlog;

import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCLib;
import net.techcable.npclib.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.sethy.api.API;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Seth on 11/03/2017.
 */
public class CombatLoggerManager implements Listener
{
    private ConcurrentHashMap<UUID, CombatEntry> combatEntries;
    private NPCRegistry npcRegistry;
    private LinkedList<UUID> killedList;

    public CombatLoggerManager()
    {
        this.combatEntries = new ConcurrentHashMap<>();
        this.npcRegistry = NPCLib.getNPCRegistry(API.getPlugin());
        this.killedList = new LinkedList<>();
        for(NPC npc : this.npcRegistry.listNpcs())
            npc.despawn();
        npcRegistry.deregisterAll();

        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    public NPCRegistry getNpcRegistry()
    {
        return this.npcRegistry;
    }

    public void addKilled(UUID uuid)
    {
        if (!this.killedList.contains(uuid))
        {
            this.killedList.add(uuid);
        }
    }

    public void removeKilled(UUID uuid)
    {
        if (this.killedList.contains(uuid))
            this.killedList.remove(uuid);
    }

    public UUID getByNPC(NPC npc)
    {
        return npc.getUUID();
    }

    public void createNPC(Player p)
    {
        if ((p.isDead()) || (!p.isValid()) || (p.getHealth() <= 0.0D))
            return;

        CombatEntry npcWrapper = new CombatEntry(p);
        npcWrapper.runTaskTimer(API.getPlugin(), 0L, 20L);

        this.combatEntries.put(p.getUniqueId(), npcWrapper);
    }

    @EventHandler(priority= EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (this.killedList.contains(event.getPlayer().getUniqueId()))
        {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(player, (Bukkit.getWorlds().get(0)).getSpawnLocation(), false));
            player.sendMessage(ChatColor.RED + "Your NPC was killed while you were offline!");
            player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 74, 0.5));
            this.killedList.remove(player.getUniqueId());
        }
        CombatEntry npcWrapper = this.combatEntries.get(event.getPlayer().getUniqueId());

        if (npcWrapper != null)
            npcWrapper.cancel();
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event)
    {
        if(!event.getPlayer().hasMetadata("SageLogout"))
            createNPC(event.getPlayer());
    }

    public ConcurrentHashMap<UUID, CombatEntry> getCombatEntries()
    {
        return combatEntries;
    }
}
