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
import com.code.dal.orm.workflow.WFTask;
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

public class InfoHQWorkFlow extends InfoWorkFlow {

	private InfoHQWorkFlow() {
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
	public static void initInfo(InfoData infoRequest, String attachments, EmployeeData emp, CustomSession... useSession) throws BusinessException {
		// Long processId will be detected.
		// TaskUrl is enum detected.
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			InfoSubject subject = InfoTypeService.getsubjectById(infoRequest.getInfoSubjectId());
			InfoType type = InfoTypeService.getInfoTypeById(subject.getInfoTypeId());
			String arInstanceMsg = getParameterizedMessage("wfMsg_infoInstance", "ar", new Object[] { infoRequest.getInfoNumber(), type.getDescription(), subject.getDescription(), infoRequest.getConfidentiality() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_infoInstance", "en", new Object[] { infoRequest.getInfoNumber(), type.getDescription(), subject.getDescription(), infoRequest.getConfidentiality() });

			WFInstance instance = addWFInstance(WFProcessesEnum.INFORMATION.getCode(), emp.getEmpId(), new Date(), HijriDateService.getHijriSysDate(), WFInstanceStatusEnum.RUNING.getCode(), null, arInstanceMsg, enInstanceMsg, session);
			infoRequest.setwFInstanceId(instance.getInstanceId());
			infoRequest.setStatus(InfoStatusEnum.SENT_FOR_ACTION.getCode());
			DataAccess.updateEntity(infoRequest.getInfo(), session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId());
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
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
	 * @param attachments
	 * @param emp
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void resendInfo(WFTask dmTask, InfoData infoRequest, String attachments, EmployeeData emp, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			infoRequest.setStatus(InfoStatusEnum.SENT_FOR_ACTION.getCode());
			DataAccess.updateEntity(infoRequest.getInfo(), session);

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId());
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), HijriDateService.getHijriSysDate(), infoRequest.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			Long generalManagerId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
			Long generalManagerAssigneeId = getDelegate(generalManagerId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(infoRequest.getwFInstanceId(), generalManagerAssigneeId, generalManagerId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
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
	public static void doInfoDM(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum isSaving, EmployeeData emp, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			Date curData = new Date();
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			if (isSaving.getCode().equals(WFTaskActionsEnum.SAVE.getCode())) {
				closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), curData, hijriCurDate, session);
				infoRequest.setStatus(InfoStatusEnum.DONE.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
			} else {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId());
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
				infoRequest.setStatus(InfoStatusEnum.ACTION_DONE.getCode());
				InfoService.updateInfo(infoRequest, emp, session);
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), curData, hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}
			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * If "Moder eledara el3ama lel2mn welste5barat" took WFTaskActionsEnum.save action then end instance and send notifications according to recommendations else WFTaskActionsEnum.REQUEST_ANALYSIS then create task for analysis at selected departments and send notifications for selected departments and create task for "Moder eledara el3ama lel2mn welste5barat" for else WFTaskActionsEnum.SEND then send notification letters to nase5 in selected departments
	 * 
	 * @param infoRequest
	 * @param processId
	 * @param attachments
	 * @param taskUrl
	 * @throws BusinessException
	 */
	public static void doInfoSM(WFTask dmTask, InfoData infoRequest, List<DepartmentData> processingList, List<DepartmentData> notifiedList, List<InfoRecommendationData> infoRecmndsData, String attachments, WFTaskActionsEnum action, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			Date curData = new Date();
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());

			// send notification for all selected notified departments
			for (DepartmentData analysis : notifiedList) {
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

				Long originalId = DepartmentService.getDepartmentManager(analysis.getId());
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
				if (analysis.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					addWFTask(instance.getInstanceId(), assigneeId, originalId, curData, hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					String msg = getParameterizedMessage("wfMsg_infoNotification", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getSubjectDescription() });
					addWFTask(instance.getInstanceId(), assigneeId, originalId, curData, hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			// user select request analysis
			if (action.equals(WFTaskActionsEnum.REQUEST_ANALYSIS)) {
				for (DepartmentData analysis : processingList) {
					String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowAnalysis", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
					String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowAnalysis", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

					Long originalId = DepartmentService.getDepartmentManager(analysis.getId());
					Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
					addWFTask(instance.getInstanceId(), assigneeId, originalId, curData, hijriCurDate, WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.GENERAL_INFO_ANALYSIS_DEPARTMENT_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

				completeWFTask(dmTask, WFTaskActionsEnum.REQUEST_ANALYSIS.getCode(), curData, hijriCurDate, instance.getInstanceId(), dmTask.getAssigneeId(), dmTask.getOriginalId(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR_ANALYSIS.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {// user select save or pass information
				String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

				Long managerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId());
				Long managerAssigneeId = getDelegate(managerOriginalId, WFProcessesEnum.INFORMATION.getCode());
				for (InfoRecommendationData infoRcmd : infoRecmndsData) {
					String msg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });

					if (infoRcmd.getSecurityCheck())
						msg += getParameterizedMessage("label_securityCheck", "ar");
					if (infoRcmd.getSurveillance())
						msg += getParameterizedMessage("label_Surveillance", "ar");

					if (infoRcmd.getSecurityCheck() || infoRcmd.getSurveillance()) {
						addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, curData, hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
					}

					if (infoRcmd.getLabCheck()) {
						Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId());
						Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.INFORMATION.getCode());
						String labCheckmsg = getParameterizedMessage("wfMsg_infoRecommndationEmpName", "ar", new Object[] { infoRequest.getInfoNumber(), infoRcmd.getEmployeeFullName() });
						labCheckmsg += getParameterizedMessage("label_labCheck", "ar");
						addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, curData, hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", labCheckmsg, arabicDetailsSummary, englishDetailsSummary, session);
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

					addWFTask(instance.getInstanceId(), managerAssigneeId, managerOriginalId, curData, hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", agentMsg, arabicDetailsSummary, englishDetailsSummary, session);
				}

				if (action.equals(WFTaskActionsEnum.SAVE)) {
					closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SAVE.getCode(), curData, hijriCurDate, session);
				} else { // send
					closeWFInstance(instance.getwFInstance(), dmTask, WFTaskActionsEnum.SEND.getCode(), curData, hijriCurDate, session);
					Long writerOriginalId = instance.getRequesterId();
					Long writerAssigneeId = getDelegate(writerOriginalId, WFProcessesEnum.INFORMATION.getCode());
					String msg = getParameterizedMessage("wfMsg_infoWriter", "ar", new Object[] { infoRequest.getInfoNumber() }); // waiting
					addWFTask(instance.getInstanceId(), writerAssigneeId, writerOriginalId, curData, hijriCurDate, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);
				}
			}
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Long generalManagerId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_MANAGER.getCode()).getEmpId();
			Long generalManagerAssigneeId = getDelegate(generalManagerId, WFProcessesEnum.INFORMATION.getCode());
			addWFTask(instance.getInstanceId(), generalManagerAssigneeId, generalManagerId, new Date(), HijriDateService.getHijriSysDate(), WFTaskUrlEnum.INFORMATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
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
	public static void doInfoAnalysis(WFTask dmTask, InfoData infoRequest, String attachments, WFTaskActionsEnum action, String depName, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoAnalysisFinish", "ar", new Object[] { infoRequest.getInfoNumber(), depName });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoAnalysisFinish", "en", new Object[] { infoRequest.getInfoNumber(), depName });
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId());
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.INFORMATION.getCode());
			String msg = getParameterizedMessage("wfMsg_infoAnalysisFinish", "ar", new Object[] { infoRequest.getInfoNumber(), depName });
			completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), HijriDateService.getHijriSysDate(), instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.NOTIFICATION.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", msg, arabicDetailsSummary, englishDetailsSummary, session);

			if (!isOpenedSession)
				session.commitTransaction();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * First : send notifications according to recommendations Second : If "Moder eledara el3ama lel2mn welste5barat" took WFTaskActionsEnum.save then end instance else WFTaskActionsEnum.SEND then send notification to nase5 in selected departments
	 **/
	public static void doInfoSMAnalysisReview(WFTask dmTask, InfoData infoRequest, List<InfoRecommendationData> infoRecmndsData, String attachments, WFTaskActionsEnum action, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession)
				session.beginTransaction();

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "ar", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_infoWorkflowGeneral", "en", new Object[] { infoRequest.getInfoNumber(), infoRequest.getRegisterationDateString() });

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstanceData instance = getWFInstanceDataById(dmTask.getInstanceId());
			Long managerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId());
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
					Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId());
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
				Log4j.traceErrorException(InfoHQWorkFlow.class, e, "InfoHQWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession)
				session.close();
		}
	}

	/**
	 * get DIRECTORATE analysis departments
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public static List<DepartmentData> getAnalysisDepartments() throws BusinessException {
		Long[] depIdsArray = new Long[] { WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId(), WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId(), WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_MILITARY_POLICE_DEPARTMENT.getCode()).getUnitId(), WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId() };
		List<DepartmentData> departmentList = DepartmentService.getDepartmentsByMultipleIds(depIdsArray);
		// departmentList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.EASTERN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.ALMADINA_ALMNWARA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.ASEER_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.GAZAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.GOUF_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.MAKA_ELMOKRAMA_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.NAGRAN_REGION_DIRECTOR_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.NORTH_BORDER_REGION_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId()));
		departmentList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.TABOK_REGION_DIRECTOR_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId()));
		return departmentList;
	}
}