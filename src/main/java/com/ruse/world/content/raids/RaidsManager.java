//package com.ruse.world.content.raids;
//
//import com.ruse.world.entity.impl.player.Player;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class RaidsManager {
//
//    private static RaidsManager raids;
//
//    public static RaidsManager getRaids() {
//        if(raids == null)
//            raids = new RaidsManager();
//        return raids;
//    }
//
//
//    private final Map<String, Raid> active = new ConcurrentHashMap<>();
//    private final Map<String, RaidParty> parties = new ConcurrentHashMap<>();
//
//    public RaidsManager(){
//
//    }
//
//    public void addRaid(Player player, Raid raid){
//        if(active.containsKey(player.getUsername().toLowerCase()))
//            return;
//        active.put(player.getUsername().toLowerCase(), raid);
//    }
//
//    public void removeRaid(Player player){
//        if(!active.containsKey(player.getUsername().toLowerCase()))
//            return;
//        active.remove(player.getUsername().toLowerCase());
//    }
//
//    public Raid getRaid(Player player){
//        if(!active.containsKey(player.getUsername().toLowerCase()))
//            return null;
//        return active.get(player.getUsername().toLowerCase());
//    }
//
//    public void addParty(Player player, RaidParty party){
//        if(parties.containsKey(player.getUsername().toLowerCase()))
//            return;
//        parties.put(player.getUsername().toLowerCase(), party);
//    }
//
//    public void removeParty(Player player){
//        if(!parties.containsKey(player.getUsername().toLowerCase()))
//            return;
//        parties.remove(player.getUsername().toLowerCase());
//    }
//
//    public RaidParty getParty(Player player){
//        if(!parties.containsKey(player.getUsername().toLowerCase()))
//            return null;
//        return parties.get(player.getUsername().toLowerCase());
//    }
//}
