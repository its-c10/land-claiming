package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfirmableCommands implements CommandExecutor {

    private LandClaiming plugin;
    private ChatFactory chatFactory;
    private PlayerDataManager playerDataManager;
    private MessagesConfig messagesConfig;

    private final String BUTTON = "[HERE]";

    public ConfirmableCommands(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }

    private void sendClaimSuccessMessage(Player player, RegionData regionData){
        int numAvailableClaims = playerDataManager.getNumClaimsAvailable(player.getUniqueId());
        String msg = messagesConfig.getMessage(Message.LAND_CLAIM_SUCCESS);
        msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(numAvailableClaims));
        player.spigot().sendMessage(Utils.createButtonMsg(chatFactory, msg, BUTTON, "/land " + regionData.getName(), "Manager Claim"));
    }

}
