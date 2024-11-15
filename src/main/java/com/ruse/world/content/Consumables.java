package com.ruse.world.content;

import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.*;
import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Locations.Location;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.Sounds.Sound;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.minigames.impl.Dueling;
import com.ruse.world.content.minigames.impl.Dueling.DuelRule;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.potion.*;
import com.ruse.world.packages.skills.S_Skills;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Consumables are items that players can use to restore stats/points. Examples
 * of Consumable items: Food, Potions
 *
 * @author Gabriel Hannason
 */
public class Consumables {

    /**
     * Checks if <code>item</code> is a valid consumable food type.
     *
     * @param player The player clicking on <code>item</code>.
     * @param item   The item being clicked upon.
     * @param slot   The slot of the item.
     * @return If <code>true</code> player will proceed to eat said item.
     */
    public static boolean isFood(Player player, int item, int slot) {
        FoodType food = FoodType.types.get(item);
        if (food != null) {
            eat(player, food, slot);
            return true;
        }
        return false;
    }

    /**
     * The heal option on the Health Orb
     *
     * @param player The player to heal
     */
    public static void handleHealAction(Player player) {
        if (!player.getFoodTimer().elapsed(1100))
            return;
        for (Item item : player.getInventory().getItems()) {
            if (item != null) {
                if (isFood(player, item.getId(), player.getInventory().getSlot(item.getId()))) {
                    return;
                }
            }
        }
        player.getPacketSender().sendMessage("You do not have any items that can heal you in your inventory.");
    }

    /**
     * Handles the player eating said food type.
     *
     * @param player The player eating the consumable.
     * @param food   The food type being consumed.
     * @param slot   The slot of the food being eaten.
     */
    private static void eat(Player player, FoodType food, int slot) {
        if (player.getConstitution() <= 0)
            return;
        if (!player.getControllerManager().canEat(food.heal)) {
            return;
        }

        if (player.getFoodTimer().elapsed(1100)) {
            player.getCombatBuilder().incrementAttackTimer(2).cooldown(false);
            player.getCombatBuilder().setDistanceSession(null);
            player.setCastSpell(null);
            player.getFoodTimer().reset();
            player.getPacketSender().sendInterfaceRemoval();
            player.performAnimation(new Animation(829));

            if (food != FoodType.CANDY)
                player.getInventory().delete(food.item, slot);

            int heal = food.heal;
            int prayer = 0;
            if (food == FoodType.PUMP_CANDIES) {
                int max = player.getNewSkills().getMaxLevel(S_Skills.HITPOINTS);
                if (heal + player.getNewSkills().getCurrentLevel(S_Skills.HITPOINTS) > max) {
                    heal = max - player.getNewSkills().getCurrentLevel(S_Skills.HITPOINTS);
                }
                prayer = 100;
                max = player.getNewSkills().getMaxLevel(S_Skills.PRAYER);
                if (prayer + player.getNewSkills().getCurrentLevel(S_Skills.PRAYER) > max) {
                    prayer = max - player.getNewSkills().getCurrentLevel(S_Skills.PRAYER);
                }
            } else {
                int max = player.getNewSkills().getMaxLevel(S_Skills.HITPOINTS);
                if (heal + player.getNewSkills().getCurrentLevel(S_Skills.HITPOINTS) > max) {
                    heal = max - player.getNewSkills().getCurrentLevel(S_Skills.HITPOINTS);
                }
            }
//            if (food == FoodType.PAPAYA) {
//                if (player.getRunEnergy() < 100) {
//                    player.setRunEnergy(100);
//                    player.getPacketSender().sendRunEnergy(100);
//                    player.getLastRunRecovery().reset();
//                }
//            }
            String e = Objects.equals(food.toString(), "BANDAGES") ? "use" : "eat";
            player.getPacketSender().sendMessage("You " + e + " the " + food.name + ".");
            player.setConstitution(player.getConstitution() + heal);
            player.getNewSkills().setCurrentLevel(S_Skills.PRAYER,
                    player.getNewSkills().getCurrentLevel(S_Skills.PRAYER)
                            + prayer, true);
            if (player.getConstitution() > 1190)
                player.setConstitution(1190);
            Sounds.sendSound(player, Sound.EAT_FOOD);
        }
    }

    /**
     * Represents a valid consumable item.
     *
     * @author relex lawl
     */
    private enum FoodType {
        /*
         * Fish food types players can get by fishing or purchasing from other entities.
         */
//        KING_WORM(new Item(2162), 20), TOAD_LEGS(new Item(2152), 30), BREAD(new Item(2309), 50),
//        PAPAYA(new Item(5972), 50), ROASTED_BIRD_MEAT(new Item(9980), 60), CHICKEN(new Item(2140), 50),
//        KEBAB(new Item(1971), 40), CHEESE(new Item(1985), 45), CAKE(new Item(1891), 50),
//        SECOND_CAKE_SLICE(new Item(1893), 50), THIRD_CAKE_SLICE(new Item(1895), 50), CHOCOLATE_CAKE(new Item(1897), 60),
//        SECOND_SLICE_OF_CHOCOLATE_CAKE(new Item(1899), 60), THIRD_SLICE_OF_CHOCOLATE_CAKE(new Item(1901), 60),
//        STRAWBERRY(new Item(5504), 60), SWEETCORN(new Item(7088), 100), BANDAGES(new Item(14640), 120),
//        JANGERBERRIES(new Item(247), 20), WORM_CRUNCHIES(new Item(2205), 70), EDIBLE_SEAWEED(new Item(403), 40),
//        ANCHOVIES(new Item(319), 10), SHRIMPS(new Item(315), 30), SARDINE(new Item(325), 40), COD(new Item(339), 70),
//        TROUT(new Item(333), 70), PIKE(new Item(351), 80), MACKEREL(new Item(355), 60), SALMON(new Item(329), 90),
//        TUNA(new Item(361), 100), BASS(new Item(365), 130), SWORDFISH(new Item(373), 140),
//        MEAT_PIZZA(new Item(2293), 145), MONKFISH(new Item(7946), 160), SHARK(new Item(385), 200),
//        CAVEFISH(new Item(15266), 230),
//
//        /*
//         * Baked goods food types a player can make with the cooking skill.
//         */
//        POTATO(new Item(1942), 10), BAKED_POTATO(new Item(6701), 40), POTATO_WITH_BUTTER(new Item(6703), 140),
//        CHILLI_POTATO(new Item(7054), 140), EGG_POTATO(new Item(7056), 160), POTATO_WITH_CHEESE(new Item(6705), 160),
//        MUSHROOM_POTATO(new Item(7058), 200), TUNA_POTATO(new Item(7060), 220),
//
//        /*
//         * Fruit food types which a player can get by picking from certain trees or
//         * hand-making them (such as pineapple chunks/rings).
//         */
//         BANANA(new Item(1963), 20),
//        BANANA_(new Item(18199), 20), CABBAGE(new Item(1965), 20), ORANGE(new Item(2108), 20),
//        PINEAPPLE_CHUNKS(new Item(2116), 20),
//
//        /*
//         * Dungeoneering food types, which you can get in the Dungeoneering skill
//         * dungeons.
//         */
//        HEIM_CRAB(new Item(18159), 20), RED_EYE(new Item(18161), 50), DUSK_EEL(new Item(18163), 70),
//        GIANT_FLATFISH(new Item(18165), 100), SHORT_FINNED_EEL(new Item(18167), 120), WEB_SNIPPER(new Item(18169), 150),
//        SALVE_EEL(new Item(18173), 200),

        /*
         * Other food types.
         */
        EASTER_EGG(new Item(1961), 14),
        /*
         * New food types.
         */
        SPINACH_ROLL(new Item(1969), 20), RABBIT(new Item(3228), 30), BLUE_CRAB(new Item(18175), 50),
        BOULDABASS(new Item(18171), 40), PEACH(new Item(6883), 80),  ROCKTAIL(new Item(15272), 290),
        FURYSHARK(new Item(17817), 500),  SEA_TURTLE(new Item(397), 160), MANTA_RAY(new Item(391), 220),
        LOBSTER(new Item(379), 100),

        /*
         * Event types.
         */
        PUMPKIN(new Item(1959), 260), OKTOBERTFEST_PRETZEL(new Item(19778), 820),
        BLUE_SWEETS(new Item(4558), 100), DEEP_SWEETS(new Item(4559), 160), WHITE_SWEETS(new Item(4560), 250),
        PINK_SWEETS(new Item(4564), 360), GREEN_SWEETS(new Item(4563), 470), PURPLE_SWEETS(new Item(4561), 520),
        RED_SWEETS(new Item(4562), 660), PUMP_CANDIES(new Item(1960), 660), CANDY(new Item(13557), 200),
        ;

        FoodType(Item item, int heal) {
            this.item = item;
            this.heal = heal;
            this.name = (toString().toLowerCase().replaceAll("__", "-").replaceAll("_", " "));
        }

        private final Item item;

        private final int heal;

        private final String name;

        private static final Map<Integer, FoodType> types = new HashMap<>();

        static {
            for (FoodType type : FoodType.values()) {
                types.put(type.item.getId(), type);
            }
        }
    }

    /**
     * Potions
     */

    public static boolean isPotion(int itemId) {
        String pot = ItemDefinition.forId(itemId).getName();
        return pot.contains("(4)") || pot.contains("(3)") || pot.contains("(2)") || pot.contains("(1)")
                || pot.toLowerCase().contains("infinite healing") || pot.toLowerCase().contains("infinite prayer")
                || pot.toLowerCase().contains("starter potion") || pot.toLowerCase().contains("rage potion")
                || pot.equalsIgnoreCase("wizard's mind bomb") && !pot.toLowerCase().contains("infinite prayer");
    }

    public static void handlePotion(final Player player, final int itemId, final int slot) {
        if (player.getConstitution() <= 0)
            return;
        if (!player.getControllerManager().canPot(itemId)) {
            return;
        }
        if (player.getPotionTimer().elapsed(900)) {
            switch (itemId) {

                case 21403:
                    if(player.getItems().getCharges("starter-pot") <= 0){
                        player.getPacketSender().sendMessage("You have no charges left on this potion.");
                        return;
                    }
                    player.getPacketSender().sendMessage("You have "+player.getItems().getCharges("starter-pot")+" charges left on this potion.");
                    player.getItems().useCharge("starter-pot");
                    player.heal(200);
                    break;
                //Healing potions
                case 23118: //T1
                    player.getTimers().register(new InfiniteHealing1(player));
                    break;
                case 23119: //T2
                    player.getTimers().register(new InfiniteHealing2(player));
                    break;
                case 23120: //T3
                    player.getTimers().register(new InfiniteHealing3(player));
                    break;

                //prayer
                case 23121: //T1
                    player.getTimers().register(new InfinitePrayer1(player));
                    break;
                case 23122: //T2
                    player.getTimers().register(new InfinitePrayer2(player));
                    break;
                case 23123: //T3
                    player.getTimers().register(new InfinitePrayer3(player));
                    break;
                case 15330: //T3
                    player.getTimers().register(new InfinitePrayer(player));
                    break;

                //Overload potions
                case 23124: //T1
                    player.getTimers().register(new InfiniteOverload1(player));
                    break;
                case 23125: //T2
                    player.getTimers().register(new InfiniteOverload2(player));
                    break;
                case 23126: //T3
                    player.getTimers().register(new InfiniteOverload3(player));
                    break;
                case 15328: //Rage
                    player.getTimers().register(new InfiniteRage(player));
                    break;

                /**
                 * Restore Pots
                 */

                case 127:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(129, 1);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3 || i == 5)
                            continue;
                        if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) < player.getNewSkills()
                                .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i)))) {
                            player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                    (int) (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)))
                                            + (player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))) * 0.11)), true);
                            if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) > player.getNewSkills()
                                    .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))))
                                player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                        player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))), false);
                        }
                    }
                    break;
                case 129:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(131, 1);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3 || i == 5)
                            continue;
                        if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) < player.getNewSkills()
                                .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i)))) {
                            player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                    (int) (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)))
                                            + (player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))) * 0.11)), true);
                            if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) > player.getNewSkills()
                                    .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))))
                                player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                        player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))), false);
                        }
                    }
                    break;
                case 131:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(229);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3 || i == 5)
                            continue;
                        if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) < player.getNewSkills()
                                .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i)))) {
                            player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                    (int) (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)))
                                            + (player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))) * 0.11)), true);
                            if (player.getNewSkills().getCurrentLevel(Objects.requireNonNull(S_Skills.forId(i))) > player.getNewSkills()
                                    .getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))))
                                player.getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)),
                                        player.getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))), false);
                        }
                    }
                    break;

                /**
                 * Super Restore Pots
                 */


                case 3024:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(3026, 1);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3)
                            continue;
                        S_Skills skill = S_Skills.forId(i);
                        if(skill != null) {
                            if (player.getNewSkills().getCurrentLevel(skill) < player.getNewSkills()
                                    .getMaxLevel(skill)) {
                                double restoreMod = i == 5 ? 0.29 : 0.18;
                                int toRestore = (int) (player.getNewSkills().getCurrentLevel(skill)
                                        + (player.getNewSkills().getMaxLevel(skill) * restoreMod));
                                player.getNewSkills().setCurrentLevel(skill, toRestore, true);
                                if (player.getNewSkills().getCurrentLevel(skill) > player.getNewSkills()
                                        .getMaxLevel(skill))
                                    player.getNewSkills().setCurrentLevel(skill,
                                            player.getNewSkills().getMaxLevel(skill), true);
                            }
                        }
                    }
                    break;
                case 3026:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(3028, 1);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3)
                            continue;
                        S_Skills skill = S_Skills.forId(i);
                        if(skill != null) {
                            if (player.getNewSkills().getCurrentLevel(skill) < player.getNewSkills()
                                    .getMaxLevel(skill)) {
                                double restoreMod = i == 5 ? 0.29 : 0.18;
                                int toRestore = (int) (player.getNewSkills().getCurrentLevel(skill)
                                        + (player.getNewSkills().getMaxLevel(skill) * restoreMod));
                                player.getNewSkills().setCurrentLevel(skill, toRestore, true);
                                if (player.getNewSkills().getCurrentLevel(skill) > player.getNewSkills()
                                        .getMaxLevel(skill))
                                    player.getNewSkills().setCurrentLevel(skill,
                                            player.getNewSkills().getMaxLevel(skill), true);
                            }
                        }
                    }
                    break;
                case 3028:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(3030, 1);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3)
                            continue;
                        S_Skills skill = S_Skills.forId(i);
                        if(skill != null) {
                            if (player.getNewSkills().getCurrentLevel(skill) < player.getNewSkills()
                                    .getMaxLevel(skill)) {
                                double restoreMod = i == 5 ? 0.29 : 0.18;
                                int toRestore = (int) (player.getNewSkills().getCurrentLevel(skill)
                                        + (player.getNewSkills().getMaxLevel(skill) * restoreMod));
                                player.getNewSkills().setCurrentLevel(skill, toRestore, true);
                                if (player.getNewSkills().getCurrentLevel(skill) > player.getNewSkills()
                                        .getMaxLevel(skill))
                                    player.getNewSkills().setCurrentLevel(skill,
                                            player.getNewSkills().getMaxLevel(skill), true);
                            }
                        }
                    }
                    break;
                case 3030:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(229);
                    player.getInventory().refreshItems();
                    for (int i = 0; i < 24; i++) {
                        if (i == 3)
                            continue;
                        S_Skills skill = S_Skills.forId(i);
                        if(skill != null) {
                            if (player.getNewSkills().getCurrentLevel(skill) < player.getNewSkills()
                                    .getMaxLevel(skill)) {
                                double restoreMod = i == 5 ? 0.29 : 0.18;
                                int toRestore = (int) (player.getNewSkills().getCurrentLevel(skill)
                                        + (player.getNewSkills().getMaxLevel(skill) * restoreMod));
                                player.getNewSkills().setCurrentLevel(skill, toRestore, true);
                                if (player.getNewSkills().getCurrentLevel(skill) > player.getNewSkills()
                                        .getMaxLevel(skill))
                                    player.getNewSkills().setCurrentLevel(skill,
                                            player.getNewSkills().getMaxLevel(skill), true);
                            }
                        }
                    }
                    break;

                    /**
                     * Anti Posion
                     *
                     */
                case 175:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(177, 1);
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 86);
                    player.getPacketSender().sendMessage("You're now immune to any kind of poison for another 86 seconds.");
                    break;
                case 177:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(179, 1);
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 86);
                    player.getPacketSender().sendMessage("You're now immune to any kind of poison for another 86 seconds.");
                    break;
                case 179:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(229);
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 86);
                    player.getPacketSender().sendMessage("You're now immune to any kind of poison for another 86 seconds.");
                    break;

                    /**
                     * Super Anti Posion
                     */

                case 181: // Super anti poison pot 3
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(183, 1);
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 346);
                    player.getPacketSender()
                            .sendMessage("You're now immune to any kind of poison for another 346 seconds.");
                    break;
                case 183: // Super anti poison pot 2
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(185, 1);
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 346);
                    player.getPacketSender()
                            .sendMessage("You're now immune to any kind of poison for another 346 seconds.");
                    break;
                case 185: // Super anti poison pot 1
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(229);
                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
                    player.getInventory().refreshItems();
                    PoisonImmunityTask.makeImmune(player, 346);
                    player.getPacketSender()
                            .sendMessage("You're now immune to any kind of poison for another 346 seconds.");
                    break;

                    /**
                     * Energy
                     */

                case 3016, 3020, 3018: // Super energy 4
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(itemId + 2, 1);
                    player.getInventory().refreshItems();
                    player.setRunEnergy(player.getRunEnergy() +  40);
                    if (player.getRunEnergy() > 100)
                        player.setRunEnergy(100);
                    break;
                case 3022:
                    player.performAnimation(new Animation(829));
                    player.getInventory().getItems()[slot] = new Item(229);
                    player.getInventory().refreshItems();
                    player.setRunEnergy(player.getRunEnergy() +  40);
                    if (player.getRunEnergy() > 100)
                        player.setRunEnergy(100);
                    break;


//                /*
//                 * Attack potions
//                 */
//
//                case 7218:
//                    drinkStatPotion(player, itemId, 229, slot, Skill.AGILITY.ordinal(), false);
//                    player.heal(110);
//                    player.getInventory().add(7220, 1);
//                    break;
//                case 7220:
//                    drinkStatPotion(player, itemId, 2313, slot, Skill.AGILITY.ordinal(), false);
//                    player.heal(110);
//                    break;
//                case 2428:
//                    drinkStatPotion(player, itemId, 121, slot, 0, false); // attack pot 4
//                    break;
//                case 121:
//                    drinkStatPotion(player, itemId, 123, slot, 0, false); // attack pot 3
//                    break;
//                case 123:
//                    drinkStatPotion(player, itemId, 125, slot, 0, false); // attack pot2
//                    break;
//                case 125:
//                    drinkStatPotion(player, itemId, 229, slot, 0, false); // attack pot 1
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    break;
//                /*
//                 * Defence potions
//                 */
//                case 2432:
//                    drinkStatPotion(player, itemId, 133, slot, 1, false); // Defence pot 4
//                    break;
//                case 133:
//                    drinkStatPotion(player, itemId, 135, slot, 1, false); // Defence pot 3
//                    break;
//                case 135:
//                    drinkStatPotion(player, itemId, 137, slot, 1, false); // Defence pot 2
//                    break;
//                case 137:
//                    drinkStatPotion(player, itemId, 229, slot, 1, false); // Defence pot 1
//                    break;
//                /*
//                 * Strength potions
//                 */
//                case 113:
//                    drinkStatPotion(player, itemId, 115, slot, 2, false); // Strength pot 4
//                    break;
//                case 115:
//                    drinkStatPotion(player, itemId, 117, slot, 2, false); // Strength pot 3
//                    break;
//                case 117:
//                    drinkStatPotion(player, itemId, 119, slot, 2, false); // Strength pot 2
//                    break;
//                case 119:
//                    drinkStatPotion(player, itemId, 229, slot, 2, false); // Strength pot 1
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    break;
//
//                /*
//                 * ALCHOHOL
//                 */
//                case 1917:
//                case 2017:
//                case 1993:
//                case 2015:
//                case 2021:
//                case 1915:
//                    player.getInventory().delete(itemId, 1);
//                    player.performAnimation(new Animation(829));
//                    player.getSkillManager().setCurrentLevel(Skill.STRENGTH,
//                            (player.getSkillManager().getCurrentLevel(Skill.STRENGTH) + 3 > player.getSkillManager()
//                                    .getMaxLevel(Skill.STRENGTH) ? player.getSkillManager().getMaxLevel(Skill.STRENGTH) + 3
//                                    : player.getSkillManager().getCurrentLevel(Skill.STRENGTH) + 3));
//                    player.heal(10);
//                    player.getInventory().add(1919, 1);
//                    player.getInventory().refreshItems();
//                    // player.getPacketSender().sendMessage("You finish off your beverage.");
//                    break;
//                case 1907:
//                    player.getInventory().delete(itemId, 1);
//                    player.performAnimation(new Animation(829));
//                    player.getSkillManager().setCurrentLevel(Skill.MAGIC,
//                            (player.getSkillManager().getCurrentLevel(Skill.MAGIC) + 3 > player.getSkillManager()
//                                    .getMaxLevel(Skill.MAGIC) ? player.getSkillManager().getMaxLevel(Skill.MAGIC) + 3
//                                    : player.getSkillManager().getCurrentLevel(Skill.MAGIC) + 3));
//                    player.heal(10);
//                    player.getInventory().add(1919, 1);
//                    player.getInventory().refreshItems();
//                    // player.getPacketSender().sendMessage("You finish off your beverage.");
//                    break;
//                /*
//                 * Antipoison
//                 */
//                case 2446:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(175, 1);
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 86);
//                    player.getPacketSender().sendMessage("You're now immune to any kind of poison for another 86 seconds.");
//                    break;
//                /*
//                 * Restore potion
//                 */
//                case 2430:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(127, 1);
//                    player.getInventory().refreshItems();
//                    for (int i = 0; i < 24; i++) {
//                        if (i == 3 || i == 5)
//                            continue;
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(i)) < player.getSkillManager()
//                                .getMaxLevel(i)) {
//                            player.getSkillManager().setCurrentLevel(Skill.forId((i)),
//                                    (int) (player.getSkillManager().getCurrentLevel(Skill.forId(i))
//                                            + (player.getSkillManager().getMaxLevel(Skill.forId(i)) * 0.11)));
//                            if (player.getSkillManager().getCurrentLevel(Skill.forId(i)) > player.getSkillManager()
//                                    .getMaxLevel(Skill.forId(i)))
//                                player.getSkillManager().setCurrentLevel(Skill.forId(i),
//                                        player.getSkillManager().getMaxLevel(Skill.forId(i)));
//                        }
//                    }
//                    break;
//                /*
//                 * Antifire potions
//                 */
//                case 2452: // Antifire pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(2454, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 50);
//                    player.getPacketSender()
//                            .sendMessage("You're now 66% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 2454: // Antifire pot 3
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(2456, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 50);
//                    player.getPacketSender()
//                            .sendMessage("You're now 66% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 2456: // Antifire pot 2
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(2458, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 50);
//                    player.getPacketSender()
//                            .sendMessage("You're now 66% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 2458: // Antifire pot 1
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 50);
//                    player.getPacketSender()
//                            .sendMessage("You're now 66% immune to any kind of fire for another 6 minutes.");
//                    break;
//                /*
//                 * Super antifire potions
//                 */
//                case 15304: // Super Antifire pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15305, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 100);
//                    player.getPacketSender()
//                            .sendMessage("You're now 100% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 15305: // Super Antifire pot 3
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15306, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 100);
//                    player.getPacketSender()
//                            .sendMessage("You're now 100% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 15306: // Super Antifire pot 2
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15307, 1);
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 100);
//                    player.getPacketSender()
//                            .sendMessage("You're now 100% immune to any kind of fire for another 6 minutes.");
//                    break;
//                case 15307: // Super Antifire pot 1
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    FireImmunityTask.makeImmune(player, 360, 100);
//                    player.getPacketSender()
//                            .sendMessage("You're now 100% immune to any kind of fire for another 6 minutes.");
//                    break;
//                /*
//                 * Restore energy
//                 */
//                /*
//                 * Combat potions
//                 */
//                case 9739:
//                    drinkStatPotion(player, itemId, 9741, slot, 0, false);
//                    drinkStatPotion(player, itemId, 9741, slot, 2, false);
//                    break;
//                case 9741:
//                    drinkStatPotion(player, itemId, 9743, slot, 0, false);
//                    drinkStatPotion(player, itemId, 9743, slot, 2, false);
//                    break;
//                case 9743:
//                    drinkStatPotion(player, itemId, 9745, slot, 0, false);
//                    drinkStatPotion(player, itemId, 9745, slot, 2, false);
//                    break;
//                case 9745:
//                    drinkStatPotion(player, itemId, 229, slot, 0, false);
//                    drinkStatPotion(player, itemId, 229, slot, 2, false);
//                    break;
//                /*
//                 * Agility potions
//                 */
//                case 3032:
//                    drinkStatPotion(player, itemId, 3034, slot, 16, false); // Agility pot 4
//                    break;
//                case 3034:
//                    drinkStatPotion(player, itemId, 3036, slot, 16, false); // Agility pot 3
//                    break;
//                case 3036:
//                    drinkStatPotion(player, itemId, 3038, slot, 16, false); // Agility pot 2
//                    break;
//                case 3038:
//                    drinkStatPotion(player, itemId, 229, slot, 16, false);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Agility pot 1
//                    break;
//                /*
//                 * Prayer potions
//                 */
//                case 2434: // Prayer pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(139, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER)
//                                    + (player.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.33)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
//                            .getMaxLevel(Skill.PRAYER))
//                        player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                                player.getSkillManager().getMaxLevel(Skill.PRAYER));
//                    break;
//                case 139: // Prayer pot 3
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(141, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER)
//                                    + (player.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.33)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
//                            .getMaxLevel(Skill.PRAYER))
//                        player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                                player.getSkillManager().getMaxLevel(Skill.PRAYER));
//                    break;
//                case 141: // Prayer pot 2
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(143, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER)
//                                    + (player.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.33)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
//                            .getMaxLevel(Skill.PRAYER))
//                        player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                                player.getSkillManager().getMaxLevel(Skill.PRAYER));
//                    break;
//                case 143: // Prayer pot 1
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER)
//                                    + (player.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.33)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
//                            .getMaxLevel(Skill.PRAYER))
//                        player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//                                player.getSkillManager().getMaxLevel(Skill.PRAYER));
//                    break;
//                /*
//                 * Summoning potions
//                 */
//                case 12140: // Summoning pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(12142, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.SUMMONING)
//                                    + (player.getSkillManager().getMaxLevel(Skill.SUMMONING) * 0.25)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) > player.getSkillManager()
//                            .getMaxLevel(Skill.SUMMONING))
//                        player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                                player.getSkillManager().getMaxLevel(Skill.SUMMONING));
//                    break;
//                case 12142: // Summoning pot 3
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(12144, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.SUMMONING)
//                                    + (player.getSkillManager().getMaxLevel(Skill.SUMMONING) * 0.25)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) > player.getSkillManager()
//                            .getMaxLevel(Skill.SUMMONING))
//                        player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                                player.getSkillManager().getMaxLevel(Skill.SUMMONING));
//                    break;
//                case 12144: // Summoning pot 2
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(12146, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.SUMMONING)
//                                    + (player.getSkillManager().getMaxLevel(Skill.SUMMONING) * 0.25)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) > player.getSkillManager()
//                            .getMaxLevel(Skill.SUMMONING))
//                        player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                                player.getSkillManager().getMaxLevel(Skill.SUMMONING));
//                    break;
//                case 12146: // Summoning pot 1
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                            (int) (player.getSkillManager().getCurrentLevel(Skill.SUMMONING)
//                                    + (player.getSkillManager().getMaxLevel(Skill.SUMMONING) * 0.25)));
//                    if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) > player.getSkillManager()
//                            .getMaxLevel(Skill.SUMMONING))
//                        player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                                player.getSkillManager().getMaxLevel(Skill.SUMMONING));
//                    break;
//                /*
//                 * Crafting potions
//                 */
//                case 14838:
//                    drinkStatPotion(player, itemId, 14840, slot, 12, false); // Crafting pot 4
//                    break;
//                case 14840:
//                    drinkStatPotion(player, itemId, 14842, slot, 12, false); // Crafting pot 3
//                    break;
//                case 14842:
//                    drinkStatPotion(player, itemId, 14844, slot, 12, false); // Crafting pot 2
//                    break;
//                case 14844:
//                    drinkStatPotion(player, itemId, 229, slot, 12, false);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Crafting pot 1
//                    break;
//                /*
//                 * Super Attack potions
//                 */
//                case 2436:
//                    drinkStatPotion(player, itemId, 145, slot, 0, true); // Super Attack pot 4
//                    break;
//                case 145:
//                    drinkStatPotion(player, itemId, 147, slot, 0, true); // Super Attack pot 3
//                    break;
//                case 147:
//                    drinkStatPotion(player, itemId, 149, slot, 0, true); // Super Attack pot 2
//                    break;
//                case 149:
//                    drinkStatPotion(player, itemId, 229, slot, 0, true);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Super Attack pot 1
//                    break;
//                /*
//                 * Super Anti poison potions
//                 */
//                case 2448: // Super anti poison pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(181, 1);
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 346);
//                    player.getPacketSender()
//                            .sendMessage("You're now immune to any kind of poison for another 346 seconds.");
//                    break;
//                /*
//                 * Fishing potions
//                 */
//                case 2438:
//                    drinkStatPotion(player, itemId, 151, slot, 10, false); // Fishing pot 4
//                    break;
//                case 151:
//                    drinkStatPotion(player, itemId, 153, slot, 10, false); // Fishing pot 3
//                    break;
//                case 153:
//                    drinkStatPotion(player, itemId, 155, slot, 10, false); // Fishing pot 2
//                    break;
//                case 155:
//                    drinkStatPotion(player, itemId, 229, slot, 10, false);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Fishing pot 1
//                    break;
//                /*
//                 * Hunter potions
//                 */
//                /*
//                 * Super Strength potions
//                 */
//                case 2440:
//                    drinkStatPotion(player, itemId, 157, slot, 2, true); // Super Strength pot 4
//                    break;
//                case 157:
//                    drinkStatPotion(player, itemId, 159, slot, 2, true); // Super Strength pot 3
//                    break;
//                case 159:
//                    drinkStatPotion(player, itemId, 161, slot, 2, true); // Super Strength pot 2
//                    break;
//                case 161:
//                    drinkStatPotion(player, itemId, 229, slot, 2, true);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Super Strength pot 1
//                    break;
//                /*
//                 * Fletching potions
//                 */
//                case 14846:
//                    drinkStatPotion(player, itemId, 14848, slot, 9, false); // Fletching pot 4
//                    break;
//                case 14848:
//                    drinkStatPotion(player, itemId, 14850, slot, 9, false); // Fletching pot 3
//                    break;
//                case 14850:
//                    drinkStatPotion(player, itemId, 14852, slot, 9, false); // Fletching pot 2
//                    break;
//                case 14852:
//                    drinkStatPotion(player, itemId, 229, slot, 9, false);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");// Fletching pot 1
//                    break;
//                /*
//                 * Super Restore potions
//                 */
//                /*
//                 * Super Defence potions
//                 */
//                case 2442:
//                    drinkStatPotion(player, itemId, 163, slot, 1, true);
//                    break;
//                case 163:
//                    drinkStatPotion(player, itemId, 165, slot, 1, true);
//                    break;
//                case 165:
//                    drinkStatPotion(player, itemId, 167, slot, 1, true);
//                    break;
//                case 167:
//                    drinkStatPotion(player, itemId, 229, slot, 1, true);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    break;
//                /*
//                 * Antipoison+ potions
//                 */
//                case 5943: // Antipoison+ pot 4
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(5945, 1);
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 518);
//                    player.getPacketSender()
//                            .sendMessage("You're now immune to any kind of poison for another 518 seconds.");
//                    break;
//                case 5945: // Antipoison+ pot 3
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(5947, 1);
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 518);
//                    player.getPacketSender()
//                            .sendMessage("You're now immune to any kind of poison for another 518 seconds.");
//                    break;
//                case 5947: // Antipoison+ pot 2
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(5949, 1);
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 518);
//                    player.getPacketSender()
//                            .sendMessage("You're now immune to any kind of poison for another 518 seconds.");
//                    break;
//                case 5949: // Antipoison+ pot 1
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    PoisonImmunityTask.makeImmune(player, 518);
//                    player.getPacketSender()
//                            .sendMessage("You're now immune to any kind of poison for another 518 seconds.");
//                    break;
//                /*
//                 * Ranging potions
//                 */
//                case 2444:
//                    drinkStatPotion(player, itemId, 169, slot, 4, false); // Ranging pot 4
//                    break;
//                case 169:
//                    drinkStatPotion(player, itemId, 171, slot, 4, false); // Ranging pot 3
//                    break;
//                case 171:
//                    drinkStatPotion(player, itemId, 173, slot, 4, false); // Ranging pot 2
//                    break;
//                case 173:
//                    drinkStatPotion(player, itemId, 229, slot, 4, false); // Ranging pot 1
//                    break;
//                /*
//                 * Magic potions
//                 */
//                case 3040:
//                    drinkStatPotion(player, itemId, 3042, slot, 6, false); // Magic pot 4
//                    break;
//                case 3042:
//                    drinkStatPotion(player, itemId, 3044, slot, 6, false); // Magic pot 3
//                    break;
//                case 3044:
//                    drinkStatPotion(player, itemId, 3046, slot, 6, false); // Magic pot 2
//                    break;
//                case 3046:
//                    drinkStatPotion(player, itemId, 229, slot, 6, false); // Magic pot 1
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    break;
//                /*
//                 * Zamorak brews
//                 */
//                case 2450:
//                    if (player.getConstitution() < 300) {
//                        player.getPacketSender().sendMessage("Your Constitution is too low for this potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(189, 1);
//                    player.getInventory().refreshItems();
//                    int[] toDecrease = {1, 3};
//                    for (int tD : toDecrease) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), tD == 1 ? 1 : 100);
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) - getBrewStat(player, 0, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(0)) > (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2));
//                    }
//                    if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) <= 0)
//                        player.getSkillManager().setCurrentLevel(Skill.ATTACK, 1);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getBrewStat(player, 2, .12));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(2)) > (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(5)) + getBrewStat(player, 5, .10));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(5)) > (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2));
//                    }
//                    break;
//                case 189:
//                    if (player.getConstitution() < 300) {
//                        player.getPacketSender().sendMessage("Your Constitution is too low for this potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(191, 1);
//                    player.getInventory().refreshItems();
//                    int[] toDecrease1 = {1, 3};
//                    for (int tD : toDecrease1) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), tD == 1 ? 1 : 100);
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) - getBrewStat(player, 0, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(0)) > (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2));
//                    }
//                    if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) <= 0)
//                        player.getSkillManager().setCurrentLevel(Skill.ATTACK, 1);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getBrewStat(player, 2, .12));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(2)) > (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(5)) + getBrewStat(player, 5, .10));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(5)) > (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2));
//                    }
//                    break;
//                case 191:
//                    if (player.getConstitution() < 300) {
//                        player.getPacketSender().sendMessage("Your Constitution is too low for this potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(193, 1);
//                    player.getInventory().refreshItems();
//                    int[] toDecrease11 = {1, 3};
//                    for (int tD : toDecrease11) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), tD == 1 ? 1 : 100);
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) - getBrewStat(player, 0, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(0)) > (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2));
//                    }
//                    if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) <= 0)
//                        player.getSkillManager().setCurrentLevel(Skill.ATTACK, 1);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getBrewStat(player, 2, .12));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(2)) > (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(5)) + getBrewStat(player, 5, .10));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(5)) > (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2));
//                    }
//                    break;
//                case 193:
//                    if (player.getConstitution() < 300) {
//                        player.getPacketSender().sendMessage("Your Constitution is too low for this potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().delete(229,1);//player.sendMessage("Remove The Pot NOW!");
//                    player.getInventory().refreshItems();
//                    int[] toDecrease111 = {1, 3};
//                    for (int tD : toDecrease111) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), tD == 1 ? 1 : 100);
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) - getBrewStat(player, 0, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(0)) > (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(0)) * 1.2));
//                    }
//                    if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) <= 0)
//                        player.getSkillManager().setCurrentLevel(Skill.ATTACK, 1);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getBrewStat(player, 2, .12));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(2)) > (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(2)) * 1.2));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(5)) + getBrewStat(player, 5, .10));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(5)) > (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(5),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(5)) * 1.2));
//                    }
//                    break;
//                /*
//                 * Saradomin brews
//                 */
//                case 6685:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(6687, 1);
//                    player.getInventory().refreshItems();
//                    int[] decrease = {0, 2, 4, 6};
//                    for (int tD : decrease) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), 1);
//                        player.getSkillManager().updateSkill(Skill.forId(tD));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getBrewStat(player, 1, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(1)) > (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2));
//                    }
//                    player.getSkillManager().updateSkill(Skill.forId(1));
//                    double amount = player.getEquipment().wearingNexAmours() ? 1.22 : 1.17;
//                    int bonus = player.getEquipment().wearingNexAmours() ? getBrewStat(player, 3, .21)
//                            : getBrewStat(player, 3, .15);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(3)) + bonus);
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(3)) > (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount));
//                    }
//                    break;
//                case 6687:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(6689, 1);
//                    player.getInventory().refreshItems();
//                    decrease = new int[]{0, 2, 4, 6};
//                    for (int tD : decrease) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), 1);
//                        player.getSkillManager().updateSkill(Skill.forId(tD));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getBrewStat(player, 1, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(1)) > (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2));
//                    }
//                    player.getSkillManager().updateSkill(Skill.forId(1));
//                    amount = player.getEquipment().wearingNexAmours() ? 1.22 : 1.17;
//                    bonus = player.getEquipment().wearingNexAmours() ? getBrewStat(player, 3, .21)
//                            : getBrewStat(player, 3, .15);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(3)) + bonus);
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(3)) > (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount));
//                    }
//                    break;
//                case 6689:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(6691, 1);
//                    player.getInventory().refreshItems();
//                    decrease = new int[]{0, 2, 4, 6};
//                    for (int tD : decrease) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), 1);
//                        player.getSkillManager().updateSkill(Skill.forId(tD));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getBrewStat(player, 1, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(1)) > (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2));
//                    }
//                    player.getSkillManager().updateSkill(Skill.forId(1));
//                    amount = player.getEquipment().wearingNexAmours() ? 1.22 : 1.17;
//                    bonus = player.getEquipment().wearingNexAmours() ? getBrewStat(player, 3, .21)
//                            : getBrewStat(player, 3, .15);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(3)) + bonus);
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(3)) > (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount));
//                    }
//                    break;
//                case 6691:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    decrease = new int[]{0, 2, 4, 6};
//                    for (int tD : decrease) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(tD),
//                                player.getSkillManager().getCurrentLevel(Skill.forId(tD)) - getBrewStat(player, tD, .10));
//                        if (player.getSkillManager().getCurrentLevel(Skill.forId(tD)) < 0)
//                            player.getSkillManager().setCurrentLevel(Skill.forId(tD), 1);
//                        player.getSkillManager().updateSkill(Skill.forId(tD));
//                    }
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getBrewStat(player, 1, .20));
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(1)) > (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2 + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(1)) * 1.2));
//                    }
//                    player.getSkillManager().updateSkill(Skill.forId(1));
//                    amount = player.getEquipment().wearingNexAmours() ? 1.22 : 1.17;
//                    bonus = player.getEquipment().wearingNexAmours() ? getBrewStat(player, 3, .21)
//                            : getBrewStat(player, 3, .15);
//                    player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(3)) + bonus);
//                    if (player.getSkillManager().getCurrentLevel(
//                            Skill.forId(3)) > (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount + 1)) {
//                        player.getSkillManager().setCurrentLevel(Skill.forId(3),
//                                (int) (player.getSkillManager().getMaxLevel(Skill.forId(3)) * amount));
//                    }
//                    break;
                /*
                 * Restore potions
                 */
                /*
                 * case 15300: if(player.getLocation() != null && player.getLocation() ==
                 * Location.WILDERNESS) { player.getPacketSender().
                 * sendMessage("This potion cannot be used in the Wilderness."); return; }
                 * if(!player.getSpecialRestoreTimer().elapsed(GameSettings.
                 * Spec_Restore_Cooldown)) { player.getPacketSender().
                 * sendMessage("This potion can only be used once every "+GameSettings.
                 * Spec_Restore_Cooldown/1000/60+" minutes."); return; }
                 * player.performAnimation(new Animation(829));
                 * player.getInventory().getItems()[slot] = new Item(15301, 1);
                 * player.getInventory().refreshItems();
                 * player.setSpecialPercentage(player.getSpecialPercentage() + 25);
                 * if(player.getSpecialPercentage() > 100) player.setSpecialPercentage(100);
                 * CombatSpecial.updateBar(player); player.getSpecialRestoreTimer().reset();
                 * break; case 15301: if(player.getLocation() != null && player.getLocation() ==
                 * Location.WILDERNESS) { player.getPacketSender().
                 * sendMessage("This potion cannot be used in the Wilderness."); return; }
                 * if(!player.getSpecialRestoreTimer().elapsed(GameSettings.
                 * Spec_Restore_Cooldown)) { player.getPacketSender().
                 * sendMessage("This potion can only be used once every "+GameSettings.
                 * Spec_Restore_Cooldown/1000/60+" minutes."); return; }
                 * player.performAnimation(new Animation(829));
                 * player.getInventory().getItems()[slot] = new Item(15302, 1);
                 * player.getInventory().refreshItems();
                 * player.setSpecialPercentage(player.getSpecialPercentage() + 25);
                 * if(player.getSpecialPercentage() > 1000) player.setSpecialPercentage(1000);
                 * CombatSpecial.updateBar(player); player.getSpecialRestoreTimer().reset();
                 * break; case 15302: if(player.getLocation() != null && player.getLocation() ==
                 * Location.WILDERNESS) { player.getPacketSender().
                 * sendMessage("This potion cannot be used in the Wilderness."); return; }
                 * if(!player.getSpecialRestoreTimer().elapsed(GameSettings.
                 * Spec_Restore_Cooldown)) { player.getPacketSender().
                 * sendMessage("This potion can only be used once every "+GameSettings.
                 * Spec_Restore_Cooldown/1000/60+" minutes."); return; }
                 * player.performAnimation(new Animation(829));
                 * player.getInventory().getItems()[slot] = new Item(15303, 1);
                 * player.getInventory().refreshItems();
                 * player.setSpecialPercentage(player.getSpecialPercentage() + 25);
                 * if(player.getSpecialPercentage() > 100) player.setSpecialPercentage(100);
                 * CombatSpecial.updateBar(player); player.getSpecialRestoreTimer().reset();
                 * break; case 15303: if(player.getLocation() != null && player.getLocation() ==
                 * Location.WILDERNESS) { player.getPacketSender().
                 * sendMessage("This potion cannot be used in the Wilderness."); return; }
                 * if(!player.getSpecialRestoreTimer().elapsed(GameSettings.
                 * Spec_Restore_Cooldown)) { player.getPacketSender().
                 * sendMessage("This potion can only be used once every "+GameSettings.
                 * Spec_Restore_Cooldown/1000/60+" minutes."); return; }
                 * player.performAnimation(new Animation(829));
                 * player.getInventory().getItems()[slot] = new Item(229);
                 * player.getInventory().refreshItems();
                 * player.setSpecialPercentage(player.getSpecialPercentage() + 25);
                 * if(player.getSpecialPercentage() > 10.00) player.setSpecialPercentage(100);
                 * CombatSpecial.updateBar(player); player.getSpecialRestoreTimer().reset();
                 * break;
                 */
                /*
                 * Extreme Attack potions
                 */
//                case 15329: //disabled -- super prayer pot
////                    if (!drinkSuperOverload(player, slot, 229))
////
////                        return;
////                    player.getPacketSender().sendInterfaceRemoval();
////                    player.getCombatBuilder().incrementAttackTimer(1).cooldown(false);
////                    player.getCombatBuilder().setDistanceSession(null);
////                    player.setCastSpell(null);
////                    player.getFoodTimer().reset();
////                    player.getPotionTimer().reset();
////                    String potion3 = ItemDefinition.forId(itemId).getName();
////                    if (potion3.contains("ummer pi")) {
////                        player.getPacketSender().sendMessage("You eat your " + potion3 + ".");
////                    } else {
////                        player.getPacketSender().sendMessage("You drink some of your " + potion3 + ".");
////                    }
////                    if (potion3.endsWith("(4)")) {
////                        player.getPacketSender().sendMessage("You have 3 doses of potion left.");
////                    } else if (potion3.endsWith("(3)")) {
////                        player.getPacketSender().sendMessage("You have 2 doses of potion left.");
////                    } else if (potion3.endsWith("(2)")) {
////                        player.getPacketSender().sendMessage("You have 1 dose of potion left.");
////                    } else if (potion3.endsWith("(1)")) {
////                        player.getPacketSender().sendMessage("You have finished your potion.");
////                    }
////                    if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
////                        Consumables.overloadIncrease(player, Skill.ATTACK, 0.38);
////                        Consumables.overloadIncrease(player, Skill.STRENGTH, 0.38);
////                        Consumables.overloadIncrease(player, Skill.DEFENCE, 0.38);
////                        Consumables.overloadIncrease(player, Skill.RANGED, 0.38);
////                        Consumables.overloadIncrease(player, Skill.MAGIC, 0.38);
////                    }
////                    Sounds.sendSound(player, Sound.DRINK_POTION);
//                    break;
//                case 15308:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15309, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Objects.requireNonNull(Skill.forId(0))) + getExtremePotionBoost(player, 0));
//                    break;
//                case 15309:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15310, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) + getExtremePotionBoost(player, 0));
//                    break;
//                case 15310:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15311, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) + getExtremePotionBoost(player, 0));
//                    break;
//                case 15311:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(0),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(0)) + getExtremePotionBoost(player, 0));
//                    break;
//                /*
//                 * Extreme Strength potion
//                 */
//                case 15312:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15313, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getExtremePotionBoost(player, 2));
//                    break;
//                case 15313:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15314, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getExtremePotionBoost(player, 2));
//                    break;
//                case 15314:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15315, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getExtremePotionBoost(player, 2));
//                    break;
//                case 15315:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(2),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(2)) + getExtremePotionBoost(player, 2));
//                    break;
//                /*
//                 * Extreme Defence potions
//                 */
//                case 15316:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15317, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getExtremePotionBoost(player, 1));
//                    break;
//                case 15317:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15318, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getExtremePotionBoost(player, 1));
//                    break;
//                case 15318:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15319, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getExtremePotionBoost(player, 1));
//                    break;
//                case 15319:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(1),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(1)) + getExtremePotionBoost(player, 1));
//                    break;
//                /*
//                 * Extreme Magic potions
//                 */
//                case 15320:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15321, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(6),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(6)) + getExtremePotionBoost(player, 6));
//                    break;
//                case 15321:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15322, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(6),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(6)) + getExtremePotionBoost(player, 6));
//                    break;
//                case 15322:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15323, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(6),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(6)) + getExtremePotionBoost(player, 6));
//                    break;
//                case 15323:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(6),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(6)) + getExtremePotionBoost(player, 6));
//                    break;
//                /*
//                 * Extreme Ranging potions
//                 */
//                case 15324:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15325, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(4),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(4)) + getExtremePotionBoost(player, 4));
//                    break;
//                case 15325:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15326, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(4),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(4)) + getExtremePotionBoost(player, 4));
//                    break;
//                case 15326:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15327, 1);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(4),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(4)) + getExtremePotionBoost(player, 4));
//                    break;
//                case 15327:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.getSkillManager().setCurrentLevel(Skill.forId(4),
//                            player.getSkillManager().getCurrentLevel(Skill.forId(4)) + getExtremePotionBoost(player, 4));
//                    break;
//                /*
//                 * Prayer Renewal potion
//                 */
//                case 21630:
//                    if (player.getPrayerRenewalPotionTimer() > 0) {
//                        player.getPacketSender().sendMessage("You already have the effect of a Prayer Renewal potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(21632, 1);
//                    player.getInventory().refreshItems();
//                    player.setPrayerRenewalPotionTimer(600);
//                    TaskManager.submit(new PrayerRenewalPotionTask(player));
//                    break;
//                case 21632:
//                    if (player.getPrayerRenewalPotionTimer() > 0) {
//                        player.getPacketSender().sendMessage("You already have the effect of a Prayer Renewal potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(21634, 1);
//                    player.getInventory().refreshItems();
//                    player.setPrayerRenewalPotionTimer(600);
//                    TaskManager.submit(new PrayerRenewalPotionTask(player));
//                    break;
//                case 21634:
//                    if (player.getPrayerRenewalPotionTimer() > 0) {
//                        player.getPacketSender().sendMessage("You already have the effect of a Prayer Renewal potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(21636, 1);
//                    player.getInventory().refreshItems();
//                    player.setPrayerRenewalPotionTimer(600);
//                    TaskManager.submit(new PrayerRenewalPotionTask(player));
//                    break;
//                case 21636:
//                    if (player.getPrayerRenewalPotionTimer() > 0) {
//                        player.getPacketSender().sendMessage("You already have the effect of a Prayer Renewal potion.");
//                        return;
//                    }
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.setPrayerRenewalPotionTimer(600);
//                    TaskManager.submit(new PrayerRenewalPotionTask(player));
//                    break;
//                case 15332:
//                    if (!drinkOverload(player, slot, 15333))
//                        return;
//                    break;
//                // restore prayer
//                case 15300:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15301);
//                    player.getInventory().refreshItems();
//                    player.setSpecialPercentage(100);
//                    CombatSpecial.updateBar(player);
//                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
//                    break;
//                case 15301:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15302);
//                    player.getInventory().refreshItems();
//                    player.setSpecialPercentage(100);
//                    CombatSpecial.updateBar(player);
//                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
//                    break;
//                case 15302:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(15303);
//                    player.getInventory().refreshItems();
//                    player.setSpecialPercentage(100);
//                    CombatSpecial.updateBar(player);
//                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
//                    break;
//                case 15303:
//                    player.performAnimation(new Animation(829));
//                    player.getInventory().getItems()[slot] = new Item(229);
//                    player.getInventory().refreshItems();
//                    player.setSpecialPercentage(100);
//                    CombatSpecial.updateBar(player);
//                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
//                    break;
//                case 15333:
//                    if (!drinkOverload(player, slot, 15334))
//                        return;
//                    break;
//
//                case 15334:
//                    if (!drinkOverload(player, slot, 15335))
//                        return;
//                    break;
//
//                case 15335:
//                    if (!drinkOverload(player, slot, 229))
//                        return;
//                    break;
            }


           // System.out.println("POTION 2");
            player.getPacketSender().sendInterfaceRemoval();
            player.getCombatBuilder().incrementAttackTimer(1).cooldown(false);
            player.getCombatBuilder().setDistanceSession(null);
            player.setCastSpell(null);
            player.getFoodTimer().reset();
            player.getPotionTimer().reset();
            String potion = ItemDefinition.forId(itemId).getName();
            player.getPacketSender().sendMessage("You drink some of your " + potion + ".");

            if (potion.endsWith("(4)")) {
                player.getPacketSender().sendMessage("You have 3 doses of potion left.");
            } else if (potion.endsWith("(3)")) {
                player.getPacketSender().sendMessage("You have 2 doses of potion left.");
            } else if (potion.endsWith("(2)")) {
                player.getPacketSender().sendMessage("You have 1 dose of potion left.");
            } else if (potion.endsWith("(1)")) {
                player.getPacketSender().sendMessage("You have finished your potion.");
            }
//            if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
//                Consumables.overloadIncrease(player, Skill.ATTACK, 0.19);
//                Consumables.overloadIncrease(player, Skill.STRENGTH, 0.19);
//                Consumables.overloadIncrease(player, Skill.DEFENCE, 0.19);
//                Consumables.overloadIncrease(player, Skill.RANGED, 0.19);
//                Consumables.overloadIncrease(player, Skill.MAGIC, 0.19);
//            }
            Sounds.sendSound(player, Sound.DRINK_POTION);
        }
    }

//    public static void drinkStatPotion(final Player player, int potion, int replacePotion, int slot, int skill,
//                                       boolean super_pot) {
//        if (slot >= 0) {
//            player.performAnimation(new Animation(829));
//            player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
//        }
//        player.getInventory().refreshItems();
//        boolean cbPot = potion == 9739 || potion == 9741 || potion == 9743 || potion == 9745;
//        Skill sk = Skill.forId(skill);
//        player.getSkillManager().setCurrentLevel(sk,
//                player.getSkillManager().getCurrentLevel(sk) + getBoostedStat(player, skill, super_pot, cbPot), true);
//    }

//    public static boolean drinkOverload(final Player player, int slot, int replacePotion) {
//        if ( player.getLocation() == Location.DUEL_ARENA) {
//            player.getPacketSender().sendMessage("You cannot use this potion here.");
//            return false;
//        }
//        if (player.getOverloadPotionTimer() > 0) {
//            player.getPacketSender().sendMessage("You already have the effect of an Overload potion.");
//            return false;
//        }
//        if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) <= 500) {
//            player.getPacketSender().sendMessage("You need more than 500 Hitpoints to drink this potion.");
//            return false;
//        }
//        player.performAnimation(new Animation(829));
//        player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
//        player.getInventory().refreshItems();
//        player.setOverloadPotionTimer(300);
//        TaskManager.submit(new OverloadPotionTask(player));
//        return true;
//    }
//
//    public static boolean drinkSuperOverload(final Player player, int slot, int replacePotion) {
//        if ( player.getLocation() == Location.DUEL_ARENA) {
//            player.getPacketSender().sendMessage("You cannot use this potion here.");
//            return false;
//        }
//        if (player.getOverloadPotionTimer() > 0) {
//            player.getPacketSender().sendMessage("You already have the effect of an Overload or Super Overload potion.");
//            return false;
//        }
//        if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) <= 500) {
//            player.getPacketSender().sendMessage("You need more than 500 Hitpoints to drink this potion.");
//            return false;
//        }
//        player.performAnimation(new Animation(829));
//        player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
//        player.getInventory().refreshItems();
//        player.setOverloadPotionTimer(600);
//        TaskManager.submit(new SuperOverloadPotionTask(player));
//        return true;
//    }
//
//    public static boolean drinkRestore(final Player player, int slot, int replacePotion) {
//        if ( player.getLocation() == Location.DUEL_ARENA) {
//            player.getPacketSender().sendMessage("You cannot use this potion here.");
//            return false;
//        }
//        player.performAnimation(new Animation(829));
//        player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
//        player.getInventory().refreshItems();
//        player.setSpecialPercentage(100);
//        CombatSpecial.updateBar(player);
//        player.getPacketSender().sendMessage("Your special attack energy has been restored.");
//        // player.setOverloadPotionTimer(600);
//        // TaskManager.submit(new OverloadPotionTask(player));
//        return true;
//    }
//
//    public static int getBoostedStat(Player player, int skillId, boolean sup, boolean combatPotion) {
//        Skill skill = Skill.forId(skillId);
//        int increaseBy = 0;
//        if (sup)
//            increaseBy = (int) ((double) player.getSkillManager().getMaxLevel(skill) * 0.2D);
//        else
//            increaseBy = (int) ((double) player.getSkillManager().getMaxLevel(skill) * (combatPotion ? 0.10D : 0.13D))
//                    + 1;
//        if (player.getSkillManager().getCurrentLevel(skill) + increaseBy > player.getSkillManager().getMaxLevel(skill)
//                + increaseBy + 1) {
//            return player.getSkillManager().getMaxLevel(skill) + increaseBy
//                    - player.getSkillManager().getCurrentLevel(skill);
//        }
//        return increaseBy;
//    }

    public static void overloadIncrease(final Player player, Skill skill, double l) {
//        player.getSkillManager().setCurrentLevel(skill,
//                (int) (SkillManager.getLevelForExperience(player.getSkillManager().getExperience(skill))
//                        + (SkillManager.getLevelForExperience(player.getSkillManager().getExperience(skill)) * l)),
//                true);
    }

    public static void levelIncrease(final Player player, Skill skill, int level) {
       // player.getSkillManager().setCurrentLevel(skill, level, true);
    }

//    public static int getExtremePotionBoost(Player player, int skill) {
//        int increaseBy = 0;
//        increaseBy = (int) (SkillManager
//                .getLevelForExperience(player.getSkillManager().getExperience(Skill.forId(skill))) * .16) + 1;
//        if (player.getSkillManager().getCurrentLevel(Skill.forId(skill)) + increaseBy > SkillManager
//                .getLevelForExperience(player.getSkillManager().getExperience(Skill.forId(skill))) + increaseBy + 1) {
//            return SkillManager.getLevelForExperience(player.getSkillManager().getExperience(Skill.forId(skill)))
//                    + increaseBy - player.getSkillManager().getCurrentLevel(Skill.forId(skill));
//        }
//        return increaseBy;
//    }
//
//    public static int getBrewStat(final Player player, int skill, double amount) {
//        return (int) (player.getSkillManager().getMaxLevel(Objects.requireNonNull(Skill.forId(skill))) * amount);
//    }
}
