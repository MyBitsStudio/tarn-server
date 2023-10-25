package com.ruse.world.packages.dissolve;

import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.skills.S_Skills;

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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 2);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 5);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 9);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 12);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 16);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 21);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 26);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 32);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 38);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 42);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 48);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 52);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 58);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 62);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 17614, 17616, 17618, 17606, 8411, 8410, 8412-> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(1021, 8160));
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 69);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 5068, 5069, 5070, 5071, 5072, 17718, 13328, 13329,
                    13330, 13332, 13333, 8828, 8829, 8833, 8830,
                    8831, 22173 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(1141, 9652));
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 74);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 22177, 23066, 23067, 23064, 23065, 22202, 22203, 23061,
                    23062, 23063, 23068 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(1341, 11985));
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 79);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 22179, 22180, 22181, 22182, 22183, 22184, 22161, 22162,
                    22163, 22164, 22165, 22166, 22167, 21607, 21608,
                    21609, 21610, 21611, 21612, 22176 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(2541, 19985));
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 86);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 22134, 8109, 8108, 8107, 8106, 8105, 8110, 8104, 8103,
                    8102, 8101, 8100, 14202, 14204, 14206, 14301,
                    14303, 14305, 14307 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(COINS, Misc.random(5241, 39985));
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 99);
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
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 198);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }

            case 23150, 23151, 23152, 23153, 23154, 23155, 23156, 23157,
                    23158, 23159, 23160, 25000, 25001 -> {
                toDissolve = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolve.getId())) {
                    player.getInventory().delete(toDissolve)
                            .add(19639, 1);
                    player.performAnimation(new Animation(712));
                    player.getNewSkills().xpUp(S_Skills.CRAFTING, 92);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@");
                }
                ran = true;
            }
        }

        if(ran){
            player.getPoints().add("dissolve", 1);
            AchievementHandler.progress(player, 1, 46, 49, 50, 71, 72, 92, 93);
            player.getTarnNormal().handleDissolveTasks(player.getPoints().get("dissolve"));
            player.getTarnElite().handleDissolveTasks(player.getPoints().get("dissolve"));
        }

        return false;
    }
}
