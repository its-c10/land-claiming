package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminClaimMenu extends Menu {

    public AdminClaimMenu(APIHook plugin, String menuTitle, int numSlots) {
        super(plugin, menuTitle, numSlots);
    }

    @Override
    public void initializeItems(Player player) {

    }

    @Override
    protected void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
