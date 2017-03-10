package xyz.sethy.core.framework;

import org.bukkit.plugin.Plugin;
import xyz.sethy.api.framework.IFramework;
import xyz.sethy.api.framework.mute.IMuteManager;
import xyz.sethy.api.framework.user.IUserManager;
import xyz.sethy.core.framework.ban.BanManager;
import xyz.sethy.core.framework.mute.MuteManager;
import xyz.sethy.core.framework.user.UserManager;

/**
 * Created by sethm on 04/12/2016.
 */
public class Framework implements IFramework
{
    private final Plugin plugin;
    private final UserManager userManager;
    private final BanManager banManager;
    private final MuteManager muteManager;

    public Framework(Plugin plugin)
    {
        this.plugin = plugin;
        this.userManager = new UserManager();
        this.banManager = new BanManager();
        this.muteManager = new MuteManager();
    }

    @Override
    public Plugin getPlugin()
    {
        return plugin;
    }

    @Override
    public IUserManager getUserManager()
    {
        return userManager;
    }

    @Override
    public BanManager getBanManager()
    {
        return banManager;
    }

    @Override
    public IMuteManager getMuteManager()
    {
        return muteManager;
    }
}
