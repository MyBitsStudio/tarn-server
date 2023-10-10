package com.ruse.world.packages.skills.crafting;

import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.seasonpass.SeasonPassManager;

public class Craft {

    private final Player player;
    private CraftType type = CraftType.MATERIALS;

    private CraftingProducts product;

    private final int CHISEL = 20897;
    public Craft(Player player){
        this.player = player;
    }

    public void open(){
        player.getPA().sendFrame126("Crafting", 47255);
        selectTab(-18272);
        player.getPA().sendInterface(47250);
    }

    public boolean selectTab(int buttonId) {
        switch (buttonId) {
            case -18272 -> {
                type = CraftType.MATERIALS;
                loadList();
                return true;
            }
            case -18269 -> {
                type = CraftType.EQUIP;
                loadList();
                return true;
            }
            case -18266 -> {
                type = CraftType.OTHER;
                loadList();
                return true;
            }
            case -18284 -> {
                player.getPA().removeInterface();
                return true;
            }
        }
        return false;
    }

    public void clearList() {
        for (int i = 47281; i < 47281 + 50; i++) {
            player.getPA().sendFrame126("", i);
        }
    }

    public void loadList() {
        clearList();
        int frame = 47281;
        for (CraftingProducts data : CraftingProducts.values()) {
            if (data.getType() == type.ordinal()) {
                player.getPA().sendFrame126(Misc.capitalize(data.name().replace("_", " ")), frame++);
                if (frame >= 47281 + 50) {
                    System.err.println("You are placing a value greater than the max list items");
                    return;
                }
            }
        }
    }

    public void clearItems() {
        for (int i = 0; i < 12; i++)
            player.getPA().sendItemOnInterface(47332, -1, i, -1);
        player.getPA().sendItemOnInterface(47263, -1, 0, -1);
    }

    public void displayItems(int buttonId) {
        clearItems();
        for (CraftingProducts data : CraftingProducts.values()) {
            if (data.getType() == type.ordinal()) {
                if (buttonId == -18255 + data.getBase()){
                    product = data;
                    for (int i = 0; i < data.getRequiredItems().length; i++)
                        player.getPA().sendItemOnInterface(47332, data.getRequiredItems()[i].getId(), i,
                                data.getRequiredItems()[i].getAmount());
                    player.getPA().sendItemOnInterface(47263, data.getFinalProduct(), 0, 1);
                    player.getPA().sendItemOnInterface(47278, -1, 0, 1);
                }
            }
        }
    }

    public boolean button(int buttonId) {
        for (CraftingProducts data : CraftingProducts.values()) {
            if (data.getType() == type.ordinal()) {
                if (buttonId == -18255 + data.getBase()) {
                    player.getPA().sendFrame126("Required Level: " + data.getReqLevel(), 47262);
                    displayItems(buttonId);
                    return true;
                }
            }
        }
        return false;
    }

    public void craftMaterial(){
        if(product != null){
            if(!player.getInventory().contains(CHISEL)){
                player.sendMessage("You do not have a chisel to craft this.");
                return;
            }

            if(player.getSkillManager().getCurrentLevel(Skill.CRAFTING) >= product.getReqLevel()){
                if(player.getInventory().contains(product.getRequiredItems())){
                    if(player.getInventory().getFreeSlots() <= 0){
                        player.sendMessage("You do not have enough inventory space to craft this.");
                        return;
                    }

                    player.performAnimation(new Animation(712));
                    for(Item item : product.getRequiredItems()){
                        player.getInventory().delete(item);
                    }

                    if(product.getType() != 0){
                        if(Misc.random(100) < 49){
                            player.getInventory().add(new Item(product.getFinalProduct()));
                            player.getSkillManager().addExperience(Skill.CRAFTING, product.getXp());
                            player.sendMessage("You have crafted a " + ItemDefinition.forId(product.getFinalProduct()).getName() + ".");
                            player.getSeasonPass().incrementExp(24 * product.getXp(), false);
                        } else {
                            player.sendMessage("You have failed to craft the item.");
                        }
                    } else {
                        player.getInventory().add(new Item(product.getFinalProduct()));
                        player.getSkillManager().addExperience(Skill.CRAFTING, product.getXp());
                        player.sendMessage("You have crafted a " + ItemDefinition.forId(product.getFinalProduct()).getName() + ".");
                        player.getSeasonPass().incrementExp(2 * product.getXp(), false);
                    }

                    player.getPA().sendFrame126("Required Level: " + product.getReqLevel(), 47262);
                    displayItems(-18255 + product.getBase());
                } else {
                    player.sendMessage("You do not have the required items to craft this.");
                }
            } else {
                player.sendMessage("You do not have the required level to craft this.");
            }
        } else {
            player.sendMessage("You must select a product to craft.");
        }
    }
}
