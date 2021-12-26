package com.code.services.infosys.securityaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securitymission.PenaltyArrestData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public class PenaltyArrestService extends BaseService {

	/**
	 * save penalty Arrest
	 * 
	 * @param penaltyArrestData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void savePenaltyArrest(PenaltyArrestData penaltyArrestData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validatePenaltyArrest(penaltyArrestData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			penaltyArrestData.setRequestDate(HijriDateService.getHijriSysDate());
			penaltyArrestData.getPenaltyArrest().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(penaltyArrestData.getPenaltyArrest(), session);
			penaltyArrestData.setId(penaltyArrestData.getPenaltyArrest().getId());

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
				Log4j.traceErrorException(PenaltyArrestService.class, e, "PenaltyArrestService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * check mandatory fields and business rules
	 * 
	 * @param penaltyArrestData
	 * @throws BusinessException
	 */
	private static void validatePenaltyArrest(PenaltyArrestData penaltyArrestData) throws BusinessException {
		if (penaltyArrestData.getArrestedEmployeeId() == null || penaltyArrestData.getRequesterEmployeeId() == null || penaltyArrestData.getArrestPeriod() == null || penaltyArrestData.getArrestLocation() == null || penaltyArrestData.getArrestLocation().trim().equals("")) {
			throw new BusinessException("error_mandatory");
		}
		if (penaltyArrestData.getArrestPeriod().compareTo(120) > 0) {
			throw new BusinessException("error_arrestPeriodIsGeaterThan120");
		}
		if (validatePenaltyrrestEmployee(penaltyArrestData.getArrestedEmployeeId())) {
			throw new BusinessException("error_arrestedEmployeeHasPenaltyArrest");
		}
	}

	/**
	 * update penalty Arrest
	 * 
	 * @param penaltyArrestData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void updatePenaltyArrest(PenaltyArrestData penaltyArrestData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			penaltyArrestData.getPenaltyArrest().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(penaltyArrestData.getPenaltyArrest(), session);

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
				Log4j.traceErrorException(PenaltyArrestService.class, e, "PenaltyArrestService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * start penalty arrest
	 * 
	 * @param penaltyArrestData
	 * @param loginEmpData
	 * @throws BusinessException
	 */
	public static void startPenaltyArrest(PenaltyArrestData penaltyArrestData, EmployeeData loginEmpData) throws BusinessException {
		penaltyArrestData.setEntryDate(HijriDateService.getHijriSysDate());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		penaltyArrestData.setEntryTime(dateFormat.format(new Date()));
		updatePenaltyArrest(penaltyArrestData, loginEmpData);
	}

	/**
	 * stop penalty arrest
	 * 
	 * @param penaltyArrestData
	 * @param loginEmpData
	 * @throws BusinessException
	 */
	public static void endPenaltyArrest(PenaltyArrestData penaltyArrestData, EmployeeData loginEmpData) throws BusinessException {
		penaltyArrestData.setExitDate(HijriDateService.getHijriSysDate());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		penaltyArrestData.setExitTime(dateFormat.format(new Date()));
		updatePenaltyArrest(penaltyArrestData, loginEmpData);
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * getPenaltyArrestByWFInsatnceId
	 * 
	 * @param wfInstanceId
	 * @return
	 * @throws BusinessException
	 */
	public static PenaltyArrestData getPenaltyArrestByWFInsatnceId(long wfInstanceId) throws BusinessException {
		try {
			return searchPenaltyArrest(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), wfInstanceId, FlagsEnum.ALL.getCode()).get(0);
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * getPenaltyArrest
	 * 
	 * @param id
	 * @param arrestedEmployeeId
	 * @param entryFromDate
	 * @param entryToDate
	 * @param exitDate
	 * @param arrestPeriod
	 * @return
	 * @throws BusinessException
	 */
	public static List<PenaltyArrestData> getPenaltyArrest(Long id, Long arrestedEmployeeId, Date entryFromDate, Date entryToDate, Date exitDate, Integer arrestPeriod) throws BusinessException {
		try {
			return searchPenaltyArrest(id == null ? FlagsEnum.ALL.getCode() : id, arrestedEmployeeId == null ? FlagsEnum.ALL.getCode() : arrestedEmployeeId, entryFromDate, entryToDate, exitDate, arrestPeriod == null ? FlagsEnum.ALL.getCode() : arrestPeriod, FlagsEnum.ALL.getCode(), WFInstanceStatusEnum.COMPLETED.getCode());
		} catch (NoDataException e) {
			return new ArrayList<PenaltyArrestData>();
		}
	}

	/**
	 * search penaltyArrestData
	 * 
	 * @param id
	 * @param arrestedEmployeeId
	 * @param entryFromDate
	 * @param entryToDate
	 * @param existDate
	 * @param arrestPeriod
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<PenaltyArrestData> searchPenaltyArrest(long id, long arrestedEmployeeId, Date entryFromDate, Date entryToDate, Date exitDate, int arrestPeriod, long wfInstanceId, int wfStatus) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_ARRESTED_EMPLOYEE_ID", arrestedEmployeeId);
			qParams.put("P_ENTRY_FROM_DATE_NULL", entryFromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ENTRY_FROM_DATE", entryFromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(entryFromDate));
			qParams.put("P_ENTRY_TO_DATE_NULL", entryToDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ENTRY_TO_DATE", entryToDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(entryToDate));
			qParams.put("P_EXIT_DATE_NULL", exitDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_EXIT_DATE", exitDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(exitDate));
			qParams.put("P_ARREST_PERIOD", arrestPeriod);
			qParams.put("P_WFINSTANCE_ID", wfInstanceId);
			qParams.put("P_WFINSTANCE_STATUS", wfStatus);
			return DataAccess.executeNamedQuery(PenaltyArrestData.class, QueryNamesEnum.PENALTY_ARREST_DATA_SEARCH.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(PenaltyArrestService.class, e, "PenaltyArrestService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * validate if there is any running penalty arrest for arrested employee
	 * 
	 * @param arrestedEmployeeId
	 * @return
	 * @throws BusinessException
	 */
	private static boolean validatePenaltyrrestEmployee(long arrestedEmployeeId) throws BusinessException {
		try {
			return getPenaltyArrestEmployeeRunning(arrestedEmployeeId) > 0 ? true : false;
		} catch (NoDataException e) {
			return false;
		}
	}

	/**
	 * validate if there is any running penalty arrest for arrested employee
	 * 
	 * @param arrestedEmployeeId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static Long getPenaltyArrestEmployeeRunning(long arrestedEmployeeId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ARRESTED_EMPLOYEE_ID", arrestedEmployeeId);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.PENALTY_ARREST_DATA_VALIDATE_PENALTY_ARREST_DATA.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(PenaltyArrestService.class, e, "PenaltyArrestService");
			throw new BusinessException("error_DBError");
		}
	}
}
