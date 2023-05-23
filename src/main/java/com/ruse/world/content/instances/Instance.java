package com.ruse.world.content.instances;

import com.ruse.GameSettings;
import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
        for(NPC npc : npcList){
            if(npc != null){
                if(npc.isDying())
                    return false;
            }
        }
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
            playerList.add(p);
        }
        for(;; npcs.poll()){
            NPC n = npcs.peek();
            if(n == null)
                break;
            if(npcList.contains(n))
                continue;
            npcList.add(n);
            addNPC(n);
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
            npcList.remove(n);
            removeNPC(n);
        }
    }

    private void preProcess(){
        for(Player player : playerList){
            if(player == null)
                continue;
            if(!location.equals(player.getLocation())){
                removePlayer.add(player);
            }
        }
        World.getPlayers().stream().filter(Objects::nonNull)
                .filter(player -> player.getLocation().equals(location))
                .filter(player -> player.getInstance() != this)
                .filter(player -> playerList.size() >= 1 && player.getPosition().getZ() == playerList.get(0).getPosition().getZ())
                .filter(player -> !playerList.contains(player))
                .filter(player -> !players.contains(player))
                .filter(player -> !player.getRights().isStaff())
                .forEach(player -> player.moveTo(GameSettings.DEFAULT_POSITION.copy()));
    }

    public void spawnNPC(NPC npc){
        npcs.add(npc);
    }

    protected void addPlayer(@NotNull Player player){
        player.setInstance(this);
    }

    protected void removePlayer(@NotNull Player player){
        player.setInstance(null);

        if(playerList.size() == 0){
            destroy();
        }

        player.moveTo(GameSettings.DEFAULT_POSITION.copy());
    }

    private void addNPC(NPC npc){
        npc.setInstance(this);
        World.register(npc);
    }

    protected void removeNPC(NPC npc){
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

    public void dispose(Player player){
        if (player.getInstance() != null)
            player.getInstance().destroy();

        player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        cancelCurrentActions(player);
        player.moveTo(GameSettings.DEFAULT_POSITION.copy()).setPosition(GameSettings.DEFAULT_POSITION.copy());
        player.getMovementQueue().setLockMovement(false).reset();

        player.getPacketSender().sendInterfaceRemoval();
    }

    public void moveTo(Player player, Position pos){
        player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        cancelCurrentActions(player);
        player.setLocation(location);
        player.moveTo(pos).setPosition(pos);
        player.getMovementQueue().setLockMovement(false).reset();

        player.getPacketSender().sendInterfaceRemoval();
    }

    public void cancelCurrentActions(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        player.setTeleporting(false);
        player.setWalkToTask(null);
        player.setInputHandling(null);
        player.getSkillManager().stopSkilling();
        player.setEntityInteraction(null);
        player.getMovementQueue().setFollowCharacter(null);
        player.getCombatBuilder().cooldown(false);
        player.setResting(false);
    }
}
