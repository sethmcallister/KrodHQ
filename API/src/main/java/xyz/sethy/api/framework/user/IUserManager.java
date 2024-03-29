package xyz.sethy.api.framework.user;

import xyz.sethy.api.framework.user.factions.FactionUser;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.kitmap.KitmapUser;
import xyz.sethy.api.framework.user.sg.SGUser;

import java.util.List;
import java.util.UUID;

public abstract interface IUserManager
{
    User findByUniqueId(UUID paramUUID);

    HCFUser findHCFByUniqueId(UUID uuid);

    SGUser findSGByUniqueId(UUID uuid);

    KitmapUser findKitmapByUniqueId(UUID uuid);

    List<User> getAllUsers();

    void createUser(User paramUser);

    void loadUser(User paramUser);

    void unloadUser(User paramUser);

    void deleteUser(String paramString);

    User getTempUser(UUID paramUUID);

    HCFUser getTempHCFUser(UUID uuid);

    KitmapUser getTempKitsUser(UUID uuid);

    FactionUser getFactionUser(UUID uuid);
    FactionUser getTempFactionUser(UUID uuid);
}
