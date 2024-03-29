package xyz.sethy.core.framework.user;

import xyz.sethy.api.API;
import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.group.Group;
import xyz.sethy.api.framework.user.User;
import xyz.sethy.api.framework.user.hcf.HCFUser;
import xyz.sethy.api.framework.user.sg.SGUser;
import xyz.sethy.core.framework.user.sg.CoreSGUser;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sethm on 04/12/2016.
 */
public class CoreUser implements User
{
    private UUID uuid;
    private String name;
    private String lastIP;
    private Double exp;
    private transient AtomicBoolean muted;
    private transient AtomicBoolean banned;
    private String lastServer;
    private boolean needsSave;
    private CoreSGUser coreSGUser;
    private HCFUser hcfUser;
    private Group group;
    private Ban ban;
    private AtomicBoolean blacklisted;
    private AtomicBoolean staffMode;
    private AtomicInteger sbType;
    private AtomicBoolean vanished;
    private AtomicBoolean staffChat;
    private AtomicBoolean pmSounds;

    public CoreUser(UUID uuid)
    {
        this.uuid = uuid;
        this.name = "unknown";
        this.lastIP = "unknown";
        this.exp = 0.0D;
        this.muted = new AtomicBoolean(false);
        this.banned = new AtomicBoolean(false);
        this.lastServer = "unknown";
        this.needsSave = true;
        this.coreSGUser = null;
        this.hcfUser = null;
        this.group = Group.DEFAULT;
        this.ban = null;
        this.blacklisted = new AtomicBoolean(false);
        this.sbType = new AtomicInteger(2);
        this.staffChat = new AtomicBoolean(false);
        this.vanished = new AtomicBoolean(false);
        this.staffMode = new AtomicBoolean(false);
        this.pmSounds = new AtomicBoolean(true);
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    @Override
    public String getLastServer()
    {
        return lastServer;
    }

    @Override
    public void setLastServer(String lastServer)
    {
        this.lastServer = lastServer;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getLastIP()
    {
        return lastIP;
    }

    public void setLastIP(String lastIP)
    {
        this.lastIP = lastIP;
    }

    public AtomicBoolean isMuted()
    {
        return muted;
    }

    public void setMuted(AtomicBoolean muted)
    {
        this.muted = muted;
    }

    public AtomicBoolean isBanned()
    {
        return banned;
    }

    public void setBanned(AtomicBoolean banned)
    {
        this.banned = banned;
    }

    public boolean needsSave()
    {
        return needsSave;
    }

    public void flagForSave()
    {
        this.needsSave = true;
    }

    @Override
    public void flagSaved()
    {
        this.needsSave = false;
    }

    public SGUser getSGUser()
    {
        return coreSGUser;
    }

    @Override
    public HCFUser getHCFUser()
    {
        return hcfUser;
    }

    @Override
    public Integer getSBType()
    {
        return sbType.get();
    }

    @Override
    public boolean isVanished()
    {
        return vanished.get();
    }

    @Override
    public boolean isStaffChat()
    {
        return staffChat.get();
    }

    @Override
    public boolean hasPMSounds()
    {
        return this.pmSounds.get();
    }

    @Override
    public void setPMSounds(boolean result)
    {
        this.pmSounds.set(result);
    }

    @Override
    public void setVansihed(boolean result)
    {
        vanished.set(result);
    }

    @Override
    public void setStaffChat(boolean result)
    {
        staffChat.set(result);
    }

    @Override
    public void setSBType(Integer type)
    {
        this.sbType.set(type);
    }

    @Override
    public boolean isStaffMode()
    {
        return staffMode.get();
    }

    @Override
    public Ban getBan()
    {
        return ban;
    }

    @Override
    public AtomicBoolean isBlacklisted()
    {
        return blacklisted;
    }

    public void setSGUser(SGUser sgUser)
    {
        this.coreSGUser = (CoreSGUser) sgUser;
    }

    @Override
    public Group getGroup()
    {
        return group;
    }

    @Override
    public void setGroup(Group group)
    {
        this.group = group;
    }

    @Override
    public void setBan(Ban ban)
    {
        this.ban = ban;
    }

    @Override
    public void setBlacklisted(boolean blacklisted)
    {
        this.blacklisted.set(blacklisted);
    }

    @Override
    public void forceSave()
    {
        UserManager userManager = (UserManager) API.getUserManager();
        userManager.unloadUser(this);
    }

    public void setHcfUser(HCFUser hcfUser)
    {
        this.hcfUser = hcfUser;
    }

    public Double getEXP()
    {
        return exp;
    }

    public void setEXP(double exp)
    {
        this.exp = exp;
    }

    @Override
    public void setStaffMode(boolean value)
    {
        this.staffMode.set(value);
    }
}
