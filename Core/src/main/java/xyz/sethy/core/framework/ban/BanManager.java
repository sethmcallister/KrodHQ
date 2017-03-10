package xyz.sethy.core.framework.ban;

import redis.clients.jedis.Jedis;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.IBanManager;

import java.util.UUID;

/**
 * Created by sethm on 07/01/2017.
 */
public class BanManager implements IBanManager
{
    private Jedis jedis;
    private String banKey = "network.bans.";

    public BanManager()
    {
        this.jedis = new Jedis();
    }

    @Override
    public void addBan(Ban ban)
    {
        jedis.connect();
        CoreBan coreBan = new CoreBan(ban.getTarget(), ban.getType(), ban.getReason(), ban.getBannedBy());
        this.jedis.set(banKey + ban.getTarget(), coreBan.saveToString());
        jedis.save();
    }

    @Override
    public void removeBan(Ban ban)
    {
        jedis.connect();
        this.jedis.del(banKey + ban.getTarget());
        jedis.save();
    }

    @Override
    public Ban getBan(UUID uuid)
    {
        jedis.connect();
        CoreBan coreBan = new CoreBan();
        coreBan.loadFromString(jedis.get(banKey + uuid.toString()));
        if (coreBan.getType() != null)
            return coreBan;
        else
            return null;
    }
}
