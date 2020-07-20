package net.dohaw.play.landclaiming.events;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerWatcher implements Listener {

    private LandClaiming plugin;
    private PlayerDataManager playerDataManager;

    public PlayerWatcher(LandClaiming plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player playerJoined = e.getPlayer();
        if(playerDataManager.hasExistingPlayerDataFiles(playerJoined.getUniqueId())){
            playerDataManager.loadPlayerData(playerJoined.getUniqueId());
        }else{
            playerDataManager.createPlayerData(playerJoined.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player playerLeft = e.getPlayer();
        if(playerDataManager.getPlayerData(playerLeft.getUniqueId()) != null){
            playerDataManager.removePlayerData(playerLeft.getUniqueId());
        }
    }

}
