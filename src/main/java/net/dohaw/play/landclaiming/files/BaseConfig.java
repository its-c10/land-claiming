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

    public int getDefaultChunkAmount(){
        return config.getInt("Default Chunk Amount");
    }

    public int getDefaultGiveAmount(){
        return config.getInt("Default Claim Give Amount");
    }

    public int getClaimBlockPlaceAmount(){
        return config.getInt("Claim Block Place Amount");
    }

}
