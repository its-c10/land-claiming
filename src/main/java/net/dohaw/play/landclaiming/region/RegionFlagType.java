package net.dohaw.play.landclaiming.region;

public enum RegionFlagType {

    PVP("PVP"),
    FRIENDLY_MOB_SPAWNING("Friendly Mob Spawning"),
    HOSTILE_MOB_SPAWNING("Hostile Mob Spawning"),
    DOOR_TRAPDOOR_ACCESS("Door and Trapdoor Access"),
    WATER_LAVA_PLACE("Water and Lava Placement"),
    FIRE_SPREAD("Fire Spread"),
    UNTRUSTED_PLAYER_ACCESS("Untrusted Player Access"),

    //Admin stuff
    DAMAGE_PLAYERS("Damage Players"),
    DAMAGE_ANIMALS("Damage Animals"),
    NAME_ANIMALS("Name Animals"),
    BLOCK_PLACING("Block Placing"),
    BLOCK_BREAKING("Block Breaking");

    private String configKey;
    RegionFlagType(String configKey){
        this.configKey = configKey;
    }

    public String getConfigKey(){
        return configKey;
    }

}
