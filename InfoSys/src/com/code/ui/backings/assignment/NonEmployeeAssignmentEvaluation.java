package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import com.code.dal.orm.assignment.AssignmentAgentClass;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.assignment.AssignmentEvaluation;
import com.code.dal.orm.assignment.AssignmentEvaluationPointData;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.setup.BankBranchData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.AssignmentEvaluationActionEnum;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentNonEmployeeService;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.assignment.NonEmployeesAssignementEvaluationWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "nonEmployeeAssignmentEvaluation")
@ViewScoped
public class NonEmployeeAssignmentEvaluation extends WFBaseBacking implements Serializable {
	private int rowsCount = 10;
	private int pageMode;
	private String screenTitle;
	private List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList;
	private AssignmentDetailData assignmentDetailData;
	private List<String> continueReasonsList;
	private List<String> endReasonsList;
	private List<String> continueEndSelectedReasonsList;
	private AssignmentEvaluation assignmentEvaluation;
	private List<InfoData> employeeRelatedInfoList;
	private List<AssignmentDetailData> previousAssignmentsList;
	private List<AssignmentEvaluation> previousEvaluationsList;
	private List<AssignmentAgentClass> assignmentAgentClassesList;
	private List<BankBranchData> bankBranchesDataList;
	private boolean ended;

	/**
	 * Default Constructor and Initializer
	 */
	public NonEmployeeAssignmentEvaluation() {
		super();
		super.init();
		this.init();
		try {
			assignmentDetailData = null;
			assignmentAgentClassesList = AssignmentService.getAgentClasses();
			bankBranchesDataList = CommonService.getBankBranches();
			// Set Reasons List
			fillReasons();
			if (getRequest().getAttribute("mode") != null) {
				assignmentDetailData = (AssignmentDetailData) getRequest().getAttribute("mode");
			}

			// Check Page Roles
			if (currentTask == null) {
				assignmentEvaluation.setAssignmentDetailId(assignmentDetailData.getId());
				assignmentEvaluation.setEvaluationAction(AssignmentEvaluationActionEnum.CONTINUE.getCode());
				List<AssignmentEvaluation> dummyEvalList = AssignmentService.getAssignmentEvaluation(assignmentDetailData.getId(), null, null);
				if (!dummyEvalList.isEmpty()) {
					assignmentEvaluation = AssignmentService.getAssignmentEvaluation(assignmentDetailData.getId(), null, null).get(0);
				}
				if (assignmentEvaluation.getId() != null) {
					assignmentEvaluation.setEvaluationStartDate(assignmentEvaluation.getEvaluationDate());
				} else {
					assignmentEvaluation.setEvaluationStartDate(assignmentDetailData.getStartDate());
				}
				assignmentEvaluation.setEvaluationDate(HijriDateService.getHijriSysDate());
				assignmentEvaluationPointDataList = AssignmentService.getLatestAssignmentEvaluationPointData(assignmentEvaluation.getId(), assignmentEvaluation.getAssignmentDetailId());
				assignmentEvaluation.setId(null);
				if (assignmentEvaluationPointDataList.isEmpty()) {
					assignmentEvaluationPointDataList = AssignmentService.getAssignmentEvaluationPoints();
				} else {
					if (assignmentEvaluation.getReasons() != null && !assignmentEvaluation.getReasons().isEmpty()) {
						String[] reasons = assignmentEvaluation.getReasons().split(",");
						assignmentEvaluation.setAssignmentEvalReasonsList(Arrays.asList(reasons));
						continueEndSelectedReasonsList.addAll(assignmentEvaluation.getAssignmentEvalReasonsList());
					}
				}
				if (assignmentEvaluationPointDataList.isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_DBError"));
				}
				pageMode = 1;
				if (assignmentDetailData.getApprovedEndDate().compareTo(HijriDateService.getHijriSysDate()) < 0) {
					ended = true;
				}
			} else {
				pageMode = 2;
				assignmentEvaluation = AssignmentService.getAssignmentEvaluation(null, instance.getInstanceId(), null).get(0);
				if (assignmentEvaluation != null) {
					assignmentEvaluationPointDataList = AssignmentService.getAssignmentEvaluationPointData(assignmentEvaluation.getId());
				} else {
					this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
					return;
				}
				assignmentDetailData = AssignmentService.getAssignmentDetailsDataById(assignmentEvaluation.getAssignmentDetailId());
				if (assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.CONTINUE.getCode()) || assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.EXTEND.getCode())) {
					screenTitle = getParameterizedMessage("label_assignmentNonEmpContinueApprove");
				} else if (assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.END.getCode())) {
					screenTitle = getParameterizedMessage("label_assignmentNonEmpEndApprove");
				}
				if (assignmentEvaluation.getReasons() != null && !assignmentEvaluation.getReasons().isEmpty()) {
					String[] reasons = assignmentEvaluation.getReasons().split(",");
					assignmentEvaluation.setAssignmentEvalReasonsList(Arrays.asList(reasons));
					continueEndSelectedReasonsList.addAll(assignmentEvaluation.getAssignmentEvalReasonsList());
				}
				if (assignmentDetailData.getApprovedEndDate().compareTo(HijriDateService.getHijriSysDate()) < 0 && !role.equals(WFTaskRolesEnum.HISTORY.getCode()) && !role.equals(WFTaskRolesEnum.NOTIFICATION.getCode())) {
					ended = true;
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(e.getMessage());
		} catch (Exception e) {
			Log4j.traceErrorException(NonEmployeeAssignmentEvaluation.class, e, "NonEmployeeAssignmentEvaluation");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Initialize Variables
	 */
	public void init() {
		screenTitle = getParameterizedMessage("title_assignmentEvaluation");
		assignmentEvaluationPointDataList = new ArrayList<AssignmentEvaluationPointData>();
		continueReasonsList = new ArrayList<String>();
		endReasonsList = new ArrayList<String>();
		continueEndSelectedReasonsList = new ArrayList<String>();
		assignmentEvaluation = new AssignmentEvaluation();
		assignmentEvaluation.setEvaluationAction(AssignmentEvaluationActionEnum.CONTINUE.getCode());
		assignmentDetailData = new AssignmentDetailData();
		previousAssignmentsList = new ArrayList<AssignmentDetailData>();
		previousEvaluationsList = new ArrayList<AssignmentEvaluation>();
		assignmentAgentClassesList = new ArrayList<AssignmentAgentClass>();
		bankBranchesDataList = new ArrayList<BankBranchData>();
	}

	/**
	 * Fill reasons list
	 */
	public void fillReasons() {
		try {
			List<SetupClass> continueSetupClass = SetupService.getClasses(ClassesEnum.ASSIGNMENT_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			List<SetupClass> endSetupClass = SetupService.getClasses(ClassesEnum.ASSIGNMENT_END_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!continueSetupClass.isEmpty() || !endSetupClass.isEmpty()) {
				List<SetupDomain> continueReasonsDomainList = SetupService.getDomains(continueSetupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				List<SetupDomain> endReasonsDomainList = SetupService.getDomains(endSetupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				if (continueReasonsDomainList.isEmpty() || endReasonsDomainList.isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_DBError"));
					return;
				}
				for (SetupDomain domain : continueReasonsDomainList) {
					continueReasonsList.add(domain.getDescription());
				}

				for (SetupDomain domain : endReasonsDomainList) {
					endReasonsList.add(domain.getDescription());
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			Log4j.traceErrorException(NonEmployeeAssignmentEvaluation.class, e, "NonEmployeeAssignmentEvaluation");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Change Monthly Reward Based on agent class
	 * 
	 * @param event
	 */
	public void changeMonthlyReward(AjaxBehaviorEvent event) {
		for (int i = 0; i < assignmentAgentClassesList.size(); i++) {
			if (assignmentAgentClassesList.get(i).getCode().equals(currentTask.getFlexField1())) {
				currentTask.setFlexField2(assignmentAgentClassesList.get(i).getValue().toString());
				return;
			}
		}
	}

	/**
	 * Get Info Details
	 */
	public void getInfoDetails() {
		try {
			employeeRelatedInfoList = InfoService.getInfoDataByAssignmentId(assignmentDetailData.getId(), null, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Get Previous Evaluations Details
	 */
	public void getEvalDetails() {
		try {
			previousEvaluationsList = AssignmentService.getAssignmentEvaluationWfInstances(assignmentDetailData.getId(), WFInstanceStatusEnum.COMPLETED.getCode(), assignmentEvaluation.getEvaluationDate(), null);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Get Previous Assignment Details
	 */
	public void getAssignDetails() {
		try {
			previousAssignmentsList = AssignmentService.getAssignmentPrevDetailData(null, assignmentDetailData.getIdentity(), assignmentDetailData.getStartDate());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save and Start work flow
	 * 
	 * @return
	 */
	public String saveAndSend() {
		try {
			List<AssignmentEvaluation> assignmentEvalsList = AssignmentService.getAssignmentEvaluationWfInstances(assignmentEvaluation.getAssignmentDetailId(), WFInstanceStatusEnum.RUNING.getCode(), null, null);
			for (AssignmentEvaluation assignmentEvals : assignmentEvalsList) {
				if (assignmentEvals.getStatus().equals(AssignmentStatusEnum.UNDER_APPROVAL.getCode())) {
					this.setServerSideErrorMessages(getParameterizedMessage("label_assignmentEvaluationUnderApproval"));
					return null;
				}
			}
			assignmentEvaluation.setAssignmentEvalReasonsList(continueEndSelectedReasonsList);
			assignmentEvaluation.setId(null);
			if (ended) {
				assignmentEvaluation.setStatus(AssignmentStatusEnum.APPROVED.getCode());
				AssignmentNonEmployeeService.updateEvaluation(assignmentEvaluation, assignmentEvaluationPointDataList, loginEmpData);
			} else {
				assignmentEvaluation.setApprovedExtendPeriod(assignmentEvaluation.getExtendPeriod());
				assignmentEvaluation.setStatus(AssignmentStatusEnum.UNDER_APPROVAL.getCode());
				AssignmentNonEmployeeService.addAssignemntEvlauation(assignmentEvaluation, assignmentEvaluationPointDataList, loginEmpData);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(NonEmployeeAssignmentEvaluation.class, e, "NonEmployeeAssignmentEvaluation");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Approve cycle
	 * 
	 * @return
	 */
	public String doApprove() {
		try {
			if (role.equals(WFTaskRolesEnum.GENERAL_INTELLIGENCE_MANAGER.getCode())) {
				NonEmployeesAssignementEvaluationWorkFlow.approveHQManager(currentTask, assignmentEvaluation, null);
			} else if (role.equals(WFTaskRolesEnum.GENERAL_DIRECTOR.getCode())) {
				if (assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.EXTEND.getCode())) {
					assignmentDetailData.setApprovedEndDate(HijriDateService.addSubHijriMonthsDays(assignmentDetailData.getEndDate(), assignmentEvaluation.getApprovedExtendPeriod(), 0));
				} else if (assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.END.getCode())) {
					assignmentDetailData.setApprovedEndDate(HijriDateService.getHijriSysDate());
				}
				assignmentDetailData.setAgentClass(currentTask.getFlexField1());
				assignmentDetailData.setMonthlyReward(Double.valueOf(currentTask.getFlexField2()));
				assignmentEvaluation.setStatus(AssignmentStatusEnum.APPROVED.getCode());
				NonEmployeesAssignementEvaluationWorkFlow.approveHQGeneralManager(currentTask, assignmentEvaluation, assignmentDetailData, null);
			} else if (role.equals(WFTaskRolesEnum.REGION_INTELLIGENCE_MANAGER.getCode())) {
				NonEmployeesAssignementEvaluationWorkFlow.approveRegionManager(currentTask, assignmentEvaluation, null);
			} else if (role.equals(WFTaskRolesEnum.REGION_SECURITY_INTELLIGENCE_MANAGER.getCode())) {
				NonEmployeesAssignementEvaluationWorkFlow.approveRegionSecurityManager(currentTask, assignmentEvaluation, null);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		} catch (Exception e) {
			Log4j.traceErrorException(NonEmployeeAssignmentEvaluation.class, e, "NonEmployeeAssignmentEvaluation");
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do Notified
	 * 
	 * @return
	 */
	public String doNotified() {
		try {
			NonEmployeesAssignementEvaluationWorkFlow.doNotify(currentTask);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Do Reject
	 * 
	 * @return
	 */
	public String doReject() {
		try {
			assignmentEvaluation.setStatus(AssignmentStatusEnum.REJECTED.getCode());
			NonEmployeesAssignementEvaluationWorkFlow.rejectEmployeeAssignmentEvaluation(currentTask, assignmentEvaluation, null, loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * print details
	 */
	public void print() {
		try {
			if (assignmentEvaluation.getEvaluationAction().equals(AssignmentEvaluationActionEnum.END.getCode())) {
				byte[] bytes = AssignmentService.getNonEmployeeEvalEndDetailBytes(assignmentDetailData.getAssginmentId(), assignmentEvaluation.getId(), assignmentEvaluation.getReasons(), loginEmpData.getFullName());
				super.print(bytes, "nonEmployeeEvalEndDetail");
			} else {
				byte[] bytes = AssignmentService.getNonEmployeeEvalResumeDetailBytes(assignmentDetailData.getAssginmentId(), assignmentEvaluation.getId(), assignmentEvaluation.getExtendPeriod(), assignmentEvaluation.getReasons(), loginEmpData.getFullName());
				super.print(bytes, "nonEmployeeEvalResumeDetail");
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public List<AssignmentEvaluationPointData> getAssignmentEvaluationPointDataList() {
		return assignmentEvaluationPointDataList;
	}

	public void setAssignmentEvaluationPointDataList(List<AssignmentEvaluationPointData> assignmentEvaluationPointDataList) {
		this.assignmentEvaluationPointDataList = assignmentEvaluationPointDataList;
	}

	public List<String> getContinueReasonsList() {
		return continueReasonsList;
	}

	public void setContinueReasonsList(List<String> continueReasonsList) {
		this.continueReasonsList = continueReasonsList;
	}

	public List<String> getEndReasonsList() {
		return endReasonsList;
	}

	public void setEndReasonsList(List<String> endReasonsList) {
		this.endReasonsList = endReasonsList;
	}

	public AssignmentEvaluation getAssignmentEvaluation() {
		return assignmentEvaluation;
	}

	public void setAssignmentEvaluation(AssignmentEvaluation assignmentEvaluation) {
		this.assignmentEvaluation = assignmentEvaluation;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public List<String> getContinueEndSelectedReasonsList() {
		return continueEndSelectedReasonsList;
	}

	public void setContinueEndSelectedReasonsList(List<String> continueEndSelectedReasonsList) {
		this.continueEndSelectedReasonsList = continueEndSelectedReasonsList;
	}

	public int getAssignmentContinue() {
		return AssignmentEvaluationActionEnum.CONTINUE.getCode();
	}

	public int getAssignmentContinueTo() {
		return AssignmentEvaluationActionEnum.EXTEND.getCode();
	}

	public int getAssignmentEnd() {
		return AssignmentEvaluationActionEnum.END.getCode();
	}

	public AssignmentDetailData getAssignmentDetailData() {
		return assignmentDetailData;
	}

	public void setAssignmentDetailData(AssignmentDetailData assignmentDetailsData) {
		this.assignmentDetailData = assignmentDetailsData;
	}

	public List<AssignmentEvaluation> getPreviousEvaluationsList() {
		return previousEvaluationsList;
	}

	public void setPreviousEvaluationsList(List<AssignmentEvaluation> previousEvaluationsList) {
		this.previousEvaluationsList = previousEvaluationsList;
	}

	public List<AssignmentDetailData> getPreviousAssignmentsList() {
		return previousAssignmentsList;
	}

	public void setPreviousAssignmentsList(List<AssignmentDetailData> previousAssignmentsList) {
		this.previousAssignmentsList = previousAssignmentsList;
	}

	public List<InfoData> getEmployeeRelatedInfoList() {
		return employeeRelatedInfoList;
	}

	public void setEmployeeRelatedInfoList(List<InfoData> employeeRelatedInfoList) {
		this.employeeRelatedInfoList = employeeRelatedInfoList;
	}

	public Integer getBankAccountEnum() {
		return PaymentMethodEnum.BANK_ACCOUNT.getCode();
	}

	public Integer getChequeAcountEnum() {
		return PaymentMethodEnum.CHEQUE.getCode();
	}

	public Integer getCashAccountEnum() {
		return PaymentMethodEnum.CASH.getCode();
	}

	public List<AssignmentAgentClass> getAssignmentAgentClassesList() {
		return assignmentAgentClassesList;
	}

	public void setAssignmentAgentClassesList(List<AssignmentAgentClass> assignmentAgentClassesList) {
		this.assignmentAgentClassesList = assignmentAgentClassesList;
	}

	public List<BankBranchData> getBankBranchesDataList() {
		return bankBranchesDataList;
	}

	public void setBankBranchesDataList(List<BankBranchData> bankBranchesDataList) {
		this.bankBranchesDataList = bankBranchesDataList;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperetor() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}
}
