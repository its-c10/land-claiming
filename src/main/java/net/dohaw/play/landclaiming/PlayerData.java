package net.dohaw.play.landclaiming;

import net.dohaw.play.landclaiming.region.RegionData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private HashMap<UUID, RegionData> regions = new HashMap<>();
    private UUID uuid;
    private FileConfiguration config;

    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

    public HashMap<UUID, RegionData> getRegions(){
        return regions;
    }

    public void setRegions(HashMap<UUID, RegionData> regions){
        this.regions = regions;
    }

    public UUID getUUID(){
        return uuid;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }
}
