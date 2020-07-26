package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.commands.*;
import net.dohaw.play.landclaiming.events.FlagWatcher;
import net.dohaw.play.landclaiming.events.PlayerWatcher;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.runnables.ClaimGiver;
import net.dohaw.play.landclaiming.runnables.DataSaver;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public final class LandClaiming extends APIHook {

    private static Economy econ = null;
    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private DefaultRegionFlagsConfig defaultRegionFlagsConfig;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        hookAPI(this);

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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
        Bukkit.getPluginManager().registerEvents(new FlagWatcher(this), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("land").setExecutor(new LandCommand(this));
        getServer().getPluginCommand("claim").setExecutor(new ClaimCommand(this));
        getServer().getPluginCommand("confirmable").setExecutor(new ConfirmableCommands(this));
        getServer().getPluginCommand("autoclaim").setExecutor(new AutoClaimCommand(this));
        getServer().getPluginCommand("unclaim").setExecutor(new UnclaimCommand(this));
        getServer().getPluginCommand("trust").setExecutor(new TrustCommand(this));
    }

    private void startRunnables(){
        new ClaimGiver(this).runTaskTimer(this, 72000L, 72000L);
        new DataSaver(this).runTaskTimer(this, 18000L, 18000L);
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
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

    public static Economy getEconomy(){
        return econ;
    }
}
