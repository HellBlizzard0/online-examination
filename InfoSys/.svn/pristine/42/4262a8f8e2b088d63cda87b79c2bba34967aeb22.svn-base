package com.code.services.infosys.securitycheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.securitycheck.SecurityCheck;
import com.code.dal.orm.securitycheck.SecurityCheckData;
import com.code.dal.orm.securitycheck.SecurityCheckEmployeeData;
import com.code.dal.orm.securitycheck.SecurityCheckNonEmployeeData;
import com.code.dal.orm.securitycheck.SecurityCheckPerson;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.RequestSourceEnum;
import com.code.enums.SecurityCheckStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.info.InfoWorkFlow;
import com.code.services.workflow.securitycheck.SecurityCheckEmployeeHQWorkFlow;
import com.code.services.workflow.securitycheck.SecurityCheckEmployeeRegionWorkFlow;
import com.code.services.workflow.securitycheck.SecurityCheckHQWorkFlow;
import com.code.services.workflow.securitycheck.SecurityCheckRegionWorkFlow;

public class SecurityCheckService extends BaseService {

	private SecurityCheckService() {
	}

	/**
	 * Save SecurityCheckService with persons (Employee/nonEmployee) in db after validation
	 * 
	 * @param loginEmpData
	 * @param securityCheckData
	 * @param securityCheckEmployeesList
	 * @param securityCheckEmployeesDeletedList
	 * @param securityCheckNonEmployeesList
	 * @param securityCheckNonEmployeesDeletedList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveUpdateSecurityCheck(EmployeeData loginEmpData, SecurityCheckData securityCheckData, List<SecurityCheckEmployeeData> securityCheckEmployeesList, List<SecurityCheckEmployeeData> securityCheckEmployeesDeletedList, List<SecurityCheckNonEmployeeData> securityCheckNonEmployeesList, List<SecurityCheckNonEmployeeData> securityCheckNonEmployeesDeletedList, CustomSession... useSession) throws BusinessException {
		validateSecurityCheck(securityCheckData, securityCheckEmployeesList, securityCheckNonEmployeesList);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityCheckData.getSecurityCheck().setSystemUser(loginEmpData.getEmpId().toString());
			if (securityCheckData.getSecurityCheck().getId() == null) {
				// generate request number
				securityCheckData.setRequestNumber(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.SECURITY_CHECK.getEntityId(), Integer.MAX_VALUE, session).toString());
				DataAccess.addEntity(securityCheckData.getSecurityCheck(), session);
				securityCheckData.setId(securityCheckData.getSecurityCheck().getId());
			} else {
				DataAccess.updateEntity(securityCheckData.getSecurityCheck(), session);
			}

			deleteSecurityCheckEmployees(loginEmpData, securityCheckEmployeesDeletedList, session);
			addSecurityCheckEmployees(loginEmpData, securityCheckData, securityCheckEmployeesList, session);

			deleteSecurityCheckNonEmployees(loginEmpData, securityCheckNonEmployeesDeletedList, session);
			addSecurityCheckNonEmployees(loginEmpData, securityCheckData, securityCheckNonEmployeesList, session);

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Security Check Employee
	 * 
	 * @param loginUser
	 * @param SecurityCheckEmployeeDeletedList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void deleteSecurityCheckEmployees(EmployeeData loginUser, List<SecurityCheckEmployeeData> SecurityCheckEmployeeDeletedList, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SecurityCheckEmployeeData employeeData : SecurityCheckEmployeeDeletedList) {
				DataAccess.deleteEntity(employeeData.getPerson(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Adding Security Check Employees
	 * 
	 * @param loginEmpData
	 * @param securityCheckData
	 * @param securityCheckEmployeeList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void addSecurityCheckEmployees(EmployeeData loginEmpData, SecurityCheckData securityCheckData, List<SecurityCheckEmployeeData> securityCheckEmployeeList, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SecurityCheckEmployeeData emp : securityCheckEmployeeList) {
				emp.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
				emp.setSecurityCheckId(securityCheckData.getId());
				if (emp.getPerson().getId() == null) {
					DataAccess.addEntity(emp.getPerson(), session);
					emp.setId(emp.getPerson().getId());
				} else {
					DataAccess.updateEntity(emp.getPerson(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Security Check Non Employee
	 * 
	 * @param loginUser
	 * @param SecurityCheckEmployeeDeletedList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void deleteSecurityCheckNonEmployees(EmployeeData loginUser, List<SecurityCheckNonEmployeeData> SecurityCheckEmployeesDeletedList, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SecurityCheckNonEmployeeData employeeData : SecurityCheckEmployeesDeletedList) {
				DataAccess.deleteEntity(employeeData.getPerson(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Adding Security Check Employees
	 * 
	 * @param loginEmpData
	 * @param securityCheckData
	 * @param securityCheckEmployeeList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void addSecurityCheckNonEmployees(EmployeeData loginEmpData, SecurityCheckData securityCheckData, List<SecurityCheckNonEmployeeData> securityCheckNonEmployeeList, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SecurityCheckNonEmployeeData nonEmp : securityCheckNonEmployeeList) {
				nonEmp.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
				nonEmp.setSecurityCheckId(securityCheckData.getId());
				if (nonEmp.getPerson().getId() == null) {
					DataAccess.addEntity(nonEmp.getPerson(), session);
					nonEmp.setId(nonEmp.getPerson().getId());
				} else {
					DataAccess.updateEntity(nonEmp.getPerson(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update security check with validation
	 * 
	 * @param loginEmpData
	 * @param securityCheck
	 * @param employeeDataList
	 * @param nonEmployeeDataList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSecurityCheck(EmployeeData loginEmpData, SecurityCheckData securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, CustomSession... useSession) throws BusinessException {
		validateSecurityCheck(securityCheck, employeeDataList, nonEmployeeDataList);
		updateSecurityCheck(loginEmpData, securityCheck.getSecurityCheck(), useSession);
	}

	/**
	 * Update security check
	 * 
	 * @param loginEmpData
	 * @param securityCheck
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSecurityCheck(EmployeeData loginEmpData, SecurityCheck securityCheck, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityCheck.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(securityCheck, session);

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Security Check with the persons
	 * 
	 * @param loginEmpData
	 * @param securityCheck
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteSecurityCheck(EmployeeData loginEmpData, SecurityCheckData securityCheck, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			deleteSecurityCheckPerson(securityCheck.getId(), session);

			securityCheck.getSecurityCheck().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(securityCheck.getSecurityCheck(), session);

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Save security check processing result
	 * 
	 * @param loginEmpData
	 * @param securityCheck
	 * @param employeeDataList
	 * @param nonEmployeeDataList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveSecurityCheckProcessing(EmployeeData loginEmpData, SecurityCheck securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, CustomSession... useSession) throws BusinessException {
		for (SecurityCheckEmployeeData emp : employeeDataList) {
			validateNote(emp.getPerson());
		}

		for (SecurityCheckNonEmployeeData nonEmp : nonEmployeeDataList) {
			validateNote(nonEmp.getPerson());
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			updateSecurityCheck(loginEmpData, securityCheck, session);

			for (SecurityCheckEmployeeData emp : employeeDataList) {
				emp.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(emp.getPerson(), session);
			}

			for (SecurityCheckNonEmployeeData nonEmp : nonEmployeeDataList) {
				nonEmp.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.updateEntity(nonEmp.getPerson(), session);
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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate Note attributes (if the note is added then all attributes must be entered)
	 * 
	 * @param person
	 * @throws BusinessException
	 */
	public static void validateNote(SecurityCheckPerson person) throws BusinessException {
		if (!(person.getDomainNoteSrcId() == null && (person.getNoteSubject() == null || person.getNoteSubject().trim().isEmpty()) && (person.getNoteDetail() == null || person.getNoteDetail().trim().isEmpty()))) {
			if (person.getDomainNoteSrcId() == null) {
				throw new BusinessException("error_noteSideMandatory");
			}
			if (person.getNoteSubject() == null || person.getNoteSubject().trim().isEmpty()) {
				throw new BusinessException("error_noteSubjectMandatory");
			}
			if (person.getNoteDetail() == null || person.getNoteDetail().trim().isEmpty()) {
				throw new BusinessException("error_noteDetailsMandatory");
			}
		}
	}

	/**
	 * Validate business rules (mandatory fields - dates - at least on person)
	 * 
	 * @param securityCheck
	 * @param employeeDataList
	 * @param nonEmployeeDataList
	 * @throws BusinessException
	 */
	private static void validateSecurityCheck(SecurityCheckData securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList) throws BusinessException {
		if (securityCheck.getRequestSource() == null || securityCheck.getRequestSource().trim().equals("")) {
			throw new BusinessException("error_requestSourceMandatory");
		}

		if (securityCheck.getRequestSource().equals(RequestSourceEnum.OTHERS.getCode()) && securityCheck.getDepartmentOrderSrcId() == null) {
			throw new BusinessException("error_unitMandatory");
		}

		if (securityCheck.getReason() == null || securityCheck.getReason().trim().equals("")) {
			throw new BusinessException("error_securityCheckReasonMandatory");
		}

		if (securityCheck.getIncomingDate() != null && securityCheck.getIncomingDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_incomingDateAfterToday");
		}

		if (employeeDataList.isEmpty() && nonEmployeeDataList.isEmpty()) {
			throw new BusinessException("error_atLeastOnePerson");
		}
	}

	/**
	 * Add Security Check Employee
	 * 
	 * @param loginEmpData
	 * @param securityCheckEmployee
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSecurityCheckEmployee(EmployeeData loginEmpData, SecurityCheckEmployeeData securityCheckEmployee, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityCheckEmployee.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(securityCheckEmployee.getPerson(), session);
			securityCheckEmployee.setId(securityCheckEmployee.getPerson().getId());

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Add Secuity Check Nonemployee
	 * 
	 * @param loginEmpData
	 * @param securityCheckNonemployee
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSecurityCheckNonemployee(EmployeeData loginEmpData, SecurityCheckNonEmployeeData securityCheckNonemployee, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityCheckNonemployee.getPerson().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(securityCheckNonemployee.getPerson(), session);
			securityCheckNonemployee.setId(securityCheckNonemployee.getPerson().getId());

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Security Check Person
	 * 
	 * @param loginEmpData
	 * @param securityCheckPerson
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteSecurityCheckPerson(EmployeeData loginEmpData, SecurityCheckPerson securityCheckPerson, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityCheckPerson.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(securityCheckPerson, session);

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
				Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * Get All security checks registered on person
	 * 
	 * @param personId
	 * @param isEmployee
	 * @return List of SecurityCheckData
	 * @throws BusinessException
	 */
	public static List<SecurityCheckData> getAllSecurityCheck(long personId, boolean isEmployee, Integer status, Date saveDate) throws BusinessException {
		try {
			return searchSecurityCheck(personId, isEmployee, status == null ? FlagsEnum.ALL.getCode() : status, saveDate);
		} catch (NoDataException e) {
			return new ArrayList<SecurityCheckData>();
		}
	}

	/**
	 * 
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param regionId
	 * @param hijriRequestDateFrom
	 * @param hijriRequestDateTo
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param securityCheckReason
	 * @return
	 * @throws BusinessException
	 */
	public static List<SecurityCheckData> getSecurityCheckEmployee(long departmentSourceId, long domainIncomingSourceId, long regionId, Date hijriRequestDateFrom, Date hijriRequestDateTo, Long employeeId, int securityCheckReason, String requestSource) throws BusinessException {
		try {
			return searchSecurityCheckEmployee(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), departmentSourceId, domainIncomingSourceId, regionId, hijriRequestDateFrom, hijriRequestDateTo, false, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, securityCheckReason, requestSource);
		} catch (NoDataException e) {
			return new ArrayList<SecurityCheckData>();
		}
	}

	/**
	 * 
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param regionId
	 * @param hijriRequestDateFrom
	 * @param hijriRequestDateTo
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param securityCheckReason
	 * @return
	 * @throws BusinessException
	 */
	public static List<SecurityCheckData> getSecurityCheckNonEmployee(long departmentSourceId, long domainIncomingSourceId, long regionId, Date hijriRequestDateFrom, Date hijriRequestDateTo, Long nonEmployeeId, int securityCheckReason, String requestSource) throws BusinessException {
		try {
			return searchSecurityCheckNonEmployee(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), departmentSourceId, domainIncomingSourceId, regionId, hijriRequestDateFrom, hijriRequestDateTo, false, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, securityCheckReason, requestSource);
		} catch (NoDataException e) {
			return new ArrayList<SecurityCheckData>();
		}
	}

	/**
	 * Get Security check by Id or wfInstanceId
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @return SecurityCheckData
	 * @throws BusinessException
	 */
	public static SecurityCheckData getSecurityCheckById(long id, long wfInstanceId) throws BusinessException {
		try {
			return searchSecurityCheckById(id, wfInstanceId).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Search all Security Checks
	 * 
	 * @param personId
	 * @param isEmployee
	 * @return list of security checks
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckData> searchSecurityCheck(long personId, boolean isEmployee, int status, Date saveDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			if (isEmployee) {
				qParams.put("P_EMPLOYEE_ID", personId);
				qParams.put("P_NON_EMPLOYEE_ID", FlagsEnum.ALL.getCode());
			} else {
				qParams.put("P_EMPLOYEE_ID", FlagsEnum.ALL.getCode());
				qParams.put("P_NON_EMPLOYEE_ID", personId);
			}
			qParams.put("P_NOT_REJECTED", FlagsEnum.ON.getCode());
			qParams.put("P_STATUS", status);
			qParams.put("P_SAVE_DATE_NULL", saveDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_SAVE_DATE", saveDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(saveDate));

			return DataAccess.executeNamedQuery(SecurityCheckData.class, QueryNamesEnum.SECURITY_CHECK_DATA_SEARCH_SECURITY_CHECK_BY_PERSON.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckData> searchSecurityCheckById(long id, long wfInstanceId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_WF_INSTANCE_ID", wfInstanceId);
			return DataAccess.executeNamedQuery(SecurityCheckData.class, QueryNamesEnum.SECURITY_CHECK_DATA_SEARCH_SECURITY_CHECK_BY_ID.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param regionId
	 * @param hijriRequestDateFrom
	 * @param hijriRequestDateTo
	 * @param notRejected
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param checkReason
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckData> searchSecurityCheckEmployee(long id, long wfInstanceId, long departmentSourceId, long domainIncomingSourceId, long regionId, Date hijriRequestDateFrom, Date hijriRequestDateTo, boolean notRejected, long employeeId, int checkReason, String requestSource) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NOT_REJECTED", notRejected ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			qParams.put("P_WF_INSTANCE_ID", wfInstanceId);
			qParams.put("P_DPET_ORDER_SRC_ID", departmentSourceId);
			qParams.put("P_DOMAIN_INCOMING_SRC_ID", domainIncomingSourceId);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_START_DATE_NULL", hijriRequestDateFrom == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", hijriRequestDateFrom == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(hijriRequestDateFrom));
			qParams.put("P_END_DATE_NULL", hijriRequestDateTo == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", hijriRequestDateTo == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(hijriRequestDateTo));
			qParams.put("P_REQUEST_NUMBER", requestSource == null || requestSource.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : requestSource);
			return DataAccess.executeNamedQuery(SecurityCheckData.class, QueryNamesEnum.SECURITY_CHECK_DATA_SEARCH_SECURITY_CHECK_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param id
	 * @param wfInstanceId
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param regionId
	 * @param hijriRequestDateFrom
	 * @param hijriRequestDateTo
	 * @param notRejected
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param checkReason
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckData> searchSecurityCheckNonEmployee(long id, long wfInstanceId, long departmentSourceId, long domainIncomingSourceId, long regionId, Date hijriRequestDateFrom, Date hijriRequestDateTo, boolean notRejected, long nonEmployeeId, int checkReason, String requestSource) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			qParams.put("P_NOT_REJECTED", notRejected ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			qParams.put("P_WF_INSTANCE_ID", wfInstanceId);
			qParams.put("P_DPET_ORDER_SRC_ID", departmentSourceId);
			qParams.put("P_DOMAIN_INCOMING_SRC_ID", domainIncomingSourceId);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_START_DATE_NULL", hijriRequestDateFrom == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", hijriRequestDateFrom == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(hijriRequestDateFrom));
			qParams.put("P_END_DATE_NULL", hijriRequestDateTo == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", hijriRequestDateTo == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(hijriRequestDateTo));
			qParams.put("P_REQUEST_NUMBER", requestSource == null || requestSource.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : requestSource);
			return DataAccess.executeNamedQuery(SecurityCheckData.class, QueryNamesEnum.SECURITY_CHECK_DATA_SEARCH_SECURITY_CHECK_NON_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all SecurityCheckEmployee by securityCheckId
	 * 
	 * @param securityCheckId
	 * @return List of SecurityCheckEmployeeData
	 * @throws BusinessException
	 */
	public static List<SecurityCheckEmployeeData> getSecurityCheckEmployees(long securityCheckId) throws BusinessException {
		try {
			return searchSecurityCheckEmployee(FlagsEnum.ALL.getCode(), securityCheckId);
		} catch (NoDataException e) {
			return new ArrayList<SecurityCheckEmployeeData>();
		}
	}

	/**
	 * Get all SecurityCheckNonEmployee by securityCheckId
	 * 
	 * @param securityCheckId
	 * @return List of SecurityCheckNonEmployeeData
	 * @throws BusinessException
	 */
	public static List<SecurityCheckNonEmployeeData> getSecurityCheckNonEmployees(long securityCheckId) throws BusinessException {
		try {
			return searchSecurityCheckNonEmployee(FlagsEnum.ALL.getCode(), securityCheckId);
		} catch (NoDataException e) {
			return new ArrayList<SecurityCheckNonEmployeeData>();
		}
	}

	/**
	 * Search all SecurityCheckEmployee
	 * 
	 * @param id
	 * @param securityCheckId
	 * @return list of SecurityCheckEmployeeData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckEmployeeData> searchSecurityCheckEmployee(long id, long securityCheckId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_SECURITY_CHECK_ID", securityCheckId);
			return DataAccess.executeNamedQuery(SecurityCheckEmployeeData.class, QueryNamesEnum.SECURITY_CHECK_EMPLOYEE_DATA_SEARCH_SECURITY_CHECK_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search all SecurityCheckNonEmployee
	 * 
	 * @param id
	 * @param securityCheckId
	 * @return list of SecurityCheckNonEmployeeData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityCheckNonEmployeeData> searchSecurityCheckNonEmployee(long id, long securityCheckId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_SECURITY_CHECK_ID", securityCheckId);
			return DataAccess.executeNamedQuery(SecurityCheckNonEmployeeData.class, QueryNamesEnum.SECURITY_CHECK_NON_EMPLOYEE_DATA_SEARCH_SECURITY_CHECK_NON_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Delete security check person
	 * 
	 * @param securityCheckId
	 * @param useSession
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static void deleteSecurityCheckPerson(long securityCheckId, CustomSession useSession) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SECURITY_CHECK_ID", securityCheckId);
			DataAccess.updateDeleteNamedQuery(SecurityCheckPerson.class, QueryNamesEnum.SECURITY_CHECK_PERSON_DELETE_SECURITY_CHECK_PERSON.getCode(), qParams, useSession);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Count Approved Security Checks for an employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	public static long countApprovedSecurityChecks(long employeeId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_STATUS", SecurityCheckStatusEnum.APPROVED.getCode());
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.SECURITY_CHECK_DATA_COUNT_SECURITY_CHECK.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Get Security Check Report
	 * 
	 * @param securityCheckId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityCheckReportBytes(long securityCheckId, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SECURITY_CHECK_ID", securityCheckId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.SECURITY_CHECK_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getEmployeeDetailsBytes
	 * 
	 * @param employeeId
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeDetailsBytes(long employeeId, EmployeeData loginEmpData) throws BusinessException {
		try {
			boolean isAuthDep = InfoWorkFlow.isIntelleigenceOrAnalysisDepartment(loginEmpData.getActualDepartmentId());
			List<InfoData> employeeRelatedInfoList = new ArrayList<InfoData>();
			if (isAuthDep) {
				employeeRelatedInfoList = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeId, FlagsEnum.ALL.getCode(), null, loginEmpData);
			} else {
				employeeRelatedInfoList = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), employeeId, FlagsEnum.ALL.getCode(), null, loginEmpData);
			}
			List<Long> infoIdsList = new ArrayList<Long>();
			for (InfoData info : employeeRelatedInfoList) {
				infoIdsList.add(info.getId());
			}
			DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_EMPLOYEE_ID", employeeId);
			parameters.put("P_REGION_ID", loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()) ? FlagsEnum.ALL.getCode() : loginDep.getRegionId());
			parameters.put("P_INFO_LIST", infoIdsList);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmpData.getFullName());
			String reportName = ReportNamesEnum.EMPLOYEE_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getNonEmployeeDetailsBytes
	 * 
	 * @param nonEmployeeId
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNonEmployeeDetailsBytes(long nonEmployeeId, EmployeeData loginEmpData) throws BusinessException {
		try {
			boolean isAuthDep = InfoWorkFlow.isIntelleigenceOrAnalysisDepartment(loginEmpData.getActualDepartmentId());
			List<InfoData> employeeRelatedInfoList = new ArrayList<InfoData>();
			if (isAuthDep) {
				employeeRelatedInfoList = InfoService.getInfoDataRelatedEntity(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), nonEmployeeId, null, loginEmpData);
			} else {
				employeeRelatedInfoList = InfoService.getInfoDataRelatedEntityByAnalysis(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), nonEmployeeId, null, loginEmpData);
			}
			List<Long> infoIdsList = new ArrayList<Long>();
			for (InfoData info : employeeRelatedInfoList) {
				infoIdsList.add(info.getId());
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			parameters.put("P_INFO_LIST", infoIdsList);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmpData.getFullName());
			String reportName = ReportNamesEnum.NON_EMPLOYEE_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Check Reply Letter Report
	 * 
	 * @param securityCheckId
	 * @param departmentName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityCheckReplyLetterBytes(long securityCheckId, String departmentName, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DEPARTMENT_NAME", departmentName);
			parameters.put("P_SECURITY_CHECK_ID", securityCheckId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.SECURITY_CHECK_REPLY_LETTER.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security check Bytes
	 * 
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param discriminator
	 * @param notesExists
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityCheckBytes(Long departmentSourceId, Long domainIncomingSourceId, int discriminator, int notesExists, Date fromDate, Date toDate, long regionId, String regionName, String loginEmployeeName, Integer checkReason) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DEPARTMENT_SOURCE_ID", departmentSourceId == null ? FlagsEnum.ALL.getCode() : departmentSourceId);
			parameters.put("P_DOMAIN_INCOMING_SOURCE_ID", domainIncomingSourceId == null ? FlagsEnum.ALL.getCode() : domainIncomingSourceId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			parameters.put("P_NOTES_EXIST", notesExists);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_NOTES_EXIST", notesExists);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_CHECK_REASON", checkReason == null ? FlagsEnum.ALL.getCode() : checkReason);
			parameters.put("P_REGION_NAME", regionName);
			if (discriminator == 1) {
				reportName = ReportNamesEnum.SECURITY_CHECK_EMP.getCode();
			} else if (discriminator == 2) {
				reportName = ReportNamesEnum.SECURITY_CHECK_NON_EMP.getCode();
			}
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security check statistical Bytes
	 * 
	 * @param departmentSourceId
	 * @param domainIncomingSourceId
	 * @param discriminator
	 * @param notesExists
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSecurityCheckStatisticalBytes(Long departmentSourceId, Long domainIncomingSourceId, int discriminator, int notesExists, Date fromDate, Date toDate, String loginEmployeeName, Integer checkReason) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DEPARTMENT_SOURCE_ID", departmentSourceId == null ? FlagsEnum.ALL.getCode() : departmentSourceId);
			parameters.put("P_DOMAIN_INCOMING_SOURCE_ID", domainIncomingSourceId == null ? FlagsEnum.ALL.getCode() : domainIncomingSourceId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			parameters.put("P_NOTES_EXIST", notesExists);
			parameters.put("P_CHECK_REASON", checkReason == null ? FlagsEnum.ALL.getCode() : checkReason);
			parameters.put("P_DISCRIMINATOR", discriminator);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			if (discriminator == 1) {
				reportName = ReportNamesEnum.SECURITY_CHECK_STATISTICAL_EMP.getCode();
			} else if (discriminator == 2) {
				reportName = ReportNamesEnum.SECURITY_CHECK_STATISTICAL_NON_EMP.getCode();
			}
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityCheckService.class, e, "SecurityCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Check notes of employees and update it
	 * 
	 * @param securityCheckEmployeesList
	 * @throws BusinessException
	 */
	public static void countEmployeesNotes(List<SecurityCheckEmployeeData> securityCheckEmployeesList) throws BusinessException {
		for (int i = 0; i < securityCheckEmployeesList.size(); i++) {
			if (countApprovedSecurityChecks(securityCheckEmployeesList.get(i).getEmployeeId()) > 0 || SurveillanceOrdersService.getCountSurveillanceEmployeeData(securityCheckEmployeesList.get(i).getEmployeeId()) > 0 || LabCheckService.getCountLabCheckEmployeesByEmployeeId(securityCheckEmployeesList.get(i).getEmployeeId()) > 0 || InfoService.getCountInfoRelatedEntity(securityCheckEmployeesList.get(i).getEmployeeId()) > 0) {
				securityCheckEmployeesList.get(i).setNotesExist(true);
				continue;
			}
		}

	}

	public static void initSecurityCheckRegion(SecurityCheckData securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckEmployeeData> employeeDataDeletedList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataDeletedList, EmployeeData loginUser, String attachments, WFTask dmTask) throws BusinessException {
		if (nonEmployeeDataList.size() != 0) {
			SecurityCheckRegionWorkFlow.initSecurityCheck(securityCheck, employeeDataList, employeeDataDeletedList, nonEmployeeDataList, nonEmployeeDataDeletedList, loginUser, attachments, dmTask);
		} else if (employeeDataList.size() != 0) {
			SecurityCheckEmployeeRegionWorkFlow.initSecurityCheck(securityCheck, employeeDataList, employeeDataDeletedList, nonEmployeeDataList, nonEmployeeDataDeletedList, loginUser, attachments, dmTask);

		}
	}

	public static void initSecurityCheckHQ(SecurityCheckData securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckEmployeeData> employeeDataDeletedList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataDeletedList, EmployeeData loginUser, String attachments, WFTask dmTask) throws BusinessException {
		if (nonEmployeeDataList.size() != 0) {
			SecurityCheckHQWorkFlow.initSecurityCheck(securityCheck, employeeDataList, employeeDataDeletedList, nonEmployeeDataList, nonEmployeeDataDeletedList, loginUser, attachments, dmTask);
		} else if (employeeDataList.size() != 0) {
			SecurityCheckEmployeeHQWorkFlow.initSecurityCheck(securityCheck, employeeDataList, employeeDataDeletedList, nonEmployeeDataList, nonEmployeeDataDeletedList, loginUser, attachments, dmTask);

		}

	}

	public static void doApproveRegion(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		if (!isEmployee) {
			SecurityCheckRegionWorkFlow.doApprove(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			SecurityCheckEmployeeRegionWorkFlow.doApprove(dmTask, action, securityCheck, loginUser, attachments);

		}
	}

	public static void doRejectRegion(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		if (!isEmployee) {
			SecurityCheckRegionWorkFlow.doReject(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			SecurityCheckEmployeeRegionWorkFlow.doReject(dmTask, action, securityCheck, loginUser, attachments);

		}
	}

	public static void doApproveRejectDM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectDM(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (!isEmployee) {
				SecurityCheckHQWorkFlow.doApproveRejectDM(dmTask, action, securityCheck, loginUser, attachments);
			} else {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectDM(dmTask, action, securityCheck, loginUser, attachments);

			}
		}
	}

	public static void doApproveRejectSM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectSM(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (!isEmployee) {
				SecurityCheckHQWorkFlow.doApproveRejectSM(dmTask, action, securityCheck, loginUser, attachments);
			} else {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectSM(dmTask, action, securityCheck, loginUser, attachments);

			}
		}
	}

	public static void doApproveRejectProcessingDM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectProcessingDM(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (!isEmployee) {
				SecurityCheckHQWorkFlow.doApproveRejectProcessingDM(dmTask, action, securityCheck, loginUser, attachments);
			} else {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectProcessingDM(dmTask, action, securityCheck, loginUser, attachments);

			}
		}
	}

	public static void doApproveRejectProcessingSM(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectProcessingSM(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (!isEmployee) {
				SecurityCheckHQWorkFlow.doApproveRejectProcessingSM(dmTask, action, securityCheck, loginUser, attachments);
			} else {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectProcessingSM(dmTask, action, securityCheck, loginUser, attachments);

			}
		}
	}

	public static void doApproveRejectRegionSecurity(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectRegionSecurity(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (isEmployee) {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectRegionSecurity(dmTask, action, securityCheck, loginUser, attachments);
			}
		}
	}

	public static void doApproveRejectProcessingRegionSecurityManager(WFTask dmTask, WFTaskActionsEnum action, SecurityCheck securityCheck, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doApproveRejectProcessingRegionSecurityManager(dmTask, action, securityCheck, loginUser, attachments);
		} else {
			if (isEmployee) {
				SecurityCheckEmployeeHQWorkFlow.doApproveRejectProcessingRegionSecurityManager(dmTask, action, securityCheck, loginUser, attachments);
			}
		}
	}

	public static void doProcessingRegion(WFTask dmTask, SecurityCheck securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		if (!isEmployee) {
			SecurityCheckRegionWorkFlow.doProcessing(dmTask, securityCheck, employeeDataList, nonEmployeeDataList, loginUser, attachments);
		} else {
			SecurityCheckEmployeeRegionWorkFlow.doProcessing(dmTask, securityCheck, employeeDataList, nonEmployeeDataList, loginUser, attachments);

		}

	}

	public static void doProcessingHQ(WFTask dmTask, SecurityCheck securityCheck, List<SecurityCheckEmployeeData> employeeDataList, List<SecurityCheckNonEmployeeData> nonEmployeeDataList, EmployeeData loginUser, String attachments, boolean isEmployee) throws BusinessException {
		if (!isEmployee) {
			SecurityCheckHQWorkFlow.doProcessing(dmTask, securityCheck, employeeDataList, nonEmployeeDataList, loginUser, attachments);
		} else {
			SecurityCheckEmployeeHQWorkFlow.doProcessing(dmTask, securityCheck, employeeDataList, nonEmployeeDataList, loginUser, attachments);

		}

	}

	public static void doNotify(WFTask currentTask, EmployeeData loginUser, boolean isEmployee) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
		if (regionId != null) {
			SecurityCheckRegionWorkFlow.doNotify(currentTask);
		} else {
			if (!isEmployee) {
				SecurityCheckHQWorkFlow.doNotify(currentTask);
			} else {
				SecurityCheckEmployeeHQWorkFlow.doNotify(currentTask);

			}
		}
	}
}