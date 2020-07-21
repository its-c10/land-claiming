package net.dohaw.play.landclaiming;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private int claimAmount;
    private FileConfiguration config;

    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

    public UUID getUUID(){
        return uuid;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public int getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(int claimAmount) {
        this.claimAmount = claimAmount;
    }
}
