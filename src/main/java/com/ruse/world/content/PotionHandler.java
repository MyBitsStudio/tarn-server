package com.ruse.world.content;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.*;
import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Locations;
import com.ruse.model.Skill;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class PotionHandler {

    public static int INFINITY_RAGE = 15328;
    public static int INF_OVERLOAD = 15330;
    public static int T1_INF_OVERLOAD = 23124;
    public static int T2_INF_OVERLOAD = 23125;
    public static int T3_INF_OVERLOAD = 23126;
    public static int SUPER_OVL_1 = 15331;
    public static int MORTAL_AGGRO = 17546;
    public static int GODLY_AGGRO = 17544;
    public static int DIVINE_AGGRO = 17542;
    public static int MORTAL_EXP = 1027;
    public static int GODLY_EXP = 1031;
    public static int DIVINE_EXP = 1033;
    public static int MORTAL_DR = 3084;
    public static int GODLY_DR = 3086;
    public static int DIVINE_DR = 3088;
    public static int MORTAL_DDR = 1035;
    public static int GODLY_DDR = 3080;
    public static int DIVINE_DDR = 3082;
    public static int MORTAL_DMG = 3090;
    public static int GODLY_DMG = 3092;
    public static int DIVINE_DMG = 3094;

    public static void drinkPotion(Player player, int slot, int type){
      //  System.out.println("Running "+type);
        if(!canDrink(player, type))
            return;

        player.performAnimation(new Animation(animation(type)));
        player.getInventory().getItems()[slot] = new Item(replacePot(type), 1);
        player.getInventory().refreshItems();

        setTimer(player, type);
        setPotUsed(player, type);

        sendTask(player, type);

        player.getPacketSender().sendInterfaceRemoval();
        player.getCombatBuilder().incrementAttackTimer(1).cooldown(false);
        player.getCombatBuilder().setDistanceSession(null);
        player.setCastSpell(null);
        player.getFoodTimer().reset();
        player.getPotionTimer().reset();

        sendInter(player, type);

        postEffect(player, type);

        Sounds.sendSound(player, Sounds.Sound.DRINK_POTION);
    }

    private static void sendInter(Player player, int type){
        switch (type) {
            case 15328, 15330, 15331, 23124, 23125, 23126 ->
                    player.getPacketSender().sendWalkableInterface(48300, true);
            case 17546, 17544, 17542 -> player.getPacketSender().sendWalkableInterface(58350, true);
            case 1027, 1031, 1033 -> player.getPacketSender().sendWalkableInterface(58360, true);
            case 3084, 3086, 3088 -> player.getPacketSender().sendWalkableInterface(58370, true);
            case 1035, 3080, 3082 -> player.getPacketSender().sendWalkableInterface(58380, true);
            case 3090, 3092, 3094 -> player.getPacketSender().sendWalkableInterface(58390, true);
        }
    }

    private static boolean canDrink(Player player, int type){
        switch(type){
            case 15328:
            case 15330:
            case 15331:
            case 23124:
            case 23125:
            case 23126:
                if (player.getLocation() == Locations.Location.WILDERNESS || player.getLocation() == Locations.Location.DUEL_ARENA) {
                    player.getPacketSender().sendMessage("You cannot use this potion here.");
                    return false;
                }
                if (player.getOverloadPotionTimer() > 0 && player.getOverloadPotionTimer() < 750) {
                    player.getPacketSender().sendMessage("You already have the effect of an Overload or Super/Infinity Overload potion.");
                    return false;
                }
                if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < 500) {
                    player.getPacketSender().sendMessage("You need to have at least 500 Hitpoints to drink this potion.");
                    return false;
                }
                return true;
        }
        return true;
    }

    public static int animation(int type){
        switch(type){
            case 15328:
            case 15330:
            case 23124:
            case 23125:
            case 23126:
            case 17546:
            case 17544:
            case 17542:
            case 1027:
            case 1031:
            case 1033:
            case 3084:
            case 3086:
            case 3088:
            case 1035:
            case 3080:
            case 3082:
            case 3090:
            case 3092:
            case 3094:
                return 829;
            default:
                return -1;
        }
    }

    public static int replacePot(int type){
        switch(type){
            case 15328:
            case 15330:
            case 15331:
                return type;
            case 23124:
                return 23124;
            case 23125:
                return 23125;
            case 23126:
                return 23126;
            case 17546:
            case 17544:
            case 17542:
            case 1027:
            case 1031:
            case 1033:
            case 3084:
            case 3086:
            case 3088:
            case 1035:
            case 3080:
            case 3082:
            case 3090:
            case 3092:
            case 3094:
            default:
                return 229;
        }
    }

    public static void setTimer(Player player, int type){
        switch (type) {
            case 15328, 15330 -> player.setOverloadPotionTimer(10000);
            case 23124 -> player.setOverloadPotionTimer(180);
            case 23125 -> player.setOverloadPotionTimer(240);
            case 23126 -> player.setOverloadPotionTimer(300);
            case 15331 -> player.setOverloadPotionTimer(600);

            //AGGRO
            case 17546 -> player.setAggroPotionTimer(200);
            case 17544 -> player.setAggroPotionTimer(500);
            case 17542 -> player.setAggroPotionTimer(1000);

            //EXP
            case 1027 -> player.setExpPotionTimer(1000);
            case 1031 -> player.setExpPotionTimer(2500);
            case 1033 -> player.setExpPotionTimer(5000);
            case 3084 -> player.setDrPotionTimer(1000);
            case 3086 -> player.setDrPotionTimer(2500);
            case 3088 -> player.setDrPotionTimer(5000);
            case 1035 -> player.setDdrPotionTimer(1000);
            case 3080 -> player.setDdrPotionTimer(2500);
            case 3082 -> player.setDdrPotionTimer(5000);
            case 3090 -> player.setDmgPotionTimer(1000);
            case 3092 -> player.setDmgPotionTimer(2500);
            case 3094 -> player.setDmgPotionTimer(5000);
        }
    }

    public static void setPotUsed(Player player, int type){
        switch (type) {
            case 15328 -> player.setPotionUsed("Rage");
            case 15330, 15331, 23124, 23125, 23126 -> player.setPotionUsed("Super Ovl");
        }
    }

    public static void sendTask(Player player, int type){
        Task task = switch (type) {
            case 15328 -> new InfinityRagePotionTask(player);
            case 15330, 15331, 23124, 23125, 23126 -> new SuperOverloadPotionTask(player);
            case 17546 -> new AggroPotionTask(player, 0);
            case 17544 -> new AggroPotionTask(player, 1);
            case 17542 -> new AggroPotionTask(player, 2);
            case 1027 -> new ExpPotionTask(player, 0);
            case 1031 -> new ExpPotionTask(player, 1);
            case 1033 -> new ExpPotionTask(player, 2);
            case 3084 -> new DrPotionTask(player, 0);
            case 3086 -> new DrPotionTask(player, 1);
            case 3088 -> new DrPotionTask(player, 2);
            case 1035 -> new DdrPotionTask(player, 0);
            case 3080 -> new DdrPotionTask(player, 1);
            case 3082 -> new DdrPotionTask(player, 2);
            case 3090 -> new DmgPotionTask(player, 0);
            case 3092 -> new DmgPotionTask(player, 1);
            case 3094 -> new DmgPotionTask(player, 2);
            default -> null;
        };
        if(task != null)
            TaskManager.submit(task);
    }

    public static void postEffect(Player player, int type){
        switch (type) {
            case 15328 -> {
                if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
                    Consumables.overloadIncrease(player, Skill.ATTACK, 0.67);
                    Consumables.overloadIncrease(player, Skill.STRENGTH, 0.67);
                    Consumables.overloadIncrease(player, Skill.DEFENCE, 0.67);
                    Consumables.overloadIncrease(player, Skill.RANGED, 0.67);
                    Consumables.overloadIncrease(player, Skill.MAGIC, 0.67);
                }
            }
            case 15330, 15331, 23124, 23125, 23126 -> {
                if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
                    Consumables.overloadIncrease(player, Skill.ATTACK, 0.38);
                    Consumables.overloadIncrease(player, Skill.STRENGTH, 0.38);
                    Consumables.overloadIncrease(player, Skill.DEFENCE, 0.38);
                    Consumables.overloadIncrease(player, Skill.RANGED, 0.38);
                    Consumables.overloadIncrease(player, Skill.MAGIC, 0.38);
                }
            }
            case 17546, 17544, 17542 -> {
                ObjectArrayList<NPC> npcs = World.getNearbyNPCs(player.getPosition(), 6);
                for (NPC npc : npcs) {
                    if (npc != null) {
                        if (!npc.isAggressive() && npc.getDefinition().isAttackable()) {
                            npc.setAggressive(true);
                            npc.setAggressiveDistance(10);
                            npc.setForceAggressive(true);
                        }
                    }
                }
            }
            case 1027, 1031, 1033 -> {
                if (player.getExpPotionTimer() > 0) {
                    //BonusExperienceTask.addBonusXp(player, player.getExpPotionTimer());
                }
            }
            case 3084, 3086, 3088 -> {
                if (player.getDrPotionTimer() > 0) {
                    //BonusExperienceTask.addBonusXp(player, player.getExpPotionTimer());
                }
            }
            case 1035, 3080, 3082 -> {
                if (player.getDdrPotionTimer() > 0) {
                    //BonusExperienceTask.addBonusXp(player, player.getExpPotionTimer());
                }
            }
            case 3090, 3092, 3094 -> {
                if (player.getDmgPotionTimer() > 0) {
                    //BonusExperienceTask.addBonusXp(player, player.getExpPotionTimer());
                }
            }
        }
    }
}
