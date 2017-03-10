package xyz.sethy.api.framework.user.kitmap;

import java.util.UUID;

/**
 * Created by sethm on 08/01/2017.
 */
public interface KitmapUser
{
    UUID getUUID();

    int getKills();

    int getDeaths();

    double getBalance();

    void setKills(int kills);

    void setDeaths(int deaths);

    void setBalance(double balance);
}
