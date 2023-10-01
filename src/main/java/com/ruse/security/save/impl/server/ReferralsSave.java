package com.ruse.security.save.impl.server;

import com.ruse.io.ThreadProgressor;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.packages.referral.Referrals;

import java.io.FileWriter;
import java.io.IOException;

public class ReferralsSave extends SecureSave {
    public ReferralsSave(){
        ;
    }

    @Override
    public ReferralsSave create() {
        object.add("referrals", builder.toJsonTree(Referrals.getInstance().getReferralMaps()));
        object.add("claims", builder.toJsonTree(Referrals.getInstance().getClaims()));
        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(false, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.REFERRALS)) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }
}
