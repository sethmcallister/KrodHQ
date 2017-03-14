package xyz.sethy.core.framework.mute;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisClient;
import xyz.sethy.api.framework.mute.IMuteManager;
import xyz.sethy.api.framework.mute.Mute;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Seth on 22/01/2017.
 */
public class MuteManager implements IMuteManager
{
    private final RedisClient redisClient = new RedisClient("localhost");
    private final Gson gson;
    private String muteKey = "network.mutes.";

    public MuteManager()
    {
        this.gson = new Gson();
    }

    @Override
    public void addMute(Mute mute)
    {
        CoreMute coreMute = (CoreMute) mute;
        String muteJson = this.gson.toJson(coreMute);
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future set = asyncConnection.set(muteKey + mute.getTarget(), muteJson);
        asyncConnection.awaitAll(set);
        asyncConnection.close();
    }

    @Override
    public void removeMute(Mute mute)
    {

    }

    public void removeMute(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future delete = asyncConnection.set(muteKey + uuid.toString(), "null");
        asyncConnection.awaitAll(delete);
        asyncConnection.close();
    }

    @Override
    public Mute getMute(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future<String> muteJson = asyncConnection.get(muteKey + uuid);
        asyncConnection.awaitAll(muteJson);

        CoreMute coreMute;
        try
        {
            coreMute = this.gson.fromJson(muteJson.get(), CoreMute.class);
        }
        catch (InterruptedException | ExecutionException e)
        {
            coreMute = null;
            e.printStackTrace();
        }

        asyncConnection.close();

        if (coreMute != null && coreMute.getType() != null)
            return coreMute;
        else
            return null;
    }
}
