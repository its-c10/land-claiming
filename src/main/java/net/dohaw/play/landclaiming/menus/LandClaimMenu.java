package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class LandClaimMenu extends Menu implements Listener {

    public LandClaimMenu(APIHook plugin) {
        super(plugin, "Land Claim", 27);
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        if(customInvHelper.isValidClickedItem(e)){
            if(e.getCurrentItem().getType() != fillerMat){
                Menu newMenu;
                if(e.getSlot() == 12){

                }else if(e.getSlot() == 14){

                }
            }
        }
    }
}
