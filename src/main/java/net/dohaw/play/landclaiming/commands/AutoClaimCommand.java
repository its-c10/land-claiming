package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.region.RegionDescription;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class AutoClaimCommand implements CommandExecutor {

    private LandClaiming plugin;
    private ChatFactory chatFactory;

    public AutoClaimCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 1){
                if(!player.hasMetadata("auto claim")){
                    String descStr = args[0];
                    if(RegionDescription.getByAlias(descStr) != null){
                        RegionDescription desc = RegionDescription.getByAlias(descStr);
                        player.setMetadata("auto claim", new FixedMetadataValue(plugin, desc.name()));
                        chatFactory.sendPlayerMessage("You have &aenabled &fauto claiming! These claims will be " + StringUtils.capitalize(desc.toString()) + " claims!", true, player, plugin.getBaseConfig().getPluginPrefix());
                    }else{
                        chatFactory.sendPlayerMessage("This is not a valid region type!", true, player, plugin.getBaseConfig().getPluginPrefix());
                    }
                }else{
                    chatFactory.sendPlayerMessage("To disable auto claim, type &6/autoclaim", true, player, plugin.getBaseConfig().getPluginPrefix());
                }
            }else if(args.length == 0){
                if(player.hasMetadata("auto claim")){
                    player.removeMetadata("auto claim", plugin);
                    chatFactory.sendPlayerMessage("You have &cdisabled &fauto claiming!", true, player, plugin.getBaseConfig().getPluginPrefix());
                }else{
                    chatFactory.sendPlayerMessage("To turn on auto claim, use the command &6/autoclaim <region type>", true, player, plugin.getBaseConfig().getPluginPrefix());
                }
            }
        }

        return false;
    }
}
