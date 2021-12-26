package com.code.services.infosys.assignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.assignment.AssignmentAgentClass;
import com.code.dal.orm.assignment.AssignmentAgentCode;
import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.assignment.AssignmentEvaluation;
import com.code.dal.orm.assignment.AssignmentEvaluationPointData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;

public abstract class AssignmentService extends BaseService {
	/**
	 * Private constructor (singleton)
	 */
	public AssignmentService() {
	}

	/**
	 * Validate Assignment Evaluation Mandatory Fields
	 * 
	 * @param assignmentEvaluation
	 * @throws BusinessException
	 */
	protected static void validateAssignmentEvaluationPoint(List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList) throws BusinessException {
		for (AssignmentEvaluationPointData assignmentEvalPointData : assignmentEvaluationPointDataList) {
			if (assignmentEvalPointData.getAvailable()) {
				return;
			}
		}
		throw new BusinessException("error_mandatory");
	}

	/**
	 * Validate Assignment Evaluation Mandatory Fields
	 * 
	 * @param assignmentEvaluation
	 * @throws BusinessException
	 */
	protected static void validateAssignmentEvaluation(AssignmentEvaluation assignmentEvaluation) throws BusinessException {
		if (assignmentEvaluation.getReasons() == null || assignmentEvaluation.getReasons().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
	}

	/**
	 * Validate Assignment Mandatory Fields
	 * 
	 * @param assignmentData
	 * @throws BusinessException
	 */
	protected static void validateAssignment(AssignmentData assignmentData) throws BusinessException {
		if (assignmentData.getRegionId() == null) {
			throw new BusinessException("error_regionMandatory");
		} else if (!assignmentData.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()) && assignmentData.getSectorId() == null) {
			throw new BusinessException("error_sectorUnitMandatory");
		} else if (assignmentData.getOfficerId() == null) {
			throw new BusinessException("error_officerMandatory");
		} else if (assignmentData.getStartDate() == null) {
			throw new BusinessException("error_startDateMandatory");
		} else if (assignmentData.getPeriod() == null) {
			throw new BusinessException("error_periodMandatory");
		}
	}

	/**
	 * Update Assignment
	 * 
	 * @param assignmentData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAssignment(AssignmentData assignmentData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			assignmentData.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(assignmentData.getAssignment(), session);
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
	 * Insert assignment details data
	 * 
	 * @param assignmentData
	 * @param assignmentDetailList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	protected static void addAssignmentDetails(AssignmentData assignmentData, List<AssignmentDetailData> assignmentDetailList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (AssignmentDetailData assignmentDetailData : assignmentDetailList) {
				assignmentDetailData.setAssginmentId(assignmentData.getAssignment().getId());
				assignmentDetailData.setRequestNumber(assignmentData.getRequestNumber());
				assignmentDetailData.setRequestDate(assignmentData.getRequestDate());
				if (assignmentDetailData.getEmployeeId() != null) {
					if (assignmentDetailData.getChangeAgentCode() != null && assignmentDetailData.getChangeAgentCode()) {
						assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), true, session));
					} else {
						List<AssignmentDetailData> pastAssignments = getAssignmentDetailsDataByIdentityAndRegionId(assignmentData.getRegionId(), assignmentDetailData.getIdentity(), FlagsEnum.OFF.getCode(), FlagsEnum.ALL.getCode());
						if (!pastAssignments.isEmpty()) {
							assignmentDetailData.setAgentCode(pastAssignments.get(0).getAgentCode());
						} else {
							assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), true, session));
						}
					}
				} else {
					if (assignmentDetailData.getChangeAgentCode() != null && assignmentDetailData.getChangeAgentCode()) {
						assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), false, session));
					} else {
						List<AssignmentDetailData> pastAssignments = getAssignmentDetailsDataByIdentityAndRegionId(FlagsEnum.ALL.getCode(), assignmentDetailData.getIdentity(), null, FlagsEnum.ALL.getCode());
						if (!pastAssignments.isEmpty()) {
							assignmentDetailData.setAgentCode(pastAssignments.get(0).getAgentCode());
						} else {
							assignmentDetailData.setAgentCode(generateAgentCode(assignmentData.getSectorId() == null ? assignmentData.getRegionId() : assignmentData.getSectorId(), false, session));
						}
					}
				}
				assignmentDetailData.getAssignmentDetail().setSystemUser(loginUser.getEmpId().toString());
				if (assignmentDetailData.getStatus() == null) {
					assignmentDetailData.setStatus(AssignmentStatusEnum.UNDER_APPROVAL.getCode());
				}
				if (assignmentDetailData.getAssignmentDetail().getId() == null) {
					DataAccess.addEntity(assignmentDetailData.getAssignmentDetail(), session);
					assignmentDetailData.setId(assignmentDetailData.getAssignmentDetail().getId());
				} else {
					DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);
				}
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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update assignment detail
	 * 
	 * @param assignmentDetailData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAssignmentDetail(AssignmentDetailData assignmentDetailData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			assignmentDetailData.getAssignmentDetail().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(assignmentDetailData.getAssignmentDetail(), session);

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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete assignment detail
	 * 
	 * @param assignmentDetailDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteAssignmentDetail(List<AssignmentDetailData> assignmentDetailDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (AssignmentDetailData assignmentDetail : assignmentDetailDataList) {
				assignmentDetail.getAssignmentDetail().setSystemUser(loginUser.getEmpId().toString());
				DataAccess.deleteEntity(assignmentDetail.getAssignmentDetail(), session);
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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update Evaluation during workflow
	 * 
	 * @param assignmentEvaluation
	 * @param assignmentEvaluationPointDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateEvaluation(AssignmentEvaluation assignmentEvaluation, List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		validateAssignmentEvaluation(assignmentEvaluation);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			assignmentEvaluation.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(assignmentEvaluation, session);
			updateEvaluationPoint(assignmentEvaluationPointDataList, assignmentEvaluation, loginUser, session);
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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update Evaluation during workflow
	 * 
	 * @param assignmentEvaluationPointDataList
	 * @param assignmentEvaluation
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateEvaluationPoint(List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList, AssignmentEvaluation assignmentEvaluation, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		validateAssignmentEvaluationPoint(assignmentEvaluationPointDataList);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (AssignmentEvaluationPointData assignmentEvaluationPointData : assignmentEvaluationPointDataList) {
				assignmentEvaluationPointData.setAssignmentEvaluationId(assignmentEvaluation.getId());
				DataAccess.updateEntity(assignmentEvaluationPointData.getAssignmentEvaluationPoint(), session);
				assignmentEvaluationPointData.setId(assignmentEvaluationPointData.getAssignmentEvaluationPoint().getId());
				assignmentEvaluationPointData.getAssignmentEvaluationPoint().setSystemUser(loginUser.getEmpId().toString());
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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Insert assignment evaluation point
	 * 
	 * @param assignmentEvaluation
	 * @param assignmentEvaluationPointDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	protected static void addAssignmentEvaluationPoint(AssignmentEvaluation assignmentEvaluation, List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			double totalPoints = 0;
			int availableCount = 0;
			for (AssignmentEvaluationPointData assignmentPointData : assignmentEvaluationPointDataList) {
				if (assignmentPointData.getAvailable()) {
					assignmentPointData.setPercent(assignmentPointData.getPercent() == null ? 0 : assignmentPointData.getPercent());
					totalPoints += assignmentPointData.getPercent();
					availableCount++;
				} else {
					assignmentPointData.setPercent(0);
				}

				assignmentPointData.setAssignmentEvaluationId(assignmentEvaluation.getId());
				assignmentPointData.getAssignmentEvaluationPoint().setSystemUser(loginUser.getEmpId().toString());
				DataAccess.addEntity(assignmentPointData.getAssignmentEvaluationPoint(), session);
				assignmentPointData.setId(assignmentPointData.getAssignmentEvaluationPoint().getId());
			}
			if (availableCount == 0) {
				throw new BusinessException("error_mandatory");
			}
			DecimalFormat df2 = new DecimalFormat(".##");
			double totalEval = 0;
			if (availableCount != 0)
				totalEval = totalPoints / availableCount;
			assignmentEvaluation.setTotalEvaluation(Double.parseDouble(df2.format(totalEval)));
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
				Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Generate assignment agent code
	 * 
	 * @param departmentId
	 * @param type
	 * @param session
	 * @return
	 * @throws BusinessException
	 * @throws DatabaseException
	 */
	protected static String generateAgentCode(long departmentId, boolean employeeAssignment, CustomSession... session) throws BusinessException, DatabaseException {
		// Agent PreDefined prefix
		// 1,2 + departmentId
		// sen or mem
		boolean generatedCodeNotExist = false;
		String agentCode = "";
		while (!generatedCodeNotExist) {
			long agentCodeSeq = CommonService.generateSequenceNumber(Long.parseLong((employeeAssignment ? "1" : "2") + departmentId), 10000, session);
			String assignType = employeeAssignment ? "\u0633" : "\u0645";
			agentCode = searchAssignmentAgentCode(departmentId).getAgentPrefix() + "/" + String.format("%04d", agentCodeSeq) + assignType;
			try {
				searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), agentCode, null, null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);
				generatedCodeNotExist = false;
			} catch (NoDataException e) {
				generatedCodeNotExist = true;
				return agentCode;
			}
		}
		return agentCode;
	}

	/**
	 * Generate assignment employee / non employee order number
	 * 
	 * @param session
	 * @return
	 * @throws BusinessException
	 * @throws DatabaseException
	 */
	protected static String generateOrderNumber(long entityId, CustomSession session) throws BusinessException, DatabaseException {
		// 3 digits : 3 digits serial
		long assignmentRequestNumber = CommonService.generateSequenceNumber(entityId, Integer.MAX_VALUE, session);
		return assignmentRequestNumber + "";
	}

	/**
	 * Check if evaluation has running instance
	 * 
	 * @param assignmentEvaluation
	 * @throws BusinessException
	 */
	protected static void isApproved(AssignmentEvaluation assignmentEvaluation) throws BusinessException {
		List<AssignmentEvaluation> assignmentEvaluations = getAssignmentEvaluationWfInstances(assignmentEvaluation.getAssignmentDetailId(), WFInstanceStatusEnum.RUNING.getCode(), null, null);
		if (!assignmentEvaluations.isEmpty()) {
			throw new BusinessException("error_assignmentEvaluationRunning");
		}
	}

	/**
	 * Decide if department is defined under Region in organization hierarchy
	 * 
	 * @param departmentId
	 * @return boolean
	 * @throws BusinessException
	 */
	public static Long isSectorDepartment(long departmentId) throws BusinessException { // return
		DepartmentData department = DepartmentService.getDepartment(departmentId);
		if (department.getDepartmentTypeId().equals(DepartmentTypeEnum.SECTOR.getCode()))
			return departmentId;
		return DepartmentService.getDepartmentSectorData(departmentId);
	}

	/**
	 * Get Non Employee Re Assignment Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonEmployeeReAssignmentDetailBytes(long assginmentId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.NON_EMPLOYEE_RE_ASSIGNMENT_DETAIL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Non Employee Assignment Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonEmployeeAssignmentDetailBytes(long assginmentId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.NON_EMPLOYEE_ASSIGNMENT_DETAIL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Non Employee Eval Resume Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonEmployeeEvalResumeDetailBytes(long assginmentId, long evalId, Integer period, String reason, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_EVAL_ID", evalId);
			parameters.put("P_PERIOD", period);
			parameters.put("P_REASON", reason);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.NON_EMPLOYEE_EVAL_RESUME.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Non Employee Eval End Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonEmployeeEvalEndDetailBytes(long assginmentId, long evalId, String reason, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_EVAL_ID", evalId);
			parameters.put("P_REASON", reason);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.NON_EMPLOYEE_EVAL_END.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Employee Assignment Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeAssignmentDetailBytes(long assginmentId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EMPLOYEE_ASSIGNMENT_DETAIL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Employee Assignment Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeReAssignmentDetailBytes(long assignmentDetailId, long assginmentId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EMPLOYEE_RE_ASSIGNMENT_DETAIL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Employee Eval End Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeEvalEndDetailBytes(long assginmentId, long assignmentDetailId, long evalId, String reason, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			parameters.put("P_EVAL_ID", evalId);
			parameters.put("P_REASON", reason);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EMPLOYEE_EVAL_END.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Employee Eval Resume Detail Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeEvalResumeDetailBytes(long assginmentId, long assignmentDetailId, long evalId, Integer period, String reason, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ASSIGNMENT_ID", assginmentId);
			parameters.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			parameters.put("P_EVAL_ID", evalId);
			parameters.put("P_PERIOD", period);
			parameters.put("P_REASON", reason);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EMPLOYEE_EVAL_RESUME.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**********************************************/
	/******************* Queries *****************/
	/**
	 * Get all assignments
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @param officerId
	 * @param departmentId
	 * @param hijriStartDate
	 * @param hijriEndData
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentData> getAssignments(Long id, Long wfInstanceId, Long officerId, Long departmentId, Date hijriStartDate, Date hijriEndData) throws BusinessException {
		try {
			return searchAssignments(id == null ? FlagsEnum.ALL.getCode() : id, wfInstanceId == null ? FlagsEnum.ALL.getCode() : wfInstanceId, officerId == null ? FlagsEnum.ALL.getCode() : officerId, departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, hijriStartDate, hijriEndData);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentData>();
		}
	}

	/**
	 * Search all assignments
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @param officerId
	 * @param departmentId
	 * @param hijriStartDate
	 * @param hijriEndData
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentData> searchAssignments(long id, long wfInstanceId, long officerId, long departmentId, Date hijriStartDate, Date hijriEndData) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INSTANCE_ID", wfInstanceId);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_HIJRI_START_DATE", hijriStartDate == null ? new Date() : hijriStartDate);
			qParams.put("P_HIJRI_START_DATE_NULL", hijriStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			return DataAccess.executeNamedQuery(AssignmentData.class, QueryNamesEnum.ASSIGNMENT_DATA_SEARCH_ASSIGNMENTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get assignment Details data
	 * 
	 * @param identity
	 * @param eliminated
	 * @return
	 * @throws BusinessException
	 */
	public static boolean hasUnderApprovalAssignment(String identity, Integer eliminated) throws BusinessException {
		try {
			List<AssignmentDetailData> assignmentDetail = searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), eliminated == null ? FlagsEnum.ALL.getCode() : eliminated, identity, AssignmentStatusEnum.UNDER_APPROVAL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(),
					FlagsEnum.ALL.getCode());
			if (assignmentDetail.isEmpty()) {
				return false;
			}
			return true;
		} catch (NoDataException e) {
			return false;
		}
	}

	/**
	 * Get assignment Details data
	 * 
	 * @param assignmentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentDetailsDataByAssignmentId(long assignmentId) throws BusinessException {
		try {
			return searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), assignmentId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static AssignmentDetailData getAssignmentDetailsDataById(long id) throws BusinessException {
		try {
			return searchAssignmentDetailsData(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param identityNo
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentDetailsDataByIdentity(String identityNo) throws BusinessException {
		try {
			return searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), identityNo, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * 
	 * @param regionId
	 * @param identityNo
	 * @param eliminated
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentDetailsDataByIdentityAndRegionId(long regionId, String identityNo, Integer eliminated, long notOfficerId) throws BusinessException {
		try {
			return searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), notOfficerId, null, null, null, FlagsEnum.ALL.getCode(), null, null, null, regionId, FlagsEnum.ALL.getCode(), eliminated == null ? FlagsEnum.ALL.getCode() : eliminated, identityNo, AssignmentStatusEnum.APPROVED.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Get active assignment details data
	 * 
	 * @param officerId
	 * @param agentCode
	 * @param sectorName
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getActiveAssignmentDetailsData(long officerId, String agentCode, String sectorName, Integer assignmentType) throws BusinessException {
		try {
			return searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), officerId, FlagsEnum.ALL.getCode(), agentCode, null, sectorName, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, AssignmentStatusEnum.APPROVED.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ON.getCode(), assignmentType == null ? FlagsEnum.ALL.getCode() : assignmentType);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Get all assignments details data
	 * 
	 * @param officerId
	 * @param agentCode
	 * @param sectorName
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentDetailsData(long officerId, String agentCode, String sectorName, Integer assignmentType) throws BusinessException {
		try {
			return searchLastAssignmentDetailsData(officerId, agentCode, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), AssignmentStatusEnum.APPROVED.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), assignmentType, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());

		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Get active assignment details data by regionId
	 * 
	 * @param regionId
	 * @param agentCode
	 * @param sectorName
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getActiveAssignmentDetailsDataByRegionId(long regionId, String agentCode, String sectorName, Integer assignmentType) throws BusinessException {
		try {
			return searchAssignmentDetailsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), agentCode, null, sectorName, FlagsEnum.ALL.getCode(), null, null, null, regionId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, AssignmentStatusEnum.APPROVED.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ON.getCode(), assignmentType == null ? FlagsEnum.ALL.getCode() : assignmentType);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search all assignment details data
	 * 
	 * @param id
	 * @param assignmentId
	 * @param officerId
	 * @param agentCode
	 * @param fullName
	 * @param employeeId
	 * @param startDate
	 * @param approvedEndDate
	 * @param requestNumber
	 * @param regionId
	 * @param sectorId
	 * @param eliminated
	 * @param identity
	 * @param status
	 * @param notEqStatus
	 * @param employeeNonEmployeeFlag
	 * @param heldFlag
	 * @param assignmentType
	 * @param session
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchAssignmentDetailsData(long id, long assignmentId, long officerId, long notOfficerId, String agentCode, String fullName, String sectorName, long employeeId, Date startDate, Date approvedEndDate, String requestNumber, long regionId, long sectorId, int eliminated, String identity, int status, int notEqStatus, int employeeNonEmployeeFlag, int heldFlag, int assignmentType, CustomSession... session) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_ASSIGNMENT_ID", assignmentId);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_NOT_OFFICER_ID", notOfficerId);
			qParams.put("P_AGENT_CODE", agentCode == null || agentCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + agentCode + "%");
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() + "" : identity);
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_APPROVED_END_DATE_NULL", approvedEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_APPROVED_END_DATE", approvedEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(approvedEndDate));
			qParams.put("P_REQUEST_NUMBER", requestNumber == null || requestNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : requestNumber);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_SECTOR_NAME", sectorName == null || sectorName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + sectorName + "%");
			qParams.put("P_ELIMINATED", eliminated);
			qParams.put("P_IS_EMPLOYEE", employeeNonEmployeeFlag);
			qParams.put("P_STATUS", status);
			qParams.put("P_NOT_EQ_STATUS", notEqStatus);
			qParams.put("P_HELD_FLAG", heldFlag);
			qParams.put("P_CURRENT_DATE", HijriDateService.getHijriSysDateString());
			qParams.put("P_TYPE", assignmentType);
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAILS_DATA_SEARCH_ASSIGNMENT_DETAILS_DATA.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get info related assignments
	 * 
	 * @param infoId
	 * @return list of info related assignments
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getInfoAssignments(long infoId) throws BusinessException {
		try {
			return searchInfoAssignments(infoId);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search info related assignments filtered by infoId
	 * 
	 * @param infoId
	 * @return list of info related assignments
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchInfoAssignments(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAIL_DATA_SEARCH_INFO_ASSIGNMENT_DETAILS_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all assignment evaluation points
	 * 
	 * @return list of assignment evaluation points
	 * @throws BusinessException
	 */
	public static List<AssignmentEvaluationPointData> getAssignmentEvaluationPoints() throws BusinessException {
		List<AssignmentEvaluationPointData> assignmentPointDataList = new ArrayList<AssignmentEvaluationPointData>();

		List<SetupDomain> setupDomainList = SetupService.getDomains(ClassesEnum.ASSIGNMENT_ATTENDANCE_EVALUATION.getCode());
		SetupClass attendanceEval = SetupService.getClasses(ClassesEnum.ASSIGNMENT_ATTENDANCE_EVALUATION.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		for (SetupDomain domain : setupDomainList) {
			AssignmentEvaluationPointData assignmentEvalPoint = new AssignmentEvaluationPointData();
			assignmentEvalPoint.setPercent(0);
			assignmentEvalPoint.setDomainEvaluationPointId(domain.getId());
			assignmentEvalPoint.setAvailable(false);
			assignmentEvalPoint.setDomainDescription(domain.getDescription());
			assignmentEvalPoint.setClassDescription(attendanceEval.getDescription());
			assignmentPointDataList.add(assignmentEvalPoint);
		}

		setupDomainList = SetupService.getDomains(ClassesEnum.ASSIGNMENT_PERSONAL_EVALUATION.getCode());
		SetupClass personalEval = SetupService.getClasses(ClassesEnum.ASSIGNMENT_PERSONAL_EVALUATION.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		for (SetupDomain domain : setupDomainList) {
			AssignmentEvaluationPointData assignmentEvalPoint = new AssignmentEvaluationPointData();
			assignmentEvalPoint.setPercent(0);
			assignmentEvalPoint.setDomainEvaluationPointId(domain.getId());
			assignmentEvalPoint.setAvailable(false);
			assignmentEvalPoint.setDomainDescription(domain.getDescription());
			assignmentEvalPoint.setClassDescription(personalEval.getDescription());
			assignmentPointDataList.add(assignmentEvalPoint);
		}

		setupDomainList = SetupService.getDomains(ClassesEnum.ASSIGNMENT_INFO_EVALUATION.getCode());
		SetupClass infoEval = SetupService.getClasses(ClassesEnum.ASSIGNMENT_INFO_EVALUATION.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		for (SetupDomain domain : setupDomainList) {
			AssignmentEvaluationPointData assignmentEvalPoint = new AssignmentEvaluationPointData();
			assignmentEvalPoint.setPercent(0);
			assignmentEvalPoint.setDomainEvaluationPointId(domain.getId());
			assignmentEvalPoint.setAvailable(false);
			assignmentEvalPoint.setDomainDescription(domain.getDescription());
			assignmentEvalPoint.setClassDescription(infoEval.getDescription());
			assignmentPointDataList.add(assignmentEvalPoint);
		}
		return assignmentPointDataList;
	}

	/**
	 * Get agent classes
	 * 
	 * @return list of assignment agent classes
	 * @throws BusinessException
	 */
	public static List<AssignmentAgentClass> getAgentClasses() throws BusinessException {
		try {
			return searchAgentClasses();
		} catch (NoDataException e) {
			return new ArrayList<AssignmentAgentClass>();
		}
	}

	/**
	 * Search agent classes
	 * 
	 * @return list of assignment agent classes
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentAgentClass> searchAgentClasses() throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			return DataAccess.executeNamedQuery(AssignmentAgentClass.class, QueryNamesEnum.ASSIGNMENT_AGENT_CLASS_SEARCH_AGENT_CLASSES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get assignment evaluation wf instance
	 * 
	 * @param assignmentDetailId
	 * @param wfInstanceStatus
	 * @param evalDate
	 * @param evalAction
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentEvaluation> getAssignmentEvaluationWfInstances(Long assignmentDetailId, Integer wfInstanceStatus, Date evalDate, Integer evalAction) throws BusinessException {
		try {
			return searchAssignmentEvaluationWfInstances(assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId, wfInstanceStatus == null ? FlagsEnum.ALL.getCode() : wfInstanceStatus, evalDate, evalAction == null ? FlagsEnum.ALL.getCode() : evalAction);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentEvaluation>();
		}
	}

	/**
	 * Search assignment evaluation wf instance
	 * 
	 * @param assignmentDetailId
	 * @param wfInstanceStatus
	 * @param evalDate
	 * @param evalAction
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentEvaluation> searchAssignmentEvaluationWfInstances(long assignmentDetailId, int wfInstanceStatus, Date evalDate, int evalAction) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_INSTANCE_STATUS", wfInstanceStatus);
			qParams.put("P_EVAL_ACTION", evalAction);
			qParams.put("P_EVAL_DATE_NULL", evalDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_EVAL_DATE", evalDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(evalDate));
			return DataAccess.executeNamedQuery(AssignmentEvaluation.class, QueryNamesEnum.ASSIGNMENT_EVALUATION_ASSIGNMENT_EVALUATION_WF_INSTANCES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get assignment evaluation
	 * 
	 * @param assignmentDetailId
	 * @param wfInstanceId
	 * @param evalAction
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentEvaluation> getAssignmentEvaluation(Long assignmentDetailId, Long wfInstanceId, Integer evalAction) throws BusinessException {
		try {
			return searchAssignmentEvaluation(assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId, wfInstanceId == null ? FlagsEnum.ALL.getCode() : wfInstanceId, evalAction == null ? FlagsEnum.ALL.getCode() : evalAction);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentEvaluation>();
		}
	}

	/**
	 * Search assignment evaluation
	 * 
	 * @param assignmentDetailId
	 * @param wfInstanceId
	 * @param evalAction
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentEvaluation> searchAssignmentEvaluation(long assignmentDetailId, Long wfInstanceId, int evalAction) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			qParams.put("P_INSTANCE_ID", wfInstanceId);
			qParams.put("P_EVAL_ACTION", evalAction);
			return DataAccess.executeNamedQuery(AssignmentEvaluation.class, QueryNamesEnum.ASSIGNMENT_EVALUATION_SEARCH_ASSIGNMENT_EVALUATION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get assignment evaluation
	 * 
	 * @param assignmentEvalId
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentEvaluationPointData> getAssignmentEvaluationPointData(Long assignmentEvalId) throws BusinessException {
		try {
			return searchAssignmentEvaluationPointData(assignmentEvalId == null ? FlagsEnum.ALL.getCode() : assignmentEvalId);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentEvaluationPointData>();
		}
	}

	/**
	 * Search assignment evaluation
	 * 
	 * @param assignmentEvalId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentEvaluationPointData> searchAssignmentEvaluationPointData(long assignmentEvalId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNMENT_EVAL_ID", assignmentEvalId);
			return DataAccess.executeNamedQuery(AssignmentEvaluationPointData.class, QueryNamesEnum.ASSIGNMENT_EVALUATION_POINT_DATA_SEARCH_ASSIGNMENT_EVALUATION_POINT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Latest assignment evaluation
	 * 
	 * @param assignmentEvalId
	 * @param assignmentDetailId
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentEvaluationPointData> getLatestAssignmentEvaluationPointData(Long assignmentEvalId, Long assignmentDetailId) throws BusinessException {
		try {
			return hasAssignmentEvaluationPointData(assignmentEvalId == null ? FlagsEnum.ALL.getCode() : assignmentEvalId, assignmentDetailId == null ? FlagsEnum.ALL.getCode() : assignmentDetailId);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentEvaluationPointData>();
		}
	}

	/**
	 * Check if has assignment evaluation
	 * 
	 * @param assignmentEvalId
	 * @param assignmentDetailId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentEvaluationPointData> hasAssignmentEvaluationPointData(long assignmentEvalId, long assignmentDetailId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ASSIGNMENT_EVALUATION_ID", assignmentEvalId);
			qParams.put("P_ASSIGNMENT_DETAIL_ID", assignmentDetailId);
			return DataAccess.executeNamedQuery(AssignmentEvaluationPointData.class, QueryNamesEnum.ASSIGNMENT_EVALUATION_POINT_DATA_HAS_ASSIGNMENT_EVALUATION_POINT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Assignment Details Data that will end after 2 months
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentDetailDataToSendNotif(Date startDate, Date endDate) throws BusinessException {
		try {
			return searchAssignmentDetailDataToSendNotif(startDate, endDate);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search Assignment Details Data that will end after 2 months
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchAssignmentDetailDataToSendNotif(Date startDate, Date endDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_STATUS", WFInstanceStatusEnum.COMPLETED.getCode());
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_END_DATE_NULL", endDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAILS_DATA_SEARCH_ASSIGNMENT_DETAILS_DATA_TO_SEND_NOTIF.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Assignment Previous Details Data
	 * 
	 * @param employeeId
	 * @param identity
	 * @param startDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getAssignmentPrevDetailData(Long employeeId, String identity, Date startDate) throws BusinessException {
		try {
			return searchAssignmentPrevDetailData(employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, identity, startDate);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search Assignment Previous Details Data
	 * 
	 * @param employeeId
	 * @param identity
	 * @param startDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchAssignmentPrevDetailData(long employeeId, String identity, Date startDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INSTANCE_STATUS", WFInstanceStatusEnum.RUNING.getCode());
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() + "" : identity);
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAILS_DATA_SEARCH_ASSIGNMENT_PREV_DETAILS_DATE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Last assignment Details Data
	 * 
	 * @param officerId
	 * @param agentCode
	 * @param fullName
	 * @param identity
	 * @param startDate
	 * @param approvedEndDate
	 * @param regionId
	 * @param eliminated
	 * @param status
	 * @param heldFlag
	 * @param employeeNonEmployeeFlag
	 * @param assignmentType
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getLastAssignmentDetailsData(Long officerId, String agentCode, String fullName, String identity, Date startDate, Date approvedEndDate, Long regionId, Integer eliminated, Integer status, Integer heldFlag, Integer employeeNonEmployeeFlag, Integer assignmentType, CustomSession... session) throws BusinessException {
		try {
			return searchLastAssignmentDetailsData(officerId == null ? FlagsEnum.ALL.getCode() : officerId, agentCode, fullName, identity, startDate, approvedEndDate, regionId == null ? FlagsEnum.ALL.getCode() : regionId, eliminated == null ? FlagsEnum.ALL.getCode() : eliminated, status == null ? FlagsEnum.ALL.getCode() : status, heldFlag == null ? FlagsEnum.ALL.getCode() : heldFlag, employeeNonEmployeeFlag == null ? FlagsEnum.ALL.getCode() : employeeNonEmployeeFlag,
					assignmentType == null ? FlagsEnum.ALL.getCode() : assignmentType, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Get Last assignment Details Data
	 * 
	 * @param officerId
	 * @param agentCode
	 * @param fullName
	 * @param identity
	 * @param startDate
	 * @param approvedEndDate
	 * @param regionId
	 * @param eliminated
	 * @param status
	 * @param heldFlag
	 * @param employeeNonEmployeeFlag
	 * @param assignmentType
	 * @param unitId
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	public static List<AssignmentDetailData> getLastAssignmentDetailsData(Long officerId, String agentCode, String fullName, String identity, Date startDate, Date approvedEndDate, Long regionId, Integer eliminated, Integer status, Integer heldFlag, Integer employeeNonEmployeeFlag, Integer assignmentType, Long sectorId, Long unitId, CustomSession... session) throws BusinessException {
		try {
			return searchLastAssignmentDetailsData(officerId == null ? FlagsEnum.ALL.getCode() : officerId, agentCode, fullName, identity, startDate, approvedEndDate, regionId == null ? FlagsEnum.ALL.getCode() : regionId, eliminated == null ? FlagsEnum.ALL.getCode() : eliminated, status == null ? FlagsEnum.ALL.getCode() : status, heldFlag == null ? FlagsEnum.ALL.getCode() : heldFlag, employeeNonEmployeeFlag == null ? FlagsEnum.ALL.getCode() : employeeNonEmployeeFlag,
					assignmentType == null ? FlagsEnum.ALL.getCode() : assignmentType, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, unitId == null ? FlagsEnum.ALL.getCode() : unitId);
		} catch (NoDataException e) {
			return new ArrayList<AssignmentDetailData>();
		}
	}

	/**
	 * Search Last Assignment Details Data
	 * 
	 * @param officerId
	 * @param agentCode
	 * @param fullName
	 * @param identity
	 * @param startDate
	 * @param approvedEndDate
	 * @param regionId
	 * @param eliminated
	 * @param status
	 * @param heldFlag
	 * @param employeeNonEmployeeFlag
	 * @param assignmentType
	 * @param session
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<AssignmentDetailData> searchLastAssignmentDetailsData(long officerId, String agentCode, String fullName, String identity, Date startDate, Date approvedEndDate, long regionId, int eliminated, int status, int heldFlag, Integer employeeNonEmployeeFlag, int assignmentType, long sectorId, long unitId, CustomSession... session) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_AGENT_CODE", agentCode == null || agentCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + agentCode + "%");
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_IDENTITY", identity == null || identity.isEmpty() ? FlagsEnum.ALL.getCode() + "" : identity);
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_APPROVED_END_DATE_NULL", approvedEndDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_APPROVED_END_DATE", approvedEndDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(approvedEndDate));
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_UNIT_ID", unitId);
			qParams.put("P_ELIMINATED", eliminated);
			qParams.put("P_STATUS", status);
			qParams.put("P_HELD_FLAG", heldFlag);
			qParams.put("P_IS_EMPLOYEE", employeeNonEmployeeFlag);
			qParams.put("P_TYPE", assignmentType);
			qParams.put("P_CURRENT_DATE", HijriDateService.getHijriSysDateString());
			return DataAccess.executeNamedQuery(AssignmentDetailData.class, QueryNamesEnum.ASSIGNMENT_DETAIL_DATA_SEARCH_LAST_ASSIGNMENT_DETAILS_DATA.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param regionId
	 * @param sectorId
	 * @return
	 * @throws BusinessException
	 */
	public static List<Long> getDepsIdsOfIntelligenceRegionDepartments(Long regionId, Long sectorId) throws BusinessException {
		try {
			return searchDepsIdsOfIntelligenceRegionDepartments(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId);
		} catch (NoDataException e) {
			return new ArrayList<Long>();
		}
	}

	/**
	 * 
	 * @param regionId
	 * @param sectorId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Long> searchDepsIdsOfIntelligenceRegionDepartments(long regionId, long sectorId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.REGION_DEPARTMENTS_SEARCH_REGION_DEPARTMENT_ID.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search AssignmentAgentCode by depId
	 * 
	 * @param departmentId
	 * @return AssignmentAgentCode
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static AssignmentAgentCode searchAssignmentAgentCode(Long departmentId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DEPARTMENT_ID", departmentId);
			return DataAccess.executeNamedQuery(AssignmentAgentCode.class, QueryNamesEnum.ASSIGNMENT_AGENT_CODE_SEARCH_ASSIGNMENT_AGENT_CODE.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return null;
		}
	}
}