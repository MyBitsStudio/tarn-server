package com.ruse.world.packages.instances;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.World;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;
import com.ruse.world.packages.bosses.multi.impl.CounterInstance;
import com.ruse.world.packages.bosses.multi.MultiBossNormalInstance;
import com.ruse.world.packages.bosses.multi.impl.DonatorSpecialInstance;
import com.ruse.world.packages.bosses.multi.impl.IronmanInstance;
import com.ruse.world.packages.bosses.multi.impl.VoteSpecialInstance;
import com.ruse.world.packages.bosses.single.agthomoth.AgthomothInstance;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceManager {

    public static InstanceManager manager;

    public static InstanceManager getManager(){
        if(manager == null)
            manager = new InstanceManager();
        return manager;
    }

    private final Map<String, Instance> instances = new ConcurrentHashMap<>();

    /**
     * Starts New Multi Instance That Doesnt Have A Count
     */

    public void removeInstance(String id){
        instances.remove(id);
    }

    public void startEventInstance(Player player, InstanceInterData data){
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }

        if(!Objects.equals(player.getInstanceId(), "")){
            instances.get(player.getInstanceId()).removePlayer(player);
            player.setInstanceId("");
            return;
        }

        switch(data.getNpcId()){
            case 10835 -> {
                if(!World.handler.eventActive("Solstice"))
                    return;

                if(takeItem(player, data)) {
                     player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
                 return;
                }

//                Instance instance = new MultiBossFlashInstance(player,
//                data.getNpcId(), data.getSpawns(), data.getCap());
//
//                instances.put(instance.getInstanceId(), instance);
//                instance.start();
            }
            case 1736 -> {
                if(!World.handler.eventActive("Solstice"))
                    return;

                if(takeItem(player, data)) {
                    player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
                    return;
                }

                Instance instance = new MultiBossNormalInstance(player,
                        data.getNpcId(), data.getSpawns(), (1000 * 60 * 15));

                instances.put(instance.getInstanceId(), instance);
                instance.start();
            }
        }


    }

    public void enterMasterInstance(Player player, InstanceInterData data){
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }

        if(!Objects.equals(player.getInstanceId(), "")){
            instances.get(player.getInstanceId()).removePlayer(player);
            player.setInstanceId("");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }



    }

    public void startMultiInstance(Player player, InstanceInterData data){
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }

        if(!Objects.equals(player.getInstanceId(), "")){
            instances.get(player.getInstanceId()).removePlayer(player);
            player.setInstanceId("");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        Instance instance = switch (data.getNpcId()) {
            case 595 -> new CounterInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
            case 591 -> new DonatorSpecialInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
            case 593 -> new VoteSpecialInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
            case 1880 -> new IronmanInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
            default -> null;
        };

        if(instance == null)
            return;

        instances.put(instance.getInstanceId(), instance);
        instance.start();

    }

    /**
     * Starts Single Boss Instances
     * @param player
     */
    public void startSingleBossInstance(@NotNull Player player, InstanceInterData data){
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }

        if(!Objects.equals(player.getInstanceId(), "")){
            instances.get(player.getInstanceId()).removePlayer(player);
            player.setInstanceId("");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        SingleBossSinglePlayerInstance instance = null;

        switch(data.getNpcId()){
            case 3013 -> instance = new AgthomothInstance(player);
        }

        if(instance == null)
            return;


        if(!instance.canEnter(player)){
            player.sendMessage(instance.failedEntry());
            return;
        }

        instances.put(instance.getInstanceId(), instance);
        instance.start();
    }

    /**
     * Starts Multi Boss Instance With A Count
     * @param player
     */
    public void startMultiAmountInstance(@NotNull Player player, InstanceInterData data){
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }

        if(!Objects.equals(player.getInstanceId(), "")){
            instances.get(player.getInstanceId()).removePlayer(player);
            player.setInstanceId("");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        Instance instance = new MultiBossNormalInstance(player,
                data.getNpcId(), data.getSpawns(), (1000 * 60 * 10));

        instances.put(instance.getInstanceId(), instance);
        instance.start();
    }

    private void clear(Player player){
        World.getNpcs().stream()
                .filter(Objects::nonNull)
                .filter(npc -> npc.getPosition().getZ() == (player.getIndex() * 4))
                .filter(npc -> npc.getLocation().equals(Locations.Location.NORMAL_INSTANCE))
                .forEach(World::deregister);
    }

    private boolean takeItem(@NotNull Player player, @NotNull InstanceInterData data){
        int diff = Integer.parseInt(player.getVariables().getInterfaceSettings()[2]);
        if(data.getCost() != null){
            if(player.getInventory().contains(data.getCost().getId(), (int) (data.getCost().getAmount() * (1 +(diff * 2L))))){
                player.getInventory().delete(data.getCost().getId(), (int) (data.getCost().getAmount() * (1 +(diff * 2L))));
                player.sendMessage("@yel@[INSTANCE] You have been charged x"+(int) (data.getCost().getAmount() * (diff * 2L))+" of "+ ItemDefinition.forId(data.getCost().getId()).getName());
                return false;
            }
            return true;
        }
        return false;
    }

    public void refresh(@NotNull Player player){
        String[] settings = player.getVariables().getInterfaceSettings();

        InstanceInterData[] data = new InstanceInterData[0];
        InstanceInterData interData;
        NpcDefinition def;

        if(settings[0] == null || settings[1] == null){
            data = InstanceInterData.getMultiInstances();
            interData = data[0];
            player.getVariables().setInterfaceSettings(0, String.valueOf(0));
            player.getVariables().setInterfaceSettings(1, String.valueOf(0));
            player.getVariables().setInterfaceSettings(2, String.valueOf(0));

        } else {
            int tab = Integer.parseInt(settings[0]);
            int child = Integer.parseInt(settings[1]);

            data = switch (tab) {
                case 0 -> InstanceInterData.getMultiInstances();
                case 1 -> InstanceInterData.getSingleInstances();
                case 2 -> InstanceInterData.getSpecialInstances();
                case 3 -> InstanceInterData.getEventInstances();
                case 4 -> InstanceInterData.getMasterInstances();
                default -> data;
            };

            if (child >= data.length) {
                child = 0;
            }

            interData = data[child];
            if (interData == null) {
                return;
            }
        }

        def = NpcDefinition.forId(interData.getNpcId());
        if (def == null) {
            return;
        }
        String prefix = "@red@";

        int diff = Integer.parseInt(settings[2]);

        player.getPacketSender().sendSpriteChange(70534, 2991 + Integer.parseInt(settings[2]));

        player.getPacketSender().sendNpcIdToDisplayPacket(interData.getNpcId(), 70511);

        player.getPacketSender().sendString(70512, interData.getName());

        player.getPacketSender().sendString(70516, prefix+ (long) (def.getHitpoints() * ( 1 + (diff * 1.1))));
        player.getPacketSender().sendString(70518, prefix+ (long)(def.getMaxHit() * (1 + (.3 * diff))));
        player.getPacketSender().sendString(70520, prefix+ (long)(def.getDefenceMelee() * (1 + (.3 * diff))));
        player.getPacketSender().sendString(70522, prefix+ (long)(def.getDefenceMage() * (1 + (.3 * diff))));
        player.getPacketSender().sendString(70524, prefix+ (long)(def.getDefenceRange() * (1 + (.3 * diff))));

        int cap = (int) (interData.getCap() * player.getDonator().getCap());

        cap += player.getVip().getBonusCap();

        if(interData.getNpcId() == 1120){
            cap = 200;
        }

        player.getPacketSender().sendString(70526, prefix+ "x"+ (interData.getCost().getAmount() * (1+ (diff * 2L))) +" "+ItemDefinition.forId(interData.getCost().getId()).getName());
        player.getPacketSender().sendString(70528, prefix+ interData.getSpawns());
        player.getPacketSender().sendString(70530, prefix+ cap);
        player.getPacketSender().sendString(70532, prefix+ interData.getReq());

        for(int i = 0; i < (57 * 2); i+=2){
            player.getPacketSender().sendString(i + 70602, "");
            player.getPacketSender().sendSpriteChange(i + 70601, -1);
        }

        AtomicInteger starts = new AtomicInteger(70601);
        Arrays.stream(data)
                .filter(Objects::nonNull)
                .forEach(data1 -> {
                    if(isUnlocked(player, data1))
                        player.getPacketSender().sendSpriteChange(starts.getAndIncrement(), 1538);
                    else
                        player.getPacketSender().sendSpriteChange(starts.getAndIncrement(), 1539);

                    player.getPacketSender().sendString(starts.getAndIncrement(), prefix +data1.getName());
                });

        AtomicInteger start = new AtomicInteger(70801);
        for(int i = start.get(); i < start.get() + 60; i++){
            player.getPacketSender().sendItemOnInterface(i, -1, 0);
        }

        Arrays.stream(NPCDrops.forId(interData.getNpcId()).getDropList())
                .filter(Objects::nonNull)
                .forEach(item -> player.getPacketSender().sendItemOnInterface(start.getAndIncrement(), item.getItem().getId(), item.getItem().getAmount()));
    }

    public void sendInterface(@NotNull Player player){
        player.getPacketSender().sendInterfaceRemoval();
        refresh(player);
        player.getPacketSender().sendInterface(70500);
    }

    public boolean isUnlocked(@NotNull Player player, InstanceInterData data){
//        if(player.getRank().isDeveloper())
//            return true;
//
//        int npcId = 0, amount, base = data.getNpcId();
//
//        if(handleSpecialLock(base)){
//            return returnSpecial(player, base);
//        }
//
//        switch (base) {
//            case 8014 -> amount = 1000;
//            case 8003 -> {
//                npcId = 8014;
//                amount = 200;
//            }
//            default -> {
//                return false;
//            }
//        }
        return true;
    }

    private boolean returnSpecial(Player player, int base){
        return switch (base) {
            case 9017 -> KillsTracker.getTotalKills(player) >= 50000;
            default -> false;
        };
    }

    private boolean handleSpecialLock(int npcId){
        return switch (npcId) {
            case 9017 -> true;
            default -> false;
        };
    }

    public boolean handleButton(Player player, int button){
        int selection = (button >= 70501 && button <= 70714) ? 0 : -1;
        if(selection == 0){
            switch (button) {
                case 70506 -> player.getVariables().setInterfaceSettings(0, String.valueOf(0));
                case 70507 -> player.getVariables().setInterfaceSettings(0, String.valueOf(1));
                case 70508 -> player.getVariables().setInterfaceSettings(0, String.valueOf(2));
                case 70509 -> player.getVariables().setInterfaceSettings(0, String.valueOf(3));
                case 70510 -> player.getVariables().setInterfaceSettings(0, String.valueOf(4));
                case 70542 -> player.sendMessage("This is coming soon!");
                case 70535 -> startInstance(player);
                default -> {
                    int select = (button - 70602) / 2;
                    player.getVariables().setInterfaceSettings(1, String.valueOf(select));
                }
            }
            refresh(player);
            return true;
        }

       return false;
    }

    private void startInstance(@NotNull Player player){
        String[] settings = player.getVariables().getInterfaceSettings();
        int tab = Integer.parseInt(settings[0]);
        int child = Integer.parseInt(settings[1]);

        InstanceInterData[] data = switch (tab) {
            case 0 -> InstanceInterData.getMultiInstances();
            case 1 -> InstanceInterData.getSingleInstances();
            case 2 -> InstanceInterData.getSpecialInstances();
            case 3 -> InstanceInterData.getEventInstances();
            case 4 -> InstanceInterData.getMasterInstances();
            default -> new InstanceInterData[0];
        };

        if(child >= data.length){
            return;
        }

        InstanceInterData interData = data[child];

        if(interData == null){
            return;
        }

        if(!isUnlocked(player, interData)){
            player.sendMessage("You have not unlocked this instance yet.");
            return;
        }

        player.getPacketSender().sendInterfaceRemoval();
        clear(player);

        switch (interData.getType()) {
            case MULTI -> startMultiAmountInstance(player, interData);
            case SINGLE -> startSingleBossInstance(player, interData);
            case SPECIAL -> startMultiInstance(player, interData);
            case EVENT -> startEventInstance(player, interData);
            case MASTER -> enterMasterInstance(player, interData);
        }

    }

    public void onLogin(@NotNull Player player){
        if(player.getVariables().getInterfaceSettings()[0] == null){
            player.getVariables().setInterfaceSettings(0, "0");
        }
        if(player.getVariables().getInterfaceSettings()[1] == null){
            player.getVariables().setInterfaceSettings(1, "0");
        }
        if(player.getVariables().getInterfaceSettings()[2] == null){
            player.getVariables().setInterfaceSettings(2, "0");
        }
        for(Locations.Location loc : Locations.bossLocations){
            if(Locations.Location.inLocation(player, loc)) {
                player.getMovementQueue().setLockMovement(false).reset();
                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
            }
        }

    }

    public void dispose(Player player){
        if (player.getInstance() != null)
            player.getInstance().clear();

        player.setInstance(null);
        player.setTeleporting(true).getMovementQueue().reset();
        cancelCurrentActions(player);
        player.moveTo(new Position(2654, 2796, 0)).setPosition(new Position(2654, 2796, 0));
        player.getMovementQueue().setLockMovement(false).reset();

        player.getPacketSender().sendInterfaceRemoval();
    }

    public void cancelCurrentActions(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        player.setTeleporting(false);
        player.setWalkToTask(null);
        player.setInputHandling(null);
        player.getSkillManager().stopSkilling();
        player.setEntityInteraction(null);
        player.getMovementQueue().setFollowCharacter(null);
        player.getCombatBuilder().cooldown(false);
        player.setResting(false);
    }


}
