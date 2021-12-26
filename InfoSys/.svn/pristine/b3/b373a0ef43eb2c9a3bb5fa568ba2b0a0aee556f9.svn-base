package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;

public class NonEmployeeService extends BaseService {
	/**
	 * Insert non employee
	 * 
	 * @param loginUser
	 * @param nonEmployee
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void insertNonEmployee(EmployeeData loginUser, NonEmployeeData nonEmployee, CustomSession... useSession) throws BusinessException {
		IsExistNonEmployee(nonEmployee);
		validate(nonEmployee);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			nonEmployee.getNonEmployee().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(nonEmployee.getNonEmployee(), session);

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
				Log4j.traceErrorException(NonEmployeeService.class, e, "NonEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}
	
	/**
	 * Update nonemployee
	 * 
	 * @param loginUser
	 * @param nonEmployee
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateNonEmployee(EmployeeData loginUser, NonEmployeeData nonEmployee, CustomSession... useSession) throws BusinessException {
		validate(nonEmployee);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			nonEmployee.getNonEmployee().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(nonEmployee.getNonEmployee(), session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_identityViolation");
			} else {
				Log4j.traceErrorException(NonEmployeeService.class, e, "NonEmployeeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}


	/**
	 * Check if this non employee is already exists in DB
	 * 
	 * @param nonEmployee
	 * @throws BusinessException
	 */
	public static void IsExistNonEmployee(NonEmployeeData nonEmployee) throws BusinessException {
		if (nonEmployee.getIdentity() != null && !getNonEmployee(nonEmployee.getIdentity(), null).isEmpty()) {
			throw new BusinessException("error_alreadyExist");
		}
	}

	/**
	 * Validate non employee information
	 * 
	 * @param nonEmployee
	 * @throws BusinessException
	 */
	public static void validate(NonEmployeeData nonEmployee) throws BusinessException {
		if ((nonEmployee.getFullName() == null || nonEmployee.getFullName().trim().equals("")) && (nonEmployee.getDescription() == null || nonEmployee.getDescription().equals("") || nonEmployee.getDescription().trim().equals("")) && (nonEmployee.getSponsorDescription() == null || nonEmployee.getSponsorDescription().equals("") || nonEmployee.getSponsorDescription().trim().equals("")) && nonEmployee.getCountryId() == null && nonEmployee.getIdentity() == null) {
			throw new BusinessException("error_oneFieldMandatory");
		}

		else if (nonEmployee.getIdentity() != null && (nonEmployee.getIdentity().toString().length() != 10 || (nonEmployee.getIdentity().toString().length() == 10 && (!nonEmployee.getIdentity().toString().startsWith("1") && !nonEmployee.getIdentity().toString().startsWith("2"))))) {
			throw new BusinessException("error_identityError");
		}

		else if ((nonEmployee.getSponsorDescription() != null && !nonEmployee.getSponsorDescription().equals("") && nonEmployee.getSponsorDescription().matches(".*\\d.*")) && (nonEmployee.getSponsorDescription().length() != 10 || (nonEmployee.getSponsorDescription().length() == 10 && (!nonEmployee.getSponsorDescription().startsWith("1") && !nonEmployee.getSponsorDescription().startsWith("2"))))) {
			throw new BusinessException("error_sponserError");
		}

		else if(nonEmployee.getCountryId() != null && nonEmployee.getCountryId().equals(InfoSysConfigurationService.getKSACountryId()) && nonEmployee.getIdentity() != null && !nonEmployee.getIdentity().toString().startsWith("1")){
			throw new BusinessException("error_nationalIdentityError");
		}
		
		else if(nonEmployee.getCountryId() != null && !nonEmployee.getCountryId().equals(InfoSysConfigurationService.getKSACountryId()) && nonEmployee.getIdentity() != null && !nonEmployee.getIdentity().toString().startsWith("2")){
			throw new BusinessException("error_residentIdentityError");
		}
	}

	/*************************************************/

	/*************** Queries ************************/

	/**
	 * Get Nonemployee by Id
	 * 
	 * @param id
	 * @return NonEmployeeData
	 * @throws BusinessException
	 */
	public static NonEmployeeData getNonEmployeeById(long id) throws BusinessException {
		try {
			return searchNonEmployee(id, FlagsEnum.ALL.getCode(), null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Nonemployee by Id
	 * 
	 * @param identity
	 * @return
	 * @throws BusinessException
	 */
	public static NonEmployeeData getNonEmployeeByIdentity(Long identity) throws BusinessException {
		try {
			return searchNonEmployee(FlagsEnum.ALL.getCode(), identity == null ? FlagsEnum.ALL.getCode() : identity, null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Search non employees filtered by identity, full name number
	 * 
	 * @param identity
	 * @param fullName
	 * @return list of non employees
	 * @throws BusinessException
	 */
	public static List<NonEmployeeData> getNonEmployee(Long identity, String fullName) throws BusinessException {
		try {
			return searchNonEmployee(FlagsEnum.ALL.getCode(), identity == null ? FlagsEnum.ALL.getCode() : identity, fullName);
		} catch (NoDataException e) {
			return new ArrayList<NonEmployeeData>();
		}
	}

	/**
	 * Search non employees filtered by id, identity, full name number
	 * 
	 * @param id
	 * @param identity
	 * @param fullName
	 * @return list of non employees
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<NonEmployeeData> searchNonEmployee(long id, long identity, String fullName) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_IDENTITY", identity);
			qParams.put("P_FULL_NAME", fullName == null || fullName.equals("") ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			return DataAccess.executeNamedQuery(NonEmployeeData.class, QueryNamesEnum.NON_EMPLOYEE_DATA_SEARCH_NON_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(NonEmployeeService.class, e, "NonEmployeeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get info related non employees
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	public static List<NonEmployeeData> getInfoRelatedNonEmployees(long infoId) throws BusinessException {
		try {
			return searchInfoRelatedNonEmployees(infoId);
		} catch (NoDataException e) {
			return new ArrayList<NonEmployeeData>();
		}
	}

	/**
	 * Search info related non employees filtered by infoId
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<NonEmployeeData> searchInfoRelatedNonEmployees(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(NonEmployeeData.class, QueryNamesEnum.NON_EMPLOYEE_SEARCH_INFO_RELATED_NON_EMPLOYEES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(NonEmployeeService.class, e, "NonEmployeeService");
			throw new BusinessException("error_DBError");
		}
	}
}
