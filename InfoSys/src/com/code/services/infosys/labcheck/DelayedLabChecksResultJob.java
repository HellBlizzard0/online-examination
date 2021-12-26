package com.code.services.infosys.labcheck;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class DelayedLabChecksResultJob extends BaseWorkFlow implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Date dateBeforeMonth = HijriDateService.addSubHijriMonthsDays(HijriDateService.getHijriSysDate(), InfoSysConfigurationService.getLabCheckWithinPeriod() , 0);
			List<LabCheckEmployeeData> employeesList = LabCheckService.getDelayedLabCheckEmployees(LabCheckStatusEnum.APPROVED.getCode(), dateBeforeMonth);
			for (LabCheckEmployeeData employee : employeesList) {
				String arabicDetailsSummary = null;
				String englishDetailsSummary = null;
				LabCheck labCheck = LabCheckService.getLabCheck(employee.getLabCheckId());
				WFInstance instance = getWFInstanceById(labCheck.getwFInstanceId());
				String message = getParameterizedMessage("wtfMsg_delayedResultsLabChecksNotifications", "ar", new Object[] { employee.getEmployeeSocialId(), employee.getEmployeeFullName(), labCheck.getOrderDate() });
				CustomSession session = DataAccess.getSession();
				try {
					session.beginTransaction();
					if (labCheck.getRegionId() != null) {
						Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(labCheck.getRegionId()));
						Long assigneeId = getDelegate(originalId, WFProcessesEnum.NOTIFICATIONS.getCode());
						addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);
					} else {
						Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
						Long originalId = DepartmentService.getDepartmentManager(unitId);
						Long assigneeId = getDelegate(originalId, WFProcessesEnum.NOTIFICATIONS.getCode());
						addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);
					}
					session.commitTransaction();
				} catch (Exception e) {
					session.rollbackTransaction();
					Log4j.traceErrorException(DelayedLabChecksResultJob.class, e, "DelayedLabChecksResultJob");
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(DelayedLabChecksResultJob.class, e, "DelayedLabChecksResultJob");
		}
	}
}
