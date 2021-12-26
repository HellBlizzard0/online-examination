package com.code.services.workflow.securityanalysis;

import java.util.Date;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.GuidanceRequestData;
import com.code.dal.orm.securityanalysis.ReferralData;
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
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class SecurityAnalysisWorkFlow extends BaseWorkFlow {
	/**
	 * Default Constructor
	 */
	private SecurityAnalysisWorkFlow() {
	}

	/**
	 * Init Guidance Request
	 * 
	 * @param conversationData
	 * @param referralData
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initGuidanceRequest(ConversationData conversationData, ReferralData referralData, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			FollowUpData followUpData = FollowUpService.getFollowUpDataById(conversationData.getFollowUpId());
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestInstance", "ar", new Object[] { loginUser.getFullName(), followUpData.getCode(), referralData.getDetails() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_guidenceRequestInstance", "en", new Object[] { loginUser.getFullName(), followUpData.getCode(), referralData.getDetails() });
			Date hijriCurDate = HijriDateService.getHijriSysDate();

			WFInstance instance = addWFInstance(WFProcessesEnum.CONVERSATION_GUIDANCE_REQUEST.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), null, arabicDetailsSummary, englishDetailsSummary, session);
			conversationData.setInstanceId(instance.getInstanceId());
			DataAccess.updateEntity(conversationData.getConversation(), session);

			long originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId());
			long assigneeId = getDelegate(originalId, WFProcessesEnum.CONVERSATION_GUIDANCE_REQUEST.getCode());

			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.GUIDANCE_REQUEST.getCode(), WFTaskRolesEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

	}

	/**
	 * Approve Guidance Request
	 * 
	 * @param currentTask
	 * @param guidanceRequestData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doApproveGuidanceRequest(WFTask currentTask, GuidanceRequestData guidanceRequestData, ConversationData conversationData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			guidanceRequestData.setConversationId(conversationData.getId());
			ConversationService.saveGuidanceRequest(guidanceRequestData, session);

			Date currHijriDate = HijriDateService.getHijriSysDate();
			WFInstance instance = BaseWorkFlow.getWFInstanceById(currentTask.getInstanceId());

			closeWFInstance(instance, currentTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), currHijriDate, session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_approveGuidanceRequest", "ar", new Object[] { conversationData.getFollowUpCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_approveGuidanceRequest", "en", new Object[] { conversationData.getFollowUpCode() });

			Long requesterOriginalId = instance.getRequesterId();
			Long requesterAssigneeId = getDelegate(requesterOriginalId, WFProcessesEnum.CONVERSATION_GUIDANCE_REQUEST.getCode());

			addWFTask(instance.getInstanceId(), requesterAssigneeId, requesterOriginalId, new Date(), currHijriDate, WFTaskUrlEnum.GUIDANCE_REQUEST.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

	}

	/**
	 * Reject Guidance Request
	 * 
	 * @param currentTask
	 * @param referralData
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doRejectGuidanceRequest(WFTask currentTask, ReferralData referralData, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			ConversationData conversationData = ConversationService.getConversationsByConversationId(referralData.getConversationId());
			FollowUpData followupData = FollowUpService.getFollowUpDataById(conversationData.getFollowUpId());

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(followupData.getInstanceId());

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_guidanceRequestReject", "ar", new Object[] { followupData.getCode() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_guidanceRequestReject", "en", new Object[] { followupData.getCode() });

			completeWFTask(currentTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), WFTaskUrlEnum.GUIDANCE_REQUEST.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession) {
				session.rollbackTransaction();
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}

	}

	/**
	 * Send notifications to notifiee
	 * 
	 * @param requesterId
	 * @param subject
	 * @param arMessage
	 * @param enMessege
	 * @param notifieeIds
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void notify(long requesterId, String subject, String arMessage, String enMessege, Long notifieeId, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			WFInstance instance = addWFInstance(WFProcessesEnum.NOTIFICATIONS.getCode(), requesterId, new Date(), HijriDateService.getHijriSysDate(), WFInstanceStatusEnum.COMPLETED.getCode(), null, null, null, session);
			addWFTask(instance.getInstanceId(), requesterId, notifieeId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", subject, arMessage, enMessege, session);

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
				Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlowO");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
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
			Log4j.traceErrorException(SecurityAnalysisWorkFlow.class, e, "SecurityAnalysisWorkFlow");
			throw new BusinessException("error_general");
		}
	}

}
