package com.code.services.workflow;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.audit.AuditLog;
import com.code.dal.orm.audit.RunningThread;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFPositionData;
import com.code.enums.AuditOperationsEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.AuditService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;

public class WFPositionNotificationThread extends BaseWorkFlow implements Runnable {
	private static final String THREAD_NAME = "WFPOSITION_NOTIFICATION_THREAD";
	private static final long CONTENT_ID = 4;

	@Override
	public void run() {
		try {
			Log4j.traceInfo(WFPositionNotificationThread.class, "Running @ : " + Calendar.getInstance().getTime());

			RunningThread thrd = CommonService.getThread(THREAD_NAME);
			// more than 1 minutes
			if (thrd != null && Calendar.getInstance().getTimeInMillis() - thrd.getRunningTime() > 60000) {
				DataAccess.deleteEntity(thrd);
				thrd = null;
			}

			if (thrd == null) {
				thrd = new RunningThread(THREAD_NAME, Calendar.getInstance().getTimeInMillis());
				DataAccess.addEntity(thrd);

				sendWFPositionEmployeeChanges();
				// Updating log of the thread running in DB by last running date and count
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
			Log4j.traceErrorException(WFPositionNotificationThread.class, e, THREAD_NAME);
		}
	};

	public void sendWFPositionEmployeeChanges() {
		try {
			List<WFPositionData> wfPositionsDataList = WFPositionService.getWFPositionDataForTransferedEmployees();

			if (!wfPositionsDataList.isEmpty()) {
				StringBuilder subject = new StringBuilder();
				subject.append(getParameterizedMessage("wfMsg_changedEmployees", "ar", new Object[] {})).append("\n");
				for (WFPositionData position : wfPositionsDataList) {
					subject.append(position.getPositionDesc()).append(" -> ").append(position.getEmpName()).append("\n");
				}

				CustomSession session = DataAccess.getSession();
				try {
					session.beginTransaction();
					String arMessage = getParameterizedMessage("wfMsg_changedEmployees", "ar", new Object[] {}).replace(":", "").trim();
					String enMessage = getParameterizedMessage("wfMsg_changedEmployees", "en", new Object[] {}).replace(":", "").trim();
					EmployeeData admin = EmployeeService.getEmployee(InfoSysConfigurationService.getSystemAdmin());
					long requesterId = admin.getEmpId();
					long notifieeId = requesterId;
					WFInstance instance = addWFInstance(WFProcessesEnum.NOTIFICATIONS.getCode(), requesterId, new Date(), HijriDateService.getHijriSysDate(), WFInstanceStatusEnum.COMPLETED.getCode(), null, null, null, session);
					addWFTask(instance.getInstanceId(), notifieeId, notifieeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", subject.toString(), arMessage, enMessage, session);

					session.commitTransaction();
				} catch (Exception e) {
					session.rollbackTransaction();
					Log4j.traceErrorException(WFPositionNotificationThread.class, e, THREAD_NAME);
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(WFPositionNotificationThread.class, e, THREAD_NAME);
		}
	}
}
