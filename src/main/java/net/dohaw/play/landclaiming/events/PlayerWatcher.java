package net.dohaw.play.landclaiming.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerWatcher implements Listener {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private ChatFactory chatFactory;

    public PlayerWatcher(LandClaiming plugin){
        this.playerDataManager = plugin.getPlayerDataManager();
        this.regionDataManager = plugin.getRegionDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.messagesConfig = plugin.getMessagesConfig();
        this.baseConfig = plugin.getBaseConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(playerDataManager.hasDataOnRecord(player.getUniqueId())){
            if(!playerDataManager.hasDataLoaded(player.getUniqueId())){
                playerDataManager.loadPlayerData(player.getUniqueId());
            }
        }else{
            playerDataManager.createPlayerData(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(playerDataManager.hasDataLoaded(player.getUniqueId())){
            playerDataManager.saveData(player.getUniqueId());
        }
    }

    @EventHandler
    public void autoClaim(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(player.hasMetadata("auto claim")){
            Chunk playerChunk = player.getLocation().getChunk();
            if(!regionDataManager.hasData(playerChunk)){
                PlayerData playerData = playerDataManager.getData(player.getUniqueId());
                String msg;
                if(playerData.getClaimAmount() != 0){
                    RegionData rd = regionDataManager.create(player.getUniqueId(), playerChunk, RegionDescription.GENERAL, RegionType.NORMAL);
                    /*
                        Created
                     */
                    if(rd != null){
                        msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
                        int numAvailableClaims = playerData.getClaimAmount();
                        msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(numAvailableClaims));
                        chatFactory.sendPlayerMessage(msg, true, player, baseConfig.getPluginPrefix());

                        playerData.setClaimAmount(numAvailableClaims - 1);
                        playerDataManager.setData(player.getUniqueId(), playerData);
                    }else{
                        chatFactory.sendPlayerMessage("There was an error while trying to claim this chunk!", true, player, baseConfig.getPluginPrefix());
                    }
                }else{
                    msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL);
                    chatFactory.sendPlayerMessage(msg, true, player, baseConfig.getPluginPrefix());
                }
            }
        }
    }

}
