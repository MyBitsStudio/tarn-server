package com.ruse.world.packages.referral;

import com.ruse.model.Item;
import com.ruse.security.save.impl.server.ReferralsSave;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Referrals {

    private static Referrals instance;

    public static Referrals getInstance() {
        if (instance == null) {
            instance = new Referrals();
        }
        return instance;
    }

    public Referrals() {
        start();
    }

    private final Map<String, Integer> referralMaps = new ConcurrentHashMap<>();
    private final List<ReferralClaim> claims = new CopyOnWriteArrayList<>();

    private void start(){
        for(Refer refer : Refer.values()){
            referralMaps.put(refer.getSynthax()[0], 0);
        }
    }
    public void load(Map<String, Integer> maps, List<ReferralClaim> claims){
        referralMaps.putAll(maps);
        this.claims.addAll(claims);
    }

    public void claimReferral(@NotNull Player player, String name){
        if(player.isClaimedReferral()){
            player.sendMessage("You have already claimed a referral.");
            return;
        }

        AtomicBoolean found = new AtomicBoolean(false);
        claims.stream().filter(Objects::nonNull).forEach(claim -> {
            if(claim.name().equalsIgnoreCase(name)){
                found.set(true);
                return;
            }

            if(claim.ip().equalsIgnoreCase(player.getHostAddress())){
                found.set(true);
                return;
            }

            if(claim.mac().equalsIgnoreCase(player.getMac())){
                found.set(true);
                return;
            }
        });

        if(found.get()){
            player.setClaimedReferral(true);
            player.sendMessage("A referral for you has already been found.");
            return;
        }

        addReferral(name);
        claim(player, name);
        save();
    }

    private void save(){
        new ReferralsSave().create().save();
    }

    private void claim(@NotNull Player player, String synthax){
        claims.add(new ReferralClaim(player.getUsername(), player.getMac(), player.getHostAddress(), synthax));

        if(Refer.isRefer(synthax)){
            for(Refer refer : Refer.values()){
                for(String s : refer.getSynthax()){
                    if(s.equalsIgnoreCase(synthax)){
                        for(Item item : refer.getPack()){
                            player.getInventory().add(item);
                        }
                        player.sendMessage("You have claimed a referral pack.");
                        return;
                    }
                }
            }
        } else {
            Refer refer = Refer.BASIC;
            for(Item item : refer.getPack()){
                player.getInventory().add(item);
            }
            player.sendMessage("You have claimed a referral pack.");
        }

    }

    private void addReferral(String name){
        if (referralMaps.containsKey(name)){
            referralMaps.replace(name, referralMaps.get(name) + 1);
        } else {
            referralMaps.put(name, 1);
        }
    }

    public void claimReferalls(@NotNull Player player){
        if (referralMaps.containsKey(player.getUsername())){
            int referrals = referralMaps.get(player.getUsername());
            if(referrals > player.getReferralClaim()){
                for(int i = player.getReferralClaim(); i < referrals; i++){
                    if(i > 10){
                        break;
                    }
                    Item[] items = PlayerRefers.forLevel(i + 1).getItems();
                    for(Item item : items){
                        player.getInventory().addDropIfFull(item.getId(), item.getAmount());
                    }
                    player.setReferralClaim(i + 1);
                }
                player.sendMessage("You have claimed your referrals.");
            } else {
                player.sendMessage("You have no referrals to claim.");
            }
        } else {
            player.sendMessage("You have no referrals to claim.");
        }
    }

}
