//package com.ruse.world.content.aura;
//
//import com.ruse.engine.task.Task;
//import com.ruse.engine.task.TaskManager;
//import com.ruse.model.*;
//import com.ruse.model.definitions.ItemDefinition;
//import com.ruse.util.Misc;
//import com.ruse.world.World;
//import com.ruse.world.content.BonusManager;
//import com.ruse.world.content.boxes.AuraRaidLoot;
//import com.ruse.world.packages.packs.casket.Box;
//import com.ruse.world.packages.combat.prayer.CurseHandler;
//import com.ruse.world.packages.combat.prayer.PrayerHandler;
//import com.ruse.world.entity.impl.npc.NPC;
//import com.ruse.world.entity.impl.player.Player;
//
//import java.util.ArrayList;
//
//public class AuraRaids {
//
//    public static Box[] loot = {
//            //herblore
//            new Box(777, 1,2, 1D, false),
//            new Box(783, 1,2, 1D, false),
//            new Box(789, 1,2, 1D, false),
//            new Box(795, 1,2, 1D, false),
//            new Box(815, 1,2, 1D, false),
//            new Box(913, 1,2, 1D, false),
//            new Box(916, 1,2, 1D, false),
//            new Box(919, 1,2, 1D, false),
//            new Box(922, 1,2, 1D, false),
//            new Box(925, 1,2, 1D, false),
//            new Box(928, 1,2, 1D, false),
//            new Box(20509, 1, 1D, false),
//            new Box(931, 1, 0.7D, false),
//            new Box(942, 1, 0.7D, false),
//            new Box(943, 1, 0.7D, false),
//            new Box(945, 1, 0.7D, false),
//            new Box(934, 1, 0.6D, false),
//            new Box(937, 1, 0.5D, false),
//            new Box(940, 1, 0.4D, false),
//            new Box(6953, 1, 0.3D, false),
//            new Box(588, 1,8, 0.3D, false),
//            new Box(13754, 1, 0.3D, false),
//            new Box(731, 1, 0.3D, false),
//            new Box(8212, 3,17, 0.7D, false),
//            //armour
//            new Box(4022, 1, 0.2D, true),
//            new Box(4024, 1, 0.2D, true),
//            new Box(4026, 1, 0.2D, true),
//            new Box(4028, 1, 0.2D, true),
//            new Box(4030, 1, 0.2D, true),
//            new Box(4032, 1, 0.2D, true),
//            new Box(4034, 1, 0.2D, true),
//            new Box(22215, 1, 0.1D, true),
//            new Box(22228, 1, 0.1D, true),
//    };
//
//    public static void start(AuraParty party) {
//
//        Player p = party.getOwner();
//        p.getPacketSender().sendInterfaceRemoval();
//
//        if (party.hasEnteredRaids()) {
//            p.getPacketSender().sendMessage("your party is already in a raids!");
//            return;
//        }
//
//        if (party.getOwner() != p) {
//            p.getPacketSender().sendMessage("Only the party leader can start the fight.");
//            return;
//        }
//
//        for (Player member : party.getPlayers()) {
//            if (member != null) {
//                member.getPacketSender().sendInterfaceRemoval();
//                if (member.getSummoning().getFamiliar() != null) {
//                    member.getPacketSender()
//                            .sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
//                    p.getPacketSender().sendMessage(
//                            "" + p.getUsername() + " has to dismiss their familiar before you can enter the dungeon.");
//                    return;
//                }
//            }
//            if (member != null) {
//                if (member.getBonusManager().getExtraBonus()[BonusManager.DEFENCE_SUMMONING] > 500) {
//                    member.sendMessage("@red@This raid is restricted to players with an ELO of 500 or less. [Use ::elo]");
//                    p.getPacketSender().sendMessage(
//                            "@red@ " + member.getUsername() + "'s ELO is higher than 145.");
//                    return;
//                }
//            }
//            if (member != null) {
////                if (KillsTracker.getTotalKillsForNpc(9914, member) < 1000) {
////                        //member.sendMessage("@red@Your Sasuke KC is too low to participate. " + NpcRequirements.SASUKE.getKillCount() + "/1000");
////                        p.getPacketSender().sendMessage(
////                                "@red@ " + member.getUsername() + "'s Sasuke KC is lower than 1000. ");
////                        return;
////                    }
//            }
//        }
//        party.enteredDungeon(true);
//        final int height = p.getIndex() * 4;
//        World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.AURA, height, null));
//
//        for (Player member : party.getPlayers()) {
//            member.setAutoRetaliate(false);
//            member.getPacketSender().sendInterfaceRemoval();
//            member.setRegionInstance(null);
//            member.getMovementQueue().reset();
//            member.getClickDelay().reset();
//            member.moveTo(new Position(2653, 3044 , height));
//            PrayerHandler.deactivateAll(member);
//            CurseHandler.deactivateAll(member);
//            TaskManager.submit(new Task(2, false) {
//
//                @Override
//                public void execute() {
//                   PrayerHandler.deactivateAll(member);
//                   CurseHandler.deactivateAll(member);
//                    stop();
//                }
//            });
//            member.getSkillManager().stopSkilling();
//            member.getPacketSender().sendClientRightClickRemoval();
//            member.getPacketSender().sendCameraNeutrality();
//            member.setInsideRaids(false);
//            member.setEnteredAuraRaids(false);
//            //member.getPacketSender().sendFade(25, 50, 50);
//            member.setAuraParty(party);
//        }
//        party.setDeathCount(0);
//        party.setKills(0);
//        party.sendMessage("@red@Welcome to Champion Raids");
//        party.setCurrentPhase(1);
//        party.setHeight(height);
//        party.startRaid();
//
//    }
//
//    public static NPC addNpc(AuraParty party, int npcId, double mult) {
//        NPC npc = (new NPC(npcId, new Position(2642 + Misc.getRandom(7), 3043 + Misc.getRandom(8), party.getHeight())));
//        npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        return npc;
//    }
//
//    public static void firstWave(AuraParty party) {
//        ArrayList<NPC> npcs = new ArrayList<NPC>();
//        double mult = 7500000;
//
//        NPC npc = new NPC(AuraRaidData.firstWaveNpc, new Position(2649, 3039, party.getHeight()));
//        NPC npc2 = new NPC(AuraRaidData.firstWaveNpc, new Position(2643, 3039, party.getHeight()));
//        NPC npc3 = new NPC(AuraRaidData.firstWaveNpc, new Position(2643, 3049, party.getHeight()));
//        NPC npc4 = new NPC(AuraRaidData.firstWaveNpc, new Position(2649, 3049, party.getHeight()));
//        //npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        //npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npcs.add(npc);
//        npcs.add(npc2);
//        npcs.add(npc3);
//        npcs.add(npc4);
//        npc.getMovementQueue().freeze(1500000);
//        npc2.getMovementQueue().freeze(1500000);
//        npc3.getMovementQueue().freeze(1500000);
//        npc4.getMovementQueue().freeze(1500000);
//        TaskManager.submit(new Task(10, false) {
//
//            @Override
//            public void execute() {
//                startTask(party, npcs, 1);
//                stop();
//            }
//        });
//    }
//
//    public static void secondWave(AuraParty party) {
//
//        ArrayList<NPC> npcs = new ArrayList<NPC>();
//        double mult = 7500000;
//
//        NPC npc = new NPC(AuraRaidData.secondWaveNpc, new Position(2649, 3039, party.getHeight()));
//        NPC npc2 = new NPC(AuraRaidData.secondWaveNpc, new Position(2643, 3039, party.getHeight()));
//        NPC npc3 = new NPC(AuraRaidData.secondWaveNpc, new Position(2643, 3049, party.getHeight()));
//        NPC npc4 = new NPC(AuraRaidData.secondWaveNpc, new Position(2649, 3049, party.getHeight()));
//        //npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        //npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npcs.add(npc);
//        npcs.add(npc2);
//        npcs.add(npc3);
//        npcs.add(npc4);
//        npc.getMovementQueue().freeze(1500000);
//        npc2.getMovementQueue().freeze(1500000);
//        npc3.getMovementQueue().freeze(1500000);
//        npc4.getMovementQueue().freeze(1500000);
//        TaskManager.submit(new Task(10, false) {
//
//            @Override
//            public void execute() {
//                startTask(party, npcs, 2);
//                stop();
//            }
//        });
//    }
//
//    public static void thirdWave(AuraParty party) {
//
//        ArrayList<NPC> npcs = new ArrayList<NPC>();
//        double mult = 7500000;
//
//        NPC npc = new NPC(AuraRaidData.thirdWaveNpc, new Position(2649, 3039, party.getHeight()));
//        NPC npc2 = new NPC(AuraRaidData.thirdWaveNpc, new Position(2643, 3039, party.getHeight()));
//        NPC npc3 = new NPC(AuraRaidData.thirdWaveNpc, new Position(2643, 3049, party.getHeight()));
//        NPC npc4 = new NPC(AuraRaidData.thirdWaveNpc, new Position(2649, 3049, party.getHeight()));
//       // npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        //npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npcs.add(npc);
//        npcs.add(npc2);
//        npcs.add(npc3);
//        npcs.add(npc4);
//        npc.getMovementQueue().freeze(1500000);
//        npc2.getMovementQueue().freeze(1500000);
//        npc3.getMovementQueue().freeze(1500000);
//        npc4.getMovementQueue().freeze(1500000);
//        TaskManager.submit(new Task(10, false) {
//
//            @Override
//            public void execute() {
//                startTask(party, npcs, 3);
//                stop();
//            }
//        });
//    }
//
//    public static void fourthWave(AuraParty party) {
//
//        ArrayList<NPC> npcs = new ArrayList<NPC>();
//        double mult = 15000000;
//
//        NPC npc = new NPC(AuraRaidData.fourthWaveNpc, new Position(2649, 3039, party.getHeight()));
//        NPC npc2 = new NPC(AuraRaidData.fourthWaveNpc, new Position(2643, 3039, party.getHeight()));
//        NPC npc3 = new NPC(AuraRaidData.fourthWaveNpc, new Position(2643, 3049, party.getHeight()));
//        NPC npc4 = new NPC(AuraRaidData.fourthWaveNpc, new Position(2649, 3049, party.getHeight()));
//       // npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//       // npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npcs.add(npc);
//        npcs.add(npc2);
//        npcs.add(npc3);
//        npcs.add(npc4);
//        npc.getMovementQueue().freeze(1500000);
//        npc2.getMovementQueue().freeze(1500000);
//        npc3.getMovementQueue().freeze(1500000);
//        npc4.getMovementQueue().freeze(1500000);
//        TaskManager.submit(new Task(10, false) {
//
//            @Override
//            public void execute() {
//                startTask(party, npcs, 4);
//                stop();
//            }
//        });
//    }
//
//    public static void fifthWave(AuraParty party) {
//
//        ArrayList<NPC> npcs = new ArrayList<NPC>();
//
//        double mult = 100000;
//
//        NPC npc = new NPC(AuraRaidData.fifthWaveNpc, new Position(2641, 3044, party.getHeight()));
//        //npc.setDefaultConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        //npc.setConstitution((int) (npc.getConstitution() + (party.getPlayers().size() * mult)));
//        npcs.add(npc);
//        npc.getMovementQueue().freeze(1500000);
//
//        TaskManager.submit(new Task(10, false) {
//
//            @Override
//            public void execute() {
//                startTask(party, npcs, 5);
//                stop();
//            }
//        });
//    }
//
//    public static void startTask(AuraParty party, ArrayList<NPC> npcs, int wave) {
//        TaskManager.submit(new Task(1, false) {
//
//            @Override
//            public void execute() {
//
//                TaskManager.submit(new Task(1, false) {
//                    int tick = 0;
//
//                    @Override
//                    protected void execute() {
//                        if ((party.getOwner().getLocation() != Locations.Location.AURA)
//                                || party.getPlayers().size() <= 0) {
//                            party.sendMessage("@red@Your party has failed to defeat the Champion Raids");
//                            destroyInstance(party);
//                            stop();
//
//
//                        }
//
//                        if (!party.hasEnteredRaids())
//                            stop();
//
//                        int count = 0;
//                        for (NPC npc : npcs) {
//                            if (npc.getConstitution() <= 0)
//                                count++;
//                        }
//                        if (count >= npcs.size()) {
//                            if (wave == 1)
//                                secondWave(party);
//                            else if (wave == 2)
//                                thirdWave(party);
//                            else if (wave == 3)
//                                fourthWave(party);
//                            else if (wave == 4)
//                                fifthWave(party);
//                            else
//                                finishRaid(party);
//                            stop();
//                        }
//
//                        if (tick == 4) {
//                            if (wave == 1)
//                                party.sendMessage("@red@The Champion Raids have started!");
//                            else if (wave == 2)
//                                party.sendMessage("@red@The second wave has started!");
//                            else if (wave == 3)
//                                party.sendMessage("@red@The third wave has started!");
//                            else if (wave == 4)
//                                party.sendMessage("@red@The fourth wave has started!");
//
//                            for (NPC npc : npcs) {
//                                spawnNpc(party, npc);
//                            }
//                        }
//
//                        tick++;
//                    }
//
//                });
//
//                stop();
//            }
//        });
//
//    }
//
//    public static void spawnNpc(AuraParty party, NPC npc) {
//        World.register(npc);
//        Player player = randomPlayer(party);
//        npc.getMovementQueue().setFollowCharacter(player);
//        npc.getCombatBuilder().attack(player);
//        npc.getCombatBuilder().attack(player);
//    }
//
//    public static void handleDeath(AuraParty party, Player player) {
//        party.getPlayers().remove(player);
//        party.remove(player, true);
//        player.sendMessage("@red@You died and were removed from the raid party.");
//    }
//
//    public static Player randomPlayer(AuraParty party) {
//        return party.getPlayers().get(Misc.getRandom(party.getPlayers().size() - 1));
//    }
//
//    public static void finishRaid(AuraParty party) {
//        party.enteredDungeon(false);
//
//
//
//        TaskManager.submit(new Task(3, false) {
//
//            @Override
//            public void execute() {
//                party.sendMessage("@red@Your party has defeated the Champion Raids");
//
//                for (Player player : party.getPlayers()) {
//                    Box[] loot = AuraRaidLoot.LOOT;
//
//                    player.getPointsHandler().incrementANGELKILLCount(1);
//
//                    Box drop = getLoot(loot, party.getPlayers().size());
//
//                    if (drop.isAnnounce()) {
//                        String message = "<img=5> @blu@News: @red@" + player.getUsername() + " @blu@has just received @red@"
//                                + ItemDefinition.forId(drop.getId()).getName() + "@blu@ from @red@Champion Raids";
//                        World.sendFilterMessage(message);
//                    }
//
//                    double amt = drop.getMin() + Misc.getRandom(drop.getMax() - drop.getMin());
//
//                    player.getInventory().add(new Item(drop.getId(), (int) amt));
//                    if (drop.getId() == 20509) {
//                        player.getInventory().delete(20509, 1);
//                        Item item1 = new Item(20504);
//                        player.getInventory().add(item1);
//                    }
//                    player.getInventory().add(10835, Misc.getRandom(712, 9764));
//                    player.sendMessage("<shad=1>@yel@You have receieved " + (int) amt + " "+ ItemDefinition.forId(drop.getId()).getName() + " from this raid!" );
//                }
//                party.moveTo(new Position(2697, 2646 , 0));
//                //party.sendMessage("@red@Use the portal south to leave the island.");
//
//                party.setDeathCount(0);
//                party.setKills(0);
//                party.setCurrentPhase(1);
//                //party.getPlayers().clear();
//
//                stop();
//            }
//        });
//    }
//
//    public static Box getLoot(Box[] loot, int size) {
//
//        Box[] possibleDrops = new Box[loot.length];
//        int possibleDropsCount = 0;
//        for (Box drop : loot) {
//            if (Misc.getRandom(500 * size) <= drop.getRate()) {
//                possibleDrops[possibleDropsCount++] = drop;
//            }
//        }
//
//        if (possibleDropsCount > 0) {
//            return possibleDrops[Misc.getRandom((possibleDropsCount - 1))];
//        } else {
//            return loot[Misc.getRandom((possibleDropsCount - 1))];
//        }
//    }
//
//    public static void destroyInstance(AuraParty party) {
//
//        for (Player member : party.getPlayers()) {
//            member.setEnteredAuraRaids(false);
//        }
//        party.moveTo(AuraRaidData.lobbyPosition);
//        party.enteredDungeon(false);
//        party.getPlayers().clear();
//
//        for (NPC npc : party.getNpcs()) {
//            if (npc != null && npc.getPosition().getZ() == party.getHeight())
//                World.deregister(npc);
//        }
//    }
//
//}
