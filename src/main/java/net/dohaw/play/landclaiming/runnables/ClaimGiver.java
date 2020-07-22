package net.dohaw.play.landclaiming.runnables;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.MathHelper;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimGiver extends BukkitRunnable {

    private LandClaiming plugin;
    private PlayerDataManager playerDataManager;
    private int defaultGiveAmount;
    private ChatFactory chatFactory;
    private MathHelper mathHelper;

    private final String PERMISSION_PREFIX = "land.earntime.";
    private final String PREFIX;

    public ClaimGiver(LandClaiming plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.defaultGiveAmount = plugin.getBaseConfig().getDefaultGiveAmount();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
        this.mathHelper = plugin.getAPI().getMathHelper();
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            int giveAmount = getGiveAmount(player);
            PlayerData data = playerDataManager.getData(player.getUniqueId());
            data.setClaimAmount(data.getClaimAmount() + giveAmount);
            chatFactory.sendPlayerMessage("You have been given &e" + giveAmount + "&f claims!", true, player, PREFIX);
        }
    }

    public int getGiveAmount(Player player){
        for(PermissionAttachmentInfo info : player.getEffectivePermissions()){
            if(info.getPermission().startsWith(PERMISSION_PREFIX)){
                String permission = info.getPermission();
                if(mathHelper.isInt(permission.substring(permission.lastIndexOf(".") + 1))){
                    return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
                }
            }
        }
        return defaultGiveAmount;
    }

}
