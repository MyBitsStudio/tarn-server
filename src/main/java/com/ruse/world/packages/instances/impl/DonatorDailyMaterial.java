package com.ruse.world.packages.instances.impl;

import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.skills.S_Skills;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DonatorDailyMaterial extends Instance {

    private final Player owner;
    private final long times;

    private long claimedWell = 0L;
    private boolean didHole = false;

    public DonatorDailyMaterial(Player owner, long time) {
        super(Locations.Location.LORDS);
        this.owner = owner;
        this.times = time;
    }

    @Override
    public void process(){
        super.process();
        long time = System.currentTimeMillis() - started;
        long timea = times - time;
        String timeStamp = ""+ timea / (1000 * 60);
        playerList.stream()
                .filter(Objects::nonNull)
                .forEach(p -> {
                    p.getPacketSender().sendWalkableInterface(63000, true);
                    p.getPacketSender().sendString(63004, timeStamp+"M");
                });

        if(System.currentTimeMillis() >= (started + times)){
            owner.sendMessage("Your daily material has expired.");
            destroy();
        }
    }

    @Override
    public void start(){
        owner.sendMessage("You have started your daily donator material instance.");

        moveTo(owner, new Position(3363, 9638));
        add(owner);

    }

    @Override
    public boolean handleObjectClick(Player player, final @NotNull GameObject object, int option){
        switch(object.getId()){
            case 10782 -> {
                if(object.getPosition().getX() == 3353 && object.getPosition().getY() == 9640){
                   if(claimedWell + (1000 * 60 * 5) < System.currentTimeMillis()) {
                       claimedWell = System.currentTimeMillis();
                       int amount = Misc.random(10000, 50000);
                       player.getInventory().add(995, amount);
                       player.sendMessage("You have claimed "+amount+" coins from the well.");
                       player.sendMessage("You must wait 5 minutes before claiming another well");
                   } else {
                       player.sendMessage("This well is not ready to claim.");
                   }
                   return true;
                } else if(object.getPosition().getX() == 3374 && object.getPosition().getY() == 9640){
                   if(player.getNewSkills().getCurrentLevel(S_Skills.CRAFTING) <= 24){
                          player.sendMessage("You need a crafting level of 25 to use this well.");
                          return true;
                   } else {
                       if(claimedWell + (1000 * 60 * 5) < System.currentTimeMillis()) {
                           claimedWell = System.currentTimeMillis();
                           int amount = Misc.random(10, 50), ticket = Misc.random(21814, 21816);
                           player.getInventory().add(ticket, amount);
                           player.sendMessage("You have claimed "+amount+" "+ ItemDefinition.forId(ticket).getName() +" from the well.");
                           player.sendMessage("You must wait 5 minutes before claiming another well");
                       } else {
                           player.sendMessage("This well is not ready to claim.");
                       }
                   }
                   return true;
                } else if(object.getPosition().getX() == 3363 && object.getPosition().getY() == 9650){
                    if(player.getNewSkills().getCurrentLevel(S_Skills.CRAFTING) <= 74){
                        player.sendMessage("You need a crafting level of 75 to use this well.");
                        return true;
                    } else {
                        if(claimedWell + (1000 * 60 * 5) < System.currentTimeMillis()) {
                            claimedWell = System.currentTimeMillis();
                            int amount = Misc.random(1, 3), ticket = Misc.random(23147, 22149);
                            player.getInventory().add(ticket, amount);
                            player.sendMessage("You have claimed "+amount+" "+ ItemDefinition.forId(ticket).getName() +" from the well.");
                            player.sendMessage("You must wait 5 minutes before claiming another well");
                        } else {
                            player.sendMessage("This well is not ready to claim.");
                        }
                    }
                    return true;
                } else if(object.getPosition().getX() == 3363 && object.getPosition().getY() == 9629){
                    if(player.getNewSkills().getCurrentLevel(S_Skills.CRAFTING) <= 98){
                        player.sendMessage("You need a crafting level of 99 to use this well.");
                        return true;
                    } else {
                        if(claimedWell + (1000 * 60 * 10) < System.currentTimeMillis()) {
                            claimedWell = System.currentTimeMillis();
                            int amount = Misc.random(1, 4), ticket = 10946;
                            player.getInventory().add(ticket, amount);
                            player.sendMessage("You have claimed "+amount+" "+ ItemDefinition.forId(ticket).getName() +" from the well.");
                            player.sendMessage("You must wait 10 minutes before claiming this well");
                        } else {
                            player.sendMessage("This well is not ready to claim.");
                        }
                    }
                    return true;
                }
                return true;
            }
            case 10803 -> {
                if(!didHole){
                    if(player.getInventory().contains(995, 1000000)){
                        player.getInventory().delete(995, 1000000);
                        player.getPacketSender().sendMessage("You have paid 1M coins to help everybody else.");
                        didHole = true;

                        World.getPlayers().stream().filter(Objects::nonNull)
                                .forEach(p -> {
                                    p.sendMessage("@red@[DAILY]@whi@ "+player.getUsername()+" has donated to the hole and coins have started raining.");
                                    p.getInventory().add(995, 50000);
                                    p.sendMessage("@red@[HOLE]@whi@ You have received 50K coins from the hole.");
                                });
                    } else {
                        player.getPacketSender().sendMessage("You need 1M coins for this hole.");
                    }
                }
                return true;
            }
        }
        return false;
    }
}
