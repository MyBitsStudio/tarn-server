package com.ruse.world.content.fuser;

import com.ruse.model.Locations;
import com.ruse.model.PlayerRights;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

public class RandomRewards {

    /**
     * FINAL INTS FOR USE SERVER-WIDE
     **/

    public static final int blueStone = 12845;
    public static final int redStone = 12846;
    public static final int blackStone = 12847;
    public static final int otherSHit = 1234;
    public static final int moreShit = 1234;


    /**
     * okay first off we need to find a way to filter the rewards
     * based on the type of skill that the player is doing
     * because you said you wanted only certain skills to reward right? yeah black= fish red =wc blue=mining
     */
    public static void giveReward(Skill skill, Player player) {

        if (player.getLocation().equals(Locations.Location.AFK)) {
            return;
        }

        int chance = Misc.random(100);
        int stoneID;
        int amountToGive = 1;
        int randomMultiplier = Misc.random(5); // Maximum they can receive
        boolean doubleReward = false;

        switch (skill) {
            case FISHING:
                stoneID = blackStone;
                break;
            case WOODCUTTING:
                stoneID = redStone;
                break;
            case MINING:
                stoneID = blueStone;
                break;
            default:
                return; // Er I think this should stop the rest of the code running if not using one of the listed skills
        }

        amountToGive *= randomMultiplier;

        if (amountToGive == 0) { // If they are unlucky enough to have rolled 0 / 5 on the randomMultiplier
            return;
        }

        PlayerRights rights = player.getRights();

        switch (rights) {
            case GRACEFUL_DONATOR:
                chance += 1; // Extra 1% Chance to win
                if (Misc.random(60) == 1) {// 1 in 60 chance
                    doubleReward = true;
                }
                break;
            case CLERIC_DONATOR:
                chance += 2; // Extra 2% Chance to win
                if (Misc.random(50) == 1) {// 1 in 50 chance
                    doubleReward = true;
                }
                break;
            case TORMENTED_DONATOR:
                chance += 3; // Extra 3% Chance to win
                if (Misc.random(40) == 1) {// 1 in 40 chance
                    doubleReward = true;
                }
                break;
            case MYSTICAL_DONATOR:
                chance += 4; // Extra 4% Chance to win
                if (Misc.random(30) == 1) {// 1 in 30 chance
                    doubleReward = true;
                }
                break;
            case OBSIDIAN_DONATOR:
                chance += 5; // Extra 5% Chance to win
                if (Misc.random(20) == 1) {// 1 in 20 chance
                    doubleReward = true;
                }
                break;
            case FORSAKEN_DONATOR:
                chance += 6; // Extra 6% Chance to win
                if (Misc.random(10) == 1) {// 1 in 10 chance
                    doubleReward = true;
                }
                break;
        }

        if (chance <= 98) { // 2% Chance to win
            return; // If the player doesn't win, stop the whole thing
        }

        if (doubleReward) {
            amountToGive *= 2;
        }

        String plural = amountToGive > 1 ? "'s" : "";

        // Checking if the stoneID has been set & player has a free slot
        if (player.getInventory().getFreeSlots() == 0 && amountToGive >= 1) {



            if (rights.isMember() || rights.isStaff()) {
                player.getBank(player.getCurrentBankTab()).add(stoneID, amountToGive);
                player.sendMessage("As an "
                        + rights
                        + " benefit, we sent "
                        + amountToGive
                        + " x "
                        + ItemDefinition.forId(stoneID).getName()
                        + plural
                        + " to your bank <3");
            } else {
                player.sendMessage("You would've received "
                        + amountToGive
                        + " x "
                        + ItemDefinition.forId(stoneID).getName()
                        + plural
                        + ".. Free up some slots!");
            }
            return;
        }

        player.getInventory().add(stoneID, amountToGive);

        if (doubleReward) {
            player.sendMessage("Congratulations! Your reward was doubled as an "
                    + rights);
        }

        player.sendMessage("You have received @blu@"
                + amountToGive
                + " @bla@x @blu@"
                + ItemDefinition.forId(stoneID).getName()
                + plural
                + "@bla@!");

    }
}
