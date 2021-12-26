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

public class SecurityCheckHQWorkFlow extends BaseWorkFlow {
	private SecurityCheckHQWorkFlow() {
	}

	/**
	 * Save wf instance and task at direct manager : If task in HQ then "Moder edaret el este5barat" in HQ If task in Region then "Moder edaret el este5barat" in region
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
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				if (dmTask == null) {
					addWFTask(securityCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, securityCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());

				if (dmTask == null) {
					addWFTask(securityCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, securityCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
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
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder edaret el este5barat" Reject then end workflow and notify requester Else if approved then open task at If in HQ "Moder eledara el3ama lel2mn welste5barat" If in Region then "Qa2ed el mante2a" in region
	 * 
	 * @param dmTask
	 * @param action
	 * @param securityCheck
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveRejectDM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				if (dmTask.getNotes() == null || dmTask.getNotes().isEmpty() || dmTask.getNotes().trim().equals("")) {
					throw new BusinessException("error_rejectReasonMandatory");
				}
				dmTask.setRefuseReasons(dmTask.getNotes());
				SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);

				// closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);

				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				// addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
			} else {// action is approved
				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckSavingTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				if (regionId != null) {
					Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
					Long originalId = DepartmentService.getDepartmentManager(unitId);
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
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
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder eledara el3ama lel2mn welste5barat" or "Qa2ed el mante2a" Reject then end workflow and notify requester Else if approved then open task at requester officer for processing
	 * 
	 * @param dmTask
	 * @param action
	 * @param securityCheck
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveRejectSM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());

			long originalId = instance.getRequesterId();
			long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				if (dmTask.getNotes() == null || dmTask.getNotes().isEmpty() || dmTask.getNotes().trim().equals("")) {
					throw new BusinessException("error_rejectReasonMandatory");
				}
				dmTask.setRefuseReasons(dmTask.getNotes());
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				// closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);
				completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				// addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
			} else {// action is approved
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessingTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessingTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Requester send processing for approval: If task in HQ then "Moder edaret el este5barat" in HQ If task in Region then "Moder edaret el este5barat" in region
	 * 
	 * @param dmTask
	 * @param securityCheck
	 * @param employeeDataList
	 * @param nonEmployeeDataList
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
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
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER_PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.SECURITY_AFFAIRS_MANAGER_PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder edaret el este5barat" Reject then end workflow and notify requester Else if approved then open task at If in HQ "Moder eledara el3ama lel2mn welste5barat" If in Region then "Qa2ed el mante2a" in region
	 * 
	 * @param dmTask
	 * @param action
	 * @param securityCheck
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveRejectProcessingDM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);

				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);

				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
			} else {// action is approved
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessedTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckProcessedTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });

				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				if (regionId != null) {
					Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER_PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
					Long originalId = DepartmentService.getDepartmentManager(unitId);
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());
					completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR_PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
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
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder eledara el3ama lel2mn welste5barat" or "Qa2ed el mante2a" Reject then end workflow and notify requester Else if approved then notify requester officer
	 * 
	 * @param dmTask
	 * @param action
	 * @param securityCheck
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveRejectProcessingSM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SecurityCheckService.updateSecurityCheck(loginUser, securityCheck, session);

			WFInstance instance = getWFInstanceById(securityCheck.getwFInstanceId());

			long originalId = instance.getRequesterId();
			long assigneeId = getDelegate(originalId, WFProcessesEnum.SECURITY_CHECK.getCode());

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			// In either cases approved or reject will close instance and notify
			// requester
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (action == WFTaskActionsEnum.REJECT) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckRejectTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApprovedTask", "ar", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_securityCheckApprovedTask", "en", new Object[] { securityCheck.getRequestNumber(), securityCheck.getRequestDateString() });
			}
			closeWFInstance(instance, dmTask, action.getCode(), new Date(), hijriCurDate, session);
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SECURITY_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
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
			Log4j.traceErrorException(SecurityCheckHQWorkFlow.class, e, "SecurityCheckHQWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}