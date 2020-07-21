package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.DataManager;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.region.RegionData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClaimCommand implements CommandExecutor {

    private LandClaiming plugin;
    private DataManager dataManager;
    private ChatFactory chatFactory;

    public ClaimCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.dataManager = plugin.getDataManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            Location playerLocation = player.getLocation();
            UUID chunkUUID = playerLocation.getChunk().
            if(){

            }
            if(dataManager.getPlayerData(player.getUniqueId()) != null){
                PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
                HashMap<UUID, RegionData> regions = playerData.getRegions();
            }else{

            }
        }

        return false;
    }
}
