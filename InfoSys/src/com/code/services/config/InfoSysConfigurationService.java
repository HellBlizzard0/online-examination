package com.code.services.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.ElmIntegConfigData;
import com.code.dal.orm.setup.InfoSysConfig;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;

public class InfoSysConfigurationService extends BaseService {
	private static Map<String, String> configurationMap;
	private static Map<String, String> elmConfigMap;

	/**
	 * Initialize configuration parameters from database
	 */
	public static void init() {
		try {
			configurationMap = new HashMap<String, String>();
			List<InfoSysConfig> configurationList = searchConfiguration(null, null, null);
			for (InfoSysConfig config : configurationList) {
				configurationMap.put(config.getCode(), config.getValue());
			}
			elmConfigMap = new HashMap<String, String>();
			List<ElmIntegConfigData> elmIntegConfigDataList = searchElmIntegConfiguration(null, null, null);
			for (ElmIntegConfigData config : elmIntegConfigDataList) {
				elmConfigMap.put(config.getCode(), config.getValue());
			}
		} catch (Exception e) {
			Log4j.traceErrorException(InfoSysConfigurationService.class, e, "InfoSysConfigurationService");
		}
	}

	/**
	 * 
	 * @param code
	 * @param value
	 * @param comment
	 * @return configuration
	 * @throws BusinessException
	 */
	public static List<InfoSysConfig> getConfiguration(String code, String value, String comment) throws BusinessException {
		return searchConfiguration(code, value, comment);
	}

	/**
	 * Search configuration filtered by code, value and comment
	 * 
	 * @param code
	 * @param value
	 * @param comment
	 * @return configuration
	 * @throws BusinessException
	 */
	private static List<InfoSysConfig> searchConfiguration(String code, String value, String comment) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CODE", (code == null || code.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : code);
			qParams.put("P_VALUE", (value == null || value.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : value);
			qParams.put("P_COMMENT", (comment == null || comment.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : "%" + comment + "%");
			return DataAccess.executeNamedQuery(InfoSysConfig.class, QueryNamesEnum.INFO_SYS_CONFIG_GET_CONFIGURATIONS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<InfoSysConfig>();
		}
	}

	/**
	 * Insert new configuration
	 * 
	 * @param infoSysConfig
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void insertConfiguration(InfoSysConfig infoSysConfig, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			DataAccess.addEntity(infoSysConfig, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoSysConfigurationService.class, e, "InfoSysConfigurationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update current configuration
	 * 
	 * @param infoSysConfig
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateConfiguration(InfoSysConfig infoSysConfig, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(infoSysConfig, session);
			InfoSysConfigurationService.setByCode(infoSysConfig.getCode(), infoSysConfig.getValue());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoSysConfigurationService.class, e, "InfoSysConfigurationService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * 
	 * @param code
	 * @param value
	 * @param comment
	 * @return configuration
	 * @throws BusinessException
	 */
	public static List<ElmIntegConfigData> getElmIntegConfiguration(String code, String value, String comment) throws BusinessException {
		return searchElmIntegConfiguration(code, value, comment);
	}

	/**
	 * Search configuration filtered by code, value and comment
	 * 
	 * @param code
	 * @param value
	 * @param comment
	 * @return configuration
	 * @throws BusinessException
	 */
	private static List<ElmIntegConfigData> searchElmIntegConfiguration(String code, String value, String comment) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CODE", (code == null || code.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : code);
			qParams.put("P_VALUE", (value == null || value.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : value);
			qParams.put("P_COMMENT", (comment == null || comment.isEmpty()) ? FlagsEnum.ALL.getCode() + "" : "%" + comment + "%");
			return DataAccess.executeNamedQuery(ElmIntegConfigData.class, QueryNamesEnum.ELM_INTEG_CONFIG_DATA_GET_ELM_CONFIGURATIONS.getCode(), qParams);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<ElmIntegConfigData>();
		}
	}

	public static String getLDAPConnectionType() {
		return configurationMap.get("LDAP_CONNECTION_TYPE");
	}

	public static String getLDAPDomain() {
		return configurationMap.get("LDAP_DOMAIN");
	}

	public static String getLDAPIP() {
		return configurationMap.get("LDAP_IP");
	}

	public static String getLDAPPort() {
		return configurationMap.get("LDAP_PORT");
	}

	public static String getLDAPBase() {
		return configurationMap.get("LDAP_BASE");
	}

	public static String getLDAPAdminPassword() {
		return configurationMap.get("LDAP_ADMIN_PASSWORD");
	}

	public static String getLDAPAdminUsername() {
		return configurationMap.get("LDAP_ADMIN_USER");
	}

	public static String getLDAPIdentityAttribute() {
		return configurationMap.get("LDAP_IDENTITY_ATTRIBUTE");
	}

	public static String getLDAPTelephoneNumberAttribute() {
		return configurationMap.get("TELEPHONE_NUMBER");
	}

	public static String getLDAPManagerAttribute() {
		return configurationMap.get("MANAGER");
	}

	public static String getLDAPMobileAttribute() {
		return configurationMap.get("MOBILE");
	}

	public static String getLDAPMailAttribute() {
		return configurationMap.get("MAIL");
	}

	public static String getLDAPPhysicalDeliveryOfficeAttribute() {
		return configurationMap.get("PHYSICAL_DELIVERY_OFFICE");
	}

	public static String getReportsRoot() {
		return configurationMap.get("REPORTS_ROOT");
	}

	public static String getSmsCarrierLink() {
		return configurationMap.get("SMS_CARRIER_LINK");
	}

	public static String getApplicationUri() {
		return configurationMap.get("APP_URI");
	}

	public static String getSmsUrl() {
		return configurationMap.get("SMS_URL");
	}

	public static String getSurveillanceNotifyLateReportAhead() {
		return configurationMap.get("NOTIFY_SURV_LATE_BEFORE_DAYS");
	}

	public static String getSmsUsername() {
		return configurationMap.get("SMS_USERNAME");
	}

	public static String getSmsPassword() {
		return configurationMap.get("SMS_PASSWORD");
	}

	public static String getSmsSenderName() {
		return configurationMap.get("SMS_SENDER_NAME");
	}

	public static String getAdminMails() {
		return configurationMap.get("ADMIN_MAILS");
	}

	public static String getSystemAdmin() {
		return configurationMap.get("SYSTEM_ADMIN");
	}

	public static String getBoolServerUploadPath() {
		return configurationMap.get("BOOL_SERVER_UPLOAD_PATH");
	}

	public static String getBoolServerDownloadPath() {
		return configurationMap.get("BOOL_SERVER_DOWNLOAD_PATH");
	}

	public static String getBoolServerDeletePath() {
		return configurationMap.get("BOOL_SERVER_DELETE_PATH");
	}

	public static String getMITokenPath() {
		return configurationMap.get("MI_TOKEN_PATH");
	}

	public static String getJMSServerURL() {
		return configurationMap.get("JMS_SERVER_URL");
	}

	public static Integer getHCMDrugRequestWindow() {
		return Integer.valueOf(configurationMap.get("HCM_DRUG_REQUEST_WINDOW"));
	}

	public static String getHCMSoapUrl() {
		return configurationMap.get("HCM_SOAP_URL");
	}
	
	public static String getElmEjadaInternalWsdlUrl() {
		return elmConfigMap.get("YAQEEN_EJADA_INTERNAL_WSDL_URL");
	}

	public static Long getHeadQuarter() {
		return Long.valueOf(configurationMap.get("HEAD_QUARTER"));
	}
	
	public static Long getKSACountryId() {
		return Long.valueOf(configurationMap.get("KSA_COUNTRY"));
	}
	
	public static Integer getLabCheckEditPeriodId() {
		return Integer.valueOf(configurationMap.get("LAB_CHECK_EDIT_PERIOD"));
	}
	
	public static Integer getMinLongKSADegree() {
		return Integer.valueOf(configurationMap.get("MIN_LONGITUDE_KSA_DEGREE"));
	}
	
	public static Integer getMaxLongKSADegree() {
		return Integer.valueOf(configurationMap.get("MAX_LONGITUDE_KSA_DEGREE"));
	}
	
	public static Integer getMinLatKSADegree() {
		return Integer.valueOf(configurationMap.get("MIN_LATITUDE_KSA_DEGREE"));
	}
	
	public static Integer getMaxLatKSADegree() {
		return Integer.valueOf(configurationMap.get("MAX_LATITUDE_KSA_DEGREE"));
	}
	
	public static int getConvReminderPeriod() {
		return Integer.valueOf(configurationMap.get("CONVERSATION_REMINDER_PERIOD_IN_MIN"));
	}
	
	public static String getDelayedLabChecksReminder() {
		return configurationMap.get("DELAYED_LAB_CHECKS_REMINDER_PERIOD_IN_WEEK");
	}
	
	public static int getPostiveChecks() {
		return Integer.valueOf(configurationMap.get("PREV_POSTIVE_CHECKS"));
	}
	
	public static int getLabCheckWithinPeriod() {
		return Integer.valueOf(configurationMap.get("LAB_CHECK_WITHIN_PERIOD"));
	}
	
	public static String getCasesRestWebServiceURL() {
		return configurationMap.get("CASES_REST_WEB_SERVICE_URL");
	}
	
	public static int getMinCaseDescriptionLength() {
		return Integer.valueOf(configurationMap.get("MAX_CASE_DESC_LENGTH_BEFORE_SHOW_MORE"));
	}
	
	public static void setByCode(String code, String value) {
		configurationMap.put(code, value);
	}
	public static String getHibernateDefaultSchema() {
		return configurationMap.get("HIBERNATE.DEFAULT_SCHEMA");
	}
}