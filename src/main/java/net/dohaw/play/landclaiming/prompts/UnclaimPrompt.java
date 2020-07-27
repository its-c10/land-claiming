package net.dohaw.play.landclaiming.prompts;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.ConnectedRegionData;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import org.bukkit.Chunk;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimPrompt extends StringPrompt {

    private PlayerDataManager playerDataManager;
    private RegionDataManager regionDataManager;
    private MessagesConfig messagesConfig;
    private ChatFactory chatFactory;
    private String regionName;
    private final String PREFIX;
    private final Player player;

    public UnclaimPrompt(final Player player, String regionName, LandClaiming plugin){
        this.regionDataManager = plugin.getRegionDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.regionName = regionName;
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
        this.messagesConfig = plugin.getMessagesConfig();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Type \"W\" to unclaim the whole region. Type \"C\" to unclaim the whole chunk. Type \"cancel\" to abort this action";
    }

    /**
     * Accepts and processes input from the user. Using the input, the next
     * Prompt in the prompt graph is returned.
     *
     * @param context Context information about the conversation.
     * @param input   The input text from the user.
     * @return The next Prompt in the prompt graph.
     */
    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Conversable conversable = context.getForWhom();

        if(input.equalsIgnoreCase("cancel")){
            conversable.sendRawMessage("Aborting...");
            return END_OF_CONVERSATION;
        }

        if(input.equalsIgnoreCase("W") || input.equalsIgnoreCase("C")){

            ConnectedRegionData data;
            int claimsGained = 0;

            /*
                Deletes the whole region
             */
            if(input.equalsIgnoreCase("W")){
                data = (ConnectedRegionData) regionDataManager.getRegionDataFromName(regionName);
                regionDataManager.delete(data);
                claimsGained = data.getConnectedData().size();
            }else{
                claimsGained = 1;
                data = (ConnectedRegionData) regionDataManager.getRegionDataFromName(regionName);
                Chunk playerChunk = player.getLocation().getChunk();
                SingleRegionData singleRegionData = getRegionStandingIn(playerChunk, data.getConnectedData());
                regionDataManager.deleteWithinConnected(singleRegionData);
                /*
                    Reloads all data to adjust affected ConnectedRegionData objects
                 */
                regionDataManager.loadData();
            }

            String msg = messagesConfig.getMessage(Message.LAND_UNCLAIM_SUCCESS);
            int playerClaims = playerDataManager.getNumClaimsAvailable(player.getUniqueId()) + claimsGained;
            msg = Utils.replacePlaceholders("%amount%", msg, String.valueOf(playerClaims));
            msg = Utils.replacePlaceholders("%claimsGained%", msg, String.valueOf(claimsGained));

            conversable.sendRawMessage(msg);
        }

        return END_OF_CONVERSATION;
    }

    private SingleRegionData getRegionStandingIn(Chunk chunk, List<SingleRegionData> srdList){
        for(SingleRegionData srd : srdList){
            if(srd.getChunk().equals(chunk)){
                return srd;
            }
        }
        return null;
    }

}
