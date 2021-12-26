package com.code.services.workflow.surveillance;

import java.util.Date;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceReport;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class SurveillanceReportWorkFlow extends BaseWorkFlow {

	private SurveillanceReportWorkFlow() {
	}

	/**
	 * Save wf instacne and task at direct manager : If task in HQ then "Moder edaret el este5barat" in HQ If task in Region then "Moder edaret el este5barat" in region to approve only
	 * 
	 * @param surveillanceReport
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void initSurveillanceReport(SurveillanceReport surveillanceReport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			SurveillanceEmpNonEmpData employeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			String arInstanceMsg = getParameterizedMessage("wfMsg_surveillanceReportInstance", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getFinalApprovalEntity(), surveillanceReport.getStartDateString(), surveillanceReport.getEndDateString() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_surveillanceReportInstance", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getFinalApprovalEntity(), surveillanceReport.getStartDateString(), surveillanceReport.getEndDateString() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInstanceMsg, enInstanceMsg, session);
			surveillanceReport.setwFInstanceId(instance.getInstanceId());

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });

			if (regionId != null) {
				// Get manager given login user actual department
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Only one action is taken : Approve then open task at If task in HQ then "Moder eledara el3ama lel2mn welste5barat" in HQ If task in Region then "Qa2ed el mante2a" in region
	 * 
	 * @param currentTask
	 * @param surveillanceReport
	 * @param attachments
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderReportDM(WFTask currentTask, SurveillanceReport surveillanceReport, String attachments, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		SurveillanceOrdersService.validateReportRecommendation(surveillanceReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SurveillanceOrdersService.updateSurveillanceReport(loginUser, surveillanceReport, session);

			SurveillanceEmpNonEmpData employeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceReport.getwFInstanceId());
			if (!employeeData.getOrderRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(employeeData.getOrderRegionId()));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * doSurveillanceOrderReportSecurityManager
	 * 
	 * @param currentTask
	 * @param surveillanceReport
	 * @param attachments
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderReportSecurityManager(WFTask currentTask, SurveillanceReport surveillanceReport, String attachments, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		SurveillanceOrdersService.validateReportRecommendation(surveillanceReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SurveillanceOrdersService.updateSurveillanceReport(loginUser, surveillanceReport, session);

			SurveillanceEmpNonEmpData employeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceReport.getwFInstanceId());

			Long originalId = DepartmentService.getDepartmentManager(employeeData.getOrderRegionId());
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.REGION_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * doSurveillanceOrderReportRegionDM
	 * 
	 * @param currentTask
	 * @param surveillanceReport
	 * @param attachments
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderReportRegionDM(WFTask currentTask, SurveillanceReport surveillanceReport, String attachments, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		SurveillanceOrdersService.validateReportRecommendation(surveillanceReport);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SurveillanceOrdersService.updateSurveillanceReport(loginUser, surveillanceReport, session);

			SurveillanceEmpNonEmpData employeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportApproval", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceReport.getwFInstanceId());

			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Only one action is taken : Approve then send notification to task initiator and call {@link SurveillanceOrdersService.applySurveillanceReportRecommendation( SurveillanceReport))}
	 * 
	 * @param currentTask
	 * @param surveillanceReport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderReportSM(WFTask currentTask, SurveillanceReport surveillanceReport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			SurveillanceOrdersService.applySurveillanceReportRecommendation(loginUser, surveillanceReport, session);

			SurveillanceEmpNonEmpData employeeData = SurveillanceOrdersService.getSurveillanceEmployeeData(surveillanceReport.getSurveillanceEmpId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportNotify", "ar", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceReportNotify", "en", new Object[] { employeeData.getOrderNumber(), employeeData.getOrderDateString() });
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceReport.getwFInstanceId());
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			changeWFInstanceStatus(instance, WFInstanceStatusEnum.COMPLETED.getCode(), session);

			if (!employeeData.getOrderRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
				Long notifyOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(employeeData.getOrderRegionId()));
				Long notifyAssigneeId = getDelegate(notifyOriginalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				addWFTask(instance.getInstanceId(), notifyAssigneeId, notifyOriginalId, new Date(), currHijriDate, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long notifyOriginalId = DepartmentService.getDepartmentManager(unitId);
				Long notifyAssigneeId = getDelegate(notifyOriginalId, WFProcessesEnum.SURVEILLANCE_ORDER_REPORT.getCode());
				addWFTask(instance.getInstanceId(), notifyAssigneeId, notifyOriginalId, new Date(), currHijriDate, WFTaskUrlEnum.SURVEILlANCE_ORDER_REPORT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Closed notification
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void doNotify(WFTask currentTask) throws BusinessException {
		try {
			setWFTaskAction(currentTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceReportWorkFlow.class, e, "SurveillanceReportWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}