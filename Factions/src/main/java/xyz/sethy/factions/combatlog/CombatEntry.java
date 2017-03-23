package xyz.sethy.factions.combatlog;

import net.techcable.npclib.NPC;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.factions.Factions;

import java.util.UUID;

/**
 * Created by Seth on 11/03/2017.
 */
public class CombatEntry extends BukkitRunnable implements Listener
{
    private UUID uuid;
    private String name;
    private NPC npc;
    private long deathBanTime;
    private BukkitTask bukkitTask;

    public UUID getUuid()
    {
        return this.uuid;
    }

    public String getName()
    {
        return this.name;
    }

    public NPC getNpc()
    {
        return this.npc;
    }

    public long getDeathBanTime()
    {
        return this.deathBanTime;
    }

    public BukkitTask getBukkitTask()
    {
        return this.bukkitTask;
    }

    private int i = 30;

    public CombatEntry(Player player)
    {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.deathBanTime = Factions.getInstance().getDeathbanManager().getDeathbanTime(API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup());

        this.npc = Factions.getInstance().getCombatLoggerManager().getNpcRegistry().createNPC(EntityType.PLAYER, this.uuid, this.name);

        this.npc.spawn(player.getLocation());
        this.npc.setProtected(false);

        Player npcPlayer = (Player)this.npc.getEntity();

        npcPlayer.getInventory().setContents(player.getInventory().getContents());
        npcPlayer.getInventory().setArmorContents(player.getInventory().getArmorContents());
        npcPlayer.setTotalExperience(player.getTotalExperience());

        Bukkit.getPluginManager().registerEvents(this, API.getPlugin());
    }

    public void removeNPC()
    {
        this.npc.despawn();
        Factions.getInstance().getCombatLoggerManager().getNpcRegistry().deregister(this.npc);
    }

    public void updateTimer()
    {
        this.i = 30;
    }

    public void run()
    {
        if (this.i > 0)
            this.i -= 1;
        else
            cancel();
    }

    public void cancel()
    {
        removeNPC();
        super.cancel();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        if (Factions.getInstance().getCombatLoggerManager().getNpcRegistry().isNPC(event.getEntity()))
        {
            NPC npc = Factions.getInstance().getCombatLoggerManager().getNpcRegistry().getAsNPC(event.getEntity());
            if (npc.equals(getNpc()))
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getUuid());
                Factions.getInstance().getCombatLoggerManager().addKilled(getUuid());
                KitmapUser kitmapUser = API.getUserManager().getTempKitsUser(getUuid());
                Player killer = event.getEntity().getKiller();
                KitmapUser kKiller = API.getUserManager().findKitmapByUniqueId(killer.getUniqueId());
                kitmapUser.setDeaths(kitmapUser.getDeaths() + 1);
                kitmapUser.setKillStreak(0);
                kKiller.setKillStreak(kKiller.getCurrentKillStreak() + 1);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7(Combat-Logger)&c" + offlinePlayer.getName() + "&4[" + kitmapUser.getKills() + "]&7 was slain by &c" + killer.getName() + "&4[" + kKiller.getKills() + "]&7 using &c" + getItemName(killer.getItemInHand()) + "&7."));
                cancel();
            }
        }
    }

    private String getItemName(final ItemStack i)
    {
        if (i.getItemMeta().hasDisplayName())
            return ChatColor.stripColor(i.getItemMeta().getDisplayName());

        return WordUtils.capitalizeFully(i.getType().name().replace('_', ' '));
    }
}
