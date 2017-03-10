package xyz.sethy.core.framework.user.kitmap;

import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sethm on 08/01/2017.
 */
public class CoreKitmapUser implements KitmapUser
{
    private AtomicInteger kills;
    private AtomicInteger deaths;
    private AtomicReference<Double> balance;

    private User user;

    public CoreKitmapUser(User user)
    {
        this.user = user;
        this.kills = new AtomicInteger(0);
        this.deaths = new AtomicInteger(0);
        this.balance = new AtomicReference<>(0D);
    }

    public String saveToString()
    {
        StringBuilder string = new StringBuilder();
        string.append("UUID:").append(this.user.getUniqueId().toString()).append("\n");
        string.append("Kills:").append(this.kills.get()).append("\n");
        string.append("Deaths:").append(this.deaths.get()).append("\n");
        string.append("Balance:").append(this.getBalance()).append("\n");
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
            else if (identifier.equalsIgnoreCase("Balance"))
                this.balance.set(Double.valueOf(lineParts[0].replace(":", "")));
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

    public User getUser()
    {
        return user;
    }
}
