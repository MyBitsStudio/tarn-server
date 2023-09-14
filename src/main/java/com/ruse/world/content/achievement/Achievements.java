//package com.ruse.world.content.achievement;
//
//import com.ruse.model.Item;
//import com.ruse.model.definitions.ItemDefinition;
//import com.ruse.util.Misc;
//import com.ruse.world.World;
//import com.ruse.world.entity.impl.player.Player;
//import lombok.Getter;
//
//import java.util.EnumSet;
//import java.util.Set;
//
//public class Achievements {
//
//    public static void doProgress(Player player, Achievement achievement) {
//        doProgress(player, achievement, 1);
//    }
//
//    public static void doProgress(Player player, Achievement achievement, int amount) {
//        if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
//            int currentAmount = player.getAchievements().getAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId());
//            int tier = achievement.getDifficulty().ordinal();
//            if (currentAmount < achievement.getAmount() && !player.getAchievements().isComplete(achievement.getDifficulty().ordinal(), achievement.getId())) {
//                player.getAchievements().setAmountRemaining(tier, achievement.getId(), currentAmount + amount);
//                if ((currentAmount + amount) >= achievement.getAmount()) {
//                    String name = achievement.name().replaceAll("_", " ");
//                    player.getPacketSender().sendMessage(
//                            "[ACHIEVEMENT] @blu@" + Misc.capitalizeJustFirst(achievement.name().replaceAll("_", " ") + " @bla@completed, claim your reward!"));
//
//                    if (achievement.getDifficulty().ordinal() == 2) {
//                        World.sendNewsMessage("<col=ff0000>" + Misc.capitalizeJustFirst(player.getUsername())
//                                + " <col=a72800>completed the achievement <col=ff0000>" + Misc.capitalizeJustFirst(achievement.name().replaceAll("_", " ") + "!"));
//                    }
//                }
//            }
//        }
//    }
//
//    public static void claimReward(Player player, Achievement achievement) {
//        if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
//            if (!player.getAchievements().isComplete(achievement.getDifficulty().ordinal(), achievement.getId())) {
//                int currentAmount = player.getAchievements().getAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId());
//                int tier = achievement.getDifficulty().ordinal();
//                if (currentAmount >= achievement.getAmount()) {
//                    if (achievement.getRewards() != null) {
//                        player.getPacketSender().sendMessage("Your achievement reward(s) has been added to your account.");
//                        player.getAchievements().setComplete(tier, achievement.getId(), true);
//                        for (Item item : achievement.getRewards())
//                            player.getInventory().add(item.getId(), item.getAmount());
//
//                        for (int x = 0; x < achievement.getPoints().length; x++) {
//                            if (achievement.getPoints()[x][0].equalsIgnoreCase("achievement")) {
//                                player.getPointsHandler().setAchievementPoints(Integer.parseInt(achievement.getPoints()[x][1]), true);
//                            } else if (achievement.getPoints()[x][0].equalsIgnoreCase("skill")) {
//                                player.getPointsHandler().setSkillingPoints(Integer.parseInt(achievement.getPoints()[x][1]), true);
//                            } else if (achievement.getPoints()[x][0].equalsIgnoreCase("voting")) {
//                                player.getPointsHandler().setVotingPoints(Integer.parseInt(achievement.getPoints()[x][1]), true);
//                            } else if (achievement.getPoints()[x][0].equalsIgnoreCase("slayer")) {
//                                player.getPointsHandler().setSlayerPoints(Integer.parseInt(achievement.getPoints()[x][1]), true);
//                            }
//                        }
//                    }
//                } else {
//                    player.getPacketSender().sendMessage("You have yet to complete this achievement!");
//                }
//            } else {
//                player.getPacketSender().sendMessage("You have already claimed this reward.");
//            }
//        }
//    }
//
//    public static void reset(Player player, Achievement achievement) {
//        if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
//            if (!player.getAchievements().isComplete(achievement.getDifficulty().ordinal(), achievement.getId())) {
//                player.getAchievements().setAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId(), 0);
//            }
//        }
//    }
//
//    public static void resetDailys(Player player) {
//        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
//            if (achievement.getDifficulty().equals(AchievementDifficulty.DAILY)) {
//                player.getAchievements().setAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId(), 0);
//                player.getAchievements().setComplete(3, achievement.getId(), false);
//            }
//        }
//        player.getAchievements().setDailyTaskDate(player.getAchievements().getTodayDate());
//    }
//
//    public static void resetEasys(Player player) {
//        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
//            if (achievement.getDifficulty().equals(AchievementDifficulty.EASY)) {
//                player.getAchievements().setAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId(), 0);
//                player.getAchievements().setComplete(0, achievement.getId(), false);
//            }
//        }
//    }
//
//    public static void resetMediums(Player player) {
//        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
//            if (achievement.getDifficulty().equals(AchievementDifficulty.MEDIUM)) {
//                player.getAchievements().setAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId(), 0);
//                player.getAchievements().setComplete(1, achievement.getId(), false);
//            }
//        }
//    }
//
//    public static void resetHards(Player player) {
//        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
//            if (achievement.getDifficulty().equals(AchievementDifficulty.HARD)) {
//                player.getAchievements().setAmountRemaining(achievement.getDifficulty().ordinal(), achievement.getId(), 0);
//                player.getAchievements().setComplete(2, achievement.getId(), false);
//            }
//        }
//    }
//
//    public static int getMaximumAchievements() {
//        return Achievement.ACHIEVEMENTS.size();
//    }
//
//    public enum Achievement {
//
//        // EASY
//        VOTE_10_TIMES(1, 1524, AchievementDifficulty.EASY, "Vote for Tarn 10 times by using the ::vote command!", 10, new String[][]{}, new Item(23057, 1)),
//        COMPLETE_20_SLAYER_TASKS(9, 643, AchievementDifficulty.EASY, "Complet 20 Slayer tasks", 20, new String[][]{}, new Item(2736, 5)),
//        BURY_100_BONES(15, 640, AchievementDifficulty.EASY, "Bury 100 bones", 100, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_MYSTIC(2, 625, AchievementDifficulty.EASY, "Kill 100 Mystic", 100, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_NIGHTMARE(3, 625, AchievementDifficulty.EASY, "Kill 100 Nightmare", 100, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_PATIENCE(4, 625, AchievementDifficulty.EASY, "Kill 100 Patience", 100, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_ZINQRUX(5, 625, AchievementDifficulty.EASY, "Kill 250 Zinqrux", 250, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_ABERRANT(6, 625, AchievementDifficulty.EASY, "Kill 250 Dr. Aberrant", 250, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_INFERNO(7, 625, AchievementDifficulty.EASY, "Kill 250 Inferno", 250, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_NAGENDRA(8, 625, AchievementDifficulty.EASY, "Kill 350 Nagendra", 350, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_KOLGAL(10, 625, AchievementDifficulty.EASY, "Kill 400 Kol'gal", 400, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_YISDAR(11, 625, AchievementDifficulty.EASY, "Kill 400 Yisdar", 400, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_IGTHAUR(12, 625, AchievementDifficulty.EASY, "Kill 400 Ig'thaur", 400, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_ZERNATH(13, 625, AchievementDifficulty.EASY, "Kill 400 Zernath", 400, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_AVALON(14, 625, AchievementDifficulty.EASY, "Kill 500 Avalon", 500, new String[][]{}, new Item(10025, 1), new Item(995, 100000)),
//        KILL_5000_NPCS(16, 625, AchievementDifficulty.EASY, "Kill 5,000 Npcs", 5000, new String[][]{}, new Item(23057, 1)),
//        KILL_45_GLOBAL_BOSSES(17, 625, AchievementDifficulty.EASY, "Kill 45 Global bosses", 45, new String[][]{}, new Item(15003, 1)),
//       // REACH_1500_TOTAL_LEVEL(18, 529, AchievementDifficulty.EASY, "Reach 1500 total level", 1, new String[][]{}, new Item(ItemDefinition.COIN_ID, 50_000_000)),
//
//        // MEDIUM
//        VOTE_50_TIMES(1, 1524, AchievementDifficulty.MEDIUM, "Vote for Tarn 50 times by using the ::vote command!", 50, new String[][]{}, new Item(23058, 1), new Item(15003, 1)),
//        COMPLETE_50_SLAYER_TASKS(9, 643, AchievementDifficulty.MEDIUM, "Complet 50 Slayer tasks", 50, new String[][]{}, new Item(2736, 10)),
//        BURY_1000_BONES(15, 640, AchievementDifficulty.MEDIUM, "Bury 1000 bones", 1000, new String[][]{}, new Item(10025, 2), new Item(995, 500000)),
//        KILL_DOOMWATCHER(2, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Doomwatcher", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_MAZE_GUARDIAN(3, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Maze Guardian", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_MISCREATION(4, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Miscreation", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_AVATAR_TITAN(5, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Avatar Titan", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_ZORBAK(6, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Zorbak", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_DEATH_GOD(7, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Death God", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_EMERALD_SLAYER(8, 625, AchievementDifficulty.MEDIUM, "Kill 1,000 Emerald Slayer", 1000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_GOLDEN_GOLEM(10, 625, AchievementDifficulty.MEDIUM, "Kill 1,500 Golden Golem", 1500, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_LUFFY(11, 625, AchievementDifficulty.MEDIUM, "Kill 1,500 Luffy", 1500, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_BROLY(12, 625, AchievementDifficulty.MEDIUM, "Kill 1,500 Broly", 1500, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_BOWSER(13, 625, AchievementDifficulty.MEDIUM, "Kill 2,000 Bowser", 2000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_SASUKE(14, 625, AchievementDifficulty.MEDIUM, "Kill 2,000 Sasuke", 2000, new String[][]{}, new Item(10029, 1), new Item(995, 500000)),
//        KILL_10000_NPCS(16, 625, AchievementDifficulty.MEDIUM, "Kill 10,000 Npcs", 10000, new String[][]{}, new Item(23057, 1)),
//        //REACH_2000_TOTAL_LEVEL(17, 529, AchievementDifficulty.MEDIUM, "Reach 2000 total level", 1, new String[][]{}, new Item(ItemDefinition.COIN_ID, 100_000_000)),
//        // HARD
//        VOTE_100_TIMES(1, 1524, AchievementDifficulty.HARD, "Vote 100 Times", 100, new String[][]{}, new Item(23058, 2), new Item(15002, 1)),
//        COMPLETE_150_SLAYER_TASKS(9, 643, AchievementDifficulty.HARD, "Complet 150 Slayer tasks", 150, new String[][]{}, new Item(2736, 15)),
//        BURY_9999_BONES(16, 640, AchievementDifficulty.HARD, "Bury 10,000 bones", 10000, new String[][]{}, new Item(10025, 3), new Item(995, 10_000_000)),
//        KILL_SANCTUM_GOLEM(2, 625, AchievementDifficulty.HARD, "Kill 2,000 Sanctum Golem", 2000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_MUTANT_HYDRA(3, 625, AchievementDifficulty.HARD, "Kill 2,000 Mutant Hydra", 2000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_GORVEK(4, 625, AchievementDifficulty.HARD, "Kill 2,000 Gorvek", 2000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_DRAGONITE(5, 625, AchievementDifficulty.HARD, "Kill 4,000 Dragonite", 4000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_ASMODEUS(6, 625, AchievementDifficulty.HARD, "Kill 4,000 Asmodeus", 4000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_MALVEK(7, 625, AchievementDifficulty.HARD, "Kill 4,000 Malvek", 4000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_ONYX_GRIFFIN(8, 625, AchievementDifficulty.HARD, "Kill 4,000 Onyx Griffin", 4000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_ZEIDAN_GRIMM(10, 625, AchievementDifficulty.HARD, "Kill 4,000 Zeidan Grimm", 4000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_AGTHOMOTH(11, 625, AchievementDifficulty.HARD, "Kill 5,000 Ag'thomoth", 5000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_LILINRYSS(12, 625, AchievementDifficulty.HARD, "Kill 5,000 Lilinryss", 5000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_GROUDON(13, 625, AchievementDifficulty.HARD, "Kill 5,000 Groudon", 5000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_VARTHRAMOTH(14, 625, AchievementDifficulty.HARD, "Kill 5,000 Varthramoth", 5000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_TYRANT_LORD(15, 625, AchievementDifficulty.HARD, "Kill 5,000 Tyrant Lord", 5000, new String[][]{}, new Item(10027, 1), new Item(995, 10_000_000)),
//        KILL_25000_NPCS(17, 625, AchievementDifficulty.HARD, "Kill 25,000 Npcs", 25000, new String[][]{}, new Item(10027, 1), new Item(995, 25_000_000)),
//        //MAX_OUT_ALL_SKILLS(18, 529, AchievementDifficulty.HARD, "Max out all skills", 1, new String[][]{}, new Item(ItemDefinition.COIN_ID, 250_000_000)),
//        ;
//
//        public static final Set<Achievement> ACHIEVEMENTS = EnumSet.allOf(Achievement.class);
//        final String[][] points;
//        private final AchievementDifficulty difficulty;
//        private final AchievementRequirement requirement;
//        private final String description;
//        private final int amount;
//        private final int npcId;
//        private final int identification;
//        @Getter
//        private final int spriteID;
//        private final Item[] rewards;
//
//        Achievement(int identification, int spriteID, AchievementDifficulty difficulty, int npcId, String description, int amount, String[][] points,
//                    Item... rewards) {
//            this.identification = identification;
//            this.spriteID = spriteID;
//            this.difficulty = difficulty;
//            this.requirement = null;
//            this.npcId = npcId;
//            this.description = description;
//            this.amount = amount;
//            this.points = points;
//            this.rewards = rewards;
//
//            for (Item b : rewards)
//                if (b.getAmount() == 0)
//                    b.setAmount(1);
//
//        }
//
//        Achievement(int identification, int spriteID, AchievementDifficulty difficulty, String description, int amount, String[][] points,
//                    Item... rewards) {
//            this.identification = identification;
//            this.spriteID = spriteID;
//            this.difficulty = difficulty;
//            this.requirement = null;
//            this.npcId = -1;
//            this.description = description;
//            this.amount = amount;
//            this.points = points;
//            this.rewards = rewards;
//
//            for (Item b : rewards)
//                if (b.getAmount() == 0)
//                    b.setAmount(1);
//
//        }
//
//        public static Achievement getAchievement(AchievementDifficulty tier, int ordinal) {
//            for (Achievement achievement : ACHIEVEMENTS)
//                if (achievement.getDifficulty() == tier && achievement.ordinal() == ordinal)
//                    return achievement;
//            return null;
//        }
//
//        public static boolean hasRequirement(Player player, AchievementDifficulty tier, int ordinal) {
//            for (Achievement achievement : ACHIEVEMENTS) {
//                if (achievement.getDifficulty() == tier && achievement.ordinal() == ordinal) {
//                    if (achievement.getRequirement() == null)
//                        return true;
//                    if (achievement.getRequirement().isAble(player))
//                        return true;
//                }
//            }
//            return false;
//        }
//
//        public int getNpcId() {
//            return npcId;
//        }
//
//        public int getId() {
//            return identification;
//        }
//
//        public AchievementDifficulty getDifficulty() {
//            return difficulty;
//        }
//
//        AchievementRequirement getRequirement() {
//            return requirement;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public int getAmount() {
//            return amount;
//        }
//
//        public String[][] getPoints() {
//            return points;
//        }
//
//        public Item[] getRewards() {
//            return rewards;
//        }
//    }
//}
