package net.dohaw.play.landclaiming;

import me.c10coding.coreapi.APIHook;

import java.io.File;

public final class LandClaiming extends APIHook {

    @Override
    public void onEnable() {
        hookAPI(this);
        validateFiles();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void validateFiles(){
        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "messages.yml")};
        for(File f : files){
            if(!f.exists()){
                saveResource(f.getName(), false);
            }
        }
        getLogger().info("Validated files!");
    }

}
