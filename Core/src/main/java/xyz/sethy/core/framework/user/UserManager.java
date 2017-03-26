package xyz.sethy.core.framework.user;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisClient;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.events.UserLoggedInEvent;
import xyz.sethy.api.framework.user.IUserManager;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.factions.FactionUser;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.core.Core;
import xyz.sethy.core.framework.user.factions.CoreFactionUser;
import xyz.sethy.core.framework.user.hcf.CoreHCFUser;
import xyz.sethy.core.framework.user.kitmap.CoreKitmapUser;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sethm on 04/12/2016.
 */
public class UserManager implements IUserManager
{
    private final ConcurrentLinkedQueue<User> users;
    private final ConcurrentLinkedQueue<SGUser> sgUsers;
    private final ConcurrentLinkedQueue<KitmapUser> kitmapUsers;
    private final ConcurrentLinkedQueue<FactionUser> factionUsers;

    private final RedisClient redisClient = new RedisClient("localhost");
    private final Gson gson;
    private final String userKey = "network.users.";
    private final String hcfKey = "network.hcf.";
    private final String kitmapKey = "network.kits.";
    private final String factionKey = "network.reg_factions.";

    public UserManager()
    {
        this.users = new ConcurrentLinkedQueue<>();
        this.sgUsers = new ConcurrentLinkedQueue<>();
        this.kitmapUsers = new ConcurrentLinkedQueue<>();
        this.factionUsers = new ConcurrentLinkedQueue<>();
        this.gson = new Gson();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
                for(User user : users)
                {
                    CoreUser coreUser = (CoreUser) user;
                    String userJson = gson.toJson(coreUser);
                    Future<String> userSet = asyncConnection.set(userKey + user.getUniqueId(), userJson);
                    System.out.println(userJson);
                    asyncConnection.awaitAll(userSet);
                }
                asyncConnection.close();

            }
        }.runTaskTimerAsynchronously(Core.getPlugin(), 60 * 20L, 60 * 20L);
    }

    @Override
    public User findByUniqueId(final UUID uuid)
    {
        return users.stream().filter(u -> u.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public HCFUser findHCFByUniqueId(UUID uuid)
    {
        return getTempHCFUser(uuid);
    }

    @Override
    public SGUser findSGByUniqueId(UUID uuid)
    {
        return sgUsers.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public KitmapUser findKitmapByUniqueId(UUID uuid)
    {
        return kitmapUsers.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }


    @Override
    public List<User> getAllUsers()
    {
        return (List<User>) this.users;
    }

    @Override
    public void createUser(User user)
    {
        this.users.add(user);
    }

    @Override
    public void loadUser(User user)
    {
        this.users.add(user);
    }

    @Override
    public User getTempUser(UUID uuid)
    {
        try
        {
            return loadFromRedis(uuid);
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HCFUser getTempHCFUser(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future<String> hcfJson = asyncConnection.get(hcfKey + uuid.toString());
        asyncConnection.awaitAll(hcfJson);
        CoreHCFUser hcfUser;
        try
        {
            hcfUser = this.gson.fromJson(hcfJson.get(), CoreHCFUser.class);
        }
        catch (InterruptedException | ExecutionException e)
        {
            hcfUser = new CoreHCFUser(uuid);
            e.printStackTrace();
        }
        asyncConnection.close();
        return hcfUser;
    }

    @Override
    public KitmapUser getTempKitsUser(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future<String> hcfJson = asyncConnection.get(kitmapKey + uuid.toString());
        asyncConnection.awaitAll(hcfJson);
        CoreKitmapUser hcfUser;
        try
        {
            hcfUser = this.gson.fromJson(hcfJson.get(), CoreKitmapUser.class);
        }
        catch (InterruptedException | ExecutionException e)
        {
            hcfUser = new CoreKitmapUser(uuid);
            e.printStackTrace();
        }
        asyncConnection.close();
        return hcfUser;
    }

    @Override
    public FactionUser getFactionUser(UUID uuid)
    {
        return factionUsers.stream().filter(factionUser -> factionUser.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public FactionUser getTempFactionUser(UUID uuid)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        Future<String> hcfJson = asyncConnection.get(factionKey + uuid.toString());
        asyncConnection.awaitAll(hcfJson);
        CoreFactionUser hcfUser;
        try
        {
            hcfUser = this.gson.fromJson(hcfJson.get(), CoreFactionUser.class);
        }
        catch (InterruptedException | ExecutionException e)
        {
            hcfUser = new CoreFactionUser(uuid);
            e.printStackTrace();
        }
        asyncConnection.close();
        return hcfUser;
    }

    public void deleteUser(String uuid)
    {

    }

    public void saveHCF(HCFUser hcfUser)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        CoreHCFUser coreHCFUser = (CoreHCFUser) hcfUser;
        String hcfJson = this.gson.toJson(coreHCFUser);
        Future<String> hcfSet = asyncConnection.set(hcfKey + hcfUser.getUUID(), hcfJson);
        asyncConnection.awaitAll(hcfSet);
        asyncConnection.close();
    }

    public void saveKits(KitmapUser hcfUser)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();
        CoreKitmapUser coreHCFUser = (CoreKitmapUser) hcfUser;
        String hcfJson = this.gson.toJson(coreHCFUser);
        Future<String> hcfSet = asyncConnection.set(kitmapKey + hcfUser.getUUID(), hcfJson);
        asyncConnection.awaitAll(hcfSet);
        asyncConnection.close();
    }

    @Override
    public void unloadUser(User user)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();

        CoreUser coreUser = (CoreUser) user;
        String userJson = this.gson.toJson(coreUser);
        Future<String> userSet = asyncConnection.set(userKey + user.getUniqueId(), userJson);
        System.out.println(userJson);

        if(Core.getInstance().isKitmap())
        {
            CoreKitmapUser coreKitmapUser = (CoreKitmapUser) findKitmapByUniqueId(user.getUniqueId());
            String kitmapJson = this.gson.toJson(coreKitmapUser);
            Future<String> kitsJson = asyncConnection.set(kitmapKey + user.getUniqueId().toString(), kitmapJson);
            asyncConnection.awaitAll(kitsJson);
            this.kitmapUsers.remove(coreKitmapUser);
        }

        asyncConnection.awaitAll(userSet);
        this.users.remove(coreUser);
        asyncConnection.close();
    }

    public CoreUser loadFromRedis(UUID uuid) throws ExecutionException, InterruptedException
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();

        Future<String> userJson = asyncConnection.get(userKey + uuid.toString());
        if(Core.getInstance().isKitmap())
        {
            Future<String> kitsJson = asyncConnection.get(kitmapKey + uuid.toString());
            asyncConnection.awaitAll(kitsJson);
            CoreKitmapUser coreKitmapUser = this.gson.fromJson(kitsJson.get(), CoreKitmapUser.class);
            if(coreKitmapUser == null)
                coreKitmapUser = new CoreKitmapUser(uuid);

            this.kitmapUsers.add(coreKitmapUser);
        }
        asyncConnection.awaitAll(userJson);

        CoreUser user = this.gson.fromJson(userJson.get(), CoreUser.class);

        if(user == null)
            user = new CoreUser(uuid);

        this.users.add(user);
        Bukkit.getPluginManager().callEvent(new UserLoggedInEvent(user, Bukkit.getPlayer(uuid)));
        asyncConnection.close();
        return user;
    }

    public ConcurrentLinkedQueue<FactionUser> getFactionUsers()
    {
        return factionUsers;
    }
}