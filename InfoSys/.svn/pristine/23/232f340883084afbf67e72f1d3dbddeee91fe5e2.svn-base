package com.code.services.infosys.securityaction;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SecurityStatusUser;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.elm.yakeen.yakeen4borderguard.GccInfoByNINResult;
import com.elm.yakeen.yakeen4borderguard.GccInfoByPassportResult;

import yaqeen.ejada.com.BusinessException_Exception;
import yaqeen.ejada.com.CarInfoResult;
import yaqeen.ejada.com.DataValidationException;
import yaqeen.ejada.com.IdType;
import yaqeen.ejada.com.PersonInfoRequest;
import yaqeen.ejada.com.PersonInfoResultWithDetailedSecurityStatus;
import yaqeen.ejada.com.Yakeen4BorderGuardFaultException;
import yaqeen.ejada.com.Yakeen4BorderGuardService;
import yaqeen.ejada.com.YaqeenCarInfoByPlateRequest;
import yaqeen.ejada.com.YaqeenGccInfoByNINRequest;
import yaqeen.ejada.com.YaqeenGccInfoByPassportRequest;
import yaqeen.ejada.com.YaqeenServices;

public class SecurityStatusService extends BaseService {
	private static String WSDL_URL = InfoSysConfigurationService.getElmEjadaInternalWsdlUrl();
	private final static SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
	private final static SimpleDateFormat SDF_HIJRI = new SimpleDateFormat("mm-MM-yyyy");
	private final static String FIS_SYSTEM_CODE = "FIS";

	private SecurityStatusService() {
	}

	/**
	 * 
	 * @param birthDate
	 * @param type
	 * @param socialId
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static PersonInfoResultWithDetailedSecurityStatus searchPersonInfo(Date birthDate, IdType type, String socialId, EmployeeData loginEmpData) throws BusinessException {
		return searchPersonInfoWithDetailedSecurityStatus(SDF.format(birthDate), type, socialId, loginEmpData);
	}

	/**
	 * 
	 * @param birthDate
	 * @param type
	 * @param socialId
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	public static PersonInfoResultWithDetailedSecurityStatus searchPersonInfoNationalIdentity(Date birthDate, IdType type, String socialId, EmployeeData loginEmpData) throws BusinessException {
		return searchPersonInfoWithDetailedSecurityStatus(SDF_HIJRI.format(birthDate), type, socialId, loginEmpData);
	}

	/**
	 * Search CitizenInfo, ResidentInfoByIqama, VisitorInfoByBorderNumber
	 * 
	 * @param birthDate
	 * @param type
	 * @param socialId
	 * @param loginEmpData
	 * @return
	 * @throws BusinessException
	 */
	private static PersonInfoResultWithDetailedSecurityStatus searchPersonInfoWithDetailedSecurityStatus(String birthDate, IdType type, String socialId, EmployeeData loginEmpData) throws BusinessException {
		try {
			Yakeen4BorderGuardService yakeen4BorderGuardService = new Yakeen4BorderGuardService(new URL(WSDL_URL));
			YaqeenServices yaqeenServices = yakeen4BorderGuardService.getYakeen4BorderGuardPort();
			PersonInfoRequest personInfoRequest = new PersonInfoRequest();
			personInfoRequest.setOperatorId(loginEmpData.getSocialID());
			personInfoRequest.setSystemCode(FIS_SYSTEM_CODE);
			personInfoRequest.setDateOfBirth(birthDate);
			personInfoRequest.setIdType(type);
			personInfoRequest.setIdNumber(socialId);
			personInfoRequest.setClientIpAddress(InetAddress.getLocalHost().getHostAddress());
			personInfoRequest.setReferenceNumber(generateReferenceNumber());
			return yaqeenServices.getPersonInfoWithDetailedSecuirtyStatus(personInfoRequest);
		} catch (Yakeen4BorderGuardFaultException e) {
			throw new BusinessException("error_yaqeenWrongData");
		} catch (MalformedURLException e) {
			throw new BusinessException("error_yaqeenWsdlError");
		} catch (BusinessException_Exception e) {
			throw new BusinessException("error_yaqeenWrapperError");
		} catch (DataValidationException e) {
			throw new BusinessException("error_yaqeenWrapperWrongData");
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_yaqeenWsdlError");
		}
	}

	/**
	 * 
	 * @param socialId
	 * @param countryId
	 * @return
	 * @throws BusinessException
	 */
	public static GccInfoByPassportResult searchGccInfoByPassportRequest(String socialId, Long countryId, EmployeeData loginEmpData) throws BusinessException {
		try {
			Yakeen4BorderGuardService yakeen4BorderGuardService = new Yakeen4BorderGuardService(new URL(InfoSysConfigurationService.getElmEjadaInternalWsdlUrl()));
			YaqeenServices yaqeenServices = yakeen4BorderGuardService.getYakeen4BorderGuardPort();
			YaqeenGccInfoByPassportRequest gccInfoByPassportRequest = new YaqeenGccInfoByPassportRequest();
			gccInfoByPassportRequest.setOperatorId(loginEmpData.getSocialID());
			gccInfoByPassportRequest.setSystemCode(FIS_SYSTEM_CODE);
			gccInfoByPassportRequest.setGccNationality((short) 0);
			gccInfoByPassportRequest.setGccPassportNumber(socialId);
			gccInfoByPassportRequest.setReferenceNumber(generateReferenceNumber());
			return yaqeenServices.getGCCInfoByPassport(gccInfoByPassportRequest);
		} catch (Yakeen4BorderGuardFaultException e) {
			throw new BusinessException("error_yaqeenWrongData");
		} catch (MalformedURLException e) {
			throw new BusinessException("error_yaqeenWsdlError");
		} catch (BusinessException_Exception e) {
			throw new BusinessException("error_yaqeenWrapperError");
		} catch (DataValidationException e) {
			throw new BusinessException("error_yaqeenWrapperWrongData");
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_yaqeenWsdlError");
		}
	}

	/**
	 * 
	 * @param socialId
	 * @param countryId
	 * @return
	 * @throws BusinessException
	 */
	public static GccInfoByNINResult searchGccInfoByNINRequest(String socialId, Long countryId, EmployeeData loginEmpData) throws BusinessException {
		try {
			Yakeen4BorderGuardService yakeen4BorderGuardService = new Yakeen4BorderGuardService(new URL(InfoSysConfigurationService.getElmEjadaInternalWsdlUrl()));
			YaqeenServices yaqeenServices = yakeen4BorderGuardService.getYakeen4BorderGuardPort();
			YaqeenGccInfoByNINRequest gccInfoByNINRequest = new YaqeenGccInfoByNINRequest();
			gccInfoByNINRequest.setOperatorId(loginEmpData.getSocialID());
			gccInfoByNINRequest.setSystemCode(FIS_SYSTEM_CODE);
			gccInfoByNINRequest.setGccNationality((short) 0);
			gccInfoByNINRequest.setGccNIN(socialId);
			gccInfoByNINRequest.setReferenceNumber(generateReferenceNumber());
			return yaqeenServices.getGCCInfoByNIN(gccInfoByNINRequest);
		} catch (Yakeen4BorderGuardFaultException e) {
			throw new BusinessException("error_yaqeenWrongData");
		} catch (MalformedURLException e) {
			throw new BusinessException("error_yaqeenWsdlError");
		} catch (BusinessException_Exception e) {
			throw new BusinessException("error_yaqeenWrapperError");
		} catch (DataValidationException e) {
			throw new BusinessException("error_yaqeenWrapperWrongData");
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_yaqeenWsdlError");
		}
	}

	/**
	 * Search CarInfoByPlateResult
	 * 
	 * @param plateText1
	 * @param plateText2
	 * @param plateText3
	 * @param plateNumber
	 * @param plateType
	 * @return
	 * @throws BusinessException
	 */
	public static CarInfoResult searchCarInfoByPlateRequest(String plateText1, String plateText2, String plateText3, Short plateNumber, Short plateType, EmployeeData loginEmpData) throws BusinessException {
		try {
			Yakeen4BorderGuardService yakeen4BorderGuardService = new Yakeen4BorderGuardService(new URL(InfoSysConfigurationService.getElmEjadaInternalWsdlUrl()));
			YaqeenServices yaqeenServices = yakeen4BorderGuardService.getYakeen4BorderGuardPort();
			YaqeenCarInfoByPlateRequest carInfoByPlateRequest = new YaqeenCarInfoByPlateRequest();
			carInfoByPlateRequest.setSystemCode(FIS_SYSTEM_CODE);
			carInfoByPlateRequest.setOperatorId(loginEmpData.getSocialID());
			carInfoByPlateRequest.setPlateText1(plateText3);
			carInfoByPlateRequest.setPlateText2(plateText2);
			carInfoByPlateRequest.setPlateText3(plateText1);
			carInfoByPlateRequest.setPlateNumber(plateNumber);
			carInfoByPlateRequest.setPlateType(plateType);
			carInfoByPlateRequest.setReferenceNumber(generateReferenceNumber());
			return yaqeenServices.getCarInfoByPlate(carInfoByPlateRequest);
		} catch (Yakeen4BorderGuardFaultException e) {
			throw new BusinessException("error_yaqeenWrongData");
		} catch (MalformedURLException e) {
			throw new BusinessException("error_yaqeenWsdlError");
		} catch (BusinessException_Exception e) {
			throw new BusinessException("error_yaqeenWrapperError");
		} catch (DataValidationException e) {
			throw new BusinessException("error_yaqeenWrapperWrongData");
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_yaqeenWsdlError");
		}
	}

	/**
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private static String generateReferenceNumber() throws BusinessException {
		try {
			return CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.REFERENCE_NUMBER.getEntityId(), Integer.MAX_VALUE).toString();
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Status Users
	 * 
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public static SecurityStatusUser getSecurityStatusUserByKeyAndType(String key, int type) throws BusinessException {
		try {
			return searchSecurityStatusUsers(key, type).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Security Status by type
	 * 
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public static List<SecurityStatusUser> getSecurityStatusUserByType(Integer type) throws BusinessException {
		try {
			return searchSecurityStatusUsers(null, type == null ? FlagsEnum.ALL.getCode() : type);
		} catch (NoDataException e) {
			return new ArrayList<SecurityStatusUser>();
		}
	}

	/**
	 * Get Security Status Users
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<SecurityStatusUser> getAllSecurityStatusUsers() throws BusinessException {
		try {
			return searchSecurityStatusUsers(null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<SecurityStatusUser>();
		}
	}

	/**
	 * Save SecurityStatusUser
	 * 
	 * @param securityStatusUser
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveSecurityStatusUser(SecurityStatusUser securityStatusUser, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			securityStatusUser.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(securityStatusUser, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			securityStatusUser.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_nameViolation");
			} else {
				Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save SecurityStatusUser
	 * 
	 * @param securityStatusUser
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveSecurityStatusUser(List<SecurityStatusUser> securityStatusUserList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			for (SecurityStatusUser securityStatusUser : securityStatusUserList) {
				securityStatusUser.setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.addEntity(securityStatusUser, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			for (SecurityStatusUser securityStatusUser : securityStatusUserList)
				securityStatusUser.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_nameViolation");
			} else {
				Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete SecurityStatusUser
	 * 
	 * @param securityStatusUser
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteSecurityStatusUser(SecurityStatusUser securityStatusUser, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			securityStatusUser.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(securityStatusUser, session);

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
				Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete list of SecurityStatusUser
	 * 
	 * @param securityStatusUserList
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteSecurityStatusUser(List<SecurityStatusUser> securityStatusUserList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SecurityStatusUser securityStatusUser : securityStatusUserList) {
				securityStatusUser.setSystemUser(loginEmpData.getEmpId().toString());
				DataAccess.deleteEntity(securityStatusUser, session);
			}

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
				Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Search Security Status Users
	 * 
	 * @param key
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SecurityStatusUser> searchSecurityStatusUsers(String key, int type) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_KEY", key == null || key.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : key);
			qParams.put("P_TYPE", type);
			return DataAccess.executeNamedQuery(SecurityStatusUser.class, QueryNamesEnum.SECURITY_STATUS_USER_SEARCH_SECURITY_STATUS_USERS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Print National Identity Security Status
	 * 
	 * @param arabicName
	 * @param foreignName
	 * @param socialId
	 * @param expiryDate
	 * @param versionNumber
	 * @param releaseDate
	 * @param releaseRegion
	 * @param lifeStatus
	 * @param birthDate
	 * @param gender
	 * @param job
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatusString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printNationalIdentitySecurityStatus(String arabicName, String foreignName, String socialId, String expiryDate, String versionNumber, String releaseDate, String releaseRegion, String lifeStatus, String birthDate, String gender, String job, String timeNow, String fullName, String hijriDateString, String securityStatusString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ARABIC_NAME", arabicName);
			parameters.put("P_ENGLISH_NAME", foreignName);
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_VERSION_NUMBER", versionNumber);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_RELEASE_REGION", releaseRegion);
			parameters.put("P_LIFE_STATUS", lifeStatus);
			parameters.put("P_BIRTH_DATE", birthDate);
			parameters.put("P_GENDER", gender);
			parameters.put("P_JOB", job);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS_STRING", securityStatusString);

			reportName = ReportNamesEnum.NATIONAL_IDENTITY_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Print Resident Security Status
	 * 
	 * @param arabicName
	 * @param foreignName
	 * @param socialId
	 * @param expiryDate
	 * @param releaseDate
	 * @param releaseRegion
	 * @param lifeStatus
	 * @param birthDate
	 * @param gender
	 * @param job
	 * @param passportExpiryDate
	 * @param nationality
	 * @param ownerNumber
	 * @param ownerName
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatusString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printResidentSecurityStatus(String arabicName, String foreignName, String socialId, String expiryDate, String releaseDate, String releaseRegion, String lifeStatus, String birthDate, String gender, String job, String passportExpiryDate, String nationality, String ownerNumber, String ownerName, String timeNow, String fullName, String hijriDateString, String securityStatusString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ARABIC_NAME", arabicName);
			parameters.put("P_ENGLISH_NAME", foreignName);
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_RELEASE_REGION", releaseRegion);
			parameters.put("P_LIFE_STATUS", lifeStatus);
			parameters.put("P_BIRTH_DATE", birthDate);
			parameters.put("P_GENDER", gender);
			parameters.put("P_JOB", job);
			parameters.put("P_PASSPORT_EXPIRY_DATE", passportExpiryDate);
			parameters.put("P_NATIONALITY", nationality);
			parameters.put("P_OWNER_NUMBER", ownerNumber);
			parameters.put("P_OWNER_NAME", ownerName);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS_STRING", securityStatusString);

			reportName = ReportNamesEnum.RESIDENT_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Print Visit Visa Security Status
	 * 
	 * @param arabicName
	 * @param foreignName
	 * @param socialId
	 * @param visitVisaNumber
	 * @param expiryDate
	 * @param releaseDate
	 * @param passportNumber
	 * @param nationality
	 * @param lifeStatus
	 * @param birthDate
	 * @param gender
	 * @param job
	 * @param ownerNumber
	 * @param ownerName
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatusString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printVisitVisaSecurityStatus(String arabicName, String foreignName, String socialId, String visitVisaNumber, String expiryDate, String releaseDate, String passportNumber, String nationality, String lifeStatus, String birthDate, String gender, String job, String ownerNumber, String ownerName, String timeNow, String fullName, String hijriDateString, String securityStatusString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ARABIC_NAME", arabicName);
			parameters.put("P_ENGLISH_NAME", foreignName);
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_VISIT_VISA_NUMBER", visitVisaNumber);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_PASSPORT_NUMBER", passportNumber);
			parameters.put("P_NATIONALITY", nationality);
			parameters.put("P_LIFE_STATUS", lifeStatus);
			parameters.put("P_BIRTH_DATE", birthDate);
			parameters.put("P_GENDER", gender);
			parameters.put("P_JOB", job);
			parameters.put("P_OWNER_NUMBER", ownerNumber);
			parameters.put("P_OWNER_NAME", ownerName);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS_STRING", securityStatusString);

			reportName = ReportNamesEnum.VISIT_VISA_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Print Khaleeji Identity Security Status
	 * 
	 * @param arabicName
	 * @param foreignName
	 * @param socialId
	 * @param releaseDate
	 * @param birthDate
	 * @param expiryDate
	 * @param passportNumber
	 * @param lifeStatus
	 * @param nationality
	 * @param gender
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatusString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printKhaleejiIdentitySecurityStatus(String arabicName, String foreignName, String socialId, String releaseDate, String birthDate, String expiryDate, String passportNumber, String lifeStatus, String nationality, String gender, String timeNow, String fullName, String hijriDateString, String securityStatusString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ARABIC_NAME", arabicName);
			parameters.put("P_ENGLISH_NAME", foreignName);
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_BIRTH_DATE", birthDate);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_PASSPORT_NUMBER", passportNumber);
			parameters.put("P_LIFE_STATUS", lifeStatus);
			parameters.put("P_NATIONALITY", nationality);
			parameters.put("P_GENDER", gender);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS_STRING", securityStatusString);

			reportName = ReportNamesEnum.KHALEEJI_IDENTITY_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Print Khaleeji Passport Security Status
	 * 
	 * @param arabicName
	 * @param foreignName
	 * @param socialId
	 * @param releaseDate
	 * @param birthDate
	 * @param expiryDate
	 * @param nationality
	 * @param lifeStatus
	 * @param gender
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatusString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printKhaleejiPassportSecurityStatus(String arabicName, String foreignName, String socialId, String releaseDate, String birthDate, String expiryDate, String nationality, String lifeStatus, String gender, String timeNow, String fullName, String hijriDateString, String securityStatusString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ARABIC_NAME", arabicName);
			parameters.put("P_ENGLISH_NAME", foreignName);
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_BIRTH_DATE", birthDate);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_NATIONALITY", nationality);
			parameters.put("P_LIFE_STATUS", lifeStatus);
			parameters.put("P_GENDER", gender);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS_STRING", securityStatusString);

			reportName = ReportNamesEnum.KHALEEJI_PASSPORT_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Print Car Security Status
	 * 
	 * @param ownerId
	 * @param registerationType
	 * @param plateNumber
	 * @param vehicleMaker
	 * @param vehicleModel
	 * @param vehicleColor
	 * @param modelYear
	 * @param sequenceNumber
	 * @param releaseRegion
	 * @param releaseDate
	 * @param expiryDate
	 * @param timeNow
	 * @param fullName
	 * @param hijriDateString
	 * @param securityStatus
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] printCarSecurityStatus(Long ownerId, Short registerationType, String plateNumber, String vehicleMaker, String vehicleModel, String vehicleColor, Short modelYear, Integer sequenceNumber, String releaseRegion, String releaseDate, String expiryDate, String timeNow, String fullName, String hijriDateString, Boolean securityStatus) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_OWNER_ID", ownerId);
			parameters.put("P_REGISTERATION_TYPE", registerationType);
			parameters.put("P_PLATE_NUMBER", plateNumber);
			parameters.put("P_VEHICLE_MAKER", vehicleMaker);
			parameters.put("P_VEHICLE_MODEL", vehicleModel);
			parameters.put("P_COLOR", vehicleColor);
			parameters.put("P_MODEL_YEAR", modelYear);
			parameters.put("P_SEQUENCE_NUMBER", sequenceNumber);
			parameters.put("P_RELEASE_REGION", releaseRegion);
			parameters.put("P_RELEASE_DATE", releaseDate);
			parameters.put("P_EXPIRY_DATE", expiryDate);
			parameters.put("P_TIME", timeNow);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", fullName);
			parameters.put("P_HIJRI_SYS_DATE", hijriDateString);
			parameters.put("P_SECURITY_STATUS", securityStatus);

			reportName = ReportNamesEnum.CAR_SECURITY_STATUS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SecurityStatusService.class, e, "SecurityStatusService");
			throw new BusinessException("error_general");
		}
	}

}
