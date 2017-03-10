package xyz.sethy.api.framework.mute;

import java.util.UUID;

/**
 * Created by Seth on 22/01/2017.
 */
public interface IMuteManager
{
    void addMute(Mute mute);

    void removeMute(Mute mute);

    Mute getMute(UUID uuid);
}
