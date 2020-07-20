package net.dohaw.play.landclaiming;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import me.c10coding.coreapi.APIHook;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.DefaultRegionFlagsConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import org.bukkit.entity.Player;

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
    }

    @Override
    public void onDisable() {
        if(playerDataManager != null){
            playerDataManager.shutDown();
        }
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml"), new File(getDataFolder(), "defaultRegionFlags.yml")};

        File playerDataFolder = new File(getDataFolder(), "data");

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
