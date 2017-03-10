package xyz.sethy.sg.spectating;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xyz.sethy.sg.SG;

import java.util.LinkedList;

/**
 * Created by sethm on 22/12/2016.
 */
public class SpectatorHandler
{
    private LinkedList<Player> spectators;

    public SpectatorHandler()
    {
        this.spectators = new LinkedList<>();
    }

    public boolean isSpectator(Player player)
    {
        return spectators.contains(player);
    }

    public void addSpectator(Player player)
    {
        this.spectators.add(player);
        player.getInventory().clear();
        player.getInventory().setItem(0, SG.getInstance().getGameItems().getDeathCompass());
        player.updateInventory();
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You are not spectating the match."));
        player.updateInventory();

        SG.getInstance().getPlayers().stream().filter(player1 -> !this.spectators.contains(player1)).forEach(player1 ->
        {
            player1.hidePlayer(player);
        });
    }

    public void removeSpectator(Player player)
    {
        this.spectators.remove(player);
    }
}
