package net.dohaw.play.landclaiming.managers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.handlers.PlayerDataHandler;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerDataManager{

    private LandClaiming plugin;
    private PlayerDataHandler playerDataHandler;
    private Map<UUID, PlayerData> dataMap = new HashMap<>();

    public PlayerDataManager(LandClaiming plugin){
        this.plugin = plugin;
        this.playerDataHandler = new PlayerDataHandler(plugin);
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

    public int getNumClaimsAvailable(UUID uuid){
        PlayerData data = dataMap.get(uuid);
        return data.getClaimAmount();
    }

    public void reduceClaimAmount(UUID uuid){
        PlayerData data = getData(uuid);
        data.setClaimAmount(data.getClaimAmount() - 1);
        setData(uuid, data);
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
