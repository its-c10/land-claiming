package net.dohaw.play.landclaiming.runnables;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class InactivityTracker extends BukkitRunnable {

    private LandClaiming plugin;
    private BaseConfig baseConfig;
    private RegionDataManager regionDataManager;

    public InactivityTracker(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataManager = plugin.getRegionDataManager();
        this.baseConfig = plugin.getBaseConfig();
    }

    @Override
    public void run() {
        List<File> playerDataFiles = Utils.getFilesInConfig(new File(plugin.getDataFolder(), "playerData"));
        for(File f : playerDataFiles){

            String fileName = f.getName();
            int dotIndex = fileName.indexOf(".");
            String uuidStr = fileName.substring(0, dotIndex);
            UUID playerUUID = UUID.fromString(uuidStr);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

            if(!offlinePlayer.isOnline()){

                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                long timeInMillis = config.getLong("Last Time Played");
                long currentTimeMillis = System.currentTimeMillis();
                long difference = currentTimeMillis - timeInMillis;
                int daysOffline = (int) (difference / (1000*60*60*24));

               // int inactivityThresholdInDays = baseConfig.get;
                /*
                if(inactivityThresholdInDays <= daysOffline){

                }*/

            }
        }
    }
}
