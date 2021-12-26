package com.code.services.workflow.info;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.info.InfoRecommendationData;
import com.code.dal.orm.info.InfoSubject;
import com.code.dal.orm.info.InfoType;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFPosition;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.InfoStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.infosys.info.InfoTypeService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;

public class InfoRegionWorkFlow extends InfoWorkFlow {

	private InfoRegionWorkFlow() {
	}

	/**
	 * Save wf instacne and task at "Moder edaret el este5barat" to approve or saveOnly(end instance)
	 * 
	 * @param infoRequest
	 * @param processId
	 * @param attachments
	 * @param taskUrl
	 * @throws BusinessException
	 */
	public static void initInfo(InfoData infoRequest, Long regionId, String attachments, EmployeeData emp, CustomSession... useSession) throws BusinessException {
		// long processId will be detected
		// taskUrl is enum detected
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			InfoSubject subject = InfoTypeService.getsubjectById(infoRequest.getInfoSubjectId());
			InfoType type = InfoTypeService.getInfoTypeById(subject.getInfoTypeId());

			String arInstanceMsg = getParameterizedMessage("wfMsg_infoInstance", "ar", new Object[] { infoRequest.getInfoNumber(), type.getDescription(), subject.getDescription(), infoRequest.getConfidentiality() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_infoInstance", "en", new Object[] { infoRequest.getInfoNumber(), type.getDescription(), subject.getDescription(), infoRequest.getConfidentiality() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = addWFInstance(WFProcessesEnum.INFORMATION.getCode(), emp.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), null, arInstanceMsg, enInstanceMsg, session);
			infoRequest.setwFInstanceId(instance.getInstanceId());
			infoRequest.setStatus(InfoStatusEnum.SENT_FOR_ACTION.getCode());
			DataAccess.updateEntity(infoRequest.getInfo(), session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * resendInfo
	 * 
	 * @param infoRequest
	 * @param regionId
	 * @param attachments
	 * @param emp
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void resendInfo(WFTask dmTask, InfoData infoRequest, Long regionId, String attachments, EmployeeData emp, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			infoRequest.setStatus(InfoStatusEnum.SENT_FOR_ACTION.getCode());
			DataAccess.updateEntity(infoRequest.getInfo(), session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, infoRequest.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			Long generalManagerId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
			Long generalManagerAssigneeId = getDelegate(generalManagerId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(infoRequest.getwFInstanceId(), generalManagerAssigneeId, generalManagerId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder edaret el este5barat" took WFTaskActionsEnum.save then end instance and update info status else took action WFTaskActionsEnum.approve then forward to super manager"Moder eledara el3ama lel2mn welste5barat"
	 * 
	 * @param infoRequest
	 * @param processId
	 * @param attachments
	 * @param taskUrl
	 * @throws BusinessException
	 */
	public static void doInfoDM(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum isSaving, Long regionId, EmployeeData emp, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			if (isSaving.getCode().equals(WFTaskActionsEnum.SAVE.getCode())) {
				closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), new Date(), hijriCurDate, session);
				infoRequest.setStatus(InfoStatusEnum.DONE.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
			} else {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionSecurityIntelligenceDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * doInfoRegionIntelligenceSecuirtyManager
	 * 
	 * @param dmTask
	 * @param infoRequest
	 * @param attachments
	 * @param isSaving
	 * @param regionId
	 * @param emp
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void doInfoRegionIntelligenceSecuirtyManager(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum isSaving, Long regionId, EmployeeData emp, CustomSession... useSession) throws BusinessException {

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			if (isSaving.getCode().equals(WFTaskActionsEnum.SAVE.getCode())) {
				closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), new Date(), hijriCurDate, session);
				infoRequest.setStatus(InfoStatusEnum.DONE.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
			} else {
				Long originalId = DepartmentService.getDepartmentManager(regionId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
				infoRequest.setStatus(InfoStatusEnum.ACTION_DONE.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Qa2ed el mante2a" took WFTaskActionsEnum.save action then end instance and send notifications according to recommendations and **** notify HQ if any **** else WFTaskActionsEnum.REQUEST_ANALYSIS then create task for analysis at selected departments and send notifications for selected departments and create task for "Qa2ed el mante2a" for else WFTaskActionsEnum.SEND then send letters notification to nase5 in selected departments
	 * 
	 * @param infoRequest
	 * @param processId
	 * @param attachments
	 * @param taskUrl
	 * @throws BusinessException
	 */
	public static void doInfoSM(WFTask dmTask, InfoData infoRequest, Long regionId, List<DepartmentData> processingList, List<DepartmentData> notifiedList, List<InfoRecommendationData> infoRecmndsData, String attachments, WFTaskActionsEnum action, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());

			for (DepartmentData analysis : notifiedList) {
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				Long originalId = null;
				if (analysis.getRegionId() == null) {
					originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId());
				} else {
					originalId = DepartmentService.getDepartmentManager(analysis.getId());
				}
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
				if (analysis.getRegionId() != null && analysis.getRegionId().equals(regionId))
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				else {
					String msg = getParameterizedMessage("wfMsg_infoNotification", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getSubjectDescription() });
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			if (action.equals(WFTaskActionsEnum.REQUEST_ANALYSIS)) {
				for (DepartmentData analysis : processingList) {
					String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowAnalysis", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
					String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowAnalysis", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

					Long originalId = DepartmentService.getDepartmentManager(analysis.getId());
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
					addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

				completeWFTask(dmTask, WFTaskActionsEnum.REQUEST_ANALYSIS.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), dmTask.getAssigneeId(), dmTask.getOriginalId(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.REGION_DIRECTOR_ANALYSIS.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				Long managerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
				Long managerAssigneeId = getDelegate(managerOriginalId, WFProcessesEnum.INFORMATION.getCode());
				for (InfoRecommendationData infoRcmd : infoRecmndsData) {
					String msg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });

					if (infoRcmd.getSecurityCheck())
						msg += getParameterizedMessage("label_securityCheck", "ar");
					if (infoRcmd.getSurveillance())
						msg += getParameterizedMessage("label_Surveillance", "ar");
					if (infoRcmd.getSecurityCheck() || infoRcmd.getSurveillance()) {
						addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
					}

					if (infoRcmd.getLabCheck()) {
						Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
						Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.INFORMATION.getCode());
						String labCheckmsg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });
						labCheckmsg += getParameterizedMessage("label_labCheck", "ar");
						addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", labCheckmsg, arabicDetailsSummary, englishDetailsSummary, session);
					}
				}

				if (infoRequest.getAgentApprove() || infoRequest.getAgentEvaluation() || infoRequest.getPayReward()) {
					String agentMsg = getParameterizedMessage("wfMsg_infoAgent", "ar", new Object[] { infoRequest.getInfoNumber() });
					agentMsg += "{";
					for (AssignmentDetailData detail : AssignmentService.getInfoAssignments(infoRequest.getId())) {
						agentMsg += detail.getAgentCode() + " ,";
					}
					agentMsg += "} : \n";
					if (infoRequest.getAgentApprove())
						agentMsg += getParameterizedMessage("label_agentApprove", "ar");
					if (infoRequest.getAgentEvaluation())
						agentMsg += getParameterizedMessage("label_agentEvaluation", "ar");
					if (infoRequest.getPayReward())
						agentMsg += getParameterizedMessage("label_payReward", "ar");

					if (infoRequest.getAgentApprove() || infoRequest.getAgentEvaluation() || infoRequest.getPayReward()) {
						addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", agentMsg, arabicDetailsSummary, englishDetailsSummary, session);
					}
				}

				if (action.equals(WFTaskActionsEnum.SAVE)) {
					closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), new Date(), hijriCurDate, session);
				} else // send
				{
					closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, session);
					Long writerOriginalId = instance.getRequesterId();
					Long writerAssigneeId = getDelegate(writerOriginalId, WFProcessesEnum.INFORMATION.getCode());
					String msg = getParameterizedMessage("wfMsg_infoWriter", "ar", new Object[] { infoRequest.getInfoNumber() }); // waiting
					addWFTask(instance.getInstanceId(), writerAssigneeId, writerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long generalManagerId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
			Long generalManagerAssigneeId = getDelegate(generalManagerId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(instance.getInstanceId(), generalManagerAssigneeId, generalManagerId, new Date(), hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * send notification to "Moder eledara el3ama lel2mn welste5barat" with finished analysis
	 * 
	 * @param infoRequest
	 * @param processId
	 * @param attachments
	 * @param taskUrl
	 * @throws BusinessException
	 */
	public static void doInfoAnalysis(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum action, Long regionId, String depName, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoAnalysisFinish", "ar", new Object[] { infoRequest.getInfoNumber(), depName });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoAnalysisFinish", "en", new Object[] { infoRequest.getInfoNumber(), depName });
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			Long originalId = DepartmentService.getDepartmentManager(regionId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			String msg = getParameterizedMessage("wfMsg_infoAnalysisFinish", "ar", new Object[] { infoRequest.getInfoNumber(), depName });
			completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * First : send notifications according to recommendations Second : If "Moder eledara el3ama lel2mn welste5barat" took WFTaskActionsEnum.save then end instance else WFTaskActionsEnum.SEND then send notification to nase5 in selected departments Third : Notify HQ if any
	 **/
	public static void doInfoSMAnalysisReview(WFTask dmTask, Long regionId, InfoData infoRequest, List<InfoRecommendationData> infoRecmndsData, String attachments, WFTaskActionsEnum action, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			Long managerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionIntelligenceDepartmentId(regionId));
			Long managerAssigneeId = getDelegate(managerOriginalId, WFProcessesEnum.INFORMATION.getCode());
			for (InfoRecommendationData infoRcmd : infoRecmndsData) {
				String msg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });

				if (infoRcmd.getSecurityCheck())
					msg += getParameterizedMessage("label_securityCheck", "ar");
				if (infoRcmd.getSurveillance())
					msg += getParameterizedMessage("label_Surveillance", "ar");

				if (infoRcmd.getSecurityCheck() || infoRcmd.getSurveillance()) {
					addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
				}

				if (infoRcmd.getLabCheck()) {
					Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
					Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.INFORMATION.getCode());
					String labCheckmsg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });
					labCheckmsg += getParameterizedMessage("label_labCheck", "ar");
					addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", labCheckmsg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			if (infoRequest.getAgentApprove() || infoRequest.getAgentEvaluation() || infoRequest.getPayReward()) {
				String agentMsg = getParameterizedMessage("wfMsg_infoAgent", "ar", new Object[] { infoRequest.getInfoNumber() });
				agentMsg += "{";
				for (AssignmentDetailData detail : AssignmentService.getInfoAssignments(infoRequest.getId())) {
					agentMsg += detail.getAgentCode() + " ,";
				}
				agentMsg += "} : \n";
				if (infoRequest.getAgentApprove())
					agentMsg += getParameterizedMessage("label_agentApprove", "ar");
				if (infoRequest.getAgentEvaluation())
					agentMsg += getParameterizedMessage("label_agentEvaluation", "ar");
				if (infoRequest.getPayReward())
					agentMsg += getParameterizedMessage("label_payReward", "ar");

				if (infoRequest.getAgentApprove() || infoRequest.getAgentEvaluation() || infoRequest.getPayReward()) {
					addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", agentMsg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			if (action.equals(WFTaskActionsEnum.SAVE)) {
				closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), new Date(), hijriCurDate, session);
			} else // send
			{
				closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, session);
				Long writerOriginalId = instance.getRequesterId();
				Long writerAssigneeId = getDelegate(writerOriginalId, WFProcessesEnum.INFORMATION.getCode());
				String msg = getParameterizedMessage("wfMsg_infoWriter", "ar", new Object[] { infoRequest.getInfoNumber() }); // waiting
				addWFTask(instance.getInstanceId(), writerAssigneeId, writerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
			}

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * get region AnalysisDepartments
	 * 
	 * @param regionId
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getAnalysisDepartments(long regionId) throws BusinessException {
		try {
			WFPosition pos = WFPositionService.getWFPositionByUnitId(regionId);
			Long[] depIdsArray = new Long[3];
			if (pos.getPositionId().equals((long) WFPositionsEnum.ALMADINA_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.EASTERN_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ASEER_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GAZAN_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.GOUF_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.MAKA_ELMOKRAMA_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NAGRAN_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.NORTH_BORDER_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.TABOK_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_ANTI_DRUG_DIRECTORATE.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_DIRECTOR_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId();
			} else if (pos.getPositionId().equals((long) WFPositionsEnum.ACADEMY_REGION_DEPARTMENT.getCode())) {
				depIdsArray[0] = WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_ANTI_DRUG_DIRECTORATE.getCode()).getUnitId();
				depIdsArray[1] = WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_DIRECTOR_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId();
				depIdsArray[2] = WFPositionService.getWFPosition(WFPositionsEnum.ACADEMY_REGION_MILITARY_POLICE_DIPARTMENT.getCode()).getUnitId();
			}

			List<DepartmentData> departmentList = DepartmentService.getDepartmentsByMultipleIds(depIdsArray);
			DepartmentData hQDepartment = DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());
			hQDepartment.setSelected(true);
			departmentList.add(hQDepartment);
			List<DepartmentData> regionDepartmentList = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
			int index = 0;
			for (int i = 0; i < regionDepartmentList.size(); i++) {
				if (regionDepartmentList.get(i).getId().equals(regionId)) {
					index = i;
				}
				regionDepartmentList.get(i).setSelected(true);
			}
			regionDepartmentList.remove(index);
			departmentList.addAll(regionDepartmentList);
			return departmentList;
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoRegionWorkFlow.class, e, "InfoRegionWorkFlow");
				throw new BusinessException("error_general");
			}
		}
	}

}