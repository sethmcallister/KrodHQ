package xyz.sethy.core.framework.user.hcf;

import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sethm on 24/12/2016.
 */
public class CoreHCFUser implements HCFUser
{
    private AtomicInteger kills;
    private AtomicInteger deaths;
    private AtomicInteger lives;
    private AtomicLong deathban;
    private AtomicReference<Double> balance;
    private AtomicLong pvpTimer;
    private AtomicReference<String> deathMessage;
    private AtomicBoolean joinedSinceSOTW;
    private AtomicBoolean redeemedRank;

    private User user;

    public CoreHCFUser(User user)
    {
        this.user = user;
        this.kills = new AtomicInteger(0);
        this.deaths = new AtomicInteger(0);
        this.lives = new AtomicInteger(0);
        this.deathban = new AtomicLong(0);
        this.balance = new AtomicReference<>(0D);
        this.pvpTimer = new AtomicLong();
        this.deathMessage = new AtomicReference<>();
        this.joinedSinceSOTW = new AtomicBoolean();
        this.redeemedRank = new AtomicBoolean();
    }

    public String saveToString()
    {
        StringBuilder string = new StringBuilder();
        string.append("UUID:").append(this.user.getUniqueId().toString()).append("\n");
        string.append("Kills:").append(this.getKills()).append("\n");
        string.append("Deaths:").append(this.getDeaths()).append("\n");
        string.append("Lives:").append(this.getLives()).append("\n");
        string.append("Deathban:").append(this.deathbanTime()).append("\n");
        string.append("Balance:").append(balance.get()).append("\n");
        string.append("PvPTimer:").append(this.getPvPTimer()).append("\n");
        string.append("DeathMessage:").append(this.getDeathbanMessage()).append("\n");
        string.append("JoinedSinceSOTW:").append(this.hasJoinedSinceSOTW()).append("\n");
        string.append("RedeemedRank:").append(this.redeemedRank.get()).append("\n");
        return string.toString();
    }

    public void loadFromString(String string)
    {
        if (string == null)
            return;

        final String[] strings = string.split("\n");
        for (String line : strings)
        {
            final String identifier = line.substring(0, line.indexOf(58));
            final String[] lineParts = line.substring(line.indexOf(58)).split(",");
            if (identifier.equalsIgnoreCase("Kills"))
                this.kills.set(Integer.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("Deaths"))
                this.deaths.set(Integer.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("Lives"))
                this.lives.set(Integer.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("Deathban"))
                this.deathban.set(Long.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("Balance"))
                this.balance.set(Double.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("PvPTimer"))
                this.pvpTimer.set(Long.valueOf(lineParts[0].replace(":", "")));
            else if (identifier.equalsIgnoreCase("DeathMessage"))
                this.deathMessage.set(lineParts[0].replace(":", ""));
            else if (identifier.equalsIgnoreCase("JoinedSinceSOTW"))
                this.joinedSinceSOTW.set(Boolean.valueOf(lineParts[0].replace(":", "")));
            else if(identifier.equalsIgnoreCase("RedeemedRank"))
                this.redeemedRank.set(Boolean.valueOf(lineParts[0].replace(":", "")));
        }
    }

    @Override
    public UUID getUUID()
    {
        return user.getUniqueId();
    }

    @Override
    public int getKills()
    {
        return kills.get();
    }

    @Override
    public int getDeaths()
    {
        return deaths.get();
    }

    @Override
    public int getLives()
    {
        return lives.get();
    }

    @Override
    public long deathbanTime()
    {
        return deathban.get();
    }

    @Override
    public double getBalance()
    {
        return balance.get();
    }

    @Override
    public void setKills(int kills)
    {
        this.kills.set(kills);
    }

    @Override
    public void setDeaths(int deaths)
    {
        this.deaths.set(deaths);
    }

    @Override
    public void setLives(int lives)
    {
        this.lives.set(lives);
    }

    @Override
    public void setDeathbanTime(long time)
    {
        this.deathban.set(time);
    }

    @Override
    public void setBalance(double balance)
    {
        this.balance.set(balance);
    }

    @Override
    public long getPvPTimer()
    {
        return pvpTimer.get();
    }

    @Override
    public void setPvPTimer(long time)
    {
        this.pvpTimer.set(time);
    }

    @Override
    public String getDeathbanMessage()
    {
        return deathMessage.get();
    }

    @Override
    public void setDeathbanMessage(String message)
    {
        deathMessage.set(message);
    }

    @Override
    public boolean hasJoinedSinceSOTW()
    {
        return joinedSinceSOTW.get();
    }

    @Override
    public void setJoinedSinceSOTW(boolean result)
    {
        joinedSinceSOTW.set(result);
    }

    @Override
    public boolean hasRedeemedRank()
    {
        return redeemedRank.get();
    }

    @Override
    public void setRedeemedRank(boolean result)
    {
        this.redeemedRank.set(result);
    }

    public User getUser()
    {
        return user;
    }
}
