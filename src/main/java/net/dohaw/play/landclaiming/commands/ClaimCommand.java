package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {

    private LandClaiming plugin;
    private RegionDataManager regionDataManager;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private final String PREFIX;

    private final String BUTTON = "[HERE]";

    public ClaimCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.regionDataManager = plugin.getRegionDataManager();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
        this.baseConfig = plugin.getBaseConfig();
        this.PREFIX = baseConfig.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            Location playerLocation = player.getLocation();
            String msg;
            if(player.hasPermission("land.claim")){
                if(args.length == 1){
                    String typeAlias = args[0];
                    RegionDescription desc = RegionDescription.getByAlias(typeAlias);
                    if(desc != null){

                        if(Utils.isAdminDescription(desc) && !player.hasPermission("land.admin")){
                            msg = "You do not have permission to use this claim type!";
                            chatFactory.sendPlayerMessage(msg, true, sender, PREFIX);
                            return false;
                        }

                        if(playerDataManager.getNumClaimsAvailable(player.getUniqueId()) > 0 || player.hasPermission("land.nolimit")){
                            if(!regionDataManager.hasData(playerLocation)){

                                msg = messagesConfig.getMessage(Message.LAND_CLAIM);
                                sendConfirmButton(msg, player, desc);

                            }else{
                                sendAlreadyClaimedMsg(sender, playerLocation);
                            }
                        }else{
                            msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL) ;
                            chatFactory.sendPlayerMessage(msg, true, player, PREFIX);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("This is not a valid chunk type!", true, sender, PREFIX);
                    }

                }else if(args.length == 0){
                    RegionDescription desc = RegionDescription.GENERAL;
                    if(playerDataManager.getNumClaimsAvailable(player.getUniqueId()) > 0 || player.hasPermission("land.nolimit")) {
                        if (!regionDataManager.hasData(playerLocation)) {
                            msg = messagesConfig.getMessage(Message.LAND_CLAIM);
                            sendConfirmButton(msg, player, desc);
                        }else{
                            sendAlreadyClaimedMsg(sender, playerLocation);
                        }
                    }else{
                        msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL) ;
                        chatFactory.sendPlayerMessage(msg, true, player, PREFIX);
                    }
                }
            }else{
                chatFactory.sendPlayerMessage("You do not have permission to claim land!", true, sender, PREFIX);
            }
        }
        return false;
    }

    private void sendConfirmButton(String msg, Player player, RegionDescription desc){
        String button1 = "[YES]";
        String button2 = "[NO]";
        TextComponent buttonMsg = Utils.createButtonMsg(chatFactory, msg, button1, button2, "/confirmable landclaim yes " + player.getUniqueId() + " " + desc.name(), "/confirmable landclaim no", "Claim Land", "Abort...");
        player.spigot().sendMessage(buttonMsg);
    }

    private void sendAlreadyClaimedMsg(CommandSender sender, Location playerLocation){
        SingleRegionData rd = regionDataManager.getDataFromLocation(playerLocation);
        OfflinePlayer regionOwner = Bukkit.getOfflinePlayer(rd.getOwnerUUID());
        chatFactory.sendPlayerMessage("This chunk has already been claimed by &e&l" + regionOwner.getName() + "!", true, sender, PREFIX);
    }




}
