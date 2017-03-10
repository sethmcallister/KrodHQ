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
    private Date expireDate;
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
        this.expireDate = new Date();
        this.banDate = new Date();
    }

    public CoreBan()
    {
        this.target = null;
        this.isActive = false;
        this.type = null;
        this.simplifiedType = null;
        this.reason = null;
        this.banDate = null;
        this.expireDate = null;
    }

    public void loadFromString(String string)
    {
        if (string == null)
        {
            return;
        }

        String[] strings = string.split("\n");
        System.out.println(string);
        for (String line : strings)
        {
            String identifier = line.substring(0, line.indexOf(58));
            String[] lineParts = line.substring(line.indexOf(58)).split(",");
            if (identifier.equalsIgnoreCase("Target"))
            {
                this.target = lineParts[0].replace(":", "");
            }
            if (identifier.equalsIgnoreCase("Type"))
            {
                this.type = BanType.valueOf(lineParts[0].replace(":", ""));
            }
            if (identifier.equalsIgnoreCase("SimpleType"))
            {
                this.simplifiedType = SimplifiedBanType.valueOf(lineParts[0].replace(":", ""));
            }
            if (identifier.equalsIgnoreCase("Reason"))
            {
                this.reason = lineParts[0].replace(":", "");
            }
            if (identifier.equalsIgnoreCase("ExpireDate"))
            {
                this.expireDate = new Date(Long.valueOf(lineParts[0].replace(":", "")));
            }
            if (identifier.equalsIgnoreCase("BanDate"))
            {
                this.banDate = new Date(Long.valueOf(lineParts[0].replace(":", "")));
            }
            if (identifier.equalsIgnoreCase("Active"))
                this.isActive = Boolean.valueOf(lineParts[0].replace(":", ""));
        }
    }


    public String saveToString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Target:").append(this.target).append("\n");
        stringBuilder.append("Type:").append(this.type.toString()).append("\n");
        stringBuilder.append("SimpleType:").append(this.simplifiedType.toString()).append("\n");
        stringBuilder.append("Reason:").append(this.reason).append("\n");
        stringBuilder.append("ExpireDate:").append(this.expireDate.getTime()).append("\n");
        stringBuilder.append("BanDate:").append(this.banDate.getTime()).append("\n");
        stringBuilder.append("Active:").append(this.isActive).append("\n");
        return stringBuilder.toString();
    }

    public CoreBan(String target, BanType type, String reason, String bannedBy, Date expireDate)
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
    public Date getExpireDate()
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
