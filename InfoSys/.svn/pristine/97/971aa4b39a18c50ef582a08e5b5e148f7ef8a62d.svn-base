package com.code.services.infosys.securityaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.securitymission.CarPermit;
import com.code.dal.orm.securitymission.EmpNonEmpCarPermitsData;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeAttendance;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeAttendanceCarData;
import com.code.dal.orm.securitymission.EmployeeNonEmployeeCars;
import com.code.dal.orm.securitymission.NonEmployeePermit;
import com.code.dal.orm.securitymission.VisitorEntranceData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public class EmployeeNonEmployeeCarService extends BaseService {
	private EmployeeNonEmployeeCarService() {
	}

	/**
	 * Save new mission attendance entry record for specific mission attendance
	 * 
	 * @param EmployeeNonEmployeeAttendance
	 * @param visitorEntranceData
	 * @param loginEmpData
	 * @param useSession
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeNonEmployeeAttendanceCarData saveEmployeeNonEmployeeAttendanceDetailsEntry(EmployeeNonEmployeeAttendance EmployeeNonEmployeeAttendance, VisitorEntranceData visitorEntranceData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			EmployeeNonEmployeeAttendance.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(EmployeeNonEmployeeAttendance, session);
			if (visitorEntranceData != null) {
				VisitorsEntranceService.updateVisitorEntrance(loginEmpData, visitorEntranceData, session);
			}
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeAttendanceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

		return getEmployeeNonEmployeeAttendanceCarData(EmployeeNonEmployeeAttendance.getId(), null, null).get(0);
	}

	/**
	 * update new mission attendance entry record for specific mission attendance
	 * 
	 * @param EmployeeNonEmployeeAttendance
	 * @param loginEmpData
	 * @param useSession
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeNonEmployeeAttendanceCarData updateEmployeeNonEmployeeAttendanceDetailsEntry(EmployeeNonEmployeeAttendance EmployeeNonEmployeeAttendance, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			EmployeeNonEmployeeAttendance.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(EmployeeNonEmployeeAttendance, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeAttendanceService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

		return getEmployeeNonEmployeeAttendanceCarData(EmployeeNonEmployeeAttendance.getId(), null, null).get(0);
	}

	/****************************** Employee Non Employee Car Permits *************************/
	/**
	 * Save employee non employee car with permits
	 * 
	 * @param empNonEmpCarPermitsData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveEmployeeNonEmployeeCarPermit(EmpNonEmpCarPermitsData empNonEmpCarPermitsData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateEmployeeNonEmployeeCarPermit(empNonEmpCarPermitsData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			empNonEmpCarPermitsData.getEmpNonEmpCars().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(empNonEmpCarPermitsData.getEmpNonEmpCars(), session);
			empNonEmpCarPermitsData.setId(empNonEmpCarPermitsData.getEmpNonEmpCars().getId());
			if (empNonEmpCarPermitsData.getPermitNo() != null && !empNonEmpCarPermitsData.getPermitNo().trim().isEmpty()) {
				empNonEmpCarPermitsData.getCarPermit().setSystemUser(loginEmpData.getEmpId().toString());
				empNonEmpCarPermitsData.getCarPermit().setEmployeeNonEmployeeCarsId(empNonEmpCarPermitsData.getEmpNonEmpCars().getId());
				DataAccess.addEntity(empNonEmpCarPermitsData.getCarPermit(), session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			empNonEmpCarPermitsData.setId(null);
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * validate mandatory fields
	 * 
	 * @param empNonEmpCarPermitsData
	 * @throws BusinessException
	 */
	private static void validateEmployeeNonEmployeeCarPermit(EmpNonEmpCarPermitsData empNonEmpCarPermitsData) throws BusinessException {
		if (empNonEmpCarPermitsData.getCarModelId() == null) {
			throw new BusinessException("error_carModelMandatory");
		}
		if (empNonEmpCarPermitsData.getPlateNumber() == null || empNonEmpCarPermitsData.getPlateNumber().trim().isEmpty()) {
			throw new BusinessException("error_plateNumberMandatory");
		}
		String[] plateNumberArr = empNonEmpCarPermitsData.getPlateNumber().split(" ");
		if (getEmployeeNonEmployeeCarsByPlateNumber(plateNumberArr[3], plateNumberArr[0], plateNumberArr[1], plateNumberArr[2]).size() > 0) {
			throw new BusinessException("error_existingCarPlateNumber");
		}
	}

	/****************************** Employee Non Employee Car *************************/
	/**
	 * Save employee non employee car
	 * 
	 * @param employeeNonEmployeeCars
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveEmployeeNonEmployeeCar(EmployeeNonEmployeeCars employeeNonEmployeeCars, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateEmployeeNonEmployeeCar(employeeNonEmployeeCars);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			employeeNonEmployeeCars.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(employeeNonEmployeeCars, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update employee non employee car
	 * 
	 * @param employeeNonEmployeeCars
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateEmployeeNonEmployeeCar(EmployeeNonEmployeeCars employeeNonEmployeeCars, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateEmployeeNonEmployeeCar(employeeNonEmployeeCars);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			employeeNonEmployeeCars.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(employeeNonEmployeeCars, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * validate mandatory fields
	 * 
	 * @param empNonEmpCarPermitsData
	 * @throws BusinessException
	 */
	private static void validateEmployeeNonEmployeeCar(EmployeeNonEmployeeCars empNonEmpCarPermitsData) throws BusinessException {
		if (empNonEmpCarPermitsData.getDomainCarModelId() == null)
			throw new BusinessException("error_carModelMandatory");
		if (empNonEmpCarPermitsData.getPlateNumber() == null || empNonEmpCarPermitsData.getPlateNumber().trim().isEmpty() || empNonEmpCarPermitsData.getPlateChar1() == null || empNonEmpCarPermitsData.getPlateChar1().trim().isEmpty() || empNonEmpCarPermitsData.getPlateChar2() == null || empNonEmpCarPermitsData.getPlateChar2().trim().isEmpty() || empNonEmpCarPermitsData.getPlateChar3() == null || empNonEmpCarPermitsData.getPlateChar3().trim().isEmpty())
			throw new BusinessException("error_plateNumberMandatory");
		if (empNonEmpCarPermitsData.getEmployeeId() == null && empNonEmpCarPermitsData.getNonEmployeeId() == null)
			throw new BusinessException("error_employeeOrNonEmployeeMandatory");
		if (getEmployeeNonEmployeeCarsByPlateNumber(empNonEmpCarPermitsData.getPlateNumber(), empNonEmpCarPermitsData.getPlateChar1(), empNonEmpCarPermitsData.getPlateChar2(), empNonEmpCarPermitsData.getPlateChar3()).size() > 0) {
			throw new BusinessException("error_existingCarPlateNumber");
		}
	}

	/**
	 * Get Night Attendance Report
	 * 
	 * @param startDateString
	 * @param endDateString
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getNightAttendanceReportBytes(String startDateString, String endDateString, String startTime, String endTime, int type, String loginEmployeeName, String socialId, Long departmentId, Integer orderIndex, String sortingType, long regionId, String regionName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_START_DATE", startDateString);
			parameters.put("P_END_DATE", endDateString);
			parameters.put("P_START_TIME", startTime == null || startTime.isEmpty() ? FlagsEnum.ALL.getCode() : startTime);
			parameters.put("P_END_TIME", endTime == null || endTime.isEmpty() ? FlagsEnum.ALL.getCode() : endTime);
			parameters.put("P_TYPE", type);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_DEPARTMENT_ID", departmentId == null ? FlagsEnum.ALL.getCode() : departmentId);
			parameters.put("P_SOCIAL_ID", socialId == null || socialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() : socialId);
			parameters.put("P_ORDERED_BY_INDEX", orderIndex);
			parameters.put("P_SORTING_TYPE", sortingType);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName);
			reportName = ReportNamesEnum.NIGHT_ATTENDANCE.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_general");
		}
	}

	/*************************************************/
	/*************** Queries ************************/
	/**
	 * Get attendance details for employee or non employee
	 * 
	 * @param employeeId
	 * @param nonEmployeeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendance> getEmployeeNonEmployeeAttendance(long employeeId, long nonEmployeeId) throws BusinessException {
		return searchEmployeeNonEmployeeAttendance(employeeId, nonEmployeeId);
	}

	/**
	 * Search attendance details for employee or non employee
	 * 
	 * @param employeeId
	 * @param nonEmployeeId
	 * @return
	 * @throws BusinessException
	 */
	private static List<EmployeeNonEmployeeAttendance> searchEmployeeNonEmployeeAttendance(long employeeId, long nonEmployeeId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeAttendance.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_ATTENDANCE_SEARCH_ATTENDENCE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeAttendanceService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendance>();
		}
	}

	/**
	 * getEmployeeNonEmployeeAttendanceDetailsData
	 * 
	 * @param id
	 * @param searchDate
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceCarData(Long id, Date searchDate, Long regionId) throws BusinessException {
		return searchEmployeeNonEmployeeAttendanceCarData(id == null ? FlagsEnum.ALL.getCode() : id, searchDate, regionId == null ? FlagsEnum.ALL.getCode() : regionId);
	}

	/**
	 * searchEmployeeNonEmployeeAttendanceDetailsData
	 * 
	 * @param id
	 * @param searchDate
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	private static List<EmployeeNonEmployeeAttendanceCarData> searchEmployeeNonEmployeeAttendanceCarData(long id, Date searchDate, long regionId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_SEARCH_DATE_NULL", searchDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_SEARCH_DATE", searchDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(searchDate));
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeAttendanceCarData.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_ATTENDANCE_CAR_DATA_SEARCH_EMPLOYEE_NON_EMPLOYEE_ATTENDENCE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendanceCarData>();
		}
	}

	/**
	 * getEmployeeNonEmployeeAttendanceDetailsData
	 * 
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @param departmentId
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceCarDataDetails(int type, Date startDate, Date endDate, String startTime, String endTime, Long departmentId, String socialId, long regionId) throws BusinessException {
		return searchEmployeeNonEmployeeAttendanceCarDataDetails(type, startDate, endDate, startTime, endTime, departmentId == null ? FlagsEnum.ALL.getCode() : departmentId, socialId, regionId);
	}

	/**
	 * searchEmployeeNonEmployeeAttendanceDetailsData
	 * 
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @param departmentId
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	private static List<EmployeeNonEmployeeAttendanceCarData> searchEmployeeNonEmployeeAttendanceCarDataDetails(int type, Date startDate, Date endDate, String startTime, String endTime, long departmentId, String socialId, long regionId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_TYPE", type);
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));
			qParams.put("P_START_TIME", startTime == null || startTime.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : startTime);
			qParams.put("P_END_TIME", endTime == null || endTime.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : endTime);
			qParams.put("P_DEPARTMENT_ID", departmentId);
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : socialId);
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeAttendanceCarData.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_ATTENDANCE_CAR_DATA_SEARCH_EMPLOYEE_NON_EMPLOYEE_ATTENDENCE_CAR_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendanceCarData>();
		}
	}

	/**
	 * getNonEmployeePermit
	 * 
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static List<NonEmployeePermit> getNonEmployeePermit(Long socialId) throws BusinessException {
		return searchNonEmployeePermit(socialId == null ? FlagsEnum.ALL.getCode() : socialId, FlagsEnum.ALL.getCode());
	}

	/**
	 * getNonEmployeePermitByNonEmployeeId
	 * 
	 * @param nonEmployeeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<NonEmployeePermit> getNonEmployeePermitByNonEmployeeId(Long nonEmployeeId) throws BusinessException {
		return searchNonEmployeePermit(FlagsEnum.ALL.getCode(), nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId);
	}

	/**
	 * searchNonEmployeePermit
	 * 
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	private static List<NonEmployeePermit> searchNonEmployeePermit(long socialId, long nonEmployeeId) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SOCIAL_ID", socialId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			return DataAccess.executeNamedQuery(NonEmployeePermit.class, QueryNamesEnum.NON_EMPLOYEE_PERMIT_SEARCH_NON_EMPLOYEE_PERMIT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeAttendanceService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return new ArrayList<NonEmployeePermit>();
		}
	}

	/****************************** Employee Non Employee Attendance Car *************************/
	/**
	 * Get mission attendances for a specific employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceCarsData(long employeeId, long nonEmployeeId) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeAttendanceCarsData(FlagsEnum.ALL.getCode(), employeeId, nonEmployeeId, null);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendanceCarData>();
		}
	}

	/**
	 * getEmployeeNonEmployeeCarsById
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeNonEmployeeAttendanceCarData getEmployeeNonEmployeeAttendanceCarsDataById(long id) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeAttendanceCarsData(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param plateNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceCarsDataByPlateNumberAndEmployeeNonEmployeeId(Long employeeId, Long nonEmployeeId, String plateNumber) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeAttendanceCarsData(FlagsEnum.ALL.getCode(), employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, plateNumber);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendanceCarData>();
		}
	}

	/**
	 * 
	 * @param plateNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeAttendanceCarData> getEmployeeNonEmployeeAttendanceCarsDataByPlateNumber(String plateNumber) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeAttendanceCarsData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), plateNumber);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeAttendanceCarData>();
		}
	}

	/**
	 * Search mission attendances for a specific employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	private static List<EmployeeNonEmployeeAttendanceCarData> searchEmployeeNonEmployeeAttendanceCarsData(long id, long employeeId, long nonEmployeeId, String plateNumber) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			qParams.put("P_ID", id);
			qParams.put("P_PLATE_NUMBER", plateNumber == null || plateNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : plateNumber);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeAttendanceCarData.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_CARS_SEARCH_EMPLOYEE_NON_EMPLOYEE_CARS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		}
	}

	/****************************** Employee Non Employee Car *************************/
	/**
	 * Get mission attendances for a specific employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeCars> getEmployeeNonEmployeeCars(long employeeId, long nonEmployeeId) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeCars(FlagsEnum.ALL.getCode(), employeeId, nonEmployeeId, null, null, null, null);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeCars>();
		}
	}

	/**
	 * getEmployeeNonEmployeeCarsById
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static EmployeeNonEmployeeCars getEmployeeNonEmployeeCarsById(long id) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeCars(id, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param employeeId
	 * @param nonEmployeeId
	 * @param plateNumber
	 * @param plateChar1
	 * @param plateChar2
	 * @param plateChar3
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeCars> getEmployeeNonEmployeeCarsByPlateNumberAndEmployeeNonEmployeeId(Long employeeId, Long nonEmployeeId, String plateNumber, String plateChar1, String plateChar2, String plateChar3) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeCars(FlagsEnum.ALL.getCode(), employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, nonEmployeeId == null ? FlagsEnum.ALL.getCode() : nonEmployeeId, plateNumber, plateChar1, plateChar2, plateChar3);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeCars>();
		}
	}

	/**
	 * 
	 * @param plateNumber
	 * @param plateChar1
	 * @param plateChar2
	 * @param plateChar3
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmployeeNonEmployeeCars> getEmployeeNonEmployeeCarsByPlateNumber(String plateNumber, String plateChar1, String plateChar2, String plateChar3) throws BusinessException {
		try {
			return searchEmployeeNonEmployeeCars(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), plateNumber, plateChar1, plateChar2, plateChar3);
		} catch (NoDataException e) {
			return new ArrayList<EmployeeNonEmployeeCars>();
		}
	}

	/**
	 * Search mission attendances for a specific employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws BusinessException
	 */
	private static List<EmployeeNonEmployeeCars> searchEmployeeNonEmployeeCars(long id, long employeeId, long nonEmployeeId, String plateNumber, String plateChar1, String plateChar2, String plateChar3) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMPLOYEE_ID", nonEmployeeId);
			qParams.put("P_ID", id);
			qParams.put("P_PLATE_NUMBER_FLAG", (plateNumber == null || plateNumber.isEmpty()) && (plateChar1 == null || plateChar1.isEmpty()) && (plateChar2 == null || plateChar2.isEmpty()) && (plateChar3 == null || plateChar3.isEmpty()) ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_PLATE_CHAR_1", plateChar1 == null || plateChar1.isEmpty() ? "" : plateChar1);
			qParams.put("P_PLATE_CHAR_2", plateChar2 == null || plateChar2.isEmpty() ? "" : plateChar2);
			qParams.put("P_PLATE_CHAR_3", plateChar3 == null || plateChar3.isEmpty() ? "" : plateChar3);
			qParams.put("P_PLATE_NUMBER", plateNumber == null || plateNumber.isEmpty() ? "" : plateNumber);
			return DataAccess.executeNamedQuery(EmployeeNonEmployeeCars.class, QueryNamesEnum.EMPLOYEE_NON_EMPLOYEE_CARS_SEARCH_EMPLOYEE_NON_EMPLOYEE_CARS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		}
	}

	/********************************************** Car Permits **********************************************/

	/**
	 * getCarPermit
	 * 
	 * @param permitNo
	 * @param plateNumber
	 * @param plateChar1
	 * @param plateChar2
	 * @param plateChar3
	 * @return
	 * @throws BusinessException
	 */
	public static List<CarPermit> getCarPermit(String permitNo, String plateNumber, String plateChar1, String plateChar2, String plateChar3) throws BusinessException {
		try {
			return searchCarPermit(permitNo, (plateNumber == null || plateNumber.isEmpty()) && (permitNo == null || permitNo.isEmpty()) ? " " : plateNumber, plateChar1, plateChar2, plateChar3);
		} catch (NoDataException e) {
			return new ArrayList<CarPermit>();
		}
	}

	/**
	 * searchCarPermit
	 * 
	 * @param permitNo
	 * @param plateNumber
	 * @param plateChar1
	 * @param plateChar2
	 * @param plateChar3
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<CarPermit> searchCarPermit(String permitNo, String plateNumber, String plateChar1, String plateChar2, String plateChar3) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_PERMIT_NO", permitNo == null || permitNo.isEmpty() ? FlagsEnum.ALL.getCode() + "" : permitNo);
			qParams.put("P_PLATE_NUMBER_FLAG", (plateNumber == null || plateNumber.isEmpty()) && (plateChar1 == null || plateChar1.isEmpty()) && (plateChar2 == null || plateChar2.isEmpty()) && (plateChar3 == null || plateChar3.isEmpty()) ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_PLATE_CHAR_1", plateChar1 == null || plateChar1.isEmpty() ? "" : plateChar1);
			qParams.put("P_PLATE_CHAR_2", plateChar2 == null || plateChar2.isEmpty() ? "" : plateChar2);
			qParams.put("P_PLATE_CHAR_3", plateChar3 == null || plateChar3.isEmpty() ? "" : plateChar3);
			qParams.put("P_PLATE_NUMBER", plateNumber == null || plateNumber.isEmpty() ? "" : plateNumber);
			return DataAccess.executeNamedQuery(CarPermit.class, QueryNamesEnum.CAR_PERMIT_SEARCH_CAR_PERMIT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @param permitNo
	 * @param plateNumber
	 * @return
	 * @throws BusinessException
	 */
	public static List<EmpNonEmpCarPermitsData> getEmpNonEmpCarPermitsData(Long empId, Long nonEmpId, String permitNo, String plateNumber) throws BusinessException {
		try {
			return searchEmpNonEmpCarPermitsData(empId == null ? FlagsEnum.ALL.getCode() : empId, nonEmpId == null ? FlagsEnum.ALL.getCode() : nonEmpId, permitNo, plateNumber);
		} catch (NoDataException e) {
			return new ArrayList<EmpNonEmpCarPermitsData>();
		}
	}

	/**
	 * 
	 * @param empId
	 * @param nonEmpId
	 * @param permitNo
	 * @param plateNumber
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<EmpNonEmpCarPermitsData> searchEmpNonEmpCarPermitsData(long empId, long nonEmpId, String permitNo, String plateNumber) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMP_ID", empId);
			qParams.put("P_NON_EMP_ID", nonEmpId);
			qParams.put("P_PERMIT_NO", permitNo == null || permitNo.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : permitNo);
			qParams.put("P_PLATE_NUMBER", plateNumber == null || plateNumber.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : plateNumber);
			return DataAccess.executeNamedQuery(EmpNonEmpCarPermitsData.class, QueryNamesEnum.EMP_NON_EMP_CAR_PERMITS_DATA_SEARCH_EMP_NON_EMP_CAR_PERMITS_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(EmployeeNonEmployeeCarService.class, e, "EmployeeNonEmployeeCarService");
			throw new BusinessException("error_DBError");
		}
	}
}
