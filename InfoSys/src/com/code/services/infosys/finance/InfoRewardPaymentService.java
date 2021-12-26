package com.code.services.infosys.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.finance.FinInfoReward;
import com.code.dal.orm.finance.FinInfoRewardDetail;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;

public class InfoRewardPaymentService extends BaseWorkFlow {
	private InfoRewardPaymentService() {
	}

	/**
	 * 
	 * @param finInfoReward
	 * @param infoDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveFinInfoReward(FinInfoReward finInfoReward, List<InfoData> infoDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		validateFinInfoReward(finInfoReward);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			finInfoReward.setSystemUser(loginUser.getEmpId().toString());
			finInfoReward.setPaymentDate(HijriDateService.getHijriSysDate());
			finInfoReward.setPaymentSerial(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.INFO_REWARD.getEntityId(), Integer.MAX_VALUE, session));
			DataAccess.addEntity(finInfoReward, session);

			for (InfoData infoData : infoDataList) {
				FinInfoRewardDetail newFinInfoRewardDetail = new FinInfoRewardDetail();
				newFinInfoRewardDetail.setSystemUser(loginUser.getEmpId().toString());
				newFinInfoRewardDetail.setInfoRewardId(finInfoReward.getId());
				newFinInfoRewardDetail.setInfoId(infoData.getId());
				DataAccess.addEntity(newFinInfoRewardDetail, session);
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
				Log4j.traceErrorException(InfoRewardPaymentService.class, e, "InfoRewardPaymentService");
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
	 * @param finInfoReward
	 * @throws BusinessException
	 */
	private static void validateFinInfoReward(FinInfoReward finInfoReward) throws BusinessException {
		if ((finInfoReward.getAssignmentDetailId() == null) || finInfoReward.getDepartmentId() == null || finInfoReward.getAmount() == null || finInfoReward.getPaymentReason() == null || finInfoReward.getPaymentReason().trim().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
		if (finInfoReward.getAmount().compareTo(FinanceAccountsService.calculateDepartmentSupportNetBalance(finInfoReward.getDepartmentId(), finInfoReward.getFinYearApprovalId())) > 0) {
			throw new BusinessException("error_balanceIsNotEnough");
		}
	}

	/**
	 * Get Info Reward Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param loginEmployeeName
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getInfoRewardReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, String loginEmployeeName, int assignmentType) throws BusinessException {
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
			parameters.put("P_ASSIGNMENT_TYPE", assignmentType);
			String reportName = ReportNamesEnum.INFO_REWARD.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoRewardPaymentService.class, e, "InfoRewardPaymentService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Statistical Info Reward Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param regionId
	 * @param regionName
	 * @param sectorId
	 * @param loginEmployeeName
	 * @param assignmentType
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getStatisticalInfoRewardReportBytes(String startDateString, String endDateString, long regionId, String regionName, long sectorId, String loginEmployeeName, int assignmentType) throws BusinessException {
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
			parameters.put("P_ASSIGNMENT_TYPE", assignmentType);
			String reportName = ReportNamesEnum.INFO_REWARD_STATISTICAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(InfoRewardPaymentService.class, e, "InfoRewardPaymentService");
			throw new BusinessException("error_general");
		}
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return
	 * @throws BusinessException
	 */
	public static double getSumInfoRewardAmount(Long departmentId, Long finYearApprovalId) throws BusinessException {
		try {
			Double sum = sumInfoRewardAmount(departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, finYearApprovalId == null ? FlagsEnum.ALL.getCode() : finYearApprovalId);
			return sum == null ? 0.0 : sum;
		} catch (NoDataException e) {
			return 0.0;
		}
	}

	/**
	 * 
	 * @param departmentId
	 * @param finYearApprovalId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static Double sumInfoRewardAmount(long departmentId, long finYearApprovalId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_FIN_YEAR_APPROVAL_ID", finYearApprovalId);
			return DataAccess.executeNamedQuery(Double.class, QueryNamesEnum.FIN_INFO_REWARD_SUM_FIN_INFO_REWARD.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoRewardPaymentService.class, e, "InfoRewardPaymentService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param officerId
	 * @param identity
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinInfoReward> getAllInfoRewardByOfficerIdAndIdentity(long officerId, String identity, Date fromDate, Date toDate) throws BusinessException {
		try {
			return searchInfoReward(FlagsEnum.ALL.getCode(), officerId, identity, fromDate, toDate);
		} catch (NoDataException e) {
			return new ArrayList<FinInfoReward>();
		}
	}

	/**
	 * 
	 * @param agentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<FinInfoReward> getAllInfoRewardByAgentId(long agentId, Date fromDate, Date toDate) throws BusinessException {
		try {
			return searchInfoReward(agentId, FlagsEnum.ALL.getCode(), null, fromDate, toDate);
		} catch (NoDataException e) {
			return new ArrayList<FinInfoReward>();
		}
	}

	/**
	 * search info reward
	 * 
	 * @param agentId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<FinInfoReward> searchInfoReward(long agentId, long officerId, String identity, Date fromDate, Date toDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_AGENT_ID", agentId);
			qParams.put("P_OFFICER_ID", officerId);
			qParams.put("P_IDENTITY", identity == null ? FlagsEnum.ALL.getCode() : identity);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			return DataAccess.executeNamedQuery(FinInfoReward.class, QueryNamesEnum.FIN_INFO_REWARD_SEARCH_FIN_INFO_REWARD.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(InfoRewardPaymentService.class, e, "InfoRewardPaymentService");
			throw new BusinessException("error_DBError");
		}
	}
}
