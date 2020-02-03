package pl.trollcraft.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.trollcraft.Main;

import java.io.File;
import java.io.IOException;

public class Configs {

    public static YamlConfiguration load(String configName, Plugin plugin){
        YamlConfiguration config;
        File file = new File(plugin.getDataFolder() + File.separator + configName);
        if (!file.exists())
            plugin.saveResource(configName, false);
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }

    public static void save(YamlConfiguration c, String file) {
        try {
            c.save(new File(Main.getInstance().getDataFolder(), file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
