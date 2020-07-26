package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.prompts.ChangeDescriptionPrompt;
import net.dohaw.play.landclaiming.prompts.RenameRegionPrompt;
import net.dohaw.play.landclaiming.region.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RegionFlagMenu extends Menu implements Listener {

    private RegionDataManager regionDataManager;
    private String regionName;
    private RegionData data;
    private RegionDescription desc;
    private RegionType type;

    private final Material CHANGE_DESC_MAT = Material.WHEAT;
    private final Material CHANGE_TYPE_MAT = Material.OBSIDIAN;
    private final Material RENAME_MAT = Material.PAPER;

    private final int RENAME_SLOT;
    private final int CHANGE_DESC_SLOT;
    private final int CHANGE_TYPE_SLOT;
    private final int BACK_SLOT;

    private final List<RegionFlagType> adminFlags = Arrays.asList(RegionFlagType.DAMAGE_PLAYERS, RegionFlagType.DAMAGE_ANIMALS, RegionFlagType.NAME_ANIMALS, RegionFlagType.BLOCK_BREAKING, RegionFlagType.BLOCK_PLACING);

    public RegionFlagMenu(APIHook plugin, String regionName, RegionDescription desc, RegionType type) {

        super(plugin, "Region Flags", 36);
        this.RENAME_SLOT = inv.getSize() - 9;
        this.CHANGE_DESC_SLOT = type == RegionType.ADMIN ? inv.getSize() - 4 : inv.getSize() - 5;
        this.CHANGE_TYPE_SLOT = inv.getSize() - 6;
        this.BACK_SLOT = inv.getSize() - 1;

        this.regionDataManager = ((LandClaiming)plugin).getRegionDataManager();
        this.regionName = regionName;
        this.data = regionDataManager.getRegionDataFromName(regionName);
        this.desc = desc;
        this.type = type;
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

                Map.Entry<RegionFlagType, RegionFlag> entry = itr.next();

                RegionFlagType flagType = entry.getKey();

                Material mat = flagType.getMenuMaterial();
                RegionFlag flag = entry.getValue();
                boolean isEnabled = flag.isEnabled();

                if(isEnabled){
                    lore.add("&a&lEnabled");
                }else{
                    lore.add("&c&lDisabled");
                }
                lore = chatFactory.colorLore(lore);

                if(type == RegionType.NORMAL){
                    if(!adminFlags.contains(flagType)){
                        inv.addItem(createGuiItem(mat, chatFactory.colorString("&e" + flagType.getConfigKey()), lore));
                    }
                }else{
                    inv.addItem(createGuiItem(mat, chatFactory.colorString("&e" + flagType.getConfigKey()), lore));
                }

            }

        }

        if(type == RegionType.ADMIN){
            createChangeTypeButton();
        }

        createChangeDescriptionButton();
        createRenameButton();

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);
    }

    private void createRenameButton(){
        inv.setItem(RENAME_SLOT, createGuiItem(RENAME_MAT, "&eRename Region", new ArrayList<>()));
    }

    private void createChangeTypeButton(){

        List<String> lore = new ArrayList<>();
        String typeStr = data.getType() == RegionType.ADMIN ? "Admin" : "Player";

        lore.add(" ");
        lore.add(chatFactory.colorString("&b&o" + typeStr));
        lore = chatFactory.colorLore(lore);
        inv.setItem(CHANGE_TYPE_SLOT, createGuiItem(CHANGE_TYPE_MAT, "&eChange Type of claim", lore));
    }

    private void createChangeDescriptionButton(){
        List<String> lore = new ArrayList<>();
        String descStrInit = data.getDescription().toString().toLowerCase();
        String descStr = StringUtils.capitalize(descStrInit);

        lore.add(" ");
        lore.add(chatFactory.colorString("&b&o" + descStr));
        lore = chatFactory.colorLore(lore);
        inv.setItem(CHANGE_DESC_SLOT, createGuiItem(CHANGE_DESC_MAT, "&eChange description of claim", lore));
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        if(customInvHelper.isWithinInventory(inv, e)) {
            if (customInvHelper.isValidClickedItem(e, fillerMat)) {

                e.setCancelled(true);
                ItemStack itemClicked = e.getCurrentItem();
                Menu newMenu = null;
                String menuTitle;
                int slot = e.getSlot();

                if(slot == inv.getSize() - 1) {

                    if (desc != null) {
                        String descriptionName = desc.name().toLowerCase().replace("_", " ");
                        menuTitle = chatFactory.firstUpperRestLower(descriptionName) + " Regions";
                    } else {
                        menuTitle = "All Regions";
                    }
                    newMenu = new ClaimDisplayMenu(plugin, menuTitle, desc, 0, type);
                /*
                    If the player wants to rename their region or change the description
                 */
                }else if(slot == RENAME_SLOT || slot == CHANGE_DESC_SLOT) {

                    player.closeInventory();
                    ConversationFactory cf = new ConversationFactory(plugin);
                    Conversation conv;

                    if (slot == RENAME_SLOT) {
                        conv = cf.withFirstPrompt(new RenameRegionPrompt(((LandClaiming) plugin), regionName)).withLocalEcho(false).buildConversation(player);
                    } else {
                        conv = cf.withFirstPrompt(new ChangeDescriptionPrompt(((LandClaiming) plugin), regionName)).withLocalEcho(false).buildConversation(player);
                    }
                    conv.begin();
                /*
                    If the player wants to change their region type
                 */
                }else if(slot == CHANGE_TYPE_SLOT){

                    ItemMeta itemClickedMeta = itemClicked.getItemMeta();

                    if(type == RegionType.ADMIN){
                        data.setType(RegionType.NORMAL);
                    }else{
                        data.setType(RegionType.ADMIN);
                    }

                    if(data instanceof ConnectedRegionData){
                        for(SingleRegionData srd : ((ConnectedRegionData)data).getConnectedData()){
                            srd.setType(type);
                        }
                    }

                    regionDataManager.setRegionData(data);
                    regionDataManager.saveData();
                    regionDataManager.loadData();

                    List<String> lore = new ArrayList<>();
                    String typeStr = chatFactory.firstUpperRestLower(data.getType().name());
                    lore.add(" ");
                    lore.add(chatFactory.firstUpperRestLower("&b&o" + typeStr));
                    lore = chatFactory.colorLore(lore);

                    itemClickedMeta.setLore(lore);
                    itemClicked.setItemMeta(itemClickedMeta);
                    player.closeInventory();
                    chatFactory.sendPlayerMessage("You have changed this region type to " + StringUtils.capitalize(type.toString()) + "!", true, player, ((LandClaiming)plugin).getBaseConfig().getPluginPrefix());
                }else{
                    menuTitle = chatFactory.removeChatColor(itemClicked.getItemMeta().getDisplayName());
                    //Menu title is just the config key;
                    RegionFlagType fType = RegionFlagType.getTypeByConfigKey(menuTitle);
                    newMenu = new AlterFlagPropertiesMenu(plugin, "Flag: " + menuTitle, regionName, fType, desc);
                }

                if(newMenu != null){
                    player.closeInventory();
                    newMenu.initializeItems(player);
                    newMenu.openInventory(player);
                }

            }
        }
    }
}
