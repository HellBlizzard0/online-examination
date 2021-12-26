package com.code.services.infosys.surveillance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.ReportService;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.dal.orm.surveillance.SurveillanceActionData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceOrder;
import com.code.dal.orm.surveillance.SurveillanceOrderData;
import com.code.dal.orm.surveillance.SurveillanceReport;
import com.code.dal.orm.surveillance.SurveillanceReportDetail;
import com.code.dal.orm.surveillance.SurveillanceReportDetailData;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryNamesEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.SurveillanceReportRecommendationEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.BaseService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.services.workflow.surveillance.SurveillanceReportWorkFlow;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;

public class SurveillanceOrdersService extends BaseService {
	private SurveillanceOrdersService() {
	}

	/**
	 * Add new surveillanceOrder with employees after validation and finally start wf
	 * 
	 * @param loginUser
	 * @param surveillanceOrderData
	 * @param surveillanceEmployeeDataList
	 * @param isSaveOnly
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSurveillanceOrder(EmployeeData loginUser, SurveillanceOrderData surveillanceOrderData, List<SurveillanceEmpNonEmpData> surveillanceEmployeeDataList, List<SurveillanceEmpNonEmpData> surveillanceEmployeeDataDeletedList, boolean isSaveOnly, boolean verbalOrderFlag, CustomSession... useSession) throws BusinessException {
		validateSurveillanceOrder(surveillanceOrderData, surveillanceEmployeeDataList, surveillanceOrderData.getId() == null ? false : true, verbalOrderFlag);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		boolean isSave = false;
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceOrderData.getSurveillanceOrder().setSystemUser(loginUser.getEmpId().toString());
			if (surveillanceOrderData.getId() == null) {
				DataAccess.addEntity(surveillanceOrderData.getSurveillanceOrder(), session);
				surveillanceOrderData.setId(surveillanceOrderData.getSurveillanceOrder().getId());
				isSave = true;
			} else {
				DataAccess.updateEntity(surveillanceOrderData.getSurveillanceOrder(), session);
			}
			// add employees and generate reports
			deleteSurveillanceOrderEmployees(loginUser, surveillanceEmployeeDataDeletedList, session);
			addSurveillanceOrderEmployees(loginUser, surveillanceOrderData, surveillanceEmployeeDataList, session);
			// start Wf
			if (!isSaveOnly) {
				SurveillanceWorkFlow.initSurveillanceOrder(surveillanceOrderData.getSurveillanceOrder(), loginUser, null, session);
			}

			if (!isOpenedSession) {
				session.commitTransaction();
			}
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (isSave) {
				surveillanceOrderData.setId(null);
			}

			for (SurveillanceEmpNonEmpData surveillanceEmployeeData : surveillanceEmployeeDataList) {
				if (surveillanceEmployeeData.getSelected()) {
					surveillanceEmployeeData.setId(null);
				}
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate surveillance Order information : Check Mandatory fields, repeated order number, employees with overlapped surveillance orders, validate date, one employee entered at least
	 * 
	 * @param surveillanceOrder
	 * @param surveillanceEmployeeList
	 * @throws BusinessException
	 */
	private static void validateSurveillanceOrder(SurveillanceOrderData surveillanceOrder, List<SurveillanceEmpNonEmpData> surveillanceEmployeeList, boolean update, boolean verbalOrderFlag) throws BusinessException {
		// Check Mandatory fields
		if (surveillanceOrder.getOrderDate() == null) {
			throw new BusinessException("error_orderDateMandatory");
		}

		if (surveillanceOrder.getStartDate() == null) {
			throw new BusinessException("error_survStartDateMandatory");
		}

		if (surveillanceOrder.getPeriodicReporting() == null) {
			throw new BusinessException("error_periodicReportingMandatory");
		}

		if (surveillanceOrder.getFinalApprovalEntity() == null) {
			throw new BusinessException("error_finalMandatory");
		}

		if (!verbalOrderFlag && (surveillanceOrder.getOrderNumber() == null || surveillanceOrder.getOrderNumber().trim().equals(""))) {
			throw new BusinessException("error_orderNumberMandatory");
		}

		if (surveillanceOrder.getPeriodMonths() != null && (surveillanceOrder.getPeriodMonths() <= 0 || surveillanceOrder.getPeriodMonths() > 12)) {
			throw new BusinessException("error_assumedPeriodMonthsBetween0And12");
		}

		// one employee entered at least
		if (surveillanceEmployeeList.size() == 0) {
			throw new BusinessException("error_atLeastOneChoosed");
		}

		// validate date
		if (surveillanceOrder.getOrderDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_orderDateAfterToday");
		}

		if (!update) {
			// check repeated order numbers
			if (!verbalOrderFlag && (surveillanceOrder.getOrderNumber() != null)) {
				try {
					searchSurveillanceOrderData(FlagsEnum.ALL.getCode(), surveillanceOrder.getOrderNumber(), FlagsEnum.ALL.getCode(), surveillanceOrder.getOrderDateString().split("/")[2], surveillanceOrder.getRegionId());
					throw new BusinessException("error_orderNumberDuplicate");
				} catch (NoDataException e) {
				}
			}

			// check for employees with overlapped surveillance orders
			boolean found = false;
			for (SurveillanceEmpNonEmpData emp : surveillanceEmployeeList) {
				try {
					List<SurveillanceEmpNonEmpData> empNonEmpList = null;
					if (emp.getEmployeeId() != null) {
						empNonEmpList = getActiveSurveillanceEmpNonEmpData(emp.getEmployeeId(), FlagsEnum.ON.getCode());
					} else {
						empNonEmpList = getActiveSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), emp.getNonEmployeeId());
					}
					for (SurveillanceEmpNonEmpData empNonEmp : empNonEmpList) {
						if (surveillanceOrder.getStartDate().before(empNonEmp.getEndDate())) {
							found = true;
						}
					}

				} catch (NoDataException e) {
				}
			}
			if (found) {
				throw new BusinessException("error_survEmployeeDuplicate");
			}
		}

	}

	/**
	 * Adding employees to surveillanceOrder
	 * 
	 * @param loginUser
	 * @param surveillanceOrderData
	 * @param surveillanceEmployeeList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void addSurveillanceOrderEmployees(EmployeeData loginUser, SurveillanceOrderData surveillanceOrderData, List<SurveillanceEmpNonEmpData> surveillanceEmployeeList, CustomSession... useSession) throws BusinessException {
		for (SurveillanceEmpNonEmpData employee : surveillanceEmployeeList) {
			validateSurveillanceEmployee(employee);
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SurveillanceEmpNonEmpData employeeData : surveillanceEmployeeList) {
				employeeData.setEndDate(HijriDateService.addSubHijriMonthsDays(surveillanceOrderData.getStartDate(), employeeData.getPeriodMonths(), 0));
				employeeData.setSurveillanceOrderId(surveillanceOrderData.getSurveillanceOrder().getId());

				employeeData.getSurveillanceEmployee().setSystemUser(loginUser.getEmpId().toString());
				if (employeeData.getSurveillanceEmployee().getId() == null) {
					DataAccess.addEntity(employeeData.getSurveillanceEmployee(), session);
				} else {
					DataAccess.updateEntity(employeeData.getSurveillanceEmployee(), session);
				}
				employeeData.setId(employeeData.getSurveillanceEmployee().getId());
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Delete surveillance Order Employees
	 * 
	 * @param loginUser
	 * @param surveillanceEmployeeDeletedList
	 * @param useSession
	 * @throws BusinessException
	 */
	private static void deleteSurveillanceOrderEmployees(EmployeeData loginUser, List<SurveillanceEmpNonEmpData> surveillanceEmployeeDeletedList, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SurveillanceEmpNonEmpData employeeData : surveillanceEmployeeDeletedList) {
				DataAccess.deleteEntity(employeeData.getSurveillanceEmployee(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Updating SurveillanceEmployeeData
	 * 
	 * @param loginUser
	 * @param surveillanceEmployeeData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSurveillanceEmployee(EmployeeData loginUser, SurveillanceEmpNonEmpData surveillanceEmployeeData, CustomSession... useSession) throws BusinessException {
		validateSurveillanceEmployee(surveillanceEmployeeData);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceEmployeeData.getSurveillanceEmployee().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(surveillanceEmployeeData.getSurveillanceEmployee(), session);

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate Surveillance Employee : Check Mandatory fields
	 * 
	 * @param surveillanceEmployee
	 * @throws BusinessException
	 */
	private static void validateSurveillanceEmployee(SurveillanceEmpNonEmpData surveillanceEmployee) throws BusinessException {
		if (surveillanceEmployee.getPeriodMonths() == null) {
			throw new BusinessException("error_periodMonthsMandatory");
		}
		if (surveillanceEmployee.getSurveillanceReasons() == null || surveillanceEmployee.getSurveillanceReasons().trim().equals("")) {
			throw new BusinessException("error_survReasonsMandatory");
		}

		if (!surveillanceEmployee.isWorkflowFlag()) {
			if (surveillanceEmployee.getPeriodMonths() <= 0 || surveillanceEmployee.getPeriodMonths() > 12) {
				throw new BusinessException("error_periodMonthsBetween0And12");
			}
		}
	}

	/**
	 * Register SurveillanceAction for employee
	 * 
	 * @param loginUser
	 * @param surveillanceAction
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSurveillanceAction(EmployeeData loginUser, SurveillanceActionData surveillanceAction, CustomSession... useSession) throws BusinessException {
		validateSurveillanceAction(surveillanceAction);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			surveillanceAction.getSurveillanceAction().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(surveillanceAction.getSurveillanceAction(), session);
			surveillanceAction.setId(surveillanceAction.getSurveillanceAction().getId());

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate Surveillance Action information : Check Mandatory fields - event date validation
	 * 
	 * @param surveillanceAction
	 * @throws BusinessException
	 */
	private static void validateSurveillanceAction(SurveillanceActionData surveillanceAction) throws BusinessException {
		if (surveillanceAction.getEventDate() == null) {
			throw new BusinessException("error_eventDateMandatory");
		}

		if (surveillanceAction.getEventDetails() == null || surveillanceAction.getEventDetails().trim().equals("")) {
			throw new BusinessException("error_eventDetailsMandatory");
		}

		validateSurveillanceActionEventDate(surveillanceAction);
	}

	/**
	 * Surveillance action event date validation
	 * 
	 * @param surveillanceAction
	 * @throws BusinessException
	 */
	private static void validateSurveillanceActionEventDate(SurveillanceActionData surveillanceAction) throws BusinessException {
		SurveillanceEmpNonEmpData surEmployeeData = getSurveillanceEmployeeData(surveillanceAction.getSurveillanceEmpId());
		// before today and within surveillance period
		if (surveillanceAction.getEventDate().after(HijriDateService.getHijriSysDate()) || surveillanceAction.getEventDate().after(surEmployeeData.getEndDate()) || surveillanceAction.getEventDate().before(surEmployeeData.getStartDate())) {
			throw new BusinessException("error_invalidEventDatePeriod");
		}
		try {
			// can't add/edit/update action within approved report period
			searchSurveillanceReports(FlagsEnum.ALL.getCode(), surEmployeeData.getId(), null, surveillanceAction.getEventDate(), FlagsEnum.ALL.getCode(), true);
			throw new BusinessException("error_invalidEventDate");
		} catch (NoDataException e) {

		}
	}

	/**
	 * Delete SurveillanceAction on employee
	 * 
	 * @param loginUser
	 * @param surveillanceAction
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void deleteSurveillanceAction(EmployeeData loginUser, SurveillanceActionData surveillanceAction, CustomSession... useSession) throws BusinessException {
		validateSurveillanceActionEventDate(surveillanceAction);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceAction.getSurveillanceAction().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.deleteEntity(surveillanceAction.getSurveillanceAction(), session);

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update SurveillanceAction
	 * 
	 * @param loginUser
	 * @param surveillanceAction
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSurveillanceAction(EmployeeData loginUser, SurveillanceActionData surveillanceAction, CustomSession... useSession) throws BusinessException {
		validateSurveillanceAction(surveillanceAction);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceAction.getSurveillanceAction().setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(surveillanceAction.getSurveillanceAction(), session);

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Add Surveillance Reports for all employees
	 * 
	 * @param loginUser
	 * @param surveillanceOrder
	 * @param surveillanceEmployeeData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSurveillanceReports(EmployeeData loginUser, SurveillanceOrder surveillanceOrder, List<SurveillanceEmpNonEmpData> surveillanceEmployeeData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			for (SurveillanceEmpNonEmpData employeeData : surveillanceEmployeeData) {
				addSurveillanceReports(loginUser, employeeData.getPeriodMonths(), employeeData, surveillanceOrder.getStartDate(), session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}

	}

	/**
	 * Generate surveillance reports
	 * 
	 * @param loginUser
	 * @param period
	 * @param surveillanceEmployeeData
	 * @param startDate
	 * @param session
	 * @throws BusinessException
	 */
	private static void addSurveillanceReports(EmployeeData loginUser, int period, SurveillanceEmpNonEmpData surveillanceEmployeeData, Date startDate, CustomSession session) throws BusinessException {
		Date endDate = null;
		int numberOfReports = (int) Math.ceil((period * 1.0) / surveillanceEmployeeData.getPeriodicReporting());
		for (int i = 0; i < numberOfReports; i++) {
			SurveillanceReport surveillanceReport = new SurveillanceReport();
			surveillanceReport.setApproved(FlagsEnum.OFF.getCode());
			surveillanceReport.setTotal(0);
			surveillanceReport.setSurveillanceEmpId(surveillanceEmployeeData.getSurveillanceEmployee().getId());

			// set start date
			if (i == 0) { // first report
				surveillanceReport.setStartDate(startDate);
			} else { // all other reports (previous report end date + 1)
				surveillanceReport.setStartDate(HijriDateService.addSubHijriMonthsDays(endDate, 0, 1));
			}

			// set end date
			if (i == numberOfReports - 1) { // last report
				endDate = surveillanceEmployeeData.getEndDate();
			} else { // all other reports(startDate + number of months for
						// periodic reporting - 1)
				endDate = HijriDateService.addSubHijriMonthsDays(surveillanceReport.getStartDate(), surveillanceEmployeeData.getPeriodicReporting(), -1);
			}
			surveillanceReport.setEndDate(endDate);

			// generate details and add report with details in db
			addSurveillanceReport(loginUser, surveillanceReport, session);
		}
	}

	/**
	 * Register SurveillanceReport for employee and generate its details of evaluation points
	 * 
	 * @param loginUser
	 * @param surveillanceReport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void addSurveillanceReport(EmployeeData loginUser, SurveillanceReport surveillanceReport, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceReport.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.addEntity(surveillanceReport, session);

			// generate a detail for each evaluation point and add in db
			for (SetupDomain domain : getSurveillanceEvaluationPoints()) {
				SurveillanceReportDetail reportDetail = new SurveillanceReportDetail();
				reportDetail.setEvalPointDomainId(domain.getId());
				reportDetail.setGrade(1);
				reportDetail.setSurveillanceReportId(surveillanceReport.getId());

				reportDetail.setSystemUser(loginUser.getEmpId().toString());
				DataAccess.addEntity(reportDetail, session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Update SurveillanceReport with details on employee and initialize WF
	 * 
	 * @param loginUser
	 * @param surveillanceReport
	 * @param surveillanceReportDetailsList
	 * @param send
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSurveillanceReportWithDetails(EmployeeData loginUser, SurveillanceReport surveillanceReport, List<SurveillanceReportDetailData> surveillanceReportDetailsList, boolean send, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			updateSurveillanceReport(loginUser, surveillanceReport, session);

			for (SurveillanceReportDetailData reportDetail : surveillanceReportDetailsList) {
				reportDetail.getSurveillanceReportDetail().setSystemUser(loginUser.getEmpId().toString());
				DataAccess.updateEntity(reportDetail.getSurveillanceReportDetail(), session);
			}

			// initialize wf
			if (send) {
				SurveillanceReportWorkFlow.initSurveillanceReport(surveillanceReport, loginUser, null, session);
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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * After final approval start applying recommendation on surveillance employee record this is called from last approval in SurveillanceReportWorkFlow
	 * 
	 * @param loginUser
	 * @param surveillanceReport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void applySurveillanceReportRecommendation(EmployeeData loginUser, SurveillanceReport surveillanceReport, CustomSession... useSession) throws BusinessException {
		validateReportRecommendation(surveillanceReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceReport.setApproved(FlagsEnum.ON.getCode());
			surveillanceReport.setRecommendationDecisionDate(HijriDateService.getHijriSysDate());
			if (surveillanceReport.getRecommendationDecisionType() == SurveillanceReportRecommendationEnum.EXTEND.getCode()) {
				// update employee period (old period + new period) and end date
				// (add months to old end date)
				SurveillanceEmpNonEmpData employee = getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
				Date oldEndDate = employee.getEndDate();
				employee.setPeriodMonths(employee.getPeriodMonths() + surveillanceReport.getRecommendationPeriod());
				employee.setEndDate(HijriDateService.addSubHijriMonthsDays(employee.getEndDate(), surveillanceReport.getRecommendationPeriod(), 0));
				employee.setWorkflowFlag(true);
				updateSurveillanceEmployee(loginUser, employee, session);

				// create new reports for the extended period
				addSurveillanceReports(loginUser, surveillanceReport.getRecommendationPeriod(), employee, HijriDateService.addSubHijriMonthsDays(oldEndDate, 0, 1), session);
			} else if (surveillanceReport.getRecommendationDecisionType() == SurveillanceReportRecommendationEnum.END.getCode()) {
				// update employee period (number of reports till the end date
				// of this report * periodic reporting) and end date(end date of
				// this report)
				SurveillanceEmpNonEmpData employee = getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
				if (employee.getEndDate().after(surveillanceReport.getEndDate())) {
					employee.setPeriodMonths(1 + (getSurveillanceReports(employee.getId(), surveillanceReport.getStartDate()).size()) * employee.getPeriodicReporting());
					employee.setEndDate(HijriDateService.getHijriSysDate());
					employee.setWorkflowFlag(true);
					updateSurveillanceEmployee(loginUser, employee, session);

					// delete reports after this report
					deleteSurveillanceReports(employee.getId(), surveillanceReport.getEndDate(), session);
				}
			}
			updateSurveillanceReport(loginUser, surveillanceReport, session);

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Validate report recommendation - mandatory fields
	 * 
	 * @param surveillanceReport
	 * @throws BusinessException
	 */
	public static void validateReportRecommendation(SurveillanceReport surveillanceReport) throws BusinessException {
		if (surveillanceReport.getRecommendationDecisionType() == SurveillanceReportRecommendationEnum.EXTEND.getCode() && surveillanceReport.getRecommendationPeriod() == null) {
			throw new BusinessException("error_extendPeriodMandatory");
		}

		if (surveillanceReport.getRecommendationRemarks() == null || surveillanceReport.getRecommendationRemarks().trim().equals("")) {
			throw new BusinessException("error_reasonsRemarksMandatory");
		}
	}

	/**
	 * Update SurveillanceReport
	 * 
	 * @param loginUser
	 * @param surveillanceReport
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void updateSurveillanceReport(EmployeeData loginUser, SurveillanceReport surveillanceReport, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			surveillanceReport.setSystemUser(loginUser.getEmpId().toString());
			DataAccess.updateEntity(surveillanceReport, session);

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
				Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Get all surveillance reasons
	 * 
	 * @return list of surveillance reasons
	 * @throws BusinessException
	 */
	public static List<SetupDomain> getAllReasons() throws BusinessException {
		return SetupService.getDomains(ClassesEnum.SURVEILLANCE_REASONS.getCode());
	}

	/**
	 * Get all surveillance evaluation points
	 * 
	 * @return list of surveillance evaluation points
	 * @throws BusinessException
	 */
	private static List<SetupDomain> getSurveillanceEvaluationPoints() throws BusinessException {
		List<SetupDomain> setupDomainList = SetupService.getDomains(ClassesEnum.SURVEILLANCE_BEHAVIOURAL_EVALUATION_POINT.getCode());
		setupDomainList.addAll(SetupService.getDomains(ClassesEnum.SURVEILLANCE_FUNCTIONAL_EVALUATION_POINT.getCode()));
		return setupDomainList;
	}

	/**********************************************/
	/******************* Queries *****************/

	/**
	 * Get Surveillance Orders
	 * 
	 * @param orderId
	 * @param orderNumber
	 * @param instanceId
	 * @return list of SurveillanceOrderData
	 * @throws BusinessException
	 */
	public static List<SurveillanceOrderData> getSurveillanceOrderData(long orderId, String orderNumber, long instanceId) throws BusinessException {
		try {
			return searchSurveillanceOrderData(orderId, orderNumber, instanceId, null, FlagsEnum.ALL.getCode());
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceOrderData>();
		}
	}

	/**
	 * Search Surveillance Order
	 * 
	 * @param orderId
	 * @param orderNumber
	 * @param instanceId
	 * @param year
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceOrderData> searchSurveillanceOrderData(long orderId, String orderNumber, long instanceId, String year, long regionId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_ORDER_ID", orderId);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.isEmpty() ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_INSTANCE_ID", instanceId);
			qParams.put("P_YEAR", year == null || year.trim().isEmpty() ? FlagsEnum.ALL.getCode() : year);
			qParams.put("P_REGION_ID", regionId);
			return DataAccess.executeNamedQuery(SurveillanceOrderData.class, QueryNamesEnum.SURVEILLANCE_ORDER_DATA_SEARCH_SURVEILLANCE_ORDER.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Surveillance Actions
	 * 
	 * @param surEmployeeId
	 * @param startDate
	 * @param endDate
	 * @return list of SurveillanceActionData
	 * @throws BusinessException
	 */
	public static List<SurveillanceActionData> getEmployeeSurveillanceActionData(long surEmployeeId, Date startDate, Date endDate) throws BusinessException {
		try {
			return searchEmployeeSurveillanceActionData(surEmployeeId, startDate, endDate);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceActionData>();
		}
	}

	/**
	 * Search Surveillance Actions
	 * 
	 * @param surEmployeeId
	 * @param startDate
	 * @param endDate
	 * @return list of SurveillanceActionData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceActionData> searchEmployeeSurveillanceActionData(long surEmployeeId, Date startDate, Date endDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", surEmployeeId);
			qParams.put("P_START_DATE_NULL", startDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_END_DATE_NULL", endDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_START_DATE", startDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(startDate));
			qParams.put("P_END_DATE", endDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(endDate));

			return DataAccess.executeNamedQuery(SurveillanceActionData.class, QueryNamesEnum.SURVEILLANCE_ACTION_DATA_SEARCH_EMPLOYEE_SURVEILLANCE_ACTION.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Surveillance Reports for employee
	 * 
	 * @param surEmployeeId
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 */
	public static List<SurveillanceReport> getEmployeeSurveillanceReports(long surEmployeeId, Date activeDate) throws BusinessException {
		try {
			return searchSurveillanceReports(FlagsEnum.ALL.getCode(), surEmployeeId, activeDate, null, FlagsEnum.ALL.getCode(), false);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceReport>();
		}
	}

	/**
	 * Get Surveillance Report by Id or wfInstanceId
	 * 
	 * @param reportId
	 * @param wfInstanceId
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 */
	public static SurveillanceReport getSurveillanceReport(long reportId, long wfInstanceId) throws BusinessException {
		try {
			return searchSurveillanceReports(reportId, FlagsEnum.ALL.getCode(), null, null, wfInstanceId, false).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Surveillance Reports for employee that ends before or equal this date
	 * 
	 * @param surEmployeeId
	 * @param beforeStartDate
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 */
	public static List<SurveillanceReport> getSurveillanceReports(long surEmployeeId, Date beforeStartDate) throws BusinessException {
		try {
			return searchSurveillanceReports(FlagsEnum.ALL.getCode(), surEmployeeId, beforeStartDate, null, FlagsEnum.ALL.getCode(), false);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceReport>();
		}
	}

	/**
	 * Get Surveillance Reports that ends before or equal this date and has no workflow with approved order
	 * 
	 * @param beforeStartDate
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 */
	public static List<SurveillanceReport> getSurveillanceReports(Date beforeStartDate) throws BusinessException {
		try {
			return searchSurveillanceReports(FlagsEnum.OFF.getCode(), beforeStartDate, FlagsEnum.ON.getCode());
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceReport>();
		}
	}

	/**
	 * Get all Surveillance Reports for approved orders that has no workflow yet
	 * 
	 * @param approved
	 * @param beforeStartDate
	 *            : get all reports which ends before or equal this date
	 * @param noWorkFlow
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceReport> searchSurveillanceReports(long approved, Date beforeStartDate, int noWorkFlow) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_APPROVED", approved);
			qParams.put("P_START_WF", noWorkFlow);
			qParams.put("P_BEFORE_START_DATE_NULL", beforeStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_BEFORE_START_DATE", beforeStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(beforeStartDate));
			qParams.put("P_WF_INSTANCE_STATUS", WFInstanceStatusEnum.COMPLETED.getCode());
			return DataAccess.executeNamedQuery(SurveillanceReport.class, QueryNamesEnum.SURVEILLANCE_REPORT_SEARCH_EMPLOYEE_SURVEILLANCE_REPORT_FOR_APPROVED_ORDERS.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Search Surveillance Reports
	 * 
	 * @param reportId
	 * @param surEmployeeId
	 * @param approved
	 * @param beforeStartDate
	 *            : get all reports which ends before or equal this date
	 * @param includeDate
	 *            : includeDate is between report start date and report end date
	 * @param wfInstanceId
	 * @return list of SurveillanceReport
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceReport> searchSurveillanceReports(long reportId, long surEmployeeId, Date beforeStartDate, Date includeDate, long wfInstanceId, boolean wfInstanceNotNull) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REPORT_ID", reportId);
			qParams.put("P_SUR_EMPLOYEE_ID", surEmployeeId);
			qParams.put("P_WF_INSTANCE_ID", wfInstanceId);
			qParams.put("P_WF_INSTANCE_NOT_NULL", wfInstanceNotNull ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			qParams.put("P_BEFORE_START_DATE_NULL", beforeStartDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_BEFORE_START_DATE", beforeStartDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(beforeStartDate));
			qParams.put("P_INCLUDE_DATE_NULL", includeDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_INCLUDE_DATE", includeDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(includeDate));

			return DataAccess.executeNamedQuery(SurveillanceReport.class, QueryNamesEnum.SURVEILLANCE_REPORT_SEARCH_EMPLOYEE_SURVEILLANCE_REPORT.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Delete surveillance reports that ends after this date
	 * 
	 * @param surEmployeeId
	 * @param endDate
	 * @param session
	 * @throws BusinessException
	 */
	private static void deleteSurveillanceReports(long surEmployeeId, Date endDate, CustomSession session) throws BusinessException {
		try {
			if (endDate == null) {
				throw new BusinessException("error_mandatory");
			}
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SUR_EMPLOYEE_ID", surEmployeeId);
			qParams.put("P_END_DATE", HijriDateService.getHijriDateString(endDate));
			DataAccess.updateDeleteNamedQuery(SurveillanceReportDetailData.class, QueryNamesEnum.SURVEILLANCE_REPORT_DETAIL_DATA_DELETE_SURVEILLANCE_REPORT_DETAIL.getCode(), qParams, session);
			DataAccess.updateDeleteNamedQuery(SurveillanceReport.class, QueryNamesEnum.SURVEILLANCE_REPORT_DELETE_SURVEILLANCE_REPORT.getCode(), qParams, session);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		} catch (NoDataException e) {

		}
	}

	/**
	 * Get Surveillance Report details for report
	 * 
	 * @param surveillanceReportId
	 * @return list of SurveillanceReportDetails
	 * @throws BusinessException
	 */
	public static List<SurveillanceReportDetailData> getSurveillanceReportDetailData(long surveillanceReportId) throws BusinessException {
		try {
			return searchSurveillanceReportDetailData(surveillanceReportId);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceReportDetailData>();
		}
	}

	/**
	 * Search Surveillance Report Details for report
	 * 
	 * @param surveillanceReportId
	 * @return list of SurveillanceReportDetailData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceReportDetailData> searchSurveillanceReportDetailData(long surveillanceReportId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_REPORT_ID", surveillanceReportId);
			return DataAccess.executeNamedQuery(SurveillanceReportDetailData.class, QueryNamesEnum.SURVEILLANCE_REPORT_DETAIL_DATA_SEARCH_SURVEILLANCE_REPORT_DETAIL.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get surveillance employee that has a running surveillance at the date
	 * 
	 * @param socialId
	 * @param currentDate
	 * @return SurveillanceEmployeeData
	 * @throws BusinessException
	 */
	public static SurveillanceEmpNonEmpData getSurveillanceEmployeeData(String socialId, Date currentDate) throws BusinessException {
		try {
			return SurveillanceOrdersService.searchSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ON.getCode(), false, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), WFInstanceStatusEnum.COMPLETED.getCode(), FlagsEnum.ALL.getCode(), socialId, currentDate).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get surveillance employee by surEmployeeId for approved orders
	 * 
	 * @param surEmployeeId
	 * @return SurveillanceEmployeeData
	 * @throws BusinessException
	 */
	public static SurveillanceEmpNonEmpData getSurveillanceEmployeeData(long surEmployeeId) throws BusinessException {
		try {
			return searchSurveillanceEmpNonEmpData(surEmployeeId, FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), false, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), WFInstanceStatusEnum.COMPLETED.getCode(), FlagsEnum.ALL.getCode(), null, null).get(0);
		} catch (NoDataException e) {
			return null;
		}
	}

	/**
	 * Get Active surveillance employee
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<SurveillanceEmpNonEmpData> getSurveillanceEmployeeData() throws BusinessException {
		try {
			return searchSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ON.getCode(), false, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceEmpNonEmpData>();
		}
	}

	/**
	 * Get surveillance employee by orderId
	 * 
	 * @param orderId
	 * @return list of SurveillanceEmployeeData
	 * @throws BusinessException
	 */
	public static List<SurveillanceEmpNonEmpData> getSurveillanceEmployeeDataByOrderId(long orderId) throws BusinessException {
		try {
			return searchSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), orderId, null, null, null, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), false, FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), null, null);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceEmpNonEmpData>();
		}
	}

	/**
	 * Get surveillance employees
	 * 
	 * @param orderNumber
	 * @param orderDate
	 * @param finalApprovalEntity
	 * @param employeeId
	 * @param surveillanceStatus
	 * @param isLateReports
	 * @param empDepartmentId
	 * @param wfInstanceStatus
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static List<SurveillanceEmpNonEmpData> getSurveillanceEmployeeData(String orderNumber, Date orderDate, String finalApprovalEntity, long employeeId, int surveillanceStatus, boolean isLateReports, long empDepartmentId, int wfInstanceStatus, String socialId) throws BusinessException {
		try {
			// validate order date
			if (orderDate != null && orderDate.after(HijriDateService.getHijriSysDate())) {
				throw new BusinessException("error_comingDate");
			}
			long empRegionId = DepartmentService.getDepartment(empDepartmentId).getRegionId();
			long orderRegionId = empRegionId == WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId() ? FlagsEnum.ALL.getCode() : empRegionId;

			return searchSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), orderNumber, orderDate, finalApprovalEntity, employeeId, surveillanceStatus, isLateReports, orderRegionId, FlagsEnum.ALL.getCode(), wfInstanceStatus, FlagsEnum.ALL.getCode(), socialId, null);

		} catch (NoDataException e) {
			return new ArrayList<SurveillanceEmpNonEmpData>();
		}
	}

	/**
	 * Get surveillance employee
	 * 
	 * @param orderNumber
	 * @param orderDate
	 * @param finalApprovalEntity
	 * @param employeeId
	 * @param surveillanceStatus
	 * @param isLateReports
	 * @param regionId
	 * @param empDepartmentId
	 * @param wfInstanceStatus
	 * @param socialId
	 * @return
	 * @throws BusinessException
	 */
	public static List<SurveillanceEmpNonEmpData> getSurveillanceEmployeeDataByRegionId(String orderNumber, Date orderDate, String finalApprovalEntity, long employeeId, int surveillanceStatus, boolean isLateReports, Long regionId, int wfInstanceStatus, String socialId) throws BusinessException {
		try {
			// validate order date
			if (orderDate != null && orderDate.after(HijriDateService.getHijriSysDate())) {
				throw new BusinessException("error_comingDate");
			}

			return searchSurveillanceEmpNonEmpData(FlagsEnum.ALL.getCode(), FlagsEnum.ALL.getCode(), orderNumber, orderDate, finalApprovalEntity, employeeId, surveillanceStatus, isLateReports, FlagsEnum.ALL.getCode(), regionId == null || regionId.equals(InfoSysConfigurationService.getHeadQuarter()) ? FlagsEnum.ALL.getCode() : regionId, wfInstanceStatus, FlagsEnum.ALL.getCode(), socialId, null);
		} catch (NoDataException e) {
			return new ArrayList<SurveillanceEmpNonEmpData>();
		}
	}

	/**
	 * Search surveillance employees
	 * 
	 * @param surEmployeeId
	 * @param orderId
	 * @param orderNumber
	 * @param orderDate
	 * @param orderIssueSiteId
	 * @param employeeId
	 * @param surveillanceStatus
	 *            : 1 -> running; 0 -> finished; -1 -> All
	 * @param isLateReports
	 *            : true->search for late reports only
	 * @param regionId
	 * @param wfInstanceStatus
	 * @return list of SurveillanceEmployeeData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceEmpNonEmpData> searchSurveillanceEmpNonEmpData(long surEmployeeId, long orderId, String orderNumber, Date orderDate, String finalApprovalEntity, long employeeId, int surveillanceStatus, boolean isLateReports, long regionId, long orderRegionId, int wfInstanceStatus, long nonEmpId, String socialId, Date currentDate) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_SUR_EMPLOYEE_ID", surEmployeeId);
			qParams.put("P_ORDER_ID", orderId);
			qParams.put("P_ORDER_NUMBER", orderNumber == null || orderNumber.equals("") ? FlagsEnum.ALL.getCode() + "" : orderNumber);
			qParams.put("P_ORDER_DATE_NULL", orderDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			qParams.put("P_ORDER_DATE", orderDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(orderDate));
			qParams.put("P_CURRENT_DATE", currentDate == null ? HijriDateService.getHijriSysDateString() : currentDate);
			qParams.put("P_FINAL_APPROVAL_ENTITY", finalApprovalEntity == null || finalApprovalEntity.equals("") ? FlagsEnum.ALL.getCode() + "" : finalApprovalEntity);
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_LATE_FLAG", isLateReports ? FlagsEnum.ON.getCode() : FlagsEnum.ALL.getCode());
			qParams.put("P_REGION_ID", regionId);
			qParams.put("P_ORDER_REGION_ID", orderRegionId);
			qParams.put("P_SURVEILLANCE_STATUS", surveillanceStatus);
			qParams.put("P_WF_INSTANCE_STATUS", wfInstanceStatus);
			qParams.put("P_NON_EMP_ID", nonEmpId);
			qParams.put("P_SOCIAL_ID", socialId == null || socialId.equals("") ? FlagsEnum.ALL.getCode() + "" : socialId);
			return DataAccess.executeNamedQuery(SurveillanceEmpNonEmpData.class, QueryNamesEnum.SURVEILLANCE_EMP_NON_EMP_DATA_SEARCH_SURVEILLANCE_EMP_NON_EMP_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get surveillance employees
	 * 
	 * @param employeeId
	 * @return list of SurveillanceEmployeeData
	 * @throws BusinessException
	 */
	public static Long getCountSurveillanceEmployeeData(long employeeId) throws BusinessException {
		try {
			return countSurveillanceEmployee(employeeId);
		} catch (NoDataException e) {
			return 0L;
		}
	}

	/**
	 * Count surveillance employees
	 * 
	 * @param employeeId
	 * @param wfInstanceStatus
	 * @return number of SurveillanceEmployeeData
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static Long countSurveillanceEmployee(long employeeId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_WF_INSTANCE_STATUS", WFInstanceStatusEnum.COMPLETED.getCode());
			return DataAccess.executeNamedQuery(Long.class, QueryNamesEnum.SURVEILLANCE_EMP_NON_EMP_DATA_COUNT_SURVEILLANCE_EMP_NON_EMP_DATA.getCode(), qParams).get(0);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}
	
	/**
	 * Get Active SurveillanceEmpNonEmpData
	 * 
	 * @param employeeId
	 * @param nonEmpId
	 * @return
	 * @throws BusinessException
	 * @throws NoDataException
	 */
	private static List<SurveillanceEmpNonEmpData> getActiveSurveillanceEmpNonEmpData(long employeeId, long nonEmpId) throws BusinessException, NoDataException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_EMPLOYEE_ID", employeeId);
			qParams.put("P_NON_EMP_ID", nonEmpId);
			qParams.put("P_WF_INSTANCE_STATUS", WFInstanceStatusEnum.REJECTED.getCode());
			qParams.put("P_RECOMMEND_DECISION_TYPE", SurveillanceReportRecommendationEnum.END.getCode());
			return DataAccess.executeNamedQuery(SurveillanceEmpNonEmpData.class, QueryNamesEnum.SURVEILLANCE_EMP_NON_EMP_DATA_GET_ACTIVE_SURVEILLANCE_EMP_NON_EMP_DATA.getCode(), qParams);
		} catch (DatabaseException e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_DBError");
		}
	}

	/**
	 * Get Surveillance Order Report Bytes
	 * 
	 * @param surveillanceOrderId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSurveillanceOrderReportBytes(long surveillanceOrderId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SURVEILLANCE_ORDER_ID", surveillanceOrderId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.SURVEILLANCE_ORDER.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Surveillance Report Bytes
	 * 
	 * @param surveillanceReportId
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSurveillanceReportBytes(long surveillanceReportId, String loginEmployeeName) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_SURVEILLANCE_REPORT_ID", surveillanceReportId);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			parameters.put("P_REPORTS_ROOT", InfoSysConfigurationService.getReportsRoot());
			reportName = ReportNamesEnum.SURVEILLANCE_REPORT.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Surveillance Employees Bytes
	 * 
	 * @param regionId
	 * @param regionName
	 * @param surveillanceStatus
	 * @param fromDate
	 * @param toDate
	 * @param loginEmployeeName
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSurveillanceEmployeesBytes(long regionId, String regionName, int surveillanceStatus, Date fromDate, Date toDate, String loginEmployeeName, int reportPercentageFrom, int reportPercentageTo) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_TOTAL_FROM", reportPercentageFrom);
			parameters.put("P_TOTAL_TO", reportPercentageTo);
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_REGION_NAME", regionName == null || regionName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			parameters.put("P_SURVEILLANCE_STATUS", surveillanceStatus);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.SURVEILLANCE_EMPLOYEES.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Get Surveillance Employees Bytes
	 * 
	 * @param regionId
	 * @param surveillanceStatus
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws BusinessException
	 */
	public static byte[] getSurveillanceEmployeesStatisticalBytes(long regionId, String regionName, int surveillanceStatus, Date fromDate, Date toDate, String loginEmployeeName, int reportPercentageFrom, int reportPercentageTo) throws BusinessException {
		String reportName = "";
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_REGION_ID", regionId);
			parameters.put("P_TOTAL_FROM", reportPercentageFrom);
			parameters.put("P_TOTAL_TO", reportPercentageTo);
			parameters.put("P_REGION_NAME", regionName == null || regionName.trim().isEmpty() ? FlagsEnum.ALL.getCode() + "" : regionName);
			parameters.put("P_HIJRI_SYS_DATE", HijriDateService.getHijriSysDateString());
			parameters.put("P_FROM_DATE_NULL", fromDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_FROM_DATE", fromDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(fromDate));
			parameters.put("P_TO_DATE_NULL", toDate == null ? FlagsEnum.ON.getCode() : FlagsEnum.OFF.getCode());
			parameters.put("P_TO_DATE", toDate == null ? HijriDateService.getHijriSysDateString() : HijriDateService.getHijriDateString(toDate));
			parameters.put("P_SURVEILLANCE_STATUS", surveillanceStatus);
			parameters.put("P_LOGIN_EMPLOYEE_NAME", loginEmployeeName);
			reportName = ReportNamesEnum.SURVEILLANCE_EMPLOYEES_STATISTICAL.getCode();
			return ReportService.getReportData(reportName, parameters);
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceOrdersService.class, e, "SurveillanceOrdersService");
			throw new BusinessException("error_general");
		}
	}
}