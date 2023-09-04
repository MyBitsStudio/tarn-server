//package com.ruse.world.packages.seasonpass;
//
//import com.ruse.world.World;
//import com.ruse.world.entity.impl.player.Player;
//import lombok.SneakyThrows;
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//
//import java.time.LocalDate;
//import java.util.TimeZone;
//
//@DisallowConcurrentExecution
//public class SeasonPassScheduler implements Job {
//
//    private static SeasonPassScheduler INSTANCE;
//
//    public Scheduler scheduler;
//
//    private final CronTrigger trigger = TriggerBuilder.newTrigger()
//            .withIdentity("SeasonPassReset")
//            .withSchedule(CronScheduleBuilder.cronSchedule(generateCron()).inTimeZone(TimeZone.getTimeZone("UTC")).withMisfireHandlingInstructionFireAndProceed())
//            .startNow()
//            .build();
//
//    public SeasonPassScheduler() {
//        INSTANCE = this;
//    }
//
//    private String generateCron() {
//        StringBuilder cron = new StringBuilder();
//        LocalDate date = SeasonPassConfig.getInstance().getLocalTime();
//        cron.append("59 ")
//                .append("59 ")
//                .append("23 ")
//                .append(date.getDayOfMonth())
//                .append(" ")
//                .append(date.getMonth().getValue())
//                .append(" ?");
//        return cron.toString();
//    }
//
//    @SneakyThrows
//    public void register() {
//        scheduler = StdSchedulerFactory.getDefaultScheduler();
//        scheduler.scheduleJob(JobBuilder.newJob(this.getClass()).build(), trigger);
//        scheduler.start();
//    }
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        SeasonPassConfig.getInstance().setSeason(SeasonPassConfig.getInstance().getSeason()+1);
//        SeasonPassConfig.getInstance().rewriteConfig();
//        for(Player player : World.getPlayers()) {
//            if(player == null) continue;
//            SeasonPass seasonPass = player.getSeasonPass();
//            SeasonPassManager.resetSeasonPass(seasonPass);
//        }
//    }
//
//    public static SeasonPassScheduler getInstance() {
//        return INSTANCE;
//    }
//}
