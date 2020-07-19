package net.dohaw.play.landclaiming.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlayerRegionsConfig extends ConfigManager {

    public PlayerRegionsConfig(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

}
