package net.dohaw.play.landclaiming.events;

import net.dohaw.play.landclaiming.DataManager;
import net.dohaw.play.landclaiming.LandClaiming;
import org.bukkit.event.Listener;

public class PlayerWatcher implements Listener {

    private LandClaiming plugin;
    private DataManager dataManager;

    public PlayerWatcher(LandClaiming plugin){
        this.plugin = plugin;
        this.dataManager = plugin.getDataManager();
    }

}
