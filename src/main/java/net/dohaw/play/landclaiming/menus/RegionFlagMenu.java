package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RegionFlagMenu extends Menu implements Listener {

    private RegionDataManager regionDataManager;
    private String regionName;
    private RegionData data;
    private RegionDescription desc;
    private final Material RENAME_MAT = Material.PAPER;

    public RegionFlagMenu(APIHook plugin, String regionName, RegionDescription desc) {
        super(plugin, "Region Flags", 27);
        this.regionName = regionName;
        this.regionDataManager = ((LandClaiming)plugin).getRegionDataManager();
        this.data = regionDataManager.getDataFromName(regionName);
        this.desc = desc;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {

        if(data != null){

            EnumMap<RegionFlagType, RegionFlag> flags = data.getFlags();
            Iterator<Map.Entry<RegionFlagType, RegionFlag>> itr = flags.entrySet().iterator();

            while(itr.hasNext()){

                List<String> lore = new ArrayList<>();
                lore.add("&eClick me &cto edit this flag!");
                lore.add(" ");

                RegionFlagType flagType = itr.next().getKey();
                Material mat = flagType.getMenuMaterial();
                RegionFlag flag = itr.next().getValue();
                boolean isEnabled = flag.isEnabled();

                if(isEnabled){
                    lore.add("&a&lEnabled");
                }else{
                    lore.add("&c&lDisabled");
                }

                lore = chatFactory.colorLore(lore);
                inv.addItem(createGuiItem(mat, chatFactory.colorString("&e" + flagType.getConfigKey()), lore));
            }

        }

        createRenameButton();

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);
    }

    private void createRenameButton(){
        inv.setItem(inv.getSize() - 9, createGuiItem(RENAME_MAT, "&eRename Region", new ArrayList<>()));
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        if(customInvHelper.isWithinInventory(inv, e)) {
            if (customInvHelper.isValidClickedItem(e, fillerMat)) {

                e.setCancelled(true);
                ItemStack itemClicked = e.getCurrentItem();
                Menu newMenu;
                String menuTitle;

                if(itemClicked.getType() == backMat){

                    if(desc != null){
                        String descriptionName = desc.name().toLowerCase().replace("_", " ");
                        menuTitle = chatFactory.firstUpperRestLower(descriptionName) + " Regions";
                    }else{
                        menuTitle = "All Regions";
                    }
                    newMenu = new DescriptionMenu(plugin, menuTitle, desc, 0);

                }else{
                    menuTitle = chatFactory.removeChatColor(itemClicked.getItemMeta().getDisplayName());
                    //Menu title is just the config key;
                    RegionFlagType fType = RegionFlagType.getTypeByConfigKey(menuTitle);
                    newMenu = new AlterFlagPropertiesMenu(plugin, "Flag: " + menuTitle, regionName, fType, desc);
                }

                player.closeInventory();
                newMenu.initializeItems(player);
                newMenu.openInventory(player);

            }
        }
    }
}
