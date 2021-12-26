package com.code.services.attach;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.attach.Attachment;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.ui.util.EncryptionUtil;

public class AttachmentService extends BaseService {

	private final static String USER = "AttachmentService";

	private AttachmentService() {
	}

	/**
	 * Add Attachment
	 * 
	 * @param attachment
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addAttachment(Attachment attachment, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			attachment.setSystemUser(USER);
			DataAccess.addEntity(attachment, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			attachment.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(AttachmentService.class, e, "AttachmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * Update Attachment
	 * 
	 * @param attachment
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateAttachment(Attachment attachment, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			attachment.setSystemUser(USER);
			DataAccess.updateEntity(attachment, session);

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
				Log4j.traceErrorException(AttachmentService.class, e, "AttachmentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * deleteAttachment
	 * 
	 * @param attachment
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteAttachment(Attachment attachment, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			attachment.setSystemUser(USER);
			DataAccess.deleteEntity(attachment, session);

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
				Log4j.traceErrorException(AttachmentService.class, e, "AttachmentService");
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
	 * @param fileParamId
	 * @param attachment
	 * @return
	 * @throws Exception
	 */
	public static void deleteFileNetAttachment(Attachment attachment) throws BusinessException {
		try {
			String fileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachment.getAttachmentId());
			URL url = new URL(InfoSysConfigurationService.getBoolServerDeletePath() + fileParamId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpServletResponse.SC_OK) {
				AttachmentService.deleteAttachment(attachment);
			} else {
				throw new BusinessException("error_deletingAttachements");
			}
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}

	}

	/**********************************************/
	/******************* Queries *****************/
	/**
	 * Get Attachments
	 * 
	 * @param entityName
	 * @param entityId
	 * @return Attachment
	 * @throws BusinessException
	 */
	public static Attachment getAttachment(String entityName, long entityId) throws BusinessException {
		try {
			return searchAttachment(entityName, entityId).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Attachments
	 * 
	 * @param entityName
	 * @param entityId
	 * @return Attachment
	 * @throws BusinessException
	 */
	public static List<Attachment> getAttachments(String entityName, long entityId) throws BusinessException {
		try {
			return searchAttachment(entityName, entityId);
		} catch (NoDataException e) {
			return new ArrayList<Attachment>();
		}
	}

	/**
	 * Search Attachment
	 * 
	 * @param entityName
	 * @param entityId
	 * @return list of Attachment
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Attachment> searchAttachment(String entityName, long entityId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ENTITY_ID", entityId);
			qParams.put("P_ENTITY_NAME", entityName == null || entityName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : entityName);
			return DataAccess.executeNamedQuery(Attachment.class, QueryNamesEnum.ATTACHMENT_SEARCH_ATTACHMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AttachmentService.class, e, "AttachmentService");
			throw new BusinessException("error_DBError");
		}
	}
}
