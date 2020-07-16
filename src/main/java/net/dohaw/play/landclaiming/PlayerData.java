package net.dohaw.play.landclaiming;

import net.dohaw.play.landclaiming.region.Region;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private HashMap<UUID, Region> regions = new HashMap<>();
    private UUID uuid;

    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

    public HashMap<UUID, Region> getRegions(){
        return regions;
    }

    public void setRegions(HashMap<UUID, Region> regions){
        this.regions = regions;
    }

    public UUID getUUID(){
        return uuid;
    }

}
