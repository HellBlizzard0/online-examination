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
import com.code.dal.orm.labcheck.LabCheckReport;
import com.code.dal.orm.labcheck.LabCheckReportData;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentAction;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentData;
import com.code.dal.orm.labcheck.LabCheckReportDepartmentEmployeeData;
import com.code.dal.orm.labcheck.LabCheckReportEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.enums.FlagsEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

public class MissionReportService extends BaseService {

	/**
	 * Saving new LabCheckReport
	 * 
	 * @param labCheckReport
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveMissionReportData(LabCheckReport labCheckReport, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateMissionReportData(labCheckReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReport.setSystemUser(loginEmpData.getEmpId().toString());
			labCheckReport.setCheckedNumber(LabCheckService.getCountLabCheckEmployees(labCheckReport.getLabChecksId()).intValue());
			DataAccess.addEntity(labCheckReport, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save new lapCheckReport employee
	 * 
	 * @param labCheckReportEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveMissionReportEmployee(LabCheckReportEmployeeData labCheckReportEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			// check if employee exist before
			List<LabCheckReportEmployeeData> existingEmployee = getLabCheckReportEmployeeData(labCheckReportEmployeeData.getLabCheckReportId(), labCheckReportEmployeeData.getEmployeeId());
			if (!existingEmployee.isEmpty()) {
				throw new BusinessException("error_alreadyChoosen");
			}
			labCheckReportEmployeeData.getLabCheckReportEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(labCheckReportEmployeeData.getLabCheckReportEmployee(), session);
			labCheckReportEmployeeData.setId(labCheckReportEmployeeData.getLabCheckReportEmployee().getId());

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
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save new lapCheckReport department/sector
	 * 
	 * @param labCheckReportDepartmentData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveMissionReportDepartment(LabCheckReportDepartmentData labCheckReportDepartmentData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentData(labCheckReportDepartmentData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			List<LabCheckReportDepartmentData> labCheckReportDepartmentDataList = getLabCheckReportDepartmentData(labCheckReportDepartmentData.getLabCheckReportId(), labCheckReportDepartmentData.getDepartmentId());
			if (!labCheckReportDepartmentDataList.isEmpty()) {
				throw new BusinessException("error_alreadyChoosenSector");
			}

			labCheckReportDepartmentData.getLabCheckReportDepartment().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(labCheckReportDepartmentData.getLabCheckReportDepartment(), session);
			labCheckReportDepartmentData.setId((labCheckReportDepartmentData.getLabCheckReportDepartment().getId()));

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save new lapCheckReportDepartment action
	 * 
	 * @param labCheckReportDepartmentAction
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveLabCheckReportDepartmentAction(LabCheckReportDepartmentAction labCheckReportDepartmentAction, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentAction(labCheckReportDepartmentAction);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentAction.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(labCheckReportDepartmentAction, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Save lapCheckReportDepartment employee
	 * 
	 * @param labCheckReportDepartmentEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void saveLabCheckReportDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentEmployee(labCheckReportDepartmentEmployeeData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.addEntity(labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee(), session);
			labCheckReportDepartmentEmployeeData.setId(labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee().getId());

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Updating LabCheckReport
	 * 
	 * @param labCheckReport
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateMissionReportData(LabCheckReport labCheckReport, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateMissionReportData(labCheckReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReport.setSystemUser(loginEmpData.getEmpId().toString());
			labCheckReport.setCheckedNumber(LabCheckService.getCountLabCheckEmployees(labCheckReport.getLabChecksId()).intValue());
			DataAccess.updateEntity(labCheckReport, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update
	 * 
	 * @param labCheckReportDepartmentData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateMissionReportDepartment(LabCheckReportDepartmentData labCheckReportDepartmentData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentData(labCheckReportDepartmentData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentData.getLabCheckReportDepartment().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(labCheckReportDepartmentData.getLabCheckReportDepartment(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update labCheckReportDepartment action
	 * 
	 * @param labCheckReportDepartmentAction
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateLabCheckReportDepartmentAction(LabCheckReportDepartmentAction labCheckReportDepartmentAction, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentAction(labCheckReportDepartmentAction);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentAction.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(labCheckReportDepartmentAction, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Update labCheckReportDepartment employee
	 * 
	 * @param labCheckReportDepartmentEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateLabCheckReportDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		validateLabCheckReportDepartmentEmployee(labCheckReportDepartmentEmployeeData);
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.updateEntity(labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete mission report
	 * 
	 * @param labCheckReport
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteMissionReport(LabCheckReport labCheckReport, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			labCheckReport.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(labCheckReport, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete labCheckReport department
	 * 
	 * @param deletedLabCheckReportDepartmentData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteMissionReportDepartment(LabCheckReportDepartmentData deletedLabCheckReportDepartmentData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			deletedLabCheckReportDepartmentData.getLabCheckReportDepartment().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(deletedLabCheckReportDepartmentData.getLabCheckReportDepartment(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof DatabaseConstraintException) {
				throw new BusinessException("error_relatedData");
			} else if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete labCheckReport employee
	 * 
	 * @param deletedEmployee
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */

	public static void deleteMissionReportEmployee(LabCheckReportEmployeeData deletedEmployee, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			deletedEmployee.getLabCheckReportEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(deletedEmployee.getLabCheckReportEmployee(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete labCheckReportDepartment employee
	 * 
	 * @param labCheckReportDepartmentEmployeeData
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteLabCheckReportDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee().setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(labCheckReportDepartmentEmployeeData.getLabCheckReportDepartmentEmployee(), session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Delete labCheckReportDepartment action
	 * 
	 * @param labCheckReportDepartmentAction
	 * @param loginEmpData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteLabCheckReportDepartmentAction(LabCheckReportDepartmentAction labCheckReportDepartmentAction, EmployeeData loginEmpData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheckReportDepartmentAction.setSystemUser(loginEmpData.getEmpId().toString());
			DataAccess.deleteEntity(labCheckReportDepartmentAction, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * validate mission report business rules
	 * 
	 * @param labCheckReport
	 * @throws BusinessException
	 */
	private static void validateMissionReportData(LabCheckReport labCheckReport) throws BusinessException {
		List<LabCheck> labCheckList = LabCheckService.getLabCheckByOrderNumberAndCheckReason(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode(), labCheckReport.getOrderNumber());
		LabCheck labCheck = labCheckList.isEmpty() ? null : labCheckList.get(0);
		if (labCheck == null) {
			throw new BusinessException("error_labCheckNotExist");
		} else {
			labCheckReport.setLabChecksId(labCheck.getId());
		}

		if (labCheckReport.getOrderNumber() == null || (labCheckReport.getOrderNumber().trim().isEmpty())) {
			throw new BusinessException("error_orderNumberMandatory");
		}

		if (labCheckReport.getDestinationDepartmentId() == null) {
			throw new BusinessException("error_destinationDepartmentMandatory");
		}

		if (labCheckReport.getStartDate() == null) {
			throw new BusinessException("error_missionStartDateMandatory");
		}

		if (labCheckReport.getEndDate() == null) {
			throw new BusinessException("error_missionEndDateMandatory");
		}

		if (labCheckReport.getStartDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_missionStartDateCantExceedCurrentDate");
		}

		if (labCheckReport.getStartDate().after(labCheckReport.getEndDate())) {
			throw new BusinessException("error_missionStartDateCantExceedEndDate");
		}

		if (labCheckReport.getMissionTypeId() == null) {
			throw new BusinessException("error_missionTypeMandatory");
		}

		if (labCheckReport.getPeriod() == null) {
			throw new BusinessException("error_missionPeriodMandatory");
		}

		if (labCheckReport.getPeriod() > 999 || labCheckReport.getPeriod() <= 0) {
			throw new BusinessException("error_invalidPeriod");
		}

		if (labCheckReport.getCheckStartDate() == null) {
			throw new BusinessException("error_checkStartDateMandatory");
		}

		if (labCheckReport.getCheckEndDate() == null) {
			throw new BusinessException("error_checkEndDateMandatory");
		}

		if (labCheckReport.getCheckStartDate().after(labCheckReport.getCheckEndDate())) {
			throw new BusinessException("error_checkStartDateCantExceedEndDate");
		}

		if (labCheckReport.getPros() == null || (labCheckReport.getPros().trim().isEmpty())) {
			throw new BusinessException("error_prosMandatory");
		}

		if (labCheckReport.getCons() == null || (labCheckReport.getCons().trim().isEmpty())) {
			throw new BusinessException("error_consMandatory");
		}
	}

	/**
	 * Check business rules for labCheckReport department
	 * 
	 * @param labCheckReportDepartmentDataList
	 * @throws BusinessException
	 */
	private static void validateLabCheckReportDepartmentData(LabCheckReportDepartmentData labCheckReportDepartmentData) throws BusinessException {
		if (labCheckReportDepartmentData.getForcesActual() == null) {
			throw new BusinessException("error_forcesActualMandatory");
		}
		if (labCheckReportDepartmentData.getForcesActual() < 1) {
			throw new BusinessException("error_forcesActualMustBeMoreThanOne");
		}
		if (labCheckReportDepartmentData.getForcesNumber() == null) {
			throw new BusinessException("error_forcesNumberMandatory");
		}
		if (labCheckReportDepartmentData.getForcesNumber() < 1) {
			throw new BusinessException("error_forcesNumberMustBeMoreThanOne");
		}
	}

	/**
	 * Check business rules for labCheckReportDepartment action
	 * 
	 * @param labCheckReportDepartmentAction
	 * @throws BusinessException
	 */
	private static void validateLabCheckReportDepartmentAction(LabCheckReportDepartmentAction labCheckReportDepartmentAction) throws BusinessException {
		if (labCheckReportDepartmentAction.getActionName() == null || (labCheckReportDepartmentAction.getActionName().trim().isEmpty())) {
			throw new BusinessException("error_actionNameMandatory");
		}
		if (labCheckReportDepartmentAction.getActionLocation() == null || (labCheckReportDepartmentAction.getActionLocation().trim().isEmpty())) {
			throw new BusinessException("error_actionLocationMandatory");
		}

		if (labCheckReportDepartmentAction.getActionDate() == null) {
			throw new BusinessException("error_actionDateMandatory");
		}
	}

	/**
	 * Check business rules for labCheckReportDepartment employee
	 * 
	 * @param labCheckReportDepartmentEmployeeData
	 * @throws BusinessException
	 */
	private static void validateLabCheckReportDepartmentEmployee(LabCheckReportDepartmentEmployeeData labCheckReportDepartmentEmployeeData) throws BusinessException {
		if (labCheckReportDepartmentEmployeeData.getDomainHospitalId() == null) {
			throw new BusinessException("error_hospitalMandatory");
		}
		if (labCheckReportDepartmentEmployeeData.getEmployeeId() == null) {
			throw new BusinessException("error_employeeMandatory");
		}

		if (labCheckReportDepartmentEmployeeData.getForwardDate() == null) {
			throw new BusinessException("error_forwardDateMandatory");
		}
	}

	/**
	 * getMissionReportBytes
	 * 
	 * @param missionId
	 * @param employeeId
	 * @param missionDstId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMissionReportBytes(long missionId, long employeeId, long missionDstId, Date fromDate, Date toDate, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_MISSION_TYPE", missionId);
			parameters.put("P_EMP_ID", employeeId);
			parameters.put("P_MISSION_DST", missionDstId);
			parameters.put("P_FROM_DATE", HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE", HijriDateService.getHijriDateString(toDate));
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.LAB_CHECK_MISSION_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get MissionReport Details Report
	 * 
	 * @param missionReportId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getMissionReportDetailsBytes(long missionReportId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_MISSION_REPORT_ID", missionReportId);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			reportName = ReportNamesEnum.LAB_CHECK_MISSION_REPORT_DETAILS.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_general");
		}
	}

	/*************************************************/
	/*************** Queries ************************/

	/**
	 * Get lab Check Report by id
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public static LabCheckReport getLabCheck(long id) throws BusinessException {
		try {
			return searchLabCheck(id, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode()).get(0).getLabCheckReport();
		} catch (NoDataException e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Lab Check Report
	 * 
	 * @param startDate
	 * @param endDate
	 * @param destinationDepartmentId
	 * @param missionTypeId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckReportData> getLabCheck(Date startDate, Date endDate, long destinationDepartmentId, long missionTypeId) throws BusinessException {
		try {
			return searchLabCheck(FlagsEnum.ALL.getCode(), startDate, endDate, destinationDepartmentId, missionTypeId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckReportData>();
		}
	}

	/**
	 * Search Lab Check Report
	 * 
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @param destinationDepartmentId
	 * @param missionTypeId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckReportData> searchLabCheck(long id, Date startDate, Date endDate, long destinationDepartmentId, long missionTypeId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ID", id);
			qParams.put("P_DESTINATION_DEPARTMENT_ID", destinationDepartmentId);
			qParams.put("P_MISSION_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_MISSION_END_DATE_NULL", endDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_MISSION_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));
			qParams.put("P_MISSION_TYPE", missionTypeId);

			return DataAccess.executeNamedQuery(LabCheckReportData.class, QueryNamesEnum.LAB_CHECK_REPORT_SEARCH_LAB_CHECK_REPORT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Lab Check Report Department Employee
	 * 
	 * @param labCheckReportId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckReportEmployeeData> getLabCheckReportEmployeeData(long labCheckReportId, long employeeId) throws BusinessException {
		try {
			return searchLabCheckReportEmployeeData(labCheckReportId, employeeId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckReportEmployeeData>();
		}
	}

	/**
	 * Search Lab Check Report Employee
	 * 
	 * @param labCheckReportId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckReportEmployeeData> searchLabCheckReportEmployeeData(long labCheckReportId, long employeeId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_REPORT_ID", labCheckReportId);
			qParams.put("P_EMPLOYEE_ID", employeeId);

			return DataAccess.executeNamedQuery(LabCheckReportEmployeeData.class, QueryNamesEnum.LAB_CHECK_REPORT_EMPLOYEE_DATA_SEARCH_LAB_CHECK_REPORT_EMPLOYEE.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * GetLab Check Report Department department
	 * 
	 * @param labCheckReportId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckReportDepartmentData> getLabCheckReportDepartmentData(long labCheckReportId, long departmentId) throws BusinessException {
		try {
			return searchLabCheckReportDepartmentData(labCheckReportId, departmentId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckReportDepartmentData>();
		}
	}

	/**
	 * Search Lab Check Report Department
	 * 
	 * @param labCheckReportId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckReportDepartmentData> searchLabCheckReportDepartmentData(long labCheckReportId, long departmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_REPORT_ID", labCheckReportId);
			qParams.put("P_DEPARTMENT_ID", departmentId);

			return DataAccess.executeNamedQuery(LabCheckReportDepartmentData.class, QueryNamesEnum.LAB_CHECK_REPORT_DEPARTMENT_DATA_SEARCH_LAB_CHECK_REPORT_DEPARTMENT_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * get Lab Check Report Department Employee
	 * 
	 * @param labCheckReportDepartmentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckReportDepartmentAction> getLabCheckReportDepartmentAction(long labCheckReportDepartmentId) throws BusinessException {
		try {
			return searchLabCheckReportDepartmentAction(labCheckReportDepartmentId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckReportDepartmentAction>();
		}
	}

	/**
	 * Search Lab Check Report Department actions
	 * 
	 * @param labCheckReportDepartmentId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckReportDepartmentAction> searchLabCheckReportDepartmentAction(long labCheckReportDepartmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_REPORT_DEPARTMENT_ID", labCheckReportDepartmentId);
			return DataAccess.executeNamedQuery(LabCheckReportDepartmentAction.class, QueryNamesEnum.LAB_CHECK_REPORT_DEPARTMENT_ACTION_SEARCH_LAB_CHECK_REPORT_DEPARTMENT_ACTION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * get Lab Check Report Department Employee
	 * 
	 * @param labCheckReportDepartmentId
	 * @return
	 * @throws BusinessException
	 */
	public static List<LabCheckReportDepartmentEmployeeData> getLabCheckReportDepartmentEmployee(long labCheckReportDepartmentId) throws BusinessException {
		try {
			return searchLabCheckReportDepartmentEmployee(labCheckReportDepartmentId);
		} catch (NoDataException e) {
			return new ArrayList<LabCheckReportDepartmentEmployeeData>();
		}
	}

	/**
	 * Search Lab Check Report Department Employee
	 * 
	 * @param labCheckReportDepartmentId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<LabCheckReportDepartmentEmployeeData> searchLabCheckReportDepartmentEmployee(long labCheckReportDepartmentId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_DEPARTMENT_ID", labCheckReportDepartmentId);
			return DataAccess.executeNamedQuery(LabCheckReportDepartmentEmployeeData.class, QueryNamesEnum.LAB_CHECK_REPORT_DEPARTMENT_EMPLOYEE_DATA_SEARCH_LAB_CHECK_REPORT_DEPARTMENT_EMPLOYEE_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Count lab checks attached to a specific order number (should be 1 or 0 )
	 * 
	 * @param labCheckOrderNumber
	 * @return
	 * @throws BusinessException
	 */
	public static long getCountLabChecksPerOrderNumber(String labCheckOrderNumber) throws BusinessException {
		return countLabChecksPerOrderNumber(labCheckOrderNumber);
	}

	/**
	 * Count lab checks attached to a specific order number (should be 1 or 0 )
	 * 
	 * @param labCheckOrderNumber
	 * @return
	 * @throws BusinessException
	 */

	private static long countLabChecksPerOrderNumber(String labCheckOrderNumber) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_LAB_CHECK_ORDER_NUMBER", labCheckOrderNumber);
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.LAB_CHECK_REPORT_DATA_COUNT_LAB_CHECK_REPORTS_PER_ORDER_NUMBER.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(MissionReportService.class, e, "MissionReportService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {
			return 0L;
		}
	}

}