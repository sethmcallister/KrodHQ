package xyz.sethy.core.framework.user.sg;

import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.sg.SGUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 22/12/2016.
 */
public class CoreSGUser implements SGUser
{
    private final AtomicInteger wins;
    private final AtomicInteger loses;
    private final AtomicInteger kills;
    private final AtomicInteger deaths;

    private final AtomicInteger elo;

    private User user;

    public CoreSGUser(User user)
    {
        this.wins = new AtomicInteger(0);
        this.loses = new AtomicInteger(0);
        this.kills = new AtomicInteger(0);
        this.deaths = new AtomicInteger(0);
        this.elo = new AtomicInteger(0);
        this.user = user;
    }

    public String saveToString()
    {
        StringBuilder string = new StringBuilder();
        string.append("UUID:").append(this.user.getUniqueId().toString()).append("\n");
        string.append("Wins:").append(this.getWins()).append("\n");
        string.append("Loses:").append(this.getLoses()).append("\n");
        string.append("Kills:").append(this.getKills()).append("\n");
        string.append("Deaths:").append(this.getDeaths()).append("\n");
        string.append("ELO:").append(this.getElo()).append("\n");
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
            if (identifier.equalsIgnoreCase("Wins:"))
                this.wins.set(Integer.valueOf(lineParts[0]));
            else if (identifier.equalsIgnoreCase("Loses:"))
                this.loses.set(Integer.valueOf(lineParts[0]));
            else if (identifier.equalsIgnoreCase("Kills:"))
                this.kills.set(Integer.valueOf(lineParts[0]));
            else if (identifier.equalsIgnoreCase("Deaths:"))
                this.deaths.set(Integer.valueOf(lineParts[0]));
            else if (identifier.equalsIgnoreCase("ELO:"))
                this.elo.set(Integer.valueOf(lineParts[0]));
        }
    }

    @Override
    public UUID getUUID()
    {
        return user.getUniqueId();
    }

    @Override
    public int getWins()
    {
        return wins.get();
    }

    @Override
    public int getLoses()
    {
        return loses.get();
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
    public int getElo()
    {
        return elo.get();
    }

    @Override
    public void setWins(int wins)
    {
        this.wins.set(wins);
    }

    @Override
    public void setLoses(int loses)
    {
        this.loses.set(loses);
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
    public void setElo(int elo)
    {
        this.elo.set(elo);
    }

    @Override
    public User getUser()
    {
        return user;
    }
}
