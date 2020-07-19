package net.dohaw.play.landclaiming.datahandlers;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.region.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RegionDataHandler {

    private LandClaiming plugin;

    public RegionDataHandler(LandClaiming plugin){
        this.plugin = plugin;
    }

    public RegionData load(UUID playerUUID, UUID regionUUID){

        File regionFile = new File(plugin.getDataFolder() + "/data/" + playerUUID.toString() + "/regionData", regionUUID.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(regionFile);

        RegionDescription desc = RegionDescription.valueOf(config.getString("Description"));
        RegionType type = RegionType.valueOf(config.getString("Type"));
        UUID uid = UUID.fromString(config.getString("UID"));
        RegionData regionData = new RegionData(uid, desc, type);

        regionData.setConfig(config);
        regionData.setFlags(loadFlags(config));
        return regionData;
    }

    public void save(RegionData data){

        FileConfiguration config = data.getConfig();
        config.set("Description", data.getDescription().name());
        config.set("Type", data.getType().name());
        config.set("UID", data.getChunkUUID());

        Iterator<Map.Entry<RegionFlagType, RegionFlag>> it = data.getFlags().entrySet().iterator();

        while(it.hasNext()){
            String key = it.next().getKey().name();
            boolean enabled = it.next().getValue().isEnabled();
            config.set("Flags." + key, enabled);
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
