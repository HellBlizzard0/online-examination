package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.DepartmentHierData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.EmployeeNonEmployeeDetail;
import com.code.dal.orm.setup.EmpNonEmpRelative;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class EmployeeService extends BaseService {

	/**
	 * Saving / Updating Employee Details
	 * 
	 * @param employeeDetail
	 * @param employeeRelativesList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveEmployeeDetails(EmployeeNonEmployeeDetail employeeDetail, List<EmpNonEmpRelative> employeeRelativesList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			employeeDetail.setSystemUser(loginEmpData.getEmpId().toString());
			if (employeeDetail.getId() == null) {
				DataAccess.addEntity(employeeDetail, session);
			} else {
				DataAccess.updateEntity(employeeDetail, session);
			}

			for (EmpNonEmpRelative employeeRelative : employeeRelativesList) {
				employeeRelative.setSystemUser(loginEmpData.getEmpId().toString());
				if (employeeRelative.getFullName() == null || employeeRelative.getFullName().trim().isEmpty()) {
					throw new BusinessException("label_relativeNameMandatory");
				}
				if (employeeRelative.getId() == null) {
					DataAccess.addEntity(employeeRelative, session);
				} else {
					DataAccess.updateEntity(employeeRelative, session);
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
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Saving / Updating Employee Details
	 * 
	 * @param employeeDetail
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveUpdateEmployeeDetails(EmployeeNonEmployeeDetail employeeDetail, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			employeeDetail.setSystemUser(loginEmpData.getEmpId().toString());
			if (employeeDetail.getId() == null) {
				DataAccess.addEntity(employeeDetail, session);
			} else {
				DataAccess.updateEntity(employeeDetail, session);
			}
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Insert Employee Relative
	 * 
	 * @param employeeDetail
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void insertEmployeeRelative(EmployeeNonEmployeeDetail employeeDetail, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			employeeDetail.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(employeeDetail, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update Employee Relative
	 * 
	 * @param employeeDetail
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateEmployeeRelative(EmployeeNonEmployeeDetail employeeDetail, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			employeeDetail.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(employeeDetail, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete Employee Relative
	 * 
	 * @param employeeRelative
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteEmployeeRelative(EmpNonEmpRelative employeeRelative, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			employeeRelative.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(employeeRelative, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_descriptionViolation");
			} else {
				Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	// ----------------- Queries ------------//
	/**
	 * Get all employees in the system
	 * 
	 * @return list of employees
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getAllEmployees() throws BusinessException {
		try {
			return searchEmployee(FlagsEnum.ALL.getCode(), null, null, null, null, null, false);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	public static EmployeeData getEmployeeByEmail(String email) throws BusinessException {
		try {
			return searchEmployee(FlagsEnum.ALL.getCode(), email, null, null, null, null, false).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_userNotFound");
		}
	}

	/**
	 * List All employees by full name, social id and military number
	 * 
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @return list of employees
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getEmployee(String fullName, String socialId, String militaryNumber, Long[] actualDepartments) throws BusinessException {
		try {
			return searchEmployee(FlagsEnum.ALL.getCode(), null, fullName, socialId, militaryNumber, actualDepartments, true);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	/**
	 * Get employee by social id
	 * 
	 * @param socialId
	 * @return employee
	 * @throws BusinessException
	 */
	public static EmployeeData getEmployee(String socialId) throws BusinessException {
		try {
			return searchEmployee(FlagsEnum.ALL.getCode(), null, null, socialId, null, null, false).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get employee by id
	 * 
	 * @param id
	 * @return employee
	 * @throws BusinessException
	 */
	public static EmployeeData getEmployee(long id) throws BusinessException {
		try {
			return searchEmployee(id, null, null, null, null, null, false).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Search employees filtered by id, email, full name, social id, military number
	 * 
	 * @param id
	 * @param email
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @return list of employees
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeData> searchEmployee(long id, String email, String fullName, String socialId, String militaryNumber, Long[] actualDepartments, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMAIL", email == null || email.isEmpty() ? FlagsEnum.ALL.getCode() + "" : email);
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_MILITARY_NUMBER", militaryNumber == null || militaryNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : militaryNumber);
			qParams.put("P_ACTUAL_DEP_LIST_SIZE", actualDepartments == null ? FlagsEnum.OFF.getCode() : actualDepartments.length);
			qParams.put("P_ACTUAL_DEP_LIST", actualDepartments == null || actualDepartments.length == 0 ? new Long[] { -1L } : actualDepartments);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Region Manager
	 * 
	 * @param departmentId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getRegionManager(Long departmentId) throws BusinessException {
		Long regionId = DepartmentService.getDepartment(departmentId).getRegionId();
		return DepartmentService.getDepartmentManager(regionId);
	}

	/**
	 * Get Department Manager
	 * 
	 * @param departmentId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getManager(Long departmentId) throws BusinessException {
		DepartmentHierData parent = DepartmentService.getDepartmentHier(departmentId, null);
		Long parentId = parent.getParentId();
		return DepartmentService.getDepartmentManager(parentId);
	}

	/**
	 * Get info related employees
	 * 
	 * @param infoId
	 * @return list of employee data
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getInfoRelatedEmployees(long infoId) throws BusinessException {
		try {
			return searchInfoRelatedEmployees(infoId);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	/**
	 * Search info related employees filtered by infoId
	 * 
	 * @param infoId
	 * @return list of employee data
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeData> searchInfoRelatedEmployees(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_INFO_RELATED_EMPLOYEES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * List All employees by full name, social id and military number, region Id
	 * 
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @param regionId
	 * @return list of employees
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getRegionEmployee(String fullName, String socialId, String militaryNumber, Long regionId) throws BusinessException {
		try {
			return searchRegionEmployee(FlagsEnum.ALL.getCode(), null, fullName, socialId, militaryNumber, regionId == null ? FlagsEnum.ALL.getCode() : regionId, true);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	/**
	 * Search employees filtered by id, email, full name, social id, military number
	 * 
	 * @param id
	 * @param email
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @param regionId
	 * @return list of employees
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeData> searchRegionEmployee(long id, String email, String fullName, String socialId, String militaryNumber, long regionId, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMAIL", email == null || email.isEmpty() ? FlagsEnum.ALL.getCode() + "" : email);
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_MILITARY_NUMBER", militaryNumber == null || militaryNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : militaryNumber);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_EMPLOYEE_BY_REGION_ID.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Employee or NonEmployee Details
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @return list of employee details
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeDetail> getEmpNonEmpDetails(Long empId, Long nonEmpId) throws BusinessException {
		try {
			return searchEmpNonEmpDetails(empId == null ? FlagsEnum.ALL.getCode() : empId, nonEmpId == null ? FlagsEnum.ALL.getCode() : nonEmpId);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeDetail>();
		}
	}

	/**
	 * Search Employee or NonEmployee Details
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @return list of employee details
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeNonEmployeeDetail> searchEmpNonEmpDetails(long empId, long nonEmpId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_NON_EMP_ID", nonEmpId);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeDetail.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_DETAILS_SEARCH_EMPLOYEE_NON_EMPLOYEE_DETAILS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Employee Nonemployee Relatives
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @return list of EmpNonEmpRelative
	 * @throws BusinessException
	 */
	public static List<EmpNonEmpRelative> getEmployeeRelatives(Long empId, Long nonEmpId) throws BusinessException {
		try {
			return searchEmpNonEmpRelatives(empId == null ? FlagsEnum.ALL.getCode() : empId, nonEmpId == null ? FlagsEnum.ALL.getCode() : nonEmpId);
		} catch (NoDataException e) {
			return new ArrayList<EmpNonEmpRelative>();
		}
	}

	/**
	 * Search Employee Nonemployee Relatives
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @return list of EmpNonEmpRelative
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmpNonEmpRelative> searchEmpNonEmpRelatives(long empId, long nonEmpId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_NON_EMP_ID", nonEmpId);
			return DataAccess.executeNamedQuery(EmpNonEmpRelative.class, QueryNamesEnum.EMP_NON_EMP_REALTIVE_SEARCH_EMP_NON_EMP_RELATIVES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * List All employees by full name, social id and military number
	 * 
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @return list of employees
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getEmployeesHavingAssignment(String fullName, String socialId, String militaryNumber, Long[] actualDepartments) throws BusinessException {
		try {
			return searchEmployeesHavingAssignment(FlagsEnum.ALL.getCode(), null, fullName, socialId, militaryNumber, actualDepartments, true);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	/**
	 * Search employees filtered by id, email, full name, social id, military number and having assignment
	 * 
	 * @param id
	 * @param email
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @return list of employees
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeData> searchEmployeesHavingAssignment(long id, String email, String fullName, String socialId, String militaryNumber, Long[] actualDepartments, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMAIL", email == null || email.isEmpty() ? FlagsEnum.ALL.getCode() + "" : email);
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_MILITARY_NUMBER", militaryNumber == null || militaryNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : militaryNumber);
			qParams.put("P_ACTUAL_DEP_LIST_SIZE", actualDepartments == null ? FlagsEnum.OFF.getCode() : actualDepartments.length);
			qParams.put("P_ACTUAL_DEP_LIST", actualDepartments == null || actualDepartments.length == 0 ? new Long[] { -1L } : actualDepartments);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_EMPLOYEE_HAVING_ASSIGNMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * List All employees by full name, social id and military number, region Id
	 * 
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @param regionId
	 * @return list of employees
	 * @throws BusinessException
	 */
	public static List<EmployeeData> getRegionsEmployeeHavingAssignment(String fullName, String socialId, String militaryNumber, Long regionId) throws BusinessException {
		try {
			return searchRegionEmployeesHavingAssignment(FlagsEnum.ALL.getCode(), null, fullName, socialId, militaryNumber, regionId == null ? FlagsEnum.ALL.getCode() : regionId, true);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}

	/**
	 * Search employees filtered by id, email, full name, social id, military number
	 * 
	 * @param id
	 * @param email
	 * @param fullName
	 * @param socialId
	 * @param militaryNumber
	 * @param regionId
	 * @return list of employees
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmployeeData> searchRegionEmployeesHavingAssignment(long id, String email, String fullName, String socialId, String militaryNumber, long regionId, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMAIL", email == null || email.isEmpty() ? FlagsEnum.ALL.getCode() + "" : email);
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_MILITARY_NUMBER", militaryNumber == null || militaryNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : militaryNumber);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_EMPLOYEE_BY_REGION_HAVING_ASSIGNMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * List employees by full name, social id, military number and within employeesIdsList
	 * 
	 * @param searchEmpName
	 * @param searchEmpSocialId
	 * @param searchEmpMilitaryNumber
	 * @param employeesIdsList
	 * @return
	 * @throws BusinessException 
	 */
	public static List<EmployeeData> getEmployeeByIdList(String searchEmpName, String searchEmpSocialId, String searchEmpMilitaryNumber, Long[] employeesIdsList) throws BusinessException {
		try {
			return searchEmployeeByEmployeeIdList(FlagsEnum.ALL.getCode(), null, searchEmpName, searchEmpSocialId, searchEmpMilitaryNumber, employeesIdsList, true);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeData>();
		}
	}
	
	
	private static List<EmployeeData> searchEmployeeByEmployeeIdList(long id, String email, String fullName, String socialId, String militaryNumber, Long[] employeesIds, boolean optimize) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMAIL", email == null || email.isEmpty() ? FlagsEnum.ALL.getCode() + "" : email);
			qParams.put("P_FULL_NAME", fullName == null || fullName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_MILITARY_NUMBER", militaryNumber == null || militaryNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : militaryNumber);
			qParams.put("P_EMPLOYEE_ID_LIST_SIZE", employeesIds == null ? FlagsEnum.OFF.getCode() : employeesIds.length);
			qParams.put("P_EMPLOYEE_ID_LIST", employeesIds == null || employeesIds.length == 0 ? new Long[] { -1L } : employeesIds);
			qParams.put("P_OPTIMIZE", optimize ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			return DataAccess.executeNamedQuery(EmployeeData.class, QueryNamesEnum.EMPLOYEE_DATA_SEARCH_EMPLOYEE_BY_EMPLOYEE_ID_LIST.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeService.class, e, "EmployeeService");
			throw new BusinessException("error_DBError");
		}
	
	}
}