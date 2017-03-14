package xyz.sethy.core.framework.user.kitmap;

import xyz.sethy.api.framework.user.kitmap.KitmapUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sethm on 08/01/2017.
 */
public class CoreKitmapUser implements KitmapUser
{
    private UUID uuid;
    private AtomicInteger kills;
    private AtomicInteger deaths;
    private AtomicReference<Double> balance;

    public CoreKitmapUser(UUID uuid)
    {
        this.uuid = uuid;
        this.kills = new AtomicInteger(0);
        this.deaths = new AtomicInteger(0);
        this.balance = new AtomicReference<>(0D);
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
    public void setBalance(double balance)
    {
        this.balance.set(balance);
    }
}
