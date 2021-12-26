package com.code.services.infosys.securityaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.securitymission.VisitorEntranceData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public class VisitorsEntranceService extends BaseService {
	private VisitorsEntranceService() {
	}

	private static void validateVisitorEntrance(VisitorEntranceData visitorEntranceData) throws BusinessException {
		if (visitorEntranceData.getVisitorId() == null) {
			throw new BusinessException("error_visitorMandatory");
		}

		if (visitorEntranceData.getVisitorCardNumber() == null || visitorEntranceData.getVisitorCardNumber().isEmpty()) {
			throw new BusinessException("error_visitorCardNumberMandatory");
		}
	}

	/**
	 * Add visitorEntranceData
	 * 
	 * @param loginUser
	 * @param visitorEntranceData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addVisitorEntrance(EmployeeData loginUser, VisitorEntranceData visitorEntranceData, CustomSession... useSession) throws BusinessException {
		validateVisitorEntrance(visitorEntranceData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			visitorEntranceData.getVisitorEntrance().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(visitorEntranceData.getVisitorEntrance(), session);

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
				Log4j.traceErrorException(VisitorsEntranceService.class, e, "VisitorsEntranceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update visitorEntranceData
	 * 
	 * @param loginUser
	 * @param visitorEntranceData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateVisitorEntrance(EmployeeData loginUser, VisitorEntranceData visitorEntranceData, CustomSession... useSession) throws BusinessException {
		validateVisitorEntrance(visitorEntranceData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			visitorEntranceData.getVisitorEntrance().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(visitorEntranceData.getVisitorEntrance(), session);

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
				Log4j.traceErrorException(VisitorsEntranceService.class, e, "VisitorsEntranceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * Get Current Visitor Entrance Data
	 * 
	 * @param regionId
	 * @return list of VisitorEntranceData
	 * @throws BusinessException
	 */
	public static List<VisitorEntranceData> getCurrentVisitorEntranceData(Long regionId) throws BusinessException {
		try {
			return searchVisitorEntranceData(HijriDateService.getHijriSysDate(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null, null, null, null, null, regionId == null ? FlagsEnum.ALL.getCode() : regionId);
		} catch (NoDataException e) {
			return new ArrayList<VisitorEntranceData>();
		}
	}

	/**
	 * Get Current Non Emp With No Exit Date
	 * 
	 * @param visitorIdentity
	 * @return
	 * @throws BusinessException
	 */
	public static VisitorEntranceData getCurrentVisitorExitByIdentity(long visitorIdentity) throws BusinessException {
		try {
			return searchVisitorEntranceData(HijriDateService.getHijriSysDate(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), visitorIdentity, null, null, null, null, null, null, null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Visitor Entrances
	 * 
	 * @param departmentId
	 * @param employeeId
	 * @param visitorIdentity
	 * @param visitorCardNumber
	 * @param visitorName
	 * @param fromDate
	 * @param toDate
	 * @param fromTime
	 * @param toTime
	 * @param entryExitFlag
	 * @return list of VisitorEntranceData
	 * @throws BusinessException
	 */
	public static List<VisitorEntranceData> getVisitorEntrances(long departmentId, long employeeId, long visitorIdentity, String visitorCardNumber, String visitorName, Date fromDate, Date toDate, String fromTime, String toTime, Boolean entryExitFlag, long regionId) throws BusinessException {
		try {
			return searchVisitorEntranceData(null, departmentId, employeeId, visitorIdentity, visitorCardNumber, visitorName, fromDate, toDate, fromTime, toTime, entryExitFlag, null, regionId);
		} catch (NoDataException e) {
			return new ArrayList<VisitorEntranceData>();
		}
	}

	/**
	 * Search VisitorEntranceData
	 * 
	 * @param currentDate
	 * @param departmentId
	 * @param employeeId
	 * @param visitorCardNumber
	 * @param visitorIdentity
	 * @param visitorName
	 * @param fromDate
	 * @param toDate
	 * @param fromTime
	 * @param toTime
	 * @param entryExitFlag:
	 *            1 entry, 0 exit
	 * 
	 * @return list of VisitorEntranceData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<VisitorEntranceData> searchVisitorEntranceData(Date currentDate, long departmentId, long employeeId, long visitorIdentity, String visitorCardNumber, String visitorName, Date fromDate, Date toDate, String fromTime, String toTime, Boolean entryExitFlag, Integer exitFlag, long regionId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_CURRENT_DATE_NULL", currentDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_CURRENT_DATE", currentDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(currentDate));
			qParams.put("P_VISITOR_CARD_NUMBER", visitorCardNumber == null || visitorCardNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : visitorCardNumber);
			qParams.put("P_VISITOR_IDENTITY", visitorIdentity);
			qParams.put("P_VISITOR_NAME", visitorName == null || visitorName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : visitorName);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_FROM_TIME", fromTime == null || fromTime.isEmpty() ? FlagsEnum.ALL.getCode() + "" : fromTime);
			qParams.put("P_TO_TIME", toTime == null || toTime.isEmpty() ? FlagsEnum.ALL.getCode() + "" : toTime);
			qParams.put("P_ENTRY_EXIT_FLAG", entryExitFlag == null ? FlagsEnum.ALL.getCode() : entryExitFlag ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_EXIT_FLAG", exitFlag == null ? FlagsEnum.ALL.getCode() : exitFlag);
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(VisitorEntranceData.class, QueryNamesEnum.VISITOR_ENTRANCE_DATA_SEARCH_VISITOR_ENTRANCE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(VisitorsEntranceService.class, e, "VisitorsEntranceService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Visitor Entrance Report Bytes
	 * 
	 * @param departmentId
	 * @param employeeId
	 * @param visitorIdentity
	 * @param visitorCardNumber
	 * @param visitorName
	 * @param fromDate
	 * @param toDate
	 * @param fromTime
	 * @param toTime
	 * @param entryExitFlag
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getVisitorEntranceReportBytes(long departmentId, long employeeId, long visitorIdentity, String visitorCardNumber, String visitorName, Date fromDate, Date toDate, String fromTime, String toTime, Boolean entryExitFlag, String loginEmployeeName, long regionId, String regionName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_VISITOR_CARD_NUMBER", visitorCardNumber == null || visitorCardNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : visitorCardNumber);
			qParams.put("P_VISITOR_IDENTITY", visitorIdentity);
			qParams.put("P_VISITOR_NAME", visitorName == null || visitorName.isEmpty() ? FlagsEnum.ALL.getCode() + "" : visitorName);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_FROM_TIME", fromTime == null || fromTime.isEmpty() ? FlagsEnum.ALL.getCode() + "" : fromTime);
			qParams.put("P_TO_TIME", toTime == null || toTime.isEmpty() ? FlagsEnum.ALL.getCode() + "" : toTime);
			qParams.put("P_ENTRY_EXIT_FLAG", entryExitFlag == null ? FlagsEnum.ALL.getCode() : entryExitFlag ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			qParams.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_REGION_NAME", regionName);
			reportName = ReportNamesEnum.VISITORS_ENTRANCE.getCode();
			return ReportService.getReportData(reportName, qParams);
		} catch (Exception e) {
			Log4j.traceErrorException(VisitorsEntranceService.class, e, "VisitorsEntranceService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Visitor Entrance with no exit records Report Bytes
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param regionId
	 * @param regionName
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getVisitorEntranceNoExitReportBytes(Date fromDate, Date toDate, long regionId, String regionName, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			qParams.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_REGION_NAME", regionName);
			qParams.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			qParams.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.VISITORS_ENTRANCE_NO_EXIT.getCode();
			return ReportService.getReportData(reportName, qParams);
		} catch (Exception e) {
			Log4j.traceErrorException(VisitorsEntranceService.class, e, "VisitorsEntranceService");
			throw new BusinessException("error_general");
		}
	}
}
