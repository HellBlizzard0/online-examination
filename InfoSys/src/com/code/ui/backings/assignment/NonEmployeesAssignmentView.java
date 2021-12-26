package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentAgentClass;
import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.setup.BankBranchData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.PaymentMethodEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.CommonService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "nonEmployeeAssginmentView")
@ViewScoped
public class NonEmployeesAssignmentView extends WFBaseBacking implements Serializable {
	private AssignmentData assignmentData;
	private AssignmentDetailData assignmentDetailData;
	private List<String> assignmentReasonsList;
	private List<AssignmentAgentClass> assignmentAgentClassesList;
	private List<BankBranchData> bankBranchesDataList;
	private String remarks;
	private String screenTitle;
	private boolean regionSelected;
	private boolean sectorSelected;
	private boolean isSaved;

	/**
	 * Default Constructor and Initializer
	 */
	public NonEmployeesAssignmentView() {
		super.init();
		this.init();
		try {
			Long assignmentDetailId = null;
			assignmentAgentClassesList = AssignmentService.getAgentClasses();
			bankBranchesDataList = CommonService.getBankBranches();
			// Set Reasons List
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.ASSIGNMENT_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				if (reasonsDomainList.isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_DBError"));
				}
				for (SetupDomain domain : reasonsDomainList) {
					assignmentReasonsList.add(domain.getDescription());
				}
			}

			if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
				assignmentDetailId = Long.parseLong(getRequest().getParameter("mode"));
				assignmentDetailData = AssignmentService.getAssignmentDetailsDataByAssignmentId(assignmentDetailId).get(0);
				assignmentData = AssignmentService.getAssignments(assignmentDetailData.getAssginmentId(), null, null, null, null, null).get(0);

				if (assignmentDetailData.getReasons() != null && !assignmentDetailData.getReasons().isEmpty()) {
					String[] reasons = assignmentDetailData.getReasons().split(",");
					assignmentDetailData.setReasonsList(Arrays.asList(reasons));
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(e.getMessage());
		}catch (Exception e) {
			 Log4j.traceErrorException(NonEmployeesAssignmentView.class, e, "NonEmployeesAssignmentView");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	/**
	 * Initialize bean variables
	 */
	public void init() {
		assignmentData = new AssignmentData();
		assignmentReasonsList = new ArrayList<String>();
		assignmentAgentClassesList = new ArrayList<AssignmentAgentClass>();
		bankBranchesDataList = new ArrayList<BankBranchData>();
		screenTitle = getParameterizedMessage("title_nonEmployeeAssignmentView");
	}

	public AssignmentData getAssignmentData() {
		return assignmentData;
	}

	public void setAssignmentData(AssignmentData assignmentData) {
		this.assignmentData = assignmentData;
	}

	public AssignmentDetailData getAssignmentDetailData() {
		return assignmentDetailData;
	}

	public void setAssignmentDetailData(AssignmentDetailData assignmentDetailData) {
		this.assignmentDetailData = assignmentDetailData;
	}

	public List<String> getAssignmentReasonsList() {
		return assignmentReasonsList;
	}

	public void setAssignmentReasonsList(List<String> assignmentReasonsList) {
		this.assignmentReasonsList = assignmentReasonsList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
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

	public Integer getBankAccountEnum() {
		return PaymentMethodEnum.BANK_ACCOUNT.getCode();
	}

	public Integer getChequeAcountEnum() {
		return PaymentMethodEnum.CHEQUE.getCode();
	}

	public Integer getCashAccountEnum() {
		return PaymentMethodEnum.CASH.getCode();
	}

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

	public Long getHeadQuarterId() throws BusinessException {
		return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}

	public boolean isRegionSelected() {
		return regionSelected;
	}

	public void setRegionSelected(boolean regionSelected) {
		this.regionSelected = regionSelected;
	}

	public boolean isSectorSelected() {
		return sectorSelected;
	}

	public void setSectorSelected(boolean sectorSelected) {
		this.sectorSelected = sectorSelected;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperetor() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}
}
