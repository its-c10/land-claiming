package net.dohaw.play.landclaiming.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.MathHelper;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.BaseConfig;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.menus.LandClaimMenu;
import net.dohaw.play.landclaiming.menus.RegionFlagMenu;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
    /land
 */
public class LandCommand implements CommandExecutor {

    private LandClaiming plugin;
    private LandClaimMenu landClaimMenu;
    private ChatFactory chatFactory;
    private PlayerDataManager playerDataManager;
    private MathHelper mathHelper;
    private BaseConfig baseConfig;
    private MessagesConfig messagesConfig;
    private final String PREFIX;

    public LandCommand(LandClaiming plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.mathHelper = plugin.getAPI().getMathHelper();
        this.landClaimMenu = new LandClaimMenu(plugin);
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.messagesConfig = plugin.getMessagesConfig();
        this.baseConfig = plugin.getBaseConfig();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                landClaimMenu.initializeItems(player);
                landClaimMenu.openInventory(player);
            }else if(args.length == 1 && !args[0].equalsIgnoreCase("help")){
                RegionDataManager regionDataManager = plugin.getRegionDataManager();
                String potRegionName = args[0];
                if(regionDataManager.getRegionDataFromName(potRegionName) != null) {
                    RegionData rd = regionDataManager.getRegionDataFromName(potRegionName);
                    RegionFlagMenu menu = new RegionFlagMenu(plugin, potRegionName, rd.getDescription(), rd.getType(), player);
                    menu.initializeItems(player);
                    menu.openInventory(player);
                }
            }else if(args[0].equalsIgnoreCase("buy") && args.length == 2){
                if(player.hasPermission("land.purchase")){
                    int pricePerClaim = baseConfig.getPricePerClaim();
                    if(mathHelper.isInt(args[1])){

                        int amount = Integer.parseInt(args[1]);
                        int totalPrice = pricePerClaim * amount;

                        Economy econ = LandClaiming.getEconomy();
                        int playerAmount = (int) econ.getBalance(player);

                        String msg;
                        if(playerAmount >= totalPrice){

                            int playerClaimAmount = playerDataManager.getNumClaimsAvailable(player.getUniqueId());
                            int totalPlayerClaimAmount = playerClaimAmount + amount;
                            PlayerData data = playerDataManager.getData(player.getUniqueId());

                            data.setClaimAmount(totalPlayerClaimAmount);
                            playerDataManager.setData(player.getUniqueId(), data);

                            msg = messagesConfig.getMessage(Message.LAND_PURCHASE_SUCCESS);
                            msg = Utils.replacePlaceholders("%amountBought%", msg, String.valueOf(amount));
                            msg = Utils.replacePlaceholders("%price%", msg, String.valueOf(totalPrice));
                            msg = Utils.replacePlaceholders("%chunkAmount%", msg, String.valueOf(totalPlayerClaimAmount));
                        }else{
                            msg = messagesConfig.getMessage(Message.LAND_PURCHASE_FAIL);
                            msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(amount));
                        }
                        chatFactory.sendPlayerMessage(msg, true, player, PREFIX);
                    }else{
                        chatFactory.sendPlayerMessage("The amount given is not a number!", true, player, PREFIX);
                    }
                }else{
                    chatFactory.sendPlayerMessage("You do not have permission to buy land!", true, player, PREFIX);
                }
            }else if(args[0].equalsIgnoreCase("help") && args.length == 1){
                chatFactory.sendPlayerMessage("LandClaiming Commands: ", true, player, PREFIX);
                chatFactory.sendPlayerMessage("&6&l/land &f- Basic command to bring up the LandClaiming GUI", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/land buy <amount> &f- Allows you to purchase land claims", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/trust <add | remove> <player name> &f- Allows you to give access/trust a player to your region.", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/claim <type> &f- Allows you to claim land with the given type (Farm, Storage, etc).", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/unclaim &f- Allows you to unclaim your land (Either the whole region or 1 chunk)", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/autoclaim <type> &f- Allows you to auto claim land via a specific chunk type. Auto claiming is triggered when you place a certain amount of blocks within a chunk", false, player, null);
                chatFactory.sendPlayerMessage("&6&l/autoclaim &f- Disables auto claiming", false, player, null);
            }
        }else{
            chatFactory.sendPlayerMessage("This command can only be used by players!", true, sender, PREFIX);
        }
        return false;
    }
}
