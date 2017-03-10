package xyz.sethy.api.framework.user.hcf;

import java.util.UUID;

/**
 * Created by sethm on 24/12/2016.
 */
public interface HCFUser
{
    UUID getUUID();

    int getKills();

    int getDeaths();

    int getLives();

    long deathbanTime();

    double getBalance();

    void setKills(int kills);

    void setDeaths(int deaths);

    void setLives(int lives);

    void setDeathbanTime(long time);

    void setBalance(double balance);

    long getPvPTimer();

    void setPvPTimer(long time);

    String getDeathbanMessage();
    void setDeathbanMessage(String message);

    boolean hasJoinedSinceSOTW();
    void setJoinedSinceSOTW(boolean result);

    boolean hasRedeemedRank();
    void setRedeemedRank(boolean result);
}
