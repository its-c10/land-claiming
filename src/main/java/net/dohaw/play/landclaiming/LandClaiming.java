package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.commands.LandCommand;
import net.dohaw.play.landclaiming.events.PlayerWatcher;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import org.bukkit.Bukkit;

import java.io.File;

public final class LandClaiming extends APIHook {

    private PlayerDataManager playerDataManager;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles();

        this.playerDataManager = new PlayerDataManager(this);
        this.defaultRegionFlagsConfig = new DefaultRegionFlagsConfig(this);
        this.messagesConfig = new MessagesConfig(this);
        this.baseConfig = new BaseConfig(this);

        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        if(playerDataManager != null){
            playerDataManager.shutDown();
        }
        playerDataManager = null;
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerWatcher(this), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("land").setExecutor(new LandCommand(this));
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml"), new File(getDataFolder(), "defaultRegionFlags.yml")};

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

    public PlayerDataManager getPlayerDataManager(){
        return playerDataManager;
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
