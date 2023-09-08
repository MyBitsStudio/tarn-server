package com.ruse.engine.task.impl;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.model.Animation;
import com.ruse.model.DamageDealer;
import com.ruse.model.Flag;
import com.ruse.model.GroundItem;
import com.ruse.model.Item;
import com.ruse.model.Locations.Location;
import com.ruse.model.Position;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.content.ItemsKeptOnDeath;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.PlayerPanel;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.packages.mode.impl.Ironman;
import com.ruse.world.packages.mode.impl.UltimateIronman;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 *
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

    /**
     * The PlayerDeathTask constructor.
     *
     * @param player The player setting off the task.
     */
    public PlayerDeathTask(Player player) {
        super(1, player, false);
        this.player = player;
    }

    private Player player;
    private int ticks = 5;
    private boolean dropItems = false;
    private boolean spawnItems = false;
    Position oldPosition;
    Location loc;
    ArrayList<Item> itemsToKeep = null;
    NPC death;

    @Override
    public void execute() {
        if (player == null) {
            stop();
            return;
        }
        try {
            switch (ticks) {
                case 5:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMovementQueue().setLockMovement(true).reset();
                    break;
                case 3:

                    if (player.currentInstanceAmount < 1) {
                    player.performAnimation(new Animation(0x900));
                    player.getPacketSender().sendMessage("Oh dear, you are dead!");
                        CurseHandler.deactivateAll(player);
                        PrayerHandler.deactivateAll(player);
                    this.death = getDeathNpc(player);
                    }
                    if (player.currentInstanceAmount >= 1) {
                        player.getPA().sendMessage("You have been kicked from instance");
                        player.getRegionInstance().destruct();
                        player.setData(null);
                        player.setCurrentInstanceAmount(-1);
                        player.setCurrentInstanceNpcId(-1);
                        player.setCurrentInstanceNpcName("");
                        player.performAnimation(new Animation(0x900));
                        Position[] locations = new Position[] { new Position(2656, 4016, 0), new Position(2656, 4016, 0) };
                        Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
                        TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
                    }
                    if(player.getInstance() != null){
                        player.getInstance().remove(player);
                    }
                    break;
                case 1:
                    this.oldPosition = player.getPosition().copy();
                    this.loc = player.getLocation();
                    if (loc != Location.DUNGEONEERING && loc != Location.CUSTOM_RAIDS && loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA
                            && loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT
                            && loc != Location.SOULWARS && loc != Location.FIGHT_PITS
                            && loc != Location.FIGHT_PITS_WAIT_ROOM
                            && loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD && loc != Location.ZULRAH
                            && loc != Location.RUNESPAN) {

                        DamageDealer damageDealer = player.getCombatBuilder().getTopDamageDealer(true, null);
                        Player killer = damageDealer == null ? null : damageDealer.getPlayer();

                        if (player.getRank().isAdmin())
                            dropItems = false;
                        if (loc == Location.WILDERNESS) {
                            if (killer != null && (killer.getRank().isAdmin())) // ||
                                // killer.getGameMode().equals(GameMode.IRONMAN)
                                // ||
                                // killer.getGameMode().equals(GameMode.ULTIMATE_IRONMAN)))
                                dropItems = false;
                        }
                        if (killer != null) {
                            if (killer.getRank().isAdmin()) {
                                dropItems = false;
                            }
                        }
                        if (loc == Location.THE_SIX || loc == Location.NOMAD) {
                            spawnItems = false;
                        } else spawnItems = loc != Location.WILDERNESS || killer == null || !killer.isPlayer()
                                || killer.getMode() instanceof Ironman || killer.getMode() instanceof UltimateIronman
                                || killer.getMode() instanceof GroupIronman;
                        if (dropItems) { // check for item dropping
                            if (!spawnItems) {
                                if (loc == Location.WILDERNESS && killer.isPlayer()
                                && (killer.getMode() instanceof Ironman || killer.getMode() instanceof UltimateIronman
                                        || killer.getMode() instanceof GroupIronman)) {
                                    killer.getPacketSender()
                                            .sendMessage("As an Iron/UIM player, you cannot loot " + player.getUsername()
                                                    + "...")
                                            .sendMessage(
                                                    "To stop them from freely attacking Iron folk, their dropped items have been removed.");
                                    player.getPacketSender().sendMessage(killer.getUsername() + " was an Iron Man or UIM.")
                                            .sendMessage(
                                                    "Because they cannot loot, all of your dropped items have been removed.");
                                    final CopyOnWriteArrayList<Item> goneItems = new CopyOnWriteArrayList<Item>();
                                    goneItems.addAll(player.getInventory().getValidItems());
                                    goneItems.addAll(player.getEquipment().getValidItems());
                                    for (Item item : goneItems) {
                                        if (item != null && item.getAmount() > 0 && item.getId() > 0) {
                                            PlayerLogs.log(player.getUsername(), "Died to IRON: " + killer.getUsername()
                                                    + ", losing: " + item.getId() + " x " + item.getAmount());
                                        }
                                    }
                                }
                            }
                            itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
                            final ArrayList<Item> playerItems = new ArrayList<Item>();
                            playerItems.addAll(player.getInventory().getValidItems());
                            playerItems.addAll(player.getEquipment().getValidItems());
                            /*
                             * final CopyOnWriteArrayList<Item> playerItems = new
                             * CopyOnWriteArrayList<Item>();
                             * playerItems.addAll(player.getInventory().getValidItems());
                             * playerItems.addAll(player.getEquipment().getValidItems());
                             */
                            final Position position = player.getPosition();
                            /*
                             * Collections.sort(playerItems, new Comparator<Item>() { // Despite this
                             * actually sorting properly, it does not affect how the client displays
                             * grounditems.
                             *
                             * @Override public int compare(Item i1, Item i2) { if (((long)
                             * i1.getAmount()*i1.getDefinition().getValue()) > ((long)
                             * i2.getAmount()*i2.getDefinition().getValue())) {
                             * // System.out.println("r1 "+((long)
                             * i1.getAmount()*i1.getDefinition().getValue()));
                             * // System.out.println("r1 "+((long)
                             * i2.getAmount()*i2.getDefinition().getValue())); return 1; } if (((long)
                             * i1.getAmount()*i1.getDefinition().getValue()) < ((long)
                             * i2.getAmount()*i2.getDefinition().getValue())) {
                             * // System.out.println("r-1 "+((long)
                             * i1.getAmount()*i1.getDefinition().getValue()));
                             * // System.out.println("r-1 "+((long)
                             * i2.getAmount()*i2.getDefinition().getValue())); return -1; } return 0; } });
                             */
                            for (Item item : playerItems) {
                                if (!item.tradeable() || itemsToKeep.contains(item)) {
                                    if (!itemsToKeep.contains(item)) {
                                        itemsToKeep.add(item);
                                    }
                                    continue;
                                }
                                if (spawnItems) {
                                    if (item.getId() > 0 && item.getAmount() > 0) {
                                        PlayerLogs.log(player.getUsername(),
                                                "Died and dropped: " + (ItemDefinition.forId(item.getId()) != null
                                                        && ItemDefinition.forId(item.getId()).getName() != null
                                                        ? ItemDefinition.forId(item.getId()).getName()
                                                        : item.getId())
                                                        + ", amount: " + item.getAmount());
                                        GroundItemManager.spawnGroundItem(
                                                (killer != null ? killer
                                                        : player),
                                                new GroundItem(item, position,
                                                        killer != null ? killer.getUsername() : player.getUsername(),
                                                        player.getHostAddress(), false, 150, true, 150));
                                    }
                                }
                            }
                            if (killer == null || player.getLocation() == Location.FREE_FOR_ALL_ARENA) {
                                PlayerLogs.logKills(player.getUsername(), "Died to npc or unknown");
                            } else {
                                killer.getPlayerKillingAttributes().add(player);
                                player.getPlayerKillingAttributes()
                                        .setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
                                player.getPlayerKillingAttributes().setPlayerKillStreak(0);
                                PlayerPanel.refreshPanel(player);
                                PlayerLogs.logKills(killer.getUsername(), "Killed player: " + player.getUsername());
                                PlayerLogs.logKills(player.getUsername(), "Died to player: " + killer.getUsername());
                            }
                            player.getInventory().resetItems().refreshItems();
                            player.getEquipment().resetItems().refreshItems();
                        }
                    } else
                        dropItems = false;
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setEntityInteraction(null);
                    player.getMovementQueue().setFollowCharacter(null);
                    player.getCombatBuilder().cooldown(false);
                    player.setTeleporting(false);
                    player.setWalkToTask(null);
                    player.getSkillManager().stopSkilling();
                    break;
                case 0:
                    if (dropItems) {
                        if (player.getMode() instanceof UltimateIronman) {
                            player.getMode().changeMode(new Ironman());
                        } else if (itemsToKeep != null) {
                            for (Item it : itemsToKeep) {
                                PlayerLogs.log(player.getUsername(),
                                        "Died, but KEPT: " + (ItemDefinition.forId(it.getId()) != null
                                                && ItemDefinition.forId(it.getId()).getName() != null
                                                ? ItemDefinition.forId(it.getId()).getName()
                                                : it.getId())
                                                + ", amount: " + it.getAmount());
                                player.getInventory().add(it.getId(), it.getAmount());

                            }
                            itemsToKeep.clear();
                        }
                    }
                    if (death != null) {
                        World.deregister(death);
                    }
                    WorldIPChecker.getInstance().leaveContent(player);
                    player.restart();
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    loc.onDeath(player);
                    if (loc != Location.DUNGEONEERING && loc != Location.CUSTOM_RAIDS) {

                        if (player.getPosition().equals(oldPosition))
                            player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                        if(player.isMiniPlayer()) {
                            player.getOwner().getMinimeSystem().targetPlayer();
                        }
                        // TeleportHandler.teleportPlayer(player, new Position(2524 +
                        // Misc.getRandom(10), 2595 + Misc.getRandom(6)),
                        // player.getSpellbook().getTeleportType());

                        // player.moveTo(new Position(2524 + Misc.getRandom(10), 2595 +
                        // Misc.getRandom(6)));
                        Dungeoneering.raidCount = 0;
                    }
                    player = null;
                    oldPosition = null;
                    stop();
                    break;
            }
            ticks--;
        } catch (Exception e) {
            setEventRunning(false);
            e.printStackTrace();
            if (player != null) {
                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                if(player.isMiniPlayer()) {
                    player.getOwner().getMinimeSystem().targetPlayer();
                }
                if (player.isGodMode()) {
                    return;
                }
                player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
            }
        }
    }

    public static NPC getDeathNpc(Player player) {
        NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
        World.register(death);
        death.setEntityInteraction(player);
        death.performAnimation(new Animation(401));
        death.forceChat(randomDeath(player.getUsername()));
        return death;
    }

    public static String randomDeath(String name) {
        return switch (Misc.getRandom(8)) {
            case 0 -> "There is no escape, " + Misc.formatText(name) + "...";
            case 1 -> "Muahahahaha!";
            case 2 -> "You belong to me!";
            case 3 -> "Beware mortals, " + Misc.formatText(name) + " travels with me!";
            case 4 -> "Your time here is over, " + Misc.formatText(name) + "!";
            case 5 -> "Now is the time you die, " + Misc.formatText(name) + "!";
            case 6 -> "I claim " + Misc.formatText(name) + " as my own!";
            case 7 -> Misc.formatText(name) + " is mine!";
            case 8 -> "Say goodbye, " + Misc.formatText(name) + "!";
            case 9 -> "I have come for you, " + Misc.formatText(name) + "!";
            default -> "";
        };
    }

}
