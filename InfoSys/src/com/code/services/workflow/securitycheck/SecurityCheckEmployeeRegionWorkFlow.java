package com.code.services.workflow.securitycheck;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securitycheck.SecurityCheck;
import com.code.dal.orm.securitycheck.SecurityCheckData;
import com.code.dal.orm.securitycheck.SecurityCheckEmployeeData;
import com.code.dal.orm.securitycheck.SecurityCheckNonEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.RequestSourceEnum;
import com.code.enums.SecurityCheckStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class SecurityCheckEmployeeRegionWorkFlow extends BaseWorkFlow {

	private SecurityCheckEmployeeRegionWorkFlow() {
	}

	/**
	 * 
	 * @param securityCheck
	 * @param employeeDataList
	 * @param employeeDataDeletedList
	 * @param nonEmployeeDataList
	 * @param nonEmployeeDataDeletedList
	 * @param loginUser
	 * @param attachments
	 * @param dmTask
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initSecurityCheck(SecurityCheckData securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckEmployeeData> employeeDataDeletedList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataDeletedList, EmployeeData loginUser, String attachments, WFTask dmTask, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityCheckService.saveUpdateSecurityCheck(loginUser, securityCheck, employeeDataList, employeeDataDeletedList, nonEmployeeDataList, nonEmployeeDataDeletedList, session);

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			if (securityCheck.getwFInstanceId() == null) {
				String arInstanceMsg = getParameterizedMessage("wfMsg_securityCheckInstance", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getDepartmentOrderSrcName(), securityCheck.getReason() });
				String enInstanceMsg = getParameterizedMessage("wfMsg_securityCheckInstance", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getDepartmentOrderSrcName(), securityCheck.getReason() });

				WFInstance instance = addWFInstance(WFProcessesEnum.SECURITY_CHECK.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInstanceMsg, enInstanceMsg, session);
				securityCheck.setwFInstanceId(instance.getInstanceId());
				DataAccess.updateEntity(securityCheck.getSecurityCheck(), session);
			}
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
			if (dmTask == null) {
				addWFTask(securityCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, securityCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SecurityCheckEmployeeRegionWorkFlow.class, e, "SecurityCheckEmployeeRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 *
	 * @param dmTask
	 * @param action
	 * @param securityCheck
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApprove(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());
			String arabicDetailsSummary;
			String englishDetailsSummary;
			String arabicDetailsSummaryProcessing;
			String englishDetailsSummaryProcessing;
			// action is approved
			arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			arabicDetailsSummaryProcessing = getParameterizedMessage("wfMsg_securityCheckProcessingTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			englishDetailsSummaryProcessing = getParameterizedMessage("wfMsg_securityCheckProcessingTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode())) {
				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode())) {
				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				Long originalId = DepartmentService.getDepartmentManager(regionId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_DIRECTOR.getCode())) {

				if (securityCheck.getRequestSource().equals(RequestSourceEnum.REGION_DIRECTOR.getCode()) || securityCheck.getRequestSource().equals(RequestSourceEnum.OTHERS.getCode())) {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApproved", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
					englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApproved", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
					Date hijriCurDate = HijriDateService.getHijriSysDate();
					// In either cases approved will close instance and notify
					securityCheck.setStatus(SecurityCheckStatusEnum.APPROVED.getCode());
					SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);
					closeWFInstance(instance, dmTask, action.getCode(), new Date(), hijriCurDate, session);
					Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.SECURITY_ASSISTANT_AFFAIRS_OFFICER.getCode()).getUnitId();
					Long originalId = DepartmentService.getDepartmentManager(unitId);
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

				} else {
					Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
					Long originalId = DepartmentService.getDepartmentManager(unitId);
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}

			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode())) {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_GENERAL_PROTECTION_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_GENERAL_PROTECTION_APPROVER_MANAGER.getCode())) {
				// TODO processing
				securityCheck.setStatus(SecurityCheckStatusEnum.UNDER_PROCCESSING.getCode());
				SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);
				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.PROCESSING.getCode(), "1", arabicDetailsSummaryProcessing, englishDetailsSummaryProcessing, session);
			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER_PROCESSING.getCode())) {

				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode(), "1", arabicDetailsSummaryProcessing, englishDetailsSummaryProcessing, session);
			} else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode())) {
				// action is approved
				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				Long originalId = DepartmentService.getDepartmentManager(regionId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_DIRECTOR_PROCESSING.getCode(), "1", arabicDetailsSummaryProcessing, englishDetailsSummaryProcessing, session);
			}

			else if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_DIRECTOR_PROCESSING.getCode())) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApprovedTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApprovedTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				Date hijriCurDate = HijriDateService.getHijriSysDate();
				// In either cases approved will close instance and notify
				securityCheck.setStatus(SecurityCheckStatusEnum.APPROVED.getCode());
				SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);
				closeWFInstance(instance, dmTask, action.getCode(), new Date(), hijriCurDate, session);
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.SECURITY_ASSISTANT_AFFAIRS_OFFICER.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SecurityCheckEmployeeRegionWorkFlow.class, e, "SecurityCheckEmployeeRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	public static void doReject(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				if (dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode()) || dmTask.getAssigneeWfRole().equals(WFTaskRolesEnum.REGION_DIRECTOR_PROCESSING.getCode())) {
					closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					if (dmTask.getNotes() == null || dmTask.getNotes().isEmpty() || dmTask.getNotes().trim().equals("")) {
						throw new BusinessException("error_rejectReasonMandatory");
					}
					dmTask.setRefuseReasons(dmTask.getNotes());
					completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}

				SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);
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
				Log4j.traceErrorException(SecurityCheckEmployeeRegionWorkFlow.class, e, "SecurityCheckEmployeeRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	public static void doProcessing(WFTask dmTask, SecurityCheck securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityCheckService.saveSecurityCheckProcessing(loginUser, securityCheck, employeeDataList, nonEmployeeDataList, session);
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessedTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessedTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
			completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityCheckEmployeeRegionWorkFlow.class, e, "SecurityCheckEmployeeRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

}
