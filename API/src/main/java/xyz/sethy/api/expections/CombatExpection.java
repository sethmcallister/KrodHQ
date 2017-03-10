package xyz.sethy.api.expections;

/**
 * Created by sethm on 30/12/2016.
 */
public class CombatExpection extends RuntimeException
{
    public CombatExpection(String message)
    {
        super(message);
    }
}
