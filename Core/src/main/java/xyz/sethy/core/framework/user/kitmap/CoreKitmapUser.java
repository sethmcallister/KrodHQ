package xyz.sethy.core.framework.user.kitmap;

import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.core.framework.user.UserManager;

import java.util.UUID;

/**
 * Created by sethm on 08/01/2017.
 */
public class CoreKitmapUser implements KitmapUser
{
    private UUID uuid;
    private Integer kills;
    private Integer deaths;
    private Double balance;
    private Integer killStreak;

    public CoreKitmapUser(UUID uuid)
    {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.balance = 0.0;
        this.killStreak = 0;
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
        forceSave();
    }

    @Override
    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
        forceSave();
    }

    @Override
    public void setBalance(double balance)
    {
        this.balance = balance;
        forceSave();
    }

    @Override
    public Integer getCurrentKillStreak()
    {
        return killStreak;
    }

    @Override
    public void setKillStreak(int killStreak)
    {
        this.killStreak = killStreak;
        forceSave();
    }

    private void forceSave()
    {
        UserManager userManager = (UserManager) API.getUserManager();
        userManager.saveKits(this);
    }
}
