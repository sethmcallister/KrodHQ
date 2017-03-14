package xyz.sethy.core.framework.user.hcf;

import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.core.framework.user.UserManager;

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
    private UUID uuid;
    private AtomicInteger kills;
    private AtomicInteger deaths;
    private AtomicInteger lives;
    private AtomicLong deathban;
    private AtomicReference<Double> balance;
    private AtomicLong pvpTimer;
    private AtomicReference<String> deathMessage;
    private AtomicBoolean joinedSinceSOTW;
    private AtomicBoolean redeemedRank;

    public CoreHCFUser(UUID uuid)
    {
        this.uuid = uuid;
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

    @Override
    public UUID getUUID()
    {
        return uuid;
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

    @Override
    public void forceSave()
    {
        UserManager userManager = (UserManager) API.getUserManager();
        userManager.unloadUser((User) this);
    }
}
