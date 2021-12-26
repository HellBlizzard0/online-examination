package com.code.services.infosys.labcheck;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.audit.AuditLog;
import com.code.dal.orm.audit.RunningThread;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.enums.AuditOperationsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.AuditService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class PeriodicRetestThread extends BaseWorkFlow implements Runnable {
	private static final String THREAD_NAME = "PERIODIC_RETEST_THREAD";
	private static final long CONTENT_ID = 2;

	@Override
	public void run() {
		try {
			Log4j.traceInfo(PeriodicRetestThread.class, "Running @ : " + Calendar.getInstance().getTime());

			RunningThread thrd = CommonService.getThread(THREAD_NAME);
			if (thrd != null && Calendar.getInstance().getTimeInMillis() - thrd.getRunningTime() > 60000) {
				DataAccess.deleteEntity(thrd);
				thrd = null;
			}

			if (thrd == null) {
				thrd = new RunningThread(THREAD_NAME, Calendar.getInstance().getTimeInMillis());
				DataAccess.addEntity(thrd);

				sendRetestNotifications();

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
			Log4j.traceErrorException(PeriodicRetestThread.class, e, THREAD_NAME);
		}
	}

	public void sendRetestNotifications() {
		try {
			List<LabCheckEmployeeData> employeesList = LabCheckService.getLabCheckEmployeesToRetest(HijriDateService.getHijriSysDate());
			for (LabCheckEmployeeData employee : employeesList) {
				String arabicDetailsSummary = null;
				String englishDetailsSummary = null;
				LabCheck labCheck = LabCheckService.getLabCheck(employee.getLabCheckId());
				WFInstance instance = getWFInstanceById(labCheck.getwFInstanceId());

				String message = getParameterizedMessage("wfMsg_retestNotification", "ar", new Object[] { labCheck.getOrderNumber(), employee.getEmployeeFullName() });
				CustomSession session = DataAccess.getSession();
				try {
					session.beginTransaction();

					addWFTask(instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);

					Long antiDrugDMId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(labCheck.getRegionId()));
					
					addWFTask(instance.getInstanceId(), antiDrugDMId, antiDrugDMId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);

					session.commitTransaction();
				} catch (Exception e) {
					session.rollbackTransaction();
					Log4j.traceErrorException(PeriodicRetestThread.class, e, THREAD_NAME);
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(PeriodicRetestThread.class, e, THREAD_NAME);
		}
	}
}
