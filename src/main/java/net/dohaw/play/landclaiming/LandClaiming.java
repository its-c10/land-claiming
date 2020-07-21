package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.commands.LandCommand;
import net.dohaw.play.landclaiming.datahandlers.RegionDataHandler;
import net.dohaw.play.landclaiming.events.PlayerWatcher;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.files.PlayersWithRegionsConfig;
import org.bukkit.Bukkit;

import java.io.File;

public final class LandClaiming extends APIHook {

    private DataManager dataManager;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private RegionDataHandler regionDataHandler;
    private PlayersWithRegionsConfig playersWithRegionsConfig;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles();

        this.playersWithRegionsConfig = new PlayersWithRegionsConfig(this);
        this.defaultRegionFlagsConfig = new DefaultRegionFlagsConfig(this);
        this.regionDataHandler = new RegionDataHandler(this);
        this.dataManager = new DataManager(this);
        this.messagesConfig = new MessagesConfig(this);
        this.baseConfig = new BaseConfig(this);

        dataManager.loadData();

        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        if(dataManager != null){
            dataManager.shutDown();
        }
        dataManager = null;
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerWatcher(this), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("land").setExecutor(new LandCommand(this));
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml"), new File(getDataFolder(), "defaultRegionFlags.yml"), new File(getDataFolder(), "playersWithRegions.yml")};

        File playerDataFolder = new File(getDataFolder(), "data");

        if(!playerDataFolder.exists()){
            playerDataFolder.mkdirs();
        }

        for(File f : files){
            if(!f.exists()){
                saveResource(f.getName(), false);
            }
        }
        getLogger().info("Validated files!");
    }

    public DataManager getDataManager(){
        return dataManager;
    }

    public RegionDataHandler getRegionDataHandler(){
        return regionDataHandler;
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
