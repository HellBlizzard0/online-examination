package com.code.services;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.code.dal.CustomSession;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.assignment.AssignmentEndNotifyThread;
import com.code.services.infosys.labcheck.DelayedLabChecksResultJob;
import com.code.services.infosys.labcheck.PeriodicRetestThread;
import com.code.services.infosys.surveillance.SurveillanceReportNotificationThread;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionNotificationThread;
import com.code.services.workflow.surveillance.jobs.ConversationReminderJob;
import com.code.services.workflow.surveillance.jobs.SecurityStatusQuartzJob;

public abstract class BaseService {
	protected static final int HIJRI_YEAR_DAYS = 354;
	private static ResourceBundle config = ResourceBundle.getBundle("com.code.resources.config");
	protected static String appContextPath = null;
	protected static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

	/**
	 * Get configuration by name
	 * 
	 * @param key
	 * @return configuration value
	 */
	public static String getConfig(String key) {
		return config.getString(key);
	}

	/**
	 * 
	 * @param securityStatusQuartzJob
	 * @param jobIdentity
	 * @param triggerIdentity
	 * @param enableFlag
	 */
	protected static void initializeYaqeenJob(Class<SecurityStatusQuartzJob> securityStatusQuartzJob, String jobIdentity, String triggerIdentity, Boolean enableFlag) {
		if (!enableFlag)
			return;

		// Setup the Job class and the Job group
		JobDetail job = newJob(securityStatusQuartzJob).withIdentity(jobIdentity, "Group").build();

		// Create a Trigger that fires everyday at 12:00 AM.
		Trigger trigger = newTrigger().withIdentity(triggerIdentity, "Group").withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1/1 * ? *")).build();

		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			Log4j.traceErrorException(BaseService.class, e, "BaseService");
		}
	}

	/**
	 * 
	 * @param ConversationReminderQuartzJob
	 * @param jobIdentity
	 * @param triggerIdentity
	 * @param enableFlag
	 * @param triggerAfterTimeMilli
	 */
	protected static void initializeSecurityReminderJob(Class<ConversationReminderJob> ConversationReminderQuartzJob, String jobIdentity, String triggerIdentity, Boolean enableFlag) {
		if (!enableFlag)
			return;

		// Setup the Job class and the Job group
		JobDetail job = newJob(ConversationReminderQuartzJob).withIdentity(jobIdentity, "Group").build();

		// Build a trigger that will fire next hour and fires every two hours.
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerIdentity, "Group").withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(InfoSysConfigurationService.getConvReminderPeriod(), IntervalUnit.MINUTE)).startAt(DateBuilder.evenHourDateAfterNow()).build();

		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			Log4j.traceErrorException(BaseService.class, e, "BaseService");
		}
	}

	/**
	 * 
	 * @param DelayedLabChecksResultJob
	 * @param jobIdentity
	 * @param triggerIdentity
	 * @param enableFlag
	 */
	protected static void initializeDelayedResultsReminderJob(Class<DelayedLabChecksResultJob> DelayedLabChecksResultJob, String jobIdentity, String triggerIdentity, Boolean enableFlag) {
		if (!enableFlag)
			return;

		// Setup the Job class and the Job group
		JobDetail job = newJob(DelayedLabChecksResultJob).withIdentity(jobIdentity, "Group").build();
		// Create a Trigger that fires everyWeek at Sunday.
		Trigger trigger = newTrigger().withIdentity(triggerIdentity, "Group").withSchedule(CronScheduleBuilder.cronSchedule(InfoSysConfigurationService.getDelayedLabChecksReminder())).build();

		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			Log4j.traceErrorException(BaseService.class, e, "BaseService");
		}
	}

	public static void stopThreads() {
		scheduler.shutdown();
	}

	public static void startThreads() {
		SurveillanceReportNotificationThread thread = new SurveillanceReportNotificationThread();
		scheduler.scheduleAtFixedRate(thread, 0, 1, TimeUnit.DAYS);

		PeriodicRetestThread threadRetest = new PeriodicRetestThread();
		scheduler.scheduleAtFixedRate(threadRetest, 0, 6, TimeUnit.DAYS);

		AssignmentEndNotifyThread threadAssignmentNotify = new AssignmentEndNotifyThread();
		scheduler.scheduleAtFixedRate(threadAssignmentNotify, 0, 7, TimeUnit.DAYS);

		WFPositionNotificationThread wfPositionthread = new WFPositionNotificationThread();
		scheduler.scheduleAtFixedRate(wfPositionthread, 0, 1, TimeUnit.DAYS);

	}

	protected static void log(int userId, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println(" < " + sdf.format(new Date()) + " > USER : " + userId + " : " + message);
	}

	protected static boolean isSessionOpened(CustomSession[] sessions) {
		if (sessions != null && sessions.length > 0)
			return true;
		return false;
	}

	protected static void validateOldHijriDate(Date hijriDate) throws BusinessException {
		if (hijriDate == null)
			return;
		if (!HijriDateService.isValidHijriDate(hijriDate))
			throw new BusinessException("error_invalidHijriDate");
		if (hijriDate.after(HijriDateService.getHijriSysDate()))
			throw new BusinessException("error_hijriDateViolation");
	}

	protected static String getParameterizedMessage(String key, String curLang, Object... params) {
		ResourceBundle messages;
		messages = ResourceBundle.getBundle("com.code.resources.messages", new Locale(curLang));

		String value = messages.getString(key);
		return MessageFormat.format(value, params);
	}
	
	protected static boolean checkRegexMatch(String charSequence,String regex) {
		  Pattern pattern = Pattern.compile(regex); // support characters , numbers, / or \
		  Matcher matcher = pattern.matcher(charSequence);
		  return matcher.matches();
	 }
}
