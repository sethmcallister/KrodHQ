package xyz.sethy.core.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Seth on 19/03/2017.
 */
public class SetMOTDCommand extends CommandBase
{
    public SetMOTDCommand()
    {
        super("setmotd", Group.OPERRATOR, false);
        Bukkit.getPluginCommand(this.getCommand()).setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {

        String message = StringUtils.join(args, " ", 0, args.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try
        {
            out.writeUTF("SetMOTD");
            out.writeUTF(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        sender.sendPluginMessage(API.getPlugin(), "SetMOTD", stream.toByteArray());
    }
}
