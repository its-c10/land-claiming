package net.dohaw.play.landclaiming.region;

import java.util.Arrays;

public enum RegionDescription {

    HOME(new String[]{"home"}),
    FARM(new String[]{"farm"}),
    STORAGE(new String[]{"storage"}),
    MARKET(new String[]{"store", "market"}),
    MOB_GRINDER(new String[]{"mob grinder", "mobg", "mg", "mob", "grinder", "mobgrinder", "mgrinder"}),
    GENERAL(new String[]{"gen", "general"}),

    //Admins
    SPAWN(new String[]{"spawn"}),
    PVP_ARENA(new String[]{"pvp", "arena", "pvparena", "pvp arena"}),
    JAIL(new String[]{"jail"}),
    TUTORIAL(new String[]{"tut", "tutorial", "how-to", "howto"});

    private String[] aliases;
    RegionDescription(String[] aliases){
        this.aliases = aliases;
    }

    public String[] getAliases(){
        return aliases;
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
