package xyz.sethy.api.framework.user;

import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.sg.SGUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sethm on 04/12/2016.
 */
public interface User
{
    UUID getUniqueId();

    String getName();

    String getLastIP();

    @Deprecated
    AtomicBoolean isBanned();

    @Deprecated
    AtomicBoolean isMuted();

    Double getEXP();

    String getLastServer();

    Group getGroup();

    SGUser getSGUser();

    HCFUser getHCFUser();

    Integer getSBType();

    boolean isVanished();

    boolean isStaffChat();

    boolean hasPMSounds();

    void setPMSounds(boolean result);

    void setVansihed(boolean result);

    void setStaffChat(boolean result);

    void setSBType(Integer type);

    boolean isStaffMode();

    Ban getBan();

    AtomicBoolean isBlacklisted();

    void setName(String name);

    void setEXP(double exp);

    void setStaffMode(boolean value);

    @Deprecated
    void setBanned(AtomicBoolean banned);

    @Deprecated
    void setMuted(AtomicBoolean muted);

    void setLastServer(String server);

    boolean needsSave();

    void flagForSave();

    void flagSaved();

    void setLastIP(String lastIP);

    void setGroup(Group group);

    void setBan(Ban ban);

    void setBlacklisted(boolean blacklisted);

    void forceSave();
}
