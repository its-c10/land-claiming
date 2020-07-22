package net.dohaw.play.landclaiming.region;

import java.util.Arrays;

public enum RegionDescription {

    HOME(new String[]{"home"}, RegionType.NORMAL),
    FARM(new String[]{"farm"}, RegionType.NORMAL),
    STORAGE(new String[]{"storage"}, RegionType.NORMAL),
    MARKET(new String[]{"store", "market"}, RegionType.NORMAL),
    MOB_GRINDER(new String[]{"mob grinder", "mobg", "mg", "mob", "grinder", "mobgrinder", "mgrinder"}, RegionType.NORMAL),
    GENERAL(new String[]{"gen", "general"}, RegionType.NORMAL),

    //Admins
    SPAWN(new String[]{"spawn"}, RegionType.ADMIN),
    PVP_ARENA(new String[]{"pvp", "arena", "pvparena", "pvp arena"}, RegionType.ADMIN),
    JAIL(new String[]{"jail"}, RegionType.ADMIN),
    TUTORIAL(new String[]{"tut", "tutorial", "how-to", "howto"}, RegionType.ADMIN);

    private String[] aliases;
    private RegionType type;
    RegionDescription(String[] aliases, RegionType type){
        this.aliases = aliases;
        this.type = type;
    }

    public String[] getAliases(){
        return aliases;
    }

    public RegionType getType(){
        return type;
    }

    public static RegionDescription getByAlias(String str){
        for(RegionDescription type : RegionDescription.values()){
            if(Arrays.asList(type.aliases).contains(str)){
                return type;
            }
        }
        return null;
    }

}
