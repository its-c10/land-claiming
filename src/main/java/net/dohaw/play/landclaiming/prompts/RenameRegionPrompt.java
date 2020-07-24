package net.dohaw.play.landclaiming.prompts;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.SingleRegionData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.regex.Pattern;

public class RenameRegionPrompt extends StringPrompt {

    private LandClaiming plugin;
    private SingleRegionData singleRegionData;
    private String regionName;
    private RegionDataManager regionDataManager;

    public RenameRegionPrompt(LandClaiming plugin, String regionName){
        this.plugin = plugin;
        this.regionDataManager = plugin.getRegionDataManager();
        this.regionName = regionName;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Please type what you would like your new region to be. Side note: It can only have Underscores, Letters, and Numbers. Type \"cancel\" if you wish to abort this action";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        if(input.equalsIgnoreCase("cancel")){
            context.getForWhom().sendRawMessage("Aborting...");
            return END_OF_CONVERSATION;
        }

        if(Pattern.matches("([A-Za-z0-9\\_\\s]+)", input)){
            SingleRegionData data = regionDataManager.getDataFromName(regionName);
            data.setName(input);
            regionDataManager.setRegionData(data);
            context.getForWhom().sendRawMessage("You have renamed your region to " + input + "!");
            return END_OF_CONVERSATION;
        }

        return new RenameRegionPrompt(plugin, regionName);
    }
}
