package com.ruse.world.packages.tracks.impl.pvm;

import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tracks.Track;

public class PvMTrack extends Track {

    protected PvmTask[] tasks = new PvmTask[3];
    public PvMTrack(Player player) {
        super(player);
        this.rewards = new PvMTrackRewards();
        this.hasPremium = false;
        this.position = 0;
        this.maxLevel = 50;
    }

    @Override
    public void sendInterfaceList() {

    }

    private void setTasks(){
        int rank = Math.floorDiv(position, 10);
        tasks = new PvmTask[3];
        for(int i = 0; i < 3; i++){
            PvMTasks task = PvMTasks.getRandomTaskForRank(rank);
            tasks[i] = new PvmTask(task.getNpcId(), Misc.random(task.getLimits()[0], task.getLimits()[1]));
            player.sendMessage(("@gre@[PVM] @bla@Your task is to kill "+tasks[i].getAmount()+" "+NpcDefinition.forId(tasks[i].getNpcId()).getName()));
        }
    }

    public void handleTask(int npcId){
        for(PvmTask task : tasks){
            if(task.getNpcId() == npcId){
                if(task.increment()){
                    player.getPacketSender().sendMessage("You have killed "+task.getCurrent()+"/"+task.getAmount()+" "+ NpcDefinition.forId(task.getNpcId()).getName());
                } else {
                    player.getPacketSender().sendMessage("You have completed your task!");
                    if(position == maxLevel){
                        player.getPacketSender().sendMessage("You have completed the track!");
                        rewards.claimRewards(player, position, this.hasPremium, this.premium);
                        return;
                    }
                    position++;
                    rewards.claimRewards(player, position, this.hasPremium, this.premium);
                    setTasks();
                }
                break;
            }
        }
    }

    public void onLogin(){
        if(tasks == null || tasks[0] == null || tasks[1] == null || tasks[2] == null){
            setTasks();
        }
    }

}
