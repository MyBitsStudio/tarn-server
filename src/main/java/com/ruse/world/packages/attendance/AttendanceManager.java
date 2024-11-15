package com.ruse.world.packages.attendance;

import com.ruse.model.Item;
import com.ruse.net.SessionState;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerSaving;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;

public class AttendanceManager {
    private final Player p;
    private LocalDate lastLoggedInDate;
    private final HashMap<AttendanceTab, AttendanceProgress> playerAttendanceProgress = new HashMap<>();

    public AttendanceManager(Player p) {
        this.p = p;
        this.lastLoggedInDate = LocalDate.now(ZoneOffset.UTC);
    }

    public void claim(){
        if(!World.attributes.getSetting("calendar")){
            p.getPacketSender().sendMessage("@red@The calendar is currently disabled.");
            return;
        }
        LocalDate now = LocalDate.now(ZoneOffset.UTC);
        if(!WorldIPChecker.getInstance().check(p, "calendar-"+now.getMonth())){
            p.sendMessage("You can only claim rewards once per game mode per IP address.");
            return;
        }
        if(lastLoggedInDate.equals(LocalDate.now(ZoneOffset.UTC))) {
            p.getPacketSender().sendMessage("@red@You have already claimed your attendance reward for today.");
        } else {
            lastLoggedInDate = now;
            for(AttendanceTab tab : getTabs()) {
                AttendanceProgress attendanceProgress = playerAttendanceProgress.computeIfAbsent(tab, x -> new AttendanceProgress());
                int nextUnclaimedDay = getNextUnclaimedDay(tab);
                if(nextUnclaimedDay != -1) {
                    Item item = getRewardOfTheDay(tab, nextUnclaimedDay);
                    if(item == null || !unlocked(p, tab)){
                        continue;
                    }
                    if(attendanceProgress.put(nextUnclaimedDay)) {
                        p.getPacketSender().sendMessage("@red@You have been given " + item.getDefinition().getName() + " x " + item.getAmount() + " as attendance reward for day " + nextUnclaimedDay + "!");
                        p.addItemUnderAnyCircumstances(item);
                        PlayerSaving.save(p);
                    }
                }
            }

        }

    }

    public void newDay() {
        if(!lastLoggedInDate.getMonth().equals(LocalDate.now(ZoneOffset.UTC).getMonth())) {
            playerAttendanceProgress.clear();
            p.getPSettings().setSetting("donator", false);
        }

        if(!lastLoggedInDate.equals(LocalDate.now(ZoneOffset.UTC))) {
            p.sendMessage("@yel@ [DAILY] You have a daily waiting to claim!");
        }
    }

    public static void nextDay() {
        for(Player onlinePlayer : World.getPlayers()) {
            if(onlinePlayer != null && onlinePlayer.isRegistered() && onlinePlayer.getSession().getState() != SessionState.LOGGING_OUT && onlinePlayer.getAttendenceManager().isDifferentDay()) {
                onlinePlayer.getAttendenceManager().newDay();
            }
        }
    }

    public boolean isDifferentDay() {
        return !LocalDate.now(ZoneOffset.UTC).isEqual(lastLoggedInDate);
    }

    private static int getCurrentDay() {
        return LocalDate.now(ZoneOffset.UTC).getDayOfMonth();
    }

    public Item[] getMonthlyRewardAsArray(AttendanceTab tab) {
        MonthlyReward monthlyArray = Arrays.stream(tab.getMonthlyReward()).filter(monthlyReward -> monthlyReward.getMonth().equals(LocalDate.now(ZoneOffset.UTC).getMonth())).findFirst().orElse(null);
        if(monthlyArray != null) {
            return monthlyArray.getItems();
        }
        return null;
    }

    private Item getRewardOfTheDay(AttendanceTab tab, int day) {
        Item[] itemsArray = getMonthlyRewardAsArray(tab);
        if(day-1 >= itemsArray.length) return null;
        return itemsArray[day - 1];
    }

    public int getNextUnclaimedDay(AttendanceTab tab) {
        int length = LocalDate.now(ZoneOffset.UTC).lengthOfMonth();
        AttendanceProgress progress = playerAttendanceProgress.computeIfAbsent(tab, x -> new AttendanceProgress());
        for(int i = 1; i <= length; i++) {
            if(!progress.hasReceived(i)) {
                return i;
            }
        }
        return -1;
    }

    public boolean unlocked(Player player, @NotNull AttendanceTab tab){

        return switch (tab) {
            case LOYAL, EVENT -> true;
            case DONATOR -> player.getPSettings().getBooleanValue("donator");
            case FALL -> player.getPSettings().getBooleanValue("fall-unlock");
            default -> false;
        };
    }

    public LocalDate getLastLoggedInDate() {
        return lastLoggedInDate;
    }

    public void setLastLoggedInDate(LocalDate lastLoggedInDate) {
        this.lastLoggedInDate = lastLoggedInDate;
    }

    public HashMap<AttendanceTab, AttendanceProgress> getPlayerAttendanceProgress() {
        return playerAttendanceProgress;
    }

    public List<AttendanceTab> getTabs() {
        List<AttendanceTab> tabs = new ArrayList<>();

        tabs.add(AttendanceTab.LOYAL);

        if(p.getPSettings().getBooleanValue("donator")) {
            tabs.add(AttendanceTab.DONATOR);
        }

        if(p.getPSettings().getBooleanValue("fall-unlock")) {
            tabs.add(AttendanceTab.FALL);
        }

        tabs.add(AttendanceTab.EVENT);

        return tabs;
    }

    public boolean handleTabs(int buttonId){
        switch (buttonId) {
            case 150006 -> {
                p.getAttendenceUI().showInterface(AttendanceTab.LOYAL);
                return true;
            }
            case 150136 -> {
                if (p.getPSettings().getBooleanValue("donator"))
                    p.getAttendenceUI().showInterface(AttendanceTab.DONATOR);
                else
                    p.getPacketSender().sendMessage("You must unlock this tab by donating.");
                return true;
            }
            case 150138 -> {
                if (p.getPSettings().getBooleanValue("fall-unlock"))
                    p.getAttendenceUI().showInterface(AttendanceTab.FALL);
                else
                    p.getPacketSender().sendMessage("You must unlock this tab with a Fall Scroll.");
                return true;
            }
            case 150140 -> {
                p.getAttendenceUI().showInterface(AttendanceTab.EVENT);
                return true;
            }
            default -> {
                return false;
            }
        }
    }


}
