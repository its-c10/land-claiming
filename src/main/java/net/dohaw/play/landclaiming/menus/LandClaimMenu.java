package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class LandClaimMenu extends Menu implements Listener {

    private RegionDataManager regionDataManager;
    private ChatFactory chatFactory;
    private final String PREFIX;

    public LandClaimMenu(APIHook plugin) {
        super(plugin, "Land Claim", 27);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.regionDataManager = ((LandClaiming)plugin).getRegionDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.PREFIX = ((LandClaiming) plugin).getBaseConfig().getPluginPrefix();
    }

    @Override
    public void initializeItems(Player player) {

        inv.setItem(12, createGuiItem(Material.LAPIS_LAZULI, "&9&lPlayer Claims", new ArrayList<>()));
        inv.setItem(14, createGuiItem(Material.REDSTONE, "&4&lAdmin Claims", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        if(customInvHelper.isWithinInventory(inv, e)){
            if(customInvHelper.isValidClickedItem(e, fillerMat)){
                if(e.getCurrentItem().getType() != fillerMat){
                    Menu newMenu;
                    if(e.getSlot() == 12){
                        newMenu = new DescriptionMenu(plugin, "Players Claim Types", RegionType.NORMAL);
                    }else if(e.getSlot() == 14){
                        if(!player.hasPermission("land.admin")){
                            player.closeInventory();
                            chatFactory.sendPlayerMessage("You don't have permission to do that!", true, player, PREFIX);
                            return;
                        }
                        newMenu = new DescriptionMenu(plugin, "Admin Claim Types", RegionType.ADMIN);
                    }else{
                        return;
                    }
                    newMenu.initializeItems(player);
                    newMenu.openInventory(player);
                }
            }
            e.setCancelled(true);
        }
    }
}
