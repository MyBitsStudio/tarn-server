//package com.ruse.world.content.raids;
//
//import com.ruse.GameSettings;
//import com.ruse.model.GameObject;
//import com.ruse.model.Position;
//import com.ruse.world.entity.impl.Character;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.instance.DestroyMode;
//import com.ruse.world.instance.MapInstance;
//import com.ruse.world.region.dynamic.DynamicMap;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public abstract class RaidRoom extends MapInstance {
//
//    protected Raid raid;
//    protected RaidBoss boss;
//    protected List<RaidNPC> stageNPCs = new CopyOnWriteArrayList<>();
//
//    protected AtomicBoolean finished = new AtomicBoolean(false), started = new AtomicBoolean(false);
//
//    protected int chunkX, chunkY, height, width;
//
//    @Getter
//    @Setter
//    private DynamicMap map;
//
//    public RaidRoom(Raid raid,  @NonNull DestroyMode mode, int chunkX, int chunkY, int width, int height) {
//        super(mode);
//
//        this.raid = raid;
//        this.chunkX = chunkX;
//        this.chunkY = chunkY;
//        this.width = width;
//        this.height = height;
//
//        initialise();
//    }
//
//    public void setRaidBoss(RaidBoss boss){
//    	this.boss = boss;
//    }
//
//
//    public abstract Position playerSpawn();
//    public abstract Position deathPosition();
//
//    public abstract void onRoomStartHook();
//    public abstract void onRoomFinishHook();
//
//    public abstract RaidRoom nextRoom();
//
//    public boolean isFinalRoom(){ return false;}
//
//    public boolean handleObjectClick(Player player, GameObject object, int option){ return false;}
//
//    public boolean handRoomExitObject(Player player, GameObject object, int option){ return false;}
//
//
//    public void setBoss(RaidBoss boss){ this.boss = boss;}
//    public RaidBoss getBoss(){ return boss;}
//    public boolean isFinished(){ return finished.get();}
//    public boolean isStarted(){ return started.get();}
//    public void setStarted(boolean started){ this.started.set(started);}
//    public void setFinished(boolean finished){ this.finished.set(finished);}
//    public Raid getRaid(){ return raid;}
//
//    @Override
//    public void process() {
//        if(isStarted()) {
//            if (raid == null) {
//                System.out.println("Raid is null");
//                setFinished(true);
//                destroy();
//            } else {
//                if (raid.isFinished()) {
//                    System.out.println("Raid is finished");
//                    setFinished(true);
//                }
//
//                if (raid.getCurrentRoom() != this) {
//                    System.out.println("Raid current room is not this room");
//                    setFinished(true);
//                    destroy();
//                }
//
//                if (raid.getParty() != null) {
//                    raid.getParty().refreshInterface();
//                }
//
//                if(getBoss().isDying()){
//                    System.out.println("Boss is dying");
//                    setFinished(true);
//                }
//            }
//        }
//        System.out.println("Processing raid room");
//    }
//
//    @Override
//    public void onEnter(Player player) {
//
//    }
//
//    @Override
//    public void onLeave(Player player) {
//
//    }
//
//    @Override
//    public void onLogout(Player player) {
//        // Put checks here to remove from raid and party
//    }
//
//    @Override
//    public void createMap(){
//        if (map != null) {
//            throw new IllegalStateException("The instance has already started");
//        }
//        map = DynamicMap.find((width + 7) / 8, (height + 7) / 8);
//        if (map == null) {
//            throw new IllegalStateException("Failed to find an empty map instance");
//        }
//        for (int level = 0; level < 4; level++) {
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    map.copy(level, chunkX + x, chunkY + y, level, x, y, 0);
//                }
//            }
//        }
//        map.apply();
//    }
//
//    @Override
//    public Position getTeleportPosition(Player player){
//        return playerSpawn();
//    }
//
//    @Override
//    public Position getLobbyPosition(Player player) {
//        return GameSettings.DEFAULT_POSITION;
//    }
//
//    @Override
//    public boolean isMultiArea(Character character) {
//        return true;
//    }
//}
