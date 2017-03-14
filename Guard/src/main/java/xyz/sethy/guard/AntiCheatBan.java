package xyz.sethy.guard;

import xyz.sethy.api.framework.ban.Ban;
import xyz.sethy.api.framework.ban.BanType;
import xyz.sethy.api.framework.ban.SimplifiedBanType;

import java.util.Date;

/**
 * Created by Seth on 15/01/2017.
 */
public class AntiCheatBan implements Ban
{
    private String target;
    private BanType type;
    private SimplifiedBanType simplifiedType;
    private String reason;
    private String bannedBy;
    private Long expireDate;
    private Date banDate;
    private boolean isActive;

    public AntiCheatBan(String target, BanType type, String reason, String bannedBy)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.simplifiedType = type == BanType.IP_PERMANENT || type == BanType.NORMAL_PERMANENT ? SimplifiedBanType.PERMANENT : SimplifiedBanType.TEMPORARILY;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = new Date().getTime();
        this.banDate = new Date();
    }

    public AntiCheatBan(String target, BanType type, String reason, String bannedBy, Date expireDate)
    {
        this.target = target;
        this.isActive = true;
        this.type = type;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expireDate = expireDate.getTime();
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
