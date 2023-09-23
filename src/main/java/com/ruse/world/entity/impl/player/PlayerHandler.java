package com.ruse.world.entity.impl.player;

import com.ruse.GameServer;
import com.ruse.GameSettings;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.*;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Bank;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.WeaponAnimations;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.net.PlayerSession;
import com.ruse.net.SessionState;
import com.ruse.net.security.ConnectionHandler;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.effect.CombatPoisonEffect;
import com.ruse.world.content.combat.effect.CombatTeleblockEffect;
import com.ruse.world.content.combat.magic.Autocasting;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.packages.instances.InstanceManager;
import com.ruse.world.packages.misc.ItemIdentifiers;
import com.ruse.world.packages.mode.GameModeConstants;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import com.ruse.world.packages.ranks.StaffRank;
import com.ruse.world.packages.seasonpass.SeasonPassConfig;
import com.ruse.world.packages.seasonpass.SeasonPassManager;
import com.ruse.world.packages.serverperks.ServerPerks;
import com.ruse.world.entity.impl.GlobalItemSpawner;
import com.ruse.world.instance.MapInstance;

import java.util.Objects;

import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

//import com.ruse.world.content.Abyssector;

public class PlayerHandler {

    public static void handleLogin(Player player) {
        if(player == null)
            return;

        World.playerMap().put(player.getLongUsername(), player);


        // Register the player
       // System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
              //  + player.getHostAddress() + "]");

        PlayerLogs.logPlayerLoginWithIP(player.getUsername(), "Login from host " + player.getHostAddress()
                + ", serial number: " + player.getSerialNumber() + ", mac address:" + player.getMac());
        PlayerLogs.logPlayerLogin(player.getUsername(), "Login ");
        player.getControllerManager().login();
        ConnectionHandler.add(player.getHostAddress());
        World.addPlayer(player);
        if (!player.isMini()) {
            PlayersOnlineInterface.add(player);
        }
        World.updatePlayersOnline();
        player.getSession().setState(SessionState.LOGGED_IN);

        // Packets
        player.getPacketSender().sendDetails();
        player.loadMap(true);
        player.getRecordedLogin().reset();

        //Mbox spinner
        player.getNewSpinner().clearMysteryBox();

        // Tabs
        player.getPacketSender().sendTabs();
        if (player.getWalkableInterfaceId() == 29050) {
            player.getPacketSender().sendWalkableInterface(29050, false);
        }
        player.getPacketSender().sendWalkableInterface(ServerPerks.OVERLAY_ID, ServerPerks.getInstance().getActivePerk() != null);
        player.getPacketSender().sendString(29053, "").sendString(29054, "");

        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(29095 + i, "");
        }

        // Setting up the player's item containers..

        //player.getMinimeSystem().onLogin();

        for (int i = 0; i < player.getBanks().length; i++) {
            if (player.getBank(i) == null) {
                player.setBank(i, new Bank(player));
            }
        }
        player.getInventory().refreshItems();
        player.getEquipment().refreshItems();

        player.getPlayerFlags().process();


        if(player.getSeasonPass().getSeason() != SeasonPassConfig.getInstance().getSeason()) {
            SeasonPassManager.resetSeasonPass(player.getSeasonPass());
        }

        // Weapons and equipment..
        WeaponAnimations.update(player);
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        CombatSpecial.updateBar(player);
        BonusManager.update(player);

        // Skills
       // player.getSummoning().login();
        //player.getFarming().load();
        //player.getBestItems().fillDefinitions();;
        for (Skill skill : Skill.values()) {
            player.getSkillManager().updateSkill(skill);
        }

        // Relations
        player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

        // Client configurations
        player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
                .sendTotalXp(player.getSkillManager().getTotalGainedExp())
                .sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
                .sendRunEnergy(player.getRunEnergy()).sendRights()
                .sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade With", 4, false);
             //   .sendInteractionOption("Gamble With", 6, false);
        player.getPacketSender().sendConfig(663, player.levelNotifications ? 1 : 0);

        Autocasting.onLogin(player);
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        BonusManager.sendCurseBonuses(player);
       // AchievementsOLD.updateInterface(player);
       // Barrows.handleLogin(player);
        //VoidOfDarkness.handleLogin(player);
        // Tasks
        TaskManager.submit(new PlayerSkillsTask(player));
        TaskManager.submit(new PlayerRegenConstitutionTask(player));
        TaskManager.submit(new SummoningRegenPlayerConstitutionTask(player));
        if (player.isPoisoned()) {
            TaskManager.submit(new CombatPoisonEffect(player));
        }
        player.getUpgradeHandler().init();
        if (player.getPrayerRenewalPotionTimer() > 0) {
            TaskManager.submit(new PrayerRenewalPotionTask(player));
        }
        if (player.getOverloadPotionTimer() > 0) {
            TaskManager.submit(new OverloadPotionTask(player));
        }
        if (player.getTeleblockTimer() > 0) {
            TaskManager.submit(new CombatTeleblockEffect(player));
        }

        //player.getDonationDeals().shouldReset();

        if (player.getSkullTimer() > 0) {
            player.setSkullIcon(1);
            TaskManager.submit(new CombatSkullEffect(player));
        }

        if (System.currentTimeMillis() > (player.lastLogin + 86400000)) {
            player.getDailyRewards().resetData();
        }

        player.getDailyRewards().handleDailyLogin();

        player.lastLogin = System.currentTimeMillis();

        player.getDailyRewards().setDataOnLogin();

        if (player.getFireImmunity() > 0) {
            FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
        }
        if (player.getSpecialPercentage() < 100) {
            TaskManager.submit(new PlayerSpecialAmountTask(player));
        }
        if (player.hasStaffOfLightEffect()) {
            TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
        }

        // Update appearance

        // Others
        Locations.login(player);

        InstanceManager.getManager().onLogin(player);

        if(player.getLocation() != Location.STARTER) {
            player.getPacketSender().sendWalkableInterface(112000, false);
        }

        player.getPacketSender().sendMessage("<shad=1>@bla@Welcome to " + GameSettings.RSPS_NAME + "!");


        if (player.experienceLocked())
            player.getPacketSender().sendMessage(MessageType.SERVER_ALERT,
                    " @red@Warning: your experience is currently locked.");

        /*
         * if (!player.getRights().OwnerDeveloperOnly() &&
         * player.getSkillManager().getExperience(Skill.INVENTION) > 1) {
         * player.getSkillManager().setExperience(Skill.INVENTION, 0);
         * player.getSkillManager().setMaxLevel(Skill.INVENTION, 1);
         * player.getSkillManager().setCurrentLevel(Skill.INVENTION, 1, true); }
         */


        if (Misc.isWeekend()) {
            player.getPacketSender().sendMessage("[" + GameSettings.RSPS_NAME
                    + "] Double EXP has been activated. It stacks with Vote scrolls, Enjoy!");
        }

        if (WellOfGoodwill.isActive()) {
            player.getPacketSender().sendMessage(MessageType.SERVER_ALERT,
                    "The Well of Goodwill is granting 30% bonus experience for another "
                            + WellOfGoodwill.getMinutesRemaining() + " minutes.");
        }

        PlayerPanel.refreshPanel(player);

        // New player
        //Give currency pouch to UIM
        if (player.newPlayer()) {
            StartScreen.open(player);
            player.setPlayerLocked(true);;
        } else if (!player.getInventory().contains(22108) && player.getMode() instanceof UltimateIronman) {
            player.getInventory().add(22108, 1);
            player.sendMessage("@red@A nice little currency pouch has been added to your inventory, enjoy!");
            player.sendMessage("@red@If you lose it relog to re-obtain!");
        }

        if(player.isSecondaryEquipment()) {
            player.getPacketSender().sendSpriteChange(15005, 3305);
            player.getSecondaryEquipment().refreshItems();
        } else {
            player.getPacketSender().sendSpriteChange(15005, 3304);
        }

        if (!player.isMini()) {
            ClanManager.getManager().leave(player, false);
            ClanManager.getManager().reset(player);
            ClanManager.getManager().joinChat(player, "help");
        }

        AchievementHandler.onPlayerLogin(player);

        player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode(GameModeConstants.ordinal(player));

        if(player.getRank() == StaffRank.TRAIL_STAFF){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "> <img=5>Trial Staff "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.HELPER){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "> <img=5>Helper "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.MODERATOR){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "> <img=1>Moderator "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.ADMINISTRATOR){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "> <img=2>Administrator "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.MANAGER){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "> <img=2>Manager "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.DEVELOPER){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "><img=4> Developer "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.OWNER){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "><img=4> Owner "
                    + player.getUsername() + " has just logged in."));
        }
        if(player.getRank() == StaffRank.YOUTUBER){
            World.sendMessage(("<shad=0><col=" + player.getYellHex() + "><img=10> @red@Youtuber@whi@ "
                    + player.getUsername() + " has just logged in."));
        }

//        if (player.getRights() == PlayerRights.FORSAKEN_DONATOR)
//            World.sendMessage(("<shad=0>@or2@<img=1508> [Forsaken Donator] "
//                    + player.getUsername() + " has just logged in."));

        if (player.getRank().isStaff() ) {
            StaffList.login(player);
        }
        StaffList.updateGlobalInterface();

        player.getUpdateFlag().flag(Flag.APPEARANCE);


        Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);

        if (weapon != null) {
            if (AutoCastSpell.getAutoCastSpell(player) == null) {
                if (player.getAutocastSpell() != null || player.isAutocast()) {
                    Autocasting.resetAutocast(player, true);
                }
            } else {
                player.setAutocastSpell(Objects.requireNonNull(AutoCastSpell.getAutoCastSpell(player)).getSpell());
            }
        }


        player.initGodMode();

        PlayerLogs.log(player.getUsername(),
                "Login. ip: " + player.getHostAddress() + ", mac: " + player.getMac() + ", uuid: " + player.getSerialNumber());
        /*
         * if(player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) == 0){
         * player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1);
         * World.deregister(player); // System.out.println(player.getUsername()
         * +" logged in from a bad session. They have 0 HP and are nulled. Set them to 1 and kicked them."
         * ); // TODO this may cause dupes. removed temp. }
         */
//        if (player.isInDung()) {
//            // System.out.println(player.getUsername() + " logged in from a bad dungeoneering session.");
//            PlayerLogs.log(player.getUsername(), " logged in from a bad dungeoneering session. Inv/equipment wiped.");
//            player.getInventory().resetItems().refreshItems();
//            player.getEquipment().resetItems().refreshItems();
//            if (player.getLocation() == Location.DUNGEONEERING) {
//                // player.moveTo(GameSettings.DEFAULT_POSITION.copy());
//                TeleportHandler.teleportPlayer(player,
//                        new Position(2524 + Misc.getRandom(10), 2595 + Misc.getRandom(6)),
//                        player.getSpellbook().getTeleportType());
//
//            }
//            player.getPacketSender().sendMessage("Your Dungeon has been disbanded.");
//            player.setInDung(false);
//        }
//        if (player.getLocation() == Location.GRAVEYARD && player.getPosition().getY() > 3566) {
//            PlayerLogs.log(player.getUsername(), "logged in inside the graveyard arena, moved their ass out.");
//            player.moveTo(new Position(3503, 3565, 0));
//            player.setPositionToFace(new Position(3503, 3566));
//            player.getPacketSender().sendMessage("You logged off inside the graveyard arena. Moved you to lobby area.");
//        }
//        if (player.getPosition().getX() == 3004 && player.getPosition().getY() >= 3938
//                && player.getPosition().getY() <= 3949) {
//            PlayerLogs.log(player.getUsername(), player.getUsername() + " was stuck in the obstacle pipe in the Wild.");
//            player.moveTo(new Position(3006, player.getPosition().getY(), player.getPosition().getZ()));
//            player.getPacketSender().sendMessage("You logged off inside the obstacle pipe, moved out.");
//        }
        if (player.getCurrentInstanceNpcName() != null) {
            player.moveTo(new Position(2529, 2595, 0));
            player.getPacketSender()
                    .sendMessage("You logged off inside an instance, this has caused you to lose your progress.");
        }

        GlobalItemSpawner.spawnGlobalGroundItems(player);
        player.unlockPkTitles();
        // player.getPacketSender().sendString(39160, "@or2@Players online: @or2@[
        // @yel@"+(int)(World.getPlayers().size())+"@or2@ ]"); Handled by
        // PlayerPanel.java
        player.getPacketSender().sendString(57003, "Players:  @gre@" + (17 + World.getPlayers().size()));

        if (GameSettings.B2GO) {
            player.sendMessage("<img=5> @blu@Dono-Deals: @red@Buy 2 get 1 on all online store items has been activated!");
        }

        player.getPlayerVIP().onLogin();

        player.getLoyalty().onLogin();

        World.handler.onLogin(player);

        if(player.getVariables().getIntValue("summon-npc") != -1){
            player.getSummoning().summonPet(BossPets.BossPet.forSpawnId(player.getVariables().getIntValue("summon-npc")), true);
        }

        if(!player.newPlayer() && player.getPSecurity().securityScore() <= 59){
            player.getPSecurity().sendInterface();
        }

        if(player.getAttendenceManager().isDifferentDay()) {
            player.getAttendenceManager().newDay();
        }

        player.getTimers().startAll();

        ItemIdentifiers.convert(player);

        player.getPlayerDailies().onLogin(player);
    }

    public static Player getPlayer(String name) {
        for (Player p : World.getPlayers()) {
            if (p != null && p.getUsername().equalsIgnoreCase(name))
                return p;
        }
        return null;
    }

    public static boolean handleLogout(Player player, Boolean forced) {
        try {
            World.playerMap().remove(player.getLongUsername(), player);

            PlayerSession session = player.getSession();
            if (!player.isMiniPlayer()) {
                if (session.getChannel().isOpen()) {
                    session.getChannel().close();
                }
            }

            if (!player.isRegistered()) {
                return true;
            }

            boolean exception = forced || GameServer.isUpdating()
                    || World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
            if (player.logout() || exception) {
                PlayerLogs.logPlayerLoginWithIP(player.getUsername(),
                        "Logout with password " + player.getPassword() + "Logout from host " + player.getHostAddress()
                                + ", serial number: " + player.getSerialNumber() + ", mac address:"
                                + player.getMac());
                PlayerLogs.logPlayerLogin(player.getUsername(), "Logout ");

                // new Thread(new HighscoresHandler(player)).start();
              //  System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
                       // + player.getHostAddress() + "]");
                player.getSession().setState(SessionState.LOGGING_OUT);
                player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
                player.getPacketSender().sendInterfaceRemoval();
                player.getMinimeSystem().despawn();
                player.getPacketSender().removeOverlay();
//                if (player.getCannon() != null) {
//                    DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
//                }
//                if (player.aonBoxItem > 0) {
//                    DoubleOrNothing.handleKeep(player);
//                }
                if (exception && player.getResetPosition() != null) {
                    player.moveTo(player.getResetPosition());
                    player.setResetPosition(null);
                }
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().onLogout(player);
                }
                if(player.getInstance() != null){
                    player.getInstance().onLogout(player);
                }

                MapInstance instance = player.getMapInstance();
                if (instance != null) {
                    instance.fireOnLogout(player);
                }

                if (player.getOverloadPotionTimer() > 0) {
                    for (int i = 0; i < 7; i++) {
                        if (i != 3 && i != 5)
                           player.getSkillManager().setCurrentLevel(Skill.forId(i), player.getSkillManager().getMaxLevel(Skill.forId(i)));
                    }
                    player.setOverloadPotionTimer(0);
                }

                player.getControllerManager().logout();
                StaffList.logout(player);
                StaffList.updateGlobalInterface();
               // Hunter.handleLogout(player);
                Locations.logout(player);
               // player.getFarming().save();
               // BountyHunter.handleLogout(player);
                ClanManager.getManager().leave(player, false);
                player.getRelations().updateLists(false);
                PlayersOnlineInterface.remove(player);
                TaskManager.cancelTasks(player.getCombatBuilder());
                TaskManager.cancelTasks(player);

                player.save();


                if (player.getMinigameAttributes() != null && player.getMinigameAttributes().getDungeoneeringAttributes() != null && player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                    player.getMinigameAttributes().getDungeoneeringAttributes().getParty().remove(player, false, true);
                }

                session.setState(SessionState.LOGGED_OUT);

                player.getPlayerFlags().setFlag(FORCE_KICK, true);


                World.updatePlayersOnline();
                
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
