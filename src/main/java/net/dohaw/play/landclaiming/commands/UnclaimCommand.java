package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionType;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnclaimCommand implements CommandExecutor {

    private RegionDataManager regionDataManager;
    private MessagesConfig messagesConfig;
    private ChatFactory chatFactory;
    private final String PREFIX;

    public UnclaimCommand(LandClaiming plugin){
        this.regionDataManager = plugin.getRegionDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            Location playerLocation = player.getLocation();
            if(regionDataManager.hasData(playerLocation)){
                RegionData rd = regionDataManager.getDataFromLocation(playerLocation);
                /*
                    Don't need to check for the owner if it's an admin claim
                 */
                if(rd.getType() == RegionType.NORMAL){
                    if(rd.getOwnerUUID().equals(player.getUniqueId())){
                        sendConfirmationMessage(player, rd);
                    }else{
                        chatFactory.sendPlayerMessage("Only the owner of the region can use this command!", true, player, PREFIX);
                    }
                }else{
                    sendConfirmationMessage(player, rd);
                }
            }else{
                chatFactory.sendPlayerMessage("This chunk doesn't have any region data!", true, player, PREFIX);
            }
        }

        return false;
    }

    private void sendConfirmationMessage(Player player, RegionData data){
        String button1 = "[YES]";
        String button2 = "[NO]";
        TextComponent buttonMsg = Utils.createButtonMsg(chatFactory, messagesConfig.getMessage(Message.LAND_UNCLAIM), button1, button2, "/confirmable unclaim yes " + player.getUniqueId() + " " + data.getName(), "/confirmable unclaim no", "Unclaim Land", "Abort...");
        player.spigot().sendMessage(buttonMsg);
    }
}
