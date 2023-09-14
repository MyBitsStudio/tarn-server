package com.ruse.world.packages.dissolve;

import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;

public class DissolveItem {

    public static int TOKENS = 10835, COINS = 995;
    public static boolean dissolveItem(Player player, int id, int slot){
        if(!World.attributes.getSetting("dissolve")){
            player.getPacketSender().sendMessage("You cannot dissolve items right now.");
            return false;
        }
        Item toDissolve;
        boolean ran = false;
        switch(id){
            case 19984,19985, 19986,19987,20400,19989,
                    19988,19993,19992,19991,
                    21063, 21064, 21066, 21067, 21068,
                    21069, 21071, 5012,
                    9942, 9939, 4684, 4685, 4686,
                    8273, 8274-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(25, 200));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 2);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 18629, 20086, 20087, 20088, 20091,
                    20089, 20092, 20093 ,
                    21042, 21043, 21044, 21045, 21046,
                    21047, 19998,
                    21018, 21015, 21016, 21017-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(50, 250));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 5);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 21036, 21037, 21038, 21039, 21040, 21041, 8088 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(75, 325));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 9);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 17999, 18005, 18001, 18003, 18009, 17011 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(100, 500));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 12);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 23050, 23051, 23052, 23053, 23054, 23056, 23055 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(150, 750));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 16);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 23079, 23080, 23075, 23076, 23077 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(250, 990));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 21);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 23127, 23128, 23129, 23130, 23131, 23133, 14924 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(375, 1660));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 26);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 14919, 23134, 23135, 23136, 23137, 23138 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(425, 2160));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 32);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 17720, 23139, 23140, 23141, 23142, 23143 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(555, 2880));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 38);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 8818, 8817, 8816, 23144, 23145, 23146 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(650, 3600));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 42);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 11320, 11321, 11322, 23132,
                    11340, 11341, 11342, 8001,
                    11421, 11422, 11423, 17600-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(780, 4200));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 48);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 15115, 15116, 15117, 15118, 15119, 15121,
                    19331, 14050, 14051, 14052,
                    19800, 14056, 20060, 20062, 20063, 20073-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve);
                    player.getInventory().add(COINS, Misc.random(890, 4990));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 52);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 3740, 3741, 3742, 3744, 3745 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(925, 5675));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 58);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
            case 3720, 3721, 3722, 3737, 3739, 3726, 3728, 3730, 3723,
                    3724, 3725, 3738-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(989, 6190));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 62);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 15005, 15006, 15007, 15008, 15200, 15201, 15100, 14915 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(TOKENS, Misc.random(25, 450));
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 198);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
        }

        if(ran){
            AchievementHandler.progress(player, 1, 46);
            AchievementHandler.progress(player, 1, 49);
            AchievementHandler.progress(player, 1, 50);
            AchievementHandler.progress(player, 1, 71);
            AchievementHandler.progress(player, 1, 72);
            AchievementHandler.progress(player, 1, 92);
            AchievementHandler.progress(player, 1, 93);
        }

        return false;
    }
}
