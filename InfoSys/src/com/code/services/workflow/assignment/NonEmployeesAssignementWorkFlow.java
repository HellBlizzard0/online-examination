package com.code.services.workflow.assignment;

import java.util.Date;
import java.util.HashSet;
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

public class NonEmployeesAssignementWorkFlow extends BaseWorkFlow {
	private NonEmployeesAssignementWorkFlow() {
	}

	/**
	 * Create task at "r2es Edart elste5barat" in Region (or) Moderya
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

			String arInsanceMsg = getParameterizedMessage("wfMsg_assgNonInstanceMsg", "ar", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_assgNonInstanceMsg", "en", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInsanceMsg, enInstanceMsg, session);
			assignmentData.setwFInstanceId(instance.getInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());

			WFTask newTask = null;
			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (isReAssigned) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * 
	 * @param assignmentData
	 * @param loginUser
	 * @param isReAssigned
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void editAssignment(AssignmentData assignmentData, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());

			WFTask newTask = null;
			String arabicDetailsSummary;
			String englishDetailsSummary;

			if (assignmentData.getwFInstanceId() == null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_assgNonInstanceMsg", "ar", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_assgNonInstanceMsg", "en", new Object[] { assignmentData.getRequestNumber(), assignmentData.getOfficerName(), assignmentData.getStartDateString(), assignmentData.getPeriod() });
				WFInstance instance = addWFInstance(WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arabicDetailsSummary, englishDetailsSummary, session);
				assignmentData.setwFInstanceId(instance.getInstanceId());
			}

			arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "ar", new Object[] { assignmentData.getRequestNumber() });
			englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "en", new Object[] { assignmentData.getRequestNumber() });

			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(assignmentData.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = addWFTask(assignmentData.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			newTask.setFlexField2("true");
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
	public static void approveRegionDepHead(WFTask currentTask, AssignmentData assignmentData, AssignmentDetailData assignmentDetailData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "en", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(assignmentData.getwFInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true"))
				newTask.setFlexField2(currentTask.getFlexField2());
			else
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
	 * @param assignmentDetailData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveRegionManager(WFTask currentTask, AssignmentData assignmentData, AssignmentDetailData assignmentDetailData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "en", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true"))
				newTask.setFlexField2(currentTask.getFlexField2());
			else
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
	 * @param assignmentDetailData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveHQManager(WFTask currentTask, AssignmentData assignmentData, AssignmentDetailData assignmentDetailData, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignEd", "en", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true"))
				newTask.setFlexField2(currentTask.getFlexField2());
			else
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
	 * @param currentTask
	 * @param assignmentData
	 * @param assignmentDetailData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void approveHQGeneralManager(WFTask currentTask, AssignmentData assignmentData, AssignmentDetailData assignmentDetailData, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedEd", "en", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApproved", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true"))
				newTask.setFlexField2(currentTask.getFlexField2());
			else
				newTask.setFlexField3(currentTask.getFlexField3());
			changeWFInstanceStatus(instance, WFInstanceStatusEnum.COMPLETED.getCode(), session);
			sendNotifications(currentTask, assignmentData);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Reject Non Employee Assignment
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param assignmentDetailData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void rejectNonEmployeeAssignment(WFTask currentTask, AssignmentData assignmentData, AssignmentDetailData assignmentDetailData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejectedEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejectedEd", "en", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejectedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejectedRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejected", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRejected", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();
			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());
			currentTask.setFlexField1("true");
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
			WFTask newTask = completeWFTask(currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			newTask.setFlexField1("true");
			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				newTask.setFlexField2(currentTask.getFlexField2());
			} else
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
			Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
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
			Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
			throw new BusinessException("error_general");
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
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignRe", "en", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssign", "en", new Object[] { assignmentData.getRequestNumber() });
			}
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_DEP_HEAD.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
				newTask = completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NON_EMPLOYEE_ASSIGNMENT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	private static void sendNotifications(WFTask currentTask, AssignmentData assignmentData) {
		try {
			WFInstance instance = BaseWorkFlow.getWFInstanceById(assignmentData.getwFInstanceId());

			String arabicDetailsSummary;
			String englishDetailsSummary;
			String message;

			if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedEd", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedEd", "en", new Object[] { assignmentData.getRequestNumber() });
				message = getParameterizedMessage("wfMsg_nonEmpAssignApprovedEd", "ar", new Object[] { assignmentData.getRequestNumber() });
			} else if (currentTask.getFlexField3() != null && currentTask.getFlexField3().equals("true")) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApprovedRe", "en", new Object[] { assignmentData.getRequestNumber() });
				message = getParameterizedMessage("wfMsg_nonEmpAssignApprovedRe", "ar", new Object[] { assignmentData.getRequestNumber() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_nonEmpAssignApproved", "en", new Object[] { assignmentData.getRequestNumber() });
				message = getParameterizedMessage("wfMsg_nonEmpAssignApproved", "ar", new Object[] { assignmentData.getRequestNumber() });
			}

			Long unitId = null;
			Long originalId = null;
			Set<Long> notifiresIdList = new HashSet<Long>();
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
					Long assigneeId = getDelegate(notifierId, WFProcessesEnum.NON_EMPLOYEE_ASSIGNEMENT.getCode());
					WFTask newNoti = addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", message, arabicDetailsSummary, englishDetailsSummary, session);
					if (currentTask.getFlexField2() != null && currentTask.getFlexField2().equals("true"))
						newNoti.setFlexField2(currentTask.getFlexField2());
					else
						newNoti.setFlexField3(currentTask.getFlexField3());
				}

				session.commitTransaction();
			} catch (Exception e) {
				session.rollbackTransaction();
				Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
			} finally {
				session.close();
			}
		} catch (Exception e) {
			Log4j.traceErrorException(NonEmployeesAssignementWorkFlow.class, e, "NonEmployeesAssignementWorkFlow");
		}
	}
}