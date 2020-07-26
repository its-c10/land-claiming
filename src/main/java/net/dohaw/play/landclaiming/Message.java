package net.dohaw.play.landclaiming;

public enum Message {

    LAND_CLAIM("Land Claim"),
    LAND_UNCLAIM("Land Unclaim"),
    LAND_UNCLAIM_SUCCESS("Land Unclaim Success"),
    LAND_CLAIM_SUCCESS("Land Claim Success"),
    LAND_CLAIM_FAIL("Land Claim Fail"),
    LAND_CLAIM_NO("Land Claim No"),
    LAND_PURCHASE_SUCCESS("Land Purchase Success"),
    LAND_PURCHASE_FAIL("Land Purchase Fail"),
    LAND_ENTRY_DENY("Land Entry Deny"),
    PVP_DENY("PVP Deny"),
    INACTIVE_PLAYER_RETURNS("Inactive Player Returns"),
    BLOCK_BREAK_DENY("Block Break Deny"),
    ITEM_USE_DENY("Item Use Deny"),
    MOB_DAMAGE_DENY("Mob Damage Deny"),
    PLAYER_TRUSTED("Player Trusted"),
    PLAYER_UNTRUSTED("Player Untrusted");

    private String configKey;
    Message(String configKey){
        this.configKey = configKey;
    }

    public String getConfigKey(){
        return configKey;
    }

}
