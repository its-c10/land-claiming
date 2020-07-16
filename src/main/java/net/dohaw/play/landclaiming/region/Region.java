package net.dohaw.play.landclaiming.region;

import net.dohaw.play.landclaiming.PlayerData;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class Region {

    private EnumMap<RegionFlagType, RegionFlag> flags = new EnumMap<>(RegionFlagType.class);
    private RegionType type;
    private UUID chunkUUID;
    private List<PlayerData> trustedPlayers = new ArrayList<>();

    public Region(UUID chunkUUID, RegionType type){
        this.chunkUUID = chunkUUID;
        this.type = type;
    }

    public boolean isTrusted(UUID uuid){
        for(PlayerData data : trustedPlayers){
            if(data.getUUID().equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public void addTrustedPlayer(PlayerData data){
        trustedPlayers.add(data);
    }

    public EnumMap<RegionFlagType, RegionFlag> getFlags() {
        return flags;
    }

    public void setFlags(EnumMap<RegionFlagType, RegionFlag> flags) {
        this.flags = flags;
    }

    public RegionType getType() {
        return type;
    }

    public UUID getChunkUUID() {
        return chunkUUID;
    }
}
