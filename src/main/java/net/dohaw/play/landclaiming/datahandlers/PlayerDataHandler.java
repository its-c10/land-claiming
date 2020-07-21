package net.dohaw.play.landclaiming.datahandlers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.region.RegionData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerDataHandler {

    private LandClaiming plugin;
    private RegionDataHandler regionDataHandler;

    public PlayerDataHandler(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataHandler = plugin.getRegionDataHandler();
    }

    public PlayerData load(UUID uuid){

        File playerDataFile = new File(plugin.getDataFolder() + "/data/" + uuid.toString(), "playerData.yml");
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

        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerData;
    }

    public void save(PlayerData data){

        UUID uuid = data.getUUID();
        File playerDataFile = new File(plugin.getDataFolder() + "/data/" + uuid.toString(), "playerData.yml");
        FileConfiguration config = data.getConfig();

        //HashMap<UUID, RegionData> regionData = data.getRegions();
        //Iterator<Map.Entry<UUID, RegionData>> itr = regionData.entrySet().iterator();

        /*
        while(itr.hasNext()){
            regionDataHandler.save(itr.next().getValue());
        }*/

        /*
            Player data stuff
         */
        config.set("uuid", uuid.toString());

        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
