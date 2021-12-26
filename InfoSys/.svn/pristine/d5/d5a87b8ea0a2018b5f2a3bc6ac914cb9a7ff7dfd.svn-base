package com.code.services;

import java.util.HashMap;
import java.util.Map;

import com.code.dal.DataAccess;
import com.code.dal.orm.audit.AuditLog;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.log4j.Log4j;

public class AuditService extends BaseService {

	/**
	 * Get audit log by content
	 * 
	 * @param systemUser
	 * @param contentId
	 * @return
	 * @throws BusinessException
	 */
	public static AuditLog getAuditLogByUser(String systemUser, long contentId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SYSTEM_USER", systemUser);
			qParams.put("P_CONTENT_ID", contentId);
			return DataAccess.executeNamedQuery(AuditLog.class, QueryNamesEnum.AUDIT_LOG_GET_LOG_BY_CONTENT.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(AuditLog.class, e, "AuditLog");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return null;
		}
	}
}
