package com.code.services.workflow.surveillance.jobs;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.code.services.log4j.Log4j;

public class SecurityStatusQuartzListener implements ServletContextListener {
	Scheduler scheduler = null;

	@Override
	public void contextInitialized(ServletContextEvent servletContext) {
		Log4j.traceInfo(SecurityStatusQuartzListener.class, "Context Initialized");
		try {
			// Setup the Job class and the Job group
			JobDetail job = newJob(SecurityStatusQuartzJob.class).withIdentity("CronQuartzJob", "Group").build();
			// Create a Trigger that fires everyday at 9:30 Am.
			Trigger trigger = newTrigger().withIdentity("TriggerName", "Group").withSchedule(CronScheduleBuilder.cronSchedule("0 2 15 1/1 * ? *")).build();
			// Setup the Job and Trigger with Scheduler & schedule jobs
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			Log4j.traceErrorException(SecurityStatusQuartzListener.class, e, "SecurityStatusQuartzListener");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContext) {
		System.out.println("Context Destroyed");
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			Log4j.traceErrorException(SecurityStatusQuartzListener.class, e, "SecurityStatusQuartzListener");
		}
	}
}