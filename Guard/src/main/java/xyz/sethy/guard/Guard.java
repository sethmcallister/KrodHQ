package xyz.sethy.guard;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.BanType;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.guard.checks.Check;
import xyz.sethy.guard.checks.combat.*;
import xyz.sethy.guard.checks.movement.Phase;
import xyz.sethy.guard.checks.movement.Speed;
import xyz.sethy.guard.checks.other.Crash;
import xyz.sethy.guard.checks.other.MorePackets;
import xyz.sethy.guard.checks.other.Sneak;
import xyz.sethy.guard.commands.CancelCommand;
import xyz.sethy.guard.packets.PacketHandler;
import xyz.sethy.guard.utils.LagUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 08/08/2016.
 */
public class Guard implements Listener
{
    private static Guard instance;
    private Plugin plugin;
    public Map<UUID, Map<Check, Integer>> violations;
    public Map<UUID, Map<Check, Long>> violationReset;
    private LagUtils lagUtils;
    private Map<Player, Map.Entry<Check, Long>> autoban;
    private PacketHandler packetHandler;
    private LinkedList<Check> checks;
    private Map<UUID, Map.Entry<Long, Vector>> lastVelocity;
    private LinkedList<Player> toCancel;
    private Settings settings;

    public Guard(Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        this.violations = new ConcurrentHashMap<>();
        this.violationReset = new ConcurrentHashMap<>();
        this.lagUtils = new LagUtils(plugin);
        this.autoban = new ConcurrentHashMap<>();
        this.packetHandler = new PacketHandler(plugin);
        this.lastVelocity = new ConcurrentHashMap<>();
        this.toCancel = new LinkedList<>();
        this.settings = new Settings();

        this.checks = new LinkedList<>();
        this.checks.add(new AttackSpeed(plugin));
        this.checks.add(new Crits(plugin));
        this.checks.add(new DoubleClick(plugin));
        this.checks.add(new FastBow(plugin));
        this.checks.add(new KillAuraA(plugin));
        this.checks.add(new KillAuraB(plugin));
        this.checks.add(new KillAuraC(plugin));
        this.checks.add(new KillAuraD(plugin));
        this.checks.add(new NoSwing(plugin));
        this.checks.add(new Reach(plugin));

        this.checks.add(new Speed(plugin));
        this.checks.add(new Phase(plugin));

        this.checks.add(new MorePackets(plugin));
        this.checks.add(new Sneak(plugin));
        this.checks.add(new Crash(plugin));

        new CancelCommand();

        for (Check check : checks)
        {
            Bukkit.getServer().getPluginManager().registerEvents(check, plugin);
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static Guard getInstance()
    {
        return instance;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public Integer getViolations(Player player, Check check)
    {
        if (this.violations.containsKey(player.getUniqueId()))
            return this.violations.get(player.getUniqueId()).get(check);

        return 0;
    }

    public void addViolation(Player player, Check check)
    {
        Map<Check, Integer> map = new ConcurrentHashMap<>();
        if (this.violations.containsKey(player.getUniqueId()))
            map = this.violations.get(player.getUniqueId());

        if (!map.containsKey(check))
        {
            map.put(check, 1);
        }
        else
        {
            int i = map.get(check) + 1;
            map.put(check, i);
        }
        this.violations.put(player.getUniqueId(), map);
    }


    public void RegisterListener(Listener listener)
    {
        Bukkit.getServer().getPluginManager().registerEvents(listener, Guard.getInstance().getPlugin());
    }

    public void autoban(final Check check, final Player player)
    {
        if (this.lagUtils.getTPS() < 17.0D)
            return;

        if (check.hasBanTimer())
        {
            if (this.autoban.containsKey(player))
                return;

            this.autoban.put(player, new AbstractMap.SimpleEntry<>(check, System.currentTimeMillis() + 15000L));

            System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " will be banned in 15s for " + check.getName() + ".");

            FancyMessage message = new FancyMessage();
            message.text("[KAC]").color(ChatColor.GOLD)
                    .then(player.getName()).color(ChatColor.WHITE)
                    .formattedTooltip(new FancyMessage()
                            .text("Click to teleport to ").color(ChatColor.GOLD)
                            .then(player.getName()).color(ChatColor.AQUA)
                            .then(".").color(ChatColor.GOLD))
                    .command("/teleport " + player.getName())
                    .then(" will be banned for ").color(ChatColor.GRAY)
                    .then(check.getName()).color(ChatColor.AQUA)
                    .then(" in 15s").color(ChatColor.GRAY)
                    .then("[Freeze]").color(ChatColor.AQUA)
                    .formattedTooltip(new FancyMessage()
                            .text("Click to freeze ").color(ChatColor.GOLD)
                            .then(player.getName()).color(ChatColor.AQUA)
                            .then(".").color(ChatColor.GOLD))
                    .command("/freeze " + player.getName())
                    .then(", ").color(ChatColor.GRAY)
                    .then("[Cancel]").color(ChatColor.AQUA)
                    .formattedTooltip(new FancyMessage()
                            .text("Click to cancel ban.").color(ChatColor.GOLD))
                    .command("/cancel " + player.getName());

            for (Player playerplayer : Bukkit.getOnlinePlayers())
            {
                User user = API.getUserManager().findByUniqueId(playerplayer.getUniqueId());
                if (user.getGroup().getPermission() > Group.TRAIL_MOD.getPermission())
                {
                    message.send(playerplayer);
                }
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (toCancel.contains(player))
                    {
                        toCancel.remove(player);
                        return;
                    }

                    AntiCheatBan antiCheatBan = new AntiCheatBan(player.getUniqueId().toString(), BanType.NORMAL_PERMANENT, "[KAC] You have been caught using " + check.getName(), "CONSOLE", new Date(Integer.MAX_VALUE));

                    API.getBanManager().addBan(antiCheatBan);
                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou're account has been permanently suspended \n&fReason&7: " + "KAC caught using " + check.getName() + "\n&fBy&7: &3" + "CONSOLE"));

                    API.sendBungeeMessage("&7&m----------------------------------------------------");
                    API.sendBungeeMessage(" ");
                    API.sendBungeeMessage("&7The player &3" + player.getName() + "&7 has been caught using &3" + check.getName() + "&7.");
                    API.sendBungeeMessage(" ");
                    API.sendBungeeMessage("&7&m----------------------------------------------------");

                }
            }, 15 * 20L);
        }
    }


    public void logCheat(Check check, Player player, String hoverabletext, String... identifiers)
    {
        String a = "";
        if (identifiers != null)
        {
            String[] arrayOfString;
            int j = (arrayOfString = identifiers).length;
            for (int i = 0; i < j; i++)
            {
                String b = arrayOfString[i];
                a = a + " (" + b + ")";
            }
        }

        if(!this.violationReset.containsKey(player.getUniqueId()))
        {
            this.violationReset.put(player.getUniqueId(), new HashMap<>());
        }
        if(!this.violationReset.get(player.getUniqueId()).containsKey(check))
        {
            this.violationReset.get(player.getUniqueId()).put(check, check.getViolationResetTime() + System.currentTimeMillis());

        }
        if(this.violationReset.get(player.getUniqueId()).get(check) < System.currentTimeMillis())
        {
            this.violations.get(player.getUniqueId()).put(check, 0);
            this.violationReset.get(player.getUniqueId()).put(check, check.getViolationResetTime() + System.currentTimeMillis());
        }

        if(!this.violations.containsKey(player.getUniqueId()))
        {
            this.violations.put(player.getUniqueId(), new HashMap<>());
            this.violations.get(player.getUniqueId()).put(check, 1);
        }

        if(!this.violations.get(player.getUniqueId()).containsKey(check))
            this.violations.get(player.getUniqueId()).put(check, 1);
        else
            this.violations.get(player.getUniqueId()).put(check, this.violations.get(player.getUniqueId()).get(check) + 1);


//        addViolation(player, check);
        FancyMessage message = new FancyMessage();
        message.text("[KAC]").color(ChatColor.GOLD)
                .then(player.getName()).color(ChatColor.WHITE)
                .formattedTooltip(new FancyMessage("Click to teleport to ").color(ChatColor.GOLD)
                        .then(player.getName()).color(ChatColor.AQUA)
                        .then(".").color(ChatColor.GOLD))
                .then(" might be using ").color(ChatColor.GRAY)
                .then(check.getName()).color(ChatColor.AQUA)
                .then("[").color(ChatColor.RED)
                .then(String.valueOf(this.violations.get(player.getUniqueId()).get(check))).color(ChatColor.WHITE)
                .then("VL]").color(ChatColor.RED)
                .then(".").color(ChatColor.GRAY);

        if (getViolations(player, check) % check.getViolationsToNotify() == 0)
        {
            for (Player staff : Bukkit.getOnlinePlayers())
            {
                User user = API.getUserManager().findByUniqueId(staff.getUniqueId());
                if (user.getGroup().getPermission() >= Group.TRAIL_MOD.getPermission())
                {
                    message.send(staff);
                }
            }
        }
        System.out.println("[" + player.getUniqueId().toString() + "] " + player.getName() + " failed " + check.getName() + a + ".");
        if ((getViolations(player, check) >= check.getMaxViolations()) && (check.isBannable()))
        {
            autoban(check, player);
        }
    }

    public LagUtils getLagUtils()
    {
        return lagUtils;
    }

    public Map<UUID, Map.Entry<Long, Vector>> getLastVelocity()
    {
        return lastVelocity;
    }

    public PacketHandler getPacketHandler()
    {
        return packetHandler;
    }

    public LinkedList<Player> getToCancel()
    {
        return toCancel;
    }

    public Settings getSettings()
    {
        return settings;
    }
}
