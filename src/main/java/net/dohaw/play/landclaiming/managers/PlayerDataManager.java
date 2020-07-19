package net.dohaw.play.landclaiming.managers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.datahandlers.PlayerDataHandler;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private LandClaiming plugin;
    private PlayerDataHandler playerDataHandler;

    public PlayerDataManager(LandClaiming plugin){
        this.plugin = plugin;
        this.playerDataHandler = new PlayerDataHandler(plugin, )
    }

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

    /*
        Run this if the player has had player data previously.
     */
    public void addPlayerData(UUID uuid){

    }

    /*
        Run this if the player has never had player data
     */
    public void createPlayerData(){

    }

    private void createPlayerFiles(){

    }

}
