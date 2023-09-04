package com.ruse.world;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.attendance.AttendanceManager;
import com.ruse.world.packages.seasonpass.SeasonPass;
import com.ruse.world.packages.seasonpass.SeasonPassConfig;
import com.ruse.world.packages.seasonpass.SeasonPassManager;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

public class WorldCalendar implements Job {

    private static WorldCalendar instance;

    public static WorldCalendar getInstance() {
        if(instance == null)
            instance = new WorldCalendar();
        return instance;
    }

    public String getTime(){
        return dates.toString();
    }

    private static final String CRON_EXPRESSION = "0 0 0 * * ?";

    private final int[] date = new int[3];
    private final LocalDate dates = LocalDate.now( ZoneOffset.UTC );

    public void load(){
        date[0] = dates.get( ChronoField.MONTH_OF_YEAR);
        date[1] = dates.get( ChronoField.DAY_OF_MONTH);
        date[2] = dates.get( java.time.temporal.ChronoField.YEAR );
        System.out.println("Loaded date: " + date[0] + "/" + date[1] + "/" + date[2]);
    }

    public int getDay(){
        return date[1];
    }

    public int getMonth(){
        return date[0];
    }

    public boolean isWeekend(){
        int dayOfWeek = dates.get( java.time.temporal.ChronoField.DAY_OF_WEEK );
        return dayOfWeek == 6 || dayOfWeek == 7;
    }

    private static final CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("MidnightReset")
            .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION).inTimeZone(TimeZone.getTimeZone("UTC")).withMisfireHandlingInstructionFireAndProceed())
            .startNow()
            .build();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        AttendanceManager.nextDay();
        World.handler.reload();
        handleSeasonPass();
        load();
    }

    @SneakyThrows
    public static void initialize() {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(JobBuilder.newJob(WorldCalendar.class).build(), trigger);
        scheduler.start();
    }

    public boolean isWithinDate(int month, int day, int end, int endf){
        LocalDate now = LocalDate.now( ZoneOffset.UTC ).withMonth(month).withDayOfMonth(day);
        LocalDate endd = LocalDate.now( ZoneOffset.UTC ).withMonth(end).withDayOfMonth(endf);
        return !dates.isBefore(now) && dates.isBefore(endd);
    }

    public void handleSeasonPass(){
        LocalDate date = SeasonPassConfig.getInstance().getLocalTime();
        if(!dates.isBefore(date)){
            SeasonPassConfig.getInstance().setSeason(SeasonPassConfig.getInstance().getSeason()+1);
            SeasonPassConfig.getInstance().rewriteConfig();
            for(Player player : World.getPlayers()) {
                if(player == null) continue;
                SeasonPass seasonPass = player.getSeasonPass();
                SeasonPassManager.resetSeasonPass(seasonPass);
            }
        }
    }
}
