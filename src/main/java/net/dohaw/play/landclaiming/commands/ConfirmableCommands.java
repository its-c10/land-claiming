package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ConfirmableCommands implements CommandExecutor {

    private LandClaiming plugin;
    private ChatFactory chatFactory;
    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private MessagesConfig messagesConfig;
    private final String PREFIX;

    private final String BUTTON = "[HERE]";

    public ConfirmableCommands(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.regionDataManager = plugin.getRegionDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args[0].equalsIgnoreCase("landclaim") && args.length == 4){

                String decision = args[1];
                String descStr = args[3];
                RegionDescription desc = RegionDescription.valueOf(descStr);

                if(decision.equalsIgnoreCase("yes")){
                    RegionType type = RegionType.NORMAL;
                    Chunk chunk = player.getLocation().getChunk();

                    if(player.hasPermission("land.admin")){
                        type = RegionType.ADMIN;
                    }

                    SingleRegionData rd = regionDataManager.create(player.getUniqueId(), chunk, desc, type);

                    if(rd != null){
                        reduceClaimAmount(player);
                        sendClaimSuccessMessage(player, rd);
                    }else{
                        chatFactory.sendPlayerMessage("There was an error while trying to claim this chunk!", true, player, PREFIX);
                    }
                }

            }else if(args[0].equalsIgnoreCase("landclaim") && args.length == 2){
                String msg = messagesConfig.getMessage(Message.LAND_CLAIM_NO);
                chatFactory.sendPlayerMessage(msg, true, player, PREFIX);
                player.setMetadata("shut up", new FixedMetadataValue(plugin, true));
            }else if(args[0].equalsIgnoreCase("unclaim") && args.length == 4){

                String decision = args[1];
                String regionName = args[3];

                if(decision.equalsIgnoreCase("yes")){
                    if(regionDataManager.getDataFromName(regionName) != null){

                    }
                }

            }else if(args[0].equalsIgnoreCase("unclaim") && args.length == 2){

            }

        }

        return false;
    }

    private void sendClaimSuccessMessage(Player player, SingleRegionData singleRegionData){
        int numAvailableClaims = playerDataManager.getNumClaimsAvailable(player.getUniqueId());
        String msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
        msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(numAvailableClaims));
        player.spigot().sendMessage(Utils.createButtonMsg(chatFactory, msg, BUTTON, "/land " + singleRegionData.getName(), "Manager Claim"));
    }

    private void reduceClaimAmount(Player player){
        PlayerData playerData = playerDataManager.getData(player.getUniqueId());
        playerData.setClaimAmount(playerData.getClaimAmount() - 1);
        playerDataManager.setData(player.getUniqueId(), playerData);
    }

}
