package com.ruse.world.packages.raid;

public class RaidManager {

    private static RaidManager manager;

    public static RaidManager getManager(){
        if(manager == null){
            manager = new RaidManager();
        }
        return manager;
    }


}
