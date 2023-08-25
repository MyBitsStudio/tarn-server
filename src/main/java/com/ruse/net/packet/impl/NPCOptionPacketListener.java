package com.ruse.net.packet.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.impl.WalkToTask;
import com.ruse.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.World;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.combat.magic.CombatSpell;
import com.ruse.world.content.combat.magic.CombatSpells;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.dailytasks_new.DailyTasks;
import com.ruse.world.content.dialogue.EnterLotteryTicketAmount;
import com.ruse.world.content.grandLottery.GrandLottery;
import com.ruse.world.content.grandexchange.GrandExchange;
import com.ruse.world.content.groupironman.GroupConfig;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.content.holidayevents.christmas2016;
import com.ruse.world.content.minigames.impl.WarriorsGuild;
import com.ruse.world.content.minigames.impl.trioMinigame;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.construction.ConstructionActions;
import com.ruse.world.content.skill.impl.crafting.Tanning;
import com.ruse.world.content.skill.impl.fishing.Fishing;
import com.ruse.world.content.skill.impl.herblore.Decanting;
import com.ruse.world.content.skill.impl.hunter.PuroPuro;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.old_dungeoneering.UltimateIronmanHandler;
import com.ruse.world.content.skill.impl.runecrafting.DesoSpan;
import com.ruse.world.content.skill.impl.slayer.SlayerMaster;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.content.skill.impl.summoning.Summoning;
import com.ruse.world.content.skill.impl.summoning.SummoningData;
import com.ruse.world.content.skill.impl.thieving.Pickpocket;
import com.ruse.world.content.skill.impl.thieving.PickpocketData;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import com.ruse.world.packages.shops.ShopHandler;

import static com.ruse.world.content.combat.CombatType.RANGED;

public class NPCOptionPacketListener implements PacketListener {

    private static void firstClick(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;
        player.setEntityInteraction(npc);
        if (player.getRank().isDeveloper())
            player.getPacketSender().sendMessage("First click npc id: " + npc.getId());
        if (BossPets.pickup(player, npc)) {
            player.getMovementQueue().reset();
            return;
        }
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), () -> {
            if (!player.getControllerManager().processNPCClick1(npc)) {
                return;
            }

            if (SummoningData.beastOfBurden(npc.getId())) {
                Summoning summoning = player.getSummoning();
                if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
                        && summoning.getFamiliar().getSummonNpc() != null
                        && summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                    summoning.store();
                    player.getMovementQueue().reset();
                } else {
                    player.getPacketSender().sendMessage("That familiar is not yours!");
                }
                return;
            }
            if (ConstructionActions.handleFirstClickNpc(player, npc)) {
                return;
            }
            switch (npc.getId()) {
                case 3321:
                    ShopHandler.getShop(4).ifPresent(shop -> shop.send(player, true));
                    break;
                case 5049:
                    ShopHandler.getShop(5).ifPresent(shop -> shop.send(player, true));
                    break;
                case 932:
                    ShopHandler.getShop(6).ifPresent(shop -> shop.send(player, true));
                    break;
                case 550:
                    if(Lobby.getInstance().getGame() == null) return;
                    Lobby.getInstance().getGame().obeliskClick(player);
                    break;
                case 4651:
                    player.getTradingPost().openMainInterface();
                    break;
                case 5249:
                    ShopHandler.getShop(0).ifPresent(shop -> shop.send(player, true));
                    break;
                case 3373:
//                    DialogueManager.start(player, 8005);
//                    player.setDialogueActionId(8005);
                    break;
                case 568:
//                    DialogueManager.start(player, 1311);
//                    player.setDialogueActionId(568);
                    break;
                case 289: //DAILY TASK
//                    if (player.dailies.isEmpty()) {
//                        DialogueManager.start(player, 9901);
//                        player.setDialogueActionId(9905);
//                    } else {
//                        DailyTasks.sendProgress(player);
//                        player.getPacketSender().sendMessage(player.taskInfo);
//                    }
                    break;
                case 9022:
                    ServerPerks.getInstance().open(player);
                    break;
                case GroupConfig.NPC_ID:
//                    if (player.getMode() instanceof GroupIronman) {
//                        if (GroupManager.isInGroup(player)) {
//                            GroupManager.openInterface(player);
//                        } else {
//                            DialogueManager.start(player, 8001);
//                            player.setDialogueActionId(8001);
//                        }
//                    } else {
//                        player.sendMessage("You must be a group ironman to do this.");
//                    }
                    break;
                case 9000://first click
                    if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.BOSS_SLAYER)
                            && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
                        SlayerMaster.changeSlayerMaster(player, SlayerMaster.BOSS_SLAYER);
                    }
                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.BOSS_SLAYER)) {
                       // DialogueManager.start(player, BossSlayerDialogues.dialogue(player));
                    }else {
                        SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
                        SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
                        String yourMastersName = "";
                        String thisMasterName = "";
                        int reqSlayer = 0;
                        if(yourMaster != null) {
                            yourMastersName = yourMaster.getSlayerMasterName();
                        }
                        if(thisMaster != null) {
                            reqSlayer = thisMaster.getSlayerReq();
                            thisMasterName = thisMaster.getSlayerMasterName();
                        }
                        if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
                            DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName  + ".");
                        } else {
                            DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
                        }
                    }
                    break;

                case 2938:
                    player.getDailyRewards().processTime();
                    player.getDailyRewards().displayRewards();
                    npc.forceChat("Talk to me to get your Daily Rewards!");
                    break;
                case 662:
                    GrandLottery.open(player);
                    break;

                case 1050:
                    player.moveTo(new Position(2793, 3276));
                    npc.forceChat("Deep sea fishing!");
                    break;

                case 783:
                    npc.forceChat("talk to me for starter tasks!");
                    StarterTasks.updateInterface(player);
                    int[] ids = {22074, 6570, 7462, 17273, 19153, 19142, 19141, 19115, 11137, 20000, 6769};
                    for (int i = 0; i < ids.length; i++) {
                        player.getPacketSender().sendItemOnInterface(53205, ids[i], i);
                    }
                    player.getPacketSender().sendInterfaceReset();
                    player.getPacketSender().sendInterface(53200);
                    break;
//                case 659:
//                    if (GameSettings.newYear2017) {
//                        if (player.getNewYear2017() == 0) {
//                            DialogueManager.start(player, 189);
//                            player.setDialogueActionId(189);
//                        } else {
//                            DialogueManager.start(player, 190);
//                        }
//                    } else {
//                        npc.forceChat("I love a good party!");
//                        ShopManager.getShops().get(81).open(player);
//                    }
//                    break;
//                case 4653:
//                    DialogueManager.start(player, 178);
//                    player.setDialogueActionId(178);
//                    break;
//                case 1872:
//                    if (player.getLocation() == Location.ZULRAH_WAITING) {
//                        DialogueManager.start(player, 200);
//                    }
//                    break;
//                case 1552:
//                    if (christmas2016.isChristmas()) {
//                        if (player.getChristmas2016() == 0) {
//                            DialogueManager.start(player, 169);
//                            player.setDialogueActionId(171);
//                        } else if (player.getChristmas2016() == 1) {
//                            player.getPacketSender().sendMessage("Santa wants me to talk to Explorer Jack at home.");
//                        } else if (player.getChristmas2016() == 2) {
//                            DialogueManager.start(player, 181);
//                        } else if (player.getChristmas2016() > 2 && player.getChristmas2016() < 5) {
//                            DialogueManager.start(player, 182);
//                            player.getPacketSender().sendMessage("The Reindeer need Law, Cosmic, and Nature runes.");
//                        } else if (player.getChristmas2016() == 5) {
//                            DialogueManager.start(player, 183);
//                            player.getPacketSender().sendMessage("I should \"use\" the Mind Bomb on Santa.");
//                        } else if (player.getChristmas2016() == 6) {
//                            DialogueManager.start(player, 184);
//                            player.setDialogueActionId(187);
//                        } else if (player.getChristmas2016() == 7) {
//                            DialogueManager.start(player, 188);
//                        } else {
//                            npc.forceChat("Ho ho ho!");
//                        }
//                    } else {
//                        npc.forceChat("Ho ho ho!");
//                    }
//                    break;
//                case 3777:
//                    DialogueManager.start(player, 141);
//                    player.setDialogueActionId(88);
//                    break;
                case 13738:
                    player.getUpgradeHandler().openInterface();
                    break;
                case 5:
//                case 4:
//                    npc.setPositionToFace(player.getPosition());
//                    DialogueManager.start(player, 167);
//                    break;
                case 1:
              //  case 2:
//                case 3:
//                    npc.setPositionToFace(player.getPosition());
//                    DialogueManager.start(player, 165);
//                    break;
//                case 2238:
//                    npc.setPositionToFace(player.getPosition());
//                    DialogueManager.start(player, 155);
//                    break;
//                case 1152:
//                    DialogueManager.start(player, 127);
//                    player.setDialogueActionId(79);
//                    break;


//                case 1837:
//                    DialogueManager.start(player, 144);
//                    player.setDialogueActionId(99);
//                    break;
//                case 457:
//                    DialogueManager.start(player, 117);
//                    player.setDialogueActionId(74);
//                    break;
                case 8710:
                case 8707:
                case 8706:
                case 8705:
                    EnergyHandler.rest(player);
                    break;
                case 534:
                    //ShopManager.getShops().get(78).open(player);
                    break;
//                case 947:
//                    if (player.getPosition().getX() >= 3092) {
//                        player.getMovementQueue().reset();
//                        GrandExchange.open(player);
//                    }
//                    break;
//                case 9713:
//                    DialogueManager.start(player, 107);
//                    player.setDialogueActionId(69);
//                    break;
//                case 2622:
//                    ShopManager.getShops().get(43).open(player);
//                    break;
//                case 3101:
//                    DialogueManager.start(player, 90);
//                    player.setDialogueActionId(57);
//                    break;
//                case 7969:
//                    // player.getPacketSender().sendMessage("yayayaya i am lord");
//                    if (christmas2016.isChristmas() == false || player.getChristmas2016() == 0) {
//                        ShopManager.getShops().get(28).open(player);
//                        return;
//                    } else if (player.getChristmas2016() == 1) {
//                        // player.getPacketSender().sendMessage("dialogue 173");
//                        DialogueManager.start(player, 173);
//                        player.setDialogueActionId(173);
//                    } else if (player.getChristmas2016() == 2) {
//                        DialogueManager.start(player, 173);
//                        player.setDialogueActionId(505050);
//                    }
//                    // DialogueManager.start(player, ExplorerJack.getDialogue(player));
//                    break;
//                case 1597:
//                    if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.EASY_SLAYER)
//                            && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                        SlayerMaster.changeSlayerMaster(player, SlayerMaster.EASY_SLAYER);
//                    }
//                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.EASY_SLAYER))
//                        DialogueManager.start(player, SlayerDialogues.dialogue(player));
//                    else {
//                        SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                        SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                        String yourMastersName = "";
//                        String thisMasterName = "";
//                        int reqSlayer = 0;
//                        if(yourMaster != null) {
//                            yourMastersName = yourMaster.getSlayerMasterName();
//                        }
//                        if(thisMaster != null) {
//                            reqSlayer = thisMaster.getSlayerReq();
//                            thisMasterName = thisMaster.getSlayerMasterName();
//                        }
//                        if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                            DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName  + ".");
//                        } else {
//                            DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                        }
//                    }
//                    break;
//                case 8275:
//                    if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.MEDIUM_SLAYER)
//                            && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                        SlayerMaster.changeSlayerMaster(player, SlayerMaster.MEDIUM_SLAYER);
//                    }
//                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.MEDIUM_SLAYER))
//                        DialogueManager.start(player, SlayerDialogues.dialogue(player));
//                    else {
//                        SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                        SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                        String yourMastersName = "";
//                        String thisMasterName = "";
//                        int reqSlayer = 0;
//                        if(yourMaster != null) {
//                            yourMastersName = yourMaster.getSlayerMasterName();
//                        }
//                        if(thisMaster != null) {
//                            reqSlayer = thisMaster.getSlayerReq();
//                            thisMasterName = thisMaster.getSlayerMasterName();
//                        }
//                        if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                            DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName  + ".");
//                        } else {
//                            DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                        }
//                    }
//                    break;
//                case 9085:
//                    if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.HARD_SLAYER)
//                            && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                        SlayerMaster.changeSlayerMaster(player, SlayerMaster.HARD_SLAYER);
//                    }
//                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.HARD_SLAYER))
//                        DialogueManager.start(player, SlayerDialogues.dialogue(player));
//                    else {
//                        SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                        SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                        String yourMastersName = "";
//                        String thisMasterName = "";
//                        int reqSlayer = 0;
//                        if(yourMaster != null) {
//                            yourMastersName = yourMaster.getSlayerMasterName();
//                        }
//                        if(thisMaster != null) {
//                            reqSlayer = thisMaster.getSlayerReq();
//                            thisMasterName = thisMaster.getSlayerMasterName();
//                        }
//                        if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                            DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName  + ".");
//                        } else {
//                            DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                        }
//                    }
//                    break;
//                case 925:
//                    if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.ELITE_SLAYER)
//                            && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                        SlayerMaster.changeSlayerMaster(player, SlayerMaster.ELITE_SLAYER);
//                    }
//                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.ELITE_SLAYER))
//                        DialogueManager.start(player, SlayerDialogues.dialogue(player));
//                    else {
//                        SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                        SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                        String yourMastersName = "";
//                        String thisMasterName = "";
//                        int reqSlayer = 0;
//                        if(yourMaster != null) {
//                            yourMastersName = yourMaster.getSlayerMasterName();
//                        }
//                        if(thisMaster != null) {
//                            reqSlayer = thisMaster.getSlayerReq();
//                            thisMasterName = thisMaster.getSlayerMasterName();
//                        }
//                        if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                            DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName  + ".");
//                        } else {
//                            DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                        }
//                    }
//                    break;
//                case 437:
//                    DialogueManager.start(player, 99);
//                    player.setDialogueActionId(58);
//                    break;
//                case 5112:
//                    ShopManager.getShops().get(38).open(player);
//                    break;
//                case 8591:
//                    // player.nomadQuest[0] = player.nomadQuest[1] = player.nomadQuest[2] = false;
//                    if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
//                        DialogueManager.start(player, 48);
//                        player.setDialogueActionId(23);
//                    } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)
//                            && !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
//                        DialogueManager.start(player, 50);
//                        player.setDialogueActionId(24);
//                    } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
//                        DialogueManager.start(player, 53);
//                    break;
//                case 273:
//                    DialogueManager.start(player, 61);
//                    player.setDialogueActionId(28);
//                    break;
//                case 3385:
//                    if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player
//                            .getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
//                        DialogueManager.start(player, 39);
//                        return;
//                    }
//                    if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
//                        DialogueManager.start(player, 46);
//                        return;
//                    }
//                    DialogueManager.start(player, 38);
//                    player.setDialogueActionId(20);
//                    break;
               /* case 6139:
                    DialogueManager.start(player, 29);
                    player.setDialogueActionId(17);
                    break;*/
                case 3789:

                    player.getPacketSender().sendInterface(18730);
                    player.getPacketSender().sendString(18729,
                            "Commendations: " + Integer.toString(player.getPointsHandler().getCommendations()));
                    break;
//                case 2948:
//                    DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
//                    break;
//                case 650:
//                    ShopManager.getShops().get(35).open(player);
//                    break;
                case 6055:
                case 6056:
                case 6057:
                case 6058:
                case 6059:
                case 6060:
                case 6061:
                case 6062:
                case 6063:
                case 6064:
                case 7903:
                    if (npc.getId() == 7903 && player.getLocation() == Location.MEMBER_ZONE) {
                        if (!player.getDonator().isMember()) {
                            player.getPacketSender().sendMessage("You must be a Member to use this.");
                            return;
                        }
                    }
                    PuroPuro.catchImpling(player, npc);
                    break;
                case 8022:
                case 8028:
                    DesoSpan.siphon(player, npc);
                    break;
//                case 6537:
//                    player.setDialogueActionId(10);
//                    DialogueManager.start(player, 19);
//                    break;
//                case 4249:
//                    player.setDialogueActionId(9);
//                    DialogueManager.start(player, 64);
//                    break;
                case 6807:
                case 6994:
                case 6995:
                case 6867:
                case 6868:
                case 6794:
                case 6795:
                case 6815:
                case 6816:
                case 6874:
                case 6873:
                case 3594:
                case 3590:
                case 3596:
                    if (player.getSummoning().getFamiliar() == null
                            || player.getSummoning().getFamiliar().getSummonNpc() == null
                            || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
                        player.getPacketSender().sendMessage("That is not your familiar.");
                        return;
                    }
                    player.getSummoning().store();
                    break;
                case 605:
                    ShopHandler.getShop(3).ifPresent(shop -> shop.send(player, true));
                    // player.setDialogueActionId(8);
                    // DialogueManager.start(player, 13);
                    break;
//                case 6970:
//                    player.setDialogueActionId(3);
//                    DialogueManager.start(player, 3);
//                    break;
                case 318:
                case 316:
                case 313:
                case 312:
                case 5748:
                case 2067:
                    player.setEntityInteraction(npc);
                    Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
                    break;
//                case 805:
//                    ShopManager.getShops().get(34).open(player);
//                    break;
//                case 2843:
//                    ShopManager.getShops().get(208).open(player);
//                    break;
//                case 462:
//                    ShopManager.getShops().get(33).open(player);
//                    break;
//                case 461:
//                    ShopManager.getShops().get(32).open(player);
//                    break;
//                case 8444:
//
//                    ShopManager.getShops().get(31).open(player);
//                    break;
//                case 400:
//
//                    ShopManager.getShops().get(101).open(player);
//                    break;
//                case 8459:
//                    ShopManager.getShops().get(30).open(player);
//                    break;
//                case 3299:
//                    ShopManager.getShops().get(21).open(player);
//                    break;
//                case 548:
//                    ShopManager.getShops().get(20).open(player);
//                    break;
//                case 1685:
//                    ShopManager.getShops().get(19).open(player);
//                    break;
//                case 308:
//                    ShopManager.getShops().get(18).open(player);
//                    break;
//                case 802:
//                    ShopManager.getShops().get(17).open(player);
//                    break;
//                case 970:
//                    ShopManager.getShops().get(81).open(player);
//                    break;
//                // case 12241:
//                case 8405:
//                    ShopManager.getShops().get(99).open(player);
//                    break;
//                case 278:
//                    ShopManager.getShops().get(16).open(player);
//                    break;
//                case 4946:
//                    ShopManager.getShops().get(15).open(player);
//                    break;
//                case 948:
//                    ShopManager.getShops().get(13).open(player);
//                    break;
//                case 4906:
//                    ShopManager.getShops().get(14).open(player);
//                    break;
                case 520:
                case 521:
                    World.sendStaffMessage("<col=FF0066><img=2> [ALERT]<col=6600FF> "
                            + player.getUsername() + " just tried to use the general store!");
                    /*
                    ShopManager.getShops().get(12).open(player);*/
                    break;
//                case 2292:
//                    ShopManager.getShops().get(11).open(player);
//                    break;
//                case 28:
//                    player.getPetShop().openInterface(PetShop.PetShopType.DAMAGE);
//                    break;
                case 2676:
                    player.getPacketSender().sendInterface(3559);
                    player.getAppearance().setCanChangeAppearance(true);
                    break;
//                case 519:
//                    ShopManager.getShops().get(84).open(player);
//                    break;
 //               case 494:
//                case 1360:
//                    if (player.getMode() instanceof GroupIronman
//                            && player.getIronmanGroup() != null) {
//                        DialogueManager.start(player, 8002);
//                        player.setDialogueActionId(8002);
//                    } else {
//                        player.getBank(player.getCurrentBankTab()).open();
//                    }
//                    break;
            }
            if (!(npc.getId() >= 8705 && npc.getId() <= 8710) && npc.getId() != 550 && npc.getId() != 551) {
                npc.setPositionToFace(player.getPosition());
            }
            player.setPositionToFace(npc.getPosition());
        }));
    }

    public static void attackNPC(Player player, Packet packet) {
        int index = packet.readShortA();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC interact = World.getNpcs().get(index);
        if (interact == null)
            return;

        if(NpcDefinition.definitions[interact.getId()] == null){
            System.out.println("NPC Def null -- "+interact.getId());
        }
        else if (!NpcDefinition.definitions[interact.getId()].isAttackable()) {
            return;
        }
        //if (!interact.isAttackable() && interact.getId() == 12810) {
       //     player.getMovementQueue().reset();
       //     player.sendMessage("You cannot attack him while his minions are still alive.");
      //      return;
      //  }

       /* if (player.getUsername() == "Nucky"){
            interact.setConstitution(10);
        }*/


        if (interact.getConstitution() <= 0 && !interact.isDying()){
        //    player.sendMessage("This npc was glitched");
            interact.setConstitution(interact.getDefinition().getHitpoints());
            //TaskManager.submit(new NPCRespawnTask(interact,  2, player));
           // World.deregister(interact);

        }

        if (interact.getConstitution() <= 0) {
            player.getMovementQueue().reset();
            return;
        }

        if (player.getUsername().equalsIgnoreCase("test")){
        //    World.deregister(interact);
        }
        if (player.getEquipment().contains(22006) && player.getLastCombatType() == RANGED) {
            if (CombatFactory.npcsDeathDartDontWork(interact)) {
                player.getMovementQueue().reset();
                return;
            }
        }
        if (player.getCombatBuilder().getStrategy() == null) {
            player.getCombatBuilder().determineStrategy();
        }
        if (CombatFactory.checkAttackDistance(player, interact)) {
            player.getMovementQueue().reset();
        }
        if (UltimateIronmanHandler.hasItemsStored(player) && player.getLocation() != Location.DUNGEONEERING) {
            player.getPacketSender().sendMessage("You must claim your stored items at Dungeoneering first.");
            player.getMovementQueue().reset();
            return;
        }

        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }
        if (interact.getId() == 111) {
            if (player.getSlayer().getSlayerTask().getNpcId() != interact.getId()) {
                player.sendMessage("@or2@You can only attack @red@" + interact.getDefinition().getName() + " @or2@when it's assigned as a slayer task.");
                return;
            }
        }

        if (interact.getId() == 3){
            player.sendMessage("The Dan Event has ended.");
            player.getCombatBuilder().reset(true);
            return;
            /*int total = KillsTracker.getTotalKillsForNpc(interact.getId(), player);
            if (total >= 10000){
                player.sendMessage("You have reached your 10,000 kill limit for Dan's presents.");
                player.getCombatBuilder().reset(true);
                return;
            }*/
        }

        if (interact.getId() == 9019){
            player.sendMessage("The St. Patrick's Event has ended.");
            player.getCombatBuilder().reset(true);
            return;
           /* int total = KillsTracker.getTotalKillsForNpc(interact.getId(), player);
            if (total >= 10000){
                player.sendMessage("You have reached your 10,000 kill limit for St. Patrick Leprechauns.");
                player.getCombatBuilder().reset(true);
                return;
            }*/
        }


        if (interact.getId() == 9020) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 9020) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking the Bunny!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

        if (interact.getId() == 8013) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 8013) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking the Vote Boss!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }
        if (interact.getId() == 9907) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 9907) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking Meruem!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }
        if (interact.getId() == 9906) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 9906) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking Veigar!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

        if (interact.getId() == 9904) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 9904) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking Nine Tails!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

        if (interact.getId() == 9908) {
            int accounts = 0;
            for (Player p : World.getPlayers()) {
                if (p == null)
                    continue;
                if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                    if (p.getCombatBuilder() != null && p.getCombatBuilder().getVictim() != null
                            && p.getCombatBuilder().getVictim().isNpc() && ((NPC)p.getCombatBuilder().getVictim()).getId() == 9908) {
                        accounts++;
                        continue;
                    }
                }
                if (!player.isMini()) {
                    accounts--;
                }
            }
            if (accounts == 1) {
                player.getPacketSender().sendMessage("You already have an account attacking Golden Great Ape!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

//        if (interact.getId() == 9017) {
//            if (player.getPointsHandler().getANGELKILLCount() < 50) {
//                player.sendMessage("@blu@You need to complete at least 50 Champion Raids to attack this NPC @bla@| @red@" + player.getPointsHandler().getANGELKILLCount() + "/50");
//                return;
//            }
//            player.getCombatBuilder().attack(interact);
//        }

        if (!player.isMini()) {
            if(player.getRank().isDeveloper()) {
                for (NpcRequirements req : NpcRequirements.values()) {
                    if (interact.getId() == req.getNpcId()) {
                        if (player.getSlayer().getSlayerTask().getNpcId() == interact.getId() && interact.getId() != 4972 && interact.getId() != 2949 && interact.getId() != 6430 && interact.getId() != 9012
                                && interact.getId() != 4540 && interact.getId() != 1234 && interact.getId() != 440 && interact.getId() != 438 && interact.getId() != 12843 && interact.getId() != 449 && interact.getId() != 452
                                && interact.getId() != 252 && interact.getId() != 187) {
                            //player.sendMessage("This can only be attacked whilst he his your assigned boss slayer task.");
                            player.getCombatBuilder().attack(interact);
                        } else if(player.getInstance() != null) {
                            if (player.getSlayer().getSlayerTask().getNpcId() == interact.getId()){
                                player.getCombatBuilder().attack(interact);
                            }
                        } else if (req.getKillCount() > 0) {
                            if (player.getPointsHandler().getNPCKILLCount() < req.getKillCount()) {
                                player.sendMessage("You need atleast " + req.getKillCount() + " NPC kills to attack this. (" + player.getPointsHandler().getNPCKILLCount() + "/"
                                        + req.getKillCount() + ")");
                                return;
                            }
                        } else {
                            int npc = req.getRequireNpcId();
                            int total = KillsTracker.getTotalKillsForNpc(npc, player);
                            if (total < req.getAmountRequired()) {
                                player.sendMessage("You need atleast " + req.getAmountRequired() + " "
                                        + NpcDefinition.forId(npc).getName() + " kills to attack this. (" + total + "/"
                                        + req.getAmountRequired() + ")");
                                return;
                            }
                        }
                        break;
                    }
                }
            }
      }


        player.getCombatBuilder().attack(interact);
             if (player.getMinimeSystem().getMiniMe() != null) {
                 player.getMinimeSystem().getMiniMe().getCombatBuilder().attack(interact);
             }

    }// this is on the attack option btw. i wasnt able to make it work on "show drop"
    // ill pay you

    public void handleSecondClick(Player player, Packet packet) {
        int index = packet.readLEShortA();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;
        player.setEntityInteraction(npc);
        final int npcId = npc.getId();
        if (player.getRank().isDeveloper())
            player.getPacketSender().sendMessage("Second click npc id: " + npcId);
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (!player.getControllerManager().processNPCClick2(npc)) {
                    return;
                }
                if (PickpocketData.forNpc(npc.getId()) != null) {
                    Pickpocket.handleNpc(player, npc);
                    return;
                }
                switch (npc.getId()) {
                    case 289 -> //DAILY TASK
                            DailyTasks.claimReward(player);
                    //case 568 -> ShopManager.getShops().get(207).open(player);
                    //case 845 -> DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                    case 8459 -> Decanting.notedDecanting(player);
                    case 788 -> {
                        player.getPacketSender().sendEnterInputPrompt(
                                "How many would you like to buy (1 Lottery ticket costs 1 Billion)");
                        player.setInputHandling(new EnterLotteryTicketAmount());
                    }
//                    case 3306, 130 -> {
//                        player.getPacketSender().sendMessage("<col=255>You currently have "
//                                + player.getPointsHandler().getEventPoints() + " Event points!");
//                        ShopManager.getShops().get(81).open(player);
//                    }
//                    case 5382 -> {
//                        if (player.getMode() instanceof UltimateIronman) {
//                            UltimateIronmanHandler.handleQuickStore(player);
//                        } else {
//                            DialogueManager.start(player, 195);
//                        }
//                        player.getClickDelay().reset();
//                    }
//                    case 4601 -> {
//                        ShopManager.getShops().get(110).open(player);
//                        player.getPacketSender().sendString(3903,
//                                "Success rate: @whi@" + player.getPointsHandler().getLoyaltyPoints() + "55%");
//                        player.getPacketSender().sendMessage("").sendMessage(
//                                "You currently have " + player.getPointsHandler().getLoyaltyPoints() + " Loyalty Points.");
//                        ;
//                    }
                    case 1394 -> {
                        int[] items = {1053, 1057, 1055, 1038, 1040, 1042, 1044, 1046, 1048, 1050, 14008, 14009, 14010,
                                14484, 19115, 19114, 13736, 13744, 13738, 13742, 13740, 6293, 18754, 11694, 11696, 11698, 11700,
                                15018, 15019, 15020, 15220, 12601, 12603, 12605, 20000, 20001, 20002, 6769, 10942, 10934,
                                455};
                        player.getPacketSender().sendInterface(52300);
                        for (int i = 0; i < items.length; i++)
                            player.getPacketSender().sendItemOnInterface(52302, items[i], 1, i);
                    }
//                    case 4653 -> {
//                        player.getPacketSender().sendInterfaceRemoval();
//                        ShopManager.getShops().get(85).open(player);
//                    }
                    case 736 -> npc.forceChat("Thanx for the follow :)");
                    case 1837 -> {
                        player.getPacketSender().sendInterfaceRemoval();
                        if (player.getInventory().getAmount(11180) < 1) {
                            player.getPacketSender().sendMessage("You do not have enough tokens.");
                            return;
                        } else
                            player.getInventory().delete(11180, 1);
                        // So we grab the players pID too determine what Z they will be getting. Not
                        // sure how kraken handles it, but this is how we'll handle it.
                        player.moveTo(new Position(3025, 5231));
                        // player.getPacketSender().sendMessage("Index: " + player.getIndex());
                        // player.getPacketSender().sendMessage("Z: " + player.getIndex() * 4);
                        player.getPacketSender().sendMessage("Teleporting to Trio...");
                        player.getPacketSender()
                                .sendMessage("@red@Warning:@bla@ you @red@will@bla@ lose your items on death here!");
                        // Will sumbit a task to handle token remove, once they leave the minigame the
                        // task will be removed.
                        // trioMinigame.failsafe(player);
                        // trioMinigame.handleNPCSpawning(player);
                        trioMinigame.handleTokenRemoval(player);
                    }
                    case 3777 -> {
                        ShopHandler.getShop(1).ifPresent(shop -> shop.send(player, true));
                    }
                    case 13738 -> player.getUpgradeHandler().openInterface();
                    case 457 -> {
                        player.getPacketSender().sendMessage("The ghost teleports you away.");
                        player.getPacketSender().sendInterfaceRemoval();
                        player.moveTo(new Position(3651, 3486));
                    }
                    //case 2622 -> ShopManager.getShops().get(43).open(player);
                    case 462 -> {
                        npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                        npc.forceChat("Off you go!");
                        TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                                player.getSpellbook().getTeleportType());
                    }
//                    case 3101 -> {
//                        DialogueManager.start(player, 95);
//                        player.setDialogueActionId(57);
//                    }
                    //case 7969 -> ShopManager.getShops().get(28).open(player);
                    case 605 -> {
                        ShopHandler.getShop(3).ifPresent(shop -> shop.send(player, true));
                    }
//                    case 1597 -> {
//                        if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.EASY_SLAYER)
//                                && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.EASY_SLAYER);
//                        }
//                        if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.EASY_SLAYER)) {
//
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.EASY_SLAYER);
////                            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
////                                player.getSlayer().assignTask();
////
////                            else
////                                DialogueManager.start(player, SlayerDialogues.findAssignment(player));
//                        } else {
//                            SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                            SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                            String yourMastersName = "";
//                            String thisMasterName = "";
//                            int reqSlayer = 0;
//                            if (yourMaster != null) {
//                                yourMastersName = yourMaster.getSlayerMasterName();
//                            }
//                            if (thisMaster != null) {
//                                reqSlayer = thisMaster.getSlayerReq();
//                                thisMasterName = thisMaster.getSlayerMasterName();
//                            }
//                            if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                                DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName + ".");
//                            } else {
//                                DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                            }
//                        }
//                    }
//                    case 8275 -> {
//                        if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.MEDIUM_SLAYER)
//                                && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.MEDIUM_SLAYER);
//                        }
//                        if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.MEDIUM_SLAYER)) {
//
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.MEDIUM_SLAYER);
////                            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
////                                player.getSlayer().assignTask();
////
////                            else
////                                DialogueManager.start(player, SlayerDialogues.findAssignment(player));
//                        } else {
//                            SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                            SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                            String yourMastersName = "";
//                            String thisMasterName = "";
//                            int reqSlayer = 0;
//                            if (yourMaster != null) {
//                                yourMastersName = yourMaster.getSlayerMasterName();
//                            }
//                            if (thisMaster != null) {
//                                reqSlayer = thisMaster.getSlayerReq();
//                                thisMasterName = thisMaster.getSlayerMasterName();
//                            }
//                            if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                                DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName + ".");
//                            } else {
//                                DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                            }
//                        }
//                    }
//                    case 9085 -> {
//                        if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.HARD_SLAYER)
//                                && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.HARD_SLAYER);
//                        }
//                        if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.HARD_SLAYER)) {
//
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.HARD_SLAYER);
//                            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
//                                player.getSlayer().assignTask();
//
//                            else
//                                DialogueManager.start(player, SlayerDialogues.findAssignment(player));
//                        } else {
//                            SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                            SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                            String yourMastersName = "";
//                            String thisMasterName = "";
//                            int reqSlayer = 0;
//                            if (yourMaster != null) {
//                                yourMastersName = yourMaster.getSlayerMasterName();
//                            }
//                            if (thisMaster != null) {
//                                reqSlayer = thisMaster.getSlayerReq();
//                                thisMasterName = thisMaster.getSlayerMasterName();
//                            }
//                            if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                                DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName + ".");
//                            } else {
//                                DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                            }
//                        }
//                    }
//                    case 925 -> {
//                        if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.ELITE_SLAYER)
//                                && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.ELITE_SLAYER);
//                        }
//                        if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.ELITE_SLAYER)) {
//
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.ELITE_SLAYER);
//                            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
//                                player.getSlayer().assignTask();
//
//                            else
//                                DialogueManager.start(player, SlayerDialogues.findAssignment(player));
//                        } else {
//                            SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                            SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                            String yourMastersName = "";
//                            String thisMasterName = "";
//                            int reqSlayer = 0;
//                            if (yourMaster != null) {
//                                yourMastersName = yourMaster.getSlayerMasterName();
//                            }
//                            if (thisMaster != null) {
//                                reqSlayer = thisMaster.getSlayerReq();
//                                thisMasterName = thisMaster.getSlayerMasterName();
//                            }
//                            if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                                DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName + ".");
//                            } else {
//                                DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                            }
//                        }
//                    }
//                    case 9000 -> {//second click
//
//                        if (!player.getSlayer().getSlayerMaster().equals(SlayerMaster.BOSS_SLAYER)
//                                && player.getSlayer().getSlayerTask().equals(SlayerTasks.NO_TASK)) {
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.BOSS_SLAYER);
//                        }
//                        if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.BOSS_SLAYER)) {
//
//                            SlayerMaster.changeSlayerMaster(player, SlayerMaster.BOSS_SLAYER);
//                            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
//                                player.getSlayer().assignTask();
//
//                            else
//                                DialogueManager.start(player, BossSlayerDialogues.findAssignment(player));
//                        } else {
//                            SlayerMaster yourMaster = player.getSlayer().getSlayerMaster();
//                            SlayerMaster thisMaster = SlayerMaster.forNpcId(npc.getId());
//                            String yourMastersName = "";
//                            String thisMasterName = "";
//                            int reqSlayer = 0;
//                            if (yourMaster != null) {
//                                yourMastersName = yourMaster.getSlayerMasterName();
//                            }
//                            if (thisMaster != null) {
//                                reqSlayer = thisMaster.getSlayerReq();
//                                thisMasterName = thisMaster.getSlayerMasterName();
//                            }
//                            if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < reqSlayer) {
//                                DialogueManager.sendStatement(player, "You need " + reqSlayer + " Slayer to use " + thisMasterName + ".");
//                            } else {
//                                DialogueManager.sendStatement(player, "You currently have an assignment with " + yourMastersName);
//                            }
//                        }
//                    }
//                    case 8591 -> {
//                        if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
//                            player.getPacketSender()
//                                    .sendMessage("You must complete Nomad's quest before being able to use this shop.");
//                            return;
//                        }
//                        ShopManager.getShops().get(37).open(player);
//                    }
                    case 805 -> Tanning.selectionInterface(player);
                    case 318, 316, 313, 312, 5748, 2067 -> {
                        player.setEntityInteraction(npc);
                        Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
                    }
//                    case 4946 -> ShopManager.getShops().get(15).open(player);
//                    case 946 -> ShopManager.getShops().get(1).open(player);
//                    case 961 -> ShopManager.getShops().get(6).open(player);
//                    case 1861 -> ShopManager.getShops().get(3).open(player);
//                    case 705 -> ShopManager.getShops().get(4).open(player);
//                    case 2253 -> ShopManager.getShops().get(9).open(player);
                    case 6970 -> {
                        player.setDialogueActionId(35);
                        //DialogueManager.start(player, 63);
                    }

                    // begin ironman second click handles

                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    public void handleThirdClick(Player player, Packet packet) {
        int index = packet.readShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;
        player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
        npc.setPositionToFace(player.getPosition());
        if (player.getRank().isDeveloper())
            player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (!player.getControllerManager().processNPCClick3(npc)) {
                    return;
                }
                switch (npc.getId()) {
                    case 289 -> DailyTasks.cancelTask(player);
//                    case 552 -> ShopManager.getShops().get(115).open(player);
//                    case 9000 -> {
//                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 90) {
//                            DialogueManager.sendStatement(player, "You need a Slayer Level of 90 to access this shop.");
//                            return;
//                        }
//                        ShopManager.getShops().get(107).open(player);
//                    }
                    case 788 ->
                            player.sendMessage("@bla@There are currently @red@" + LotterySystem.getCurrentTicketAmount()
                                    + " @bla@Lottery tickets- Winner pot is@red@: " + LotterySystem.getTotalPrizepool()
                                    + "@bla@ Tokens");
                    case 5382 -> {
                        if (player.getMode() instanceof UltimateIronman) {
                            UltimateIronmanHandler.handleQuickRetrieve(player);
                        } else {
                           // DialogueManager.start(player, 195);
                        }
                        player.getClickDelay().reset();
                    }
                    case 4653 -> player.getPacketSender()
                            .sendMessage("Unfortunately, ship charters are still being established. Check back soon.");
                    case 736 -> player.forceChat("Nah. I don't want to feed the cancer.");
//                    case 5604 -> {
//                        ShopManager.getShops().get(102).open(player);
//                        player.sendMessage(
//                                "<img=99>You have @red@" + player.getPointsHandler().getBossPoints() + " Boss Points!");
//                    }
                    case 3777 -> ShopHandler.getShop(1).ifPresent(shop -> shop.send(player, true));
                    case 13738 -> player.getUpgradeHandler().openInterface();
                   // case 3101 -> ShopManager.getShops().get(42).open(player);
//                    case 1597, 8275, 9085 -> {
//                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 80) {
//                            DialogueManager.sendStatement(player, "You need a Slayer Level of 80 to access this shop.");
//                        }
//                        ShopManager.getShops().get(40).open(player);
//                    }
//                    case 946 -> ShopManager.getShops().get(0).open(player);
//                    case 1861 -> ShopManager.getShops().get(2).open(player);

                    // case 597:
                    // ShopManager.getShops().get(54).open(player);
                    // break;
                    case 961 -> {
                        if (!player.getDonator().isMember()) {
                            player.getPacketSender().sendMessage("This feature is currently only available for members.");
                            return;
                        }
                        boolean restore = player.getSpecialPercentage() < 100;
                        if (restore) {
                            player.setSpecialPercentage(100);
                            CombatSpecial.updateBar(player);
                            player.getPacketSender().sendMessage("Your special attack energy has been restored.");
                        }
                        for (Skill skill : Skill.values()) {
                            if (player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager()
                                    .getMaxLevel(skill)) {
                                player.getSkillManager().setCurrentLevel(skill,
                                        player.getSkillManager().getMaxLevel(skill));
                                restore = true;
                            }
                        }
                        if (restore) {
                            player.performGraphic(new Graphic(1302));
                            player.getPacketSender().sendMessage("Your stats have been restored.");
                        } else
                            player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
                    }
                   // case 705 -> ShopManager.getShops().get(5).open(player);
                    case 605 -> player.getPacketSender().sendMessage("Coming soon!");

                    // player.getPacketSender().sendMessage("").sendMessage("You currently have
                    // "+player.getPointsHandler().getVotingPoints()+" Voting
                    // points.").sendMessage("You can earn points and coins by voting. To do so,
                    // simply use the ::vote command.");;
                    // ShopManager.getShops().get(90).open(player);
//                    case 2253 -> ShopManager.getShops().get(10).open(player);
//                    case 5913 -> ShopManager.getShops().get(0).open(player);
                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    public void handleFourthClick(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;

       
        if (npc.getDefinition().isAttackable()) {
            DropsInterface.open(player);
            DropsInterface.buildRightSide(player, npc.getId());
            return;
        }
        if (BossPets.pickup(player, npc)) { // done in ur NPCDef just change pick up option index from 1 to 3 and ur
            // fine (or if it was 0 before change to 3 )
            player.getMovementQueue().reset();
            return;
        }
        player.setEntityInteraction(npc);
        if (player.getRank().isDeveloper())
            player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (!player.getControllerManager().processNPCClick4(npc)) {
                    return;
                }
                switch (npc.getId()) {
//                    case 961 -> ShopManager.getShops().get(118).open(player);
//                    case 946 -> ShopManager.getShops().get(82).open(player);
                    case 3777 -> {
                        // ShopManager.getShops().get(24).open(player); //DONATOR SHOP 3 HERE
                        player.sendMessage("<shad=1>@yel@<img=14>Please check out the donation deals in our ::Discord - #Donation-deals");
                        player.sendMessage("<shad=1>@yel@<img=14>Please check out the donation deals in our ::Discord - #Donation-deals");
                    }
                    case 13738 -> player.getUpgradeHandler().openInterface();
//                    case 705 -> ShopManager.getShops().get(7).open(player);
//                    case 2253 -> ShopManager.getShops().get(8).open(player);
                    case 605 -> LoyaltyProgramme.open(player);
                    case 4601 -> LoyaltyProgramme.open(player);
//                    case 1597 -> ShopManager.getShops().get(47).open(player);
//                    case 9085 -> {
//                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 80) {
//                            DialogueManager.sendStatement(player, "You need a Slayer Level of 80 to access this shop.");
//                            return;
//                        }
//                        ShopManager.getShops().get(472).open(player);
//                    }
//                    case 925 -> ShopManager.getShops().get(473).open(player);
//                    case 8275 -> {
//                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 60) {
//                            DialogueManager.sendStatement(player, "You need a Slayer Level of 60 to access this shop.");
//                            return;
//                        }
//                        ShopManager.getShops().get(471).open(player);
//                    }
                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
                // DropsInterface.open(player);
                // DropsInterface.getList(NpcDefinition.getDefinitions().getClass().getName());

            }
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
            return;
        player.afkTicks = 0;
        player.afk = false;
        switch (packet.getOpcode()) {
            case ATTACK_NPC:
                attackNPC(player, packet);
                break;
            case FIRST_CLICK_OPCODE:
                firstClick(player, packet);
                break;
            case SECOND_CLICK_OPCODE:
                handleSecondClick(player, packet);
                break;
            case THIRD_CLICK_OPCODE:
                handleThirdClick(player, packet);
                break;
            case FOURTH_CLICK_OPCODE:
                handleFourthClick(player, packet);
                break;
            case MAGE_NPC:
                int npcIndex = packet.readLEShortA();
                int spellId = packet.readShortA();

                if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
                    return;
                }

                NPC n = World.getNpcs().get(npcIndex);
                player.setEntityInteraction(n);

                if (n != null && player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("Used spell id: " + spellId + " on npc: " + n.getId());
                }

                CombatSpell spell = CombatSpells.getSpell(spellId);

                if (n == null || spell == null) {
                    player.getMovementQueue().reset();
                    return;
                }

                if (!NpcDefinition.definitions[n.getId()].isAttackable()) {
                    player.getMovementQueue().reset();
                    return;
                }

                if (n.getConstitution() <= 0) {
                    player.getMovementQueue().reset();
                    return;
                }



                player.setPositionToFace(n.getPosition());
                player.setCastSpell(spell);
                if (player.getCombatBuilder().getStrategy() == null) {
                    player.getCombatBuilder().determineStrategy();
                }
                if (CombatFactory.checkAttackDistance(player, n)) {
                    player.getMovementQueue().reset();
                }
                player.getCombatBuilder().resetCooldown();
                player.getCombatBuilder().attack(n);
                break;
        }
    }

    public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 186, MAGE_NPC = 151, SECOND_CLICK_OPCODE = 31,
            THIRD_CLICK_OPCODE = 171, FOURTH_CLICK_OPCODE = 28;
}
