package com.ruse.world.packages.dialogue.impl.slayer;

import com.google.common.base.Objects;
import com.ruse.model.container.impl.Shop;
import com.ruse.world.World;
import com.ruse.world.content.skill.impl.slayer.Slayer;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.DialogueType;

public class SlayerDialogue extends Dialogue {
    public SlayerDialogue(Player player) {
        super(player);
    }

    @Override
    public DialogueType type() {
        return DialogueType.NPC_STATEMENT;
    }

    @Override
    public DialogueExpression animation() {
        return DialogueExpression.TALK_SWING;
    }

    @Override
    public String[] items() {
        return new String[0];
    }

    @Override
    public void next(int stage) {
        switch(stage){
            case 0 -> sendNpcChat("Hey there! ", "What can I do for you?");
            case 1-> {
                if(Objects.equal(getPlayer().getSlayer().getSlayerTask(), SlayerTasks.NO_TASK)){
                    sendOption("I'd like a Slayer task", "I'd like to view your Slayer rewards",
                            "I'd like to view your stock of Slayer items",
                            getPlayer().getSlayer().getDuoPartner() != null ? "I'd like to reset my duo team" : "Nothing, thanks");
                } else {
                    sendOption("What's my current assignment?", "I'd like to reset my Slayer Task",
                            "How many points do I currently receive per task?",
                            getPlayer().getSlayer().getDuoPartner() != null ? "I'd like to reset my duo team"
                                    : "Nothing, thanks");
                }
            }
        }
    }

    @Override
    public int id() {
        return 5;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(getStage()){
            case 1 ->{
                if(Objects.equal(getPlayer().getSlayer().getSlayerTask(), SlayerTasks.NO_TASK)){
                    switch(option){

                        case 1 -> {
                            getPlayer().getSlayer().assignTask();
                            end();
                            return true;
                        }

                        case 2 ->{
                            if (getPlayer().getSlayer().getSlayerMaster().getNpcId() == 1597) {
                                Shop.ShopManager.getShops().get(47).open(getPlayer());
                            }
                            if (getPlayer().getSlayer().getSlayerMaster().getNpcId() == 9085) {
                                Shop.ShopManager.getShops().get(472).open(getPlayer());
                            }
                            if (getPlayer().getSlayer().getSlayerMaster().getNpcId() == 8275) {
                                Shop.ShopManager.getShops().get(471).open(getPlayer());
                            }
                            if (getPlayer().getSlayer().getSlayerMaster().getNpcId() == 9000) {
                                Shop.ShopManager.getShops().get(107).open(getPlayer());
                            }
                            end();
                            return true;
                        }

                        case 3-> {
                            Shop.ShopManager.getShops().get(40).open(getPlayer());
                            end();
                            return true;
                        }

                        case 4 -> {
                            getPlayer().getPacketSender().sendInterfaceRemoval();
                            if (getPlayer().getSlayer().getDuoPartner() != null) {
                                Slayer.resetDuo(getPlayer(), World.getPlayerByName(getPlayer().getSlayer().getDuoPartner()));
                            }
                            end();
                            return true;
                        }
                    }
                } else {
                    switch(option){
                        case 1-> {
                            DialogueManager.sendDialogue(getPlayer(), new FindTask(getPlayer()), getPlayer().getSlayer().getSlayerMaster().getNpcId());
                            end();
                            return true;
                        }
                        case 2->{
                            DialogueManager.sendDialogue(getPlayer(), new ResetTask(getPlayer()), getPlayer().getSlayer().getSlayerMaster().getNpcId());
                            end();
                            return true;
                        }
                        case 3-> {
                            DialogueManager.sendDialogue(getPlayer(), new TotalPoints(getPlayer(), getPlayer().getSlayer().getSlayerMaster()), getPlayer().getSlayer().getSlayerMaster().getNpcId());
                            end();
                            return true;
                        }
                        case 4 -> {
                            getPlayer().getPacketSender().sendInterfaceRemoval();
                            if (getPlayer().getSlayer().getDuoPartner() != null) {
                                Slayer.resetDuo(getPlayer(), World.getPlayerByName(getPlayer().getSlayer().getDuoPartner()));
                            }
                            end();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
