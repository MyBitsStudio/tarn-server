package com.ruse.world.content.instances;

import com.ruse.GameSettings;
import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.world.World;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    protected long canLeave;

    public Instance(Locations.Location location){
        this.location = location;
    }

    public void setCanLeave(long time) {
    	this.canLeave = time;
    }

    public boolean canLeave() {
        System.out.println("Can leave: " + (canLeave < System.currentTimeMillis()) + " " + canLeave + " " + System.currentTimeMillis());
    	return canLeave < System.currentTimeMillis();
    }

    public void process(){
        preProcess();
        for(;; players.poll()){
            Player p = players.peek();
            if(p == null)
                break;
            if(playerList.contains(p))
                continue;
            System.out.println("Adding player to instance: " + p.getUsername());
            playerList.add(p);
        }
        for(;; npcs.poll()){
            NPC n = npcs.peek();
            if(n == null)
                break;
            if(npcList.contains(n))
                continue;
            System.out.println("Adding npc to instance: " + n.getId());
            npcList.add(n);
            addNPC(n);
        }
        for(;; removePlayer.poll()){
            Player p = removePlayer.peek();
            if(p == null)
                break;
            System.out.println("Removing player from instance: " + p.getUsername());
            playerList.remove(p);
            removePlayer(p);
        }
        for(;; removeNPC.poll()){
            NPC n = removeNPC.peek();
            if(n == null)
                break;
            System.out.println("Removing npc from instance: " + n.getId());
            npcList.remove(n);
            removeNPC(n);
        }
    }

    private void preProcess(){
        System.out.println("Preprocessing instance: " + location.name());
        for(Player player : playerList){
            if(player == null)
                continue;
            if(!location.equals(player.getLocation())){
                System.out.println("Removing player from instance: " + player.getUsername());
                removePlayer.add(player);
            }
        }
    }

    public void spawnNPC(NPC npc){
        npcs.add(npc);
    }

    private void addPlayer(@NotNull Player player){
        player.setInstance(this);

    }

    private void removePlayer(@NotNull Player player){
        player.setInstance(null);

        if(playerList.size() == 0){
            System.out.println("Destroying instance: " + location.name());
            destroy();
        }

        player.moveTo(GameSettings.DEFAULT_POSITION.copy());
    }

    private void addNPC(NPC npc){
        npc.setInstance(this);
        World.register(npc);
    }

    private void removeNPC(NPC npc){
        npc.setInstance(null);
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
            npcs.add((NPC) en);
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
        System.out.println("Destroying instance: " + location.name());
        for(Player player : playerList){
            removePlayer(player);
        }
        for(NPC npc : npcList){
            removeNPC(npc);
        }
        for(GameObject object : objects){
            World.deregister(object);
        }
    }
}
