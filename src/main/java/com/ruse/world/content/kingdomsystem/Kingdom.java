package com.ruse.world.content.kingdomsystem;

import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Kingdom {
    private static final int MAX_WORKERS = 10;
    private static final int INTERFACE_ID = 167500;
    private static final int WORKER_OVERLAY_INTERFACE_ID = 167513;

    private final Player player;

    private KingdomType selectedKingdom;

    private final HashMap<KingdomType, Worker[]> playerKingdoms = new HashMap<>();

    public Kingdom(Player player) {
        this.player = player;
    }

    public boolean handleButtonClick(int id) {
        if(selectEmptyWorkerSlot(id)) return true;
        switch (id) {
            case 167516 -> {
                player.getPacketSender().removeOverlay();
                return true;
            }
            case 167503 -> {
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
        }
        KingdomType kingdomType;
        if((kingdomType = KingdomType.KINGDOM_MAP.get(id)) != null) {
            openKingdom(kingdomType);
            return true;
        }
        return false;
    }

    private boolean selectEmptyWorkerSlot(int id) {
        if(id >= 167520 && id <= 167529) {
            if(selectedKingdom != null) {
                int slot = id - 167520;
                Worker worker = playerKingdoms.get(selectedKingdom)[slot];
                if(worker == null) {
                    // handle adding new worker here
                }
            }
            return true;
        }
        return false;
    }

    private void openKingdom(KingdomType kingdomType) {
        selectedKingdom = kingdomType;
        Worker[] workers = playerKingdoms.computeIfAbsent(kingdomType, x -> new Worker[MAX_WORKERS]);
        if(workers.length != MAX_WORKERS) {
            workers = increaseWorkerSize(workers);
        }
        for(Worker worker : workers) {
            if(worker == null) {
                // send empty worker slot here
            } else {

                // send worker details here
            }
        }
        player.getPacketSender().sendString(167517, kingdomType.name)
                        .sendInterfaceOverlay(INTERFACE_ID, WORKER_OVERLAY_INTERFACE_ID);
    }

    private Worker[] increaseWorkerSize(Worker[] workers) {
        Worker[] temp = new Worker[MAX_WORKERS];
        for(int i = 0; i < MAX_WORKERS; i++) {
            if(workers.length > i) {
                temp[i] = workers[i];
            } else {
                temp[i] = null;
            }
        }
        return temp;
    }

    private enum KingdomType {
        WORLD_1(167506, "world 1"),
        WORLD_2(167507, "world 2"),
        WORLD_3(167508, "world 3"),
        WORLD_4(167509, "world 4"),
        WORLD_5(167510, "world 5"),
        WORLD_6(167511, "world 6"),
        WORLD_7(167512, "world 7")
        ;

        private final int buttonId;
        private final String name;

        KingdomType(int buttonId, String name) {
            this.buttonId = buttonId;
            this.name = name;
        }

        public int getButtonId() {
            return buttonId;
        }

        public String getName() {
            return name;
        }

        private static final HashMap<Integer, KingdomType> KINGDOM_MAP = (HashMap<Integer, KingdomType>) Arrays.stream(values()).collect(Collectors.toMap(KingdomType::getButtonId, Function.identity()));
    }
}
