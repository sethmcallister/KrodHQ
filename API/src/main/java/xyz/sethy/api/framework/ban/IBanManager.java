package xyz.sethy.api.framework.ban;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by sethm on 07/01/2017.
 */
public interface IBanManager
{
    void addBan(Ban ban);

    void removeBan(Ban ban);

    Ban getBan(UUID uuid) throws ExecutionException, InterruptedException;
}
