package com.ruse.world.packages.transmorgify;

import com.ruse.model.Flag;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Transmorgify {

    @Getter
    private final List<Transformations> transformations = new ArrayList<>();

    private final Player player;

    @Getter
    private Transformations currentTransformation;

    public Transmorgify(Player player){
        this.player = player;
    }

    public void addTransformation(Transformations transformation){
        transformations.add(transformation);
    }

    public void load(List<Transformations> transformations){
        this.transformations.addAll(transformations);
    }

    public void returnToNormal(){
        if(currentTransformation != null){
            currentTransformation = null;
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }

    public void transmorgify(Transformations trans){
        if(currentTransformation != null){
            currentTransformation = null;
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        currentTransformation = trans;
        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public boolean handleSouls(Player player, int itemId){
        switch(itemId){
            case 17985 -> {
                if(transformations.contains(Transformations.DEATH)){
                    player.sendMessage("You already have this transformation.");
                    return true;
                }
                player.getInventory().delete(17985, 1);
                addTransformation(Transformations.DEATH);
                player.sendMessage("You have unlocked the Death transformation.");
                return true;
            }
            case 17986 -> {
                if(transformations.contains(Transformations.WARLORD)){
                    player.sendMessage("You already have this transformation.");
                    return true;
                }
                player.getInventory().delete(17986, 1);
                addTransformation(Transformations.WARLORD);
                player.sendMessage("You have unlocked the Warlord transformation.");
                return true;
            }
            case 17987 -> {
                if(transformations.contains(Transformations.PIRATE)){
                    player.sendMessage("You already have this transformation.");
                    return true;
                }
                player.getInventory().delete(17987, 1);
                addTransformation(Transformations.PIRATE);
                player.sendMessage("You have unlocked the Pirate transformation.");
                return true;
            }
        }
        return false;

    }

    public boolean handleButtons(int button){
        if(button >= 167101 && button <= 167151){
            int perk = (button - 167101);
            player.getVariables().setSetting("trans-chosen", perk);
            sendInterface(player.getVariables().getIntValue("trans-chosen"));
            return true;
        }
        switch(button){
            case 167005 -> {
                if(currentTransformation != null){
                    returnToNormal();
                    player.getPacketSender().sendInterfaceRemoval();
                    return true;
                }
                Transformations transform = transformations.get(player.getVariables().getIntValue("trans-chosen"));
                if(transform != null){
                    transmorgify(transform);
                }
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
            case 167007 -> {
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void sendInterface(int chosen){
        if(!World.attributes.getSetting("transform")){
            player.sendMessage("Transform is currently disabled.");
            return;
        }

        reset(player);

        Transformations transform;
        if(transformations.isEmpty()){
            transform = Transformations.DEATH;
        } else {
            transform = transformations.get(chosen);
        }

        if(transform == null){
           transform = Transformations.DEATH;
        }

        if(currentTransformation == null){
            player.getPacketSender().sendString(167006, "Transform");
        } else {
            player.getPacketSender().sendString(167006, "Normal");
        }

        player.getPacketSender().sendInterface(167000);
        int i = 167101;
        for(Transformations transforms : transformations){
            player.getPacketSender().sendString(i++, NpcDefinition.forId(transforms.getNpcId()).getName());
        }

        player.getPacketSender().sendString(167003, NpcDefinition.forId(transform.getNpcId()).getName());
        player.getPacketSender().sendString(167004, "Transform into "+NpcDefinition.forId(transform.getNpcId()).getName());
    }

    private static void reset(Player player){
        for(int i = 167101; i < 167151; i++){
            player.getPacketSender().sendString(i, "");
        }
    }

}
