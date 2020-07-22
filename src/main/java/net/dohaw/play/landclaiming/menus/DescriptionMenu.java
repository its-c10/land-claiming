package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class DescriptionMenu extends Menu implements Listener {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private List<RegionData> regionData;
    private RegionType typeOfRegions;

    public DescriptionMenu(APIHook plugin, String menuTitle, RegionType typeOfRegions) {
        super(plugin, menuTitle, 9);
        this.playerDataManager = ((LandClaiming)plugin).getPlayerDataManager();
        this.regionDataManager = ((LandClaiming) plugin).getRegionDataManager();
        this.typeOfRegions = typeOfRegions;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {

        this.regionData = regionDataManager.getPlayerRegionData(player.getUniqueId());

        inv.addItem(createGuiItem(Material.SPONGE, "&eAll Regions", getNumRegionType(null) == 0 ? 1 : getNumRegionType(null), new ArrayList<>()));
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
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(customInvHelper.isWithinInventory(inv, e)) {
            if (customInvHelper.isValidClickedItem(e, fillerMat)) {
                int slot = e.getSlot();
                Menu newMenu;
                switch(slot){
                    case 0:
                        newMenu = new ClaimDisplayMenu(plugin, "All Regions", null, 0, typeOfRegions);
                        break;
                    case 1:
                        newMenu = new ClaimDisplayMenu(plugin, "Home Regions", RegionDescription.HOME, 0, typeOfRegions);
                        break;
                    case 2:
                        newMenu = new ClaimDisplayMenu(plugin, "Farm Regions", RegionDescription.FARM, 0, typeOfRegions);
                        break;
                    case 3:
                        newMenu = new ClaimDisplayMenu(plugin, "Storage Regions", RegionDescription.STORAGE, 0, typeOfRegions);
                        break;
                    case 4:
                        newMenu = new ClaimDisplayMenu(plugin, "Market Regions", RegionDescription.MARKET, 0, typeOfRegions);
                        break;
                    case 5:
                        newMenu = new ClaimDisplayMenu(plugin, "Mob Grinder Regions", RegionDescription.MOB_GRINDER, 0, typeOfRegions);
                        break;
                    case 6:
                        newMenu = new ClaimDisplayMenu(plugin, "General Regions", RegionDescription.GENERAL, 0, typeOfRegions);
                        break;
                    case 8:
                        newMenu = new LandClaimMenu(plugin);
                        break;
                    default:
                        return;
                }

                if(newMenu != null){
                    player.closeInventory();
                    newMenu.initializeItems(player);
                    newMenu.openInventory(player);
                }

            }
            e.setCancelled(true);
        }
    }

    private int getNumRegionType(RegionDescription desc){
        int num = 0;
        for(RegionData data : regionData){
            if(desc == null){
                if(data.getType() == typeOfRegions){
                    num++;
                }
            }else{
                if(data.getDescription() == desc && data.getType() == typeOfRegions){
                    num++;
                }
            }
        }
        return num;
    }

}
