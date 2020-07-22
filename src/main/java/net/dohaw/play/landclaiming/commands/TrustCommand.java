package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TrustCommand implements CommandExecutor {

    private RegionDataManager regionDataManager;
    private LandClaiming plugin;
    private ChatFactory chatFactory;

    public TrustCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataManager = plugin.getRegionDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")){
                String playerName = args[1];
                if(Bukkit.getPlayer(playerName) != null){
                    Player addedPlayer = Bukkit.getPlayer(playerName);
                    UUID uuid = addedPlayer.getUniqueId();
                }else{
                    chatFactory.sendPlayerMessage("This is not a valaid player!", true, player, plugin.getBaseConfig().getPluginPrefix());
                }
            }
        }

        return false;
    }
}
