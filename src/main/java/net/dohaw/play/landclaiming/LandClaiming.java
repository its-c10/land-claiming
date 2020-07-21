package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.commands.LandCommand;
import net.dohaw.play.landclaiming.datahandlers.RegionDataHandler;
import net.dohaw.play.landclaiming.events.PlayerWatcher;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.files.PlayersWithRegionsConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import org.bukkit.Bukkit;

import java.io.File;

public final class LandClaiming extends APIHook {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private PlayersWithRegionsConfig playersWithRegionsConfig;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles();

        this.playersWithRegionsConfig = new PlayersWithRegionsConfig(this);
        this.defaultRegionFlagsConfig = new DefaultRegionFlagsConfig(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.regionDataManager = new RegionDataManager(this);
        this.messagesConfig = new MessagesConfig(this);
        this.baseConfig = new BaseConfig(this);

        regionDataManager.loadData();

        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        if(regionDataManager != null){
            regionDataManager.saveData();
        }
        if(playerDataManager != null){
            playerDataManager.saveData();
        }
        regionDataManager = null;
        playerDataManager = null;
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerWatcher(this), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("land").setExecutor(new LandCommand(this));
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml"), new File(getDataFolder(), "defaultRegionFlags.yml"), new File(getDataFolder(), "playersWithRegions.yml")};

        File playerDataFolder = new File(getDataFolder(), "playerData");
        File regionDataFolder = new File(getDataFolder(), "regionData");

        if(!playerDataFolder.exists()){
            playerDataFolder.mkdirs();
        }

        if(!regionDataFolder.exists()){
            regionDataFolder.mkdirs();
        }

        for(File f : files){
            if(!f.exists()){
                saveResource(f.getName(), false);
            }
        }
        getLogger().info("Validated files!");
    }

    public PlayerDataManager getPlayerDataManager(){
        return playerDataManager;
    }

    public RegionDataManager getRegionDataManager(){
        return regionDataManager;
    }

    public PlayersWithRegionsConfig getPlayersWithRegionsConfig(){
        return playersWithRegionsConfig;
    }

    public DefaultRegionFlagsConfig getDefaultRegionFlagsConfig() {
        return defaultRegionFlagsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }
}
