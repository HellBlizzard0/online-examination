package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.RankData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.CategoryEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class SetupService extends BaseService {

	private SetupService() {
	}

	/**
	 * Validate class mandatory fields
	 * 
	 * @param setupClass
	 * @throws BusinessException
	 */
	private static void validateClass(SetupClass setupClass) throws BusinessException {
		if (setupClass.getDescription() == null || setupClass.getDescription().isEmpty() || setupClass.getCode() == null || setupClass.getCode().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Saving class
	 * 
	 * @param setupClass
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveClass(SetupClass setupClass, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClass(setupClass);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setupClass.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(setupClass, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_CodeViolation");
			} else {
				Log4j.traceErrorException(SetupService.class, e, "SetupService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update class
	 * 
	 * @param setupClass
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateClass(SetupClass setupClass, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClass(setupClass);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setupClass.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(setupClass, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_CodeViolation");
			} else {
				Log4j.traceErrorException(SetupService.class, e, "SetupService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate domain mandatory fields
	 * 
	 * @param setupClass
	 * @throws BusinessException
	 */
	private static void validateDomain(SetupDomain setupDomain) throws BusinessException {
		if (setupDomain.getDescription() == null || setupDomain.getDescription().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Saving Domain
	 * 
	 * @param setupDomain
	 * @throws BusinessException
	 */
	public static void saveDomain(SetupDomain setupDomain, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateDomain(setupDomain);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setupDomain.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(setupDomain, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SetupService.class, e, "SetupService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update Domain
	 * 
	 * @param setupDomain
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateDomain(SetupDomain setupDomain, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateDomain(setupDomain);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			setupDomain.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(setupDomain, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SetupService.class, e, "SetupService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/*************************************************/

	/*************** Queries ************************/
	/**
	 * Search for classes by any combination of (code, description, type)
	 * 
	 * @param classCode
	 * @param classDescription
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public static List<SetupClass> getClasses(String classCode, String classDescription, int type) throws BusinessException {
		return searchClasses(classCode, classDescription, type,false);
	}
	
	public static List<SetupClass> getSecurityAnalysisClasses(String classCode, String classDescription, int type) throws BusinessException {
		return searchClasses(classCode, classDescription, type,true);
	}
	/**
	 * Search for classes by any combination of (code, description, type)
	 * 
	 * @param classCode
	 * @param classDescription
	 * @param type
	 * @return a list of SetupClass
	 * @throws BusinessException
	 */
	private static List<SetupClass> searchClasses(String classCode, String classDescription, int type,Boolean securityAnlysisFlag) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CODE", classCode == null || classCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + classCode + "%");
			qParams.put("P_TYPE", type);
			qParams.put("P_DESCRIPTION", classDescription == null || classDescription.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + classDescription + "%");
			qParams.put("P_SECURITY_ANALYSIS_FLAG", securityAnlysisFlag == null ? FlagsEnum.ALL.getCode() : securityAnlysisFlag ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			return DataAccess.executeNamedQuery(SetupClass.class, QueryNamesEnum.SETUP_CLASS_SEARCH_CLASS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SetupService.class, e, "SetupService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<SetupClass>();
		}
	}

	/**
	 * Search for Domains by any combination of (class Id, description, active)
	 * 
	 * @param classId
	 *            id of parent class
	 * @param domainDescription
	 * @param active
	 * @return a list of SetupDomain
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getDomains(long classId, String domainDescription, int active) throws BusinessException {
		try {
			return searchDomains(domainDescription, active, classId, null, null,false);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}
	
	/**
	 * get Security Analysis Domains by classCode 
	 * 
	 * @param classCode
	 * @return
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getSecurityAnalysisDomainsByClassCode(String classCode) throws BusinessException {
		try {
			String[] classCodes = new String[1];
			classCodes[0] = classCode;
			return searchDomains(null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(), classCodes, null,true);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}
	
	/**
	 * get Security Analysis Domains by classCode 
	 * 
	 * @param classCode
	 * @return
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getAllSecurityAnalysisDomainsByClassCode(String classCode) throws BusinessException {
		try {
			String[] classCodes = new String[1];
			classCodes[0] = classCode;
			return searchDomains(null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), classCodes, null,true);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}

	/**
	 * Search for SecurityAnalysisDomains by any combination of (class Id, description, active)
	 * 
	 * @param classId
	 *            id of parent class
	 * @param domainDescription
	 * @param active
	 * @return a list of SetupDomain
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getSecurityAnalysisDomains(long classId, String domainDescription, int active) throws BusinessException {
		try {
			return searchDomains(domainDescription, active, classId, null, null,true);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}
	/**
	 * Search for Domains by any combination of (class Id, description, active)
	 * 
	 * @param domainDescription
	 * @param classCodes
	 * @param classDescription
	 * @return
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getDomains(String domainDescription, String[] classCodes, String classDescription) throws BusinessException {
		try {
			return searchDomains(domainDescription, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(), classCodes, classDescription,false);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}

	/**
	 * Search for Domains by class code
	 * 
	 * @param classCode
	 * @return a list of SetupDomain
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getDomains(String classCode) throws BusinessException {
		try {
			String[] classCodes = new String[1];
			classCodes[0] = classCode;
			return searchDomains(null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(), classCodes, null,false);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}
	
	/**
	 * Search for Domains by class code and Domain Desc
	 * 
	 * @param classCode
	 * @param domianDesc
	 * @return a list of SetupDomain
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getSecurityAnalysisDomainsByDomainDescAndClassCode(String domianDesc, String classCode) throws BusinessException {
		try {
			String[] classCodes = new String[1];
			classCodes[0] = classCode;
			return searchDomains(domianDesc, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(), classCodes, null,true);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}

	/**
	 * Search for Domains by class description
	 * 
	 * @param domainDescription
	 * @param active
	 * @param classId
	 * @param classCode
	 * @param classDescription
	 * @return a list of SetupDomain
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SetupDomain> searchDomains(String domainDescription, long active, long classId, String[] classCodes, String classDescription,Boolean securityAnlysisFlag) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DESCRIPTION", domainDescription == null || domainDescription.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + domainDescription + "%");
			qParams.put("P_ACTIVE", active);
			qParams.put("P_CLASS_CODE_LIST_SIZE", classCodes == null ? 0 : classCodes.length);
			qParams.put("P_CLASS_CODE_LIST", classCodes == null || classCodes.length == 0 ? "" : classCodes);
			qParams.put("P_CLASS_ID", classId);
			qParams.put("P_CLASS_DESCRIPTION", classDescription == null || classDescription.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + classDescription + "%");
			//qParams.put("P_SECURITY_ANALYSIS_FLAG", securityAnlysisFlag == null ? FlagsEnum.ALL.getCode() : securityAnlysisFlag ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			return DataAccess.executeNamedQuery(SetupDomain.class, QueryNamesEnum.SETUP_DOMAIN_SEARCH_DOMAIN.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SetupService.class, e, "SetupService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get info related entities as Domain values
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getInfoRelatedEntities(long infoId) throws BusinessException {
		try {
			return searchInfoRelatedEntities(infoId);
		} catch (NoDataException e) {
			return new ArrayList<SetupDomain>();
		}
	}

	/**
	 * Search info related entities Domain values filtered by infoId
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SetupDomain> searchInfoRelatedEntities(long infoId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(SetupDomain.class, QueryNamesEnum.SETUP_DOMAIN_SEARCH_INFO_RELATED_ENTITIES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SetupDomain.class, e, "SetupDomain");
			throw new BusinessException("error_DBError");
		}
	}
	
	/**
	 * Get rank be categoryId
	 * 
	 * @param categoryId
	 * @return
	 * @throws BusinessException
	 */
	public static List<RankData> getRanksByCategoryId(long categoryId) throws BusinessException {
		try {
			return searchRank((long) FlagsEnum.ALL.getCode(), categoryId);
		} catch (NoDataException e) {
			return new ArrayList<RankData>();
		}
	}
	
	/**
	 * Get rank by rankId
	 * 
	 * @param rankId
	 * @return
	 * @throws BusinessException
	 */
	public static RankData getRankByRankId(long rankId) throws BusinessException {
		try {
			return searchRank(rankId, (long) FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}
	
	
	/**
	 * Search for ranks by rankId and categoryId
	 * 
	 * @param rankId
	 * @param categoryId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<RankData> searchRank(long rankId, long categoryId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_RANK_ID", rankId);
			qParams.put("P_CATEGORY_ID", categoryId != (long) FlagsEnum.ALL.getCode() ? categoryId : new Long[] { CategoryEnum.OFFICER.getCode(), CategoryEnum.SOLIDER.getCode() });
			return DataAccess.executeNamedQuery(RankData.class, QueryNamesEnum.RANK_DATA_SEARCH_RANK_BY_CATEGORY_ID.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(RankData.class, e, "RankData");
			throw new BusinessException("error_DBError");
		}
	}
}