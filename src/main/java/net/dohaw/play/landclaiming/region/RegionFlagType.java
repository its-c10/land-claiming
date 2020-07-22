package net.dohaw.play.landclaiming.region;

import org.bukkit.Material;

public enum RegionFlagType {

    PVP("PVP", Material.GOLDEN_SWORD),
    FRIENDLY_MOB_SPAWNING("Friendly Mob Spawning", Material.SHEEP_SPAWN_EGG),
    HOSTILE_MOB_SPAWNING("Hostile Mob Spawning", Material.ZOMBIE_HEAD),
    DOOR_TRAPDOOR_ACCESS("Door and Trapdoor Access", Material.DARK_OAK_DOOR),
    WATER_LAVA_PLACE("Water and Lava Placement", Material.BUCKET),
    FIRE_SPREAD("Fire Spread", Material.FLINT_AND_STEEL),
    UNTRUSTED_PLAYER_ACCESS("Untrusted Player Access", Material.PLAYER_HEAD),

    //Admin stuff
    DAMAGE_PLAYERS("Damage Players", Material.IRON_SWORD),
    DAMAGE_ANIMALS("Damage Animals", Material.WOODEN_SWORD),
    NAME_ANIMALS("Name Animals", Material.NAME_TAG),
    BLOCK_PLACING("Block Placing", Material.GRASS_BLOCK),
    BLOCK_BREAKING("Block Breaking", Material.GOLDEN_PICKAXE);

    private String configKey;
    private Material menuMaterial;
    RegionFlagType(String configKey, Material menuMaterial){
        this.configKey = configKey;
        this.menuMaterial = menuMaterial;
    }

    public String getConfigKey(){
        return configKey;
    }

    public Material getMenuMaterial(){
        return menuMaterial;
    }

    public static RegionFlagType getTypeByConfigKey(String configKey){
        for(RegionFlagType fType : RegionFlagType.values()){
            if(fType.configKey.equalsIgnoreCase(configKey)){
                return fType;
            }
        }
        return null;
    }


}
