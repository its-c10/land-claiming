package net.dohaw.play.landclaiming.region;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class SingleRegionData extends RegionData{

    private Chunk chunk;
    private FileConfiguration config;
    private File file;
    private boolean isConnected = false;

    public SingleRegionData(String name, Chunk chunk, RegionDescription description, RegionType type){
        this.type = type;
        this.desc = description;
        this.chunk = chunk;
        this.name = name;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
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

    public void setIsConnected(boolean b){
        this.isConnected = b;
    }

    public boolean isConnected(){
        return isConnected;
    }
}
