package com.code.services.workflow.finance;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.finance.FinMonthlyRewDepDetail;
import com.code.dal.orm.finance.FinMonthlyReward;
import com.code.dal.orm.finance.FinYearApproval;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.EntitySequenceGeneratorEnum;
import com.code.enums.FinMonthlyRewardStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.infosys.finance.MonthlyRewardPaymentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class MonthlyRewardWorkFlow extends BaseWorkFlow {
	private MonthlyRewardWorkFlow() {
	}

	/**
	 * init workflow
	 * 
	 * @param finMonthlyReward
	 * @param finMonthlyRewDepDetailList
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void initFinMonthlyReward(FinMonthlyReward finMonthlyReward, List<FinMonthlyRewDepDetail> finMonthlyRewDepDetailList, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			finMonthlyReward.setRequestNumber(CommonService.generateSequenceNumber(EntitySequenceGeneratorEnum.MONTHLY_REWARD.getEntityId(), Integer.MAX_VALUE, session).toString());
			String[] hijriMonths = new String[] { "label_moharm", "label_safr", "label_rabieAwl", "label_rabieTani", "label_gamadAwl", "label_gamadTani", "label_ragb", "label_shaban", "label_ramdan", "label_shwal", "label_zwAlqada", "label_zwAlhaga" };
			FinYearApproval finYear = FinanceAccountsService.getFinYearApprovalById(finMonthlyReward.getFinYearApprovalId());
			String arInstanceMsg = getParameterizedMessage("wfMsg_monthlyRewardInstance", "ar", new Object[] { finMonthlyReward.getRequestNumber(), getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString(), finYear.getFinYear().toString() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_monthlyRewardInstance", "en", new Object[] { finMonthlyReward.getRequestNumber(), getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString(), finYear.getFinYear().toString() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.MONTHLY_REWARD.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments,arInstanceMsg,enInstanceMsg, session);

			finMonthlyReward.setwFInstanceId(instance.getInstanceId());
			MonthlyRewardPaymentService.saveFinMonthlyRewardPayment(finMonthlyReward, finMonthlyRewDepDetailList, loginUser, session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

			Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
			Long originalId = DepartmentService.getDepartmentManager(unitId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			finMonthlyReward.setwFInstanceId(null);

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MonthlyRewardWorkFlow.class, e, "MonthlyRewardWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Intelligence Department Manger Action and send to "MODER AL EDARA AL AMA" WHEN approved
	 * 
	 * @param dmTask
	 * @param action
	 * @param finMonthlyReward
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveRejectDM(WFTask dmTask, WFTaskActionsEnum action, FinMonthlyReward finMonthlyReward, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			String[] hijriMonths = new String[] { "label_moharm", "label_safr", "label_rabieAwl", "label_rabieTani", "label_gamadAwl", "label_gamadTani", "label_ragb", "label_shaban", "label_ramdan", "label_shwal", "label_zwAlqada", "label_zwAlhaga" };

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finMonthlyReward.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);

				finMonthlyReward.setStatus(FinMonthlyRewardStatusEnum.REJECTED.getCode());
				MonthlyRewardPaymentService.updateFinMonthlyReward(finMonthlyReward, loginUser, session);

				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
			} else {// action is approved

				String arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			}
			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MonthlyRewardWorkFlow.class, e, "MonthlyRewardWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * "MODER AL EDARA AL AMA" action and send to "Almodir Alam" when approved
	 * 
	 * @param dmTask
	 * @param action
	 * @param finMonthlyReward
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveRejectSM(WFTask dmTask, WFTaskActionsEnum action, FinMonthlyReward finMonthlyReward, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			String[] hijriMonths = new String[] { "label_moharm", "label_safr", "label_rabieAwl", "label_rabieTani", "label_gamadAwl", "label_gamadTani", "label_ragb", "label_shaban", "label_ramdan", "label_shwal", "label_zwAlqada", "label_zwAlhaga" };

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finMonthlyReward.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				closeWFInstance(instance, dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), currHijriDate, session);

				finMonthlyReward.setStatus(FinMonthlyRewardStatusEnum.REJECTED.getCode());
				MonthlyRewardPaymentService.updateFinMonthlyReward(finMonthlyReward, loginUser, session);

				long originalId = instance.getRequesterId();
				long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);
			} else {// action is approved
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardWorkflow", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				Long originalId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.GENERAL_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			}
			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MonthlyRewardWorkFlow.class, e, "MonthlyRewardWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * "ALMODER AL 3AM" action and end Instance
	 * 
	 * @param dmTask
	 * @param action
	 * @param finMonthlyReward
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveRejectGM(WFTask dmTask, WFTaskActionsEnum action, FinMonthlyReward finMonthlyReward, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			String[] hijriMonths = new String[] { "label_moharm", "label_safr", "label_rabieAwl", "label_rabieTani", "label_gamadAwl", "label_gamadTani", "label_ragb", "label_shaban", "label_ramdan", "label_shwal", "label_zwAlqada", "label_zwAlhaga" };
			String arabicDetailsSummary = "";
			String englishDetailsSummary = "";
			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(finMonthlyReward.getwFInstanceId());

			if (action.equals(WFTaskActionsEnum.REJECT)) {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardReject", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				finMonthlyReward.setStatus(FinMonthlyRewardStatusEnum.REJECTED.getCode());
			} else {
				arabicDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardApproved", "ar", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "ar"), finMonthlyReward.getHijriYear().toString() });
				englishDetailsSummary = getParameterizedMessage("wfMsg_monthlyRewardApproved", "en", new Object[] { getParameterizedMessage(hijriMonths[finMonthlyReward.getMonthNumber() - 1], "en"), finMonthlyReward.getHijriYear().toString() });

				finMonthlyReward.setStatus(FinMonthlyRewardStatusEnum.APPROVED.getCode());
			}
			MonthlyRewardPaymentService.updateFinMonthlyReward(finMonthlyReward, loginUser, session);

			closeWFInstance(instance, dmTask, action.getCode(), new Date(), currHijriDate, session);

			long originalId = instance.getRequesterId();
			long assigneeId = getDelegate(originalId, WFProcessesEnum.MONTHLY_REWARD.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), currHijriDate, WFTaskUrlEnum.MONTHLY_REWARD.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(MonthlyRewardWorkFlow.class, e, "MonthlyRewardWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param currentTask
	 * @throws BusinessException
	 */
	public static void doNotify(WFTask currentTask) throws BusinessException {
		try {
			setWFTaskAction(currentTask, WFTaskActionsEnum.NOTIFIED.getCode(), new Date(), HijriDateService.getHijriSysDate());
		} catch (Exception e) {
			Log4j.traceErrorException(MonthlyRewardWorkFlow.class, e, "MonthlyRewardWorkFlow");
			throw new BusinessException("error_general");
		}
	}
}
