package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
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

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                if(player.hasMetadata("auto claim")){
                    player.removeMetadata("auto claim", plugin);
                    chatFactory.sendPlayerMessage("You have &cdisabled &fauto claiming!", true, player, plugin.getBaseConfig().getPluginPrefix());
                }else{
                    player.setMetadata("auto claim", new FixedMetadataValue(plugin, true));
                    chatFactory.sendPlayerMessage("You have &aenabled &fauto claiming!", true, player, plugin.getBaseConfig().getPluginPrefix());
                }
            }
        }

        return false;
    }
}
