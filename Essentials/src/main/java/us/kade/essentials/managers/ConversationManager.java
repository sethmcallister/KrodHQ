package us.kade.essentials.managers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.sethy.api.API;
import xyz.sethy.api.framework.user.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 25/09/2016.
 */
public class ConversationManager
{
    private static ConversationManager instance;
    private ConcurrentHashMap<Player, Player> conversations = new ConcurrentHashMap<>();

    public static ConversationManager getInstance()
    {
        return instance;
    }

    public ConversationManager()
    {
        instance = this;
    }

    public void setConversations(Player player, Player target, String message)
    {
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        User receiver = API.getUserManager().findByUniqueId(target.getUniqueId());

        this.conversations.put(player, target);
        this.conversations.put(target, player);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d(To &r" + receiver.getGroup().getName() + target.getName() + "&d) ") + message);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d(From &r" + user.getGroup().getName() + player.getName() + "&d) ") + message);
        if (receiver.hasPMSounds())
            target.playSound(target.getLocation(), Sound.ORB_PICKUP, 10F, 10F);
    }

    public void reply(Player player, String message)
    {
        if (!this.conversations.containsKey(player))
        {
            player.sendMessage(ChatColor.RED + "You're not in a conversation with anyone.");
            return;
        }
        if (this.conversations.get(player) != null)
        {
            Player target = this.conversations.get(player);
            setConversations(player, target, message);
            return;
        }
        player.sendMessage(ChatColor.RED + "That player is not online.");
    }

    public boolean isConversing(Player player)
    {
        return this.conversations.contains(player);
    }

    public ConcurrentHashMap<Player, Player> getConversations()
    {
        return this.conversations;
    }
}
