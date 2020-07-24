package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
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

    public UnclaimCommand(LandClaiming plugin){
        this.regionDataManager = plugin.getRegionDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            Location playerLocation = player.getLocation();
            if(regionDataManager.hasData(playerLocation)){
                SingleRegionData rd = regionDataManager.getDataFromLocation(playerLocation);
                if(rd.getOwnerUUID().equals(player.getUniqueId())){
                    sendConfirmationMessage(player, rd);
                }
            }
        }

        return false;
    }

    private void sendConfirmationMessage(Player player, SingleRegionData data){
        String button1 = "[YES]";
        String button2 = "[NO]";
        TextComponent buttonMsg = Utils.createButtonMsg(chatFactory, messagesConfig.getMessage(Message.LAND_UNCLAIM), button1, button2, "/confirmable unclaim yes " + player.getUniqueId() + " " + data.getName(), "/confirmable unclaim no", "Unclaim Land", "Abort...");
        player.spigot().sendMessage(buttonMsg);
    }
}
