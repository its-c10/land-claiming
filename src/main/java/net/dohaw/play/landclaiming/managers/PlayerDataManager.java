package net.dohaw.play.landclaiming.managers;

import net.dohaw.play.landclaiming.PlayerData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerData getPlayerData(UUID uuid){
        return playerData.get(uuid);
    }

    public void setPlayerData(UUID uuid, PlayerData data){
        playerData.replace(uuid, data);
    }

    public void removePlayerData(UUID uuid){
        playerData.remove(uuid);
    }

    public void addPlayerData(UUID uuid){

    }

}
