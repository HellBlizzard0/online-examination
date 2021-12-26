package com.code.services.workflow.securityaction;

import java.util.Date;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securitymission.PenaltyArrestData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.PenaltyArrestService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class PenaltyArrestWorkFlow extends BaseWorkFlow {
	/**
	 * Default Constructor
	 */
	private PenaltyArrestWorkFlow() {
	}

	/**
	 * Create task at "Moder Edart elste5barat" in Region (or) Moderya
	 * 
	 * @param penaltyArrestData
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initPenaltyArrest(PenaltyArrestData penaltyArrestData, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			penaltyArrestData.setRequestNumber(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.PENALTY_ARREST.getEntityId(), Integer.MAX_VALUE, session).toString());
			String arIntanceMsg = getParameterizedMessage("wfMsg_penaltyArrestInstance", "ar", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString(), penaltyArrestData.getRequesterEmployeeName(), penaltyArrestData.getArrestedEmployeeName() });
			String enIntanceMsg = getParameterizedMessage("wfMsg_penaltyArrestInstance", "en", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString(), penaltyArrestData.getRequesterEmployeeName(), penaltyArrestData.getArrestedEmployeeName() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.PENALTY_ARREST.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arIntanceMsg, enIntanceMsg, session);
			penaltyArrestData.setwFInstanceId(instance.getInstanceId());
			PenaltyArrestService.savePenaltyArrest(penaltyArrestData, loginUser, session);
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_initPenatyArrest", "ar", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_initPenatyArrest", "en", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString() });
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionMilitaryPoliceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.PENALTY_ARREST.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.PENALTY_ARREST.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.PENALTY_ARREST.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.PENALTY_ARREST.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(PenaltyArrestWorkFlow.class, e, "PenaltyArrestWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Processed by "Moder eledara el3ama lel2mn welste5barat" in HQ and send notification for "Moder Edart elste5barat" in HQ for printing letter from another screen
	 * 
	 * @param currentTask
	 * @param penaltyArrestData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApprove(WFTask currentTask, PenaltyArrestData penaltyArrestData, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_approvePenatyArrest", "ar", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_approvePenatyArrest", "en", new Object[] { penaltyArrestData.getRequestNumber(), penaltyArrestData.getRequestDateString() });
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(penaltyArrestData.getwFInstanceId());
			Long requesterOriginalId = instance.getRequesterId();
			Long requesterAssigneeId = getDelegate(requesterOriginalId, WFProcessesEnum.PENALTY_ARREST.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), requesterAssigneeId, requesterOriginalId, WFTaskUrlEnum.PENALTY_ARREST.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			changeWFInstanceStatus(instance, WFInstanceStatusEnum.COMPLETED.getCode(), session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(PenaltyArrestWorkFlow.class, e, "PenaltyArrestWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Closed notification
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void doNotify(WFTask currentTask) throws BusinessException {
		try {
			setWFTaskAction(currentTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(PenaltyArrestWorkFlow.class, e, "PenaltyArrestWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}