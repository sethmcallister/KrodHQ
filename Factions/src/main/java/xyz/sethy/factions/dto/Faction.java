package xyz.sethy.factions.dto;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.factions.Factions;
import xyz.sethy.factions.dto.claim.Claim;
import xyz.sethy.factions.handlers.dtr.DTRType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
    private final ArrayList<Claim> claims;
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
        this.claims = new ArrayList<>();
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
        this.claims = new ArrayList<>();
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
                return ChatColor.GOLD + "Road";
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

    public List<Claim> getClaims()
    {
        return claims;
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
        return this.claims.contains(claim);
    }

    public void getInformation(final Player sender)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&m--------------&7[" + this.getName(sender) + "&7]&3&m--------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR: &7" + getDTR() + " / " + getMaxDTR() + (isRaidable() ? "&c(RAIDABLE)" : "")));
        if (this.deathCooldown > System.currentTimeMillis())
        {
            final long till = Math.max(this.getRaidableCooldown(), this.getDeathCooldown());
            final int seconds = (int) (till - System.currentTimeMillis()) / 1000;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3DTR freeze: &7" + getConvertedTime(seconds)));
        }
        final String homestr = (this.home == null) ? "None" : (this.home.getBlockX() + ", " + this.home.getBlockY() + ", " + this.home.getBlockZ());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Faction home: &7" + homestr));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Balance &7$" + balance));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Members: &7" + getAllMembers().size() + " / 10"));


        if (!Factions.getInstance().isKitmap())
        {
            StringBuilder online = new StringBuilder();
            for (UUID playerUUID : getOnlineMembers())
            {
                Player player = Bukkit.getPlayer(playerUUID);

                User user = API.getUserManager().findByUniqueId(playerUUID);

                online.append(" &c");
                if (isLeader(player))
                    online.append("**");

                if (getCaptains().contains(player.getUniqueId()))
                    online.append("*");

                online.append(player.getName())
                        .append("&7[")
                        .append("&c")
                        .append(user.getHCFUser().getKills())
                        .append("&7]&7,");
            }

            String onlineString = online.toString();
            if (onlineString.endsWith(","))
            {
                onlineString = onlineString.substring(0, onlineString.length() - 1);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Members online(" + getOnlineMembers().size() + "):" + onlineString));

            StringBuilder offline = new StringBuilder();
            for (UUID playerUUID : getAllMembers())
            {
                if (!this.getOnlineMembers().contains(playerUUID))
                {
                    User user = API.getUserManager().getTempUser(playerUUID);

                    offline.append(" &c");
                    if (this.leader.equals(playerUUID))
                        offline.append("**");

                    if (this.captains.contains(playerUUID))
                        offline.append("*");

                    offline.append(user.getName())
                            .append("&7[")
                            .append("&c")
                            .append(user.getHCFUser().getKills())
                            .append("&7]&7,");
                }
            }
            String offlineString = offline.toString();
            if (offlineString.endsWith(","))
            {
                offlineString = offlineString.substring(0, offlineString.length() - 1);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Members offline(" + (getAllMembers().size() - getAllMembers().size()) + "):" + offlineString));
        }
        else
        {
            StringBuilder online = new StringBuilder();
            for (UUID playerUUID : getOnlineMembers())
            {
                Player player = Bukkit.getPlayer(playerUUID);
                KitmapUser kitmapUser = API.getUserManager().findKitmapByUniqueId(playerUUID);

                online.append(" &c");
                if (isLeader(player))
                    online.append("**");

                if (getCaptains().contains(player.getUniqueId()))
                    online.append("*");

                online.append(player.getName())
                        .append("&7[")
                        .append("&c")
                        .append(kitmapUser.getKills())
                        .append("&7]&7,");
            }

            String onlineString = online.toString();
            if (onlineString.endsWith(","))
            {
                onlineString = onlineString.substring(0, onlineString.length() - 1);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Members online(" + getOnlineMembers().size() + "):" + onlineString));

            StringBuilder offline = new StringBuilder();
            for (UUID playerUUID : getAllMembers())
            {
                if (!this.getOnlineMembers().contains(playerUUID))
                {
                    User user = API.getUserManager().getTempUser(playerUUID);
                    KitmapUser kitmapUser = API.getUserManager().findKitmapByUniqueId(playerUUID);

                    offline.append(" &c");
                    if (this.leader.equals(playerUUID))
                        offline.append("**");

                    if (this.captains.contains(playerUUID))
                        offline.append("*");

                    offline.append(user.getName())
                            .append("&7[")
                            .append("&c")
                            .append(kitmapUser.getKills())
                            .append("&7]&7,");
                }
            }
            String offlineString = offline.toString();
            if (offlineString.endsWith(","))
            {
                offlineString = offlineString.substring(0, offlineString.length() - 1);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Members offline(" + (getAllMembers().size() - getAllMembers().size()) + "):" + offlineString));
        }

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

    public void loadFromString(String string)
    {
        final String[] split2 = string.split("\n");
        for (final String line : split2)
        {
            final String identifier = line.substring(0, line.indexOf(58));
            final String[] lineParts = line.substring(line.indexOf(58)).split(",");
            if (identifier.equalsIgnoreCase("Leader"))
            {
                if (!lineParts[0].replace(":", "").equals("null"))
                {
                    String preUUID = lineParts[0].replace(":", "");
                    if (!isUUID(preUUID))
                        continue;

                    UUID uuid = UUID.fromString(preUUID);

                    this.setLeader(uuid);
                    this.getAllMembers().add(uuid);
                }
            }
            else if (identifier.equalsIgnoreCase("UUID"))
                this.uuid = UUID.randomUUID();
            else if (identifier.equalsIgnoreCase("Members"))
            {
                for (final String name : lineParts)
                {
                    if (name.length() >= 2 && !name.equalsIgnoreCase("null"))
                    {
                        String preUUID = name.replace(":", "");
                        if (!isUUID(preUUID))
                            continue;

                        UUID uuid = UUID.fromString(preUUID);

                        this.getAllMembers().add(uuid);
                        this.getMembers().add(uuid);
                    }
                }
            }
            else if (identifier.equalsIgnoreCase("Captains"))
            {
                for (final String name : lineParts)
                {
                    if (name.length() >= 2 && !name.equalsIgnoreCase("null"))
                    {
                        String preUUID = name.replace(":", "");
                        if (!isUUID(preUUID))
                            continue;

                        UUID uuid = UUID.fromString(preUUID);
                        this.getCaptains().add(uuid);
                        this.getAllMembers().add(uuid);
                    }
                }
            }
            else if (identifier.equalsIgnoreCase("Home"))
            {
                Location home = stringToLoc(lineParts[0].replace(":", ""));
                this.setHome(home);
            }
            else if (identifier.equalsIgnoreCase("DTR"))
            {
                if (lineParts[0].replace(":", "").equalsIgnoreCase("null"))
                {
                    this.setDtr(0.0);
                }
                else
                {
                    this.setDtr(Double.valueOf(lineParts[0].replace(":", "")));
                }

            }
            else if (identifier.equalsIgnoreCase("Balance"))
            {
                this.setBalance(Integer.valueOf(lineParts[0].replace(":", "")));
            }
            else if (identifier.equalsIgnoreCase("DeathCooldown"))
            {
                this.setDeathCooldown(Long.valueOf(lineParts[0].replace(":", "")));
            }
            else if (identifier.equalsIgnoreCase("RaidableCooldown"))
            {
                this.setRaidableCooldown(Long.valueOf(lineParts[0].replace(":", "")));
            }
            else if (identifier.equalsIgnoreCase("FriendlyName"))
            {
                this.setName(lineParts[0].replace(":", ""));
            }
            else if (identifier.equalsIgnoreCase("Claims"))
            {
                System.out.println(lineParts[0]);
                for (String claim : lineParts)
                {
                    claim = claim.replace("[", "").replace("]", "");
                    if (claim.contains(":"))
                    {
                        final String[] split = claim.split(":");
                        if (split.length != 0)
                        {
                            int x1 = 0;
                            int y1 = 0;
                            int z1 = 0;
                            int x2 = 0;
                            int y2 = 0;
                            int z2 = 0;
                            try
                            {
                                x1 = Integer.parseInt(split[1].trim());
                                y1 = Integer.parseInt(split[2].trim());
                                z1 = Integer.parseInt(split[3].trim());
                                x2 = Integer.parseInt(split[4].trim());
                                y2 = Integer.parseInt(split[5].trim());
                                z2 = Integer.parseInt(split[6].trim());
                            }
                            catch (NumberFormatException nfe)
                            {
                                System.out.println("Not a number.");
                            }
                            final String name2 = split[7].trim();
                            final String world = split[8].trim();
                            final Claim claimObj = new Claim(world, x1, y1, z1, x2, y2, z2);
                            claimObj.setName(name2);
                            this.getClaims().add(claimObj);
                            Factions.getInstance().getLandBoard().setFactionAt(claimObj, this);
                        }
                    }
                }
            }
        }
        this.needsSave = false;
    }

    public String locToString(Location l)
    {
        return new StringBuilder().append(l.getWorld().getName()).append("~")
                .append(l.getX()).append("~")
                .append(l.getY()).append("~")
                .append(l.getZ()).append("~")
                .append(l.getYaw()).append("~")
                .append(l.getPitch()).toString();
    }

    public Location stringToLoc(String s)
    {
        try
        {
            String[] parts = s.split("~", 6);
            if (parts.length != 6)
            {
                throw new IllegalArgumentException("Invalid location. It did not contain all the parts");
            }
            World w = Bukkit.getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            if (w == null)
            {
                throw new IllegalStateException("World cannot be null");
            }
            return new Location(w, x, y, z, yaw, pitch);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public Location parseLocation(final String[] args)
    {
        if (args.length != 6)
        {
            return null;
        }
        final World world = Bukkit.getWorld(args[0]);
        final double x = Double.parseDouble(args[1]);
        final double y = Double.parseDouble(args[2]);
        final double z = Double.parseDouble(args[3]);
        final float yaw = Float.parseFloat(args[4]);
        final float pitch = Float.parseFloat(args[5]);
        return new Location(world, x, y, z, yaw, pitch);
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

    private boolean isUUID(String uuid)
    {
        if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"))
            return true;

        return false;
    }
}