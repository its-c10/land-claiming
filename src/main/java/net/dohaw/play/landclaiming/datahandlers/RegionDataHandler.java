package net.dohaw.play.landclaiming.datahandlers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.region.*;
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

    public RegionData load(UUID playerUUID, UUID regionUUID){

        File regionFile = new File(plugin.getDataFolder() + "/data/" + playerUUID.toString() + "/regionData", regionUUID.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(regionFile);

        RegionDescription desc = RegionDescription.valueOf(config.getString("Description"));
        RegionType type = RegionType.valueOf(config.getString("Type"));
        UUID uid = UUID.fromString(config.getString("UID"));
        UUID ownerUUID = UUID.fromString(config.getString("Owner"));
        RegionData regionData = new RegionData(uid, desc, type);

        regionData.setConfig(config);
        regionData.setFlags(loadFlags(config));
        regionData.setOwnerUUID(ownerUUID);
        return regionData;
    }

    public RegionData create(UUID playerUUID, UUID regionUID, RegionDescription desc, RegionType type){
        RegionData newRegionData = new RegionData(regionUID, desc, type);

        File regionFile = new File(plugin.getDataFolder() + "/data/" + playerUUID.toString() + "/regionData", regionUID.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(regionFile);

        newRegionData.setConfig(config);
        newRegionData.setFlags(defaultRegionFlagsConfig.loadDefaultFlags(type));
        newRegionData.setOwnerUUID(playerUUID);
        return newRegionData;
    }

    public void save(RegionData data){

        File regionFile = new File(plugin.getDataFolder() + "/data/" + data.getOwnerUUID().toString() + "/regionData", data.getChunkUUID().toString() + ".yml");
        FileConfiguration config = data.getConfig();
        config.set("Description", data.getDescription().name());
        config.set("Type", data.getType().name());
        config.set("UID", data.getChunkUUID());
        config.set("Owner", data.getOwnerUUID().toString());

        List<PlayerData> trustedPlayers = data.getTrustedPlayers();
        List<String> trustedPlayerStr = new ArrayList<>();
        trustedPlayers.forEach(d -> trustedPlayerStr.add(d.getUUID().toString()));
        config.set("Trusted Players", trustedPlayerStr);

        Iterator<Map.Entry<RegionFlagType, RegionFlag>> it = data.getFlags().entrySet().iterator();

        while(it.hasNext()){
            String key = it.next().getKey().name();
            boolean enabled = it.next().getValue().isEnabled();
            config.set("Flags." + key, enabled);
        }

        try{
            config.save(regionFile);
        }catch(IOException e){
            e.printStackTrace();
        }

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
