package com.code.services.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.info.Classification;
import com.code.dal.orm.info.ClassificationType;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class ClassificationService extends BaseService {

	private ClassificationService() {
	}

	/**
	 * Validate Classification Type mandatory fields
	 * 
	 * @param classificationType
	 * @throws BusinessException
	 */
	private static void validateClassificationType(ClassificationType classificationType) throws BusinessException {
		if (classificationType.getDescription() == null || classificationType.getDescription().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Trim description to check if this description was in DB before
	 */
	private static void manipulateClassificationTypeData(ClassificationType classificationType) {
		classificationType.setDescription(classificationType.getDescription().trim());
	}

	/**
	 * Saving Classification Type
	 * 
	 * @param classificationType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveClassificationType(ClassificationType classificationType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClassificationType(classificationType);
		manipulateClassificationTypeData(classificationType);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			classificationType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(classificationType, session);

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
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update classificationType
	 * 
	 * @param classificationType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateClassificationType(ClassificationType classificationType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClassificationType(classificationType);
		manipulateClassificationTypeData(classificationType);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			classificationType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(classificationType, session);

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
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete classification Type
	 * 
	 * @param classificationType
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteClassificationType(ClassificationType classificationType, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			classificationType.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(classificationType, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else if (e instanceof DatabaseException) {
				throw new BusinessException("error_DBError");
			} else {
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Validate classification mandatory fields
	 * 
	 * @param classification
	 * @throws BusinessException
	 */
	private static void validateClassification(Classification classification) throws BusinessException {
		if (classification.getDescription() == null || classification.getDescription().isEmpty())
			throw new BusinessException("error_mandatory");
	}

	/**
	 * Trim description to check if this description was in DB before
	 */
	private static void manipulateClassificationData(Classification classification) {
		classification.setDescription(classification.getDescription().trim());
	}

	/**
	 * Saving classification
	 * 
	 * @param classification
	 * @throws BusinessException
	 */
	public static void saveClassification(Classification classification, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClassification(classification);
		manipulateClassificationData(classification);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			classification.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(classification, session);

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
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update Classification
	 * 
	 * @param classification
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateClassification(Classification classification, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateClassification(classification);
		manipulateClassificationData(classification);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			classification.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(classification, session);

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
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete classification
	 * 
	 * @param classification
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteClassification(Classification classification, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			classification.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(classification, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else if (e instanceof DatabaseException) {
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
				throw new BusinessException("error_DBError");
			} else {
				Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
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
	 * get classification Types by description
	 * 
	 * @param description
	 * @return a list of ClassificationType
	 * @throws BusinessException
	 */
	public static List<ClassificationType> getClassificationTypes(Long id, String description) throws BusinessException {
		return searchClassificationTypes(id == null ? FlagsEnum.ALL.getCode() : id, description);
	}

	/**
	 * Search for classification Types by description
	 * 
	 * @param description
	 * @return a list of ClassificationType
	 * @throws BusinessException
	 */
	private static List<ClassificationType> searchClassificationTypes(long id, String description) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(ClassificationType.class, QueryNamesEnum.CLASSIFICATION_TYPES_SEARCH_CLASSIFICATION_TYPES.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<ClassificationType>();
		}
	}

	/**
	 * get classifications by id
	 * 
	 * @param id
	 * @return a list of Classifications
	 * @throws BusinessException
	 */
	public static Classification getClassificationById(Long id) throws BusinessException {
		return searchClassifications(FlagsEnum.ALL.getCode(), null, id == null ? FlagsEnum.ALL.getCode() : id).get(0);
	}

	/**
	 * Get classifications by id and description
	 * 
	 * @param typeId
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	public static List<Classification> getClassifications(Long typeId, String description) throws BusinessException {
		return searchClassifications(typeId == null ? FlagsEnum.ALL.getCode() : typeId, description, FlagsEnum.ALL.getCode());
	}

	/**
	 * search classifications by id and description
	 * 
	 * @param typeId
	 * @param description
	 * @return a list of Classifications
	 * @throws BusinessException
	 */
	private static List<Classification> searchClassifications(long typeId, String description, long id) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CLASSIFICATION_TYPE_ID", typeId);
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			qParams.put("P_ID", id);
			return DataAccess.executeNamedQuery(Classification.class, QueryNamesEnum.CLASSIFICATION_SEARCH_CLASSIFICATION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ClassificationService.class, e, "ClassificationService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<Classification>();
		}
	}
}