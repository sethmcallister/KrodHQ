package xyz.sethy.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.sethy.api.framework.IFramework;
import xyz.sethy.api.framework.ban.IBanManager;
import xyz.sethy.api.framework.mute.IMuteManager;
import xyz.sethy.api.framework.user.IUserManager;
import xyz.sethy.api.framework.uuid.UUIDFetcher;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by sethm on 04/12/2016.
 */
public abstract class API
{
    public static Plugin getPlugin()
    {
        return framework.getPlugin();
    }

    private static IFramework framework;

    public static IUserManager getUserManager()
    {
        return framework.getUserManager();
    }

    public static IBanManager getBanManager()
    {
        return framework.getBanManager();
    }

    public static IMuteManager getMuteManager()
    {
        return framework.getMuteManager();
    }

    public static UUIDFetcher getUUIDFetcher(List<String> names)
    {
        return new UUIDFetcher(names);
    }

    public static void setFramework(IFramework kadeFramework)
    {
        framework = kadeFramework;
    }

    public static void sendBungeeMessage(String message)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try
        {
            out.writeUTF("SendMessage");
            out.writeUTF(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Random random = new Random();
        int i = random.nextInt(Bukkit.getOnlinePlayers().length);
        Bukkit.getOnlinePlayers()[i].sendPluginMessage(API.getPlugin(), "SendMessage", stream.toByteArray());
    }

}
