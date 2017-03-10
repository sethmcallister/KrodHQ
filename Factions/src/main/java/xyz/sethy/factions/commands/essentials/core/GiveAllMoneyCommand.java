package xyz.sethy.factions.commands.essentials.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.hcf.HCFUser;

/**
 * Created by Seth on 10/03/2017.
 */
public class GiveAllMoneyCommand extends CommandBase
{
    public GiveAllMoneyCommand()
    {
        super("givebal", Group.OWNER, true);
        Bukkit.getPluginCommand("givebal").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            HCFUser hcfUser = API.getUserManager().findHCFByUniqueId(player.getUniqueId());
            if(hcfUser.getBalance() == 0)
                hcfUser.setBalance(250);
        }
    }
}
