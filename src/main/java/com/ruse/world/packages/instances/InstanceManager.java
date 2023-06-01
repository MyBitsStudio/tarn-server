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
import com.ruse.world.packages.bosses.crucio.CrucioInstance;
import com.ruse.world.packages.bosses.multi.MultiBossFlashInstance;
import com.ruse.world.packages.bosses.multi.impl.CounterInstance;
import com.ruse.world.packages.bosses.multi.MultiBossNormalInstance;
import com.ruse.world.packages.bosses.multi.impl.DonatorSpecialInstance;
import com.ruse.world.packages.bosses.multi.impl.VoteSpecialInstance;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.packages.donation.FlashDeals;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceManager {

    public static InstanceManager manager;

    public static InstanceManager getManager(){
        if(manager == null)
            manager = new InstanceManager();
        return manager;
    }

    private final Map<String, Instance> instances = new ConcurrentHashMap<>();
    private final List<Instance> groups = new CopyOnWriteArrayList<>();

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

        if(player.getLocation() != Locations.Location.INSTANCE_LOBBY){
            player.sendMessage("Are you trying to abuse our system?");
            AdminCord.sendMessage(1111137818178236477L, "[" + player.getUsername() + "] is manipulating instances.");
            return;
        }

        Instance instance = new MultiBossFlashInstance(player,
                data.getNpcId(), data.getSpawns(), data.getCap());

        instances.put(instance.getInstanceId(), instance);
        instance.start();

    }

    public void enterGroupInstance(Player player, InstanceInterData data){
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

        if(player.getLocation() != Locations.Location.INSTANCE_LOBBY){
            player.sendMessage("Are you trying to abuse our system?");
            AdminCord.sendMessage(1111137818178236477L, "[" + player.getUsername() + "] is manipulating instances.");
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

        if(player.getLocation() != Locations.Location.INSTANCE_LOBBY){
            player.sendMessage("Are you trying to abuse our system?");
            AdminCord.sendMessage(1111137818178236477L, "[" + player.getUsername() + "] is manipulating instances.");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        Instance instance = null;

        switch(data.getNpcId()){
            case 595:
                instance = new CounterInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
                break;
            case 591:
                instance = new DonatorSpecialInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
                break;
            case 1880:
                instance = new VoteSpecialInstance(player, data.getNpcId(), data.getSpawns(), data.getCap());
                break;
        }
        
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

        if(player.getLocation() != Locations.Location.INSTANCE_LOBBY){
            player.sendMessage("Are you trying to abuse our system?");
            AdminCord.sendMessage(1111137818178236477L, "[" + player.getUsername() + "] is manipulating instances.");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        SingleBossSinglePlayerInstance instance = null;

        switch(data.getNpcId()){
            case 589:
                instance = new CrucioInstance(player);
                break;
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

        if(player.getLocation() != Locations.Location.INSTANCE_LOBBY){
            player.sendMessage("Are you trying to abuse our system?");
            AdminCord.sendMessage(1111137818178236477L, "[" + player.getUsername() + "] is manipulating instances.");
            return;
        }

        if(takeItem(player, data)) {
            player.sendMessage("You don't have x"+data.getCost().getAmount()+" of "+ItemDefinition.forId(data.getCost().getId()).getName());
            return;
        }

        Instance instance = new MultiBossNormalInstance(player,
                data.getNpcId(), data.getSpawns(), data.getCap());

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

            switch (tab) {
                case 0:
                    data = InstanceInterData.getMultiInstances();
                    break;
                case 1:
                    data = InstanceInterData.getSingleInstances();
                    break;
                case 2:
                    data = InstanceInterData.getSpecialInstances();
                    break;
                case 3:
                    data = InstanceInterData.getEventInstances();
                    break;
            }

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

        String prefix = "@blu@";
        switch(Integer.parseInt(settings[2])){
            case 1:
                prefix = "@gre@";
                break;
            case 2:
                prefix = "@red@";
                break;
            case 3:
                prefix = "@yel@";
                break;
        }

        int diff = Integer.parseInt(settings[2]);

        player.getPacketSender().sendSpriteChange(70534, 2991 + Integer.parseInt(settings[2]));

        player.getPacketSender().sendNpcIdToDisplayPacket(interData.getNpcId(), 70511);

        player.getPacketSender().sendString(70512, interData.getName());

        player.getPacketSender().sendString(70516, prefix+ (long) (def.getHitpoints() * ( 1 + (diff * .5))));
        player.getPacketSender().sendString(70518, prefix+ (long)(def.getMaxHit() * (1 + (.1 * diff))));
        player.getPacketSender().sendString(70520, prefix+ (long)(def.getDefenceMelee() * (1 + (.3 * diff))));
        player.getPacketSender().sendString(70522, prefix+ (long)(def.getDefenceMage() * (1 + (.3 * diff))));
        player.getPacketSender().sendString(70524, prefix+ (long)(def.getDefenceRange() * (1 + (.3 * diff))));

        player.getPacketSender().sendString(70526, prefix+ "x"+ (interData.getCost().getAmount() * (1+ (diff * 2L))) +" "+ItemDefinition.forId(interData.getCost().getId()).getName());
        player.getPacketSender().sendString(70528, prefix+ interData.getSpawns());
        player.getPacketSender().sendString(70530, prefix+ interData.getCap());
        player.getPacketSender().sendString(70532, prefix+ interData.getReq());

        for(int i = 0; i < (57 * 2); i+=2){
            player.getPacketSender().sendString(i + 70602, "");
            player.getPacketSender().sendSpriteChange(i + 70601, -1);
        }

        AtomicInteger starts = new AtomicInteger(70601);
        String finalPrefix = prefix;
        Arrays.stream(data)
                .filter(Objects::nonNull)
                .forEach(data1 -> {
                    if(isUnlocked(player, data1))
                        player.getPacketSender().sendSpriteChange(starts.getAndIncrement(), 1538);
                    else
                        player.getPacketSender().sendSpriteChange(starts.getAndIncrement(), 1539);

                    player.getPacketSender().sendString(starts.getAndIncrement(), finalPrefix +data1.getName());
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
        if(player.getRights().OwnerDeveloperOnly())
            return true;

        int npcId = 0, amount, base = data.getNpcId();

        if(handleSpecialLock(base)){
            return returnSpecial(player, base);
        }

        switch(base){
            case 8014 : amount = 1000;break;
            case 8003: npcId = 8014; amount = 200;break;
            case 202: npcId = 8003; amount = 200;break;
            case 811: npcId = 202; amount = 200;break;
            case 9815 : npcId = 811; amount = 200;break;
            case 9817 : npcId = 9815; amount = 300;break;
            case 9920: npcId = 9817; amount = 300;break;
            case 3831 : npcId = 9920; amount = 300;break;
            case 9025: npcId = 3831; amount = 300;break;
            case 9026: npcId = 9025; amount = 500;break;
            case 9836: npcId = 9026; amount = 500;break;
            case 92: npcId = 9836; amount = 500;break;
            case 3313: npcId = 92; amount = 500;break;
            case 8008: npcId = 3313; amount = 500;break;
            case 1906: npcId = 8008; amount = 500;break;
            case 9915: npcId = 1906; amount = 500;break;
            case 2342: npcId = 9915; amount = 500;break;
            case 9024: npcId = 2342; amount = 500;break;
            case 9916: npcId = 9024; amount = 750;break;
            case 9918: npcId = 9916; amount = 750;break;
            case 9919: npcId = 9918; amount = 750;break;
            case 9914 : npcId = 9919; amount = 1000;break;
            case 9839: npcId = 9017; amount = 1000;break;
            case 9806: npcId = 9839; amount = 1000;break;
            case 9816: npcId = 9806; amount = 1000;break;
            case 9903: npcId = 9816; amount = 2000;break;
            case 8002: npcId = 9903; amount = 2000;break;
            case 1746: npcId = 8002; amount = 2000;break;
            case 3010: npcId = 1746; amount = 2000;break;
            case 3013: npcId = 3010; amount = 2000;break;
            case 3014: npcId = 3013; amount = 3000;break;
            case 8010: npcId = 3014; amount = 3000;break;
            case 3016: npcId = 8010; amount = 3000;break;
            case 4972: npcId = 3016; amount = 3000;break;
            case 9012: npcId = 4972; amount = 3000;break;
            case 3019: npcId = 9012; amount = 3000;break;
            case 3020: npcId = 3019; amount = 3000;break;
            case 3021: npcId = 3020; amount = 5000;break;
            case 3305: npcId = 3021; amount = 5000;break;
            case 9818: npcId = 3305; amount = 5000;break;
            case 9912: npcId = 9818; amount = 5000;break;
            case 9913: npcId = 9912; amount = 5000;break;
            case 3117: npcId = 9913; amount = 5000;break;
            case 3115: npcId = 3117; amount = 5000;break;
            case 12239: npcId = 3115; amount = 5000;break;
            case 3112: npcId = 12239; amount = 5000;break;
            case 3011: npcId = 3112; amount = 5000;break;
            case 252: npcId = 3011; amount = 5000;break;
            case 449: npcId = 252; amount = 5000;break;
            case 452: npcId = 449; amount = 5000;break;
            case 187: npcId = 452; amount = 5000;break;
            case 188: npcId = 187; amount = 10000;break;
            case 1311: npcId = 188; amount = 15000;break;
            case 1313: npcId = 1311; amount = 15000;break;
            case 1318: npcId = 1313; amount = 25000;break;

            case 595: npcId = 1318; amount = 50000;break;

            case 589: npcId = 595; amount = 15000;break;
            default : return false;
        }
        if(data.getNpcId() == 8014)
            return KillsTracker.getTotalKills(player) > amount;
        else
            return KillsTracker.getTotalKillsForNpc(npcId, player) > amount;
    }

    private boolean returnSpecial(Player player, int base){
        switch(base){
            case 9017:

                return true;
            case 591:
            case 593:
            case 587:
            case 8013:
                return true;
            case 601:
                return FlashDeals.getDeals().isActive();

        }
        return false;
    }

    private boolean handleSpecialLock(int npcId){
        switch(npcId){
            case 9017:
            case 591:
            case 593:
            case 601:
            case 587:
            case 8013:
                return true;
        }
        return false;
    }

    public boolean handleButton(Player player, int button){
        int selection = (button >= 70501 && button <= 70714) ? 0 : -1;
        if(selection == 0){
            switch(button){
                case 70506: player.getVariables().setInterfaceSettings(0, String.valueOf(0)); break;
                case 70507: player.sendMessage("This is coming soon!"); break;
                case 70508: player.getVariables().setInterfaceSettings(0, String.valueOf(2)); break;
                case 70509: player.getVariables().setInterfaceSettings(0, String.valueOf(3)); break;
                case 70510: player.sendMessage("This is coming soon!"); break;
                case 70534 :
                    int current = Integer.parseInt(player.getVariables().getInterfaceSettings()[2]);
                    player.getVariables().setInterfaceSettings(2, String.valueOf(
                        current >= 3 ? 0 : current + 1));
                    player.sendMessage("You have selected " + player.getVariables().getInterfaceSettings()[2] + " difficulty.");
                    break;
                case 70535 :
                    startInstance(player);
                    break;
                default:
                    int select = (button - 70602) / 2;
                    player.getVariables().setInterfaceSettings(1, String.valueOf(select));
                    break;
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

        InstanceInterData[] data = new InstanceInterData[0];

        switch(tab){
            case 0:
                data = InstanceInterData.getMultiInstances();
                break;
            case 1:
                data = InstanceInterData.getSingleInstances();
                break;
            case 2:
                data = InstanceInterData.getSpecialInstances();
                break;
            case 3:
                data = InstanceInterData.getEventInstances();
                break;
        }

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

        switch(interData.getType()){
            case MULTI:
                startMultiAmountInstance(player, interData);
                break;
            case SINGLE:
                startSingleBossInstance(player, interData);
                break;
            case SPECIAL:
                startMultiInstance(player, interData);
                break;
            case EVENT:
                startEventInstance(player, interData);
                break;
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
