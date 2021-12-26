package com.code.services.infosys.surveillance;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.audit.AuditLog;
import com.code.dal.orm.audit.RunningThread;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceOrderData;
import com.code.dal.orm.surveillance.SurveillanceReport;
import com.code.dal.orm.workflow.WFInstance;
import com.code.enums.AuditOperationsEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.AuditService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class SurveillanceReportNotificationThread extends BaseWorkFlow implements Runnable {
	private static final String THREAD_NAME = "SURVEILLANCE_REPORT_NOTIFICATION_THREAD";
	private static final long CONTENT_ID = 1;

	@Override
	public void run() {
		try {
			Log4j.traceInfo(SurveillanceReportNotificationThread.class, "Running @ : " + Calendar.getInstance().getTime());

			RunningThread thrd = CommonService.getThread(THREAD_NAME);
			if (thrd != null && Calendar.getInstance().getTimeInMillis() - thrd.getRunningTime() > 60000) { // more
																											// than
																											// 1
																											// minutes
				DataAccess.deleteEntity(thrd);
				thrd = null;
			}

			if (thrd == null) {
				thrd = new RunningThread(THREAD_NAME, Calendar.getInstance().getTimeInMillis());
				DataAccess.addEntity(thrd);

				sendReportsNotifications();

				// Updating log of the thread running in DB by last running date
				// and
				// count
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
			Log4j.traceErrorException(SurveillanceReportNotificationThread.class, e, THREAD_NAME);
		}
	}

	public void sendReportsNotifications() {
		try {
			List<SurveillanceReport> reportList = SurveillanceOrdersService.getSurveillanceReports(HijriDateService.addSubHijriDays(HijriDateService.getHijriSysDate(), Integer.valueOf(InfoSysConfigurationService.getSurveillanceNotifyLateReportAhead())));
			for (SurveillanceReport report : reportList) {
				String arabicDetailsSummary = null;
				String englishDetailsSummary = null;
				SurveillanceEmpNonEmpData employee = SurveillanceOrdersService.getSurveillanceEmployeeData(report.getSurveillanceEmpId());
				SurveillanceOrderData order = SurveillanceOrdersService.getSurveillanceOrderData(employee.getSurveillanceOrderId(), null, FlagsEnum.ALL.getCode()).get(0);
				WFInstance instance = BaseWorkFlow.getWFInstanceById(order.getwFInstanceId());

				String message = getParameterizedMessage("wfMsg_surveillanceReport", "ar", new Object[] { order.getOrderNumber(), employee.getEmployeeFullName() });
				CustomSession session = DataAccess.getSession();
				try {
					session.beginTransaction();
					Long orginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(order.getRegionId()));
					addWFTask(instance.getInstanceId(), orginalId, orginalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);

					session.commitTransaction();
				} catch (Exception e) {
					session.rollbackTransaction();
					Log4j.traceErrorException(SurveillanceReportNotificationThread.class, e, THREAD_NAME);
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceReportNotificationThread.class, e, THREAD_NAME);
		}
	}
}