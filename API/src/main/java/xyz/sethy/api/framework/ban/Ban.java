package xyz.sethy.api.framework.ban;

import java.util.Date;

/**
 * Created by sethm on 01/01/2017.
 */
public interface Ban
{
    BanType getType();

    SimplifiedBanType getSimplifiedType();

    String getReason();

    String getBannedBy();

    Long getExpireDate();

    Date getBanDate();

    String getTarget();

    boolean isActive();
}
