package com.code.services.infosys.assignment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.audit.AuditLog;
import com.code.dal.orm.audit.RunningThread;
import com.code.dal.orm.workflow.WFInstance;
import com.code.enums.AuditOperationsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.AuditService;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;

public class AssignmentEndNotifyThread extends BaseWorkFlow implements Runnable {
	private static final String THREAD_NAME = "ASSIGNMENT_END_NOTIFY_THREAD";
	private static final long CONTENT_ID = 3;

	@Override
	public void run() {
		try {
			Log4j.traceInfo(AssignmentEndNotifyThread.class, "Running @ : " + Calendar.getInstance().getTime());

			RunningThread thrd = CommonService.getThread(THREAD_NAME);
			if (thrd != null && Calendar.getInstance().getTimeInMillis() - thrd.getRunningTime() > 60000) {
				DataAccess.deleteEntity(thrd);
				thrd = null;
			}

			if (thrd == null) {
				thrd = new RunningThread(THREAD_NAME, Calendar.getInstance().getTimeInMillis());
				DataAccess.addEntity(thrd);

				sendEndNotification();

				AuditLog log = AuditService.getAuditLogByUser(THREAD_NAME, CONTENT_ID);
				if (log == null) {
					log = new AuditLog();
					log.setSystemUser(THREAD_NAME);
					log.setContent(THREAD_NAME);

					log.setOperation(AuditOperationsEnum.INSERT.toString());
					log.setOperationDate(new Date());
					log.setContentEntity(this.getClass().getCanonicalName());
					log.setContentId(CONTENT_ID);

					DataAccess.addEntity(log);
				} else {
					log.setOperation(AuditOperationsEnum.UPDATE.toString());
					log.setOperationDate(new Date());
					DataAccess.updateEntity(log);
				}

				DataAccess.deleteEntity(thrd);
			}
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentEndNotifyThread.class, e, THREAD_NAME);
		}
	}

	/**
	 * Send Notification To Assignment Officer that his assignments
	 * employees/non employees will end after two months
	 */
	private void sendEndNotification() {
		try {
			Date startDate = HijriDateService.addSubHijriMonthsDays(HijriDateService.getHijriSysDate(), 2, 0);
			Date endDate = HijriDateService.addSubHijriMonthsDays(HijriDateService.getHijriSysDate(), 2, 7);
			String arabicDetailsSummary = null;
			String englishDetailsSummary = null;
			List<AssignmentDetailData> assignmentDetailsDataList = AssignmentService.getAssignmentDetailDataToSendNotif(startDate, endDate);
			for (AssignmentDetailData assginmentDetail : assignmentDetailsDataList) {
				WFInstance instance = getWFInstanceById(assginmentDetail.getwFInstanceId());
				String message = getParameterizedMessage(assginmentDetail.getEmployeeId() == null ? "wfMsg_assignmentOfficerNotifyNonEmp" : "wfMsg_assignmentOfficerNotifyEmp", "ar", new Object[] { assginmentDetail.getFullName() });
				CustomSession session = DataAccess.getSession();
				try {
					session.beginTransaction();
					addWFTask(instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);
					session.commitTransaction();
				} catch (Exception e) {
					session.rollbackTransaction();
					Log4j.traceErrorException(AssignmentEndNotifyThread.class, e, THREAD_NAME);
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentEndNotifyThread.class, e, THREAD_NAME);
		}
	}
}
