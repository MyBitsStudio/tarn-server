//package com.ruse.world.packages.dialogue.impl.slot;
//
//import com.ruse.model.Item;
//import com.ruse.model.container.impl.Equipment;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.dialogue.Dialogue;
//import com.ruse.world.packages.dialogue.DialogueExpression;
//import com.ruse.world.packages.dialogue.DialogueType;
//import com.ruse.world.packages.slot.SlotBonus;
//import com.ruse.world.packages.slot.SlotEffect;
//
//public class EquipSlot extends Dialogue {
//
//    private int item = 23150;
//    private final Item perk;
//    private String name = "Item";
//    public EquipSlot(Player player, int item, Item perk) {
//        super(player);
//        this.item = item;
//        this.perk = perk;
//        this.name = perk.getDefinition().getName();
//    }
//
//    @Override
//    public DialogueType type() {
//        return DialogueType.STATEMENT;
//    }
//
//    @Override
//    public DialogueExpression animation() {
//        return null;
//    }
//
//    @Override
//    public String[] items() {
//        return new String[0];
//    }
//
//    @Override
//    public void next(int stage) {
//        switch(stage){
//            case 0 -> sendRegularStatement("Are you sure you want to add this perk to your slot?");
//            case 1 -> sendOption("Use Perk Item?", "Yes", "Cancel");
//            case 2 -> {
//                boolean weaponPerk = SlotEffect.values()[perk.getEffect()].isWeapon();
//                if(weaponPerk){
//                    sendOption("Weapon Perk. Add To Weapon Slot?", "Yes", "Cancel");
//                } else {
//                    sendRegularStatement("Please choose the slot to use.");
//                }
//            }
//            case 3 -> {
//                boolean weaponPerk = SlotEffect.values()[perk.getEffect()].isWeapon();
//                if(weaponPerk){
//                    sendRegularStatement("There is an effect in place. Do you wish to replace it?");
//                } else {
//                    sendOption("Pick A Slot", "Halo", "Helmet", "Gemstone", "Next");
//                }
//            }
//            case 4->{
//                boolean weaponPerk = SlotEffect.values()[perk.getEffect()].isWeapon();
//                if(weaponPerk){
//                    sendRegularStatement("If you don't have a transfer crystal, you will lose it.");
//                } else {
//                    sendOption("Pick A Slot", "Quiver", "Cape", "Amulet", "Next");
//                }
//            }
//        }
//    }
//
//    @Override
//    public int id() {
//        return 14;
//    }
//
//    @Override
//    public void onClose() {
//
//    }
//
//    @Override
//    public boolean handleOption(int option) {
//        switch(getStage()){
//            case 1->{
//                switch(option){
//                    case FIRST_OPTION_OF_TWO-> {
//                        nextStage();
//                        return true;
//                    }
//                    case SECOND_OPTION_OF_TWO -> {
//                        end();
//                        return true;
//                    }
//                }
//            }
//            case 2 -> {
//                switch(option){
//                    case FIRST_OPTION_OF_TWO-> {
//                        if(getPlayer().getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT].getEffect() == SlotEffect.NOTHING) {
//                            getPlayer().getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT] = new SlotBonus(SlotEffect.values()[perk.getEffect()], perk.getBonus());
//                            getPlayer().getInventory().delete(perk);
//                            getPlayer().getEquipment().refreshItems();
//                            getPlayer().sendMessage("You have added the perk to your weapon slot.");
//                            end();
//                        } else {
//                            nextStage();
//                        }
//                        return true;
//                    }
//                    case SECOND_OPTION_OF_TWO -> {
//                        end();
//                        return true;
//                    }
//                }
//            }
//            case 3 -> {
//                switch(option){
//                    case FIRST_OPTION_OF_FOUR-> {
//                        if(getPlayer().getEquipment().getSlotBonuses()[Equipment.HALO_SLOT].getEffect() == SlotEffect.NOTHING) {
//                            getPlayer().getEquipment().getSlotBonuses()[Equipment.HALO_SLOT] = new SlotBonus(SlotEffect.values()[perk.getEffect()]);
//                            getPlayer().getInventory().delete(perk);
//                            getPlayer().getEquipment().refreshItems();
//                            getPlayer().sendMessage("You have added the perk to your halo slot.");
//                            end();
//                        } else {
//                            nextStage();
//                        }
//                        return true;
//                    }
//                    case SECOND_OPTION_OF_FOUR -> {
//                        if(getPlayer().getEquipment().getSlotBonuses()[Equipment.HEAD_SLOT].getEffect() == SlotEffect.NOTHING) {
//                            getPlayer().getEquipment().getSlotBonuses()[Equipment.HEAD_SLOT] = new SlotBonus(SlotEffect.values()[perk.getEffect()]);
//                            getPlayer().getInventory().delete(perk);
//                            getPlayer().getEquipment().refreshItems();
//                            getPlayer().sendMessage("You have added the perk to your weapon slot.");
//                            end();
//                        } else {
//                            nextStage();
//                        }
//                        return true;
//                    }
//                    default -> {
//                        end();
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//}
