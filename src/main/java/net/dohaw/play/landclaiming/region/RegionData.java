package net.dohaw.play.landclaiming.region;

import net.dohaw.play.landclaiming.PlayerData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class RegionData {

    private EnumMap<RegionFlagType, RegionFlag> flags = new EnumMap<>(RegionFlagType.class);
    private RegionDescription description;
    private RegionType type;
    private UUID ownerUUID;
    private Chunk chunk;
    private List<UUID> trustedPlayers = new ArrayList<>();
    private FileConfiguration config;
    private File file;
    private String name;

    public RegionData(String name, Chunk chunk, RegionDescription description, RegionType type){
        this.type = type;
        this.description = description;
        this.chunk = chunk;
        this.name = name;
    }

    public boolean isTrusted(UUID uuid){
        return trustedPlayers.contains(uuid);
    }

    public boolean isOwner(UUID uuid){
        return uuid == getOwnerUUID();
    }

    public void addTrustedPlayer(PlayerData data){
        trustedPlayers.add(data.getUUID());
    }

    public void addTrustedPlayer(UUID uuid){
        trustedPlayers.add(uuid);
    }

    public List<UUID> getTrustedPlayers(){
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

    public Chunk getChunk() {
        return chunk;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
