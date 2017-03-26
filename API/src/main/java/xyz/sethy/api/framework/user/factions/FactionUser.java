package xyz.sethy.api.framework.user.factions;

import java.util.UUID;

/**
 * Created by Seth on 26/03/2017.
 */
public interface FactionUser
{
    UUID getUUID();
    int getKills();
    int getDeaths();
    double getBalance();
    void setKills(int kills);
    void setDeaths(int deaths);
    void setBalance(double balance);
}
