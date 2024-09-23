package com.joshbarros.taskmanager;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerSetup {

    public static void startScheduler() {
        try {
            // Define the job and link to our QuartzJob class
            JobDetail job = JobBuilder.newJob(QuartzJob.class)
                    .withIdentity("GraphQLJob")
                    .build();

            // Trigger the job to run every 1 hour (3600 seconds)
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("GraphQLTrigger")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(3600)
                            .repeatForever())
                    .build();

            // Schedule the job
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
