package com.code.services.infosys.labcheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.HCMLabCheckStatusEnum;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.integration.promotionsdrugstest.PromotionsDrugsTestWS;
import com.code.integration.promotionsdrugstest.PromotionsDrugsTestWSService;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.labcheck.LabCheckWorkFlow;

public class LabCheckService extends BaseService {

	// private static final String ORDERNUMBER_REGEX = "[a-zA-Z0-9\\\\/]+"; // support English characters , numbers, / or \
	private static final String ORDERNUMBER_ARABIC_REGEX = "[\\u0621-\\u064A0-9\\u0660-\\u0669\\\\/]+"; // support Arabic characters , Arabic and English numbers, / or \

	/**
	 * Save lab check with details
	 * 
	 * @param loginEmpData
	 * @param labCheck
	 * @param employeeDataList
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveLabCheck(LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheck(labCheck, employeeDataList);

		boolean isSave = false;
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			labCheck.setSystemUser(loginEmpData.getEmpId().toString());
			if (labCheck.getId() == null) {
				DataAccess.addEntity(labCheck, session);
				isSave = true;
			} else {
				DataAccess.updateEntity(labCheck, session);
			}

			for (LabCheckEmployeeData emp : employeeDataList) {
				emp.setLabCheckId(labCheck.getId());
				emp.setOrderDate(labCheck.getOrderDate());
				if (emp.getId() == null) {
					saveLabCheckEmployee(emp, loginEmpData, session);
				} else if (labCheck.isIntegration() && emp.getId() != null) {
					updateLabCheckEmployee(emp, loginEmpData, session);
				}
			}

			if (!labCheck.isIntegration()) {
				checkEmployeesLabCheckUnderProcess(employeeDataList);
				checkEmployeesLabCheckBeforeRetestDate(employeeDataList);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			if (isSave) {
				labCheck.setId(null);
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Save new lab check employee
	 * 
	 * @param labCheckEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveLabCheckEmployee(LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			labCheckEmployeeData.getLabCheckEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			if (labCheckEmployeeData.getRetestNumber() == null) {
				labCheckEmployeeData.setRetestNumber(1);
			}
			labCheckEmployeeData.setRetested(false);
			DataAccess.addEntity(labCheckEmployeeData.getLabCheckEmployee(), session);
			labCheckEmployeeData.setId(labCheckEmployeeData.getLabCheckEmployee().getId());
			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			labCheckEmployeeData.setId(null);
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_repeatedValue");
			} else {
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Add new lab check employee after saving lab check
	 * 
	 * @param labCheck
	 * @param labCheckEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addLabCheckEmployee(LabCheck labCheck, LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		if (!getLabCheckEmployeesByLabCheckEmpIdAndLabCheckIdAndRetestNo(labCheckEmployeeData.getEmployeeId(), labCheckEmployeeData.getLabCheckId(), 1).isEmpty())
			throw new BusinessException("error_labCheckEmployeeAddedBefore", new Object[] { labCheckEmployeeData.getEmployeeFullName(), labCheck.getOrderNumber() });

		if (!labCheck.isIntegration()) {
			List<LabCheckEmployeeData> empList = new ArrayList<>();
			empList.add(labCheckEmployeeData);
			checkEmployeesLabCheckUnderProcess(empList);
			checkEmployeesLabCheckBeforeRetestDate(empList);
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			labCheckEmployeeData.getLabCheckEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			if (labCheckEmployeeData.getRetestNumber() == null) {
				labCheckEmployeeData.setRetestNumber(1);
			}
			labCheckEmployeeData.setRetested(false);
			DataAccess.addEntity(labCheckEmployeeData.getLabCheckEmployee(), session);
			labCheckEmployeeData.setId(labCheckEmployeeData.getLabCheckEmployee().getId());
			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			labCheckEmployeeData.setId(null);
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_repeatedValue");
			} else {
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * check if employee exist in other labcheck with some labcheck status
	 * 
	 * @param labCheckEmployeeData
	 * @throws BusinessException
	 */
	public static void checkEmployeesLabCheckUnderProcess(List<LabCheckEmployeeData> labCheckEmployeeData) throws BusinessException {
		String employees = "";
		for (LabCheckEmployeeData emp : labCheckEmployeeData) {
			employees += validateLabCheckEmployee(emp);
		}

		if (!employees.isEmpty()) {
			Object[] Params = new Object[1];
			Params[0] = employees;
			throw new BusinessException("error_checkUnderProcess", Params);
		}
	}

	/**
	 * check if the order date is before employee retest date with positive result
	 * 
	 * @param labCheckEmployeeData
	 * @throws BusinessException
	 */
	public static void checkEmployeesLabCheckBeforeRetestDate(List<LabCheckEmployeeData> labCheckEmployeeData) throws BusinessException {
		String empLabCheckBeforeRetestDate = "";
		for (LabCheckEmployeeData emp : labCheckEmployeeData) {
			empLabCheckBeforeRetestDate += validateLabCheckEmployeeBeforeRetestDate(emp);
		}

		if (!empLabCheckBeforeRetestDate.isEmpty()) {
			Object[] Params = new Object[1];
			Params[0] = empLabCheckBeforeRetestDate;
			throw new BusinessException("error_checkBeforeRetestDate", Params);
		}
	}

	/**
	 * Validate lab check check mandatory and unique fields
	 * 
	 * @param labCheck
	 * @param employeeDataList
	 * @throws BusinessException
	 */
	private static void validateLabCheck(LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList) throws BusinessException {
		labCheck.setOrderNumber(labCheck.getOrderNumber().trim());

		if (labCheck.getOrderDate() == null || labCheck.getCheckReason() == null || labCheck.getOrderSourceDomainId() == null || labCheck.getOrderNumber().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}

		if (!checkRegexMatch(labCheck.getOrderNumber(), ORDERNUMBER_ARABIC_REGEX)) {
			throw new BusinessException("error_ordernumberMismatchRegex");
		}

		if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()) && (labCheck.getCaseDate() == null || labCheck.getCaseNumber() == null || labCheck.getCaseNumber().trim().isEmpty())) {
			throw new BusinessException("error_mandatory");
		}

		if (!labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()) && (employeeDataList == null || employeeDataList.isEmpty())) {
			throw new BusinessException("error_atLeastOneChoosed");
		}
		if (labCheck.getOrderDate() != null && labCheck.getOrderDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_comingDate");
		}

		try {
			List<LabCheck> labCheckDuplicateList = searchLabCheck(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), labCheck.getOrderNumber(), labCheck.getRegionId());
			if (labCheckDuplicateList.size() > 1 || (labCheck.getId() == null && labCheckDuplicateList.size() == 1) || (labCheckDuplicateList.size() == 1 && labCheckDuplicateList.get(0).getId().longValue() != labCheck.getId().longValue()))
				throw new BusinessException("error_labCheckExistsWithSameNumber");
		} catch (NoDataException e) {
			;
		}
	}

	/**
	 * Validate lab check employee
	 * 
	 * @param employeeDataList
	 * @throws BusinessException
	 */
	private static String validateLabCheckEmployee(LabCheckEmployeeData employeeData) throws BusinessException {
		if (checkEmployeeActionStatus(employeeData) > 0) {
			return "-" + employeeData.getEmployeeFullName();
		} else if (getValidateEmployeeLabCheck(employeeData.getEmployeeId(), employeeData.getLabCheckId()) > 0) {
			return "-" + employeeData.getEmployeeFullName();
		}
		return "";
	}

	/**
	 * Count of labcheck employees and order date before periodicRetestDate
	 * 
	 * @param employeeDataList
	 * @throws BusinessException
	 */
	private static String validateLabCheckEmployeeBeforeRetestDate(LabCheckEmployeeData employeeData) throws BusinessException {
		if (searchCountLabCheckEmployeesBeforeRetestDate(employeeData.getEmployeeId(), employeeData.getLabCheckId(), employeeData.getOrderDate()) > 0) {
			return "-" + employeeData.getEmployeeFullName();
		}
		return "";
	}

	/**
	 * update lab check
	 * 
	 * @param labCheck
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateLabCheck(LabCheck labCheck, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheck.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(labCheck, session);

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
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * update labCheckEmployee
	 * 
	 * @param labCheckEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateLabCheckEmployee(LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckEmployeeData(labCheckEmployeeData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckEmployeeData.getLabCheckEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(labCheckEmployeeData.getLabCheckEmployee(), session);
			if (labCheckEmployeeData.getHcmFlag() != null && labCheckEmployeeData.getOldCheckStatus() != null && labCheckEmployeeData.getCheckStatus() != null) {
				if ((labCheckEmployeeData.getOldCheckStatus() != labCheckEmployeeData.getCheckStatus()) && labCheckEmployeeData.getHcmFlag()) {
					labCheckHCMupdatedStatus(labCheckEmployeeData);
				}
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
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * validateDeleteLabCheckEmployeeData
	 * 
	 * @param labCheckEmployeeData
	 * @throws BusinessException
	 */
	private static void validateDeletedLabCheckEmployeeData(LabCheckEmployeeData labCheckEmployeeData) throws BusinessException {
		if (labCheckEmployeeData.getCheckStatus() != null && !labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode())) {
			throw new BusinessException("error_labCheckEmpCantBeDeleted");
		}
	}

	/**
	 * delete lab check employee
	 * 
	 * @param labCheckEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteLabCheckEmployee(LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateDeletedLabCheckEmployeeData(labCheckEmployeeData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckEmployeeData.getLabCheckEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(labCheckEmployeeData.getLabCheckEmployee(), session);

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
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * retestLabCheckEmployee
	 * 
	 * @param oldlabCheckEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void retestLabCheckEmployee(LabCheckEmployeeData oldlabCheckEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			LabCheckEmployeeData newLabCheckEmployeeData = new LabCheckEmployeeData();
			newLabCheckEmployeeData.setLabCheckId(oldlabCheckEmployeeData.getLabCheckId());
			newLabCheckEmployeeData.setEmployeeId(oldlabCheckEmployeeData.getEmployeeId());
			newLabCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
			newLabCheckEmployeeData.setRetestNumber(oldlabCheckEmployeeData.getRetestNumber() + 1);
			saveLabCheckEmployee(newLabCheckEmployeeData, loginEmpData, session);

			oldlabCheckEmployeeData.setRetested(true);
			if (oldlabCheckEmployeeData.getHcmFlag() != null && oldlabCheckEmployeeData.getHcmFlag()) {
				labCheckHCMupdatedStatus(newLabCheckEmployeeData);
			}
			updateLabCheckEmployee(oldlabCheckEmployeeData, loginEmpData, session);

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
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * validate lab check employee data
	 * 
	 * @param labCheckEmployeeData
	 * @throws BusinessException
	 */
	private static void validateLabCheckEmployeeData(LabCheckEmployeeData labCheckEmployeeData) throws BusinessException {
		if (labCheckEmployeeData.getSampleDate() != null && labCheckEmployeeData.getSampleDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_comingDate");
		}
	}

	/**
	 * @param msg
	 * @param useSession
	 */
	public static String labCheckSoapHCMAnalysis(String msg) {
		Log4j.traceInfo(LabCheckService.class, "HCM Rest WebService Called");
		Log4j.traceInfo(LabCheckService.class, "HCM Rest WebService Message : " + msg);
		String respond = "";
		try {
			String[] msgTokens = msg.split(",");
			EmployeeData admin = EmployeeService.getEmployee(InfoSysConfigurationService.getSystemAdmin());
			for (int i = 0; i < msgTokens.length; i++) {
				Log4j.traceInfo(LabCheckService.class, "--------------------------------------------------");
				String socialId = msgTokens[i];
				if (socialId == null || socialId.trim().isEmpty()) {
					Log4j.traceError(LabCheckService.class, "Social Id can not be found.");
					throw new Exception();
				}
				Log4j.traceInfo(LabCheckService.class, "Start Processing For: " + socialId);
				LabCheckEmployeeData labCheckEmployeeData = getLastLabCheckEmployeeDataByEmpSocialId(socialId);
				if (labCheckEmployeeData != null) {
					labCheckEmployeeData.setHcmFlag(true);
					updateLabCheckEmployee(labCheckEmployeeData, admin);
					Log4j.traceInfo(LabCheckService.class, "LabCheck For : " + socialId + " Updated Successfully");
				}
				EmployeeData empData = EmployeeService.getEmployee(socialId);
				if (empData == null) {
					Log4j.traceError(LabCheckService.class, "Employee not found");
					throw new Exception();
				} else if (labCheckEmployeeData == null) {
					String resp = socialId + "_" + HCMLabCheckStatusEnum.NO_LAB_CHECK.getCode() + ",";
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else if (labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.UNDER_APPROVAL.getCode()) || labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.REGISTERED.getCode())) {
					String resp = socialId + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode())) {
					String resp = socialId + "_" + HCMLabCheckStatusEnum.NO_LAB_CHECK.getCode() + ",";
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode())) {
					String resp = "";
					if ((labCheckEmployeeData.getNationalForceSampleSentDate() == null ? HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow() : HijriDateService.hijriDateDiff(labCheckEmployeeData.getNationalForceSampleSentDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow())) { // If this result within a year
						resp = socialId + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
					} else {
						resp = socialId + "_" + HCMLabCheckStatusEnum.NO_LAB_CHECK.getCode() + ",";
					}
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHEATING.getCode())) {
					String resp = "";
					if ((labCheckEmployeeData.getNationalForceSampleSentDate() == null ? HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow() : HijriDateService.hijriDateDiff(labCheckEmployeeData.getNationalForceSampleSentDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow())) { // If this result within a year
						resp = socialId + "_" + HCMLabCheckStatusEnum.CHEATING.getCode() + ",";
					} else {
						resp = socialId + "_" + HCMLabCheckStatusEnum.NO_LAB_CHECK.getCode() + ",";
					}
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode())) {
					String resp = "";
					if ((labCheckEmployeeData.getNationalForceSampleSentDate() == null ? HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow() : HijriDateService.hijriDateDiff(labCheckEmployeeData.getNationalForceSampleSentDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow())) { // If this result within a year
						resp = socialId + "_" + HCMLabCheckStatusEnum.NEGATIVE.getCode() + ",";
					} else {
						resp = socialId + "_" + HCMLabCheckStatusEnum.NO_LAB_CHECK.getCode() + ",";
					}
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				} else {
					String resp = socialId + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
					respond += resp;
					Log4j.traceInfo(LabCheckService.class, "Response Added : " + resp);
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
		}
		Log4j.traceInfo(LabCheckService.class, "Response Message : " + respond);
		return respond.substring(0, respond.length() - 1);
	}

	/**
	 * @param msg
	 * @param useSession
	 */
	public static void labCheckHCMAnalysis(String msg, CustomSession... useSession) {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			String[] msgTokens = msg.split(",");
			String respond = "";
			for (int i = 0; i < msgTokens.length; i++) {
				String[] tokens = msgTokens[i].split("_");
				if (tokens.length != 2) {
					Log4j.traceError(LabCheckService.class, "The parameter count is not correct.");
					throw new Exception();
				}
				String socialId = tokens[0];
				String labCheckIdStr = tokens[1];

				if (socialId == null || socialId.trim().isEmpty()) {
					Log4j.traceError(LabCheckService.class, "Social Id can not be found.");
					throw new Exception();
				} else if (labCheckIdStr != null && labCheckIdStr.trim().isEmpty()) {
					Log4j.traceError(LabCheckService.class, "R symbole or value cannot be found.");
					throw new Exception();
				}
				Long labCheckId = null;
				try {
					if (labCheckIdStr != null && !labCheckIdStr.toUpperCase().equals("R")) {
						labCheckId = Long.parseLong(labCheckIdStr);
					}
				} catch (Exception e) {
					Log4j.traceError(LabCheckService.class, "Lab check data type is invalid");
					throw e;
				}
				if (labCheckIdStr.toUpperCase().equals("R")) {
					Log4j.traceInfo(LabCheckService.class, "Social Id Start search :" + socialId);
					LabCheckEmployeeData labCheckEmployeeData = getLastLabCheckEmployeeDataByEmpSocialId(socialId);
					Log4j.traceInfo(LabCheckService.class, "Social Id END search :" + socialId);
					EmployeeData admin = EmployeeService.getEmployee(InfoSysConfigurationService.getSystemAdmin());
					if (labCheckEmployeeData == null) { // Has no previous lab checks
						LabCheck labCheck = new LabCheck();
						labCheck.setOrderNumber(generateOrderNumber(EntitySequenceGeneratorEnum.LAB_CHECK.getEntityId(), session));
						labCheck.setOrderDate(HijriDateService.getHijriSysDate());
						labCheck.setOrderSourceDomainId(SetupService.getDomains(FlagsEnum.ALL.getCode(), getParameterizedMessage("label_genralManager", "ar"), FlagsEnum.ALL.getCode()).get(0).getId());
						labCheck.setOrderSourceDomainDescription(getParameterizedMessage("label_genralManager", "ar"));
						labCheck.setCheckReason(LabCheckReasonsEnum.PROMOTION.getCode());
						labCheck.setRegionId(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());
						labCheck.setIntegration(true);
						List<LabCheckEmployeeData> employeeDataList = new ArrayList<LabCheckEmployeeData>();
						EmployeeData empData = EmployeeService.getEmployee(socialId);
						if (empData == null) {
							Log4j.traceError(LabCheckService.class, "LabCheckService : Employee not found");
						} else {
							labCheckEmployeeData = new LabCheckEmployeeData();
							labCheckEmployeeData.setEmployeeId(empData.getEmpId());
							labCheckEmployeeData.setEmployeeFullName(empData.getFullName());
							labCheckEmployeeData.setEmployeeMilitaryNumber(empData.getMilitaryNo());
							labCheckEmployeeData.setEmployeeRank(empData.getRank());
							labCheckEmployeeData.setEmployeeSocialId(empData.getSocialID());
							labCheckEmployeeData.setEmployeeDepartmentId(empData.getActualDepartmentId());
							labCheckEmployeeData.setEmployeeDepartmentName(empData.getActualDepartmentName());
							labCheckEmployeeData.setHcmFlag(true);
							labCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
							employeeDataList.add(labCheckEmployeeData);
							Log4j.traceInfo(LabCheckService.class, "Init approved workflow :" + socialId);
							LabCheckWorkFlow.initApprovedLabCheck(labCheck, employeeDataList, admin, session);
							respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
						}
					} else if (labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.UNDER_APPROVAL.getCode()) || labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.REGISTERED.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
						Log4j.traceInfo(LabCheckService.class, "Social Id add response  :" + socialId);
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHEATING.getCode())) {
						if ((labCheckEmployeeData.getNationalForceSampleSentDate() == null ? HijriDateService.hijriDateDiff(labCheckEmployeeData.getSampleDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow() : HijriDateService.hijriDateDiff(labCheckEmployeeData.getNationalForceSampleSentDate(), HijriDateService.getHijriSysDate()) < InfoSysConfigurationService.getHCMDrugRequestWindow())) { // If this result within a year
							if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode())) {
								respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
								Log4j.traceInfo(LabCheckService.class, "Social Id add response  :" + socialId);
							} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHEATING.getCode())) {
								respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.CHEATING.getCode() + ",";
								Log4j.traceInfo(LabCheckService.class, "Social Id add response  :" + socialId);
							} else {
								respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.NEGATIVE.getCode() + ",";
								Log4j.traceInfo(LabCheckService.class, "Social Id add response  :" + socialId);
							}
						} else {
							LabCheck labCheck = new LabCheck();
							labCheck.setOrderNumber(generateOrderNumber(EntitySequenceGeneratorEnum.LAB_CHECK.getEntityId(), session));
							labCheck.setOrderDate(HijriDateService.getHijriSysDate());
							labCheck.setOrderSourceDomainId(SetupService.getDomains(FlagsEnum.ALL.getCode(), getParameterizedMessage("label_genralManager", "ar"), FlagsEnum.ALL.getCode()).get(0).getId());
							labCheck.setOrderSourceDomainDescription(getParameterizedMessage("label_genralManager", "ar"));
							labCheck.setCheckReason(LabCheckReasonsEnum.PROMOTION.getCode());
							labCheck.setRegionId(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());
							labCheck.setIntegration(true);
							List<LabCheckEmployeeData> employeeDataList = new ArrayList<LabCheckEmployeeData>();
							EmployeeData empData = EmployeeService.getEmployee(socialId);
							if (empData == null) {
								Log4j.traceError(LabCheckService.class, "LabCheckService : Employee not found");
							} else {
								labCheckEmployeeData = new LabCheckEmployeeData();
								labCheckEmployeeData.setEmployeeId(empData.getEmpId());
								labCheckEmployeeData.setEmployeeFullName(empData.getFullName());
								labCheckEmployeeData.setEmployeeMilitaryNumber(empData.getMilitaryNo());
								labCheckEmployeeData.setEmployeeRank(empData.getRank());
								labCheckEmployeeData.setHcmFlag(true);
								labCheckEmployeeData.setEmployeeSocialId(empData.getSocialID());
								labCheckEmployeeData.setEmployeeDepartmentId(empData.getActualDepartmentId());
								labCheckEmployeeData.setEmployeeDepartmentName(empData.getActualDepartmentName());
								labCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
								employeeDataList.add(labCheckEmployeeData);
								Log4j.traceInfo(LabCheckService.class, "Init approve for social Id :" + socialId);
								LabCheckWorkFlow.initApprovedLabCheck(labCheck, employeeDataList, admin, session);
								respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
							}
						}
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode())) {
						LabCheck labCheck = new LabCheck();
						labCheck.setOrderNumber(generateOrderNumber(EntitySequenceGeneratorEnum.LAB_CHECK.getEntityId(), session));
						labCheck.setOrderDate(HijriDateService.getHijriSysDate());
						labCheck.setOrderSourceDomainId(SetupService.getDomains(FlagsEnum.ALL.getCode(), getParameterizedMessage("label_genralManager", "ar"), FlagsEnum.ALL.getCode()).get(0).getId());
						labCheck.setOrderSourceDomainDescription(getParameterizedMessage("label_genralManager", "ar"));
						labCheck.setCheckReason(LabCheckReasonsEnum.PROMOTION.getCode());
						labCheck.setRegionId(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());
						labCheck.setIntegration(true);
						List<LabCheckEmployeeData> employeeDataList = new ArrayList<LabCheckEmployeeData>();
						EmployeeData empData = EmployeeService.getEmployee(socialId);
						if (empData == null) {
							Log4j.traceError(LabCheckService.class, "LabCheckService : Employee not found");
						} else {
							labCheckEmployeeData = new LabCheckEmployeeData();
							labCheckEmployeeData.setEmployeeId(empData.getEmpId());
							labCheckEmployeeData.setEmployeeFullName(empData.getFullName());
							labCheckEmployeeData.setEmployeeMilitaryNumber(empData.getMilitaryNo());
							labCheckEmployeeData.setEmployeeRank(empData.getRank());
							labCheckEmployeeData.setEmployeeSocialId(empData.getSocialID());
							labCheckEmployeeData.setEmployeeDepartmentId(empData.getActualDepartmentId());
							labCheckEmployeeData.setEmployeeDepartmentName(empData.getActualDepartmentName());
							labCheckEmployeeData.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
							labCheckEmployeeData.setHcmFlag(true);
							employeeDataList.add(labCheckEmployeeData);
							Log4j.traceInfo(LabCheckService.class, "Init approve for social id :" + socialId);
							LabCheckWorkFlow.initApprovedLabCheck(labCheck, employeeDataList, admin, session);
							respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
						}
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.RETEST.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.SENT_TO_SECURITY_FORCES.getCode() + ",";
					}
				} else {
					Log4j.traceInfo(LabCheckService.class, "Search labcheck for social id :" + socialId);
					LabCheckEmployeeData labCheckEmployeeData = getLabCheckEmployeesByLabCheckIdAndSocialId(labCheckId, socialId);
					Log4j.traceInfo(LabCheckService.class, "Labcheck returned for social id :" + socialId);
					if (labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.UNDER_APPROVAL.getCode()) || labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.REGISTERED.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHEATING.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.CHEATING.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.NEGATIVE.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.RETEST.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
					} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode())) {
						respond += socialId + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.SENT_TO_SECURITY_FORCES.getCode() + ",";
					}
				}
			}

			Log4j.traceInfo(LabCheckService.class, "Start committing");
			if (!isOpenedSession)
				session.commitTransaction();
			Log4j.traceInfo(LabCheckService.class, "End committing");

			// SEND TO HCM USING SOAP MESSAGE
			try {
				Log4j.traceInfo(LabCheckService.class, "Start SOAP function Calling ..");
				Log4j.traceInfo(LabCheckService.class, "SOAP Message : " + (respond.trim().isEmpty() ? respond : respond.substring(0, respond.length() - 1)));
				PromotionsDrugsTestWSService promotionsDrugTestWsService = new PromotionsDrugsTestWSService();
				PromotionsDrugsTestWS promotionsDrugTestWs = promotionsDrugTestWsService.getPromotionsDrugsTestWSHttpPort();
				promotionsDrugTestWs.adjustPromotionsDrugsTestResults(respond.trim().isEmpty() ? respond : respond.substring(0, respond.length() - 1));
				Log4j.traceInfo(LabCheckService.class, "SOAP Called Successfully");
			} catch (Exception e) {
				Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * @param labCheckEmployeeData
	 * @param status
	 */
	public static void labCheckHCMupdatedStatus(LabCheckEmployeeData labCheckEmployeeData) {
		String respond = "";
		if (labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.UNDER_APPROVAL.getCode()) || labCheckEmployeeData.getLabCheckStatus().equals(LabCheckStatusEnum.REGISTERED.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHEATING.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.CHEATING.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.POSITIVE.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.NEGATIVE.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.RETEST.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.RUNNING.getCode() + ",";
		} else if (labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode()) || labCheckEmployeeData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.SENDING_TO_FORCE.getCode())) {
			respond += labCheckEmployeeData.getEmployeeSocialId() + "_" + labCheckEmployeeData.getLabCheckId() + "_" + HCMLabCheckStatusEnum.SENT_TO_SECURITY_FORCES.getCode() + ",";
		}
		// SEND TO HCM USING SOAP MESSAGE
		try {
			Log4j.traceInfo(LabCheckService.class, "Start SOAP function Calling ..");
			Log4j.traceInfo(LabCheckService.class, "SOAP Message : " + (respond.trim().isEmpty() ? respond : respond.substring(0, respond.length() - 1)));
			PromotionsDrugsTestWSService promotionsDrugTestWsService = new PromotionsDrugsTestWSService();
			PromotionsDrugsTestWS promotionsDrugTestWs = promotionsDrugTestWsService.getPromotionsDrugsTestWSHttpPort();
			promotionsDrugTestWs.adjustPromotionsDrugsTestResults(respond.trim().isEmpty() ? respond : respond.substring(0, respond.length() - 1));
			Log4j.traceInfo(LabCheckService.class, "SOAP Called Successfully");
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
		}
	}

	/******************************************** QUERIES ********************************************/

	/**
	 * Get Lab Check Details Report
	 * 
	 * @param labCheckId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckDetailsReportBytes(long labCheckId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_LAB_CHECK_ID", labCheckId);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			reportName = ReportNamesEnum.LAB_CHECK_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Employee Lab Check Details Report
	 * 
	 * @param labCheckEmployeeId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getEmployeeLabCheckDetailsReportBytes(long labCheckEmployeeId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_LAB_CHECK_EMPLOYEE_ID", labCheckEmployeeId);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			reportName = ReportNamesEnum.LAB_CHECK_EMPLOYEE_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Lab Check Report
	 * 
	 * @param employeeId
	 * @param sampleNumber
	 * @param sampleDateString
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckReportBytes(String employeeId, String sampleNumber, String sampleDateString) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_EMPLOYEE_ID", employeeId);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_SAMPLE_NUMER", sampleNumber);
			parameters.put("P_SAMPLE_DATE_STRING", sampleDateString);
			reportName = ReportNamesEnum.LAB_CHECK.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Guard Mission Bytes
	 * 
	 * @param regionId
	 * @param labCheckResult
	 * @param labCheckReason
	 * @param fromDate
	 * @param toDate
	 * @param regionName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckReportBytes(long regionId, long labCheckResult, long labCheckReason, Date fromDate, Date toDate, String regionName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_LAB_CHECK_STATUS", labCheckResult);
			parameters.put("P_LAB_CHECK_REASON", labCheckReason);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LABORATORY_TEST_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Security Guard Mission Bytes
	 * 
	 * @param regionId
	 * @param labCheckResult
	 * @param labCheckReason
	 * @param fromDate
	 * @param toDate
	 * @param regionName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckReportStatisticsBytes(long regionId, long labCheckResult, long labCheckReason, Date fromDate, Date toDate, String regionName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_LAB_CHECK_STATUS", labCheckResult);
			parameters.put("P_LAB_CHECK_REASON", labCheckReason);
			parameters.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			parameters.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LABORATORY_TEST_STATISTICS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Lab Check Bytes
	 * 
	 * @param employeeId
	 * @param orderDate
	 * @param orderNumber
	 * @param checkReason
	 * @param checkStatus
	 * @param departmentId
	 * @param regionId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckSearchBytes(Long employeeId, Date orderDate, String orderNumber, Integer checkReason, Integer checkStatus, Long departmentId, Long regionId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_EMPLOYEE_ID", employeeId);
			parameters.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			parameters.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			parameters.put("P_CHECK_REASON", checkReason);
			parameters.put("P_CEHCK_STATUS", checkStatus);
			parameters.put("P_DEPARTMENT_ID", departmentId);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LAB_CHECK_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Exempts Bytes
	 * 
	 * @param regionId
	 * @param regionName
	 * @param fromDate
	 * @param toDate
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getExemptsReportBytes(long regionId, String regionName, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.EXEMPTS_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Generate Order Number
	 * 
	 * @param session
	 * @return
	 * @throws BusinessException
	 * @throws DatabaseException
	 */
	public static String generateOrderNumber(long entityId, CustomSession session) throws BusinessException, DatabaseException {
		long assignmentRequestNumber = CommonService.generateSequenceNumber(entityId, Integer.MAX_VALUE, session);
		return assignmentRequestNumber + "/" + HijriDateService.getHijriSysDateString().split("/")[2] + "/ " + "\u062a";
	}

	/**********************************************/
	/******************* Queries *****************/
	/**
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheck getLabCheck(long id) throws BusinessException {
		try {
			return searchLabCheck(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	public static List<LabCheck> getLabCheck(String orderNumber, long regionId) throws BusinessException {
		try {
			return searchLabCheck(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), orderNumber, regionId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheck>();
		}
	}

	/**
	 * get Lab Check By Order Number And Check Reason
	 * 
	 * @param checkReason
	 * @param orderNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheck> getLabCheckByOrderNumberAndCheckReason(int checkReason, String orderNumber) throws BusinessException {
		try {
			return searchLabCheck(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), checkReason, orderNumber, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheck>();
		}
	}

	/**
	 * @param instanceId
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheck getLabCheckByWfInstanceId(long instanceId) throws BusinessException {
		try {
			return searchLabCheck(FlagsEnum.ALL.getCode(), instanceId, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Search Lab Check by order no, date, order source domain and check reason
	 * 
	 * @param orderNo
	 * @param orderFromDate
	 * @param orderToDate
	 * @param orderSourceDomainId
	 * @param checkReason
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheck> getApprovedLabCheck(String orderNo, Date orderFromDate, Date orderToDate, Long orderSourceDomainId, Integer checkReason) throws BusinessException {
		try {
			return searchLabCheckDetails(orderNo, orderFromDate, orderToDate, orderSourceDomainId == null ? FlagsEnum.ALL.getCode() : orderSourceDomainId, checkReason == null ? FlagsEnum.ALL.getCode() : checkReason, LabCheckStatusEnum.APPROVED.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheck>();
		}
	}

	/**
	 * @param id
	 * @param instanceId
	 * @param orderNumber
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheck> searchLabCheck(long id, long instanceId, int checkReason, String orderNumber, long regionId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(LabCheck.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECK.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search Lab Check by order no, date, order source domain and check reason
	 * 
	 * @param orderNo
	 * @param orderFromDate
	 * @param orderToDate
	 * @param orderSourceDomainId
	 * @param checkReason
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheck> searchLabCheckDetails(String orderNo, Date orderFromDate, Date orderToDate, long orderSourceDomainId, int checkReason, int labCheckStatus) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ORDER_NUMBER", orderNo == null || orderNo.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNo);
			qParams.put("P_FROM_DATE_NULL", orderFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", orderFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderFromDate));
			qParams.put("P_TO_DATE_NULL", orderToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE", orderToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderToDate));
			qParams.put("P_ORDER_SOURCE_DOMAIN_ID", orderSourceDomainId);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_LAB_CHECK_STATUS", labCheckStatus);
			return DataAccess.executeNamedQuery(LabCheck.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECK_DETAILS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getPervEmployeesLabChecks(long employeeId, Date previousDate) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), employeeId, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), LabCheckStatusEnum.APPROVED.getCode(), previousDate, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * @param labCheckId
	 * @param retestNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabCheckEmployesByLabCheckId(long labCheckId, Integer retestNumber) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), labCheckId, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, retestNumber == null ? FlagsEnum.ALL.getCode() : retestNumber, null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * @param labCheckId
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheckEmployeeData getLabCheckEmployeesByLabCheckIdAndSocialId(long labCheckId, String socialId) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), labCheckId, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), socialId, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get lab check employees by lab check id
	 * 
	 * @param labCheckId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabCheckEmployeesByLabCheckId(long labCheckId) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), labCheckId, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * @param employeeId
	 * @param orderDate
	 * @param orderNumber
	 * @param checkReason
	 * @param checkStatus
	 * @param departmentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabCheckEmployees(Long employeeId, Date orderDate, String orderNumber, Integer checkReason, Integer checkStatus, Long departmentId, Integer labCheckStatus, Long regionId, String sampleNumber) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, FlagsEnum.ALL.getCode(), orderDate, orderNumber, null, checkReason == null ? FlagsEnum.ALL.getCode() : checkReason, checkStatus == null ? FlagsEnum.ALL.getCode() : checkStatus, departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, labCheckStatus == null ? FlagsEnum.ALL.getCode() : labCheckStatus, null, regionId == null ? FlagsEnum.ALL.getCode() : regionId,
					sampleNumber, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * Get LabCheckEmployees By labCheckEmpId, labCheckId and retestNo
	 * 
	 * @param employeeId
	 * @param labCheckId
	 * @param retestNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabCheckEmployeesByLabCheckEmpIdAndLabCheckIdAndRetestNo(long employeeId, long labCheckId, Integer retestNumber) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), employeeId, labCheckId, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, retestNumber == null ? FlagsEnum.ALL.getCode() : retestNumber, null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * @param orderDate
	 * @param orderNumber
	 * @param checkReason
	 * @param checkStatus
	 * @param employeeId
	 * @param labCheckStatus
	 * @param depChilds
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabChecksAtUnitForEmployees(Date orderDate, String orderNumber, Integer checkReason, Integer checkStatus, Long employeeId, Integer labCheckStatus, List<Long> depChilds) throws BusinessException {
		try {
			return searchLabChecksAtUnitForEmployees(orderDate, orderNumber, checkReason == null ? FlagsEnum.ALL.getCode() : checkReason, checkStatus == null ? FlagsEnum.ALL.getCode() : checkStatus, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, labCheckStatus == null ? FlagsEnum.ALL.getCode() : labCheckStatus, depChilds);
		} catch (NoDataException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * @param orderDate
	 * @param orderNumber
	 * @param checkReason
	 * @param checkStatus
	 * @param employeeId
	 * @param labCheckStatus
	 * @param unitsIds
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckEmployeeData> searchLabChecksAtUnitForEmployees(Date orderDate, String orderNumber, int checkReason, int checkStatus, long employeeId, int labCheckStatus, List<Long> unitsIds) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + orderNumber + "%");
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_CEHCK_STATUS", checkStatus);
			qParams.put("P_LAB_CHECK_STATUS", labCheckStatus);
			qParams.put("DEP_CHILDS", unitsIds.toArray(new Long[unitsIds.size()]));
			return DataAccess.executeNamedQuery(LabCheckEmployeeData.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECKS_AT_UNIT_FOR_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheckEmployeeData getLabCheckEmployeeDataById(long id) throws BusinessException {
		try {
			return searchLabCheckEmployees(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheckEmployeeData getLastLabCheckEmployeeDataByEmpSocialId(String socialId) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), socialId, LabCheckStatusEnum.REJECTED.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheckEmployeeData getLastLabCheckEmployeeDataByOrderNoAndOrderDate(Date orderDate, String exactOrderNumber) throws BusinessException {
		try {
			return searchLabCheckEmployees(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), orderDate, null, exactOrderNumber, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode(), null, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	public static LabCheckEmployeeData getLastLabCheckEmpByEmpIdAndCheckReason(Long employeeId, int checkReason, int labCheckStatus) throws BusinessException {
		try {
			return searchLastLabCheckEmployeeByCheckReason(employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, checkReason, labCheckStatus).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	private static List<LabCheckEmployeeData> searchLastLabCheckEmployeeByCheckReason(long employeeId, int checkReason, int labCheckStatus) throws NoDataException, BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_LAB_CHECK_STATUS", labCheckStatus);
			return DataAccess.executeNamedQuery(LabCheckEmployeeData.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAST_LAB_CHECK_EMPLOYEE_BY_CHECK_REASON.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param id
	 * @param employeeId
	 * @param labCheckId
	 * @param orderDate
	 * @param orderNumber
	 * @param checkReason
	 * @param checkStatus
	 * @param employeeDepartmentId
	 * @param labCheckStatus
	 * @param previousDate
	 * @param regionId
	 * @param sampleNumber
	 * @param retestNumber
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckEmployeeData> searchLabCheckEmployees(long id, long employeeId, long labCheckId, Date orderDate, String orderNumber, String exactOrderNumber, int checkReason, int checkStatus, long employeeDepartmentId, int labCheckStatus, Date previousDate, long regionId, String sampleNumber, int retestNumber, String socialId, int notLabCheckStatus) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_LAB_CHECK_ID", labCheckId);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_PREVIOUS_DATE_NULL", previousDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_PREVIOUS_DATE", previousDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(previousDate));
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : "%" + orderNumber + "%");
			qParams.put("P_EXACT_ORDER_NUMBER", exactOrderNumber == null || exactOrderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : exactOrderNumber);
			qParams.put("P_SAMPLE_NUMBER", sampleNumber == null || sampleNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : sampleNumber);
			qParams.put("P_CHECK_REASON", checkReason);
			qParams.put("P_CEHCK_STATUS", checkStatus);
			qParams.put("P_LAB_CHECK_STATUS", labCheckStatus);
			qParams.put("P_NOT_LAB_CHECK_STATUS", notLabCheckStatus);
			qParams.put("P_EMPLOYEE_DEPARTMENT_ID", employeeDepartmentId);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_RETEST_NUMBER", retestNumber);
			return DataAccess.executeNamedQuery(LabCheckEmployeeData.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECK_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param labCheckStatus
	 * @param orderDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getDelayedLabCheckEmployees(Integer labCheckStatus, Date orderDate) throws BusinessException {
		try {
			return searchDelayedLabCheckEmployees(labCheckStatus, orderDate);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * @param checkStatus
	 * @param labCheckStatus
	 * @param orderDate
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckEmployeeData> searchDelayedLabCheckEmployees(Integer labCheckStatus, Date orderDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_LAB_CHECK_STATUS", labCheckStatus);
			return DataAccess.executeNamedQuery(LabCheckEmployeeData.class, QueryNamesEnum.LAB_CHECK_SEARCH_DELAYED_LAB_CHECK_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param currentDate
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckEmployeeData> getLabCheckEmployeesToRetest(Date currentDate) throws BusinessException {
		try {
			return searchLabCheckEmployeesToRetest(currentDate, LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode());
		} catch (NoDataException e) {
			return new ArrayList<LabCheckEmployeeData>();
		}
	}

	/**
	 * @param currentDate
	 * @param checkStatus
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckEmployeeData> searchLabCheckEmployeesToRetest(Date currentDate, int checkStatus) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CURRENT_DATE_NULL", currentDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_CURRENT_DATE", currentDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(currentDate));
			qParams.put("P_END_DATE_NULL", currentDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE", currentDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(HijriDateService.addSubHijriDays(currentDate, 6)));
			qParams.put("P_CEHCK_STATUS", checkStatus);
			return DataAccess.executeNamedQuery(LabCheckEmployeeData.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECK_EMPLOYEE_TO_RETEST.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * @param labCheckId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getCountLabCheckEmployees(long labCheckId) throws BusinessException {
		try {
			return searchCountLabCheckEmployees(labCheckId, FlagsEnum.ALL.getCode());
		} catch (BusinessException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getValidateEmployeeLabCheck
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	public static Long getValidateEmployeeLabCheck(long employeeId, long labCheckId) throws BusinessException {
		try {
			return validateEmployeeLabCheck(employeeId, labCheckId);
		} catch (BusinessException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Count of labcheck employees
	 * 
	 * @param employeeId
	 * @return count of labcheck employees
	 * @throws BusinessException
	 */
	public static Long getCountLabCheckEmployeesByEmployeeId(long employeeId) throws BusinessException {
		return searchCountLabCheckEmployees(FlagsEnum.ALL.getCode(), employeeId);
	}

	/**
	 * Count of labcheck employees
	 * 
	 * @param labCheckId
	 * @param orderNumber
	 * @param employeeId
	 * @return count of labcheck employees
	 * @throws BusinessException
	 */
	private static Long searchCountLabCheckEmployees(long labCheckId, long employeeId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_ID", labCheckId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_COUNT_LAB_CHECK_EMPLOYEE.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Check Employee Status if has under approval, no sample, positive under approval checks within this labCheckRecord
	 * 
	 * @param labCheckEmployeeData
	 * @return
	 * @throws BusinessException
	 */
	private static Long checkEmployeeActionStatus(LabCheckEmployeeData labCheckEmployeeData) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", labCheckEmployeeData.getEmployeeId());
			qParams.put("P_LAB_CHECK_ID", labCheckEmployeeData.getLabCheckId());
			qParams.put("P_STATUS", new Integer[] { LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode() });
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_EMPLOYEE_DATE_CHECK_EMPLOYEE_ACTION_STATUS.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * validateEmployeeLabCheck
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	private static Long validateEmployeeLabCheck(long employeeId, long labCheckId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_ID", labCheckId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_STATUS", new Integer[] { LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode(), LabCheckEmployeeCheckStatusEnum.CHEATING.getCode(), LabCheckEmployeeCheckStatusEnum.CHECK_PRIVATION.getCode() });
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_EMPLOYEE_DATE_VALIDATE_EMPLOYEE_LAB_CHECK.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	public static Long getCountSampleNumber(String sampleYear, String sampleNumber) throws BusinessException {
		try {
			return countSampleNumber(sampleYear, sampleNumber);
		} catch (BusinessException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Check if there is the same sample number in the current year
	 * 
	 * @param sampleYear
	 * @param sampleNumber
	 * @return
	 * @throws BusinessException
	 */
	private static Long countSampleNumber(String sampleYear, String sampleNumber) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SAMPLE_YEAR", sampleYear);
			qParams.put("P_SAMPLE_NUMBER", sampleNumber);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_EMPLOYEE_DATE_VALIDATE_SAMPLE_NUMBER.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Count of labcheck employees and order date before periodicRetestDate
	 * 
	 * @param labCheckId
	 * @param employeeId
	 * @param orderDate
	 * @return
	 * @throws BusinessException
	 */
	private static Long searchCountLabCheckEmployeesBeforeRetestDate(long employeeId, long labCheckId, Date orderDate) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_ID", labCheckId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_STATUS", new Integer[] { LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode() });
			qParams.put("P_ORDER_DATE", HijriDateService.getHijriDateString(orderDate));
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_COUNT_LAB_CHECK_EMPLOYEE_WITHIN_PERIOD.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Get list of lab check by list of task ids of these lab checks
	 * 
	 * @param labCheckWFTaskIds
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheck> getLabCheckByWFTaskIds(Long[] labCheckWFTaskIds) throws BusinessException {
		try {
			return searchLabCheckByWFTaskIds(labCheckWFTaskIds);
		} catch (NoDataException e) {
			return new ArrayList<LabCheck>();
		}
	}

	/**
	 * Get list of lab check by list of task ids of these lab checks
	 * 
	 * @param labCheckWFTaskIds
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheck> searchLabCheckByWFTaskIds(Long[] labCheckWFTaskIds) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_TASKS_ID_LIST", labCheckWFTaskIds);
			return DataAccess.executeNamedQuery(LabCheck.class, QueryNamesEnum.LAB_CHECK_SEARCH_LAB_CHECK_BY_WF_TASK_IDS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get employee lab check by sample year and sample number
	 * 
	 * @param sampleYear
	 * @param sampleNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<Long> getEmployeeLabCheckBySampleData(String sampleYear, String sampleNumber) throws BusinessException {
		try {
			return searchEmployeeLabCheckBySampleData(sampleYear, sampleNumber);
		} catch (BusinessException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Search employee lab check by sample year and sample number
	 * 
	 * @param sampleYear
	 * @param sampleNumber
	 * @return
	 * @throws BusinessException
	 */
	private static List<Long> searchEmployeeLabCheckBySampleData(String sampleYear, String sampleNumber) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SAMPLE_YEAR", sampleYear);
			qParams.put("P_SAMPLE_NUMBER", sampleNumber);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_EMPLOYEE_DATA_SEARCH_EMPLOYEE_LAB_CHECK_BY_SAMPLE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<Long>();
		}
	}

	/**
	 * Get Lab Check Report Bytes By order number and order date
	 * 
	 * @param orderNumber
	 * @param orderDate
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckReportBytesByOrderNoAndOrderDate(String orderNumber, Date orderDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_ORDER_NUMBER", orderNumber);
			parameters.put("P_ORDER_DATE", HijriDateService.getHijriDateString(orderDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LAB_CHECK_BY_ORDER_NUMBER_AND_ORDER_DATE.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Lab Check Report Bytes for specific employee
	 * 
	 * @param orderNumber
	 * @param orderDate
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckEmployeeBytes(Long employeeId, String endTreatmentDate, int labCheckStatus, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_EMPLOYEE_ID", employeeId);
			parameters.put("P_END_TREATMENT_DATE", endTreatmentDate);
			parameters.put("P_LAB_CHECK_STATUS", labCheckStatus);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.PAST_LAB_CHECK_EMPLOYEE.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get lab check details report of tasks needs approval from General Manager or Region Manager
	 * 
	 * @param labCheckIds
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getLabCheckWFTasksApprovalReportBytes(ArrayList<Long> labCheckIds, boolean isGeneralDirectorFlag, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_LAB_CHECK_IDS_LIST", labCheckIds);
			parameters.put("P_IS_GENERAL_DIRECTOR", isGeneralDirectorFlag);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LAB_CHECK_WF_TASKS_APPROVAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get promotions eligible for all employees report bytes
	 * 
	 * @param regionId
	 * @param categoryId
	 * @param rank
	 * @param promotionEligibleLastDate
	 * @param promotionExecutionDate
	 * @param regionName
	 * @param categoryName
	 * @param rankName
	 * @param labCheckStatusName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getAllPromotionsEligibleReportBytes(Long regionId, Long categoryId, Long rank, Date promotionEligibleLastDate, Date promotionExecutionDate, String regionName, String categoryName, String rankName, String labCheckStatusName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_CATEGORY_ID", categoryId);
			parameters.put("P_RANK", rank);
			parameters.put("P_PROMOTION_ELIGIBLE_LAST_DATE", HijriDateService.getHijriDateString(promotionEligibleLastDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE", HijriDateService.getHijriDateString(promotionExecutionDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE_BEFORE_YEAR", HijriDateService.getHijriDateString(HijriDateService.addSubHijriMonthsDays(promotionExecutionDate, -12, 0)));
			parameters.put("P_LAB_CHECK_RESULT_LIST", new Integer[] { LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode(), LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode(), LabCheckEmployeeCheckStatusEnum.SAMPLE_TAKED.getCode() });
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_CATEGORY_NAME", categoryName);
			parameters.put("P_RANK_NAME", rankName);
			parameters.put("P_LAB_CHECK_STATUS_NAME", labCheckStatusName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.PROMOTIONS_ELIGIBLE_ALL_EMP_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get promotions eligible for employees with lab check report bytes
	 * 
	 * @param regionId
	 * @param categoryId
	 * @param rank
	 * @param promotionEligibleLastDate
	 * @param promotionExecutionDate
	 * @param labCheckResult
	 * @param regionName
	 * @param categoryName
	 * @param rankName
	 * @param labCheckStatusName
	 * @param labCheckResultName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getWithLabCheckPromotionsEligibleReportBytes(Long regionId, Long categoryId, Long rank, Date promotionEligibleLastDate, Date promotionExecutionDate, Integer labCheckResult, String regionName, String categoryName, String rankName, String labCheckStatusName, String labCheckResultName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_CATEGORY_ID", categoryId);
			parameters.put("P_RANK", rank);
			parameters.put("P_PROMOTION_ELIGIBLE_LAST_DATE", HijriDateService.getHijriDateString(promotionEligibleLastDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE", HijriDateService.getHijriDateString(promotionExecutionDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE_BEFORE_YEAR", HijriDateService.getHijriDateString(HijriDateService.addSubHijriMonthsDays(promotionExecutionDate, -12, 0)));
			parameters.put("P_LAB_CHECK_RESULT", labCheckResult);
			parameters.put("P_LAB_CHECK_RESULT_LIST", new Integer[] { LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode(), LabCheckEmployeeCheckStatusEnum.NEGATIVE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode(), LabCheckEmployeeCheckStatusEnum.POSITIVE_UNDER_APPROVE.getCode() });
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_CATEGORY_NAME", categoryName);
			parameters.put("P_RANK_NAME", rankName);
			parameters.put("P_LAB_CHECK_STATUS_NAME", labCheckStatusName);
			parameters.put("P_LAB_CHECK_RESULT_NAME", labCheckResultName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.PROMOTIONS_ELIGIBLE_EMP_WITH_LAB_CHECK_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get promotions eligible for employees without lab check report bytes
	 * 
	 * @param regionId
	 * @param categoryId
	 * @param rank
	 * @param promotionEligibleLastDate
	 * @param promotionExecutionDate
	 * @param regionName
	 * @param categoryName
	 * @param rankName
	 * @param labCheckStatusName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getWithoutLabCheckPromotionsEligibleReportBytes(Long regionId, Long categoryId, Long rank, Date promotionEligibleLastDate, Date promotionExecutionDate, String regionName, String categoryName, String rankName, String labCheckStatusName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_CATEGORY_ID", categoryId);
			parameters.put("P_RANK", rank);
			parameters.put("P_PROMOTION_ELIGIBLE_LAST_DATE", HijriDateService.getHijriDateString(promotionEligibleLastDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE", HijriDateService.getHijriDateString(promotionExecutionDate));
			parameters.put("P_PROMOTION_EXECUTION_DATE_BEFORE_YEAR", HijriDateService.getHijriDateString(HijriDateService.addSubHijriMonthsDays(promotionExecutionDate, -12, 0)));
			parameters.put("P_REGION_NAME", regionName);
			parameters.put("P_CATEGORY_NAME", categoryName);
			parameters.put("P_RANK_NAME", rankName);
			parameters.put("P_LAB_CHECK_STATUS_NAME", labCheckStatusName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.PROMOTIONS_ELIGIBLE_EMP_WITHOUT_LAB_CHECK_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(LabCheckService.class, e, "LabCheckService");
			throw new BusinessException("error_general");
		}
	}

}