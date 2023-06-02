package com.ruse.world.packages.attendance;

import com.ruse.model.Item;
import com.ruse.net.SessionState;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerSaving;

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

    public void newDay() {
        if(!lastLoggedInDate.getMonth().equals(LocalDate.now(ZoneOffset.UTC).getMonth())) {
            playerAttendanceProgress.clear();
            p.getPSettings().setSetting("donator-unlock", -1);
            p.getPSettings().setSetting("summer-unlock", false);
        }
        lastLoggedInDate = LocalDate.now(ZoneOffset.UTC);
        for(AttendanceTab tab : getTabs()) {
            AttendanceProgress attendanceProgress = playerAttendanceProgress.computeIfAbsent(tab, x -> new AttendanceProgress());
            int nextUnclaimedDay = getNextUnclaimedDay(tab);
            if(nextUnclaimedDay != -1) {
                Item item = getRewardOfTheDay(tab, nextUnclaimedDay);
                if(item == null) {
                    p.getPacketSender().sendMessage("@red@This day has no reward.");
                    return;
                }
                if(!unlocked(p, tab)){
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
        if(itemsArray != null) {
            return itemsArray[day-1];
        }
        return null;
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

    public boolean unlocked(Player player, AttendanceTab tab){
        switch(tab){
            case LOYAL:
                return true;
            case DONATOR:
                return player.getPSettings().getDoubleValue("donator-unlock") == Calendar.MONTH;
            case SUMMER:
                return player.getPSettings().getBooleanValue("summer-unlock");
            default:
                return false;
        }
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

        if(p.getPSettings().getIntValue("donator-unlock") == Calendar.MONTH)
            tabs.add(AttendanceTab.DONATOR);

        if(p.getPSettings().getBooleanValue("summer-unlock"))
            tabs.add(AttendanceTab.SUMMER);

        return tabs;
    }

    public boolean handleTabs(int buttonId){
        switch(buttonId){
            case 150006 : p.getAttendenceUI().showInterface(AttendanceTab.LOYAL); return true;
            case 150136 : p.getAttendenceUI().showInterface(AttendanceTab.DONATOR); return true;
            case 150138 : p.getAttendenceUI().showInterface(AttendanceTab.SUMMER); return true;
            default : return false;
        }
    }


}
