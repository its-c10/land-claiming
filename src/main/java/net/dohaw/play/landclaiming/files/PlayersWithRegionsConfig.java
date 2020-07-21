package net.dohaw.play.landclaiming.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class PlayersWithRegionsConfig extends ConfigManager {

    public PlayersWithRegionsConfig(JavaPlugin plugin) {
        super(plugin, "playersWithRegions.yml");
    }

    public void addPlayer(UUID uuid){
        List<String> playersWithRegions = getPlayersWithRegions();
        playersWithRegions.add(uuid.toString());
        config.set("Players With Regions", playersWithRegions);
        saveConfig();
    }

    public List<String> getPlayersWithRegions(){
        return config.getStringList("Players With Regions");
    }

    public void removePlayer(UUID uuid){
        List<String> playersWithRegions = getPlayersWithRegions();
        playersWithRegions.remove(uuid);
        config.set("Players With Regions", playersWithRegions);
        saveConfig();
    }

}
