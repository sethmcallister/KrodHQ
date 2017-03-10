package xyz.sethy.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.core.framework.ban.BanManager;

/**
 * Created by sethm on 07/01/2017.
 */
public class UnbanCommand implements CommandExecutor
{
    public UnbanCommand()
    {
//        super("unban", Group.ADMIN, true);
        Bukkit.getPluginCommand("unban").setExecutor(this);
    }

//    @Override
//    public void execute(Player sender, Command paramCommand, String paramString, String[] args)
//    {
//        if (args.length == 0)
//        {
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/unban <player> <reason>"));
//            return;
//        }
//        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
//        BanManager banManager = (BanManager) API.getBanManager();
//        if (banManager.getBan(target.getUniqueId()) == null)
//        {
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player has not been banned."));
//            return;
//        }
//        while (banManager.getBan(target.getUniqueId()) != null)
//        {
//            Ban ban = banManager.getBan(target.getUniqueId());
//            banManager.removeBan(ban);
//        }
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        DataOutputStream out = new DataOutputStream(stream);
//        try
//        {
//            String towrite = "&7The player &3" + target.getName() + "&7 has been unbanned from &3&lKrodHQ&7.";
//
//            out.writeUTF("SendMessage");
//            out.writeUTF(towrite);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        sender.sendPluginMessage(API.getPlugin(), "SendMessage", stream.toByteArray());
//    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            if(API.getUserManager().findByUniqueId(player.getUniqueId()).getGroup().getPermission() >= Group.MOD.getPermission())
            {
                handleBan(sender, args);
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
            return true;
        }

        handleBan(sender, args);
        return true;
    }

    private void handleBan(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Usage: &7/unban <player> <reason>"));
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        BanManager banManager = (BanManager) API.getBanManager();
        if (banManager.getBan(target.getUniqueId()) == null)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player has not been banned."));
            return;
        }
        while (banManager.getBan(target.getUniqueId()) != null)
        {
            Ban ban = banManager.getBan(target.getUniqueId());
            banManager.removeBan(ban);
        }
        API.sendBungeeMessage("&7The player &3" + target.getName() + "&7 has been unbanned from &3&lKrodHQ&7.");
    }
}
