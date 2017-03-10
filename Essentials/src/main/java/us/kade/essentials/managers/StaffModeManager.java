package us.kade.essentials.managers;

import org.bukkit.entity.Player;

import java.util.LinkedList;

/**
 * Created by sethm on 02/01/2017.
 */
public class StaffModeManager
{
    private LinkedList<Player> staffModes;

    public StaffModeManager()
    {
        this.staffModes = new LinkedList<>();
    }

    public LinkedList<Player> getStaffModes()
    {
        return staffModes;
    }
}
