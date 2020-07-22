package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.commands.ClaimCommand;
import net.dohaw.play.landclaiming.commands.ConfirmableCommands;
import net.dohaw.play.landclaiming.commands.LandCommand;
import net.dohaw.play.landclaiming.events.PlayerWatcher;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.runnables.ClaimGiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public final class LandClaiming extends APIHook {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        hookAPI(this);
        validateFiles();

        this.baseConfig = new BaseConfig(this);
        this.messagesConfig = new MessagesConfig(this);
        this.defaultRegionFlagsConfig = new DefaultRegionFlagsConfig(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.regionDataManager = new RegionDataManager(this);

        for(Player player : Bukkit.getOnlinePlayers()){
            playerDataManager.loadPlayerData(player.getUniqueId());
        }

        regionDataManager.loadData();

        registerEvents();
        registerCommands();
        startRunnables();

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
        getServer().getPluginCommand("claim").setExecutor(new ClaimCommand(this));
        getServer().getPluginCommand("confirmable").setExecutor(new ConfirmableCommands(this));
    }

    private void startRunnables(){
        new ClaimGiver(this).runTaskTimer(this, 1200L, 72000L);
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml"), new File(getDataFolder(), "defaultRegionFlags.yml")};

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
