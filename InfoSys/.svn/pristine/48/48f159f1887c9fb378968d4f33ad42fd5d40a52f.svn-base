package com.code.services.infosys.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class InfoTypeService extends BaseService {

	private InfoTypeService() {
	}

	/**
	 * Validate Info Type mandatory fields
	 * 
	 * @param InfoType
	 * @throws BusinessException
	 */
	private static void validateInfoType(InfoType infoType) throws BusinessException {
		infoType.setDescription(infoType.getDescription().trim());
		if (infoType.getDescription() == null || infoType.getDescription().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Save Info Type
	 * 
	 * @param infoType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoType(InfoType infoType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfoType(infoType);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(infoType, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update Info Type
	 * 
	 * @param infoType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateInfoType(InfoType infoType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfoType(infoType);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(infoType, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete Info Type but when only it contains zero subjects
	 * 
	 * @param infoType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoType(InfoType infoType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(infoType, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate info subject mandatory fields
	 * 
	 * @param infoSubject
	 * @throws BusinessException
	 */
	private static void validateInfoSubject(InfoSubject infoSubject) throws BusinessException {
		infoSubject.setDescription(infoSubject.getDescription().trim());
		if (infoSubject.getDescription() == null || infoSubject.getDescription().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Save InfoSubject
	 * 
	 * @param infoSubject
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveInfoSubject(InfoSubject infoSubject, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfoSubject(infoSubject);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoSubject.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(infoSubject, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update InfoSubject
	 * 
	 * @param infoSubject
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateInfoSubject(InfoSubject infoSubject, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateInfoSubject(infoSubject);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoSubject.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(infoSubject, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete InfoSubject and if there are any relation to this entity stop user with error message
	 * 
	 * @param infoSubject
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteInfoSubject(InfoSubject infoSubject, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoSubject.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(infoSubject, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/*************** Queries ************************/
	/**
	 * get classification Types by description
	 * 
	 * @param description
	 * @return a list of ClassificationType
	 * @throws BusinessException
	 */
	public static List<InfoType> getInfoTypes(String description) throws BusinessException {
		try {
			return searchInfoTypes(FlagsEnum.ALL.getCode(), description);
		} catch (NoDataException e) {
			return new ArrayList<InfoType>();
		}
	}

	/**
	 * Get info type by id
	 * 
	 * @param id
	 * @return info type
	 * @throws BusinessException
	 */
	public static InfoType getInfoTypeById(long id) throws BusinessException {
		try {
			return searchInfoTypes(id, null).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param id
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoType> searchInfoTypes(long id, String description) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(InfoType.class, QueryNamesEnum.INFO_TYPE_SEARCH_INFO_TYPES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Info Subjects
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static InfoSubject getsubjectById(long id) throws BusinessException {
		try {
			return searchInfoSubjects(id, FlagsEnum.ALL.getCode(), null).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Info Subjects
	 * 
	 * @param id
	 * @param infoTypeId
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	public static List<InfoSubject> getInfoSubjects(long id, long infoTypeId, String description) throws BusinessException {
		try {
			return searchInfoSubjects(id, infoTypeId, description);
		} catch (NoDataException e) {
			return new ArrayList<InfoSubject>();
		}
	}

	/**
	 * Search all InfoSubjects
	 * 
	 * @param infoTypeId
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	private static List<InfoSubject> searchInfoSubjects(long id, long infoTypeId, String description) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INFO_TYPE_ID", infoTypeId);
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(InfoSubject.class, QueryNamesEnum.INFO_SUBJECT_SEARCH_INFO_SUBJECTS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoTypeService.class, e, "InfoTypeService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoSubject>();
		}
	}
}