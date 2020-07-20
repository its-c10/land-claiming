package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerClaimMenu extends Menu {

    private PlayerDataManager playerDataManager;

    public PlayerClaimMenu(APIHook plugin) {
        super(plugin, "Player Claims", 54);
        this.playerDataManager = ((LandClaiming)plugin).getPlayerDataManager();
    }

    @Override
    public void initializeItems(Player player) {
        PlayerData data = playerDataManager.getPlayerData(player.getUniqueId());
        HashMap<UUID, RegionData> regionDataMap = data.getRegions();

        Iterator<Map.Entry<UUID, RegionData>> itr = regionDataMap.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<UUID, RegionData> entry = itr.next();
            RegionData rData = entry.getValue();
        }

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(true);
    }

    @Override
    protected void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
