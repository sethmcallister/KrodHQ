package xyz.sethy.api.framework.mute;

import java.util.Date;

/**
 * Created by Seth on 22/01/2017.
 */
public interface Mute
{
    MuteType getType();

    SimplifiedMuteType getSimplifiedType();

    String getReason();

    String getBannedBy();

    Long getExpireDate();

    Date getMuteDate();

    String getTarget();

    boolean isActive();
}
