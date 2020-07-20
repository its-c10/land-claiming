package net.dohaw.play.landclaiming.events;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerWatcher {

    private LandClaiming plugin;
    private PlayerDataManager

    public PlayerWatcher(LandClaiming plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(){

        }
    }

}
