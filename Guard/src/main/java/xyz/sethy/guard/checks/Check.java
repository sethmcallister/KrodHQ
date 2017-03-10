package xyz.sethy.guard.checks;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sethm on 16/11/2016.
 */
public class Check implements Listener
{
    private String Identifier;
    private String Name;
    private Plugin plugin;
    private boolean Enabled = true;
    private boolean BanTimer = false;
    private boolean Bannable = true;
    private boolean JudgementDay = false;
    private Integer MaxViolations = 5;
    private Integer ViolationsToNotify = 1;
    private Long ViolationResetTime = 600000L;
    public Map<String, List<String>> DumpLogs = new ConcurrentHashMap<>();

    public Check(String Identifier, String Name, Plugin plugin)
    {
        this.Name = Name;
        this.plugin = plugin;
        this.Identifier = Identifier;
    }

    public boolean isEnabled()
    {
        return this.Enabled;
    }

    public boolean isBannable()
    {
        return this.Bannable;
    }

    public boolean hasBanTimer()
    {
        return this.BanTimer;
    }

    public boolean isJudgmentDay()
    {
        return this.JudgementDay;
    }


    public Integer getMaxViolations()
    {
        return this.MaxViolations;
    }

    public Integer getViolationsToNotify()
    {
        return this.ViolationsToNotify;
    }

    public Long getViolationResetTime()
    {
        return this.ViolationResetTime;
    }

    public void setBannable(boolean Bannable)
    {
        this.Bannable = Bannable;
    }

    public void setAutobanTimer(boolean BanTimer)
    {
        this.BanTimer = BanTimer;
    }

    public void setMaxViolations(int MaxViolations)
    {
        this.MaxViolations = MaxViolations;
    }

    public void setViolationsToNotify(int ViolationsToNotify)
    {
        this.ViolationsToNotify = ViolationsToNotify;
    }

    public void setViolationResetTime(long ViolationResetTime)
    {
        this.ViolationResetTime = ViolationResetTime;
    }

    public void setJudgementDay(boolean JudgementDay)
    {
        this.JudgementDay = JudgementDay;
    }

    public String getName()
    {
        return this.Name;
    }

    public String getIdentifier()
    {
        return this.Identifier;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }
}
