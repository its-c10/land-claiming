package net.dohaw.play.landclaiming.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.landclaiming.LandClaiming;
import net.dohaw.play.landclaiming.Message;
import net.dohaw.play.landclaiming.Utils;
import net.dohaw.play.landclaiming.files.MessagesConfig;
import net.dohaw.play.landclaiming.managers.PlayerDataManager;
import net.dohaw.play.landclaiming.managers.RegionDataManager;
import net.dohaw.play.landclaiming.region.RegionData;
import net.dohaw.play.landclaiming.region.RegionFlag;
import net.dohaw.play.landclaiming.region.RegionFlagType;
import net.dohaw.play.landclaiming.region.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class FlagWatcher implements Listener {

    private RegionDataManager regionDataManager;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private MessagesConfig messagesConfig;
    private final String PREFIX;

    public FlagWatcher(LandClaiming plugin){
        this.regionDataManager = plugin.getRegionDataManager();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.messagesConfig = plugin.getMessagesConfig();
        this.PREFIX = plugin.getBaseConfig().getPluginPrefix();
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        Entity eDamaged = e.getEntity();

        if(!(eDamaged instanceof Player)) return;

        if(!(eDamager instanceof Player)){
            if(eDamager instanceof Projectile){
                Projectile proj = (Projectile) eDamager;
                if(!(proj.getShooter() instanceof Player)) {
                    return;
                }else{
                    eDamager = (Entity) proj.getShooter();
                }
            }
        }

        if(eDamager instanceof Player && eDamaged instanceof Player){
            Chunk chunk = eDamaged.getLocation().getChunk();
            if(regionDataManager.hasData(chunk)){
                if(!isFlagEnabled(chunk, RegionFlagType.PVP)){
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e){

        Entity entity = e.getEntity();
        Chunk chunkSpawned = e.getLocation().getChunk();

        if(regionDataManager.hasData(chunkSpawned)){
            /*
                Hostile
            */
            if(entity instanceof Monster){
                if(!isFlagEnabled(chunkSpawned, RegionFlagType.HOSTILE_MOB_SPAWNING)){
                    e.setCancelled(true);
                }
            /*
                Friendly
             */
            }else{
                if(!isFlagEnabled(chunkSpawned, RegionFlagType.FRIENDLY_MOB_SPAWNING)){
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onDoorAndTrapdoorUse(PlayerInteractEvent e){
        Chunk chunkOccurred = e.getPlayer().getLocation().getChunk();
        Action action = e.getAction();
        if(regionDataManager.hasData(chunkOccurred)){
            if(action == Action.RIGHT_CLICK_BLOCK){
                Block blockClicked = e.getClickedBlock();
                Material matBlockClicked = blockClicked.getType();
                if(matBlockClicked.name().toLowerCase().contains("door")){
                    if(!isFlagEnabled(chunkOccurred, RegionFlagType.DOOR_TRAPDOOR_ACCESS) && !isTrusted(chunkOccurred, e.getPlayer()) && !e.getPlayer().hasPermission("land.bypassaccess")){
                        e.setCancelled(true);
                        String msg = messagesConfig.getMessage(Message.ITEM_USE_DENY);
                        chatFactory.sendPlayerMessage(msg, true, e.getPlayer(), PREFIX);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLiquidPlacement(PlayerBucketEmptyEvent e){
        Chunk chunk = e.getBlockClicked().getChunk();
        if(regionDataManager.hasData(chunk)){
            if(!isFlagEnabled(chunk, RegionFlagType.WATER_LAVA_PLACE) && !e.getPlayer().hasPermission("land.bypassaccess")){
                e.setCancelled(true);
                String msg = messagesConfig.getMessage(Message.ITEM_USE_DENY);
                chatFactory.sendPlayerMessage(msg, true, e.getPlayer(), PREFIX);
            }
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent e){
        Chunk chunk = e.getSource().getChunk();
        if(regionDataManager.hasData(chunk)){
            if(!isFlagEnabled(chunk, RegionFlagType.FIRE_SPREAD)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerEnterRegion(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Chunk chunk = e.getTo().getChunk();
        if(regionDataManager.hasData(chunk)){
            if(!isFlagEnabled(chunk, RegionFlagType.UNTRUSTED_PLAYER_ACCESS) && !isTrusted(chunk, player) && !player.hasPermission("land.bypassaccess")){

                e.setCancelled(true);
                String msg = messagesConfig.getMessage(Message.LAND_ENTRY_DENY);
                RegionData data = regionDataManager.getDataFromChunk(chunk);

                String ownerName = Bukkit.getOfflinePlayer(data.getOwnerUUID()).getName();
                msg = Utils.replacePlaceholders("%player%", msg, ownerName);
                chatFactory.sendPlayerMessage(msg, true, player, PREFIX);

                player.setVelocity(player.getLocation().getDirection().multiply(-1.25));
            }
        }
    }

    /*
        ==================================================
        ==================================================
                         ADMIN FLAGS
        ==================================================
        ==================================================
     */
    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            Chunk chunk = player.getLocation().getChunk();
            if(regionDataManager.hasData(chunk)){
                RegionData rd = regionDataManager.getDataFromChunk(chunk);
                if(rd.getType() == RegionType.ADMIN){
                    if(!isFlagEnabled(chunk, RegionFlagType.DAMAGE_PLAYERS) && !player.hasPermission("land.bypassaccess")){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageAnimals(EntityDamageByEntityEvent e){

        Entity entity = e.getEntity();
        Entity eDamager = e.getDamager();

        if(!(eDamager instanceof Player)){
            if(eDamager instanceof Projectile){
                Projectile proj = (Projectile) eDamager;
                if(!(proj.getShooter() instanceof Player)) {
                    return;
                }else{
                    eDamager = (Entity) proj.getShooter();
                }
            }
        }

        /*
            If the player is doing damage to a mob
         */
        if(!(entity instanceof Player) && eDamager instanceof Player){
            Chunk chunk = e.getEntity().getLocation().getChunk();
            if(regionDataManager.hasData(chunk)){
                RegionData rd = regionDataManager.getDataFromChunk(chunk);
                if(rd.getType() == RegionType.ADMIN){
                    if(!isFlagEnabled(chunk, RegionFlagType.DAMAGE_ANIMALS) && !isTrusted(chunk, (Player)eDamager) && !eDamager.hasPermission("land.bypassaccess")){
                        e.setCancelled(true);
                        String msg = messagesConfig.getMessage(Message.MOB_DAMAGE_DENY);
                        chatFactory.sendPlayerMessage(msg, true, (Player)eDamager, PREFIX);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerNameAnimal(PlayerInteractAtEntityEvent e){

        Player player = e.getPlayer();
        Entity rightClickedMob = e.getRightClicked();
        Chunk chunk = rightClickedMob.getLocation().getChunk();

        if(regionDataManager.hasData(chunk)){
            RegionData rd = regionDataManager.getDataFromChunk(chunk);
           /*
            Is a mob
            */
            if(!(rightClickedMob instanceof Player)){
                if(rd.getType() == RegionType.ADMIN){
                    if(!isFlagEnabled(chunk, RegionFlagType.NAME_ANIMALS) && !isTrusted(chunk, player) && !e.getPlayer().hasPermission("land.bypassaccess")){
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Chunk chunk = e.getBlock().getChunk();
        if(regionDataManager.hasData(chunk)){
            RegionData rd = regionDataManager.getDataFromChunk(chunk);
            if(rd.getType() == RegionType.ADMIN){
                if(!isFlagEnabled(chunk, RegionFlagType.BLOCK_BREAKING) && !isTrusted(chunk, e.getPlayer()) && !e.getPlayer().hasPermission("land.bypassaccess")){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Chunk chunk = e.getBlock().getChunk();
        if(regionDataManager.hasData(chunk)){
            RegionData rd = regionDataManager.getDataFromChunk(chunk);
            if(rd.getType() == RegionType.ADMIN){
                if(!isFlagEnabled(chunk, RegionFlagType.BLOCK_PLACING) && !isTrusted(chunk, e.getPlayer()) && !e.getPlayer().hasPermission("land.bypassaccess")){
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean isFlagEnabled(Chunk chunk, RegionFlagType rfType){
        RegionData data = regionDataManager.getDataFromChunk(chunk);
        RegionFlag flag = data.getFlags().get(rfType);
        return flag.isEnabled();
    }

    private boolean isTrusted(Chunk chunk, Player player){
        RegionData data = regionDataManager.getDataFromChunk(chunk);
        return data.isOwner(player.getUniqueId()) || data.isTrusted(player.getUniqueId());
    }

}
