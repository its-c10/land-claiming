package net.dohaw.play.landclaiming.region;

import net.dohaw.play.landclaiming.PlayerData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class RegionData {

    private EnumMap<RegionFlagType, RegionFlag> flags = new EnumMap<>(RegionFlagType.class);
    protected RegionDescription desc;
    protected RegionType type;
    protected UUID ownerUUID;
    protected List<UUID> trustedPlayers =  new ArrayList<>();
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner(UUID uuid){
        return uuid.equals(ownerUUID);
    }

    public boolean isTrusted(UUID uuid){
        return trustedPlayers.contains(uuid);
    }

    public void addTrustedPlayer(PlayerData data){
        trustedPlayers.add(data.getUUID());
    }

    public void addTrustedPlayer(UUID uuid){
        trustedPlayers.add(uuid);
    }

    public void addTrustedPlayer(Player player){
        trustedPlayers.add(player.getUniqueId());
    }

    public void removeTrustedPlayer(PlayerData data){
        trustedPlayers.remove(data.getUUID());
    }

    public void removeTrustedPlayer(UUID uuid){
        trustedPlayers.remove(uuid);
    }

    public void removeTrustedPlayer(Player player){
        trustedPlayers.remove(player.getUniqueId());
    }

    public List<UUID> getTrustedPlayers(){
        return trustedPlayers;
    }

    public RegionType getType() {
        return type;
    }

    public void setType(RegionType type){
        this.type = type;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void setDescription(RegionDescription description) {
        this.desc = description;
    }

    public EnumMap<RegionFlagType, RegionFlag> getFlags() {
        return flags;
    }

    public void setFlags(EnumMap<RegionFlagType, RegionFlag> flags) {
        this.flags = flags;
    }

    public void setFlag(RegionFlagType type, boolean b){
        flags.replace(type, new RegionFlag(b));
    }

    public RegionDescription getDescription() {
        return desc;
    }



}
