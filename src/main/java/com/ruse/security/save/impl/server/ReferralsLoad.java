package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.World;
import com.ruse.world.packages.referral.ReferralClaim;
import com.ruse.world.packages.referral.Referrals;

import java.util.List;
import java.util.Map;

public class ReferralsLoad extends SecureLoad {

    public ReferralsLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public ReferralsLoad run() {
        Map<String, Integer> banned = builder.fromJson(object.get("referrals"),
                new com.google.gson.reflect.TypeToken<Map<String,Integer>>() {
                }.getType());
        List<ReferralClaim> amounts = builder.fromJson(object.get("claims"),
                new com.google.gson.reflect.TypeToken<List<ReferralClaim>>() {
                }.getType());
        Referrals.getInstance().load(banned, amounts);
        return this;
    }
}
