package com.ruse.world.content.tbdminigame;

import com.google.common.collect.ImmutableMap;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.npc.NPCMovementCoordinator;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.DialogueType;

import java.util.*;

public class Game {

    private static final Position START_POSITION = new Position(2498,3771,4);

    private static final Random random = new Random();

    private static final int BASE_ESSENCE_ITEM_ID = 15430;
    public static int NPC_MIN_DAMAGE = 3;
    public static int NPC_MAX_DAMAGE = 6;

    int obeliskMaxHealth = 10000;
    private int deathCount;

    public static int[] npcIds = {
            1508,
            1511,
            4470,
            1812
    };

    private final Set<Player> playerSet;

    private final ImmutableMap<TreeEssenceType, EvilTree> evilTrees = new ImmutableMap.Builder<TreeEssenceType, EvilTree>()
            .put(TreeEssenceType.ELDER, new EvilTree(new GameObject(11922, new Position(2495, 3786, 4), 10, 4), "Elder", 9, 0))
            .put(TreeEssenceType.MAGIC, new EvilTree(new GameObject(11919, new Position(2476, 3766, 4), 10, 3), "Magic", 9, 1))
            .put(TreeEssenceType.YEW, new EvilTree(new GameObject(11916, new Position(2495, 3747, 4), 10, 2), "Yew", 9, 2))
            .put(TreeEssenceType.MAPLE, new EvilTree(new GameObject(11444, new Position(2517, 3766, 4), 10, 1), "Maple", 9, 3))
            .build();

    private List<EvilTree> aliveTrees = new ArrayList<>();
    private final String[] obeliskOptions = new String[] {"Select Target", "", "", "", ""};
    private final List<NPC> npcList = new ArrayList<>();
    private final HashMap<Position, NPC> barriers = new HashMap<>();
    private final List<GroundItem> groundItemList = new ArrayList<>();

    private Task gameTask;
    int taskTicks = 0;

    private final NPC obelisk = new NPC(550, new Position(2495, 3770, 4));

    public Game(Set<Player> playerSet) {
        this.playerSet = playerSet;
        World.register(obelisk);
        obelisk.setConstitution(obeliskMaxHealth);
        obelisk.setDefaultConstitution(obeliskMaxHealth);
        obelisk.getCombatBuilder().setDidAutoRetaliate(false);
        obelisk.getMovementQueue().freeze(2000);
    }

    public void start() {
        for(EvilTree evilTree : evilTrees.values())
            evilTree.spawn();

        getAliveTrees();

        playerSet.forEach(player -> {
            player.getPacketSender().sendWalkableInterface(151100, false);
            player.moveTo(START_POSITION);
            player.getPacketSender().sendString(151109, "@gre@Elder")
                    .sendString(151110, "@gre@Magic")
                    .sendString(151111, "@gre@Yew")
                    .sendString(151112, "@gre@Maple")
                    .updateProgressBar(151113, 0)
                    .updateProgressBar(151114, 0)
                    .updateProgressBar(151115, 0)
                    .updateProgressBar(151116, 0)
                    .updateProgressBar(151119, 0)
                    .sendString(151121, obelisk.getConstitution() + "/" + obeliskMaxHealth)
                    .sendWalkableInterface(151106, true)
                    .sendMessage("@red@The game will begin in 5 seconds!");
        });

        gameTask = new Task(1) {
            @Override
            protected void execute() {
                if (taskTicks > 9) {
                    if (taskTicks == 2000 || obelisk.isDying() || obelisk.getConstitution() <= 0) {
                        end(false);
                    } else {
                        if (taskTicks % 30 == 0) {
                            spawnNpcs(TreeEssenceType.YEW);
                            spawnNpcs(TreeEssenceType.MAGIC);
                            spawnNpcs(TreeEssenceType.ELDER);
                            spawnNpcs(TreeEssenceType.MAPLE);
                        }
                        if (taskTicks % 4 == 0) {
                            for (NPC npc : npcList) {
                                if (npc == null || npc.isDying() || npc.getCombatBuilder().getVictim() instanceof Player)
                                    continue;
                                if(npc.getId() == 4470 || npc.getId() == 1812) {
                                    boolean isAttackingBarrier = false;
                                    List<NPC> toKill = new ArrayList<>();
                                    for (Map.Entry<Position, NPC> position : barriers.entrySet()) {
                                        if (position.getValue().getConstitution() <= 0) {
                                            toKill.add(position.getValue());
                                        } else if (npc.getPosition().isWithinDistance(position.getKey(), 2)) {
                                            npc.getCombatBuilder().attack(position.getValue());
                                            isAttackingBarrier = true;
                                        }
                                    }
                                    for (NPC barrier : toKill) {
                                        barrierDeath(barrier);
                                    }
                                    if (!isAttackingBarrier) npc.getCombatBuilder().attack(obelisk);
                                } else {
                                    npc.getCombatBuilder().attack(obelisk);
                                }
                            }
                            updateObeliskProgressBar();
                        }
                    }
                }
                taskTicks++;
            }
        };
        TaskManager.submit(gameTask);
    }

    public void leave(Player player, boolean early) {
        if(!playerSet.contains(player))
            return;
        player.getInventory().delete(BASE_ESSENCE_ITEM_ID, player.getInventory().getAmount(BASE_ESSENCE_ITEM_ID));
        player.moveTo(Lobby.LOBBY_EXIT_POSITION);
        player.getPacketSender().sendWalkableInterface(151106, false);
        World.deregister(obelisk);
        if(early) {
            playerSet.remove(player);
            if(playerSet.isEmpty())
                end(false);
        }
    }

    public void end(boolean hasWon) {
        gameTask.stop();
        TaskManager.submit(new Task(1) {
            @Override
            protected void execute() {
                groundItemList.forEach(groundItem -> GroundItemManager.remove(groundItem, true));
                npcList.forEach(World::deregister);
                stop();
            }
        });
        World.sendMessage("@red@TBD minigame has been " + (hasWon? "won" : "defeated") + " by " + playerSet.size() + " players!");
        playerSet.forEach(player -> leave(player, false));
        Lobby.getInstance().allowEntrance();
    }

    private void getAliveTrees() {
        aliveTrees = evilTrees.values().stream()
                .filter(tree -> !tree.isDead())
                .toList();
        int i = 1;
        for(Map.Entry<TreeEssenceType, EvilTree> entry : evilTrees.entrySet()) {
            EvilTree evl = entry.getValue();
            if(aliveTrees.stream().anyMatch(evilTree -> evilTree.getName().equals(evl.getName()))) {
                obeliskOptions[i] = evl.getName();
            } else {
                obeliskOptions[i] = "@str@"+evl.getName();
            }
            i++;
        }
    }

    public void obeliskClick(Player player) {
        if(deathCount == 3) {
            obeliskOfferTask(player, aliveTrees.get(0));
            return;
        }
        DialogueManager.sendDialogue(player, new Dialogue(player) {
            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public DialogueExpression animation() {
                return null;
            }

            @Override
            public String[] items() {
                return new String[] {};
            }

            @Override
            public void next(int stage) {
                if(stage == 0)
                    sendOption(obeliskOptions);
            }

            @Override
            public int id() {
                return 0;
            }

            @Override
            public void onClose() {

            }

            @Override
            public boolean handleOption(int option) {
                String treeName = getOptions().get(option);
                Optional<EvilTree> optionalEvilTree = aliveTrees.stream().filter(evilTree -> evilTree.getName().equalsIgnoreCase(treeName)).findFirst();
                optionalEvilTree.ifPresent(evilTree -> obeliskOfferTask(getPlayer(), evilTree));
                getPlayer().getPacketSender().sendInterfaceRemoval();
                return true;
            }
        }, -1);
    }

    public void dealDamage(EvilTree evilTree) {
        if(evilTree == null || evilTree.isDead()) return;
        evilTree.decrementHealth(3);
        if(evilTree.getCurrentHealth() <= 0) {
            deathCount++;
            evilTree.die();
            getAliveTrees();
        }
        if(deathCount == 4) {
            end(true);
        }
    }

    public void updateObeliskProgressBar() {
        playerSet.forEach(player -> {
            float percentage = (((float) obelisk.getConstitution() / obeliskMaxHealth) * 100);
            player.getPacketSender().updateProgressBar(151119, (int) (100 - percentage));
            player.getPacketSender().sendString(151121, obelisk.getConstitution() + "/" + obeliskMaxHealth);
        });
    }

    public int calcNpcSpawnAmount() {
        return (int) (Math.max(6, playerSet.size() * 1.5) + taskTicks/250 + (Misc.random(2)+1));
    }

    public void spawnNpcs(TreeEssenceType essenceType) {
        if(Objects.requireNonNull(evilTrees.get(essenceType)).isDead()) return;
        List<Position> spawnedPositions = generateRandomSpawnPoints(essenceType, calcNpcSpawnAmount());
        for(Position pos : spawnedPositions) {
            if(npcList.size() == 50) return;
            int npcId = getRandomNpcId();
            NPC npc = new NPC(npcId, pos);
            npc.isTreeMinigameNpc = true;
            npc.getMovementCoordinator().setCoordinator(new NPCMovementCoordinator.Coordinator(true, 5));
            npcList.add(npc);
            World.register(npc);
        }
    }

    public void dropBarrier(Player player) {
        Position position = player.getPosition();
        if(barriers.keySet().stream().anyMatch(it -> it.getX() == position.getX() && it.getY() == position.getY())) {
            player.getPacketSender().sendMessage("@red@Cannot place barrier here.");
            return;
        }
        if(barriers.size() == 10) {
            player.getPacketSender().sendMessage("@red@Only 10 barriers can be active at a time.");
            return;
        }
        if(position.getX() >= 2492 && position.getY() <= 2500 && position.getY() >= 3775 && position.getY() <= 3767) {
            player.getPacketSender().sendMessage("@red@Cannot place barrier here");
            return;
        }
        player.getInventory().delete(8644, 1);
        NPC npc = new NPC(551, position);
        barriers.put(position, npc);
        npc.isTreeMinigameNpc = true;
        npc.getCombatBuilder().setDidAutoRetaliate(false);
        npc.getMovementQueue().freeze(2000);
        World.register(npc);
        RegionClipping.addClipping(position.getX(), position.getY(), 4, 0x200000);
    }

    public void barrierDeath(NPC barrier) {
        if(barrier.getId() != 551) return;
        Position position = barrier.getPosition();
        barriers.remove(position);
        RegionClipping.removeClipping(position.getX(), position.getY(), 4, 0x200000);
        World.deregister(barrier);
    }

    public List<Position> generateRandomSpawnPoints(TreeEssenceType type, int amount) {
        Position basePoint = type.basePosition;
        int x = basePoint.getX();
        int y = basePoint.getY();
        List<Position> positionList = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            switch (type) {
                case MAPLE -> positionList.add(new Position(x + Misc.random(-4), y + Misc.random(-11,11), 4));
                case ELDER -> positionList.add(new Position(x + Misc.random(-11,11), y + Misc.random(-4), 4));
                case MAGIC -> positionList.add(new Position(x + Misc.random(4), y + Misc.random(-11,11), 4));
                case YEW -> positionList.add(new Position(x + Misc.random(-11,11), y + Misc.random(4), 4));
            }
        }
        return positionList;
    }

    public void npcDeath(NPC npc, Player killer) {
        if(npc.isTreeMinigameNpc) {
            if(npc.getId() == 551) {
                barrierDeath(npc);
                return;
            }
            npcList.remove(npc);
            GroundItem groundItem = new GroundItem(new Item(BASE_ESSENCE_ITEM_ID), npc.getPosition().copy(), killer.getUsername(), false, 5, true, 120);
            GroundItemManager.spawnGroundItem(killer, groundItem);
            groundItemList.add(groundItem);
        }
    }

    private void obeliskOfferTask(Player player, EvilTree evilTree) {
        Task task = new Task(3, true) {
            @Override
            protected void execute() {
                if(evilTree.isDead() || player.getInventory().getAmount(BASE_ESSENCE_ITEM_ID) <= 0) {
                    player.getPacketSender().sendInterfaceRemoval();
                    stop();
                } else {
                    player.getInventory().delete(BASE_ESSENCE_ITEM_ID, 1);
                    player.performAnimation(new Animation(713));
                    dealDamage(evilTree);
                    new Projectile(obelisk, Objects.requireNonNull(evilTree).getGameObject(), 2721, 44, 3, 43, 31, 0).sendProjectile();
                    playerSet.forEach(player -> player.getPacketSender().updateProgressBar(151113+evilTree.getIndex(),  (int) (100 - (((float) evilTree.getCurrentHealth() / evilTree.getMaxHealth()) * 100))));
                }
            }
        };
        TaskManager.submit(task);
        player.setCurrentTask(task);
    }

    public int getRandomNpcId() {
        return npcIds[random.nextInt(npcIds.length)];
    }

    enum TreeEssenceType {
        ELDER(0, new Position(2496, 3787, 4)),
        MAGIC(1, new Position(2482, 3771, 4)),
        YEW(2, new Position(2496, 3757, 4)),
        MAPLE(3, new Position(2513, 3771, 4));

        private final int key;
        private final Position basePosition;

        TreeEssenceType(int key, Position basePosition) {
            this.key = key;
            this.basePosition = basePosition;
        }
    }
}
