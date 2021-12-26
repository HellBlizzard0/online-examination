package com.code.services.infosys.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.finance.FinMonthlyRewDepDetail;
import com.code.dal.orm.finance.FinMonthlyReward;
import com.code.dal.orm.finance.FinMonthlyRewardResourceData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FinMonthlyRewardStatusEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public class MonthlyRewardPaymentService extends BaseService {
	private MonthlyRewardPaymentService() {
	}

	/**
	 * 
	 * @param finMonthlyReward
	 * @param finMonthlyRewDepDetailList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveFinMonthlyRewardPayment(FinMonthlyReward finMonthlyReward, List<FinMonthlyRewDepDetail> finMonthlyRewDepDetailList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		validateFinMonthlyReward(finMonthlyReward);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finMonthlyReward.setSystemUser(loginUser.getEmpId().toString());
			finMonthlyReward.setStatus(FinMonthlyRewardStatusEnum.UNDER_PROCESSING.getCode());
			finMonthlyReward.setRequestDate(HijriDateService.getHijriSysDate());

			DataAccess.addEntity(finMonthlyReward, session);

			for (FinMonthlyRewDepDetail finMonthlyRewDepDetail : finMonthlyRewDepDetailList) {
				finMonthlyRewDepDetail.setSystemUser(loginUser.getEmpId().toString());
				finMonthlyRewDepDetail.setMonthlyRewardId(finMonthlyReward.getId());
				DataAccess.addEntity(finMonthlyRewDepDetail, session);

				for (FinMonthlyRewardResourceData finMonthlyRewardResourceData : finMonthlyRewDepDetail.getFinMonthlyRewardResourceDataList()) {
					finMonthlyRewardResourceData.getFinMonthlyRewardResource().setSystemUser(loginUser.getEmpId().toString());
					finMonthlyRewardResourceData.setMonthlyRewardDeptDetailId(finMonthlyRewDepDetail.getId());
					DataAccess.addEntity(finMonthlyRewardResourceData.getFinMonthlyRewardResource(), session);
				}
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			finMonthlyReward.setId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
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
	 * @param finMonthlyReward
	 * @throws BusinessException
	 */
	private static void validateFinMonthlyReward(FinMonthlyReward finMonthlyReward) throws BusinessException {
		if (!getFinMonthlyReward(finMonthlyReward.getMonthNumber(), finMonthlyReward.getFinYearApprovalId(), FinMonthlyRewardStatusEnum.APPROVED.getCode(), finMonthlyReward.getHijriYear()).isEmpty()) {
			throw new BusinessException("error_monthIsExist");
		}
		if (!getFinMonthlyReward(finMonthlyReward.getMonthNumber(), finMonthlyReward.getFinYearApprovalId(), FinMonthlyRewardStatusEnum.UNDER_PROCESSING.getCode(), finMonthlyReward.getHijriYear()).isEmpty()) {
			throw new BusinessException("error_monthIsExist");
		}
		if (finMonthlyReward.getTotalSpent().compareTo(finMonthlyReward.getAccountBalance()) > 0) {
			throw new BusinessException("error_balanceIsNotEnough");
		}
	}

	/**
	 * 
	 * @param finMonthlyReward
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateFinMonthlyReward(FinMonthlyReward finMonthlyReward, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finMonthlyReward.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(finMonthlyReward, session);

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
				Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
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
	 * @param finMonthlyRewardResourceDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateFinMonthlyRewardResourceData(List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (FinMonthlyRewardResourceData recource : finMonthlyRewardResourceDataList) {
				recource.getFinMonthlyRewardResource().setSystemUser(loginUser.getEmpId().toString());
				DataAccess.updateEntity(recource.getFinMonthlyRewardResource(), session);
			}

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
				Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * getMonthlyRewardReportBytes
	 * 
	 * @param regionId
	 * @param sectorId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMonthlyRewardReportBytes(String regionName, long regionId, long sectorId, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_MONTHLY_REWARD_STATUS", FinMonthlyRewardStatusEnum.APPROVED.getCode());
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.MONTHLY_REWARD_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getMonthlyRewardStatisticsReportBytes
	 * 
	 * @param regionId
	 * @param sectorId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMonthlyRewardStatisticsReportBytes(String regionName, long regionId, long sectorId, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_MONTHLY_REWARD_STATUS", FinMonthlyRewardStatusEnum.APPROVED.getCode());
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.MONTHLY_REWARD_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getMonthlyRewardByIbanOrCheckReportBytes
	 * 
	 * @param monthlyRewardId
	 * @param method
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMonthlyRewardByIBANOrChequeReportBytes(long monthlyRewardId, int method, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_MONTHLY_REWARD_ID", monthlyRewardId);
			parameters.put("P_PAYMENT_METHOD", method);
			if (PaymentMethodEnum.CHEQUE.getCode().equals(method)) {
				parameters.put("P_PAYMENT_METHOD_CASH", PaymentMethodEnum.CASH.getCode());
			}
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = PaymentMethodEnum.BANK_ACCOUNT.getCode().equals(method) ? ReportNamesEnum.MONTHLY_REWARD_REPORT_BY_IBAN.getCode() : ReportNamesEnum.MONTHLY_REWARD_REPORT_BY_CHEQUE.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get MonthlyReward Details Report
	 * 
	 * @param monthlyRewardId
	 * @param finYear
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMonthlyRewardDetailsReportBytes(long monthlyRewardId, String finYear, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_MONTHLY_REWARD_ID", monthlyRewardId);
			parameters.put("P_YEAR", finYear);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			reportName = ReportNamesEnum.MONTHLY_REWARD_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * 
	 * @param monthNumber
	 * @param finYearApprovalId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinMonthlyReward> getFinMonthlyReward(int monthNumber, long finYearApprovalId, int status, int hijriYear) throws BusinessException {
		try {
			return searchFinMonthlyReward(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), monthNumber, finYearApprovalId, status, hijriYear);
		} catch (NoDataException e) {
			return new ArrayList<FinMonthlyReward>();
		}
	}

	/**
	 * 
	 * @param instanceId
	 * @return
	 * @throws BusinessException
	 */
	public static FinMonthlyReward getFinMonthlyRewardByInstanceId(long instanceId) throws BusinessException {
		try {
			return searchFinMonthlyReward(FlagsEnum.ALL.getCode(), instanceId, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * 
	 * @param id
	 * @param instanceId
	 * @param monthNumber
	 * @param finYearApprovalId
	 * @return
	 * @throws NoDataException
	 * @throws BusinessException
	 */
	private static List<FinMonthlyReward> searchFinMonthlyReward(long id, long instanceId, int monthNumber, long finYearApprovalId, int status, int hijriYear) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_MONTH_NUMBER", monthNumber);
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			qParams.put("P_STATUS", status);
			qParams.put("P_HIJRI_YEAR", hijriYear);
			return DataAccess.executeNamedQuery(FinMonthlyReward.class, QueryNamesEnum.FIN_MONTHLY_REWARD_SEARCH_FIN_MONTHLY_REWARD.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param finMonthlyRewardId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinMonthlyRewDepDetail> getFinMonthlyRewDepDetail(long finMonthlyRewardId) throws BusinessException {
		try {
			return searchFinMonthlyRewDepDetail(finMonthlyRewardId);
		} catch (NoDataException e) {
			return new ArrayList<FinMonthlyRewDepDetail>();
		}
	}

	/**
	 * 
	 * @param finMonthlyRewardId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinMonthlyRewDepDetail> searchFinMonthlyRewDepDetail(long finMonthlyRewardId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_MONTHLY_REWARD_ID", finMonthlyRewardId);
			return DataAccess.executeNamedQuery(FinMonthlyRewDepDetail.class, QueryNamesEnum.FIN_MONTHLY_REW_DEP_DETAIL_SEARCH_FIN_MONTHLY_REW_DEP_DETAIL.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param agentId
	 * @param officerId
	 * @param regionId
	 * @param sectorId
	 * @param month
	 * @param year
	 * @param monthlyRewardStatus
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinMonthlyRewardResourceData> getFinMonthlyRewardResourceData(Long agentId, Long officerId, Long regionId, Long sectorId, int month, int year, Integer monthlyRewardStatus) throws BusinessException {
		try {
			return searchFinMonthlyRewardResourceData(agentId == null ? FlagsEnum.ALL.getCode() : agentId, officerId == null ? FlagsEnum.ALL.getCode() : officerId, null, regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, month, year, monthlyRewardStatus == null ? FlagsEnum.ALL.getCode() : monthlyRewardStatus, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<FinMonthlyRewardResourceData>();
		}
	}

	/**
	 * get All FinMonthlyRewardResourceData By Agent Id
	 * 
	 * @param agentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinMonthlyRewardResourceData> getAllFinMonthlyRewardResourceDataByAgentId(long agentId, int month, int hijriYear) throws BusinessException {
		try {
			return searchFinMonthlyRewardResourceData(agentId, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), month, FlagsEnum.ALL.getCode(), FinMonthlyRewardStatusEnum.APPROVED.getCode(), hijriYear);
		} catch (NoDataException e) {
			return new ArrayList<FinMonthlyRewardResourceData>();
		}
	}

	/**
	 * 
	 * @param officerId
	 * @param identity
	 * @param month
	 * @param hijriYear
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinMonthlyRewardResourceData> getAllFinMonthlyRewardResourceDataByOfficerIdAndIdentity(Long officerId, String identity, int month, int hijriYear) throws BusinessException {
		try {
			return searchFinMonthlyRewardResourceData(FlagsEnum.ALL.getCode(), officerId == null ? FlagsEnum.ALL.getCode() : officerId, identity, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), month, FlagsEnum.ALL.getCode(), FinMonthlyRewardStatusEnum.APPROVED.getCode(), hijriYear);
		} catch (NoDataException e) {
			return new ArrayList<FinMonthlyRewardResourceData>();
		}
	}

	/**
	 * 
	 * @param agentId
	 * @param officerId
	 * @param regionId
	 * @param sectorId
	 * @param month
	 * @param year
	 * @param monthlyRewardStatus
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinMonthlyRewardResourceData> searchFinMonthlyRewardResourceData(long agentId, long officerId, String identity, long regionId, long sectorId, int month, int year, int monthlyRewardStatus, int hijriYear) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_AGENT_ID", agentId);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_SECTOR_ID", sectorId);
			qParams.put("P_MONTH", month);
			qParams.put("P_YEAR", year);
			qParams.put("P_IDENTITY", (identity == null || identity.isEmpty()) ? FlagsEnum.ALL.getCode() : identity);
			qParams.put("P_HIJRI_YEAR", hijriYear);
			qParams.put("P_MONTHLY_REWARD_STATUS", monthlyRewardStatus);
			return DataAccess.executeNamedQuery(FinMonthlyRewardResourceData.class, QueryNamesEnum.FIN_MONTHLY_REWARD_RESOURCE_DATA_SEARCH_FIN_MONTHLY_REWARDRE_SOURCE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MonthlyRewardPaymentService.class, e, "MonthlyRewardPaymentService");
			throw new BusinessException("error_DBError");
		}
	}
}
