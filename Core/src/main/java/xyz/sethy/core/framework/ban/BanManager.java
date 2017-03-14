package xyz.sethy.core.framework.ban;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisClient;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.IBanManager;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sethm on 07/01/2017.
 */
public class BanManager implements IBanManager
{
    private final RedisClient redisClient = new RedisClient("localhost");
    private final Gson gson;
    private String banKey = "network.bans.";

    public BanManager()
    {
        this.gson = new Gson();
    }

    @Override
    public void addBan(Ban ban)
    {
        CoreBan coreBan = new CoreBan(ban.getTarget(), ban.getType(), ban.getReason(), ban.getBannedBy());
        String banJson = this.gson.toJson(coreBan);

        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future set = asyncConnection.set(banKey + coreBan.getTarget(), banJson);
        asyncConnection.awaitAll(set);
        asyncConnection.close();
    }

    @Override
    public void removeBan(Ban ban)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future delete = asyncConnection.set(ban + ban.getTarget(), "null");
        asyncConnection.awaitAll(delete);
        asyncConnection.close();
    }

    public void removeBan(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future delete = asyncConnection.set(banKey + uuid.toString(), "null");
        asyncConnection.awaitAll(delete);
        asyncConnection.close();
    }

    @Override
    public Ban getBan(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future<String> banJson = asyncConnection.get(banKey + uuid.toString());
        asyncConnection.awaitAll(banJson);

        CoreBan coreBan;
        try
        {
            coreBan = this.gson.fromJson(banJson.get(), CoreBan.class);
            System.out.println("ban json ->>>>>>" + banJson.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
            coreBan = null;
            e.printStackTrace();
        }

        asyncConnection.close();

        if (coreBan != null)
            return coreBan;
        else
            return null;
    }
}
