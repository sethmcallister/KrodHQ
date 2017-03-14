package xyz.sethy.core.framework.user;

import com.google.gson.Gson;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisClient;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.api.events.UserLoggedInEvent;
import xyz.sethy.api.framework.user.IUserManager;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.core.Core;
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
    private final ConcurrentLinkedQueue<HCFUser> hcfUsers;
    private final ConcurrentLinkedQueue<SGUser> sgUsers;
    private final ConcurrentLinkedQueue<KitmapUser> kitmapUsers;

    private final RedisClient redisClient = new RedisClient("localhost");
    private final Gson gson;
    private final String userKey = "network.users.";
    private final String hcfKey = "network.hcf.";
    private final String kitmapKey = "network.kits.";

    public UserManager()
    {
        this.users = new ConcurrentLinkedQueue<>();
        this.hcfUsers = new ConcurrentLinkedQueue<>();
        this.sgUsers = new ConcurrentLinkedQueue<>();
        this.kitmapUsers = new ConcurrentLinkedQueue<>();
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

                    CoreHCFUser coreHCFUser = (CoreHCFUser) findHCFByUniqueId(user.getUniqueId());
                    String hcfJson = gson.toJson(coreHCFUser);
                    Future<String> hcfSet = asyncConnection.set(hcfKey + user.getUniqueId(), hcfJson);

                    CoreKitmapUser core = (CoreKitmapUser) findKitmapByUniqueId(user.getUniqueId());
                    if(core == null)
                        core = new CoreKitmapUser(user.getUniqueId());

                    String kitmapJson = gson.toJson(core);
                    Future<String> kitmapSet = asyncConnection.set(kitmapKey + user.getUniqueId(), kitmapJson);

                    System.out.println(kitmapJson);
                    asyncConnection.awaitAll(userSet, hcfSet, kitmapSet);
                }
                asyncConnection.close();

            }
        }.runTaskTimerAsynchronously(Core.getPlugin(), 10 * 20L, 10 * 20L);
    }

    @Override
    public User findByUniqueId(final UUID uuid)
    {
        return users.stream().filter(u -> u.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public HCFUser findHCFByUniqueId(UUID uuid)
    {
        return hcfUsers.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public SGUser findSGByUniqueId(UUID uuid)
    {
        return sgUsers.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public KitmapUser findKitmapByUniqueId(UUID uuid)
    {
        for(KitmapUser kitmapUser : kitmapUsers)
        {
            if(kitmapUser.getUUID().equals(uuid))
                return kitmapUser;
        }
        return null;
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
            hcfUser = null;
            e.printStackTrace();
        }
        asyncConnection.close();
        return hcfUser;
    }

    public void deleteUser(String uuid)
    {

    }

    @Override
    public void unloadUser(User user)
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();

        CoreUser coreUser = (CoreUser) user;
        String userJson = this.gson.toJson(coreUser);
        Future<String> userSet = asyncConnection.set(userKey + user.getUniqueId(), userJson);
        System.out.println(userJson);

        CoreHCFUser coreHCFUser = (CoreHCFUser) findHCFByUniqueId(user.getUniqueId());
        String hcfJson = this.gson.toJson(coreHCFUser);
        Future<String> hcfSet = asyncConnection.set(hcfKey + user.getUniqueId(), hcfJson);

        CoreKitmapUser core = (CoreKitmapUser) findKitmapByUniqueId(user.getUniqueId());
        if(core == null)
            core = new CoreKitmapUser(user.getUniqueId());

        String kitmapJson = this.gson.toJson(core);
        Future<String> kitmapSet = asyncConnection.set(kitmapKey + user.getUniqueId(), kitmapJson);

        System.out.println("------------");
        System.out.println(kitmapJson);
        asyncConnection.awaitAll(userSet, hcfSet, kitmapSet);
        this.users.remove(coreUser);
        this.hcfUsers.remove(coreHCFUser);
        this.kitmapUsers.remove(core);
        asyncConnection.close();
    }

    public CoreUser loadFromRedis(UUID uuid) throws ExecutionException, InterruptedException
    {
        RedisAsyncConnection<String, String> asyncConnection = redisClient.connectAsync();

        Future<String> userJson = asyncConnection.get(userKey + uuid.toString());
        Future<String> hcfJson = asyncConnection.get(hcfKey + uuid.toString());
        Future<String> kitmapJson = asyncConnection.get(kitmapKey + uuid.toString());
        asyncConnection.awaitAll(userJson, hcfJson, kitmapJson);

        CoreUser user = this.gson.fromJson(userJson.get(), CoreUser.class);
        CoreHCFUser hcfUser = this.gson.fromJson(hcfJson.get(), CoreHCFUser.class);
        CoreKitmapUser kitmapUser = this.gson.fromJson(kitmapJson.get(), CoreKitmapUser.class);

        if(user == null)
            user = new CoreUser(uuid);

        if(hcfUser == null)
            hcfUser = new CoreHCFUser(uuid);

        if(kitmapUser == null)
            kitmapUser = new CoreKitmapUser(uuid);

        this.users.add(user);
        this.hcfUsers.add(hcfUser);
        this.kitmapUsers.add(kitmapUser);

        System.out.println("User Json -> " + userJson.get());
        Bukkit.getPluginManager().callEvent(new UserLoggedInEvent(user, Bukkit.getPlayer(uuid)));
        asyncConnection.close();
        return user;
    }
}