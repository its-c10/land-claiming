package net.dohaw.play.landclaiming.region;

import net.dohaw.play.landclaiming.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class RegionData {

    private EnumMap<RegionFlagType, RegionFlag> flags = new EnumMap<>(RegionFlagType.class);
    private RegionDescription description;
    private RegionType type;
    private UUID chunkUUID, ownerUUID;
    private List<PlayerData> trustedPlayers = new ArrayList<>();
    private FileConfiguration config;

    public RegionData(UUID chunkUUID, RegionDescription description, RegionType type){
        this.chunkUUID = chunkUUID;
        this.type = type;
        this.description = description;
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

    public List<PlayerData> getTrustedPlayers(){
        return trustedPlayers;
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

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public RegionDescription getDescription() {
        return description;
    }

    public void setDescription(RegionDescription description) {
        this.description = description;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
