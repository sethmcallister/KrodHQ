package us.kade.essentials.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.commands.CommandBase;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;

/**
 * Created by sethm on 20/08/2016.
 */
public class ListCommand extends CommandBase
{
    public ListCommand()
    {
        super("list", Group.DEFAULT, true);
        Bukkit.getPluginCommand("list").setExecutor(this);
    }

    @Override
    public void execute(Player sender, Command command, String label, String[] args)
    {
        showList(sender);
        return;

    }

    private void showList(CommandSender sender)
    {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Owner&f, &bDeveloper&f, &c&oPlatform Admin&f, &cAdmin&f, &5Mod&f, &eTrail Mod&f, &5\u262F&2Famous&f, " +
                "&dYouTube&f, &3Krod&f, &7Elaph&f, &6Ant&f, &fDefault"));
        handleList(sender);
        sender.sendMessage(ChatColor.YELLOW + "If you require support, please join " + ChatColor.GOLD + "ts.KrodHQ.com" + ChatColor.YELLOW + ".");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
    }

    private void handleList(CommandSender sender)
    {
        StringBuilder owner = new StringBuilder();
        StringBuilder lead_developer = new StringBuilder();
        StringBuilder dev = new StringBuilder();
        StringBuilder manager = new StringBuilder();
        StringBuilder network_admin = new StringBuilder();
        StringBuilder sr_admin = new StringBuilder();
        StringBuilder admin = new StringBuilder();
        StringBuilder sr_mod = new StringBuilder();
        StringBuilder mod = new StringBuilder();
        StringBuilder trail_mod = new StringBuilder();
        StringBuilder famous = new StringBuilder();
        StringBuilder youtube = new StringBuilder();
        StringBuilder krod = new StringBuilder();
        StringBuilder elaph = new StringBuilder();
        StringBuilder ant = new StringBuilder();

        StringBuilder member = new StringBuilder();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            if (user.getName().equals("unknown"))
                continue;

            switch (user.getGroup().toString().toLowerCase())
            {
                case "owner":
                    owner.append(ChatColor.DARK_RED).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "developer":
                    dev.append(ChatColor.AQUA).append(ChatColor.ITALIC).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "manager":
                    manager.append(ChatColor.DARK_RED).append(ChatColor.ITALIC).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "platform_admin":
                    network_admin.append(ChatColor.RED).append(ChatColor.ITALIC).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "sr_admin":
                    sr_admin.append(ChatColor.RED).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "admin":
                    admin.append(ChatColor.RED).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "sr_mod":
                    sr_mod.append(ChatColor.DARK_PURPLE).append(ChatColor.ITALIC).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "mod":
                    mod.append(ChatColor.DARK_PURPLE).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "trail_mod":
                    trail_mod.append(ChatColor.YELLOW).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "famous":
                    famous.append(ChatColor.DARK_PURPLE).append("\u262F").append(ChatColor.DARK_GREEN).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "youtube":
                    youtube.append(ChatColor.LIGHT_PURPLE).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "krod":
                    krod.append(ChatColor.DARK_AQUA).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "elaph":
                    elaph.append(ChatColor.GRAY).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "ant":
                    ant.append(ChatColor.GOLD).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
                case "power_faction":
                case "default":
                    member.append(ChatColor.WHITE).append(user.getName()).append(ChatColor.RESET).append(", ");
                    break;
            }
        }
        String players = owner.toString() + lead_developer.toString() + dev.toString() + manager.toString() + network_admin.toString() + sr_admin.toString() + admin.toString() + sr_mod.toString() + mod.toString() + trail_mod.toString() + famous.toString() + youtube.toString() + krod.toString() + elaph.toString() + ant.toString() + member.toString();
        players = players.trim();
        if (players.endsWith(","))
        {
            players = players.substring(0, players.length() - 1);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&b" + Bukkit.getOnlinePlayers().length + "&7/&b" + Bukkit.getServer().getMaxPlayers() + "&7) " + players));
    }
}
