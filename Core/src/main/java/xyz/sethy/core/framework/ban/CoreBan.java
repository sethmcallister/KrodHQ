package xyz.sethy.core.framework.ban;

import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.BanType;
import xyz.sethy.api.framework.ban.SimplifiedBanType;

import java.util.Date;

/**
 * Created by sethm on 01/01/2017.
 */
public class CoreBan implements Ban
{
    private String target;
    private BanType type;
    private SimplifiedBanType simplifiedType;
    private String reason;
    private String bannedBy;
    private Long expireDate;
    private Date banDate;
    private boolean isActive;

    public CoreBan(String target, BanType type, String reason, String bannedBy)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.simplifiedType = type == BanType.IP_PERMANENT || type == BanType.NORMAL_PERMANENT ? SimplifiedBanType.PERMANENT : SimplifiedBanType.TEMPORARILY;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = 0L;
        this.banDate = new Date();
    }

    public CoreBan(String target, BanType type, String reason, String bannedBy, Long expireDate)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.simplifiedType = type == BanType.IP_PERMANENT || type == BanType.NORMAL_PERMANENT ? SimplifiedBanType.PERMANENT : SimplifiedBanType.TEMPORARILY;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = expireDate;
        this.banDate = new Date();
    }

    @Override
    public BanType getType()
    {
        return this.type;
    }

    @Override
    public SimplifiedBanType getSimplifiedType()
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
    public Date getBanDate()
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
