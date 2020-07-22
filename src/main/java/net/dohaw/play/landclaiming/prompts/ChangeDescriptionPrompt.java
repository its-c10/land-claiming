package net.dohaw.play.landclaiming.prompts;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionDescription;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ChangeDescriptionPrompt extends StringPrompt {

    private LandClaiming plugin;
    private RegionDataManager regionDataManager;
    private RegionData regionData;
    private ChatFactory chatFactory;

    public ChangeDescriptionPrompt(LandClaiming plugin, String regionName){
        this.plugin = plugin;
        this.regionDataManager = plugin.getRegionDataManager();
        this.regionData = regionDataManager.getDataFromName(regionName);
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Please type what you would like your region description to be (Examples: farm, home, storage, etc)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        if(input.equalsIgnoreCase("cancel")){
            context.getForWhom().sendRawMessage("Aborting...");
            return END_OF_CONVERSATION;
        }

        Player player = (Player) context.getForWhom();
        if(RegionDescription.getByAlias(input) != null){
            RegionDescription newDesc = RegionDescription.getByAlias(input);
            RegionType descType = newDesc.getType();
            /*
                If they are trying to give a player claim a description that can only be given to admin claims.
             */
            if(regionData.getType() == RegionType.NORMAL && descType == RegionType.ADMIN){
                player.sendRawMessage("You can't give a player claim a description that is only valid for admin claims!");
            }else{
                regionData.setDescription(newDesc);
                regionDataManager.setRegionData(regionData);
                player.sendRawMessage("You have changed the region description of region " + regionData.getName() + " to " + chatFactory.firstUpperRestLower(newDesc.name()));
            }
        }else{
            player.sendRawMessage("This is not a valid region description!");
        }
        return END_OF_CONVERSATION;

    }
}
