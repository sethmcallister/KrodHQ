package xyz.sethy.api.framework;

import org.bukkit.plugin.Plugin;
import xyz.sethy.api.framework.ban.IBanManager;
import xyz.sethy.api.framework.mute.IMuteManager;
import xyz.sethy.api.framework.user.IUserManager;

public abstract interface IFramework
{
    Plugin getPlugin();

    IUserManager getUserManager();

    IBanManager getBanManager();

    IMuteManager getMuteManager();
}
