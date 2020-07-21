package net.dohaw.play.landclaiming.managers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.handlers.RegionDataHandler;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionDataManager {

    private LandClaiming plugin;
    private RegionDataHandler regionDataHandler;
    private List<RegionData> regionDataList = new ArrayList<>();

    public RegionDataManager(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataHandler = new RegionDataHandler(plugin);
    }

    public void loadData(){
        this.regionDataList = regionDataHandler.load();
    }

    public void saveData(){
        for(RegionData rd : regionDataList){
            regionDataHandler.save(rd);
        }
    }

    public List<RegionData> getRegionData(){
        return regionDataList;
    }

    public boolean hasData(Chunk chunk){
        for(RegionData rd : regionDataList){
            if(rd.getChunk().equals(chunk)){
                return true;
            }
        }
        return false;
    }

    public boolean hasData(Location location){
        return hasData(location.getChunk());
    }

    public RegionData create(UUID owner, Chunk chunk, RegionDescription desc, RegionType type){
        RegionData regionData = regionDataHandler.create(owner, chunk, desc, type);
        regionDataList.add(regionData);
        return regionData;
    }

    public RegionData getDataFromLocation(Location loc){
        return getDataFromChunk(loc.getChunk());
    }

    public RegionData getDataFromChunk(Chunk chunk){
        for(RegionData rd : regionDataList){
            if(rd.getChunk().equals(chunk)){
                return rd;
            }
        }
        return null;
    }

    public List<RegionData> getPlayerRegionData(UUID uuid){
        List<RegionData> playerRegionData = new ArrayList<>();
        for(RegionData rd : regionDataList){
            if(rd.getOwnerUUID().equals(uuid)){
                playerRegionData.add(rd);
            }
        }
        return playerRegionData;
    }

    public List<RegionData> getPlayerRegionDataByDescription(UUID uuid, RegionDescription desc, RegionType type){
        List<RegionData> playerRegionData = new ArrayList<>();
        if(!getPlayerRegionData(uuid).isEmpty()){
            for(RegionData rd : getPlayerRegionData(uuid)){
                if(rd.getDescription() == desc && rd.getType() == type){
                    playerRegionData.add(rd);
                }
            }
        }
        return playerRegionData;
    }

    public List<RegionData> getPlayerRegionDataByDescription(UUID uuid, RegionDescription desc){
        List<RegionData> playerRegionData = new ArrayList<>();
        if(!getPlayerRegionData(uuid).isEmpty()){
            for(RegionData rd : getPlayerRegionData(uuid)){
                if(rd.getDescription() == desc){
                    playerRegionData.add(rd);
                }
            }
        }
        return playerRegionData;
    }

}
