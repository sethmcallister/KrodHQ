package xyz.sethy.core.framework.user;

import com.google.common.collect.Lists;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import xyz.sethy.api.framework.user.IUserManager;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.core.framework.user.hcf.CoreHCFUser;
import xyz.sethy.core.framework.user.kitmap.CoreKitmapUser;
import xyz.sethy.core.framework.user.sg.CoreSGUser;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by sethm on 04/12/2016.
 */
public class UserManager implements IUserManager
{
    private final List<User> users;
    private final List<HCFUser> hcfUsers;
    private final List<SGUser> sgUsers;
    private final List<KitmapUser> kitmapUsers;
    private Jedis jedis;

    public UserManager()
    {
        this.users = Lists.newArrayList();
        this.hcfUsers = Lists.newArrayList();
        this.sgUsers = Lists.newArrayList();
        this.kitmapUsers = Lists.newArrayList();
        this.jedis = new Jedis();
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
        return kitmapUsers.stream().filter(u -> u.getUUID().equals(uuid)).findAny().orElse(null);
    }


    @Override
    public List<User> getAllUsers()
    {
        return Collections.unmodifiableList(users);
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
        return loadFromRedis(uuid);
    }

    public void deleteUser(String uuid)
    {
        Pipeline pipeline = jedis.pipelined();
        pipeline.del("network.users." + uuid);
        pipeline.del("network.sg.users." + uuid);
        pipeline.del("network.hcf.users." + uuid);
        pipeline.del("network.kitmap.users." + uuid);
    }

    @Override
    public void unloadUser(User user)
    {
        jedis.connect();
        CoreUser coreUser = (CoreUser) user;
        jedis.set("network.users." + user.getUniqueId().toString(), coreUser.saveToString());

        CoreSGUser coreSGUser = (CoreSGUser) findSGByUniqueId(user.getUniqueId());
        jedis.set("network.sg.users." + user.getUniqueId().toString(), coreSGUser.saveToString());

        CoreHCFUser coreHCFUser = (CoreHCFUser) findHCFByUniqueId(user.getUniqueId());
        jedis.set("network.hcf.users." + user.getUniqueId().toString(), coreHCFUser.saveToString());

        CoreKitmapUser coreKitmapUser = (CoreKitmapUser) findKitmapByUniqueId(user.getUniqueId());
        jedis.set("network.kitmap.users." + user.getUniqueId().toString(), coreKitmapUser.saveToString());

        this.users.remove(user);
        this.hcfUsers.remove(coreHCFUser);
        this.sgUsers.remove(coreSGUser);
        this.kitmapUsers.remove(coreKitmapUser);
        jedis.save();
    }

    public CoreUser loadFromRedis(UUID uuid)
    {
        jedis.connect();

        CoreUser user = new CoreUser(uuid);
        user.loadFromString(jedis.get("network.users." + uuid.toString()));

        CoreSGUser coreSGUser = new CoreSGUser(user);
        coreSGUser.loadFromString(jedis.get("network.sg.users." + uuid.toString()));
        this.sgUsers.add(coreSGUser);

        CoreHCFUser coreHCFUser = new CoreHCFUser(user);
        coreHCFUser.loadFromString(jedis.get("network.hcf.users." + uuid.toString()));
        this.hcfUsers.add(coreHCFUser);

        CoreKitmapUser coreKitmapUser = new CoreKitmapUser(user);
        coreKitmapUser.loadFromString(jedis.get("network.kitmap.users." + uuid.toString()));
        this.kitmapUsers.add(coreKitmapUser);

        user.setSGUser(coreSGUser);
        user.setHcfUser(coreHCFUser);
        return user;
    }
}