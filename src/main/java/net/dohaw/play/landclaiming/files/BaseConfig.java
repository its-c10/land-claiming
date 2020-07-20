package net.dohaw.play.landclaiming.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BaseConfig extends ConfigManager {

    public BaseConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public String getPluginPrefix(){
        return config.getString("Prefix");
    }

}
