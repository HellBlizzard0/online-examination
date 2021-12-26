package com.code.services.workflow.finance;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.finance.FinDepSupportData;
import com.code.dal.orm.finance.FinDepSupportDetailData;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class RegionFinancialSupportWorkFlow extends BaseWorkFlow {
	private RegionFinancialSupportWorkFlow() {
	}

	/**
	 * Initialize Region Financial Support Workflow
	 * 
	 * @param finDepSupport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initRegionFinancialSupport(FinDepSupportData finDepSupport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			FinYearApproval finYear = FinanceAccountsService.getFinYearApprovalById(finDepSupport.getFinYearApprovalId());
			String arInstanceMsg = getParameterizedMessage("wfMsg_regionSupportInstance", "ar", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getFinYear().toString(), finDepSupport.getBalance().toString() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_regionSupportInstance", "en", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getFinYear().toString(), finDepSupport.getBalance().toString() });

			// add new wf instance and set the id in the finDepSupport object in db
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInstanceMsg, enInstanceMsg, session);
			finDepSupport.setWfInstanceId(instance.getInstanceId());
			DataAccess.updateEntity(finDepSupport.getFinDepSupport(), session);

			// add new dm task
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_regionSupportSavingTask", "ar", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_regionSupportSavingTask", "en", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });

			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Modify Region Financial Support after rejection
	 * 
	 * @param task
	 * @param finDepSupport
	 * @param depsSupportDetailList
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doModify(WFTask task, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> depsSupportDetailList, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			// update object in db
			FinanceAccountsService.updateDepartmentAccountSupport(loginUser, finDepSupport, depsSupportDetailList, session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_regionSupportModifiedTask", "ar", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_regionSupportModifiedTask", "en", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });

			// get instance from object
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finDepSupport.getWfInstanceId());

			// complete the old task and add new dm task
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			completeWFTask(task, WFTaskActionsEnum.MODIFY.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Direct Manager Approval
	 * 
	 * @param dmTask
	 * @param finDepSupport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveDM(WFTask dmTask, FinDepSupportData finDepSupport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = dmTask.getArabicDetailsSummary();
			String englishDetailsSummary = dmTask.getEnglishDetailsSummary();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finDepSupport.getWfInstanceId());

			// complete dm task and add new sm task
			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Super Manager Approval
	 * 
	 * @param smTask
	 * @param finDepSupport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveSM(WFTask smTask, FinDepSupportData finDepSupport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = smTask.getArabicDetailsSummary();
			String englishDetailsSummary = smTask.getEnglishDetailsSummary();

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finDepSupport.getWfInstanceId());

			// complete old task and add new gm task
			Long originalId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			completeWFTask(smTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.GENERAL_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * General Manager Approval
	 * 
	 * @param task
	 * @param finDepSupport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveGM(WFTask task, FinDepSupportData finDepSupport, List<FinDepSupportDetailData> depsSupportDetailList, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			FinanceAccountsService.updateDepartmentAccountSupport(loginUser, finDepSupport, depsSupportDetailList, session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_regionSupportApprovedTask", "ar", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_regionSupportApprovedTask", "en", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finDepSupport.getWfInstanceId());

			// close the instance and send notification to the requester
			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			closeWFInstance(instance, task, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, session);
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * Rejection of any of the 3 manager (DM, SM, GM)
	 * 
	 * @param task
	 * @param finDepSupport
	 * @param loginUser
	 * @param attachments
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doReject(WFTask task, FinDepSupportData finDepSupport, EmployeeData loginUser, String attachments, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_regionSupportRejectTask", "ar", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_regionSupportRejectTask", "en", new Object[] { finDepSupport.getRequestNumber(), finDepSupport.getRequestDateString() });

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finDepSupport.getWfInstanceId());

			// complete old task and send notification to modify the region
			// support to the requester
			long originalId = instance.getRequesterId();
			long assigneeId = getDelegate(originalId, WFProcessesEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode());
			completeWFTask(task, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.FINANCIAL_DEPARTMENT_SUPPORT.getCode(), WFTaskRolesEnum.PROCESSING.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * set notification task action as notified
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void doNotify(WFTask currentTask) throws BusinessException {
		try {
			setWFTaskAction(currentTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(RegionFinancialSupportWorkFlow.class, e, "RegionFinancialSupportWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}