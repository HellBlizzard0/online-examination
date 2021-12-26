package com.code.services.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.OptimisticLockException;

import org.hibernate.dialect.lock.OptimisticEntityLockException;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.activity.IntegrationEmployeeActivityData;
import com.code.dal.orm.audit.RunningThread;
import com.code.dal.orm.setup.BankBranchData;
import com.code.dal.orm.setup.EntitySequenceGenerator;
import com.code.dal.orm.setup.LocationProfileData;
import com.code.dal.orm.setup.QualificationData;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckStatsReportEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.labcheck.DelayedLabChecksResultJob;
import com.code.services.log4j.Log4j;
import com.code.services.workflow.surveillance.jobs.ConversationReminderJob;
import com.code.services.workflow.surveillance.jobs.SecurityStatusQuartzJob;

public class CommonService extends BaseService {

	public static RunningThread getThread(String threadName) throws BusinessException {
		List<RunningThread> resultList = searchThread(threadName);
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	private static List<RunningThread> searchThread(String threadName) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_THREAD_NAME", threadName == null ? FlagsEnum.ALL : threadName);
			return DataAccess.executeNamedQuery(RunningThread.class, QueryNamesEnum.THREAD_GET_THREAD.getCode(), qParams);
		} catch (NoDataException e) {
			return new ArrayList<RunningThread>();
		} catch (Exception e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_DBError");
		}
	}

	public static void runThreads() {
		BaseService.startThreads();
	}

	public static void stopThreads() {
		BaseService.stopThreads();
	}

	// Job for send notifications if employee or non-employee security status is changed
	public static void startYaqeenJob() {
		BaseService.initializeYaqeenJob(SecurityStatusQuartzJob.class, "YaqeenQuartzJob", "YaqeenTrigger", Boolean.valueOf(getConfig("yaqeenJobFlag")));
	}

	// Job for send Reminder notifications
	public static void startConversationReminderJob() {
		BaseService.initializeSecurityReminderJob(ConversationReminderJob.class, "ConversationReminderJob", "ConversationReminderTrigger", Boolean.valueOf(getConfig("ConversationReminderJobFlag")));
	}

	// Job for Delayed Results of LabChecks every Sunday
	public static void startDelayedResultsRemiderJob() {
		BaseService.initializeDelayedResultsReminderJob(DelayedLabChecksResultJob.class, "DelayedLabChecksResultJob", "DelayedLabChecksResultJob", Boolean.valueOf(getConfig("DelayedLabChecksResultJob")));
	}
	/**
	 * Generate Sequence Number given entity id, mask
	 * 
	 * @param entityId
	 * @param maxValue
	 * @param useSession
	 * @return
	 * @throws BusinessException
	 */
	synchronized public static Long generateSequenceNumber(long entityId, int maxValue, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		EntitySequenceGenerator entitySeq = searchSequenceNumber(entityId, session);
		Long sequence = null;
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			if (entitySeq == null) {
				entitySeq = new EntitySequenceGenerator();
				entitySeq.setEntityId(entityId);
				entitySeq.setSequenceNumber(1L);
				saveEntitySeqGenerator(entitySeq, session);
				sequence = entitySeq.getSequenceNumber();
			} else {
				sequence = entitySeq.getSequenceNumber();
				sequence = sequence % maxValue + 1;
				entitySeq.setSequenceNumber(sequence);
				updateEntitySeqGenerator(entitySeq, session);
			}
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (e instanceof OptimisticEntityLockException) {
				Log4j.traceError(CommonService.class, "OptimisticEntityLockException : " + e.getMessage());
				return generateSequenceNumber(entityId, maxValue, session);
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
		return sequence;
	}

	/**
	 * Search Sequence Generator Number Given Entity ID
	 * 
	 * @param entityId
	 * @param useSession
	 * @return
	 * @throws BusinessException
	 */
	private static EntitySequenceGenerator searchSequenceNumber(long entityId, CustomSession... useSession) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ENTITY", entityId);
			return DataAccess.executeNamedQuery(EntitySequenceGenerator.class, QueryNamesEnum.ENTITY_SEQUENCE_GENERATOR_SEARCH_SEQUENCE_GENERATOR.getCode(), qParams, useSession).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Saving EntitySequenceGenerator
	 * 
	 * @param entitySeq
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void saveEntitySeqGenerator(EntitySequenceGenerator entitySeq, CustomSession... useSession) throws BusinessException, OptimisticEntityLockException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.addEntity(entitySeq, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
			if (e instanceof OptimisticEntityLockException || e instanceof OptimisticLockException) {
				throw new OptimisticEntityLockException(CommonService.class, e.getMessage());
			} else {
				Log4j.traceErrorException(CommonService.class, e, "CommonService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update EntitySequenceGenerator
	 * 
	 * @param entitySeq
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void updateEntitySeqGenerator(EntitySequenceGenerator entitySeq, CustomSession... useSession) throws BusinessException, OptimisticEntityLockException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			DataAccess.updateEntity(entitySeq, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
			if (e instanceof OptimisticEntityLockException || e instanceof OptimisticLockException) {
				throw new OptimisticEntityLockException(CommonService.class, e.getMessage());
			} else {
				Log4j.traceErrorException(CommonService.class, e, "CommonService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get banks branches
	 * 
	 * @return list of bank branches
	 * @throws BusinessException
	 */
	public static List<BankBranchData> getBankBranches() throws BusinessException {
		try {
			return searchBankBranches();
		} catch (NoDataException e) {
			return new ArrayList<BankBranchData>();
		}
	}

	/**
	 * Search banks branches
	 * 
	 * @return list of bank branches
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<BankBranchData> searchBankBranches() throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			return DataAccess.executeNamedQuery(BankBranchData.class, QueryNamesEnum.BANK_BRANCH_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Qualifications
	 * 
	 * @return list of Qualifications
	 * @throws BusinessException
	 */
	public static List<QualificationData> getQualifications() throws BusinessException {
		try {
			return searchQualifications();
		} catch (NoDataException e) {
			return new ArrayList<QualificationData>();
		}
	}

	/**
	 * Search Qualifications
	 * 
	 * @return list of Qualifications
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<QualificationData> searchQualifications() throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			return DataAccess.executeNamedQuery(QualificationData.class, QueryNamesEnum.QUALIFICATION_DATA_SEARCH_QUALIFICATION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Employee Activity
	 * 
	 * @param socialId
	 * @param fullName
	 * @param activityClass
	 * @param activityDomain
	 * @param fromDate
	 * @param toDate
	 * @param notes
	 * @return
	 * @throws BusinessException
	 */
	public static List<IntegrationEmployeeActivityData> getEmployeeActivity(String socialId, String fullName, String activityClass, String activityDomain, String fromDate, String toDate, String notes) throws BusinessException {
		try {
			return searchEmployeeActivity(socialId, fullName, activityClass, activityDomain, fromDate, toDate, notes);
		} catch (NoDataException e) {
			return new ArrayList<IntegrationEmployeeActivityData>();
		}
	}

	/**
	 * Search Employee Activity
	 * 
	 * @param socialId
	 * @param fullName
	 * @param activityClass
	 * @param activityDomain
	 * @param fromDate
	 * @param toDate
	 * @param notes
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<IntegrationEmployeeActivityData> searchEmployeeActivity(String socialId, String fullName, String activityClass, String activityDomain, String fromDate, String toDate, String notes) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_FULL_NAME", fullName == null || fullName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + fullName + "%");
			qParams.put("P_NOTES", notes == null || notes.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + notes + "%");
			qParams.put("P_ACTIVITY_CLASS", activityClass == null || activityClass.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : activityClass);
			qParams.put("P_ACTIVITY_DOMAIN", activityDomain == null || activityDomain.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : activityDomain);
			qParams.put("P_FROM_DATE_STRING", fromDate == null || fromDate.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : fromDate);
			qParams.put("P_TO_DATE_STRING", toDate == null || toDate.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : toDate);
			return DataAccess.executeNamedQuery(IntegrationEmployeeActivityData.class, QueryNamesEnum.INTEGRATION_EMPLOYEE_ACTIVITY_DATA_SEARCH_INTEGRATION_EMPLOYEE_ACTIVITY_DATE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * get all locations
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<LocationProfileData> getLocations() throws BusinessException {
		try {
			return searchLocationProfile();
		} catch (NoDataException e) {
			return new ArrayList<LocationProfileData>();
		}
	}

	/**
	 * Search location profile
	 * 
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LocationProfileData> searchLocationProfile() throws BusinessException, NoDataException {
		try {
			return DataAccess.executeNamedQuery(LocationProfileData.class, QueryNamesEnum.SETUP_SEARCH_LOCATION_PROFILE_DATA.getCode(), null);
		} catch (DatabaseException e) {
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Employee Activity Bytes
	 * 
	 * @param assginmentId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeActivityBytes(String socialId, String fullName, String activityClass, String activityDomain, String fromDate, String toDate, String additionalNotes, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SOCIAL_ID", socialId);
			parameters.put("P_NAME", fullName);
			parameters.put("P_ACTIVITIY_CLASS", activityClass);
			parameters.put("P_ACTIVITY_DOMAIN", activityDomain);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_ADITTIONAL_NOTES", additionalNotes);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EMPLOYEE_ACTIVITIES_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(AssignmentService.class, e, "AssignmentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param startDateFlag
	 * @param endDateFlag
	 * @param regionName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getStatusReportBytes(String startDateString, String endDateString, long regionId, String startDateFlag, String endDateFlag, String regionName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DATE_FROM", startDateString);
			parameters.put("P_DATE_TO", endDateString);
			parameters.put("P_REGION", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_DATE_FROM_FLAG", startDateFlag);
			parameters.put("P_DATE_TO_FLAG", endDateFlag);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			String reportName = ReportNamesEnum.REVIEWING_SYSTEM_WORK_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param startDateFlag
	 * @param endDateFlag
	 * @param pageMode
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckStatsReportBytes(String startDateString, String endDateString, String startDateFlag, String endDateFlag, int pageMode, String employeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DATE_FROM", startDateString);
			parameters.put("P_DATE_TO", endDateString);
			parameters.put("P_DATE_FROM_FLAG", startDateFlag);
			parameters.put("P_DATE_TO_FLAG", endDateFlag);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE_STRING", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE", employeeName);
			parameters.put("P_MODE", pageMode);
			List<LabCheckStatsReportEnum> labCheckStatsEnumList = LabCheckStatsReportEnum.getAllCheckStatsValues();
			String reportNameEnum = "";
			for (LabCheckStatsReportEnum labChecksEnum : labCheckStatsEnumList) {
				if (pageMode == labChecksEnum.getCode()) {
					reportNameEnum = labChecksEnum.getReportEnumName();
					break;
				}
			}
			String reportName = ReportNamesEnum.valueOf(reportNameEnum).getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(CommonService.class, e, "CommonService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Check if the location is inside Saudi Arabia boundaries
	 * 
	 * @param coordinateLatitude
	 * @param coordinateLongitude
	 * @return
	 */
	public static boolean isLocationOutOfBounds(Double coordinateLatitude, Double coordinateLongitude) {
		int minLongDegree = InfoSysConfigurationService.getMinLongKSADegree();
		int maxLongDegree = InfoSysConfigurationService.getMaxLongKSADegree();
		int minLatDegree = InfoSysConfigurationService.getMinLatKSADegree();
		int maxLatDegree = InfoSysConfigurationService.getMaxLatKSADegree();
		if (coordinateLatitude != null) {
			int coordinateLatDegree = coordinateLatitude.intValue();
			if (coordinateLatDegree < minLatDegree || coordinateLatDegree >= maxLatDegree)
				return true;
		}
		if (coordinateLongitude != null) {
			int coordinateLongDegree = coordinateLongitude.intValue();
			if (coordinateLongDegree < minLongDegree || coordinateLongDegree >= maxLongDegree)
				return true;
		}
		return false;
	}
}