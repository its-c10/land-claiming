package net.dohaw.play.landclaiming.handlers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.files.BaseConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataHandler {

    private LandClaiming plugin;
    private BaseConfig baseConfig;

    public PlayerDataHandler(LandClaiming plugin){
        this.plugin = plugin;
        this.baseConfig = plugin.getBaseConfig();
    }

    public PlayerData load(UUID uuid){

        File playerDataFile = new File(plugin.getDataFolder() + "/playerData", uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);

        PlayerData data = new PlayerData(uuid);
        data.setConfig(config);
        data.setClaimAmount(config.getInt("Claim Amount"));

        return data;
    }

    public PlayerData create(UUID uuid){

        File playerDataFile = new File(plugin.getDataFolder() + "/playerData", uuid.toString() + ".yml");

        try{
            playerDataFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }

        PlayerData playerData = new PlayerData(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);

        int defaultChunkAmount = baseConfig.getDefaultChunkAmount();
        config.set("Claim Amount", defaultChunkAmount);
        config.set("Last Time Played", System.currentTimeMillis());

        playerData.setConfig(config);
        playerData.setClaimAmount(defaultChunkAmount);

        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerData;
    }

    public void save(PlayerData data){

        UUID uuid = data.getUUID();
        File playerDataFile = new File(plugin.getDataFolder() + "/playerData", uuid.toString() + ".yml");
        FileConfiguration config = data.getConfig();

        config.set("Claim Amount", data.getClaimAmount());
        config.set("Last Time Played", System.currentTimeMillis());

        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasDataFiles(UUID uuid){
        return new File(plugin.getDataFolder() + "/playerData", uuid.toString() + ".yml").exists();
    }

}
