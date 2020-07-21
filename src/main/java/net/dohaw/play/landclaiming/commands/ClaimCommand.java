package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
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
    private ChatFactory chatFactory;
    private MessagesConfig messagesConfig;
    private BaseConfig baseConfig;
    private final String PREFIX;

    public ClaimCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.regionDataManager = plugin.getRegionDataManager();
        this.messagesConfig = plugin.getMessagesConfig();
        this.baseConfig = plugin.getBaseConfig();
        this.PREFIX = baseConfig.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            Location playerLocation = player.getLocation();
            if(){
                if(!regionDataManager.hasData(playerLocation)){
                    RegionData newRegionData = new RegionData();
                }else{
                    RegionData rd = regionDataManager.getDataFromLocation(playerLocation);
                    OfflinePlayer regionOwner = Bukkit.getOfflinePlayer(rd.getOwnerUUID());
                    chatFactory.sendPlayerMessage("This chunk has already been claimed by &e&l" + regionOwner + "!", true, sender, PREFIX);
                }
            }

        }

        return false;
    }
}
