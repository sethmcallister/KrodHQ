package xyz.sethy.factions.dto;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.claim.Claim;
import xyz.sethy.factions.handlers.dtr.DTRType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;

/**
 * Created by sethm on 26/11/2016.
 */
public class Faction
{
    private UUID uuid;
    private String name;
    private UUID leader;
    private final ConcurrentSkipListSet<UUID> captains;
    private final ConcurrentSkipListSet<UUID> members;
    private final ConcurrentSkipListSet<UUID> allMembers;
    private final ConcurrentSkipListSet<UUID> onlineMembers;
    private final ConcurrentSkipListSet<UUID> invites;
    private Claim claim;
    private boolean needsSave;
    private Double dtr;
    private Double maxDtr;
    private long raidableCooldown;
    private long deathCooldown;

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }

    private Integer balance;

    public void setHome(Location home)
    {
        this.home = home;
    }

    private Location home;

    public Faction(final String name, final UUID leader)
    {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.leader = leader;
        this.captains = new ConcurrentSkipListSet<>();
        this.members = new ConcurrentSkipListSet<>();
        this.allMembers = new ConcurrentSkipListSet<>();
        this.allMembers.add(leader);
        this.onlineMembers = new ConcurrentSkipListSet<>();
        this.onlineMembers.add(leader);
        this.invites = new ConcurrentSkipListSet<>();
        this.claim = null;
        this.balance = 0;
        this.dtr = 1.01;
        this.maxDtr = 1.01;
        this.raidableCooldown = 0L;
        this.deathCooldown = 0L;
    }

    public Faction()
    {
        this.captains = new ConcurrentSkipListSet<>();
        this.members = new ConcurrentSkipListSet<>();
        this.allMembers = new ConcurrentSkipListSet<>();
        this.onlineMembers = new ConcurrentSkipListSet<>();
        this.invites = new ConcurrentSkipListSet<>();
        this.claim = null;
        Bukkit.getLogger().log(Level.INFO, "Loading from redis");
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    public void setNeedsSave(boolean needsSave)
    {
        this.needsSave = needsSave;
    }

    public void setMaxDtr(Double maxDtr)
    {
        this.maxDtr = maxDtr;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public double getMaxDTR()
    {
        return Factions.getInstance().getDtrHandler().getMaxDTR(this.getAllMembers().size());
    }

    public String getName(final Player player)
    {
        if (this.leader == null)
        {
            if (this.hasDTRBitmask(DTRType.SAFEZONE))
            {
                switch (player.getWorld().getEnvironment())
                {
                    case NETHER:
                        return ChatColor.GREEN + "Nether Spawn";
                    case THE_END:
                        return ChatColor.GREEN + "End Spawn";
                    default:
                        return ChatColor.GREEN + "Spawn";
                }
            }
            else if (this.hasDTRBitmask(DTRType.ROAD))
                return ChatColor.GOLD + this.name.replace("Road", "") + " Road";
            else if (this.hasDTRBitmask(DTRType.KOTH))
                return ChatColor.GOLD + this.name + " KoTH";
        }
        if (this.allMembers.contains(player.getUniqueId()))
            return ChatColor.GREEN + this.name;

        return ChatColor.RED + this.name;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean hasDTRBitmask(final DTRType bitmaskType)
    {
        if (this.getLeader() != null)
            return false;
        final int dtrInt = (int) Math.floor(this.dtr);
        return (dtrInt & bitmaskType.getBitmask()) == bitmaskType.getBitmask();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public UUID getLeader()
    {
        return leader;
    }

    public void setLeader(UUID leader)
    {
        this.leader = leader;
    }

    public ConcurrentSkipListSet<UUID> getCaptains()
    {
        return captains;
    }

    public ConcurrentSkipListSet<UUID> getMembers()
    {
        return members;
    }

    public ConcurrentSkipListSet<UUID> getAllMembers()
    {
        return allMembers;
    }

    public boolean needsSave()
    {
        return needsSave;
    }

    public void flagSave()
    {
        this.needsSave = true;
    }

    public Double getDTR()
    {
        return dtr;
    }

    public void setDtr(Double dtr)
    {
        this.dtr = dtr;
    }

    public Double getMax()
    {
        return maxDtr;
    }

    public Integer getBalance()
    {
        return balance;
    }

    public Claim getClaims()
    {
        return claim;
    }

    public boolean isLeader(Player player)
    {
        return player.getUniqueId().equals(this.leader);
    }

    public boolean isCaptain(Player player)
    {
        return (this.captains.contains(player.getUniqueId()) || leader.equals(player.getUniqueId()));
    }

    public boolean isRaidable()
    {
        return this.dtr <= 0.0;
    }

    public boolean ownsClaim(final Claim claim)
    {
        if(this.claim == null)
            return false;

        return this.claim == claim;
    }

    public void getInformation(final Player sender)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9" + getName() + "&7[" + onlineMembers.size() + "/" + allMembers.size() + "]" + "&3 &eHome: &f" +
                (home == null ? "None" : home.getX() + ", " + home.getZ())));
        String leader;
        if(Bukkit.getPlayer(getLeader()) != null)
            leader = "4" + Bukkit.getPlayer(getLeader()).getName() + "&e[&a" + API.getUserManager().getTempHCFUser(getLeader()).getKills() + "&e]";
        else
        {
            if(Factions.getInstance().isKitmap())
                leader = "&f" + Bukkit.getOfflinePlayer(getLeader()).getName() + "&e[&a" + API.getUserManager().getTempHCFUser(getLeader()).getKills() + "&e]";
            else
            {
                if(API.getUserManager().getTempHCFUser(getLeader()).deathbanTime() > System.currentTimeMillis())
                    leader = "&c" + Bukkit.getOfflinePlayer(getLeader()).getName() + "&e[&a" + API.getUserManager().getTempHCFUser(getLeader()).getKills() + "&e]";
                else
                    leader = "&f" + Bukkit.getOfflinePlayer(getLeader()).getName() + "&e[&a" + API.getUserManager().getTempHCFUser(getLeader()).getKills() + "&e]";
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7Leader: &f" + leader));

        if(getCaptains().size() > 0)
        {
            StringBuilder captains = new StringBuilder();
            for(UUID uuid : getCaptains())
            {
                if(Bukkit.getPlayer(uuid) != null)
                {
                    if(Factions.getInstance().isKitmap())
                        captains.append("&a").append(Bukkit.getPlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempKitsUser(uuid).getKills()).append("&e]");
                    else
                        captains.append("&a").append(Bukkit.getPlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempHCFUser(uuid).getKills()).append("&e]");
                }
                else
                {
                    if(Factions.getInstance().isKitmap())
                        captains.append("&f").append(Bukkit.getOfflinePlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempKitsUser(uuid).getKills()).append("&e]");
                    else
                    {
                        if(API.getUserManager().getTempHCFUser(uuid).deathbanTime() > System.currentTimeMillis())
                            captains.append("&c").append(Bukkit.getOfflinePlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempHCFUser(uuid).getKills()).append("&e]");
                        else
                            captains.append("&f").append(Bukkit.getOfflinePlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempHCFUser(uuid).getKills()).append("&e]");
                    }
                }
            }

            if (captains.toString().endsWith(","))
            {
                captains.append(captains.substring(0, captains.length() - 1));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7Captains: " + captains.toString()));
        }

        if(getMembers().size() > 0)
        {
            StringBuilder members = new StringBuilder();
            for(UUID uuid : getMembers())
            {
                if(Bukkit.getPlayer(uuid) != null)
                {
                    if(Factions.getInstance().isKitmap())
                        members.append("&a").append(Bukkit.getPlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().findKitmapByUniqueId(uuid).getKills()).append("&e]");
                    else
                        members.append("&a").append(Bukkit.getPlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().findHCFByUniqueId(uuid).getKills()).append("&e]");
                }
                else
                {
                    if(API.getUserManager().getTempHCFUser(uuid).deathbanTime() > System.currentTimeMillis())
                        members.append("&c").append(Bukkit.getOfflinePlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempHCFUser(uuid).getKills()).append("&e]");
                    else
                        members.append("&f").append(Bukkit.getOfflinePlayer(uuid).getName()).append("&e[&a").append(API.getUserManager().getTempHCFUser(uuid).getKills()).append("&e]");
                }
            }

            if (members.toString().endsWith(","))
            {
                members.append(members.substring(0, members.length() - 1));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7Members: " + members.toString()));
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7Balance: &9$" + balance));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7Deaths Until Raidable: &a" + dtr));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m----------------------------------------------------"));
    }


    public ConcurrentSkipListSet<UUID> getOnlineMembers()
    {
        return this.onlineMembers;
    }

    public ConcurrentSkipListSet<UUID> getInvites()
    {
        return invites;
    }

    public String saveString(final boolean toJedis)
    {
        if (toJedis)
            this.needsSave = false;

        final StringBuilder factionString = new StringBuilder();
        final StringBuilder members1 = new StringBuilder();
        final StringBuilder captains1 = new StringBuilder();
        final StringBuilder invites1 = new StringBuilder();
        final Location homeLoc = this.getHome();

        for (final UUID member : this.getMembers())
            members1.append(member.toString()).append(", ");
        if (members1.length() > 2)
            members1.setLength(members1.length() - 2);

        for (final UUID captain : this.getCaptains())
            captains1.append(captain.toString()).append(", ");
        if (captains1.length() > 2)
            captains1.setLength(captains1.length() - 2);

        for (final UUID invite : this.getInvites())
            invites1.append(invite.toString()).append(", ");
        if (invites1.length() > 2)
            invites1.setLength(invites1.length() - 2);

        factionString.append("UUID:").append(this.getUUID().toString()).append("\n");
        factionString.append("Leader:").append(this.getLeader()).append('\n');
        factionString.append("Captains:").append(captains1.toString()).append('\n');
        factionString.append("Members:").append(members1.toString()).append('\n');
        factionString.append("Invited:").append(invites1.toString()).append('\n');
        factionString.append("Claims:").append(this.getClaims().toString()).append('\n');
        factionString.append("DTR:").append(this.getDTR()).append('\n');
        factionString.append("Balance:").append(this.getBalance()).append('\n');
        factionString.append("DeathCooldown:").append(this.getDeathCooldown()).append('\n');
        factionString.append("RaidableCooldown:").append(this.getRaidableCooldown()).append('\n');
        factionString.append("FriendlyName:").append(this.name).append('\n');
        if (homeLoc != null)
            factionString.append("Home:").append(locToString(homeLoc)).append('\n');

        return factionString.toString();
    }

    private String locToString(Location l)
    {
        return new StringBuilder().append(l.getWorld().getName()).append("~")
                .append(l.getX()).append("~")
                .append(l.getY()).append("~")
                .append(l.getZ()).append("~")
                .append(l.getYaw()).append("~")
                .append(l.getPitch()).toString();
    }

    public Location getHome()
    {
        return home;
    }

    public long getRaidableCooldown()
    {
        return raidableCooldown;
    }

    public long getDeathCooldown()
    {
        return deathCooldown;
    }

    public void setDeathCooldown(long deathCooldown)
    {
        this.deathCooldown = deathCooldown;
    }

    public void setRaidableCooldown(long raidableCooldown)
    {
        this.raidableCooldown = raidableCooldown;
    }

    public BigDecimal getDTRIncrement()
    {
        return this.getDTRIncrement(this.getOnlineMembers().size());
    }

    public BigDecimal getDTRIncrement(final int playersOnline)
    {
        final BigDecimal dtrPerHour = BigDecimal.valueOf(Factions.getInstance().getDtrHandler().getBaseDTRIncrement(this.getAllMembers().size())).multiply(BigDecimal.valueOf(playersOnline));
        return dtrPerHour.divide(BigDecimal.valueOf(60), 5, RoundingMode.HALF_DOWN);
    }

    private String getConvertedTime(long i)
    {
        i = Math.abs(i);
        final int hours = (int) Math.floor(i / 3600L);
        final int remainder = (int) (i % 3600L);
        final int minutes = remainder / 60;
        final int seconds = remainder % 60;
        if (seconds == 0 && minutes == 0)
        {
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + "0 seconds";
        }
        if (minutes == 0)
        {
            if (seconds == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
        }
        else if (seconds == 0)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
        }
        else if (seconds == 1)
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
        }
        else
        {
            if (minutes == 1)
            {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            final String toReturn = String.format("%sm %ss", minutes, seconds);
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + " " + toReturn;
        }
    }

    public void setClaim(Claim claim)
    {
        this.claim = claim;
    }

}