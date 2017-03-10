package xyz.sethy.factions.koth.dto;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sethm on 26/12/2016.
 */
public interface Koth
{
    AtomicLong getTimeRemaining();

    AtomicReference<String> getCapper();

    AtomicReference<String> getName();

    AtomicLong getStartTime();

    LinkedList<Location> getKoTHLocations();

    void captured();
}
