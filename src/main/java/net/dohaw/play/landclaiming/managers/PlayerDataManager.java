package net.dohaw.play.landclaiming.managers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.datahandlers.PlayerDataHandler;
import net.dohaw.play.landclaiming.datahandlers.RegionDataHandler;
import net.dohaw.play.landclaiming.files.PlayersWithRegionsConfig;
import net.dohaw.play.landclaiming.region.RegionData;

import java.util.*;


public class PlayerDataManager{

    private LandClaiming plugin;
    private PlayerDataHandler playerDataHandler;
    private Map<UUID, PlayerData> dataMap = new HashMap<>();

    public PlayerDataManager(LandClaiming plugin){
        this.plugin = plugin;
    }

    public Map<UUID, PlayerData> getAllData(){
        return dataMap;
    }

    public PlayerData getData(UUID uuid){
        return dataMap.get(uuid);
    }

    public void saveData(){
        for(Map.Entry<UUID, PlayerData> entry : dataMap.entrySet()){
            playerDataHandler.save(entry.getValue());
        }
    }

    public void saveData(UUID uuid){
        playerDataHandler.save(getData(uuid));
    }

    public void setData(UUID uuid, PlayerData newData){
        dataMap.replace(uuid, newData);
    }

    public boolean hasDataLoaded(UUID uuid){
        return dataMap.containsKey(uuid);
    }

    public boolean hasDataOnRecord(UUID uuid){
        return playerDataHandler.hasDataFiles(uuid);
    }

    public void createPlayerData(UUID uuid){
        dataMap.put(uuid, playerDataHandler.create(uuid));
    }

    public void loadPlayerData(UUID uuid){
        dataMap.put(uuid, playerDataHandler.load(uuid));
    }

    public void removePlayerData(UUID uuid){
        dataMap.remove(uuid);
    }

}
