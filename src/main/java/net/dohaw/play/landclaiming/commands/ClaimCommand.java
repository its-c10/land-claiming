package net.dohaw.play.landclaiming.commands;

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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

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
            if(args.length == 1){
                String typeAlias = args[0];
                RegionDescription desc = RegionDescription.getByAlias(typeAlias);
                if(desc != null){
                    if(playerDataManager.getNumClaimsAvailable(player.getUniqueId()) > 0){
                        if(!regionDataManager.hasData(playerLocation)){

                            PlayerData playerData = playerDataManager.getData(player.getUniqueId());
                            playerData.setClaimAmount(playerData.getClaimAmount() - 1);
                            playerDataManager.setData(player.getUniqueId(), playerData);

                            RegionType type = RegionType.NORMAL;
                            Chunk chunk = playerLocation.getChunk();
                            if(player.hasPermission("land.admin")){
                                type = RegionType.ADMIN;
                            }

                            RegionData rd = regionDataManager.create(player.getUniqueId(), chunk, desc, type);
                            if(rd != null){
                                sendSuccessMessage(player);
                            }else{
                                chatFactory.sendPlayerMessage("There was an error while trying to claim this chunk!", true, player, PREFIX);
                            }
                        }else{
                            RegionData rd = regionDataManager.getDataFromLocation(playerLocation);
                            Bukkit.broadcastMessage(regionDataManager.getRegionData().toString());
                            OfflinePlayer regionOwner = Bukkit.getOfflinePlayer(rd.getOwnerUUID());
                            chatFactory.sendPlayerMessage("This chunk has already been claimed by &e&l" + regionOwner + "!", true, sender, PREFIX);
                        }
                    }else{
                        msg = messagesConfig.getMessage(Message.LAND_CLAIM_FAIL) ;
                        chatFactory.sendPlayerMessage(msg, true, player, PREFIX);
                    }
                }else{
                    chatFactory.sendPlayerMessage("This is not a valid chunk type!", true, sender, PREFIX);
                }
            }
        }
        return false;
    }

    private void sendSuccessMessage(Player player){

        int numAvailableClaims = playerDataManager.getNumClaimsAvailable(player.getUniqueId());
        String msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
        msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(numAvailableClaims));

        List<String> arr = Arrays.asList(msg.split(" "));
        int indexButton = 0;
        for(String s : arr){
            if(s.equalsIgnoreCase(BUTTON)){
                indexButton = arr.indexOf(s);
            }
        }

        List<String> firstPartTemp = arr.subList(0, indexButton);
        String firstPart = String.join(" ", firstPartTemp);

        String button = arr.get(indexButton);

        List<String> restOfSentenceTemp = arr.subList(indexButton + 1, arr.size());
        String restOfSentence = String.join(" ", restOfSentenceTemp);

        TextComponent tcMsg1 = new TextComponent(chatFactory.colorString(firstPart) + " ");
        TextComponent tcButton = new TextComponent(chatFactory.colorString(button) + " ");
        TextComponent tcMsg2 = new TextComponent(chatFactory.colorString(restOfSentence));

        tcButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Manager claim").create()));
        tcButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/land " + player.getName()));

        tcMsg1.addExtra(tcButton);
        tcMsg1.addExtra(tcMsg2);

        player.spigot().sendMessage(tcMsg1);

    }


}
