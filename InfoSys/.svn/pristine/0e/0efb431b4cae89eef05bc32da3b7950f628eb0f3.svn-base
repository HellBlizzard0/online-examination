package com.code.services.infosys.assignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.assignment.AssignmentEvaluation;
import com.code.dal.orm.assignment.AssignmentEvaluationPointData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.AssignmentDetailStatusEnum;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.assignment.EmployeesAssignementEvaluationWorkFlow;
import com.code.services.workflow.assignment.EmployeesAssignementWorkFlow;

public class AssignmentEmployeeService extends AssignmentService {
	/**
	 * Private constructor (singleton)
	 */
	private AssignmentEmployeeService() {
	}

	/**
	 * Validate Employee Assignment Details Mandatory Fields
	 */
	protected static void validateEmployeeAssignmentDetails(AssignmentDetailData assignmentDetailData) throws BusinessException {
		if (assignmentDetailData.getStartDate() != null && assignmentDetailData.getStartDate().compareTo(HijriDateService.getHijriSysDate()) < 0) {
			throw new BusinessException("error_assignmentInvalideStartDate");
		} else if (assignmentDetailData.getPeriod() == null) {
			throw new BusinessException("error_periodMandatory");
		} else if (assignmentDetailData.getStartDate() == null) {
			throw new BusinessException("error_startDateMandatory");
		} else if (assignmentDetailData.getReasons() == null || assignmentDetailData.getReasons().isEmpty()) {
			throw new BusinessException("error_reasonMandatory");
		} else if (!((assignmentDetailData.getPrimaryPhone() != null && !assignmentDetailData.getPrimaryPhone().trim().isEmpty()) || (assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getSecondaryPhone().trim().isEmpty()) || (assignmentDetailData.getTernaryPhone() != null) && !assignmentDetailData.getTernaryPhone().trim().isEmpty())) {
			throw new BusinessException("error_phoneMandatory");
		} else if (assignmentDetailData.getWorkNature() == null || assignmentDetailData.getWorkNature().trim().isEmpty()) {
			throw new BusinessException("error_workNatureMandatory");
		} else if (assignmentDetailData.getWorkScope() == null || assignmentDetailData.getWorkScope().trim().isEmpty()) {
			throw new BusinessException("error_workScopeMandatory");
		}
	}

	/**
	 * Validate Employee Assignment Details Mandatory Fields
	 * 
	 * @param assignmentDetailData
	 * @param isReAssigned
	 * @return
	 * @throws BusinessException
	 */
	protected static String validateEmployeeAssignmentDetails(AssignmentDetailData assignmentDetailData, boolean isReAssigned) throws BusinessException {
		validateEmployeeAssignmentDetails(assignmentDetailData);
		// first condition checks if has under approval assignment
		// second condition checks if this officer have this assignment employee before
		// third condition checks if have an active assignment
		if (hasUnderApprovalAssignment(assignmentDetailData.getIdentity(), null)) {
			return "-UNDER_APPROVAL " + assignmentDetailData.getFullName();
		} else if (!isReAssigned && !AssignmentService.getLastAssignmentDetailsData(assignmentDetailData.getOfficerId(), null, null, assignmentDetailData.getIdentity(), null, null, null, FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.ON.getCode(), InfoSourceTypeEnum.ASSIGNMENT.getCode()).isEmpty()) {
			return "-PRE_ASSIGN " + assignmentDetailData.getFullName();
		} else if (!AssignmentService.getLastAssignmentDetailsData(null, null, null, assignmentDetailData.getIdentity(), null, null, null, FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), AssignmentDetailStatusEnum.HELD.getCode(), FlagsEnum.ON.getCode(), InfoSourceTypeEnum.ASSIGNMENT.getCode()).isEmpty()
				|| !AssignmentService.getLastAssignmentDetailsData(null, null, null, assignmentDetailData.getIdentity(), null, null, null, FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), AssignmentDetailStatusEnum.HELD_NOT_STARTED.getCode(), FlagsEnum.ON.getCode(), InfoSourceTypeEnum.ASSIGNMENT.getCode()).isEmpty()) {
			return "-ASSIGNED_OTHR_OFFICER " + assignmentDetailData.getFullName();
		}

		return "";
	}

	/**
	 * Insert assignment and assignment details data
	 * 
	 * @param assignmentData
	 * @param assignmentDetailsDataList
	 * @param loginUser
	 * @param isReAssigned
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addAssignments(AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailsDataList, EmployeeData loginUser, boolean isReAssigned, CustomSession... useSession) throws BusinessException {
		validateAssignment(assignmentData);
		String assginmentErrorMessage = "";
		for (AssignmentDetailData assginDetail : assignmentDetailsDataList) {
			assginmentErrorMessage += validateEmployeeAssignmentDetails(assginDetail, isReAssigned);
		}
		// If have previous assignment then show error message with duplicate names
		if (!assginmentErrorMessage.isEmpty()) {
			String errorMessage = "";
			Object[] Params = new Object[1];

			if (assginmentErrorMessage.contains("-UNDER_APPROVAL")) {
				errorMessage = "error_hasUnderApprovalAssignment";
				assginmentErrorMessage = assginmentErrorMessage.replace("-UNDER_APPROVAL", "-");
			} else if (assginmentErrorMessage.contains("-ASSIGNED_OTHR_OFFICER")) {
				errorMessage = "error_employeeAssignedToOtherOfficer";
				assginmentErrorMessage = assginmentErrorMessage.replace("-ASSIGNED_OTHR_OFFICER", "-");
			} else {
				errorMessage = "error_hasAssignment";
				assginmentErrorMessage = assginmentErrorMessage.replace("-PRE_ASSIGN", "-");
			}

			Params[0] = assginmentErrorMessage;
			throw new BusinessException(errorMessage, Params);
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			assignmentData.setSystemUser(loginUser.getEmpId().toString());
			assignmentData.setRequestNumber(generateOrderNumber(EntitySequenceGeneratorEnum.ASSIGNMENT_EMPLOYEE.getEntityId(), session));
			EmployeesAssignementWorkFlow.initAssignemnt(assignmentData, loginUser, isReAssigned, null, session);
			DataAccess.addEntity(assignmentData.getAssignment(), session);
			assignmentData.setId(assignmentData.getAssignment().getId());
			addAssignmentDetails(assignmentData, assignmentDetailsDataList, loginUser, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			assignmentData.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update assignment and assignment details data
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param assignmentDetailsDataList
	 * @param assignmentDetailsDataDeletedList
	 * @param loginUser
	 * @param isReAssigned
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAssignments(WFTask currentTask, AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailsDataList, List<AssignmentDetailData> assignmentDetailsDataDeletedList, EmployeeData loginUser, boolean isReAssigned, CustomSession... useSession) throws BusinessException {
		validateAssignment(assignmentData);
		for (AssignmentDetailData assginDetail : assignmentDetailsDataList) {
			validateEmployeeAssignmentDetails(assginDetail);
		}
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			assignmentData.setSystemUser(loginUser.getEmpId().toString());
			EmployeesAssignementWorkFlow.resendAssignemnt(currentTask, assignmentData, loginUser, isReAssigned, null, session);
			DataAccess.updateEntity(assignmentData.getAssignment(), session);
			deleteAssignmentDetail(assignmentDetailsDataDeletedList, loginUser, session);
			if (currentTask.getFlexField1() != null && currentTask.getFlexField1().equals("true")) {
				updateAssignmentDetails(assignmentDetailsDataList, assignmentData, loginUser, session);
			} else {
				addAssignmentDetails(assignmentData, assignmentDetailsDataList, loginUser, session);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			assignmentData.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @param assignmentDetailDataList
	 * @param assignmentData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAssignmentDetails(List<AssignmentDetailData> assignmentDetailDataList, AssignmentData assignmentData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (AssignmentDetailData assignmentDetailData : assignmentDetailDataList) {
				if (assignmentDetailData.getChangeAgentCode() != null && assignmentDetailData.getChangeAgentCode()) {
					assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), true, session));
				}
				AssignmentService.updateAssignmentDetail(assignmentDetailData, loginUser, session);
			}
			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Initiate new Evaluation for approval
	 * 
	 * @param assignmentEvaluation
	 * @param assignmentEvaluationPointDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addAssignemntEvaluation(AssignmentEvaluation assignmentEvaluation, List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// can't insert new unless all previous are approved
		isApproved(assignmentEvaluation);
		validateAssignmentEvaluation(assignmentEvaluation);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			EmployeesAssignementEvaluationWorkFlow.initAssignmentEvaluation(assignmentEvaluation, loginUser, null, session);

			assignmentEvaluation.setSystemUser(loginUser.getEmpId().toString());
			assignmentEvaluation.setEvaluationDate(HijriDateService.getHijriSysDate());
			DataAccess.addEntity(assignmentEvaluation, session);
			addAssignmentEvaluationPoint(assignmentEvaluation, assignmentEvaluationPointDataList, loginUser, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Get Employee Assignment Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param officerId
	 * @param active
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeAssignmentReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, long officerId, int active, int assignmentStatus, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_START_DATE", startDateString);
			parameters.put("P_END_DATE", endDateString);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_OFFICER_ID", officerId);
			parameters.put("P_ACTIVE", active);
			parameters.put("P_ASSIGNMENT_STATUS", assignmentStatus);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.EMPLOYEE_ASSIGNMENT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Statistical Employee Assignment Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param officerId
	 * @param active
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getStatisticalEmployeeAssignmentReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, long officerId, int active, int assignmentStatus, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_START_DATE", startDateString == null ? HijriDateService.getHijriSysDateString() : startDateString);
			parameters.put("P_START_DATE_NULL", startDateString == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_END_DATE", endDateString == null ? HijriDateService.getHijriSysDateString() : endDateString);
			parameters.put("P_END_DATE_NULL", endDateString == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_OFFICER_ID", officerId);
			parameters.put("P_ACTIVE", active);
			parameters.put("P_ASSIGNMENT_STATUS", assignmentStatus);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_STATUS", AssignmentStatusEnum.APPROVED.getCode());
			String reportName = ReportNamesEnum.EMPLOYEE_ASSIGNMENT_STATISTICAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
			throw new BusinessException("error_general");
		}
	}

	public static byte[] getAssignmentLetterBytes(long assignmentId, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assignmentId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.EMPLOYEE_ASSIGNMENT_REWARD_LETTER.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentEmployeeService.class, e, "AssignmentEmployeeService");
			throw new BusinessException("error_general");
		}
	}
}