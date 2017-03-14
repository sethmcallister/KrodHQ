package xyz.sethy.core.framework.mute;

import xyz.sethy.api.framework.mute.Mute;
import xyz.sethy.api.framework.mute.MuteType;
import xyz.sethy.api.framework.mute.SimplifiedMuteType;

import java.util.Date;

/**
 * Created by Seth on 22/01/2017.
 */
public class CoreMute implements Mute
{
    private String target;
    private MuteType type;
    private SimplifiedMuteType simplifiedType;
    private String reason;
    private String bannedBy;
    private long expireDate;
    private Date banDate;
    private boolean isActive;

    public CoreMute(String target, MuteType type, String reason, String bannedBy)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.simplifiedType = type == MuteType.IP_PERMANENT || type == MuteType.NORMAL_PERMANENT ? SimplifiedMuteType.PERMANENT : SimplifiedMuteType.TEMPORARILY;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = 0L;
        this.banDate = new Date();
    }

    public CoreMute()
    {
        this.target = null;
        this.isActive = false;
        this.type = null;
        this.simplifiedType = null;
        this.reason = null;
        this.banDate = null;
        this.expireDate = 0L;
    }

    public CoreMute(String target, MuteType type, String reason, String bannedBy, Long expireDate)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = expireDate;
        this.banDate = new Date();
    }

    @Override
    public MuteType getType()
    {
        return this.type;
    }

    @Override
    public SimplifiedMuteType getSimplifiedType()
    {
        return this.simplifiedType;
    }

    @Override
    public String getReason()
    {
        return this.reason;
    }

    @Override
    public String getBannedBy()
    {
        return this.bannedBy;
    }

    @Override
    public Long getExpireDate()
    {
        return this.expireDate;
    }

    @Override
    public Date getMuteDate()
    {
        return this.banDate;
    }

    @Override
    public String getTarget()
    {
        return this.target;
    }

    @Override
    public boolean isActive()
    {
        return this.isActive;
    }

    public void setActive(boolean active)
    {
        this.isActive = active;
    }
}
