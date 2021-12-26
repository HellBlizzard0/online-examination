package com.code.services.setup;

import java.util.HashMap;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.Image;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class ImageService extends BaseService {
	private ImageService() {
	}

	/**
	 * Update Image
	 * 
	 * @param image
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateImage(Image image, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			DataAccess.updateEntity(image, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ImageService.class, e, "ImageService");
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Insert Image
	 * 
	 * @param image
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void insertImage(Image image, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			DataAccess.addEntity(image, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (DatabaseException e) {
			Log4j.traceErrorException(ImageService.class, e, "ImageService");
			if (!isOpenedSession)
				session.rollbackTransaction();
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Delete Image
	 * 
	 * @param image
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteImage(Image image, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			DataAccess.deleteEntity(image, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			Log4j.traceErrorException(ImageService.class, e, "ImageService");
			if (!isOpenedSession)
				session.rollbackTransaction();
		}
	}

	/**
	 * Get Image By Id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static Image getImageById(long id) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_IMAGE_ID", id);
			return DataAccess.executeNamedQuery(Image.class, QueryNamesEnum.IMAGE_GET_IMAGE.getCode(), qParams).get(0);
		} catch (NoDataException e) {
			return null;
		} catch (DatabaseException e) {
			throw new BusinessException("error_general");
		}
	}
}
