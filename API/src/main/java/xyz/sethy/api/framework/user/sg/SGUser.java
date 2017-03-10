package xyz.sethy.api.framework.user.sg;

import xyz.sethy.api.framework.user.User;

import java.util.UUID;

public interface SGUser
{
    UUID getUUID();

    public abstract int getWins();

    public abstract int getLoses();

    public abstract int getKills();

    public abstract int getDeaths();

    public abstract int getElo();

    public abstract User getUser();

    public abstract void setWins(int paramInt);

    public abstract void setLoses(int paramInt);

    public abstract void setKills(int paramInt);

    public abstract void setDeaths(int paramInt);

    public abstract void setElo(int paramInt);
}
