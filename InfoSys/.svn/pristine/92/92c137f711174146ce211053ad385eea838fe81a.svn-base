package com.code.services.infosys.assignment;

import java.util.ArrayList;
import java.util.Date;
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
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.assignment.NonEmployeesAssignementEvaluationWorkFlow;
import com.code.services.workflow.assignment.NonEmployeesAssignementWorkFlow;

public class AssignmentNonEmployeeService extends AssignmentService {
	/**
	 * Private constructor (singleton)
	 */
	private AssignmentNonEmployeeService() {
	}

	/**
	 * Validate Non Employee Assignment Details Mandatory Fields
	 * 
	 * @param assignmentDetailData
	 * @param isReAssigned
	 * @return
	 * @throws BusinessException
	 */
	private static String validateNonEmployeeAssignmentDetails(AssignmentDetailData assignmentDetailData, boolean isReAssigned, boolean isUpdateAssignment) throws BusinessException {
		List<AssignmentDetailData> assignmentWithSameIdentity = getAssignmentDetailsDataByIdentity(assignmentDetailData.getIdentity());
		if ((assignmentWithSameIdentity != null && assignmentWithSameIdentity.size() > 0 && !isReAssigned && !isUpdateAssignment) || (assignmentWithSameIdentity != null && assignmentWithSameIdentity.size() > 0 && (isReAssigned || isUpdateAssignment) && (!assignmentWithSameIdentity.get(0).getAgentCode().equals(assignmentDetailData.getAgentCode())))) {
			throw new BusinessException("error_assignmentWithTheSameIdentity");
		} else if (!isUpdateAssignment && assignmentDetailData.getStartDate() != null && assignmentDetailData.getStartDate().compareTo(HijriDateService.getHijriSysDate()) < 0) {
			throw new BusinessException("error_assignmentNonEmpInvalideStartDate");
		} else if (isUpdateAssignment && assignmentDetailData.getStartDate() != null && assignmentDetailData.getStartDate().compareTo(assignmentDetailData.getRequestDate()) < 0) {
			throw new BusinessException("error_assignmentNonEmpInvalideStartDate");
		}  else if ((assignmentDetailData.getWorkScope() == null || assignmentDetailData.getWorkScope().trim().isEmpty())) {
			throw new BusinessException("error_workScopeMandatory");
		} else if (assignmentDetailData.getReasons() == null || assignmentDetailData.getReasons().isEmpty()) {
			throw new BusinessException("error_reasonMandatory");
		} else if (assignmentDetailData.getIdentity() == null || assignmentDetailData.getIdentity().trim().isEmpty()) {
			throw new BusinessException("error_identityMandatory");
		} /*
			 * else if (assignmentDetailData.getIdentity().length() != 10 || (assignmentDetailData.getIdentity().length() == 10 && (!assignmentDetailData.getIdentity().startsWith("1") && !assignmentDetailData.getIdentity().startsWith("2")))) { throw new BusinessException("error_identityError"); }
			 */ else if (assignmentDetailData.getFullName() == null || assignmentDetailData.getFullName().trim().isEmpty()) {
			throw new BusinessException("error_fullNameMandatory");
		} else if (assignmentDetailData.getBirthDate() == null) {
			throw new BusinessException("error_birthDateMandatory");
		} else if (assignmentDetailData.getBirthDate() != null && HijriDateService.addSubHijriMonthsDays(assignmentDetailData.getBirthDate(), 180, 0).compareTo(HijriDateService.getHijriSysDate()) > 0) {
			throw new BusinessException("error_birthDateValidation");
		} else if (assignmentDetailData.getCountryId() == null) {
			throw new BusinessException("error_countryMandatory");
		} else if (!((assignmentDetailData.getPrimaryPhone() != null && !assignmentDetailData.getPrimaryPhone().trim().isEmpty()) || (assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getSecondaryPhone().trim().isEmpty()) || (assignmentDetailData.getTernaryPhone() != null) && !assignmentDetailData.getTernaryPhone().trim().isEmpty())) {
			throw new BusinessException("error_phoneMandatory");
		} else if (assignmentDetailData.getType().equals(InfoSourceTypeEnum.ASSIGNMENT.getCode())) {
			if (assignmentDetailData.getAgentClass() == null || assignmentDetailData.getAgentClass().isEmpty()) {
				throw new BusinessException("error_agentClassMandatory");
			} else if (assignmentDetailData.getMonthlyReward() == null) {
				throw new BusinessException("error_monthlyRewardMandatory");
			} else if (assignmentDetailData.getPaymentMethod() == null) {
				throw new BusinessException("error_paymentMandatory");
			} else if (assignmentDetailData.getPaymentMethod().equals(PaymentMethodEnum.BANK_ACCOUNT.getCode()) && (assignmentDetailData.getBankBranchId() == null || assignmentDetailData.getIban() == null || assignmentDetailData.getIban().isEmpty())) {
				throw new BusinessException("error_paymentAccountMandatory");
			}
		} else if (((assignmentDetailData.getPrimaryPhone() != null && !assignmentDetailData.getPrimaryPhone().trim().isEmpty()) && (assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getSecondaryPhone().trim().isEmpty()) && assignmentDetailData.getPrimaryPhone().equals(assignmentDetailData.getSecondaryPhone()))
				|| ((assignmentDetailData.getPrimaryPhone() != null && !assignmentDetailData.getPrimaryPhone().trim().isEmpty()) && (assignmentDetailData.getTernaryPhone() != null && !assignmentDetailData.getTernaryPhone().trim().isEmpty()) && assignmentDetailData.getPrimaryPhone().equals(assignmentDetailData.getTernaryPhone()))
				|| ((assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getSecondaryPhone().trim().isEmpty()) && (assignmentDetailData.getTernaryPhone() != null && !assignmentDetailData.getTernaryPhone().trim().isEmpty()) && assignmentDetailData.getSecondaryPhone().equals(assignmentDetailData.getTernaryPhone()))) {
			throw new BusinessException("error_phoneRepeated");
		}

		// first condition checks if has under approval assignment
		// second condition checks if this officer have this assignment employee before
		// third condition checks if have an active assignment

		else if (hasUnderApprovalAssignment(assignmentDetailData.getIdentity(), null)) {
			return "-UNDER_APPROVAL " + assignmentDetailData.getFullName();
		} else if (!AssignmentService.getLastAssignmentDetailsData(null, null, null, assignmentDetailData.getIdentity(), null, null, null, null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).isEmpty()
				&& AssignmentService.getLastAssignmentDetailsData(assignmentDetailData.getOfficerId(), null, null, assignmentDetailData.getIdentity(), null, null, null, null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).isEmpty()) {
			return "-ASSIGNED_OTHR_OFFICER " + assignmentDetailData.getFullName();
		} else if (!isReAssigned && !AssignmentService.getLastAssignmentDetailsData(assignmentDetailData.getOfficerId(), null, null, assignmentDetailData.getIdentity(), null, null, null, null, AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.OFF.getCode(), assignmentDetailData.getType()).isEmpty()) {
			return "-PRE_ASSIGN " + assignmentDetailData.getFullName();
		}
		return "";
	}

	/**
	 * Validate Non Employee Assignment Details Mandatory Fields
	 * 
	 * @param assignmentDetailData
	 * @return
	 * @throws BusinessException
	 */
	private static void validateNonEmployeeAssignmentDetails(AssignmentDetailData assignmentDetailData) throws BusinessException {
		if ((assignmentDetailData.getWorkScope() == null || assignmentDetailData.getWorkScope().trim().isEmpty())) {
			throw new BusinessException("error_workScopeMandatory");
		} else if (!((assignmentDetailData.getPrimaryPhone() != null && !assignmentDetailData.getPrimaryPhone().trim().isEmpty()) || (assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getSecondaryPhone().trim().isEmpty()) || (assignmentDetailData.getTernaryPhone() != null) && !assignmentDetailData.getTernaryPhone().trim().isEmpty())) {
			throw new BusinessException("error_phoneMandatory");
		} else if (assignmentDetailData.getType().equals(InfoSourceTypeEnum.ASSIGNMENT.getCode())) {
			if (assignmentDetailData.getPaymentMethod() == null) {
				throw new BusinessException("error_paymentMandatory");
			} else if (assignmentDetailData.getPaymentMethod().equals(PaymentMethodEnum.BANK_ACCOUNT.getCode()) && (assignmentDetailData.getBankBranchId() == null || assignmentDetailData.getIban() == null || assignmentDetailData.getIban().isEmpty())) {
				throw new BusinessException("error_paymentAccountMandatory");
			}
		} else if ((assignmentDetailData.getPrimaryPhone() != null && assignmentDetailData.getSecondaryPhone() != null && !assignmentDetailData.getPrimaryPhone().isEmpty() && !assignmentDetailData.getSecondaryPhone().isEmpty() && assignmentDetailData.getPrimaryPhone().equals(assignmentDetailData.getSecondaryPhone()))
				|| (assignmentDetailData.getPrimaryPhone() != null && assignmentDetailData.getTernaryPhone() != null && !assignmentDetailData.getPrimaryPhone().isEmpty() && !assignmentDetailData.getTernaryPhone().isEmpty() && assignmentDetailData.getPrimaryPhone().equals(assignmentDetailData.getTernaryPhone()))
				|| (assignmentDetailData.getSecondaryPhone() != null && assignmentDetailData.getTernaryPhone() != null && !assignmentDetailData.getSecondaryPhone().isEmpty() && !assignmentDetailData.getTernaryPhone().isEmpty() && assignmentDetailData.getSecondaryPhone().equals(assignmentDetailData.getTernaryPhone()))) {
			throw new BusinessException("error_phoneRepeated");
		}
	}

	/**
	 * Insert assignment and assignment details
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
			assginmentErrorMessage = validateNonEmployeeAssignmentDetails(assginDetail, isReAssigned, false);
		}

		// If have previous assignment then show error message with duplicate name
		if (!assginmentErrorMessage.isEmpty()) {
			String errorMessage = "";
			Object[] Params = new Object[1];

			if (assginmentErrorMessage.contains("-UNDER_APPROVAL")) {
				errorMessage = "error_hasUnderApprovalAssignment";
				assginmentErrorMessage = assginmentErrorMessage.replace("-UNDER_APPROVAL", "-");
			} else if (assginmentErrorMessage.contains("-ASSIGNED_OTHR_OFFICER")) {
				errorMessage = "error_nonEmployeeAssignedToOtherOfficer";
				assginmentErrorMessage = assginmentErrorMessage.replace("-ASSIGNED_OTHR_OFFICER", "-");
			} else {
				errorMessage = "error_hasNonEmpAssignment";
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
			assignmentData.setRequestNumber(generateOrderNumber(EntitySequenceGeneratorEnum.ASSIGNMENT_NON_EMPLOYEE.getEntityId(), session));
			NonEmployeesAssignementWorkFlow.initAssignemnt(assignmentData, loginUser, isReAssigned, null, session);
			DataAccess.addEntity(assignmentData.getAssignment(), session);
			assignmentData.setId(assignmentData.getAssignment().getId());
			addAssignmentDetails(assignmentData, assignmentDetailsDataList, loginUser, session);

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
				Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	public static void updateAssignmentDetails(AssignmentDetailData assignmentDetailData, AssignmentData assignmentData, EmployeeData loginUser, boolean initWorkFlow, CustomSession... useSession) throws BusinessException {
		validateNonEmployeeAssignmentDetails(assignmentDetailData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			if (assignmentDetailData.getChangeAgentCode() != null && assignmentDetailData.getChangeAgentCode()) {
				assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), false, session));
			}
			assignmentData.setSystemUser(loginUser.getEmpId().toString());
			AssignmentService.updateAssignment(assignmentData, loginUser, session);
			if (initWorkFlow) {
				assignmentDetailData.setStatus(AssignmentStatusEnum.UNDER_APPROVAL.getCode());
				NonEmployeesAssignementWorkFlow.editAssignment(assignmentData, loginUser, null, session);
			}
			AssignmentService.updateAssignmentDetail(assignmentDetailData, loginUser, session);
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
				Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
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
	public static void addAssignemntEvlauation(AssignmentEvaluation assignmentEvaluation, List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		// can't insert new unless all previous are approved
		isApproved(assignmentEvaluation);
		validateAssignmentEvaluation(assignmentEvaluation);
		validateAssignmentEvaluationPoint(assignmentEvaluationPointDataList);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			NonEmployeesAssignementEvaluationWorkFlow.initAssignmentEvaluation(assignmentEvaluation, loginUser, null, session);
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
				Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * Get Nonemployee Assignment Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param officerId
	 * @param active
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonemployeeAssignmentReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, long officerId, int active, int assignmentStatus, String loginEmployeeName, int assignmentType) throws BusinessException {
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
			parameters.put("P_TYPE", assignmentType);
			String reportName = ReportNamesEnum.NONEMPLOYEE_ASSIGNMENT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Statistical Nonemployee Assignment Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param officerId
	 * @param active
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getStatisticalNonemployeeAssignmentReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, long officerId, int active, int assignmentStatus, String loginEmployeeName, int assignmentType) throws BusinessException {
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
			parameters.put("P_TYPE", assignmentType);
			parameters.put("P_STATUS", AssignmentStatusEnum.APPROVED.getCode());
			String reportName = ReportNamesEnum.NONEMPLOYEE_ASSIGNMENT_STATISTICAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
			throw new BusinessException("error_general");
		}
	}

	/**********************************************/
	/******************* Queries *****************/
	/**
	 * Get total monthly rewards for a region
	 * 
	 * @param regionId
	 * @param month
	 * @param year
	 * @return
	 * @throws BusinessException
	 */
	public static Double getRegionAssignmentDetailsMonthlyReward(Long regionId, Integer month, Integer year) throws BusinessException {
		Date startDate = HijriDateService.getHijriDate("1/" + month + "/" + year);
		Date endDate = HijriDateService.addSubHijriMonthsDays(startDate, 1, 0);
		return sumMonthlyReward(regionId == null ? FlagsEnum.ALL.getCode() : regionId, startDate, endDate);
	}

	/**
	 * Get assignment details count for a region
	 * 
	 * @param regionId
	 * @param month
	 * @param year
	 * @return
	 * @throws BusinessException
	 */
	public static Integer getRegionAssignmentDetailsCount(Long regionId, Integer month, Integer year) throws BusinessException {
		Date startDate = HijriDateService.getHijriDate("1/" + month + "/" + year);
		Date endDate = HijriDateService.addSubHijriMonthsDays(startDate, 1, 0);
		return getActiveAssignmentDetailDataByDate(regionId, startDate, endDate).size();
	}

	/**
	 * Get assignment details for a region for assignment type only
	 * 
	 * @param regionId
	 * @param month
	 * @param year
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getRegionAssignmentDetails(Long regionId, Integer month, Integer year) throws BusinessException {
		Date startDate = HijriDateService.getHijriDate("1/" + month + "/" + year);
		Date endDate = HijriDateService.addSubHijriMonthsDays(startDate, 1, 0);
		return getActiveAssignmentDetailDataByDate(regionId, startDate, endDate);
	}

	/**
	 * Get Active Assignment Details Data
	 * 
	 * @param regionId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getActiveAssignmentDetailDataByDate(Long regionId, Date startDate, Date endDate) throws BusinessException {
		try {
			return searchActiveAssignmentDetailDataByDate(regionId, startDate, endDate);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search Active Assignment Details Data
	 * 
	 * @param regionId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchActiveAssignmentDetailDataByDate(Long regionId, Date startDate, Date endDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_END_DATE_NULL", endDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));
			qParams.put("P_STATUS", AssignmentStatusEnum.APPROVED.getCode());
			qParams.put("P_TYPE_ASSIGNMENT", InfoSourceTypeEnum.ASSIGNMENT.getCode());
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAILS_DATA_SEARCH_ACTIVE_ASSIGNMENT_DETAIL_DATE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Sum total monthly rewards for a region and assignment source only
	 * 
	 * @param regionId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 */
	private static Double sumMonthlyReward(long regionId, Date startDate, Date endDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_END_DATE_NULL", endDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));
			qParams.put("P_STATUS", AssignmentStatusEnum.APPROVED.getCode());
			qParams.put("P_TYPE_ASSIGNMENT", InfoSourceTypeEnum.ASSIGNMENT.getCode());
			return DataAccess.executeNamedQuery(Double.class, QueryNamesEnum.ASSIGNMENT_DETAILS_DATA_SUM_MONTHLY_REWARD.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return (double) 0;
		}
	}

	/**
	 * 
	 * @param currentTask
	 * @param assignmentData
	 * @param assignmentDetailsDataList
	 * @param loginUser
	 * @param isReAssigned
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAssignments(WFTask currentTask, AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailsDataList, EmployeeData loginUser, boolean isReAssigned, CustomSession... useSession) throws BusinessException {
		validateAssignment(assignmentData);
		String assginmentErrorMessage = "";
		for (AssignmentDetailData assginDetail : assignmentDetailsDataList) {
			assginmentErrorMessage = validateNonEmployeeAssignmentDetails(assginDetail, isReAssigned, true);
		}

		// If have previous assignment then show error message with duplicate name
		if (!assginmentErrorMessage.isEmpty()) {
			String errorMessage = "";
			Object[] Params = new Object[1];

			if (assginmentErrorMessage.contains("-UNDER_APPROVAL")) {
				errorMessage = "error_hasUnderApprovalAssignment";
				assginmentErrorMessage = assginmentErrorMessage.replace("-UNDER_APPROVAL", "-");
			} else if (assginmentErrorMessage.contains("-ASSIGNED_OTHR_OFFICER")) {
				errorMessage = "error_nonEmployeeAssignedToOtherOfficer";
				assginmentErrorMessage = assginmentErrorMessage.replace("-ASSIGNED_OTHR_OFFICER", "-");
			} else {
				errorMessage = "error_hasNonEmpAssignment";
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
			NonEmployeesAssignementWorkFlow.resendAssignemnt(currentTask, assignmentData, loginUser, isReAssigned, null, session);
			DataAccess.updateEntity(assignmentData.getAssignment(), session);
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
				Log4j.traceErrorException(AssignmentNonEmployeeService.class, e, "AssignmentNonEmployeeService");
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
}
