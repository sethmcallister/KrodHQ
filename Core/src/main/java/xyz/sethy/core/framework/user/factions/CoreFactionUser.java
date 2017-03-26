package xyz.sethy.core.framework.user.factions;

import xyz.sethy.api.framework.user.factions.FactionUser;

import java.util.UUID;

/**
 * Created by Seth on 26/03/2017.
 */
public class CoreFactionUser implements FactionUser
{
    private UUID uuid;
    private Integer kills;
    private Integer deaths;
    private Double balance;

    public CoreFactionUser(UUID uuid)
    {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.balance = 0D;
    }

    @Override
    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public int getKills()
    {
        return kills;
    }

    @Override
    public int getDeaths()
    {
        return deaths;
    }

    @Override
    public double getBalance()
    {
        return balance;
    }

    @Override
    public void setKills(int kills)
    {
        this.kills = kills;
    }

    @Override
    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    @Override
    public void setBalance(double balance)
    {
        this.balance = balance;
    }
}
