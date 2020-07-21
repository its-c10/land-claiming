package net.dohaw.play.landclaiming.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.landclaiming.DataManager;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class LandClaimMenu extends Menu implements Listener {

    private DataManager dataManager;
    private ChatFactory chatFactory;
    private final String PREFIX;

    public LandClaimMenu(APIHook plugin) {
        super(plugin, "Land Claim", 27);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.dataManager = ((LandClaiming)plugin).getDataManager();
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
            if(customInvHelper.isValidClickedItem(e)){
                if(e.getCurrentItem().getType() != fillerMat){

                    if(dataManager.getPlayerData(player.getUniqueId()) == null){
                        player.closeInventory();
                        chatFactory.sendPlayerMessage("You don't have any claims!", true, player, PREFIX);
                    }else{
                        PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
                        if(playerData.getRegions().size() == 0){
                            player.closeInventory();
                            chatFactory.sendPlayerMessage("You don't have any claims!", true, player, PREFIX);
                        }else{
                            Menu newMenu;
                            if(e.getSlot() == 12){
                                newMenu = new PlayerClaimMenu(plugin);
                            }else if(e.getSlot() == 14){
                                return;
                            }else{
                                return;
                            }
                            newMenu.initializeItems(player);
                            newMenu.openInventory(player);
                        }
                    }

                }
            }
            e.setCancelled(true);
        }

    }
}
