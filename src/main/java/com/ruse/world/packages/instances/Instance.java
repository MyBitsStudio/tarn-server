package com.ruse.world.packages.instances;

import com.ruse.GameSettings;
import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Instance {

    protected final Queue<Player> players = new LinkedList<>();
    protected final Queue<NPC> npcs = new LinkedList<>();

    protected final Queue<Player> removePlayer = new LinkedList<>();
    protected final Queue<NPC> removeNPC = new LinkedList<>();

    protected final List<Player> playerList = new CopyOnWriteArrayList<>();
    protected final List<NPC> npcList = new CopyOnWriteArrayList<>();
    protected final List<GameObject> objects = new CopyOnWriteArrayList<>();

    protected final Locations.Location location;

    protected InstanceDifficulty difficulty = InstanceDifficulty.EASY;
    protected long canLeave, started = System.currentTimeMillis();
    protected String instanceId = SecurityUtils.createRandomString(16);

    public int getZ = -1;

    public Instance(Locations.Location location){
        this.location = location;
    }

    public void setDifficulty(InstanceDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public InstanceDifficulty getDifficulty() {
        return difficulty;
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public void setCanLeave(long time) {
        this.canLeave = time;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public boolean canLeave() {
        return canLeave < System.currentTimeMillis();
    }

    public String getTime(){
        long time = System.currentTimeMillis() - started;
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return hours + ":" + minutes + ":" + seconds;
    }

    public void process(){
        preProcess();
        for(;; players.poll()){
            Player p = players.peek();
            if(p == null)
                break;
            if(playerList.contains(p))
                continue;
            if(playerList.isEmpty())
                getZ = p.getIndex() * 4;

            playerList.add(p);
        }
        for(;; npcs.poll()){
            NPC n = npcs.peek();
            if(n == null)
                break;
            if(npcList.contains(n))
                continue;
            addNPC(n);
            npcList.add(n);
        }
        for(;; removePlayer.poll()){
            Player p = removePlayer.peek();
            if(p == null)
                break;
            playerList.remove(p);
            removePlayer(p);
        }
        for(;; removeNPC.poll()){
            NPC n = removeNPC.peek();
            if(n == null)
                break;
            removeNPC(n);
            npcList.remove(n);
        }
        postProcess();
    }

    public void signalSpawn(NPC n){

    }

    private void postProcess(){
         playerList.stream()
                .filter(Objects::nonNull)
                .filter(p -> !location.equals(p.getLocation()))
                .forEach(p -> {
                    System.out.println("Location "+location + " player "+p.getLocation());
                    p.sendMessage("@red@[INSTANCE] How did you get here?");
                    removePlayer.add(p);
                });

         npcList.stream()
                 .filter(Objects::nonNull)
                 .filter(n -> !Objects.equals(n.getInstanceId(), this.instanceId))
                 .forEach(removeNPC::add);

         npcList.stream()
                 .filter(Objects::nonNull)
                 .forEach(n -> {
                     boolean found = false;
                     for(Player player : playerList){
                         if(Arrays.asList(n.getSpawnedFor()).contains(player)){
                             found = true;
                             break;
                         }
                     }
                     if(!found){
                        removeNPC.add(n);
                     }
                 });
    }

    private void preProcess(){
        World.getPlayers().stream().filter(Objects::nonNull)
                .filter(player -> player.getLocation().equals(this.location))
                .filter(player -> player.getPosition().getZ() == getZ)
                .filter(player -> !player.getInstanceId().equals(this.instanceId))
                .filter(player -> !playerList.contains(player))
                .filter(player -> !players.contains(player))
                .filter(player -> !player.getRank().isStaff())
                .forEach(player -> {
                    player.sendMessage("@red@[INSTANCE] This isn't your instance. Moving you home.");
                    player.setInstance(null);
                    player.setLocation(Locations.Location.DEFAULT);
                    player.getPacketSender().sendWalkableInterface(63000, false);
                    player.moveTo(GameSettings.DEFAULT_POSITION.copy()).setPosition(GameSettings.DEFAULT_POSITION.copy());
                    player.getMovementQueue().setLockMovement(false).reset();
                });
    }

    protected void addPlayer(@NotNull Player player){
        player.setInstance(this);
        player.setInstanceId(instanceId);
    }

    protected void removePlayer(@NotNull Player player){
        player.setInstance(null);
        player.setInstanceId("");

        if(playerList.isEmpty()){
           destroy();
        }

        player.getPacketSender().sendWalkableInterface(63000, false);
        player.moveTo(GameSettings.DEFAULT_POSITION);
    }

    protected void addNPC(@NotNull NPC npc){
        npc.setLocation(this.location);
        npc.setPosition(npc.getPosition().setZ(getZ));
        npc.setInstance(this);
        npc.setInstanceId(instanceId);
        World.register(npc);
    }

    protected void removeNPC(NPC npc){
        World.deregister(npc);
    }

    public void add(Entity en){
        if(en instanceof Player){
            if(playerList.contains(en))
                return;
            if(players.contains(en))
                return;
            players.add((Player) en);
            addPlayer((Player) en);
        } else if(en instanceof NPC){
            npcs.add(en.toNpc());
        } else if(en instanceof GameObject){
            if(objects.contains(en))
                return;
            objects.add((GameObject) en);
            World.register(en);
        }
    }


    public void remove(Entity en){
        if(en instanceof Player){
            if(playerList.contains(en) || players.contains(en)){
                if(playerList.contains(en)){
                   removePlayer.add((Player) en);
                } else {
                    players.remove(en);
                }
            }
        } else if(en instanceof NPC){
            if(npcList.contains(en) || npcs.contains(en)){
                if(npcList.contains(en)){
                    removeNPC.add((NPC) en);
                } else {
                    npcs.remove(en);
                }
            }
        } else if(en instanceof GameObject){
            objects.remove(en);
        }
    }

    public void onLogout(Player player){
        remove(player);
    }

    public void destroy(){
        for(Player player : playerList){
            removePlayer(player);
        }
        clear();
        playerList.clear();
        InstanceManager.getManager().removeInstance(instanceId);
    }

    public void clear(){
        for(NPC npc : npcList){
            removeNPC(npc);
        }
        npcList.clear();
        for(GameObject object : objects){
            World.deregister(object);
        }
        objects.clear();

    }


    public void moveTo(Player player, Position pos){
        player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        InstanceManager.getManager().cancelCurrentActions(player);
        player.setLocation(this.location);
        player.moveTo(pos.setZ(player.getIndex() * 4)).setPosition(pos.setZ(player.getIndex() * 4));
        player.getMovementQueue().setLockMovement(false).reset();

        player.getPacketSender().sendInterfaceRemoval();
    }


    public void start(){
    }

    public int cost(){
        return -1;
    }

    public int itemId(){
        return -1;
    }

    public boolean canEnter(Player player){
        return true;
    }

    public String failedEntry(){return "Something went wrong...";}

    public boolean handleObjectClick(Player player, GameObject object, int option){
        return false;
    }

}
