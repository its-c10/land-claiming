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
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class PlayerWatcher implements Listener {

    private LandClaiming plugin;
    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private ChatFactory chatFactory;

    public PlayerWatcher(LandClaiming plugin){
        this.plugin = plugin;
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

        if(player.hasMetadata("shut up")){
            player.removeMetadata("shut up", plugin);
        }

    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent e){

        Player player = e.getPlayer();
        int numBlocksPlacedByPlayer = 0;

        if(!player.hasMetadata("shut up") && player.hasMetadata("auto claim")){

            Block block = e.getBlock();
            block.setMetadata("placed by", new FixedMetadataValue(plugin, player.getUniqueId()));
            Chunk chunk = block.getChunk();
            World w = chunk.getWorld();

            int cx = chunk.getX() << 4;
            int cz = chunk.getZ() << 4;
            int cxMax = cx + 16;
            int czMax = cz + 16;

            for(int x = cx; x < cxMax; x++){
                for(int z = cz; z < czMax; z++){
                    for(int y = 0; y < 256; y++){
                        Block b = w.getBlockAt(x, y, z);
                        if(b.hasMetadata("placed by")){
                            String uuidStr = b.getMetadata("placed by").get(0).asString();
                            UUID uuid = UUID.fromString(uuidStr);
                            if(uuid.equals(player.getUniqueId())){
                                numBlocksPlacedByPlayer++;
                            }
                        }
                    }
                }
            }

            int requiredAmountToTriggerClaim = baseConfig.getClaimBlockPlaceAmount();
            if(numBlocksPlacedByPlayer >= requiredAmountToTriggerClaim){
                PlayerData data = playerDataManager.getData(player.getUniqueId());
                String msg;
                if(!regionDataManager.hasData(chunk)){
                    if(data.getClaimAmount() != 0){

                        RegionDescription desc = RegionDescription.valueOf(player.getMetadata("auto claim").get(0).asString());
                        RegionType type = RegionType.NORMAL;
                        SingleRegionData rd = regionDataManager.create(player.getUniqueId(), chunk, desc, type);

                        if(rd != null){
                            playerDataManager.reduceClaimAmount(player.getUniqueId());
                            int numAvailableClaims = playerDataManager.getNumClaimsAvailable(player.getUniqueId());
                            msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
                            msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(numAvailableClaims));
                            player.spigot().sendMessage(Utils.createButtonMsg(chatFactory, msg, "[HERE]", "/land " + rd.getName(), "Manager Claim"));
                        }

                    }
                }
                /*else{
                    msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL);
                    chatFactory.sendPlayerMessage(msg, true, player, baseConfig.getPluginPrefix());
                }*/
            }

        }

    }

    /*
    @EventHandler
    public void autoClaim(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(player.hasMetadata("auto claim")){
            Chunk playerChunk = player.getLocation().getChunk();
            if(!regionDataManager.hasData(playerChunk)){
                PlayerData playerData = playerDataManager.getData(player.getUniqueId());
                String msg;
                if(playerData.getClaimAmount() != 0){
                    //RegionData rd = regionDataManager.create(player.getUniqueId(), playerChunk, RegionDescription.GENERAL, RegionType.NORMAL);

                        Created

                    //if(rd != null){


                        msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
                        int numAvailableClaims = playerData.getClaimAmount();
                        int newNumAvailableClaims = numAvailableClaims - 1;
                        playerData.setClaimAmount(newNumAvailableClaims);
                        playerDataManager.setData(player.getUniqueId(), playerData);

                        msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(newNumAvailableClaims));
                        chatFactory.sendPlayerMessage(msg, true, player, baseConfig.getPluginPrefix());


                        msg = messagesConfig.getMessage(Message.LAND_CLAIM);
                        String button1 = "[YES]";
                        String button2 = "[NO]";
                        RegionDescription desc = RegionDescription.GENERAL;
                        TextComponent buttonMsg = Utils.createButtonMsg(chatFactory, msg, button1, button2, "/confirmable landclaim yes " + player.getUniqueId() + " " + desc.name(), "/confirmable landclaim no", "Claim Land", "Abort...");
                        player.spigot().sendMessage(buttonMsg);

                    }else{
                        chatFactory.sendPlayerMessage("There was an error while trying to claim this chunk!", true, player, baseConfig.getPluginPrefix());
                    }
                }else{
                    msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL);
                    chatFactory.sendPlayerMessage(msg, true, player, baseConfig.getPluginPrefix());
                }
            }
        }
    }*/

}
