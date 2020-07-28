package net.dohaw.play.landclaiming.files;

import me.c10coding.coreapi.files.Config;
import net.dohaw.play.landclaiming.region.RegionFlag;
import net.dohaw.play.landclaiming.region.RegionFlagType;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;

public class DefaultRegionFlagsConfig extends Config {

    public DefaultRegionFlagsConfig(JavaPlugin plugin) {
        super(plugin, "defaultRegionFlags.yml");
    }

    public EnumMap<RegionFlagType, RegionFlag> loadDefaultFlags(RegionType regionType){
        EnumMap<RegionFlagType, RegionFlag> defaultFlags = new EnumMap<>(RegionFlagType.class);
        RegionFlagType[] flagTypes = RegionFlagType.values();
        for(RegionFlagType type : flagTypes){
            String configKey = type.getConfigKey();
            if(config.getString(regionType.name() + "." + configKey) != null){
                boolean enabled = config.getBoolean(regionType.name() + "." + configKey);
                defaultFlags.put(type, new RegionFlag(enabled));
            }
        }
        return defaultFlags;
    }

}
