package com.code.services.workflow.labcheck;

import java.util.Date;
import java.util.List;

import com.code.dal.CustomSession;
import com.code.dal.DataAccess;
import com.code.dal.orm.labcheck.LabCheck;
import com.code.dal.orm.labcheck.LabCheckEmployeeData;
import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.enums.LabCheckEmployeeCheckStatusEnum;
import com.code.enums.LabCheckReasonsEnum;
import com.code.enums.LabCheckStatusEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFPositionsEnum;
import com.code.enums.WFProcessesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.enums.WFTaskUrlEnum;
import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.services.workflow.WFPositionService;

public class LabCheckWorkFlow extends BaseWorkFlow {
	 
	 //private static final String ORDERNUMBER_REGEX = "[a-zA-Z0-9\\\\/]+"; // support English characters , numbers, / or \
	 private static final String ORDERNUMBER_ARABIC_REGEX = "[\\u0621-\\u064A0-9\\u0660-\\u0669\\\\/]+"; // support Arabic characters , Arabic and English numbers, / or \
	 
	private LabCheckWorkFlow() {
	}

	/**
	 * Init approved lab check
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param employeeDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initApprovedLabCheckAndApproveTask(WFTask dmTask, LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			initApprovedLabCheck(labCheck, employeeDataList, loginUser, session);
			closeInstanceAndApproveTask(dmTask, session);

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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}

		}
	}

	/**
	 * Init approved lab check
	 * 
	 * @param labCheck
	 * @param employeeDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initApprovedLabCheck(LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			String arInsanceMsg = getParameterizedMessage("wfMsg_labCheckInstance", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderSourceDomainDescription(), getParameterizedMessage("label_promotion", "ar") });
			String enInstanceMsg = getParameterizedMessage("wfMsg_labCheckInstance", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderSourceDomainDescription(), getParameterizedMessage("label_promotion", "ar") });
			WFInstance instance = addWFInstance(WFProcessesEnum.LAB_CHECK.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.COMPLETED.getCode(), null, arInsanceMsg, enInstanceMsg, session);
			labCheck.setwFInstanceId(instance.getInstanceId());
			labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
			LabCheckService.saveLabCheck(labCheck, employeeDataList, loginUser, session);

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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}

		}
	}

	/**
	 * Init approved lab check
	 * 
	 * @param labCheck
	 * @param employeeDataList
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void initDecisionLabCheck(LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			String arInsanceMsg = getParameterizedMessage("wfMsg_labCheckInstanceDecision", "ar", new Object[] { employeeDataList.get(0).getEmployeeFullName() });
			String enInstanceMsg = getParameterizedMessage("wfMsg_labCheckInstanceDecision", "en", new Object[] { employeeDataList.get(0).getEmployeeFullName() });
			WFInstance instance = addWFInstance(WFProcessesEnum.LAB_CHECK.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), null, arInsanceMsg, enInstanceMsg, session);
			labCheck.setStatus(LabCheckStatusEnum.UNDER_APPROVAL.getCode());
			labCheck.setwFInstanceId(instance.getInstanceId());
			LabCheckService.saveLabCheck(labCheck, employeeDataList, loginUser, session);
			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflowDecision", "ar", new Object[] { employeeDataList.get(0).getEmployeeFullName() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflowDecision", "en", new Object[] { employeeDataList.get(0).getEmployeeFullName() });
			Long regionId = DepartmentService.isRegionDepartment(employeeDataList.get(0).getEmployeeDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.DRUG_HCM_DECISION.getCode(), WFTaskRolesEnum.DECISION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.DRUG_HCM_DECISION.getCode(), WFTaskRolesEnum.DECISION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Init lab check workflow send task to "Moder edartat almo5drat"
	 * 
	 * @param labCheck
	 * @param employeeDataList
	 * @param deletedEmployeeList
	 * @param loginUser
	 * @param attachments
	 * @param dmTask
	 * @throws BusinessException
	 */
	public static void initLabCheck(LabCheck labCheck, List<LabCheckEmployeeData> employeeDataList, EmployeeData loginUser, String attachments, WFTask dmTask) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			String checkReasonMsg = "";
			if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
				checkReasonMsg = "label_cases";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
				checkReasonMsg = "label_medicalCommitteeCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
				checkReasonMsg = "label_promotion";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
				checkReasonMsg = "label_treatmentPurpose";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
				checkReasonMsg = "label_randomCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
				checkReasonMsg = "label_periodicCheck";
			else
				checkReasonMsg = "label_unexpected";

			if (labCheck.getwFInstanceId() == null) {
				String arInsanceMsg = getParameterizedMessage("wfMsg_labCheckInstance", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderSourceDomainDescription(), getParameterizedMessage(checkReasonMsg, "ar") });
				String enInstanceMsg = getParameterizedMessage("wfMsg_labCheckInstance", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderSourceDomainDescription(), getParameterizedMessage(checkReasonMsg, "ar") });

				WFInstance instance = addWFInstance(WFProcessesEnum.LAB_CHECK.getCode(), loginUser.getEmpId(), new Date(), hijriCurDate, WFInstanceStatusEnum.RUNING.getCode(), attachments, arInsanceMsg, enInstanceMsg, session);
				labCheck.setwFInstanceId(instance.getInstanceId());
				labCheck.setStatus(LabCheckStatusEnum.UNDER_APPROVAL.getCode());
			}
			
			LabCheckService.saveLabCheck(labCheck, employeeDataList, loginUser, session);

			for (LabCheckEmployeeData employeeData : employeeDataList) {
				List<LabCheckEmployeeData> previousLabCheckEmployeesData = LabCheckService.getPervEmployeesLabChecks(employeeData.getEmployeeId(), null);
				int noPostiveChecks = 0;
				for (LabCheckEmployeeData labCheckData : previousLabCheckEmployeesData) {
					if (labCheckData.getCheckStatus() != null && labCheckData.getCheckStatus().equals(LabCheckEmployeeCheckStatusEnum.POSITIVE.getCode())) {
						noPostiveChecks++;
						if (noPostiveChecks == InfoSysConfigurationService.getPostiveChecks()) {
							Object[] Params = new Object[3];
							Params[0] = labCheckData.getEmployeeSocialId();
							Params[1] = labCheckData.getEmployeeFullName();
							Params[2] = InfoSysConfigurationService.getPostiveChecks();
							throw new BusinessException("error_PostivePreviousChecks", Params);
						}
					}
				}
			}
			

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				if (dmTask == null) {
					addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, labCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				if (dmTask == null) {
					addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, labCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
			}

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (dmTask == null) {
				labCheck.setwFInstanceId(null);
			}

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Approve action for "Moder edartat almo5drat" and send task to "Moder aledara alamma / region dorector"
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveDM(WFTask dmTask, LabCheck labCheck, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			String checkReasonMsg = "";
			if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
				checkReasonMsg = "label_cases";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
				checkReasonMsg = "label_medicalCommitteeCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
				checkReasonMsg = "label_promotion";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
				checkReasonMsg = "label_treatmentPurpose";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
				checkReasonMsg = "label_randomCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
				checkReasonMsg = "label_periodicCheck";
			else
				checkReasonMsg = "label_unexpected";

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionProtectionDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_PROTECTION_DIRECTORATE.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.GENERAL_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * doApproveRegionSecurityManager
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveRegionProtectionManager(WFTask dmTask, LabCheck labCheck, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			String checkReasonMsg = "";
			if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
				checkReasonMsg = "label_cases";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
				checkReasonMsg = "label_medicalCommitteeCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
				checkReasonMsg = "label_promotion";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
				checkReasonMsg = "label_treatmentPurpose";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
				checkReasonMsg = "label_randomCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
				checkReasonMsg = "label_periodicCheck";
			else
				checkReasonMsg = "label_unexpected";

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());

			Long originalId = DepartmentService.getDepartmentManager(regionId);
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
			completeWFTask(dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.REGION_DIRECTOR.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * doReject
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doReject(WFTask dmTask, LabCheck labCheck, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();
			if (dmTask.getNotes() == null || dmTask.getNotes().isEmpty() || dmTask.getNotes().trim().equals("")) {
				throw new BusinessException("error_rejectReasonMandatory");
			}
			dmTask.setRefuseReasons(dmTask.getNotes());

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			String checkReasonMsg = "";
			if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
				checkReasonMsg = "label_cases";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
				checkReasonMsg = "label_medicalCommitteeCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
				checkReasonMsg = "label_promotion";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
				checkReasonMsg = "label_treatmentPurpose";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
				checkReasonMsg = "label_randomCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
				checkReasonMsg = "label_periodicCheck";
			else
				checkReasonMsg = "label_unexpected";

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflowReject", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflowReject", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

			completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.REQUESTER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Approve action for "Moder aledara alamma / region dorector" send notification to "zabt edarat al est5barat"
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param labCheckEmployeeList
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveSM(WFTask dmTask, LabCheck labCheck, List<LabCheckEmployeeData> labCheckEmployeeList, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
			LabCheckService.updateLabCheck(labCheck, loginUser, session);
			for (LabCheckEmployeeData labEmp : labCheckEmployeeList) {
				labEmp.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
				LabCheckService.updateLabCheckEmployee(labEmp, loginUser, session);
			}

			closeWFInstance(instance, dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, session);

			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
			String checkReasonMsg = "";
			if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
				checkReasonMsg = "label_cases";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
				checkReasonMsg = "label_medicalCommitteeCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
				checkReasonMsg = "label_promotion";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
				checkReasonMsg = "label_treatmentPurpose";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
				checkReasonMsg = "label_randomCheck";
			else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
				checkReasonMsg = "label_periodicCheck";
			else
				checkReasonMsg = "label_unexpected";

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
				Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				Long drugManagerOriginalId = DepartmentService.getDepartmentManager(unitId);
				Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			}

			// TODO to be removed later after approval (Setup Data problems)
			// List<Long> depManagerIds = new ArrayList<Long>();
			// for (LabCheckEmployeeData labEmp : labCheckEmployeeList) {
			// Long depId = EmployeeService.getEmployee(labEmp.getEmployeeId()).getActualDepartmentId();
			// Long managerId = DepartmentService.getDepartmentManagerId(depId);
			// if (managerId != null && !depManagerIds.contains(managerId)) {
			// Long managerAssigneeId = getDelegate(managerId, WFProcessesEnum.LAB_CHECK.getCode());
			// addWFTask(instance.getInstanceId(), managerAssigneeId, managerId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
			// depManagerIds.add(managerId);
			// }
			// }

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Approve the group of lab check tasks by "Moder l 2mn wl 7maya / region director" and send notification to "zabt edarat al est5barat"
	 * 
	 * @param selectedLabCheckList
	 * @param labCheckWFTaskIds
	 * @param loginUser
	 * @throws BusinessException
	 */
	public static void doCollectiveApproveSM(List<LabCheck> selectedLabCheckList, Long[] labCheckWFTaskIds, EmployeeData loginUser) throws BusinessException {
		CustomSession session = DataAccess.getSession();
		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			List<WFTask> taskList = getWFTaskByTaskIdsList(labCheckWFTaskIds);

			for (LabCheck selectedLabCheck : selectedLabCheckList) {
				WFTask dmTask = new WFTask();
				LabCheck labCheck = new LabCheck();
				for (WFTask wfTask : taskList) {
					if (selectedLabCheck.getwFInstanceId().equals(wfTask.getInstanceId())) {
						labCheck = selectedLabCheck;
						dmTask = wfTask;
						break;
					}
				}
				List<LabCheckEmployeeData> labCheckEmployeeList = LabCheckService.getLabCheckEmployeesByLabCheckId(labCheck.getId());
				WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

				labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
				LabCheckService.updateLabCheck(labCheck, loginUser, session);
				for (LabCheckEmployeeData labEmp : labCheckEmployeeList) {
					labEmp.setCheckStatus(LabCheckEmployeeCheckStatusEnum.NO_SAMPLE.getCode());
					LabCheckService.updateLabCheckEmployee(labEmp, loginUser, session);
				}

				closeWFInstance(instance, dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, session);

				Long originalId = instance.getRequesterId();
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				String checkReasonMsg = "";
				if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()))
					checkReasonMsg = "label_cases";
				else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.MEDICAL_COMMITTEE_CHECK.getCode()))
					checkReasonMsg = "label_medicalCommitteeCheck";
				else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PROMOTION.getCode()))
					checkReasonMsg = "label_promotion";
				else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.TREATMENT_PURPOSE.getCode()))
					checkReasonMsg = "label_treatmentPurpose";
				else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.RANDOM_MISSION_FROM_DIRECTORATE.getCode()))
					checkReasonMsg = "label_randomCheck";
				else if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.PERIODICAL_CHECK.getCode()))
					checkReasonMsg = "label_periodicCheck";
				else
					checkReasonMsg = "label_unexpected";

				String arabicDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "ar", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "ar") });
				String englishDetailsSummary = getParameterizedMessage("wfMsg_labcheckWorkflow", "en", new Object[] { labCheck.getOrderNumber(), labCheck.getOrderDateString(), getParameterizedMessage(checkReasonMsg, "en") });

				addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);

				Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
				if (regionId != null) {
					Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
					Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
					addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				} else {
					Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
					Long drugManagerOriginalId = DepartmentService.getDepartmentManager(unitId);
					Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
					addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, session);
				}
			}
			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void takeOldResultAndCloseInstance(WFTask dmTask, LabCheck labCheck, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
			LabCheckService.updateLabCheck(labCheck, loginUser, session);
			closeInstanceAndApproveTask(dmTask, session);

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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Close Instance
	 * 
	 * @param dmTask
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void closeInstanceAndApproveTask(WFTask dmTask, CustomSession... useSession) throws BusinessException {
		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			if (!isOpenedSession) {
				session.beginTransaction();
			}

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());
			closeWFInstance(instance, dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, session);

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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
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
			Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Edit Lab Check with workflow
	 * 
	 * @param labCheck
	 * @param labCheckBeforeEd
	 * @param labCheckEmployeeData
	 * @param labCheckEmployeeDataBeforeEd
	 * @param loginUser
	 * @param dmTask
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void editLabCheckAndInitWorkflow(LabCheck labCheck, LabCheck labCheckBeforeEd, LabCheckEmployeeData labCheckEmployeeData, LabCheckEmployeeData labCheckEmployeeDataBeforeEd, EmployeeData loginUser, WFTask dmTask, CustomSession... useSession) throws BusinessException {
		validateEditedLabCheckValues(labCheck);
		labCheck = insertLabCheckBeforeValues(labCheck);
		LabCheck labCheckAfterEd = compareEditedLabCheckValues(labCheck, labCheckBeforeEd);
		validateLabCheckEmpData(labCheckEmployeeData, labCheckEmployeeDataBeforeEd);
		labCheckEmployeeData = insertLabCheckEmployeeDataBeforeValues(labCheckEmployeeData);
		LabCheckEmployeeData labCheckEmployeeDataAfterEd = compareEditedLabCheckEmpDataValues(labCheckEmployeeData, labCheckEmployeeDataBeforeEd);

		if (labCheckAfterEd == null && labCheckEmployeeDataAfterEd == null) {
			labCheck = resetLabCheckBeforeEditValues(labCheck);
			labCheckEmployeeData = resetLabCheckEmpDataBeforeEditValues(labCheckEmployeeData);
			throw new BusinessException("error_noChanges");
		}

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			Date hijriCurDate = HijriDateService.getHijriSysDate();
			if (labCheckAfterEd != null) {
				labCheckAfterEd.setStatus(LabCheckStatusEnum.UNDER_APPROVAL.getCode());
				LabCheckService.updateLabCheck(labCheckAfterEd, loginUser, session);
			} else {
				labCheck.setStatus(LabCheckStatusEnum.UNDER_APPROVAL.getCode());
				LabCheckService.updateLabCheck(labCheck, loginUser, session);
			}

			if (labCheckEmployeeDataAfterEd != null) {
				labCheckEmployeeDataAfterEd.setEditorEmployeeId(loginUser.getEmpId());
				labCheckEmployeeDataAfterEd.setEditingDate(hijriCurDate);
				LabCheckService.updateLabCheckEmployee(labCheckEmployeeDataAfterEd, loginUser, session);
			} else {
				labCheckEmployeeData.setEditorEmployeeId(loginUser.getEmpId());
				labCheckEmployeeData.setEditingDate(hijriCurDate);
				LabCheckService.updateLabCheckEmployee(labCheckEmployeeData, loginUser, session);
			}

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflow", "ar", new Object[] { labCheckEmployeeDataBeforeEd.getEmployeeFullName() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflow", "en", new Object[] { labCheckEmployeeDataBeforeEd.getEmployeeFullName() });

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long originalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				if (dmTask == null) {
					addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeDataBeforeEd.getId().toString(), session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, labCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.REGION_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeDataBeforeEd.getId().toString(), session);
				}
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				Long originalId = DepartmentService.getDepartmentManager(unitId);
				Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());
				if (dmTask == null) {
					addWFTask(labCheck.getwFInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode(), "1", null, arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeDataBeforeEd.getId().toString(), session);
				} else {
					completeWFTask(dmTask, WFTaskActionsEnum.SEND.getCode(), new Date(), hijriCurDate, labCheck.getwFInstanceId(), assigneeId, originalId, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.GENERAL_ANTI_DRUG_MANAGER.getCode(), "1", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeDataBeforeEd.getId().toString(), session);
				}
			}
			if (!isOpenedSession) {
				session.beginTransaction();
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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	/**
	 * Edit Lab Check
	 * 
	 * @param labCheck
	 * @param labCheckEmployeeData
	 * @param labCheckEmployeeDataBeforeEd
	 * @param loginUser
	 * @param useSession
	 * @throws BusinessException
	 */
	public static void editLabCheck(LabCheck labCheck, LabCheckEmployeeData labCheckEmployeeData, LabCheckEmployeeData labCheckEmployeeDataBeforeEd, EmployeeData loginUser, CustomSession... useSession) throws BusinessException {
		validateEditedLabCheckValues(labCheck);
		validateLabCheckEmpData(labCheckEmployeeData, labCheckEmployeeDataBeforeEd);

		boolean isOpenedSession = isSessionOpened(useSession);
		CustomSession session = isOpenedSession ? useSession[0] : DataAccess.getSession();
		try {
			LabCheckService.updateLabCheck(labCheck, loginUser, session);
			LabCheckService.updateLabCheckEmployee(labCheckEmployeeData, loginUser, session);

			if (!isOpenedSession) {
				session.beginTransaction();
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
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			if (!isOpenedSession) {
				session.close();
			}
		}
	}

	private static void validateLabCheckEmpData(LabCheckEmployeeData labCheckEmployeeData, LabCheckEmployeeData labCheckEmployeeDataBeforeEd) throws BusinessException {
		if (labCheckEmployeeDataBeforeEd.getSampleNumber() != null && !(labCheckEmployeeDataBeforeEd.getSampleNumber().trim().isEmpty())) {
			if (labCheckEmployeeData.getSampleNumber() == null || labCheckEmployeeData.getSampleNumber().trim().isEmpty() || labCheckEmployeeData.getSampleDate() == null) {
				throw new BusinessException("error_mandatory");
			}
			List<Long> labCheckIds = LabCheckService.getEmployeeLabCheckBySampleData(HijriDateService.getHijriDateYear(labCheckEmployeeData.getSampleDate()) + "", labCheckEmployeeData.getSampleNumber());
			if (!labCheckIds.isEmpty() && !labCheckIds.contains(labCheckEmployeeData.getId())) {
				throw new BusinessException("error_sampleDuplicated");
			}
		}
		if (labCheckEmployeeDataBeforeEd.getNationalForceSampleNumber() != null && !(labCheckEmployeeDataBeforeEd.getNationalForceSampleNumber().trim().isEmpty())) {
			if (labCheckEmployeeData.getNationalForceSampleNumber() == null || labCheckEmployeeData.getNationalForceSampleNumber().trim().isEmpty() || labCheckEmployeeData.getNationalForceSampleSentDate() == null) {
				throw new BusinessException("error_mandatory");
			}
			if (labCheckEmployeeData.getNationalForceSampleSentDate() != null && labCheckEmployeeData.getNationalForceSampleSentDate().after(HijriDateService.getHijriSysDate())) {
				throw new BusinessException("error_comingDate");
			}
			if (labCheckEmployeeData.getNationalForceSampleSentDate() != null && labCheckEmployeeData.getNationalForceSampleSentDate().before(labCheckEmployeeData.getSampleDate())) {
				throw new BusinessException("error_nationalForcesSampleDateMustExceedSampleInitialDate");
			}
		}
	}

	private static void validateEditedLabCheckValues(LabCheck labCheck) throws BusinessException {
		labCheck.setOrderNumber(labCheck.getOrderNumber().trim());
		
		if (labCheck.getOrderDate() == null || labCheck.getCheckReason() == null || labCheck.getOrderSourceDomainId() == null || labCheck.getOrderNumber() == null || labCheck.getOrderNumber().isEmpty()) {
			throw new BusinessException("error_mandatory");
		}
		
		if(!checkRegexMatch(labCheck.getOrderNumber(), ORDERNUMBER_ARABIC_REGEX)){
			   throw new BusinessException("error_ordernumberMismatchRegex");
		  }
		
		if (labCheck.getCheckReason().equals(LabCheckReasonsEnum.CASES.getCode()) && (labCheck.getCaseDate() == null || labCheck.getCaseNumber() == null || labCheck.getCaseNumber().trim().isEmpty())) {
			throw new BusinessException("error_mandatory");
		}
		if (labCheck.getOrderDate() != null && labCheck.getOrderDate().after(HijriDateService.getHijriSysDate())) {
			throw new BusinessException("error_comingDate");
		}

		List<LabCheck> labCheckDuplicateList = LabCheckService.getLabCheck(labCheck.getOrderNumber(), labCheck.getRegionId());
		if (labCheckDuplicateList.size() > 1 || (labCheckDuplicateList.size() == 1 && !labCheckDuplicateList.get(0).getId().equals(labCheck.getId())))
			throw new BusinessException("error_labCheckExistsWithSameNumber");
	}

	private static LabCheck compareEditedLabCheckValues(LabCheck labCheck, LabCheck labCheckTemp) {
		boolean isEditedFlag = false;
		if (!(labCheck.getOrderNumber() == null && labCheckTemp.getOrderNumber() == null)) {
			if ((labCheck.getOrderNumber() == null && labCheckTemp.getOrderNumber() != null) || (labCheck.getOrderNumber() != null && labCheckTemp.getOrderNumber() == null) || !(labCheck.getOrderNumber().equals(labCheckTemp.getOrderNumber()))) {
				isEditedFlag = true;
				labCheck.setOrderNumberBeforeEdit(labCheckTemp.getOrderNumber());
			}
		}
		if (!(labCheck.getOrderDate() == null && labCheckTemp.getOrderDate() == null)) {
			if ((labCheck.getOrderDate() == null && labCheckTemp.getOrderDate() != null) || (labCheck.getOrderDate() != null && labCheckTemp.getOrderDate() == null) || !(labCheck.getOrderDate().equals(labCheckTemp.getOrderDate()))) {
				isEditedFlag = true;
				labCheck.setOrderDateBeforeEdit(labCheckTemp.getOrderDate());
			}
		}
		if (!(labCheck.getOrderSourceDomainId() == null && labCheckTemp.getOrderSourceDomainId() == null)) {
			if ((labCheck.getOrderSourceDomainId() == null && labCheckTemp.getOrderSourceDomainId() != null) || (labCheck.getOrderSourceDomainId() != null && labCheckTemp.getOrderSourceDomainId() == null) || !(labCheck.getOrderSourceDomainId().equals(labCheckTemp.getOrderSourceDomainId()))) {
				isEditedFlag = true;
				labCheck.setOrderSourceDomainIdBeforeEdit(labCheckTemp.getOrderSourceDomainId());
			}
		}
		if (!(labCheck.getOrderSourceDomainDescription() == null && labCheckTemp.getOrderSourceDomainDescription() == null)) {
			if ((labCheck.getOrderSourceDomainDescription() == null && labCheckTemp.getOrderSourceDomainDescription() != null) || (labCheck.getOrderSourceDomainDescription() != null && labCheckTemp.getOrderSourceDomainDescription() == null) || !(labCheck.getOrderSourceDomainDescription().equals(labCheckTemp.getOrderSourceDomainDescription()))) {
				isEditedFlag = true;
				labCheck.setOrderSourceDomainDescriptionBeforeEdit(labCheckTemp.getOrderSourceDomainDescription());
			}
		}
		if (!(labCheck.getCheckReason() == null && labCheckTemp.getCheckReason() == null)) {
			if ((labCheck.getCheckReason() == null && labCheckTemp.getCheckReason() != null) || (labCheck.getCheckReason() != null && labCheckTemp.getCheckReason() == null) || !(labCheck.getCheckReason().equals(labCheckTemp.getCheckReason()))) {
				isEditedFlag = true;
				labCheck.setCheckReasonBeforeEdit(labCheckTemp.getCheckReason());
			}
		}
		if (!(labCheck.getCaseNumber() == null && labCheckTemp.getCaseNumber() == null)) {
			if ((labCheck.getCaseNumber() == null && labCheckTemp.getCaseNumber() != null) || (labCheck.getCaseNumber() != null && labCheckTemp.getCaseNumber() == null) || !(labCheck.getCaseNumber().equals(labCheckTemp.getCaseNumber()))) {
				isEditedFlag = true;
				labCheck.setCaseNumberBeforeEdit(labCheckTemp.getCaseNumber());
			}
		}
		if (!(labCheck.getCaseDate() == null && labCheckTemp.getCaseDate() == null)) {
			if ((labCheck.getCaseDate() == null && labCheckTemp.getCaseDate() != null) || (labCheck.getCaseDate() != null && labCheckTemp.getCaseDate() == null) || !(labCheck.getCaseDate().equals(labCheckTemp.getCaseDate()))) {
				isEditedFlag = true;
				labCheck.setCaseDateBeforeEdit(labCheckTemp.getCaseDate());
			}
		}
		if (!((labCheck.getRemarks() == null || labCheck.getRemarks().equals("")) && (labCheckTemp.getRemarks() == null || labCheckTemp.getRemarks().equals("")))) {
			if ((labCheck.getRemarks() == null && labCheckTemp.getRemarks() != null) || (labCheck.getRemarks() != null && labCheckTemp.getRemarks() == null) || !(labCheck.getRemarks().equals(labCheckTemp.getRemarks()))) {
				isEditedFlag = true;
				labCheck.setRemarksBeforeEdit(labCheckTemp.getRemarks());
			}
		}

		if (!isEditedFlag)
			return null;
		return labCheck;
	}

	private static LabCheckEmployeeData compareEditedLabCheckEmpDataValues(LabCheckEmployeeData labCheckEmployeeData, LabCheckEmployeeData labCheckEmployeeDataTemp) {
		boolean isEditedFlag = false;
		if (!(labCheckEmployeeData.getSampleNumber() == null && labCheckEmployeeDataTemp.getSampleNumber() == null)) {
			if ((labCheckEmployeeData.getSampleNumber() == null && labCheckEmployeeDataTemp.getSampleNumber() != null) || (labCheckEmployeeData.getSampleNumber() != null && labCheckEmployeeDataTemp.getOrderNumber() == null) || !(labCheckEmployeeData.getSampleNumber().equals(labCheckEmployeeDataTemp.getSampleNumber()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setSampleNumberBeforeEdit(labCheckEmployeeDataTemp.getSampleNumber());
			}
		}
		if (!(labCheckEmployeeData.getSampleDate() == null && labCheckEmployeeDataTemp.getSampleDate() == null)) {
			if ((labCheckEmployeeData.getSampleDate() == null && labCheckEmployeeDataTemp.getSampleDate() != null) || (labCheckEmployeeData.getSampleDate() != null && labCheckEmployeeDataTemp.getSampleDate() == null) || !(labCheckEmployeeData.getSampleDate().equals(labCheckEmployeeDataTemp.getSampleDate()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setSampleDateBeforeEdit(labCheckEmployeeDataTemp.getSampleDate());
			}
		}
		if (!(labCheckEmployeeData.getCheckStatus() == null && labCheckEmployeeDataTemp.getCheckStatus() == null)) {
			if ((labCheckEmployeeData.getCheckStatus() == null && labCheckEmployeeDataTemp.getCheckStatus() != null) || (labCheckEmployeeData.getCheckStatus() != null && labCheckEmployeeDataTemp.getCheckStatus() == null) || !(labCheckEmployeeData.getCheckStatus().equals(labCheckEmployeeDataTemp.getCheckStatus()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setCheckStatusBeforeEdit(labCheckEmployeeDataTemp.getCheckStatus());
			}
		}
		if (!(labCheckEmployeeData.getDomainMaterialTypeId() == null && labCheckEmployeeDataTemp.getDomainMaterialTypeId() == null)) {
			if ((labCheckEmployeeData.getDomainMaterialTypeId() == null && labCheckEmployeeDataTemp.getDomainMaterialTypeId() != null) || (labCheckEmployeeData.getDomainMaterialTypeId() != null && labCheckEmployeeDataTemp.getDomainMaterialTypeId() == null) || !(labCheckEmployeeData.getDomainMaterialTypeId().equals(labCheckEmployeeDataTemp.getDomainMaterialTypeId()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setDomainMaterialTypeIdBeforeEdit(labCheckEmployeeDataTemp.getDomainMaterialTypeId());
			}
		}
		if (!(labCheckEmployeeData.getDomainMaterialTypeDescripttion() == null && labCheckEmployeeDataTemp.getDomainMaterialTypeDescripttion() == null)) {
			if ((labCheckEmployeeData.getDomainMaterialTypeDescripttion() == null && labCheckEmployeeDataTemp.getDomainMaterialTypeDescripttion() != null) || (labCheckEmployeeData.getDomainMaterialTypeDescripttion() != null && labCheckEmployeeDataTemp.getDomainMaterialTypeDescripttion() == null) || !(labCheckEmployeeData.getDomainMaterialTypeDescripttion().equals(labCheckEmployeeDataTemp.getDomainMaterialTypeDescripttion()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setDomainMaterialTypeDescripttionBeforeEdit(labCheckEmployeeDataTemp.getDomainMaterialTypeDescripttion());
			}
		}
		if (!(labCheckEmployeeData.getNationalForceSampleNumber() == null && labCheckEmployeeDataTemp.getNationalForceSampleNumber() == null)) {
			if ((labCheckEmployeeData.getNationalForceSampleNumber() == null && labCheckEmployeeDataTemp.getNationalForceSampleNumber() != null) || (labCheckEmployeeData.getNationalForceSampleNumber() != null && labCheckEmployeeDataTemp.getNationalForceSampleNumber() == null) || !(labCheckEmployeeData.getNationalForceSampleNumber().equals(labCheckEmployeeDataTemp.getNationalForceSampleNumber()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setNationalForceSampleNumberBeforeEdit(labCheckEmployeeDataTemp.getNationalForceSampleNumber());
			}
		}
		if (!(labCheckEmployeeData.getNationalForceSampleSentDate() == null && labCheckEmployeeDataTemp.getNationalForceSampleSentDate() == null)) {
			if ((labCheckEmployeeData.getNationalForceSampleSentDate() == null && labCheckEmployeeDataTemp.getNationalForceSampleSentDate() != null) || (labCheckEmployeeData.getNationalForceSampleSentDate() != null && labCheckEmployeeDataTemp.getNationalForceSampleSentDate() == null) || !(labCheckEmployeeData.getNationalForceSampleSentDate().equals(labCheckEmployeeDataTemp.getNationalForceSampleSentDate()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setNationalForceSampleSentDateBeforeEdit(labCheckEmployeeDataTemp.getNationalForceSampleSentDate());
			}
		}
		if (!(labCheckEmployeeData.getCurement() == null && labCheckEmployeeDataTemp.getCurement() == null)) {
			if ((labCheckEmployeeData.getCurement() == null && labCheckEmployeeDataTemp.getCurement() != null) || (labCheckEmployeeData.getCurement() != null && labCheckEmployeeDataTemp.getCurement() == null) || !(labCheckEmployeeData.getCurement().equals(labCheckEmployeeDataTemp.getCurement()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setCurementBeforeEdit(labCheckEmployeeDataTemp.getCurement());
			}
		}
		if (!(labCheckEmployeeData.getDomainNationalForceMaterialTypeId() == null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeId() == null)) {
			if ((labCheckEmployeeData.getDomainNationalForceMaterialTypeId() == null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeId() != null) || (labCheckEmployeeData.getDomainNationalForceMaterialTypeId() != null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeId() == null) || !(labCheckEmployeeData.getDomainNationalForceMaterialTypeId().equals(labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeId()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setDomainNationalForceMaterialTypeIdBeforeEdit(labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeId());
			}
		}
		if (!(labCheckEmployeeData.getDomainNationalForceMaterialTypeDesc() == null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeDesc() == null)) {
			if ((labCheckEmployeeData.getDomainNationalForceMaterialTypeDesc() == null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeDesc() != null) || (labCheckEmployeeData.getDomainNationalForceMaterialTypeDesc() != null && labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeDesc() == null) || !(labCheckEmployeeData.getDomainNationalForceMaterialTypeDesc().equals(labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeDesc()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setDomainNationalForceMaterialTypeDescBeforeEdit(labCheckEmployeeDataTemp.getDomainNationalForceMaterialTypeDesc());
			}
		}
		if (!(labCheckEmployeeData.getDomainCureHospitalId() == null && labCheckEmployeeDataTemp.getDomainCureHospitalId() == null)) {
			if ((labCheckEmployeeData.getDomainCureHospitalId() == null && labCheckEmployeeDataTemp.getDomainCureHospitalId() != null) || (labCheckEmployeeData.getDomainCureHospitalId() != null && labCheckEmployeeDataTemp.getDomainCureHospitalId() == null) || !(labCheckEmployeeData.getDomainCureHospitalId().equals(labCheckEmployeeDataTemp.getDomainCureHospitalId()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setDomainCureHospitalIdBeforeEdit(labCheckEmployeeDataTemp.getDomainCureHospitalId());
			}
		}
		if (!(labCheckEmployeeData.getPeriodicRetestDate() == null && labCheckEmployeeDataTemp.getPeriodicRetestDate() == null)) {
			if ((labCheckEmployeeData.getPeriodicRetestDate() == null && labCheckEmployeeDataTemp.getPeriodicRetestDate() != null) || (labCheckEmployeeData.getPeriodicRetestDate() != null && labCheckEmployeeDataTemp.getPeriodicRetestDate() == null) || !(labCheckEmployeeData.getPeriodicRetestDate().equals(labCheckEmployeeDataTemp.getPeriodicRetestDate()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setPeriodicRetestDateBeforeEdit(labCheckEmployeeDataTemp.getPeriodicRetestDate());
			}
		}
		if (!((labCheckEmployeeData.getEmployeeRemarks() == null || labCheckEmployeeData.getEmployeeRemarks().equals("")) && (labCheckEmployeeDataTemp.getEmployeeRemarks() == null || labCheckEmployeeDataTemp.getEmployeeRemarks().equals("")))) {
			if ((labCheckEmployeeData.getEmployeeRemarks() == null && labCheckEmployeeDataTemp.getEmployeeRemarks() != null) || (labCheckEmployeeData.getEmployeeRemarks() != null && labCheckEmployeeDataTemp.getEmployeeRemarks() == null) || !(labCheckEmployeeData.getEmployeeRemarks().equals(labCheckEmployeeDataTemp.getEmployeeRemarks()))) {
				isEditedFlag = true;
				labCheckEmployeeData.setEmployeeRemarksBeforeEdit(labCheckEmployeeDataTemp.getEmployeeRemarks());
			}
		}
		
		if (!isEditedFlag)
			return null;
		return labCheckEmployeeData;
	}

	/**
	 * Approve action for "Moder aledara alamma / region dorector" send notification to "zabt edarat al est5barat"
	 * 
	 * @param dmTask
	 * @param labCheck
	 * @param labCheckEmployeeData
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doApproveDMLabCheckEdit(WFTask dmTask, LabCheck labCheck, LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());
			labCheck = resetLabCheckBeforeEditValues(labCheck);
			labCheckEmployeeData = resetLabCheckEmpDataBeforeEditValues(labCheckEmployeeData);

			labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
			LabCheckService.updateLabCheck(labCheck, loginUser, session);
			LabCheckService.updateLabCheckEmployee(labCheckEmployeeData, loginUser, session);

			closeWFInstance(instance, dmTask, WFTaskActionsEnum.APPROVE.getCode(), new Date(), hijriCurDate, session);

			Long originalId = instance.getRequesterId();
			Long assigneeId = getDelegate(originalId, WFProcessesEnum.LAB_CHECK.getCode());

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflow", "ar", new Object[] { labCheckEmployeeData.getEmployeeFullName() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflow", "en", new Object[] { labCheckEmployeeData.getEmployeeFullName() });

			addWFTask(instance.getInstanceId(), assigneeId, originalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", "", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeData.getId().toString(), session);

			Long regionId = DepartmentService.isRegionDepartment(loginUser.getActualDepartmentId());
			if (regionId != null) {
				Long drugManagerOriginalId = DepartmentService.getDepartmentManager(WFPositionService.getRegionAntiDrugDepartmentId(regionId));
				Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", "", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeData.getId().toString(), session);
			} else {
				Long unitId = WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_ANTI_DRUG_DEPARTMENT.getCode()).getUnitId();
				Long drugManagerOriginalId = DepartmentService.getDepartmentManager(unitId);
				Long drugManagerAssigneeId = getDelegate(drugManagerOriginalId, WFProcessesEnum.LAB_CHECK.getCode());
				addWFTask(instance.getInstanceId(), drugManagerAssigneeId, drugManagerOriginalId, new Date(), hijriCurDate, WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", "", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeData.getId().toString(), session);
			}

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	/**
	 * doReject
	 * 
	 * @param dmTask
	 * @param labCheckEmployeeData
	 * @param loginUser
	 * @param attachments
	 * @throws BusinessException
	 */
	public static void doRejectLabCheckEdit(WFTask dmTask, LabCheck labCheck, LabCheckEmployeeData labCheckEmployeeData, EmployeeData loginUser, String attachments) throws BusinessException {
		CustomSession session = DataAccess.getSession();

		try {
			session.beginTransaction();

			labCheck = insertLabCheckValues(labCheck);
			labCheckEmployeeData = insertLabCheckEmpDataValues(labCheckEmployeeData);

			Date hijriCurDate = HijriDateService.getHijriSysDate();
			WFInstance instance = getWFInstanceById(dmTask.getInstanceId());

			String arabicDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflowReject", "ar", new Object[] { labCheckEmployeeData.getEmployeeFullName() });
			String englishDetailsSummary = getParameterizedMessage("wfMsg_labCheckEditWorkflowReject", "en", new Object[] { labCheckEmployeeData.getEmployeeFullName() });

			LabCheckService.updateLabCheck(labCheck, loginUser, session);
			LabCheckService.updateLabCheckEmployee(labCheckEmployeeData, loginUser, session);

			completeWFTask(dmTask, WFTaskActionsEnum.REJECT.getCode(), new Date(), hijriCurDate, instance.getInstanceId(), instance.getRequesterId(), instance.getRequesterId(), WFTaskUrlEnum.LAB_CHECK_EDIT.getCode(), WFTaskRolesEnum.NOTIFICATION.getCode(), "1", "", arabicDetailsSummary, englishDetailsSummary, labCheckEmployeeData.getId().toString(), session);

			session.commitTransaction();
		} catch (Exception e) {
			session.rollbackTransaction();

			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Log4j.traceErrorException(LabCheckWorkFlow.class, e, "LabCheckWorkFlow");
				throw new BusinessException("error_general");
			}
		} finally {
			session.close();
		}
	}

	private static LabCheck insertLabCheckValues(LabCheck labCheck) {

		labCheck.setOrderNumber(labCheck.getOrderNumberBeforeEdit());
		labCheck.setOrderDate(labCheck.getOrderDateBeforeEdit());
		labCheck.setOrderSourceDomainId(labCheck.getOrderSourceDomainIdBeforeEdit());
		labCheck.setOrderSourceDomainDescription(labCheck.getOrderSourceDomainDescriptionBeforeEdit());
		labCheck.setCheckReason(labCheck.getCheckReasonBeforeEdit());
		labCheck.setCaseNumber(labCheck.getCaseNumberBeforeEdit());
		labCheck.setCaseDate(labCheck.getCaseDateBeforeEdit());
		labCheck.setRemarks(labCheck.getRemarksBeforeEdit());
		labCheck.setStatus(LabCheckStatusEnum.APPROVED.getCode());
		labCheck = resetLabCheckBeforeEditValues(labCheck);
		return labCheck;
	}

	private static LabCheckEmployeeData insertLabCheckEmpDataValues(LabCheckEmployeeData labCheckEmployeeData) {
		labCheckEmployeeData.setSampleNumber(labCheckEmployeeData.getSampleNumberBeforeEdit());
		labCheckEmployeeData.setSampleDate(labCheckEmployeeData.getSampleDateBeforeEdit());
		labCheckEmployeeData.setCheckStatus(labCheckEmployeeData.getCheckStatusBeforeEdit());
		labCheckEmployeeData.setDomainMaterialTypeId(labCheckEmployeeData.getDomainMaterialTypeIdBeforeEdit());
		labCheckEmployeeData.setNationalForceSampleNumber(labCheckEmployeeData.getNationalForceSampleNumberBeforeEdit());
		labCheckEmployeeData.setNationalForceSampleSentDate(labCheckEmployeeData.getNationalForceSampleSentDateBeforeEdit());
		labCheckEmployeeData.setDomainNationalForceMaterialTypeId(labCheckEmployeeData.getDomainNationalForceMaterialTypeIdBeforeEdit());
		labCheckEmployeeData.setDomainCureHospitalId(labCheckEmployeeData.getDomainCureHospitalIdBeforeEdit());
		labCheckEmployeeData.setPeriodicRetestDate(labCheckEmployeeData.getPeriodicRetestDateBeforeEdit());
		labCheckEmployeeData.setCurement(labCheckEmployeeData.getCurementBeforeEdit());
		labCheckEmployeeData.setEmployeeRemarks(labCheckEmployeeData.getEmployeeRemarksBeforeEdit());
		labCheckEmployeeData = resetLabCheckEmpDataBeforeEditValues(labCheckEmployeeData);
		return labCheckEmployeeData;
	}

	private static LabCheck resetLabCheckBeforeEditValues(LabCheck labCheck) {
		labCheck.setOrderNumberBeforeEdit(null);
		labCheck.setOrderDateBeforeEdit(null);
		labCheck.setOrderSourceDomainIdBeforeEdit(null);
		labCheck.setOrderSourceDomainDescriptionBeforeEdit(null);
		labCheck.setCheckReasonBeforeEdit(null);
		labCheck.setCaseNumberBeforeEdit(null);
		labCheck.setCaseDateBeforeEdit(null);
		labCheck.setRemarksBeforeEdit(null);
		return labCheck;
	}

	private static LabCheckEmployeeData resetLabCheckEmpDataBeforeEditValues(LabCheckEmployeeData labCheckEmployeeData) {
		labCheckEmployeeData.setSampleNumberBeforeEdit(null);
		labCheckEmployeeData.setSampleDateBeforeEdit(null);
		labCheckEmployeeData.setCheckStatusBeforeEdit(null);
		labCheckEmployeeData.setDomainMaterialTypeIdBeforeEdit(null);
		labCheckEmployeeData.setNationalForceSampleNumberBeforeEdit(null);
		labCheckEmployeeData.setNationalForceSampleSentDateBeforeEdit(null);
		labCheckEmployeeData.setDomainNationalForceMaterialTypeIdBeforeEdit(null);
		labCheckEmployeeData.setDomainCureHospitalIdBeforeEdit(null);
		labCheckEmployeeData.setPeriodicRetestDateBeforeEdit(null);
		labCheckEmployeeData.setCurementBeforeEdit(null);
		labCheckEmployeeData.setEmployeeRemarksBeforeEdit(null);
		return labCheckEmployeeData;
	}

	private static LabCheckEmployeeData insertLabCheckEmployeeDataBeforeValues(LabCheckEmployeeData labCheckEmployeeData) {
		labCheckEmployeeData.setSampleNumberBeforeEdit(labCheckEmployeeData.getSampleNumber());
		labCheckEmployeeData.setSampleDateBeforeEdit(labCheckEmployeeData.getSampleDate());
		labCheckEmployeeData.setCheckStatusBeforeEdit(labCheckEmployeeData.getCheckStatus());
		labCheckEmployeeData.setDomainMaterialTypeIdBeforeEdit(labCheckEmployeeData.getDomainMaterialTypeId());
		labCheckEmployeeData.setDomainMaterialTypeDescripttionBeforeEdit(labCheckEmployeeData.getDomainMaterialTypeDescripttionBeforeEdit());
		labCheckEmployeeData.setNationalForceSampleNumberBeforeEdit(labCheckEmployeeData.getNationalForceSampleNumber());
		labCheckEmployeeData.setNationalForceSampleSentDateBeforeEdit(labCheckEmployeeData.getNationalForceSampleSentDate());
		labCheckEmployeeData.setDomainNationalForceMaterialTypeIdBeforeEdit(labCheckEmployeeData.getDomainNationalForceMaterialTypeId());
		labCheckEmployeeData.setDomainCureHospitalIdBeforeEdit(labCheckEmployeeData.getDomainCureHospitalId());
		labCheckEmployeeData.setPeriodicRetestDateBeforeEdit(labCheckEmployeeData.getPeriodicRetestDate());
		labCheckEmployeeData.setCurementBeforeEdit(labCheckEmployeeData.getCurement());
		labCheckEmployeeData.setEmployeeRemarksBeforeEdit(labCheckEmployeeData.getEmployeeRemarks());
		return labCheckEmployeeData;
	}

	private static LabCheck insertLabCheckBeforeValues(LabCheck labCheck) {
		labCheck.setOrderNumberBeforeEdit(labCheck.getOrderNumber());
		labCheck.setOrderDateBeforeEdit(labCheck.getOrderDate());
		labCheck.setOrderSourceDomainIdBeforeEdit(labCheck.getOrderSourceDomainId());
		labCheck.setOrderSourceDomainDescriptionBeforeEdit(labCheck.getOrderSourceDomainDescription());
		labCheck.setCheckReasonBeforeEdit(labCheck.getCheckReason());
		labCheck.setCaseNumberBeforeEdit(labCheck.getCaseNumber());
		labCheck.setCaseDateBeforeEdit(labCheck.getCaseDate());
		labCheck.setRemarksBeforeEdit(labCheck.getRemarks());
		return labCheck;
	}

}