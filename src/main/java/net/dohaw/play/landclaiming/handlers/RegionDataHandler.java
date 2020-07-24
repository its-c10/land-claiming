package net.dohaw.play.landclaiming.handlers;

import me.c10coding.coreapi.serializers.LocationSerializer;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RegionDataHandler {

    private LandClaiming plugin;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;

    public RegionDataHandler(LandClaiming plugin){
        this.plugin = plugin;
        this.defaultRegionFlagsConfig = plugin.getDefaultRegionFlagsConfig();
    }

    public List<SingleRegionData> load(){

        List<SingleRegionData> singleRegionDataList = new ArrayList<>();
        List<File> regionConfigs = Utils.getFilesInConfig(new File(plugin.getDataFolder(), "regionData"));

        for(File file : regionConfigs){

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            RegionDescription desc = RegionDescription.valueOf(config.getString("Description"));
            RegionType type = RegionType.valueOf(config.getString("Type"));
            UUID ownerUUID = UUID.fromString(config.getString("Owner"));
            String regionName = config.getString("Name");

            LocationSerializer ls = plugin.getAPI().createLocationSerializer(config);
            Location regionLocation = ls.toLocationFromPath("Location");
            Chunk chunk = regionLocation.getChunk();

            SingleRegionData singleRegionData = new SingleRegionData(regionName, chunk, desc, type);

            singleRegionData.setFile(file);
            singleRegionData.setConfig(config);
            singleRegionData.setFlags(loadFlags(config));
            singleRegionData.setOwnerUUID(ownerUUID);

            singleRegionDataList.add(singleRegionData);
        }
        return singleRegionDataList;
    }

    public SingleRegionData create(UUID ownerUUID, Chunk chunk, RegionDescription desc, RegionType type){

        int num = 1;
        String regionName = getRegionName(ownerUUID, num);
        File regionFile = new File(plugin.getDataFolder() + "/regionData", regionName + ".yml");

        try{
            while(!regionFile.createNewFile()){
                num++;
                regionName = getRegionName(ownerUUID, num);
                regionFile = new File(plugin.getDataFolder() + "/regionData", regionName + ".yml");
            }
        }catch(IOException e){
            plugin.getLogger().severe("Could not create the region file " + regionFile.getName());
            e.printStackTrace();
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(regionFile);
        SingleRegionData newSingleRegionData = new SingleRegionData(regionName, chunk, desc, type);

        newSingleRegionData.setFile(regionFile);
        newSingleRegionData.setConfig(config);
        newSingleRegionData.setFlags(defaultRegionFlagsConfig.loadDefaultFlags(type));
        newSingleRegionData.setOwnerUUID(ownerUUID);

        save(newSingleRegionData);
        return newSingleRegionData;
    }

    private String getRegionName(UUID ownerUUID, int num){
        String playerName = Bukkit.getPlayer(ownerUUID).getName();
        return playerName + "_" + (num);
    }

    public void save(SingleRegionData data){

        File file = data.getFile();
        FileConfiguration config = data.getConfig();
        LocationSerializer ls = plugin.getAPI().createLocationSerializer(config);

        config.set("Description", data.getDescription().name());
        config.set("Type", data.getType().name());
        config.set("Owner", data.getOwnerUUID().toString());
        config.set("Name", data.getName());
        ls.storeLocation("Location", data.getChunk().getBlock(0, 0,0).getLocation());

        List<UUID> trustedPlayers = data.getTrustedPlayers();
        List<String> trustedPlayerStr = new ArrayList<>();
        trustedPlayers.forEach(d -> trustedPlayerStr.add(d.toString()));
        config.set("Trusted Players", trustedPlayerStr);

        Iterator<Map.Entry<RegionFlagType, RegionFlag>> it = data.getFlags().entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<RegionFlagType, RegionFlag> entry = it.next();
            String key = entry.getKey().name();
            boolean enabled = entry.getValue().isEnabled();
            config.set("Flags." + key, enabled);
        }

        try{
            config.save(file);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void delete(SingleRegionData data){
        File file = data.getFile();
        file.delete();
    }

    private EnumMap<RegionFlagType, RegionFlag> loadFlags(FileConfiguration config){
        EnumMap<RegionFlagType, RegionFlag> flags = new EnumMap<>(RegionFlagType.class);
        for(RegionFlagType type : RegionFlagType.values()){
            boolean enabled = config.getBoolean("Flags." + type.name());
            flags.put(type, new RegionFlag(enabled));
        }
        return flags;
    }

}
