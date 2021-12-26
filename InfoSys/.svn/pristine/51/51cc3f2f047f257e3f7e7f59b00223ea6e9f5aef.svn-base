package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.info.OpenSource;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class OpenSourceService extends BaseService {
	private OpenSourceService() {
	}

	/**
	 * Validate openSource Type mandatory fields
	 * 
	 * @param openSource
	 * @throws BusinessException
	 */
	private static void validateOpenSource(OpenSource openSource) throws BusinessException {
		if (openSource.getName() == null || openSource.getName().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Saving openSource
	 * 
	 * @param openSource
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveOpenSource(OpenSource openSource, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateOpenSource(openSource);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			openSource.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(openSource, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_nameViolation");
			} else {
				Log4j.traceErrorException(OpenSourceService.class, e, "OpenSourceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update openSource
	 * 
	 * @param openSource
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateOpenSource(OpenSource openSource, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateOpenSource(openSource);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			openSource.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(openSource, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_nameViolation");
			} else {
				Log4j.traceErrorException(OpenSourceService.class, e, "OpenSourceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete open Sources
	 * 
	 * @param openSource
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteOpenSource(OpenSource openSource, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			
			openSource.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(openSource, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(OpenSourceService.class, e, "OpenSourceService");
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
	 * get openSources
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	public static List<OpenSource> getOpenSources(Long id, String name) throws BusinessException {
		return searchOpenSources(id == null ? FlagsEnum.ALL.getCode() : id, name);
	}

	/**
	 * search OpenSource
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	private static List<OpenSource> searchOpenSources(long id, String name) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_NAME", name == null || name.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + name + "%");
			return DataAccess.executeNamedQuery(OpenSource.class, QueryNamesEnum.OPEN_SOURCE_SEARCH_OPEN_SOURCE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(OpenSourceService.class, e, "OpenSourceService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<OpenSource>();
		}
	}

	/**
	 * get openSources given info id
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	public static List<OpenSource> getInfoOpenSources(long infoId) throws BusinessException {
		return searchInfoOpenSources(infoId);
	}

	/**
	 * search OpenSource given info id
	 * 
	 * @param infoId
	 * @return
	 * @throws BusinessException
	 */
	private static List<OpenSource> searchInfoOpenSources(long infoId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_INFO_ID", infoId);
			return DataAccess.executeNamedQuery(OpenSource.class, QueryNamesEnum.OPEN_SOURCE_SEARCH_INFO_OPEN_SOURCE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(OpenSourceService.class, e, "OpenSourceService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<OpenSource>();
		}
	}
}