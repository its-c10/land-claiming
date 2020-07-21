package net.dohaw.play.landclaiming;

import net.dohaw.play.landclaiming.datahandlers.PlayerDataHandler;
import net.dohaw.play.landclaiming.datahandlers.RegionDataHandler;
import net.dohaw.play.landclaiming.files.PlayersWithRegionsConfig;
import net.dohaw.play.landclaiming.region.RegionData;

import java.util.*;

/*
    Little bit of a poor design to have player data and region data within the same manager
    Was in too deep into the project to fix it.

    Is a design issue, but doesn't hinder performance.
 */
public class DataManager {

    private LandClaiming plugin;
    private PlayerDataHandler playerDataHandler;
    private RegionDataHandler regionDataHandler;
    private PlayersWithRegionsConfig playersWithRegionsConfig;
    private HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private List<RegionData> regionData = new HashMap<>();

    public DataManager(LandClaiming plugin){
        this.plugin = plugin;
        //Object is created here because this is the only class that uses it.
        this.playerDataHandler = new PlayerDataHandler(plugin);
        //Is set in the LandClaiming class because it's shared between multiple classes.
        this.regionDataHandler = plugin.getRegionDataHandler();
        this.playersWithRegionsConfig = plugin.getPlayersWithRegionsConfig();
    }

    public PlayerData getPlayerData(UUID uuid){
        return playerData.get(uuid);
    }

    public RegionData getRegionData(UUID uuid){
        return regionData.get(uuid);
    }

    public boolean hasRegionData(UUID uuid){
        return regionData.containsKey(uuid);
    }

    public void setRegionData(UUID uuid, RegionData data){
        regionData.replace(uuid, data);
    }

    /*
    public void removeRegionData(UUID uuid){
        regionData.remove(uuid);
    }*/

    public void setPlayerData(UUID uuid, PlayerData data){
        playerData.replace(uuid, data);
    }

    public void removePlayerData(UUID uuid){
        playerDataHandler.save(playerData.get(uuid));
        playerData.remove(uuid);
    }

    public void loadData(){
        List<String> playersWithRegions = playersWithRegionsConfig.getPlayersWithRegions();
        for(String s : playersWithRegions){

            UUID uuid = UUID.fromString(s);
            PlayerData pData = playerDataHandler.load(uuid);
            playerData.put(uuid, pData);

            /*
                Loads region data into the map
             */
            Iterator<Map.Entry<UUID, RegionData>> itr = pData.getRegions().entrySet().iterator();
            while(itr.hasNext()){
                Map.Entry<UUID, RegionData> entry = itr.next();
                regionData.put(entry.getKey(), entry.getValue());
            }
        }
    }

     /*
    public void loadPlayerData(UUID uuid){

        if(!playerData.containsKey(uuid)){
            playerData.put(uuid, playerDataHandler.load(uuid));
        }
    }*/

    /*
        Run this if the player has never had player data
     */
    public void createPlayerData(UUID uuid){
        playerData.put(uuid, playerDataHandler.create(uuid));
    }

    public void shutDown(){

        Iterator<Map.Entry<UUID, PlayerData>> itr = playerData.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<UUID, PlayerData> entry = itr.next();
            playerDataHandler.save(entry.getValue());
        }

        Iterator<Map.Entry<UUID, RegionData>> itrr = regionData.entrySet().iterator();
        while(itrr.hasNext()){
            Map.Entry<UUID, RegionData> entry = itrr.next();
            regionDataHandler.save(entry.getValue());
        }

    }

    /*
        Dictates whether you need to create player data files for this player
     */
    public boolean hasExistingPlayerDataFiles(UUID uuid){
        return playerDataHandler.hasExistingPlayerData(uuid);
    }

}
