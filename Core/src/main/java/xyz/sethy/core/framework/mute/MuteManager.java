package xyz.sethy.core.framework.mute;

import redis.clients.jedis.Jedis;
import xyz.sethy.api.framework.mute.IMuteManager;
import xyz.sethy.api.framework.mute.Mute;

import java.util.UUID;

/**
 * Created by Seth on 22/01/2017.
 */
public class MuteManager implements IMuteManager
{
    private Jedis jedis;
    private String muteKey = "network.mutes.";

    public MuteManager()
    {
        this.jedis = new Jedis();
    }

    @Override
    public void addMute(Mute mute)
    {
        jedis.connect();
        CoreMute coreBan = new CoreMute(mute.getTarget(), mute.getType(), mute.getReason(), mute.getBannedBy());
        this.jedis.set(muteKey + mute.getTarget(), coreBan.saveToString());
        jedis.save();
    }

    @Override
    public void removeMute(Mute mute)
    {
        jedis.connect();
        this.jedis.del(muteKey + mute.getTarget());
        jedis.save();
    }

    @Override
    public Mute getMute(UUID uuid)
    {
        jedis.connect();
        CoreMute coreMute = new CoreMute();
        coreMute.loadFromString(jedis.get(muteKey + uuid.toString()));
        if (coreMute.getType() != null)
            return coreMute;
        else
            return null;
    }
}
