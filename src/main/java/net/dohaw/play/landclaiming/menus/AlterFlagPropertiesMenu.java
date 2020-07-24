package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionFlag;
import net.dohaw.play.landclaiming.region.RegionFlagType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class AlterFlagPropertiesMenu extends Menu implements Listener {

    private SingleRegionData data;
    private RegionDataManager regionDataManager;
    private RegionFlag flag;
    private RegionFlagType fType;
    private String regionName;
    private RegionDescription desc;

    public AlterFlagPropertiesMenu(APIHook plugin, String menuTitle, String regionName, RegionFlagType fType, RegionDescription desc) {
        super(plugin, menuTitle, 9);
        this.regionDataManager = ((LandClaiming)plugin).getRegionDataManager();
        this.data = regionDataManager.getDataFromName(regionName);
        this.fType = fType;
        this.flag = data.getFlags().get(fType);
        this.regionName = regionName;
        this.desc = desc;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {

        HashMap<String, Object> changeSettingData = getChangeSettingData();
        String displayName = (String) changeSettingData.get("Display Name");
        Material mat = (Material) changeSettingData.get("Material");
        inv.setItem(4, createGuiItem(mat, displayName, new ArrayList<>()));

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
                ItemStack itemClicked = e.getCurrentItem();
                Material mat = itemClicked.getType();
                if(e.getSlot() == 4){
                    /*
                        Disable it
                     */
                    if(mat == Material.LIME_CONCRETE){
                        data.setFlag(fType, false);
                        flag = new RegionFlag(false);
                        regionDataManager.setRegionData(data);
                        itemClicked.setType(Material.RED_CONCRETE);
                        chatFactory.sendPlayerMessage("You have &cdisabled&f the region flag &e" + fType.name() + "&f for region &e" + regionName, true, player, ((LandClaiming)plugin).getBaseConfig().getPluginPrefix());
                    }else{
                        data.setFlag(fType, true);
                        flag = new RegionFlag(true);
                        regionDataManager.setRegionData(data);
                        itemClicked.setType(Material.LIME_CONCRETE);
                        chatFactory.sendPlayerMessage("You have &aenabled&f the region flag &e" + fType.name() + "&f for region &e" + regionName, true, player, ((LandClaiming)plugin).getBaseConfig().getPluginPrefix());
                    }

                    ItemMeta itemClickedMeta = itemClicked.getItemMeta();
                    HashMap<String, Object> newMetaStuff = getChangeSettingData();
                    String displayName = (String) newMetaStuff.get("Display Name");
                    itemClickedMeta.setDisplayName(chatFactory.colorString(displayName));

                    itemClicked.setItemMeta(itemClickedMeta);
                /*
                    Back
                 */
                }else if(e.getSlot() == inv.getSize() - 1){
                    RegionFlagMenu newMenu = new RegionFlagMenu(plugin, regionName, desc, data.getType());
                    player.closeInventory();
                    newMenu.initializeItems(player);
                    newMenu.openInventory(player);
                }
            }
            e.setCancelled(true);
        }

    }

    private HashMap<String, Object> getChangeSettingData(){
        HashMap<String, Object> map = new HashMap<>();
        if(flag.isEnabled()){
            map.put("Display Name", "&cDisable this flag");
            map.put("Material", Material.LIME_CONCRETE);
        }else{
            map.put("Display Name", "&aEnable this flag");
            map.put("Material", Material.RED_CONCRETE);
        }
        return map;
    }
}
