package com.code.services.infosys.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.finance.FinDepSupportData;
import com.code.dal.orm.finance.FinDepSupportDetailData;
import com.code.dal.orm.finance.FinYearAccountSupport;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.dal.orm.setup.EmployeeData;
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
import com.code.services.util.HijriDateService;
import com.code.services.workflow.finance.RegionFinancialSupportWorkFlow;

public class FinanceAccountsService extends BaseService {
	private FinanceAccountsService() {
	}

	/**
	 * Add new financial year approval
	 * 
	 * @param loginUser
	 * @param finYearApproval
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addFinYearApproval(EmployeeData loginUser, FinYearApproval finYearApproval, CustomSession... useSession) throws BusinessException {
		validateFinYearApproval(finYearApproval);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finYearApproval.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(finYearApproval, session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate FinYearApproval: add values in valid range, divide all the balance
	 * 
	 * @param finYearApproval
	 * @throws BusinessException
	 */
	private static void validateFinYearApproval(FinYearApproval finYearApproval) throws BusinessException {
		if (finYearApproval.getRegionsInitialBalance() < 0 || finYearApproval.getRewardInitialBalance() < 0 || !(finYearApproval.getCurrentYearBalance() > 0)) {
			throw new BusinessException("error_balanceMoreThanZero");
		}
		double max = Math.pow(10, 27);
		if (finYearApproval.getRegionsInitialBalance() >= max || finYearApproval.getRewardInitialBalance() >= max || finYearApproval.getCurrentYearBalance() >= max) {
			throw new BusinessException("error_numberTooLarge");
		}
		if (finYearApproval.getRegionsInitialBalance() + finYearApproval.getRewardInitialBalance() != finYearApproval.getCurrentYearBalance() + calculateYearBalance(finYearApproval.getFinYear() - 1)) {
			throw new BusinessException("error_balanceNotDivided");
		}
	}

	/**
	 * Add new FinYearAccountSupport
	 * 
	 * @param loginUser
	 * @param accountSupport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addFinYearAccountSupport(EmployeeData loginUser, FinYearAccountSupport accountSupport, CustomSession... useSession) throws BusinessException {
		validateFinYearAccountSupport(accountSupport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			accountSupport.setSupportNumber(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.SUPPORT_FINANCE_ACCOUNTS.getEntityId(), Integer.MAX_VALUE, session).toString());
			accountSupport.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(accountSupport, session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * updateFinYearAccountSupport
	 * 
	 * @param loginUser
	 * @param accountSupport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateFinYearAccountSupport(EmployeeData loginUser, FinYearAccountSupport accountSupport, CustomSession... useSession) throws BusinessException {
		validateFinYearAccountSupport(accountSupport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			accountSupport.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(accountSupport, session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * deleteFinYearAccountSupport
	 * 
	 * @param loginUser
	 * @param accountSupport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteFinYearAccountSupport(EmployeeData loginUser, FinYearAccountSupport accountSupport, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			accountSupport.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.deleteEntity(accountSupport, session);

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else {
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate FinYearAccountSupport: support in a valid range
	 * 
	 * @param accountSupport
	 * @throws BusinessException
	 */
	private static void validateFinYearAccountSupport(FinYearAccountSupport accountSupport) throws BusinessException {
		if ((!(accountSupport.getRegionsSupportBalance() > 0) && !(accountSupport.getRewardSupportBalance() > 0))) {
			throw new BusinessException("error_supportMoreThanZero");
		}
		double max = Math.pow(10, 27);
		if (accountSupport.getRegionsSupportBalance() >= max || accountSupport.getRewardSupportBalance() >= max) {
			throw new BusinessException("error_numberTooLarge");
		}
	}

	/**
	 * Add new Department Account Support
	 * 
	 * @param loginUser
	 * @param finDepSupport
	 * @param finDepSupportDetailList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addDepartmentAccountSupport(EmployeeData loginUser, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> finDepSupportDetailList, CustomSession... useSession) throws BusinessException {
		validateFinDepSupport(finDepSupportDetailList, finDepSupport.getFinYear());

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finDepSupport.setRequestNumber(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.FIN_DEP_SUPPORT.getEntityId(), Integer.MAX_VALUE, session).toString());
			finDepSupport.getFinDepSupport().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(finDepSupport.getFinDepSupport(), session);
			finDepSupport.setId(finDepSupport.getFinDepSupport().getId());

			for (FinDepSupportDetailData detail : finDepSupportDetailList) {
				detail.setDepartmentSupportId(finDepSupport.getId());
				detail.getFinDepSupportDetail().setSystemUser(loginUser.getEmpId().toString());
				DataAccess.addEntity(detail.getFinDepSupportDetail(), session);
			}

			RegionFinancialSupportWorkFlow.initRegionFinancialSupport(finDepSupport, loginUser, null, session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate department support (support at least one department - balance is enough - support is in range - department id exists)
	 * 
	 * @param finDepSupportDetailList
	 * @param year
	 * @throws BusinessException
	 */
	private static void validateFinDepSupport(List<FinDepSupportDetailData> finDepSupportDetailList, int year) throws BusinessException {
		if (finDepSupportDetailList.isEmpty()) {
			throw new BusinessException("error_atLeastOneDepartment");
		}
		double total = 0;
		double max = Math.pow(10, 27);

		for (FinDepSupportDetailData detail : finDepSupportDetailList) {
			if (!(detail.getDepartmentId() > 0)) {
				throw new BusinessException("error_departmentMandatory");
			}
			if (!(detail.getSupportAmount() > 0)) {
				throw new BusinessException("error_supportMoreThanZero");
			}
			if (detail.getSupportAmount() >= max) {
				throw new BusinessException("error_numberTooLarge");
			}
			total += detail.getSupportAmount();
		}
		if (total > calculateDepartmentsSupportNetBalance(year)) {
			throw new BusinessException("error_balanceIsNotEnough");
		}
	}

	/**
	 * Update department account support
	 * 
	 * @param loginUser
	 * @param finDepSupport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateDepartmentAccountSupport(EmployeeData loginUser, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> finDepSupportDetailList, CustomSession... useSession) throws BusinessException {
		validateFinDepSupport(finDepSupportDetailList, finDepSupport.getFinYear());

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finDepSupport.getFinDepSupport().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(finDepSupport.getFinDepSupport(), session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Add Department Account Support Detail
	 * 
	 * @param loginUser
	 * @param finDepSupportDetail
	 * @param finDepSupport
	 * @param finDepSupportDetailList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addDepartmentAccountSupportDetail(EmployeeData loginUser, FinDepSupportDetailData finDepSupportDetail, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> finDepSupportDetailList, CustomSession... useSession) throws BusinessException {
		validateFinDepSupport(finDepSupportDetailList, finDepSupport.getFinYear());

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finDepSupportDetail.getFinDepSupportDetail().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(finDepSupportDetail.getFinDepSupportDetail(), session);
			finDepSupportDetail.setId(finDepSupportDetail.getFinDepSupportDetail().getId());

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update Department Account Support Detail
	 * 
	 * @param loginUser
	 * @param finDepSupportDetail
	 * @param finDepSupport
	 * @param finDepSupportDetailList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateDepartmentAccountSupportDetail(EmployeeData loginUser, FinDepSupportDetailData finDepSupportDetail, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> finDepSupportDetailList, CustomSession... useSession) throws BusinessException {
		validateFinDepSupport(finDepSupportDetailList, finDepSupport.getFinYear());

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finDepSupportDetail.getFinDepSupportDetail().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(finDepSupportDetail.getFinDepSupportDetail(), session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete Department Account Support Detail
	 * 
	 * @param loginUser
	 * @param finDepSupportDetail
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteDepartmentAccountSupportDetail(EmployeeData loginUser, FinDepSupportDetailData finDepSupportDetail, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finDepSupportDetail.getFinDepSupportDetail().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.deleteEntity(finDepSupportDetail.getFinDepSupportDetail(), session);

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
				Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Calculate the balance for a certain year
	 * 
	 * @param year
	 * @return year balance
	 * @throws BusinessException
	 */
	public static double calculateYearBalance(int year) throws BusinessException {
		return calculateMonthlyRewardNetBalance(year) + calculateDepartmentsSupportNetBalance(year);
	}

	/**
	 * Calculate the monthly reward net balance for a certain year
	 * 
	 * @param year
	 * @return year balance
	 * @throws BusinessException
	 */
	public static double calculateMonthlyRewardNetBalance(int year) throws BusinessException {
		try {
			FinYearApproval finYearApproval = searchFinYearApproval(FlagsEnum.ALL.getCode(), year).get(0);
			return calculateMonthlyRewardCurrentYearBalance(year) - sumAllMonthlyRewardsTotalSpent(finYearApproval.getId());
		} catch (NoDataException e) {
		}
		return 0.0;
	}

	/**
	 * Calculate the monthly reward balance for a certain year
	 * 
	 * @param year
	 * @return year balance
	 * @throws BusinessException
	 */
	public static double calculateMonthlyRewardCurrentYearBalance(int year) throws BusinessException {
		try {
			FinYearApproval finYearApproval = searchFinYearApproval(FlagsEnum.ALL.getCode(), year).get(0);
			double rewardNet = finYearApproval.getRewardInitialBalance();
			try {
				Object[] recordList = sumFinYearSupport(finYearApproval.getId()).toArray();
				Object sum = ((Object[]) recordList[0])[0];
				if (sum != null) {
					rewardNet += (Double) sum;
				}
			} catch (NoDataException e) {
			}
			return rewardNet;
		} catch (NoDataException e) {
		}
		return 0.0;
	}

	/**
	 * Calculate the department support balance for a certain year
	 * 
	 * @param year
	 * @return year balance
	 * @throws BusinessException
	 */
	public static double calculateDepartmentsSupportNetBalance(int year) throws BusinessException {
		try {
			FinYearApproval finYearApproval = searchFinYearApproval(FlagsEnum.ALL.getCode(), year).get(0);
			double regionsNet = finYearApproval.getRegionsInitialBalance();
			try {
				Object[] recordList = sumFinYearSupport(finYearApproval.getId()).toArray();
				Object sum = ((Object[]) recordList[0])[1];
				if (sum != null) {
					regionsNet += (Double) sum;
				}
			} catch (NoDataException e) {
			}
			regionsNet -= sumAllDepSupportAmount(finYearApproval.getId());

			return regionsNet;
		} catch (NoDataException e) {
		}
		return 0.0;
	}

	/**
	 * calculateFinYearAcoountSum
	 * 
	 * @param finYearApprovalId
	 * @return
	 * @throws BusinessException
	 */
	public static double calculateFinYearAcoountSum(long finYearApprovalId) throws BusinessException {
		Double sumTotal = 0.0;
		try {
			Object[] recordList = sumFinYearSupport(finYearApprovalId).toArray();
			Object sumReward = ((Object[]) recordList[0])[0];
			Object sumRegions = ((Object[]) recordList[0])[1];
			if (sumReward != null) {
				sumTotal += (Double) sumReward;
			}

			if (sumRegions != null) {
				sumTotal += (Double) sumRegions;
			}
		} catch (NoDataException e) {
		}
		return sumTotal;
	}

	/**
	 * Calculate Department support Net Balance for a certain department
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return net balance
	 * @throws BusinessException
	 */
	public static double calculateDepartmentSupportNetBalance(long departmentId, long finYearApprovalId) throws BusinessException {
		return sumAllDepSupportAmount(departmentId, finYearApprovalId) - InfoRewardPaymentService.getSumInfoRewardAmount(departmentId, finYearApprovalId);
	}
	
	/**
	 * Get Department Support Report
	 * 
	 * @param balance
	 * @param hijriYears
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getDepartmentSupportDetailsReportBytes(long departmentSupportId, double balance, String hijriYears, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_DEPARTMENT_SUPORT_ID", departmentSupportId);
			parameters.put("P_BALANCE", balance);
			parameters.put("P_HIJRI_YEARS", hijriYears);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.DEPARTMENT_ACCOUNT_SUPPORT_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_general");
		}
	}
	
	/**
	 * Get Department Support Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getDepartmentSupportReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_START_DATE", startDateString);
			parameters.put("P_END_DATE", endDateString);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.DEPARTMENT_ACCOUNT_SUPPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Statistical Department Support Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getStatisticalDepartmentSupportReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, String loginEmployeeName) throws BusinessException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_START_DATE", startDateString == null ? HijriDateService.getHijriSysDateString() : startDateString);
			parameters.put("P_START_DATE_NULL", startDateString == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_END_DATE", endDateString == null ? HijriDateService.getHijriSysDateString() : endDateString);
			parameters.put("P_END_DATE_NULL", endDateString == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_SECTOR_ID", sectorId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			String reportName = ReportNamesEnum.DEPARTMENT_ACCOUNT_SUPPORT_STATISTICAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getFinanceSupportReportBytes
	 * 
	 * @param year
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getFinanceSupportReportBytes(long year, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_YEAR", year);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.FINANCE_ACCOUNT_SUPPORT_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_general");
		}
	}

	/**********************************************/
	/******************* Queries *****************/
	/**
	 * Get the FinYearApproval by id
	 * 
	 * @param id
	 * @return the FinYearApproval by id
	 * @throws BusinessException
	 */
	public static FinYearApproval getFinYearApprovalById(long id) throws BusinessException {
		try {
			return searchFinYearApproval(id, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get the FinYearApproval for a certain year
	 * 
	 * @param year
	 * @return the FinYearApproval for a certain year
	 * @throws BusinessException
	 */
	public static FinYearApproval getFinYearApproval(int year) throws BusinessException {
		try {
			return searchFinYearApproval(FlagsEnum.ALL.getCode(), year).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get the FinYearApproval for a certain year or by id
	 * 
	 * @param year
	 * @param id
	 * @return list of FinYearApproval
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinYearApproval> searchFinYearApproval(long id, int year) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_YEAR", year);
			return DataAccess.executeNamedQuery(FinYearApproval.class, QueryNamesEnum.FIN_YEAR_APPROVAL_SEARCH_FIN_YEAR_APPROVAL.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all years saved in FinYearApproval
	 * 
	 * @return list of years
	 * @throws BusinessException
	 */
	public static List<Long> getAllFinYears() throws BusinessException {
		try {
			return searchFinYearApproval();
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get all years saved in FinYearApproval
	 * 
	 * @return list of years
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Long> searchFinYearApproval() throws BusinessException, NoDataException {
		try {
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.FIN_YEAR_APPROVAL_SEARCH_FIN_YEARS.getCode(), null);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * getFinYearAccountSupport
	 * 
	 * @param finYearApprovalId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinYearAccountSupport> getFinYearAccountSupport(long finYearApprovalId, Date fromDate, Date toDate) throws BusinessException {
		try {
			return searchFinYearAccountSupport(finYearApprovalId, fromDate, toDate);
		} catch (NoDataException e) {
			return new ArrayList<FinYearAccountSupport>();
		}
	}

	/**
	 * searchFinYearAccountSupport
	 * 
	 * @param finYearApprovalId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinYearAccountSupport> searchFinYearAccountSupport(long finYearApprovalId, Date fromDate, Date toDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			return DataAccess.executeNamedQuery(FinYearAccountSupport.class, QueryNamesEnum.FIN_YEAR_ACCOUNT_SUPPORT_SEARCH_FIN_YEAR_SUPPORT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get finDepSupport by wfInstanceId
	 * 
	 * @param wfInstanceId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 */
	public static FinDepSupportData getFinDepSupport(long wfInstanceId) throws BusinessException {
		try {
			return searchFinDepSupport(wfInstanceId).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get FinDepSupport by wfInstanceId
	 * 
	 * @param wfInstanceId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinDepSupportData> searchFinDepSupport(long wfInstanceId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_WF_INSTANCE_ID", wfInstanceId);
			return DataAccess.executeNamedQuery(FinDepSupportData.class, QueryNamesEnum.FIN_DEP_SUPPORT_DATA_SEARCH_FIN_DEP_SUPPORT_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all FinDepSupportDetailData for finDepSupport
	 * 
	 * @param finDepSupportId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 */
	public static List<FinDepSupportDetailData> getFinDepSupportDetail(long finDepSupportId) throws BusinessException {
		try {
			return searchFinDepSupportDetail(finDepSupportId);
		} catch (NoDataException e) {
			return new ArrayList<FinDepSupportDetailData>();
		}
	}

	/**
	 * Get all FinDepSupportDetailData for finDepSupport
	 * 
	 * @param finDepSupportId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinDepSupportDetailData> searchFinDepSupportDetail(long finDepSupportId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_DEP_SUPPORT_ID", finDepSupportId);
			return DataAccess.executeNamedQuery(FinDepSupportDetailData.class, QueryNamesEnum.FIN_DEP_SUPPORT_DETAIL_DATA_SEARCH_FIN_DEP_SUPPORT_DETAIL_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get all FinDepSupportDetailData for department
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 */
	public static List<FinDepSupportDetailData> getFinDepSupportDetailByDepartment(long departmentId, long finYearApprovalId) throws BusinessException {
		try {
			return searchFinDepSupportDetailByDepartment(departmentId, finYearApprovalId);
		} catch (NoDataException e) {
			return new ArrayList<FinDepSupportDetailData>();
		}
	}

	/**
	 * Get all FinDepSupportDetailData for department
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return list of FinDepSupportDetailData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinDepSupportDetailData> searchFinDepSupportDetailByDepartment(long departmentId, long finYearApprovalId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			return DataAccess.executeNamedQuery(FinDepSupportDetailData.class, QueryNamesEnum.FIN_DEP_SUPPORT_DETAIL_DATA_SEARCH_FIN_DEP_SUPPORT_DETAIL_DATA_BY_DEPARTMENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Sum of FinYearSupport for a certain year
	 * 
	 * @param finYearApprovalId
	 * @return Sum of FinYearSupport for a certain year(reward - regions)
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Object> sumFinYearSupport(long finYearApprovalId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			return DataAccess.executeNamedQuery(Object.class, QueryNamesEnum.FIN_YEAR_ACCOUNT_SUPPORT_SUM_FIN_YEAR_SUPPORT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Sum of Monthly Reward for a certain year
	 * 
	 * @param finYearApprovalId
	 * @return Sum of Monthly Reward for a certain year
	 * @throws BusinessException
	 */
	public static Double sumAllMonthlyRewardsTotalSpent(long finYearApprovalId) throws BusinessException {
		try {
			List<Double> sumList = sumMonthlyRewardsTotalSpent(finYearApprovalId);
			return sumList.get(0) == null ? 0.0D : sumList.get(0);
		} catch (NoDataException e) {
			return 0.0D;
		}
	}

	/**
	 * Sum of Monthly Reward for a certain year
	 * 
	 * @param finYearApprovalId
	 * @return Sum of Monthly Reward for a certain year
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Double> sumMonthlyRewardsTotalSpent(long finYearApprovalId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			return DataAccess.executeNamedQuery(Double.class, QueryNamesEnum.FIN_MONTHLY_REW_DEP_DETAIL_SUM_FIN_MONTHLY_REWARD_TOTAL_SPENT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Sum of Department support for a certain year
	 * 
	 * @param finYearApprovalId
	 * @return Sum of Department support for a certain year
	 * @throws BusinessException
	 */
	public static Double sumAllDepSupportAmount(long finYearApprovalId) throws BusinessException {
		try {
			List<Double> sumList = sumDepSupportAmount(finYearApprovalId, FlagsEnum.ALL.getCode());
			return sumList.get(0) == null ? 0.0D : sumList.get(0);
		} catch (NoDataException e) {
			return 0.0D;
		}
	}

	/**
	 * Sum of Department support for a department in a certain year
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return Sum of Department support for a department in a certain year
	 * @throws BusinessException
	 */
	public static Double sumAllDepSupportAmount(long departmentId, long finYearApprovalId) throws BusinessException {
		try {
			List<Double> sumList = sumDepSupportAmount(finYearApprovalId, departmentId);
			return sumList.get(0) == null ? 0.0D : sumList.get(0);
		} catch (NoDataException e) {
			return 0.0D;
		}
	}

	/**
	 * Sum of Department support for a certain year
	 * 
	 * @param finYearApprovalId
	 * @return Sum of Department support for a certain year
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<Double> sumDepSupportAmount(long finYearApprovalId, long departmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			return DataAccess.executeNamedQuery(Double.class, QueryNamesEnum.FIN_DEP_SUPPORT_DETAIL_DATA_SUM_FIN_DEP_SUPPORT_AMOUNT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(FinanceAccountsService.class, e, "FinanceAccountsService");
			throw new BusinessException("error_DBError");
		}
	}
}