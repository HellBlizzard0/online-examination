package com.code.services.workflow.surveillance.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.ReminderData;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.securityanalysis.SecurityAnalysisWorkFlow;

public class ConversationReminderJob extends BaseWorkFlow implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			List<ReminderData> reminderDataList = new ArrayList<ReminderData>();
			reminderDataList = ConversationService.getAllRemindersNotDone();
			for (ReminderData reminderData : reminderDataList) {
				Date gReminderDate = HijriDateService.hijriToGregDate(reminderData.getReminderDate());
				int h = Integer.parseInt(reminderData.getReminderTime().substring(0, 2));
				int m = Integer.parseInt(reminderData.getReminderTime().substring(3));
				Calendar futureDate = Calendar.getInstance();
				futureDate.setTime(gReminderDate);
				futureDate.set(Calendar.HOUR_OF_DAY, h);
				futureDate.set(Calendar.MINUTE, m);
				Date futureTime = futureDate.getTime();
				Calendar cal = Calendar.getInstance();
				Date currentTime = cal.getTime();

				long diffTimeMilli = futureTime.getTime() - currentTime.getTime();
				long diffTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(diffTimeMilli);

				if (diffTimeMinutes < 0 || diffTimeMinutes > InfoSysConfigurationService.getConvReminderPeriod())
					continue;// send reminder within the next hour

				ConversationData conversationData = ConversationService.getConversationsByConversationId(reminderData.getConversationId());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_reminderConversation", "ar", new Object[] { conversationData.getFollowUpCode(), reminderData.getReminderDetails() });

				String englishDetailsSummary = getParameterizedMessage("wfMsg_reminderConversation", "en", new Object[] { conversationData.getFollowUpCode(), reminderData.getReminderDetails() });
				// Send Notifications
				SecurityAnalysisWorkFlow.notify(reminderData.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, reminderData.getEmpId(), session);

				reminderData.setDoneFlag(true);
				ConversationService.updateReminder(reminderData, session);

			}

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();
			Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlow");
		} finally {
			session.close();
		}

	}

}