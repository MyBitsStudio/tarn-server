package com.ruse.world.packages.attendance;

import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

@DisallowConcurrentExecution
public class DailyResetScheduler implements Job {

    private static final String CRON_EXPRESSION = "0 0 0 * * ?";

    public static final CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("MidnightReset")
            .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION).inTimeZone(TimeZone.getTimeZone("UTC")).withMisfireHandlingInstructionFireAndProceed())
            .startNow()
            .build();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        AttendanceManager.nextDay();
    }

    public static Date nextFireTime() {
        try {
            CronExpression cronExpression = new CronExpression(CRON_EXPRESSION);
            cronExpression.setTimeZone(TimeZone.getTimeZone("UTC"));
            return cronExpression.getNextValidTimeAfter(Date.from(Instant.now()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static void initialize() {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(JobBuilder.newJob(DailyResetScheduler.class).build(), trigger);
        scheduler.start();
    }

}
