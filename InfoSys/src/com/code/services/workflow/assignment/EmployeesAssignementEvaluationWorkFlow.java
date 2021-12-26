package com.code.services.workflow.assignment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.assignment.AssignmentEvaluation;
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
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class EmployeesAssignementEvaluationWorkFlow extends BaseWorkFlow {

	private EmployeesAssignementEvaluationWorkFlow() {
	}

	/**
	 * Create task at "Moder Edart elste5barat" in Region (or) Moderya
	 * 
	 * @param assignmentEvaluation
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initAssignmentEvaluation(AssignmentEvaluation assignmentEvaluation, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			AssignmentDetailData assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
			String arInsanceMsg = getParameterizedMessage("wfMsg_assgEvalInstanceMsg", "ar", new Object[] { assignmentDetailData.getAgentCode(), assignmentDetailData.getOfficerName(), assignmentDetailData.getStartDateString(), assignmentDetailData.getPeriod() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_assgEvalInstanceMsg", "en", new Object[] { assignmentDetailData.getAgentCode(), assignmentDetailData.getOfficerName(), assignmentDetailData.getStartDateString(), assignmentDetailData.getPeriod() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInsanceMsg, enInstanceMsg, session);
			assignmentEvaluation.setwFInstanceId(instance.getInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "en", new Object[] { assignmentDetailData.getAgentCode() });

			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Approve by "Moder Edart elste5barat" in Region and open task at "Moder Edary elste5barat" Moderya
	 * 
	 * @param currentTask
	 * @param assignmentEvaluation
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveRegionManager(WFTask currentTask, AssignmentEvaluation assignmentEvaluation, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			AssignmentDetailData assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "en", new Object[] { assignmentDetailData.getAgentCode() });
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentEvaluation, session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentEvaluation.getwFInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * 
	 * @param currentTask
	 * @param assignmentEvaluation
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveRegionSecurityManager(WFTask currentTask, AssignmentEvaluation assignmentEvaluation, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			AssignmentDetailData assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "en", new Object[] { assignmentDetailData.getAgentCode() });
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentEvaluation, session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentEvaluation.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Approve by "Moder Edart elste5barat" in HQ and open task at "Moder eledara el3ama lel2mn welste5barat" in HQ
	 * 
	 * @param currentTask
	 * @param assignmentEvaluation
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveHQManager(WFTask currentTask, AssignmentEvaluation assignmentEvaluation, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			AssignmentDetailData assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEval", "en", new Object[] { assignmentDetailData.getAgentCode() });
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentEvaluation, session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentEvaluation.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.HQ_GENERAL_PROTECTION_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Approve by "Moder eledara el3ama lel2mn welste5barat" in HQ and send notification to creator
	 * 
	 * @param currentTask
	 * @param assignmentEvaluation
	 * @param assignmentDetailData
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveHQGeneralManager(WFTask currentTask, AssignmentEvaluation assignmentEvaluation, AssignmentDetailData assignmentDetailData, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEvalApprove", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEvalApprove", "en", new Object[] { assignmentDetailData.getAgentCode() });
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentEvaluation, session);
			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentEvaluation.getwFInstanceId());
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			changeWFInstanceStatus(instance, WFInstanceStatusEnum.COMPLETED.getCode(), session);
			sendNotifications(assignmentDetailData.getAgentCode(), instance.getInstanceId());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Reject Employee Assignment Evaluation in any work flow Step
	 * 
	 * @param currentTask
	 * @param assignmentEvaluation
	 * @param attachments
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void rejectEmployeeAssignmentEvaluation(WFTask currentTask, AssignmentEvaluation assignmentEvaluation, String attachments, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			AssignmentDetailData assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEvalReject", "ar", new Object[] { assignmentDetailData.getAgentCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEvalReject", "en", new Object[] { assignmentDetailData.getAgentCode() });
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentEvaluation.getwFInstanceId());
			closeWFInstance(instance, currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
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
			Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Send notifications after approved
	 * 
	 * @param assignmentDetailData
	 */
	private static void sendNotifications(String agentCode, long instanceId) {
		try {
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_assignEvalApprove", "ar", new Object[] { agentCode });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_assignEvalApprove", "en", new Object[] { agentCode });
			String message = getParameterizedMessage("wfMsg_assignEvalApprove", "ar", new Object[] { agentCode });
			
			Long unitId = null;
			Long originalId = null;
			Set<Long> notifiresIdList = new HashSet<Long>();
			unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
			originalId = DepartmentService.getDepartmentManager(unitId);
			notifiresIdList.add(originalId);
			unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
			originalId = DepartmentService.getDepartmentManager(unitId);
			notifiresIdList.add(originalId);
			unitId = WFPositionService.getWFPosition(WFPositionsEnum.SECURITY_ASSISTANT_AFFAIRS_OFFICER.getCode()).getUnitId();
			originalId = DepartmentService.getDepartmentManager(unitId);
			notifiresIdList.add(originalId);

			CustomSession session = DataAccess.getSession();
			try {
				session.beginTransaction();
				for (Long notifierId : notifiresIdList) {
					Long assigneeId = getDelegate(notifierId, WFProcessesEnum.EMPLOYEE_ASSIGNMENT_EVALUATION.getCode());
					addWFTask(instanceId, assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);	
				}

				session.commitTransaction();
			} catch (Exception e) {
				session.rollbackTransaction();
				Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
			} finally {
				session.close();
			}
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignementEvaluationWorkFlow.class, e, "EmployeesAssignementEvaluationWorkFlow");
		}
	}
}