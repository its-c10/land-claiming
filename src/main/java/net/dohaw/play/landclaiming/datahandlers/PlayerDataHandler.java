package net.dohaw.play.landclaiming.datahandlers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataHandler {

    private LandClaiming plugin;
    private RegionDataHandler regionDataHandler;

    public PlayerDataHandler(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataHandler = new RegionDataHandler(plugin);
    }

    public PlayerData load(FileConfiguration config){

        UUID uuid = UUID.fromString(config.getString("UUID"));
        PlayerData data = new PlayerData(uuid);



        return data;
    }

    public boolean hasExistingPlayerData(UUID uuid){
        File playerFolder = new File(plugin.getDataFolder() + "/data", uuid.toString() + ".yml");
        return playerFolder.exists();
    }

    public PlayerData create(UUID uuid){

        File playerFolder = new File(plugin.getDataFolder() + "/data", uuid.toString());
        File regionFolder = new File(plugin.getDataFolder() + "/data/" + uuid.toString(), "regionData");
        File playerDataFile = new File(plugin.getDataFolder() + "/data/" + uuid.toString(), "playerData.yml");

        try{
            playerFolder.mkdirs();
            regionFolder.mkdirs();
            playerDataFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }

        PlayerData playerData = new PlayerData(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);

        config.set("uuid", uuid.toString());

        playerData.setConfig(config);
        return playerData;
    }


}
