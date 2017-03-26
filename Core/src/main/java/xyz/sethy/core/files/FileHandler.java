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

    private File modulesFile1;
    private FileConfiguration modules1;

    public FileHandler(Plugin plugin)
    {
        this.plugin = plugin;

        modulesFile = new File(this.plugin.getDataFolder(), "modules.yml");
        modules = YamlConfiguration.loadConfiguration(modulesFile);

        modulesFile1 = new File(this.plugin.getDataFolder(), "config.yml");
        modules1 = YamlConfiguration.loadConfiguration(modulesFile);


    }

    public File getModules()
{
    return modulesFile;
}
    public File getConfiguration()
    {
        return modulesFile1;
    }


    public FileConfiguration getModulesFile()
    {
        return modules;
    }

    public FileConfiguration getConfigFile()
    {
        return modules1;
    }
}
