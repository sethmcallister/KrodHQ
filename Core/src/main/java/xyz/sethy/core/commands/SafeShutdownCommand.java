package xyz.sethy.core.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

/**
 * Created by Seth on 09/03/2017.
 */
public class SafeShutdownCommand extends CommandBase
{
    public SafeShutdownCommand()
    {
        super("safeshutdown", Group.DEVELOPER, true);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);

    }

    @Override
    public void execute(Player paramPlayer, Command paramCommand, String paramString, String[] paramArrayOfString)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Hardcore Factions&7 is restarting."));
            sendToHub(player);
        }

        Bukkit.shutdown();
    }

    private void sendToHub(Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("hub");
        player.sendPluginMessage(API.getPlugin(), "BungeeCord", out.toByteArray());
    }
}
