package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.menus.LandClaimMenu;
import net.dohaw.play.landclaiming.menus.RegionFlagMenu;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionFlag;
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
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                landClaimMenu.initializeItems(player);
                landClaimMenu.openInventory(player);
            }else if(args.length == 1){
                RegionDataManager regionDataManager = plugin.getRegionDataManager();
                String potRegionName = args[0];
                if(regionDataManager.getDataFromName(potRegionName) != null) {
                    RegionData rd = regionDataManager.getDataFromName(potRegionName);
                    RegionFlagMenu menu = new RegionFlagMenu(plugin, potRegionName, rd.getDescription(), rd.getType());
                    menu.initializeItems(player);
                    menu.openInventory(player);
                }
            }
        }else{
            chatFactory.sendPlayerMessage("This command can only be used by players!", true, sender, PREFIX);
        }
        return false;
    }
}
