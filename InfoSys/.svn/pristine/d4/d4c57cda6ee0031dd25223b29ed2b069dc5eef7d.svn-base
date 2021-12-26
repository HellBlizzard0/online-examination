package com.code.services.infosys.securityanalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.securityanalysis.DecisionData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.FollowUpResultData;
import com.code.dal.orm.securityanalysis.Network;
import com.code.dal.orm.securityanalysis.NetworkData;
import com.code.dal.orm.securityanalysis.ReferralData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.FollowUpDecisionTypeEnum;
import com.code.enums.FollowUpStatusEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.securityanalysis.SecurityAnalysisWorkFlow;

public class FollowUpService extends BaseService {
	private FollowUpService() {

	}

	/**
	 * Validate FollowUp data
	 * 
	 * @param followUpData
	 * @throws BusinessException
	 */
	private static void validateFollowUp(FollowUpData followUpData) throws BusinessException {
		if ((followUpData.getContactNumber() == null || followUpData.getContactNumber().trim().isEmpty()) && (followUpData.getCode() == null || followUpData.getCode().trim().isEmpty())) {
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_contactNumberOrCodeNumber", "ar") });
		}
		if (followUpData.getContactNumber().length() > 15) {
			throw new BusinessException("error_contactNumberValidation");
		}
		if (followUpData.getFollowUpStartDate() == null) {
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_followUpStartDate", "ar") });
		}
		if (followUpData.getFollowUpPeriod() == null) {
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_followUpPeriod", "ar") });
		}
		if (followUpData.getRegionId() == null) {
			throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_region", "ar") });
		}
		// TODO-SHALABY check on null latitude and longitude
		if (CommonService.isLocationOutOfBounds(followUpData.getCoordinateLatitude(), followUpData.getCoordinateLongitude())) {
			throw new BusinessException("error_coordinatesOutOfBounds");
		}
		if (followUpData.getCode() != null && !followUpData.getCode().trim().isEmpty()) {
			FollowUpData followUpExists = getFollowUpDataByCode(followUpData.getCode());
			// Check if this code found with different contact number or not
			if (followUpData.getId() == null) {
				if (followUpExists != null && !(followUpExists.getContactNumber().equals(followUpData.getContactNumber()))) {
					throw new BusinessException("error_codeRepeated");
				}
			} else {// in update case
				if (followUpExists != null && !(followUpExists.getContactNumber().equals(followUpData.getContactNumber()))) {
					throw new BusinessException("error_codeRepeated");
				}
			}
		}
	}

	/**
	 * Save FollowUp details
	 * 
	 * @param followUpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveFollowUp(FollowUpData followUpData, CustomSession... useSession) throws BusinessException {
		validateFollowUp(followUpData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(followUpData.getFollowUp(), session);
			followUpData.setId(followUpData.getFollowUp().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			followUpData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * Update FollowUp details
	 * 
	 * @param followUpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateFollowUp(FollowUpData followUpData, CustomSession... useSession) throws BusinessException {
		validateFollowUp(followUpData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.updateEntity(followUpData.getFollowUp(), session);

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
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * Validate FollowUp result
	 * 
	 * @param followUpResultData
	 * @throws BusinessException
	 */
	private static void validateFollowUpResult(FollowUpResultData followUpResultData) throws BusinessException {
		if (followUpResultData.getFollowUpId() == null) {
			throw new BusinessException("error_mandatory");
		}
		if (followUpResultData.getDomainIdFollowResults() == null) {
			throw new BusinessException("error_mandatory");
		}
		if (followUpResultData.getResultDetails() == null || followUpResultData.getResultDetails().trim().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
		if (!(getFollowUpResultsByFollowUpIdAndTypeAndDetails(followUpResultData.getFollowUpId(), followUpResultData.getDomainIdFollowResults(), followUpResultData.getResultDetails()).isEmpty())) {
			throw new BusinessException("error_resultDetailsRepeated");
		}
	}

	/**
	 * Save FollowUp result
	 * 
	 * @param followUpResultData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveFollowUpResult(FollowUpResultData followUpResultData, CustomSession... useSession) throws BusinessException {
		validateFollowUpResult(followUpResultData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(followUpResultData.getResult(), session);
			followUpResultData.setId(followUpResultData.getResult().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			followUpResultData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * validate Decision Data
	 * 
	 * @param decisionData
	 * @throws BusinessException
	 */
	private static void validateDecision(DecisionData decisionData) throws BusinessException {
		if (decisionData.getDecisionType() != null && decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_EXTEND.getCode())) {
			if (decisionData.getExtensionDuration() == null || decisionData.getExtensionDuration() <= 0) {
				throw new BusinessException("error_extendDurationMandatory");
			}
		}

		if (decisionData.getDecisionType() != null && decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_END.getCode())) {
			if (decisionData.getDomainIdStopReasons() == null) {
				throw new BusinessException("error_stopReasonMandatory");
			}
		}

		if (decisionData.getDomainIdExternalSides() != null) {
			if (decisionData.getEmpId() == null) {
				throw new BusinessException("error_specialistEmployeeMandatory");
			}
		}
		if (decisionData.getResumeDate() != null && decisionData.getResumeDate().before(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_resumeDateBeforeCurrDate");
		}
	}

	/**
	 * Save DecisionData
	 * 
	 * @param decisionData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveDecision(EmployeeData loginEmp, DecisionData decisionData, FollowUpData followUpData, Date followUpEndDate, CustomSession... useSession) throws BusinessException {
		validateDecision(decisionData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(decisionData.getDecision(), session);
			decisionData.setId(decisionData.getDecision().getId());

			if (decisionData.getResumeDate() != null) {
				followUpData.setStatus(FollowUpStatusEnum.SUSPENDED.getCode());
				followUpData.setActiveFlag(false);
				FollowUpData followUpDataClone = new FollowUpData();
				followUpDataClone = (FollowUpData) followUpData.clone();
				followUpDataClone.setId(null);
				followUpDataClone.setStatus(FollowUpStatusEnum.RE_FOLLOW_UP.getCode());
				followUpDataClone.setFollowUpStartDate(decisionData.getResumeDate());
				followUpDataClone.setFollowUpEndDate(followUpEndDate);
				saveFollowUp(followUpDataClone, session);
			} else if (decisionData.getResumeDate() == null && decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_END.getCode())) {
				followUpData.setStatus(FollowUpStatusEnum.FINISHED.getCode());
				followUpData.setActiveFlag(false);

			} else {
				followUpData.setFollowUpEndDate(followUpEndDate);
				followUpData.setFollowUpPeriod(decisionData.getExtensionDuration());
			}
			updateFollowUp(followUpData, session);
			if (decisionData.getEmpId() != null) { // notify employee if referral exist
				String arabicDetailsSummary = getParameterizedMessage(decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_EXTEND.getCode()) ? "wf_followUpExtend" : "wf_followUpEnded", "ar", new Object[] { followUpData.getCode() });
				String englishDetailsSummary = getParameterizedMessage(decisionData.getDecisionType().equals(FollowUpDecisionTypeEnum.FOLLOW_UP_EXTEND.getCode()) ? "wf_followUpExtend" : "wf_followUpEnded", "en", new Object[] { followUpData.getCode() });
				SecurityAnalysisWorkFlow.notify(loginEmp.getEmpId(), arabicDetailsSummary, arabicDetailsSummary, englishDetailsSummary, decisionData.getEmpId(), session);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			decisionData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Save Referral data
	 * 
	 * @param referralData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveReferral(ReferralData referralData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			DataAccess.addEntity(referralData.getReferral(), session);
			referralData.setId(referralData.getReferral().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			referralData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate Network data
	 * 
	 * @param networkData
	 * @throws BusinessException
	 */
	private static void validateNetwork(NetworkData networkData) throws BusinessException {
		if (networkData.getDescription() == null || networkData.getDescription().trim().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
	}

	/**
	 * Save Network data
	 * 
	 * @param networkData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveNetwork(NetworkData networkData, CustomSession... useSession) throws BusinessException {
		validateNetwork(networkData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			String networkNumber = generateNetworkNumber(session);
			networkData.getNetwork().setNetworkNumber(networkNumber);
			networkData.setNetworkNumber(networkNumber);

			DataAccess.addEntity(networkData.getNetwork(), session);
			networkData.setId(networkData.getNetwork().getId());

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			networkData.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Generate sequence for Network number
	 * 
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	private static String generateNetworkNumber(CustomSession session) throws BusinessException {
		// TODO Adjust Code Length
		long networkNumber = CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.NETWORK_NUMBER.getEntityId(), 100000, session);
		return HijriDateService.getHijriSysDateString().substring(6) + '-' + String.format("%05d", networkNumber);
	}

	/**
	 * Get Extended FollowUp Report
	 * 
	 * @param regionId
	 * @param followUpId
	 * @param networkId
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getExtendedFollowUpReportBytes(Long regionId, Long followUpId, Long networkId, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			String reportName = ReportNamesEnum.FOLLOW_UP_EXTENDED_NUMBERS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}

	}

	/**
	 * getFollowUpInDurationReportBytes
	 * 
	 * @param regionId
	 * @param followUpId
	 * @param networkId
	 * @param ghostName
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getFollowUpInDurationReportBytes(Long regionId, String regionName, Long followUpId, String followUpCode, Long networkId, String networkName, String ghostName, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_FOLLOW_UP_CODE", followUpCode == null || followUpCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : followUpCode);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : ghostName);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			reportName = ReportNamesEnum.FOLLOW_UP_IN_DURATION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getFollowUpInDurationStatisiticsReportBytes
	 * 
	 * @param regionId
	 * @param followUpId
	 * @param networkId
	 * @param ghostName
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getFollowUpInDurationStatisiticsReportBytes(Long regionId, String regionName, Long followUpId, String followUpCode, Long networkId, String networkName, String ghostName, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_FOLLOW_UP_CODE", followUpCode == null || followUpCode.isEmpty() ? FlagsEnum.ALL.getCode() + "" : followUpCode);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : ghostName);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.FOLLOW_UP_IN_DURATION_STATISITICS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getObservedGhostFollowUpInDurationReportBytes
	 * 
	 * @param regionId
	 * @param caseTypeId
	 * @param networkId
	 * @param ghostName
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getObservedGhostInDurationReportBytes(Long regionId, String regionName, Long caseTypeId, String caseTypeDesc, Long networkId, String networkName, String ghostName, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_CASE_TYPE_ID", caseTypeId == null ? FlagsEnum.ALL.getCode() : caseTypeId);
			parameters.put("P_CASE_TYPE_DESC", caseTypeDesc == null || caseTypeDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : caseTypeDesc);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : ghostName);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.OBSERVED_GHOST_IN_DURATION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getDelayedGhostFollowUpInDurationReportBytes
	 * 
	 * @param regionId
	 * @param caseTypeId
	 * @param networkId
	 * @param ghostName
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getDelayedGhostInDurationReportBytes(Long regionId, String regionName, Long caseTypeId, String caseTypeDesc, Long networkId, String networkName, String ghostName, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_CASE_TYPE_ID", caseTypeId == null ? FlagsEnum.ALL.getCode() : caseTypeId);
			parameters.put("P_CASE_TYPE_DESC", caseTypeDesc == null || caseTypeDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : caseTypeDesc);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : ghostName);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.DELAYED_GHOST_IN_DURATION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getReferralGhostFollowUpInDurationReportBytes
	 * 
	 * @param regionId
	 * @param caseTypeId
	 * @param networkId
	 * @param referralId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getReferralGhostInDurationReportBytes(Long regionId, String regionName, Long caseTypeId, String caseTypeDesc, Long networkId, String networkName, Long referralId, String referralDesc, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_CASE_TYPE_ID", caseTypeId == null ? FlagsEnum.ALL.getCode() : caseTypeId);
			parameters.put("P_CASE_TYPE_DESC", caseTypeDesc == null || caseTypeDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : caseTypeDesc);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_REFERRAL_ID", referralId == null ? FlagsEnum.ALL.getCode() : referralId);
			parameters.put("P_REFERRAL_DESC", referralDesc == null || referralDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : referralDesc);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.REFERRAL_GHOST_IN_DURATION.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getReferralGhostFollowUpInDurationReportBytes
	 * 
	 * @param regionId
	 * @param caseTypeId
	 * @param networkId
	 * @param referralId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getReferralGhostInDurationStatisticsReportBytes(Long regionId, String regionName, Long caseTypeId, String caseTypeDesc, Long networkId, String networkName, Long referralId, String referralDesc, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_CASE_TYPE_ID", caseTypeId == null ? FlagsEnum.ALL.getCode() : caseTypeId);
			parameters.put("P_CASE_TYPE_DESC", caseTypeDesc == null || caseTypeDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : caseTypeDesc);
			parameters.put("P_NETWORK_ID", networkId == null ? FlagsEnum.ALL.getCode() : networkId);
			parameters.put("P_NETWORK_NAME", networkName == null || networkName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : networkName);
			parameters.put("P_REFERRAL_ID", referralId == null ? FlagsEnum.ALL.getCode() : referralId);
			parameters.put("P_REFERRAL_DESC", referralDesc == null || referralDesc.isEmpty() ? FlagsEnum.ALL.getCode() + "" : referralDesc);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.REFERRAL_GHOST_IN_DURATION_STATISITICS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getConversationDetailsReportBytes
	 * 
	 * @param conversationId
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getConversationDetailsReportBytes(Long conversationId, Long followUpId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_CONVERSATION_ID", conversationId == null ? FlagsEnum.ALL.getCode() : conversationId);
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.CONVERSATION_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getPositiveFollowUpResultsReportBytes
	 * 
	 * @param followUpId
	 * @param resultTypeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 **/
	public static byte[] getPositiveFollowUpResultsReportBytes(Long followUpId, Long resultTypeId, String fromDate, String toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_FOLLOW_UP_ID", followUpId == null ? FlagsEnum.ALL.getCode() : followUpId);
			parameters.put("P_RESULT_TYPE_ID", resultTypeId == null ? FlagsEnum.ALL.getCode() : resultTypeId);
			parameters.put("P_FROM_DATE", fromDate);
			parameters.put("P_TO_DATE", toDate);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.FOLLOW_UP_POSITIVE_RESULTS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}
	
	/**
	 * 
	 * @param filteredDirectFollowUpList
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getDirectFollowsUpsReportBytes(List<Long> filteredDirectFollowUpList , String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_FOLLOW_UPS_IDS", filteredDirectFollowUpList );
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.DIRECT_FOLLOW_UPS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_general");
		}
	}

	/** -------------------------------------------------------------------- Queries -------------------------------------------------------------------- **/
	/**
	 * Get Follow Up Data By Id
	 * 
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 */
	public static FollowUpData getFollowUpDataById(Long followUpId) throws BusinessException {
		try {
			return searchFollowUps(followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Follow Up Data By Code
	 * 
	 * @param followUpcode
	 * @return
	 * @throws BusinessException
	 */
	public static FollowUpData getFollowUpDataByCode(String followUpcode) throws BusinessException {
		try {
			return searchFollowUps(FlagsEnum.ALL.getCode(), null, followUpcode, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get ended near active FollowUps between two dates
	 * 
	 * @param followUpEndDateFrom
	 * @param followUpEndDateTo
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpData> getNearEndFollowUpsByEndDate(Date followUpEndDateFrom, Date followUpEndDateTo) throws BusinessException {
		try {
			return searchFollowUps(FlagsEnum.ALL.getCode(), null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FollowUpStatusEnum.ACTIVE.getCode(), followUpEndDateFrom, followUpEndDateTo, null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<FollowUpData>();
		}
	}

	/**
	 * Get Direct FollowUp
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpData> getDirectFollowUps() throws BusinessException {
		try {
			return getCurrentFollowUps(FlagsEnum.ALL.getCode(), null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(), true, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<FollowUpData>();
		}
	}

	/**
	 * Get follow Ups by Contact Number
	 * 
	 * @param contactNumber
	 * @return
	 */
	public static List<FollowUpData> getFollowUpDataByContactNumber(String contactNumber) {
		try {
			return searchFollowUps(FlagsEnum.ALL.getCode(), contactNumber, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode());
		} catch (BusinessException | NoDataException e) {
			return new ArrayList<FollowUpData>();
		}
	}

	/**
	 * Get follow ups by network id for communication nodes
	 * 
	 * @param networkId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpData> getFollowUpsDataByNetworkId(Long networkId) throws BusinessException {
		try {
			return searchFollowUps(FlagsEnum.ALL.getCode(), null, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, networkId == null ? FlagsEnum.ALL.getCode() : networkId);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get FollowUp Data
	 * 
	 * @param active
	 * @param contactNumber
	 * @param ghostSocialId
	 * @param equipmentTypeId
	 * @param ghostName
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpData> getFollowUpData(Boolean active, String contactNumber, String ghostSocialId, Long equipmentTypeId, String ghostName, String code) throws BusinessException {
		try {
			return searchFollowUps(FlagsEnum.ALL.getCode(), contactNumber, code, ghostName, ghostSocialId, equipmentTypeId == null ? FlagsEnum.ALL.getCode() : equipmentTypeId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, active, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<FollowUpData>();
		}
	}

	/**
	 * Search FollowUp Data
	 * 
	 * @param id
	 * @param contactNumber
	 * @param code
	 * @param ghostName
	 * @param ghostSocialId
	 * @param equipmentTypeId
	 * @param directFlag
	 * @param status
	 * @param followUpEndDateFrom
	 * @param followUpEndDateTo
	 * @param active
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FollowUpData> searchFollowUps(long id, String contactNumber, String code, String ghostName, String ghostSocialId, long equipmentTypeId, int directFlag, int status, Date followUpEndDateFrom, Date followUpEndDateTo, Boolean active, long networkId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_CONTACT_NUMBER", contactNumber == null || contactNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : contactNumber);
			qParams.put("P_CODE", code == null || code.isEmpty() ? FlagsEnum.ALL.getCode() + "" : code);
			qParams.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostName + "%");
			qParams.put("P_GHOST_SOCIAL_ID", ghostSocialId == null || ghostSocialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostSocialId + "%");
			qParams.put("P_EQUIPMENT_TYPE_ID", equipmentTypeId);
			qParams.put("P_DIRECT_FLAG", directFlag);
			qParams.put("P_STATUS", status);
			qParams.put("P_END_DATE_FROM_NULL", followUpEndDateFrom == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE_FROM", followUpEndDateFrom == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(followUpEndDateFrom));
			qParams.put("P_END_DATE_TO_NULL", followUpEndDateTo == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE_TO", followUpEndDateTo == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(followUpEndDateTo));
			qParams.put("P_ACTIVE_FLAG", active == null ? FlagsEnum.ALL.getCode() : active ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_NETWORK_ID", networkId);
			return DataAccess.executeNamedQuery(FollowUpData.class, QueryNamesEnum.FOLLOW_UP_DATA_SEARCH_FOLLOW_UP_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}
	/**
	 * Search FollowUp Data
	 * 
	 * @param id
	 * @param contactNumber
	 * @param code
	 * @param ghostName
	 * @param ghostSocialId
	 * @param equipmentTypeId
	 * @param directFlag
	 * @param status
	 * @param followUpEndDateFrom
	 * @param followUpEndDateTo
	 * @param active
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FollowUpData> getCurrentFollowUps(long id, String contactNumber, String code, String ghostName, String ghostSocialId, long equipmentTypeId, int directFlag, int status, Boolean active, long networkId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_CONTACT_NUMBER", contactNumber == null || contactNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : contactNumber);
			qParams.put("P_CODE", code == null || code.isEmpty() ? FlagsEnum.ALL.getCode() + "" : code);
			qParams.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostName + "%");
			qParams.put("P_GHOST_SOCIAL_ID", ghostSocialId == null || ghostSocialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostSocialId + "%");
			qParams.put("P_EQUIPMENT_TYPE_ID", equipmentTypeId);
			qParams.put("P_DIRECT_FLAG", directFlag);
			qParams.put("P_STATUS", status);
			qParams.put("P_TODAY", HijriDateService.getHijriSysDateString());
			qParams.put("P_ACTIVE_FLAG", active == null ? FlagsEnum.ALL.getCode() : active ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_NETWORK_ID", networkId);
			return DataAccess.executeNamedQuery(FollowUpData.class, QueryNamesEnum.FOLLOW_UP_DATA_CURRENT_FOLLOW_UP_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}
	/**
	 * Get last FollowUp data for specific contact number or code
	 * 
	 * @param contactNumber
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public static FollowUpData getLastFollowUpDataByContactAndCode(String contactNumber, String code) throws BusinessException {
		try {
			return searchLastFollowUpData(contactNumber, code);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get last FollowUp data for specific contact number or code
	 * 
	 * @param contactNumber
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private static FollowUpData searchLastFollowUpData(String contactNumber, String code) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CONTACT_NUMBER", contactNumber == null || contactNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : contactNumber);
			qParams.put("P_CODE", code == null || code.isEmpty() ? FlagsEnum.ALL.getCode() + "" : code);
			return DataAccess.executeNamedQuery(FollowUpData.class, QueryNamesEnum.FOLLOW_UP_DATA_SEARCH_LAST_FOLLOW_UP_DATA.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all FollowUp results by Id
	 * 
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpResultData> getFollowUpResultsById(Long id) throws BusinessException {
		try {
			return searchFollowUpResult(id == null ? FlagsEnum.ALL.getCode() : id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<FollowUpResultData>();
		}
	}

	/**
	 * Get all FollowUp results by FollowUpId
	 * 
	 * @param followUpId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpResultData> getFollowUpResultsByFollowUpId(Long followUpId) throws BusinessException {
		try {
			return searchFollowUpResult(FlagsEnum.ALL.getCode(), followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, FlagsEnum.ALL.getCode(), null);
		} catch (NoDataException e) {
			return new ArrayList<FollowUpResultData>();
		}
	}

	/**
	 * Get FollowUp result by FollowUpId and ResultType and ResultDetails
	 * 
	 * @param followUpId
	 * @param domainIdFollowResults
	 * @param resultDetails
	 * @return
	 * @throws BusinessException
	 */
	public static List<FollowUpResultData> getFollowUpResultsByFollowUpIdAndTypeAndDetails(Long followUpId, Long domainIdFollowResults, String resultDetails) throws BusinessException {
		try {
			return searchFollowUpResult(FlagsEnum.ALL.getCode(), followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, domainIdFollowResults, resultDetails);
		} catch (NoDataException e) {
			return new ArrayList<FollowUpResultData>();
		}
	}

	/**
	 * Search FollowUp results
	 * 
	 * @param followUpId
	 * @param domainIdFollowResults
	 * @param resultDetails
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FollowUpResultData> searchFollowUpResult(long id, long followUpId, long domainIdFollowResults, String resultDetails) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_FOLLOW_UP_ID", followUpId);
			qParams.put("P_DOMAIN_ID_FOLLOW_RESULTS", domainIdFollowResults);
			qParams.put("P_RESULT_DETAILS", resultDetails == null || resultDetails.isEmpty() ? FlagsEnum.ALL.getCode() + "" : resultDetails);
			return DataAccess.executeNamedQuery(FollowUpResultData.class, QueryNamesEnum.FOLLOW_UP_RESULT_DATA_SEARCH_FOLLOW_UP_RESULT_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get network by its id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	public static NetworkData getNetworkDataById(Long id) throws BusinessException, NoDataException {
		return searchFollowUpNetworks(FlagsEnum.ALL.getCode(), null, null, id == null ? FlagsEnum.ALL.getCode() : id, FlagsEnum.ALL.getCode(), null).get(0);
	}

	/**
	 * Get Networks by networkId or followUp search parameters
	 * 
	 * @param regionId
	 * @param ghostName
	 * @param ghostSocialId
	 * @param networkId
	 * @param followUpId
	 * @param contactNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<NetworkData> getFollowUpNetworks(Long regionId, String ghostName, String ghostSocialId, Long networkId, Long followUpId, String contactNumber) throws BusinessException {
		try {
			return searchFollowUpNetworks(regionId == null ? FlagsEnum.ALL.getCode() : regionId, ghostName, ghostSocialId, networkId == null ? FlagsEnum.ALL.getCode() : networkId, followUpId == null ? FlagsEnum.ALL.getCode() : followUpId, contactNumber);
		} catch (NoDataException e) {
			return new ArrayList<NetworkData>();
		}
	}

	/**
	 * Search FollowUp Networks
	 * 
	 * @param regionId
	 * @param ghostName
	 * @param ghostSocialId
	 * @param networkId
	 * @param followUpId
	 * @param contactNumber
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<NetworkData> searchFollowUpNetworks(long regionId, String ghostName, String ghostSocialId, long networkId, long followUpId, String contactNumber) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_GHOST_NAME", ghostName == null || ghostName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostName + "%");
			qParams.put("P_GHOST_SOCIAL_ID", ghostSocialId == null || ghostSocialId.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + ghostSocialId + "%");
			qParams.put("P_NETWORK_ID", networkId);
			qParams.put("P_FOLLOW_UP_ID", followUpId);
			qParams.put("P_CONTACT_NUMBER", contactNumber == null || contactNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : contactNumber);
			return DataAccess.executeNamedQuery(NetworkData.class, QueryNamesEnum.NETWORK_DATA_SEARCH_FOLLOW_UP_NETWORKS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get All Regions
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<RegionData> getAllRegionData() throws BusinessException {
		try {
			return searchRegionData(null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<RegionData>();
		}
	}

	/**
	 * Search RegionData
	 * 
	 * @param regionId
	 * @param regionType
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<RegionData> searchRegionData(Long regionId, int regionType) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REGION_ID", regionId == null ? FlagsEnum.ALL.getCode() : regionId);
			qParams.put("P_REGION_TYPE", regionType);
			return DataAccess.executeNamedQuery(RegionData.class, QueryNamesEnum.REGION_DATA_SEARCH_REGION_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/***
	 * get All Networks
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<NetworkData> getAllNetworks() throws BusinessException {
		try {
			return searchNetworks(null, null);
		} catch (NoDataException e) {
			return new ArrayList<NetworkData>();
		}
	}

	/**
	 * Advanced search for Networks by networkNumber, description
	 * 
	 * @param networkNumber
	 * @param description
	 * @return
	 * @throws BusinessException
	 */

	public static List<NetworkData> searchNetworks(String networkNumber, String description) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_NETWORK_NUMBER", networkNumber == null || networkNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + networkNumber + "%");
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(NetworkData.class, QueryNamesEnum.NETWORK_DATA_SEARCH_NETWORKS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param networkNumber
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	public static List<Network> getNetworks(String networkNumber, String description) throws BusinessException {
		try {
			return searchNetworksSummary(networkNumber, description);
		} catch (NoDataException e) {
			return new ArrayList<Network>();
		}
	}

	/**
	 * Advanced search for Networks by networkNumber, description
	 * 
	 * @param networkNumber
	 * @param description
	 * @return
	 * @throws BusinessException
	 */
	private static List<Network> searchNetworksSummary(String networkNumber, String description) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_NETWORK_NUMBER", networkNumber == null || networkNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + networkNumber + "%");
			qParams.put("P_DESCRIPTION", description == null || description.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + description + "%");
			return DataAccess.executeNamedQuery(Network.class, QueryNamesEnum.NETWORK_DATA_SEARCH_NETWORKS_SUMMARY.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Advanced search for FollowUps by FollowUpData, ConversationData, ConversationPartyData and FollowUpResultData
	 * 
	 * @param contactNumber
	 * @param code
	 * @param ghostName
	 * @param ghostSocialId
	 * @param equipmentTypeId
	 * @param regionId
	 * @param sectorId
	 * @param followUpFromDate
	 * @param followUpToDate
	 * @param networkId
	 * @param caseTypeId
	 * @param directFlag
	 * @param conversationType
	 * @param conversationResult
	 * @param conversationFromDate
	 * @param conversationToDate
	 * @param conversationLong
	 * @param conversationLat
	 * @param conversationPartyConNum
	 * @param domainIdFollowResults
	 * @param resultFromDate
	 * @param resultToDate
	 * @param activeFlag
	 * @param networkDesc
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException 
	 */
	public static List<FollowUpData> advancedSearchFollowUps(String contactNumber, String code, String ghostName, String ghostSocialId , Long regionId, Long sectorId, Date followUpFromDate, Date followUpToDate, Long networkId, Long caseTypeId, int directFlag, int conversationType, int conversationResult, Date conversationFromDate, Date conversationToDate, Double conversationLong, Double conversationLat, Long conversationPartyConNum, String conversationDetails , Long domainIdFollowResults,
			Date resultFromDate, Date resultToDate, int activeFlag, String networkDesc) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			List<FollowUpData> result;
			StringBuffer query = new StringBuffer("select distinct fu from FollowUpData fu");
			StringBuffer fromClause = new StringBuffer("");
			StringBuffer whereClause = new StringBuffer(" where 1 = 1 ");

			/************************************** FOLLOWUP ******************************/

			if (contactNumber != null && !contactNumber.isEmpty()) {
				whereClause.append(" and fu.contactNumber = :P_CONTACT_NUMBER ");
				qParams.put("P_CONTACT_NUMBER", contactNumber);
			}

			if (activeFlag != FlagsEnum.ALL.getCode()) {
				whereClause.append(" and fu.activeFlag = :P_ACTIVE_FLAG ");
				qParams.put("P_ACTIVE_FLAG", activeFlag);
			}

			if (code != null && !code.isEmpty()) {
				whereClause.append(" and fu.code = :P_CODE ");
				qParams.put("P_CODE", code);
			}

			if (ghostName != null && !ghostName.isEmpty()) {
				whereClause.append(" and (fu.employeeName like :P_GHOST_NAME or fu.nonEmployeeName like :P_GHOST_NAME) ");
				qParams.put("P_GHOST_NAME", "%" + ghostName + "%");
			}

			if (ghostSocialId != null && !ghostSocialId.isEmpty()) {
				whereClause.append(" and (fu.employeeSocialId like :P_GHOST_SOCIAL_ID or fu.nonEmployeeSocialId like :P_GHOST_SOCIAL_ID) ");
				qParams.put("P_GHOST_SOCIAL_ID", "%" + ghostSocialId + "%");
			}

			if (networkDesc != null && !networkDesc.isEmpty()) {
				whereClause.append(" and fu.followUpNetworkDesc like :P_NETWORK_DESC ");
				qParams.put("P_NETWORK_DESC", "%" + networkDesc + "%");
			}

			if (regionId != null && !regionId.equals((long) FlagsEnum.ALL.getCode())) {
				whereClause.append(" and fu.regionId = :P_REGION_ID ");
				qParams.put("P_REGION_ID", regionId);
			}

			if (sectorId != null) {
				whereClause.append(" and fu.sectorId = :P_SECTOR_ID ");
				qParams.put("P_SECTOR_ID", sectorId);
			}

			if (followUpToDate != null) {
				whereClause.append(" and fu.followUpEndDate <= PKG_CHAR_TO_DATE (:P_FOLLOW_UP_TO_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_FOLLOW_UP_TO_DATE", HijriDateService.getHijriDateString(followUpToDate));
			}

			if (followUpFromDate != null) {
				whereClause.append(" and fu.followUpStartDate >= PKG_CHAR_TO_DATE (:P_FOLLOW_UP_FROM_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_FOLLOW_UP_FROM_DATE", HijriDateService.getHijriDateString(followUpFromDate));
			}

			if (networkId != null) {
				whereClause.append(" and fu.followUpNetworkId = :P_NETWORK_ID ");
				qParams.put("P_NETWORK_ID", networkId);
			}

			if (caseTypeId != null && !caseTypeId.equals((long) FlagsEnum.ALL.getCode())) {
				whereClause.append(" and fu.domainIdCasesType = :P_CASE_TYPE_ID ");
				qParams.put("P_CASE_TYPE_ID", caseTypeId);
			}

			if (directFlag != FlagsEnum.ALL.getCode()) {
				whereClause.append(" and fu.directFlag = :P_DIRECT_FLAG ");
				qParams.put("P_DIRECT_FLAG", directFlag);
			}

			/************************************** CONVERSATION ******************************/

			if (conversationType != FlagsEnum.ALL.getCode()) {
				whereClause.append(" and conv.conversationType = :P_CONVERSATION_TYPE ");
				qParams.put("P_CONVERSATION_TYPE", conversationType);
			}

			if (conversationResult != FlagsEnum.ALL.getCode()) {
				whereClause.append(" and conv.conversationResult = :P_CONVERSATION_RESULT ");
				qParams.put("P_CONVERSATION_RESULT", conversationResult);
			}

			if (conversationToDate != null) {
				whereClause.append(" and conv.conversationDate <= PKG_CHAR_TO_DATE (:P_CONVERSATION_TO_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_CONVERSATION_TO_DATE", HijriDateService.getHijriDateString(conversationToDate));
			}

			if (conversationFromDate != null) {
				whereClause.append(" and conv.conversationDate >= PKG_CHAR_TO_DATE (:P_CONVERSATION_FROM_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_CONVERSATION_FROM_DATE", HijriDateService.getHijriDateString(conversationFromDate));
			}

			if (conversationLong != null) {
				whereClause.append(" and conv.coordinateLongitude = :P_CONVERSATION_LONG ");
				qParams.put("P_CONVERSATION_LONG", conversationLong);
			}

			if (conversationLat != null) {
				whereClause.append(" and conv.coordinateLatitude = :P_CONVERSATION_LAT ");
				qParams.put("P_CONVERSATION_LAT", conversationLat);
			}
			
			if (conversationDetails != null && !conversationDetails.isEmpty()) {
				whereClause.append(" and conv.conversationDetails like :P_CONVERSATION_DETAILS ");
				qParams.put("P_CONVERSATION_DETAILS", "%" + conversationDetails  + "%");
			}

			/************************************** CONVERSATION PARTY ************************/

			if (conversationPartyConNum != null) {
				fromClause.append(", ConversationData convD ");
				whereClause.append(" and ");
				whereClause.append("convD.followUpId = fu.id");

				fromClause.append(", ConversationPartyData convP ");
				whereClause.append(" and ");
				whereClause.append("convD.id = convP.conversationId");

				whereClause.append(" and convP.contactNumber = :P_CONVERSATION_PARTY_CON_NUM ");
				qParams.put("P_CONVERSATION_PARTY_CON_NUM", conversationPartyConNum);
			}

			/************************************** FOLLOW UP RESULT **************************/

			if (domainIdFollowResults != null && !domainIdFollowResults.equals((long) FlagsEnum.ALL.getCode())) {
				whereClause.append(" and res.domainIdFollowResults = :P_FOLLOW_RESULTS ");
				qParams.put("P_FOLLOW_RESULTS", domainIdFollowResults);
			}

			if (resultToDate != null) {
				whereClause.append(" and res.resultDate <= PKG_CHAR_TO_DATE (:P_RESULT_TO_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_RESULT_TO_DATE", HijriDateService.getHijriDateString(resultToDate));
			}

			if (resultFromDate != null) {
				whereClause.append(" and res.resultDate >= PKG_CHAR_TO_DATE (:P_RESULT_FROM_DATE, 'MI/MM/YYYY') ");
				qParams.put("P_RESULT_FROM_DATE", HijriDateService.getHijriDateString(resultFromDate));
			}

			/************************* JOIN WITH CONVERSATION ************************************/

			if (conversationType != FlagsEnum.ALL.getCode() || (conversationDetails != null && !conversationDetails.isEmpty()) || conversationResult != FlagsEnum.ALL.getCode() || conversationToDate != null || conversationFromDate != null || conversationLong != null || conversationLat != null) {
				fromClause.append(", ConversationData conv ");
				whereClause.append(" and ");
				whereClause.append("conv.followUpId = fu.id");
			}

			/************************* JOIN WITH FOLLOW UP RESULT ********************************/

			if ((domainIdFollowResults != null && !domainIdFollowResults.equals((long) FlagsEnum.ALL.getCode())) || resultToDate != null || resultFromDate != null) {
				fromClause.append(", FollowUpResultData res ");
				whereClause.append(" and ");
				whereClause.append("res.followUpId = fu.id");
			}

			/************************************** QUERY ***************************************/

			query.append(fromClause.toString() + whereClause.toString());
			result = DataAccess.executeNativeQuery(query.toString(), qParams);
			return result;
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FollowUpService.class, e, "FollowUpService");
			throw new BusinessException("error_DBError");
		}

	}
}
