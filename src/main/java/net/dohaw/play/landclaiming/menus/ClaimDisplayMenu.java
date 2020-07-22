package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.helpers.ItemStackHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClaimDisplayMenu extends Menu implements Listener {

    private RegionDataManager regionDataManager;
    private ItemStackHelper itemStackHelper;
    private RegionDescription desc;
    private List<RegionData> data;
    private List<RegionData> thisPageData = new ArrayList<>();
    private String menuTitle;
    private RegionType typeOfMenu;

    private final Material NEXT_PAGE_MAT = Material.STRING;
    private final Material BACK_MAT = Material.ARROW;
    private final Material ITEM_MAT = Material.SPONGE;

    //0 indexed
    private int page;

    public ClaimDisplayMenu(APIHook plugin, String menuTitle, RegionDescription desc, int page, RegionType typeOfMenu) {
        super(plugin, menuTitle + " Page " + (page + 1), 54);
        this.regionDataManager = ((LandClaiming) plugin).getRegionDataManager();
        this.desc = desc;
        this.page = page;
        this.menuTitle = menuTitle;
        this.typeOfMenu = typeOfMenu;
        this.itemStackHelper = plugin.getAPI().getItemStackHelper();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {

        if(desc == null){
            this.data = regionDataManager.getPlayerRegionData(player.getUniqueId());
        }else{
            this.data = regionDataManager.getPlayerRegionDataByDescription(player.getUniqueId(), desc);
        }

        if(!data.isEmpty()){

            int startingIndex = page * inv.getSize();
            for(int x = startingIndex; x < data.size(); x++){
                if(data.get(x).getType() == typeOfMenu){
                    thisPageData.add(data.get(x));
                }
            }

            for(int x = 0; x < thisPageData.size(); x++){

                RegionData data = thisPageData.get(x);
                List<String> lore = new ArrayList<>();
                String typeStr = chatFactory.firstUpperRestLower(data.getType().name());
                String displayName = chatFactory.colorString("&f&l" + data.getName());

                if(data.getType() == RegionType.NORMAL){
                    typeStr = "Player";
                }

                lore.add("&b&o" + typeStr + " Claim");
                lore.add("&eLocation: &cX: " + data.getChunk().getX() + " | Z: " + data.getChunk().getZ());
                if(desc == null){
                    String desc = chatFactory.firstUpperRestLower(data.getDescription().name());
                    desc = desc.replace("_", " ");
                    lore.add("&eType: &c" + desc);
                }

                lore = chatFactory.colorLore(lore);

                inv.addItem(createGuiItem(ITEM_MAT, displayName, lore));
                /*
                    If the player is sitting in their own claim, then set the item to their player head and make it glow.
                 */
                Chunk playerChunk = player.getLocation().getChunk();
                Chunk dataChunk = data.getChunk();

                if(dataChunk.equals(playerChunk)){

                    ItemStack item = inv.getItem(x);
                    ItemMeta itemMeta = item.getItemMeta();
                    ItemStack playerHead = itemStackHelper.getHead(player, Material.LEGACY_SKULL_ITEM);
                    ItemMeta playerHeadMeta = playerHead.getItemMeta();

                    playerHeadMeta.setDisplayName(itemMeta.getDisplayName());
                    playerHeadMeta.setLore(itemMeta.getLore());
                    playerHead.setItemMeta(playerHeadMeta);

                    ItemStack itemGlowed = itemStackHelper.addGlowToItem(playerHead);

                    ItemMeta glowingItemMeta = itemGlowed.getItemMeta();
                    List<String> glowingItemLore = glowingItemMeta.getLore();

                    glowingItemLore.add("  ");
                    glowingItemLore.add(chatFactory.colorString("&b&lCurrent Claim"));

                    glowingItemMeta.setLore(glowingItemLore);
                    itemGlowed.setItemMeta(glowingItemMeta);

                    inv.setItem(x, itemGlowed);
                }


            }
        }

        createBackPageButton();
        createNextPageButton();

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(false);
    }

    private void createNextPageButton(){
        inv.setItem(inv.getSize() - 1, createGuiItem(NEXT_PAGE_MAT, "&eNext page", new ArrayList<>()));
    }

    private void createBackPageButton(){
        inv.setItem(inv.getSize() - 9, createGuiItem(BACK_MAT, "&eGo back", new ArrayList<>()));
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        if(customInvHelper.isWithinInventory(inv, e)) {
            if (customInvHelper.isValidClickedItem(e, fillerMat)) {

                e.setCancelled(true);
                ItemStack itemClicked = e.getCurrentItem();
                int slot = e.getSlot();
                int backSlot = inv.getSize() - 9;
                int nextPageSlot = inv.getSize() - 1;
                Menu newMenu = null;

                if(slot == backSlot){
                    if(page == 0){
                        if(typeOfMenu == RegionType.ADMIN){
                            newMenu = new DescriptionMenu(plugin, "Admin Claim Types", typeOfMenu);
                        }else{
                            newMenu = new DescriptionMenu(plugin, "Player Claim Types", typeOfMenu);
                        }
                    }else{
                        newMenu = new ClaimDisplayMenu(plugin, menuTitle, desc, (page - 1), typeOfMenu);
                    }
                }else if(slot == nextPageSlot){
                    newMenu = new ClaimDisplayMenu(plugin, menuTitle, desc, (page + 1), typeOfMenu);
                }else if(itemClicked.getType() == ITEM_MAT){
                    ItemMeta itemClickedMeta = itemClicked.getItemMeta();
                    String regionName = chatFactory.removeChatColor(itemClickedMeta.getDisplayName());
                    newMenu = new RegionFlagMenu(plugin, regionName, desc, typeOfMenu);
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

}
