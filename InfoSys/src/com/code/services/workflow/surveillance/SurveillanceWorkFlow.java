package com.code.services.workflow.surveillance;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.setup.NonEmployeeData;
import com.code.dal.orm.surveillance.SurveillanceEmpNonEmpData;
import com.code.dal.orm.surveillance.SurveillanceOrder;
import com.code.dal.orm.surveillance.SurveillanceOrderData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFPosition;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.EmployeeSocialIdTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.IntegrationFishingSailorType;
import com.code.enums.SecurityStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityaction.SecurityStatusService;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.CountryService;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.EmployeeService;
import com.code.services.setup.NonEmployeeService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

import yaqeen.ejada.com.IdType;
import yaqeen.ejada.com.PersonInfoResultWithDetailedSecurityStatus;

public class SurveillanceWorkFlow extends BaseWorkFlow {
	private SurveillanceWorkFlow() {
	}

	/**
	 * Save wf instance and task at direct manager : If task in HQ then "Moder edaret el este5barat" in HQ If task in Region then "Moder edaret el este5barat" in region to approve only
	 * 
	 * @param surveillanceOrder
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initSurveillanceOrder(SurveillanceOrder surveillanceOrder, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is Enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();
			String arabicDetailsSummary;
			String englishDetailsSummary;
			String arInstanceMsg = "";
			String enInstanceMsg = "";

			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				arInstanceMsg = getParameterizedMessage("wfMsg_surveillanceInstance", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getFinalApprovalEntity(), surveillanceOrder.getStartDateString() });
				enInstanceMsg = getParameterizedMessage("wfMsg_surveillanceInstance", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getFinalApprovalEntity(), surveillanceOrder.getStartDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "en", new Object[] { surveillanceOrder.getOrderDateString() });
				arInstanceMsg = getParameterizedMessage("wfMsg_surveillanceInstanceVerbal", "ar", new Object[] { surveillanceOrder.getFinalApprovalEntity(), surveillanceOrder.getStartDateString() });
				enInstanceMsg = getParameterizedMessage("wfMsg_surveillanceInstanceVerbal", "en", new Object[] { surveillanceOrder.getFinalApprovalEntity(), surveillanceOrder.getStartDateString() });
			}
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.SURVEILLANCE_ORDER.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInstanceMsg, enInstanceMsg, session);
			surveillanceOrder.setwFInstanceId(instance.getInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Resend SurveillanceOrder and send task: If task in HQ then "Moder edaret el este5barat" in HQ If task in Region then "Moder edaret el este5barat" in region to approve only
	 * 
	 * @param currentTask
	 * @param surveillanceOrder
	 * @param surveillanceEmployeeDataList
	 * @param surveillanceEmployeeDataDeletedList
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void resendSurveillanceOrder(WFTask currentTask, SurveillanceOrderData surveillanceOrder, List<SurveillanceEmpNonEmpData> surveillanceEmployeeDataList, List<SurveillanceEmpNonEmpData> surveillanceEmployeeDataDeletedList, EmployeeData loginUser, String attachments, boolean verbalOrderFlag, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SurveillanceOrdersService.addSurveillanceOrder(loginUser, surveillanceOrder, surveillanceEmployeeDataList, surveillanceEmployeeDataDeletedList, true, verbalOrderFlag);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "en", new Object[] { surveillanceOrder.getOrderDateString() });
			}
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceOrder.getwFInstanceId());

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Only one action is taked : Approve then open task at If task in HQ then "Moder eledara el3ama lel2mn welste5barat" in HQ If task in Region then "Qa2ed el mante2a" in region
	 * 
	 * @param currentTask
	 * @param surveillanceOrder
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderDM(WFTask currentTask, SurveillanceOrder surveillanceOrder, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "en", new Object[] { surveillanceOrder.getOrderDateString() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceOrder.getwFInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
				completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * doSurveillanceOrderRegionSecurityManager
	 * 
	 * @param currentTask
	 * @param surveillanceOrder
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderRegionSecurityManager(WFTask currentTask, SurveillanceOrder surveillanceOrder, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceApproval", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbarOrderApproval", "en", new Object[] { surveillanceOrder.getOrderDateString() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceOrder.getwFInstanceId());
			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			Long originalId = DepartmentService.getDepartmentManager(regionId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.REGION_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Only one action is taked : Approve then send notification to task initiator
	 * 
	 * @param currentTask
	 * @param surveillanceOrder
	 * @param surveillanceEmployeeData
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doSurveillanceOrderSM(WFTask currentTask, SurveillanceOrder surveillanceOrder, List<SurveillanceEmpNonEmpData> surveillanceEmployeeData, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			SurveillanceOrdersService.addSurveillanceReports(loginUser, surveillanceOrder, surveillanceEmployeeData, session);

			String arabicDetailsSummary;
			String englishDetailsSummary;
			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceNotify", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceNotify", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbalOrderNotify", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbalOrderNotify", "en", new Object[] { surveillanceOrder.getOrderDateString() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceOrder.getwFInstanceId());
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			changeWFInstanceStatus(instance, WFInstanceStatusEnum.COMPLETED.getCode(), session);

			// Send Notifications if employee or non-employee is issued
			for (SurveillanceEmpNonEmpData surveillanceEmpNonEmpData : surveillanceEmployeeData) {
				PersonInfoResultWithDetailedSecurityStatus personInfoResult = null;

				String socialId = surveillanceEmpNonEmpData.getSocialId();
				Date birthDate = surveillanceEmpNonEmpData.getBirthDate();
				Date birthDateGreg = surveillanceEmpNonEmpData.getBirthDateGreg();

				String arabicDetailsSummaryForSurveillance;
				String englishDetailsSummaryForSurveillance;
				if (surveillanceOrder.getOrderNumber() != null) {
					arabicDetailsSummaryForSurveillance = getParameterizedMessage("wfMsg_surveillanceApprovalNotify", "ar", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
					englishDetailsSummaryForSurveillance = getParameterizedMessage("wfMsg_surveillanceApprovalNotify", "en", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				} else {
					arabicDetailsSummaryForSurveillance = getParameterizedMessage("wfMsg_surveillanceApprovalOnVerbalOrderNotify", "ar", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), surveillanceOrder.getOrderDateString() });
					englishDetailsSummaryForSurveillance = getParameterizedMessage("wfMsg_surveillanceApprovalOnVerbalOrderNotify", "en", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), surveillanceEmpNonEmpData.getBirthDateString(), surveillanceOrder.getOrderDateString() });
				}
				// TODO To be replaced by a thread
				try {
					if (birthDate != null && socialId.startsWith(EmployeeSocialIdTypeEnum.NATIONAL_IDENTITY.getCode())) {
						personInfoResult = SecurityStatusService.searchPersonInfoNationalIdentity(birthDate, IdType.C, socialId, loginUser);
					} else if (birthDateGreg != null && socialId.startsWith(EmployeeSocialIdTypeEnum.ACCOMIDATION.getCode())) {
						personInfoResult = SecurityStatusService.searchPersonInfo(birthDateGreg, IdType.R, socialId, loginUser);
					}

				} catch (BusinessException e) {
					Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				}

				// TODO check status later On with new Yaqeen Service
				if (personInfoResult != null) {
					surveillanceEmpNonEmpData.setYaqeenStatus((int) (personInfoResult.getSecurityStatus().getValue().equals(FlagsEnum.ON.getCode() + "") ? SecurityStatusEnum.WANTED.getCode() : (short) FlagsEnum.OFF.getCode()));
					SurveillanceOrdersService.updateSurveillanceEmployee(loginUser, surveillanceEmpNonEmpData, session);
					if (personInfoResult.getSecurityStatus().getValue().equals(FlagsEnum.ON.getCode() + "")) {
						List<Long> notifiersIds = getSurveillanceNotifers(surveillanceEmpNonEmpData.getOrderRegionId());
						for (Long originalIdForSurveillance : notifiersIds) {
							Long assigneeIdForSurveillance = getDelegate(originalIdForSurveillance, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
							addWFTask(instance.getInstanceId(), assigneeIdForSurveillance, originalIdForSurveillance, new Date(), currHijriDate, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummaryForSurveillance, englishDetailsSummaryForSurveillance, session);
						}
					}
				}
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
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Reject surveillance order and send it to requester for modifications
	 * 
	 * @param currentTask
	 * @param surveillanceOrder
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void rejectSurveillanceOrder(WFTask currentTask, SurveillanceOrder surveillanceOrder, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		if (currentTask.getNotes() == null || currentTask.getNotes().isEmpty()) {
			throw new BusinessException("error_notesMandatoryAtRejection");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();
			//
			String arabicDetailsSummary;
			String englishDetailsSummary;
			
			if (surveillanceOrder.getOrderNumber() != null) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceRejection", "ar", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceRejection", "en", new Object[] { surveillanceOrder.getOrderNumber(), surveillanceOrder.getOrderDateString() });
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbalOrderRejection", "ar", new Object[] { surveillanceOrder.getOrderDateString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceOnVerbalOrderRejection", "en", new Object[] { surveillanceOrder.getOrderDateString() });
			}
			Date currHijriDate = HijriDateService.getHijriSysDate();

			WFInstance instance = BaseWorkFlow.getWFInstanceById(surveillanceOrder.getwFInstanceId());
			BaseWorkFlow.changeWFInstanceStatus(instance, WFInstanceStatusEnum.REJECTED.getCode(), session);
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.SURVEILLANCE_ORDER.getCode());
			completeWFTask(currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.SURVEILlANCE_ORDER.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Send Notification to Movement Region
	 * 
	 * @param wfTask
	 * @param navigationRegionId
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void sendMovementNotifications(WFTask wfTask, Long navigationRegionId, EmployeeData loginEmp, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			List<WFTask> wfTasks = getWFInstanceTasksByRole(wfTask.getInstanceId(), WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode());
			if (!wfTasks.isEmpty() || wfTasks.size() != 0) {
				throw new BusinessException("error_resendMovementNotification");
			}
			if (navigationRegionId != null) {
				Date currHijriDate = HijriDateService.getHijriSysDate();
				List<Long> notifiersIds = getFishingNotifers(navigationRegionId);
				for (Long originalId : notifiersIds) {
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.FIHSING_AND_EXCURSIONS.getCode());
					wfTask.setSystemUser(loginEmp.getEmpId().toString());
					addWFTask(wfTask.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.FIHSING_AND_EXCURSION_NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION_WITHOUT_RESEND.getCode(), "1", wfTask.getNotes(), wfTask.getArabicDetailsSummary(), wfTask.getEnglishDetailsSummary(), session);
				}
			} else {
				throw new BusinessException("error_navigationWithoutMovementRegion");
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
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Send notifications
	 * 
	 * @param msg
	 * @param useSession
	 */
	public static void sendNotifications(String msg, CustomSession... useSession) {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}
			Log4j.traceInfo(SurveillanceWorkFlow.class, "OPR FSH MESSAGE : " + msg);
			String[] msgTokens = msg.split(";");
			if (msgTokens.length != 10) {
				Log4j.traceError(SurveillanceWorkFlow.class, "The parameter count is not correct.");
				throw new Exception();
			}

			EmployeeData employeeData = null;
			NonEmployeeData nonEmployeeData = null;
			List<NonEmployeeData> nonEmployeeList = null;
			Long socialId = null;
			String socialIdStr = msgTokens[0];
			String sailorType = msgTokens[1];
			String anchorageName = msgTokens[2];
			String boatName = msgTokens[3];
			String boatNumber = msgTokens[4];
			String movementType = msgTokens[5];
			String purpose = msgTokens[6];
			String sailDate = msgTokens[7];
			String sailTime = msgTokens[8];
			String navigationNumber = msgTokens[9];
			Date hijriSailData;
			try {
				socialId = Long.parseLong(msgTokens[0]);
				employeeData = EmployeeService.getEmployee(socialIdStr);
				nonEmployeeList = NonEmployeeService.getNonEmployee(socialId, null);
				if (!nonEmployeeList.isEmpty()) {
					nonEmployeeData = nonEmployeeList.get(0);
				}
				if (employeeData == null && nonEmployeeData == null) {
					throw new Exception("Social Id is not correct.");
				} else if (employeeData != null && nonEmployeeData != null) {
					throw new Exception("Social Id is not correct.");
				}
			} catch (Exception e) {
				Log4j.traceError(SurveillanceWorkFlow.class, e.getMessage());
				return;
			}

			if (sailorType == null || sailorType.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Sailor Type is Null or Empty.");
				throw new Exception();
			}

			if (anchorageName == null || anchorageName.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Anchorage Name is Null or Empty.");
				throw new Exception();
			}

			if (boatName == null || boatName.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Boat Name is Null or Empty.");
				throw new Exception();
			}

			if (boatNumber == null || boatNumber.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Boat Number is Null or Empty.");
				throw new Exception();
			}

			if (movementType == null || movementType.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Movement Type is Null or Empty.");
				throw new Exception();
			}

			if (purpose == null || purpose.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Purpose is Null or Empty.");
				throw new Exception();
			}

			if (navigationNumber == null || navigationNumber.trim().isEmpty()) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Navigation Number is Null or Empty.");
				throw new Exception();
			}

			if (!sailDate.matches("(0[1-9]|[12][0-9]|30)/(0[1-9]|1[012])/1\\d{3}")) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Date format is not correct.");
				throw new Exception();
			} else {
				hijriSailData = HijriDateService.getHijriDate(sailDate);
				if (hijriSailData == null || !HijriDateService.isValidHijriDate(hijriSailData)) {
					Log4j.traceError(SurveillanceWorkFlow.class, "Date format is not correct.");
					throw new Exception();
				}
			}

			if (!sailTime.matches("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]")) {
				Log4j.traceError(SurveillanceWorkFlow.class, "Time format is not correct.");
				throw new Exception();
			}

			SurveillanceEmpNonEmpData surveillanceEmpNonEmpData = SurveillanceOrdersService.getSurveillanceEmployeeData(socialIdStr, hijriSailData);

			if (surveillanceEmpNonEmpData != null) {
				Date currHijriDate = HijriDateService.getHijriSysDate();
				String nationality = surveillanceEmpNonEmpData.getEmployeeId() == null ? nonEmployeeData.getCountryArabicName() : employeeData.getNationalityId() == null ? null : CountryService.getCountryById(employeeData.getNationalityId()).getArabicName();
				String notes = navigationNumber + ";" + movementType + ";" + sailDate, arabicDetailsSummary, englishDetailsSummary;
				if (sailorType == IntegrationFishingSailorType.OWNER.getCode()) {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceFishingOwnerNotification", "ar", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), nationality == null ? "" : nationality, surveillanceEmpNonEmpData.getRank() == null ? "" : surveillanceEmpNonEmpData.getRank(), boatName, movementType, purpose, anchorageName, sailDate, sailTime, navigationNumber });
					englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceFishingOwnerNotification", "en", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), nationality == null ? "" : nationality, surveillanceEmpNonEmpData.getRank() == null ? "" : surveillanceEmpNonEmpData.getRank(), boatName, movementType, purpose, anchorageName, sailDate, sailTime, navigationNumber });
				} else {
					arabicDetailsSummary = getParameterizedMessage("wfMsg_surveillanceFishingOtherNotification", "ar", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), nationality == null ? "" : nationality, surveillanceEmpNonEmpData.getRank() == null ? "" : surveillanceEmpNonEmpData.getRank(), movementType, sailorType, purpose, anchorageName, boatName, boatNumber, sailDate, sailTime, navigationNumber });
					englishDetailsSummary = getParameterizedMessage("wfMsg_surveillanceFishingOtherNotification", "en", new Object[] { surveillanceEmpNonEmpData.getEmployeeFullName(), surveillanceEmpNonEmpData.getSocialId(), nationality == null ? "" : nationality, surveillanceEmpNonEmpData.getRank() == null ? "" : surveillanceEmpNonEmpData.getRank(), movementType, sailorType, purpose, anchorageName, boatName, boatNumber, sailDate, sailTime, navigationNumber });
				}
				WFInstance instance = addWFInstance(WFProcessesEnum.FIHSING_AND_EXCURSIONS.getCode(), Long.parseLong(InfoSysConfigurationService.getSystemAdmin()), new Date(), HijriDateService.getHijriSysDate(), WFInstanceStatusEnum.COMPLETED.getCode(), null, null, null, session);
				List<Long> notifiersIds = getFishingNotifers(surveillanceEmpNonEmpData.getOrderRegionId());
				for (Long originalId : notifiersIds) {
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.FIHSING_AND_EXCURSIONS.getCode());
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.FIHSING_AND_EXCURSION_NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", notes, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Get Notifiers id
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<Long> getSurveillanceNotifers(Long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else {
				return WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_SURVEILLANCE_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

	/**
	 * Get Surveillance Notifiers id
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	private static List<Long> getFishingNotifers(Long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			if (pos.getPositionId().equals((long) WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				return WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			} else {
				return WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_FISHING_NOTIFICATION_RECEIVER.getCode()).getEmpsGroupList();
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
				throw new BusinessException("error_general");
			}
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
			Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * get directorate or region id depend on employee actual department
	 * 
	 * @param emp
	 * @return
	 * @throws BusinessException
	 */
	public static Long getDirectorateOrRegionId(EmployeeData emp) throws BusinessException {
		Long regionId = DepartmentService.isRegionDepartment(emp.getActualDepartmentId());
		if (regionId != null)
			return regionId;
		else
			return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}
	
	/**
	 * Closed surveillance Order
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void closeSurveillanceOrder(WFInstance instance, WFTask currentTask) throws BusinessException {
		try {
			closeWFInstance(instance, currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(SurveillanceWorkFlow.class, e, "SurveillanceWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}