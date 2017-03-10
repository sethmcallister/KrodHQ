package xyz.sethy.core.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * Created by sethm on 23/12/2016.
 */
public class FileHandler
{
    private Plugin plugin;

    private File modulesFile;
    private FileConfiguration modules;

    public FileHandler(Plugin plugin)
    {
        this.plugin = plugin;

        modulesFile = new File(this.plugin.getDataFolder(), "modules.yml");
        modules = YamlConfiguration.loadConfiguration(modulesFile);
    }

    public File getModules()
    {
        return modulesFile;
    }

    public FileConfiguration getModulesFile()
    {
        return modules;
    }
}
