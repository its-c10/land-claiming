package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerClaimMenu extends Menu {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private List<RegionData> regionData;

    public PlayerClaimMenu(APIHook plugin) {
        super(plugin, "Player Claim Types", 9);
        this.playerDataManager = ((LandClaiming)plugin).getPlayerDataManager();
        this.regionDataManager = ((LandClaiming) plugin).getRegionDataManager();
    }

    @Override
    public void initializeItems(Player player) {

        this.regionData = regionDataManager.getPlayerRegionData(player.getUniqueId());

        inv.addItem(createGuiItem(Material.SPONGE, "&eAll Regions", regionData.size() == 0 ? 1 : regionData.size(), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.SAND, "&eHome", getNumRegionType(RegionDescription.HOME) == 0 ? 1 : getNumRegionType(RegionDescription.HOME), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.MELON_SEEDS, "&eFarm", getNumRegionType(RegionDescription.FARM) == 0 ? 1 : getNumRegionType(RegionDescription.FARM), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.CHEST, "&eStorage", getNumRegionType(RegionDescription.STORAGE) == 0 ? 1 : getNumRegionType(RegionDescription.STORAGE), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.GOLD_INGOT, "&eMarket", getNumRegionType(RegionDescription.MARKET) == 0 ? 1 : getNumRegionType(RegionDescription.MARKET), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.ZOMBIE_HEAD, "&eMob Grinder", getNumRegionType(RegionDescription.MOB_GRINDER) == 0 ? 1 : getNumRegionType(RegionDescription.MOB_GRINDER), new ArrayList<>()));
        inv.addItem(createGuiItem(Material.SLIME_BALL, "&eGeneral", getNumRegionType(RegionDescription.GENERAL) == 0 ? 1 : getNumRegionType(RegionDescription.GENERAL), new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    private int getNumRegionType(RegionDescription desc){
        int num = 0;
        for(RegionData data : regionData){
            if(data.getDescription() == desc){
                num++;
            }
        }
        return num;
    }

}
