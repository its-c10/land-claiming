package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.ConnectedRegionData;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.SingleRegionData;
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
    private MessagesConfig messagesConfig;

    public TrustCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.regionDataManager = plugin.getRegionDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.messagesConfig = plugin.getMessagesConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")){
                String decision = args[0];
                String playerName = args[1];
                if(Bukkit.getPlayer(playerName) != null){

                    Player pl = Bukkit.getPlayer(playerName);
                    UUID uuid = pl.getUniqueId();

                    if(uuid.equals(player.getUniqueId())){
                        chatFactory.sendPlayerMessage("You can't use this command on yourself!", true, player, plugin.getBaseConfig().getPluginPrefix());
                        return false;
                    }

                    if(regionDataManager.hasData(player.getLocation().getChunk())){
                        RegionData data = regionDataManager.getDataFromChunk(player.getLocation().getChunk());
                        if(decision.equalsIgnoreCase("add")){
                            if(!data.isTrusted(uuid)){

                                data.addTrustedPlayer(uuid);

                                String msg = messagesConfig.getMessage(Message.PLAYER_TRUSTED);
                                msg = Utils.replacePlaceholders("%player%", msg, pl.getName());
                                chatFactory.sendPlayerMessage(msg, true, player, plugin.getBaseConfig().getPluginPrefix());

                                if(data instanceof ConnectedRegionData){
                                    for(SingleRegionData srd : ((ConnectedRegionData)data).getConnectedData()){
                                        srd.addTrustedPlayer(uuid);
                                    }
                                }

                                regionDataManager.setRegionData(data);

                            }else{
                                chatFactory.sendPlayerMessage("This player is already trusted in this region!", true, player, plugin.getBaseConfig().getPluginPrefix());
                            }
                        }else{

                            if(data.isTrusted(uuid)){

                                data.removeTrustedPlayer(uuid);

                                String msg = messagesConfig.getMessage(Message.PLAYER_UNTRUSTED);
                                msg = Utils.replacePlaceholders("%player%", msg, pl.getName());
                                chatFactory.sendPlayerMessage(msg, true, player, plugin.getBaseConfig().getPluginPrefix());

                                if(data instanceof ConnectedRegionData){
                                    for(SingleRegionData srd : ((ConnectedRegionData)data).getConnectedData()){
                                        srd.removeTrustedPlayer(uuid);
                                    }
                                }

                                regionDataManager.setRegionData(data);

                            }else{
                                chatFactory.sendPlayerMessage("This player is not trusted in this region!", true, player, plugin.getBaseConfig().getPluginPrefix());
                            }
                        }
                    }

                }else{
                    chatFactory.sendPlayerMessage("This player is either not online or isn't valid!", true, player, plugin.getBaseConfig().getPluginPrefix());
                }
            }
        }

        return false;
    }
}
