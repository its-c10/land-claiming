package net.dohaw.play.landclaiming.events;

import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.PlayerData;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionFlag;
import net.dohaw.play.landclaiming.region.RegionFlagType;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FlagWatcher implements Listener {

    private RegionDataManager regionDataManager;
    private PlayerDataManager playerDataManager;

    public FlagWatcher(LandClaiming plugin){
        this.regionDataManager = plugin.getRegionDataManager();
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent e){
        Entity eDamager = e.getDamager();
        Entity eDamaged = e.getEntity();
        if(eDamager instanceof Player && eDamaged instanceof Player){

            Player damager = (Player) eDamager;
            Player damaged = (Player) eDamaged;

            Chunk chunk = damaged.getLocation().getChunk();
            if(regionDataManager.hasData(chunk)){
                RegionData data = regionDataManager.getDataFromChunk(chunk);
                RegionFlag pvpFlag = data.getFlags().get(RegionFlagType.PVP);
                if(!pvpFlag.isEnabled()){
                    e.setCancelled(true);
                }
            }

        }
    }

}
