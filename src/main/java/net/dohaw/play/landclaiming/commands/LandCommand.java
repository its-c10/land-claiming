package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.menus.LandClaimMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
    /land
 */
public class LandCommand implements CommandExecutor {

    private LandClaiming plugin;
    private LandClaimMenu landClaimMenu;
    private ChatFactory chatFactory;
    private final String PREFIX;

    public LandCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.landClaimMenu = new LandClaimMenu(plugin);
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                landClaimMenu.initializeItems(player);
                landClaimMenu.openInventory(player);
            }else{
                chatFactory.sendPlayerMessage("This command can only be used by players!", true, sender, PREFIX);
            }
        }

        return false;
    }
}
