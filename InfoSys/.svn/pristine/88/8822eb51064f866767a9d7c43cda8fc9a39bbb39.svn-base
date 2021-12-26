package com.code.services.workflow.assignment;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
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
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class EmployeesAssignementWorkFlow extends BaseWorkFlow {
	private EmployeesAssignementWorkFlow() {
	}

	/**
	 * Create task at "r2es qsm elste5barat" in Region (or) Moderya
	 * 
	 * @param assignmentData
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void initAssignemnt(AssignmentData assignmentData, EmployeeData loginUser, boolean isReAssigned, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arInsanceMsg = getParameterizedMessage("wfMsg_assgInstanceMsg", "ar", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_assgInstanceMsg", "en", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInsanceMsg, enInstanceMsg, session);
			assignmentData.setwFInstanceId(instance.getInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());

			WFTask newTask = null;
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (isReAssigned) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}
			newTask.setFlexField3(String.valueOf(isReAssigned));
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Create task at "r2es qsm elste5barat" in Region (or) Moderya
	 * 
	 * @param assignmentData
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void resendAssignemnt(WFTask currentTask, AssignmentData assignmentData, EmployeeData loginUser, boolean isReAssigned, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			WFInstance instance = getWFInstanceById(assignmentData.getwFInstanceId());
			WFTask newTask = null;
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (isReAssigned) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}
			newTask.setFlexField3(String.valueOf(isReAssigned));
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Approve by "r2es qesm el ast5barat" in Region and open a task at "moder edart el ast5barat" region
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveRegionDepHead(WFTask currentTask, AssignmentData assignmentData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(assignmentData.getwFInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
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
	 * @param assignmentData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveRegionManager(WFTask currentTask, AssignmentData assignmentData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
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
	 * @param assignmentData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveHQManager(WFTask currentTask, AssignmentData assignmentData, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_GENERAL_PROTECTION_APPROVER_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Approve by "Moder eledara el3ama lel2mn welste5barat" in HQ and open task at "Moder eledart elste5barat" in HQ for processing
	 * 
	 * @param assignmentData
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void approveHQGeneralManager(WFTask currentTask, AssignmentData assignmentData, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReq", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReq", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_PROCESSOR_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Processed by "Moder Edart elste5barat" in HQ and open task at "Moder eledara el3ama lel2mn welste5barat" in HQ
	 * 
	 * @param assignmentData
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void processHQManager(WFTask currentTask, AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailsData, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqApprovedRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqApproved", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			for (AssignmentDetailData assignmentDetailData : assignmentDetailsData) {
				DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
			}
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_GENERAL_PROTECTION_PROCESSOR_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Processed by "Moder eledara el3ama lel2mn welste5barat" in HQ and send notification for "Moder Edart elste5barat" in HQ and send to the requester for printing letter
	 * 
	 * @param assignmentData
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void processHQGeneralManager(WFTask currentTask, AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailDataList, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			String assignmentLetterArabicDetailsSummary, assignmentLetterEnglishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignApprovedRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignApproved", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			assignmentLetterArabicDetailsSummary = getParameterizedMessage("wfMsg_assignmentLetterSummary", "ar", new Object[] { assignmentData.getRequestNumber() });
			assignmentLetterEnglishDetailsSummary = getParameterizedMessage("wfMsg_assignmentLetterSummary", "en", new Object[] { assignmentData.getRequestNumber() });

			for (AssignmentDetailData assignmentDetailData : assignmentDetailDataList) {
				DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());

			Long smOriginalId = instance.getRequesterId();
			Long smAssigneeId = getDelegate(smOriginalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), smAssigneeId, smOriginalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			sendNotifications(currentTask, assignmentData);

			/**
			 * This part to initiate the assignment letter printing task
			 */
			Long originalUnitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(originalUnitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			newTask = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.SECURITY_AFFAIRS_PRINT_ASSIGNMENT_LETTER_MANAGER.getCode(), "1", assignmentLetterArabicDetailsSummary, assignmentLetterEnglishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());

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
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Reject Employee AssignmentData in any work flow Step
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void rejectEmployeeAssignmentAnalaysis(WFTask currentTask, AssignmentData assignmentData, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReqRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReq", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignAnalysisReq", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECORATE_SECURITY_AFFAIRS_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.HQ_SECURITY_AFFAIRS_PROCESSOR_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			newTask.setFlexField1("true");
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Reject Employee AssignmentData in any work flow Step
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void rejectEmployeeAssignment(WFTask currentTask, AssignmentData assignmentData, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRejectedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRejectedRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignRejected", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignRejected", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField3(currentTask.getFlexField3());
			newTask.setFlexField1("true");
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * getDirectorateGMOfficeDepartmentId
	 * 
	 * @return id
	 * @throws BusinessException
	 */
	public static long getDirectorateGMOfficeDepartmentId() throws BusinessException {
		try {
			return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_GENERAL_MANAGER_OFFICE.getCode()).getUnitId();
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
			throw new BusinessException("error_general");
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
			Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	private static void sendNotifications(WFTask currentTask, AssignmentData assignmentData) {
		try {
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());

			String arabicDetailsSummary;
			String englishDetailsSummary;
			String message;

			if (currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignApprovedRe", "en", new Object[] { assignmentData.getRequestNumber() });
				message = getParameterizedMessage("wfMsg_empAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_empAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_empAssignApproved", "en", new Object[] { assignmentData.getRequestNumber() });
				message = getParameterizedMessage("wfMsg_empAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
			}

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
			unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			originalId = DepartmentService.getDepartmentManager(unitId);
			notifiresIdList.add(originalId);
			unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			originalId = DepartmentService.getDepartmentManager(unitId);
			notifiresIdList.add(originalId);

			CustomSession session = DataAccess.getSession();
			try {
				session.beginTransaction();
				for (Long notifierId : notifiresIdList) {
					Long assigneeId = getDelegate(notifierId, WFProcessesEnum.EMPLOYEE_ASSIGNEMENT.getCode());
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);
				}

				session.commitTransaction();
			} catch (Exception e) {
				session.rollbackTransaction();
				Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
			} finally {
				session.close();
			}
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeesAssignementWorkFlow.class, e, "EmployeesAssignementWorkFlow");
		}
	}
}