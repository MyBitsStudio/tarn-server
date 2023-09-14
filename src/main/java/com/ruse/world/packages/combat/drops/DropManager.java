package com.ruse.world.packages.combat.drops;

import com.ruse.model.GroundItem;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.DropLog;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.packages.collectionlog.CollectionEntry;
import com.ruse.world.packages.discordbot.AdminCord;
import com.ruse.world.content.equipmentenhancement.BoostType;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.items.loot.Lootbag;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.johnachievementsystem.PerkType;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DropManager {

    private static DropManager instance;

    public static DropManager getManager(){
        if(instance == null){
            instance = new DropManager();
        }
        return instance;
    }

    private final List<DropTable> tables = new ArrayList<>();

    private final Map<Integer, NPCDrops> npcDrops = new HashMap<>();

    public void sendDrop(NPC npc, Player player){
        sendDrop(npc, player, 1.0);
    }

    public void addTables(DropTable table){
        tables.add(table);
    }

    public void addNPCDrops(NPCDrops drop, int id){
        npcDrops.put(id, drop);
    }

    public NPCDrops forId(int id){
        return npcDrops.get(id);
    }

    public DropTable forName(String name){
        for(DropTable table : tables){
            if(table == null)
                continue;
            if(table.name().equalsIgnoreCase(name)){
                return table;
            }
        }
        return null;
    }
    public void sendDrop(@NotNull NPC npc, Player player, double modifier){
        Optional<NPCDrops> drops = getDrops(npc.getId());
        if(drops.isEmpty()){
            return;
        }

        NPCDrops npcDrops = drops.get();

        double chance = Misc.getRandom(0.0, 10.0), dblChance = 0.0;

        List<DropTable> tables = new ArrayList<>(Arrays.asList(npcDrops.tables()));
        List<Drop> finalDrops = new CopyOnWriteArrayList<>(), extraDrops = new CopyOnWriteArrayList<>();

        chance += DropCalculator.getDropChance(player, npc.getId());
        dblChance += DropCalculator.getDoubleDropChance(player, npc.getId());

        List<DropTable> spin = new ArrayList<>(tables);

        if(spin.isEmpty() && npcDrops.customTable() == null){
            handleDryStreak(player, npc);
            return;
        }

        DropTable table = spin.isEmpty() ? null : spin.get(new SecureRandom().nextInt(spin.size())), extra = null;
        if(table != null){
            if(dblChance >= Misc.getRandom(1, 5000)){
                extra = spin.get(new SecureRandom().nextInt(spin.size()));
            }
            double reduce = npcDrops.customTable() == null ? 1 : npcDrops.customTable().weight();
            final int v = chance + modifier >= 5000 ? 4999 : (int) (chance + modifier);

           // System.out.println("V: " + v + " | Reduce: " + reduce + " | Chance: " + chance + " | Modifier: " + modifier);

            for(Drop drop : table.drops()){
                if(drop.modifier() == 1.0) {
                    finalDrops.add(drop);
                    continue;
                }
                double rolled = (Misc.RAND.nextDouble() * v) / reduce;
                double req = 1 + (Misc.RAND.nextDouble() * drop.modifier());
               // System.out.println("First Rolled: " + rolled + " | Req: " + req);
                if(rolled > req){
                    finalDrops.add(drop);
                }
            }

            if(npcDrops.customTable() != null){
                for(Drop drop : npcDrops.customTable().drops()){
                    if(drop.modifier() == 1.0) {
                        finalDrops.add(drop);
                        continue;
                    }
                    double rolled = (Misc.RAND.nextDouble() * v) / reduce;
                    double req = 1 + (Misc.RAND.nextDouble() * drop.modifier());
                   // System.out.println("Second Rolled: " + rolled + " | Req: " + req);
                    if(rolled > req){
                        finalDrops.add(drop);
                    }
                }
            }

            if(extra != null){
                for(Drop drop : extra.drops()){
                    if(drop.modifier() == 1.0) {
                        continue;
                    }
                    double rolled = (Misc.RAND.nextDouble() * v) / reduce;
                    double req = 1 + (Misc.RAND.nextDouble() * drop.modifier());
                    //System.out.println("Third Rolled: " + rolled + " | Req: " + req);
                    if(rolled > req){
                        extraDrops.add(drop);
                    }
                }
            }

            if(finalDrops.isEmpty() && extraDrops.isEmpty()){
                handleDryStreak(player, npc);
                return;
            } else {
                player.getDryStreak().getDryStreakMap().put(npc.getId(), 0);
            }

            Position pos = npc.getPosition().copy();
            boolean collector = player.getEquipment().hasCollector();
            boolean hasLoot = player.getInventory().contains(Lootbag.LOOT_DEVICE);

            for(Drop drop : finalDrops){
                if(drop.modifier() != 1.0) {
                    continue;
                }
                int spinned = Misc.random(drop.min(), drop.max());
                drop = getModifier(player, drop, spinned);
                Item item = new Item(drop.id(), drop.chance());
                if(drop.announce()){
                    String itemName = item.getDefinition().getName();
                    String itemMessage = "x" + item.getAmount() + " " + itemName;
                    String npcName = Misc.formatText(npc.getDefinition().getName());
                    String message = "<img=15><shad><col=CB0101> [" + player.getUsername()
                            + "]<col=680000> has received <col=CB0101>" + itemMessage + "<col=680000> from <col=CB0101>" + npcName;
                    World.sendFilterMessage(message);
                }
                log(player, npc, drop, item.getAmount());

                if(collector || hasLoot){
                    player.depositItemBank(item);
                    player.sendMessage("x"+item.getAmount()+" of "+item.getDefinition().getName() +" has been sent to your bank from your collector.");
                } else {
                    GroundItemManager.spawnGroundItem(player,
                            new GroundItem(item, pos, player.getUsername(), false, 150, false, 200));
                }
                finalDrops.remove(drop);
            }

            if(finalDrops.isEmpty() && extraDrops.isEmpty()){
                return;
            }

            if(finalDrops.isEmpty()){
                return;
            }

            Drop finalDrop = finalDrops.get(new SecureRandom().nextInt(finalDrops.size()));
            int spinned = Misc.random(finalDrop.min(), finalDrop.max());
            finalDrop = getModifier(player, finalDrop, spinned);

            Item item = new Item(finalDrop.id(), finalDrop.chance());

            if(finalDrop.announce()){
                String itemName = item.getDefinition().getName();
                String itemMessage = "x" + item.getAmount() + " " + itemName;
                String npcName = Misc.formatText(npc.getDefinition().getName());
                String message = "<img=15><shad><col=CB0101> [" + player.getUsername()
                        + "]<col=680000> has received <col=CB0101>" + itemMessage + "<col=680000> from <col=CB0101>" + npcName;
                World.sendFilterMessage(message);
            }
            log(player, npc, finalDrop, item.getAmount());

            if(collector || hasLoot){
                player.depositItemBank(item);
                player.sendMessage("x"+item.getAmount()+" of "+item.getDefinition().getName() +" has been sent to your bank from your collector.");
            } else {
                GroundItemManager.spawnGroundItem(player,
                        new GroundItem(item, pos, player.getUsername(), false, 150, false, 200));
            }

            if(extraDrops.isEmpty()){
                return;
            }

            Drop finalDrop2 = extraDrops.get(new SecureRandom().nextInt(extraDrops.size()));
            int spinned2 = Misc.random(finalDrop2.min(), finalDrop2.max());
            finalDrop2 = getModifier(player, finalDrop2, spinned2);

            Item item2 = new Item(finalDrop2.id(), finalDrop2.chance());

            if(finalDrop2.announce()){
                String itemName = item2.getDefinition().getName();
                String itemMessage = "x" + item2.getAmount() + " " + itemName;
                String npcName = Misc.formatText(npc.getDefinition().getName());
                String message = "<img=15><shad><col=CB0101> [" + player.getUsername()
                        + "]<col=680000> has received <col=CB0101>" + itemMessage + "<col=680000> from <col=CB0101>" + npcName;
                World.sendFilterMessage(message);
            }
            log(player, npc, finalDrop2, item2.getAmount());

            if(collector || hasLoot){
                player.depositItemBank(item2);
                player.sendMessage("x"+item.getAmount()+" of "+item.getDefinition().getName() +" has been sent to your bank from your collector.");
            } else {
                GroundItemManager.spawnGroundItem(player,
                        new GroundItem(item2, pos, player.getUsername(), false, 150, false, 200));
            }
        }

    }

    private Optional<NPCDrops> getDrops(int npcId){
        return Optional.ofNullable(npcDrops.get(npcId));
    }

    private void log(Player player, NPC npc, Drop finalDrop, int amount){
        DropLog.submit(player, new DropLog.DropLogEntry(finalDrop.id(), amount, finalDrop.announce()));
        new CollectionEntry(npc.getId(), finalDrop.id(), amount).submit(player);
        AdminCord.sendMessage(1116222355673464883L, "[" + player.getUsername() + "] has received x" +amount + " " + ItemDefinition.forId(finalDrop.id()).getName() +" from " + Misc.formatText(npc.getDefinition().getName()) + ".");
        PlayerLogs.log(player.getUsername(),
                player.getUsername() + " received " + ItemDefinition.forId(finalDrop.id()).getName() + " from " + npc.getDefinition().getName() + "!");

        PlayerLogs.logNpcDrops(player.getUsername(), "Player received drop: " + ItemDefinition.forId(finalDrop.id()).getName()
                + ", id: " + finalDrop.id() + ", amount: " + amount + ", from: " + npc.getDefinition().getName());
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.parseDouble(df.format(value));
    }

    private void handleDryStreak(@NotNull Player player, @NotNull NPC npc){
        boolean hit = player.getDryStreak().hasHitDryStreak(npc.getId());

        if(hit){
            player.getDryStreak().getDryStreakMap().put(npc.getId(), 0);
            sendDrop(npc, player, 5000.0);
        }
    }

    @Contract(pure = true)
    private @NotNull Drop getModifier(@NotNull Player player, @NotNull Drop drop, int amounts){
        int id = drop.id(), amount = amounts;
        boolean isCash = id == 995 || id == 10835;

        if (player.getEquipment().hasDoubleCash() && isCash) {
            amount *= 2;
        } else if(player.getEquipment().hasTripleCash() && isCash) {
            amount *= 3;
        }

        if(player.getEquipment().contains(15585) && isCash){
            amount *= 1.25;
        }

        if(isCash){
            var multiplier = player.getEquipmentEnhancement().getBoost(BoostType.CASH);
            amount *= (1 + (multiplier * 0.01));
        }

        if(isCash){
            if(AchievementHandler.hasUnlocked(player, PerkType.COINS)){
                amount *= (1 + (AchievementHandler.getPerkLevel(player, PerkType.COINS) * 0.10));
            }
        }

        if (id == 22120) {
            boolean ultimateIron = player.getMode() instanceof UltimateIronman;
            boolean groupIron = player.getMode() instanceof GroupIronman;
            if (ultimateIron) {
                id = 22119;
            } else if (groupIron) {
                id = 22118;
            }
        }

        if(drop.chance() == 1){
            amount = 1;
        }

        return new Drop(id, amount, drop.min(), drop.max(), drop.modifier(), drop.announce());
    }

}
