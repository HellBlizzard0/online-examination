package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentData;
import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.setup.SetupClass;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeAssginmentView")
@ViewScoped
public class EmployeeAssignmentView extends BaseBacking implements Serializable {

	private AssignmentData assignmentData;
	private AssignmentDetailData assignmentDetailsData;
	private List<String> assignmentReasonsList;

	/**
	 * Default Constructor and Initializer
	 */
	public EmployeeAssignmentView() {
		try {
			// Set Reasons List
			List<SetupClass> setupClass = SetupService.getClasses(ClassesEnum.ASSIGNMENT_REASONS.getCode(), null, FlagsEnum.ALL.getCode());
			if (!setupClass.isEmpty()) {
				List<SetupDomain> reasonsDomainList = SetupService.getDomains(setupClass.get(0).getId(), null, FlagsEnum.ALL.getCode());
				if (reasonsDomainList.isEmpty()) {
					this.setServerSideErrorMessages(getParameterizedMessage("error_DBError"));
				}
				assignmentReasonsList = new ArrayList<String>();
				for (SetupDomain domain : reasonsDomainList) {
					assignmentReasonsList.add(domain.getDescription());
				}
			}

			if (getRequest().getParameter("mode") != null) {
				Long assignmentDetailId = Long.parseLong(getRequest().getParameter("mode"));
				assignmentDetailsData = AssignmentService.getAssignmentDetailsDataById(assignmentDetailId);
				assignmentData = AssignmentService.getAssignments(assignmentDetailsData.getAssginmentId(), null, null, null, null, null).get(0);

				// Set employees reasons list
				if (assignmentDetailsData.getReasons() != null && !assignmentDetailsData.getReasons().isEmpty()) {
					String[] reasons = assignmentDetailsData.getReasons().split(",");
					assignmentDetailsData.setReasonsList(Arrays.asList(reasons));
				}
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(EmployeeAssignmentView.class, e, "EmployeeAssignmentView");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
	public AssignmentData getAssignmentData() {
		return assignmentData;
	}

	public void setAssignmentData(AssignmentData assignmentData) {
		this.assignmentData = assignmentData;
	}

	public List<String> getAssignmentReasonsList() {
		return assignmentReasonsList;
	}

	public void setAssignmentReasonsList(List<String> assignmentReasonsList) {
		this.assignmentReasonsList = assignmentReasonsList;
	}

	public Long getHeadQuarterId() throws BusinessException {
		return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}

	public AssignmentDetailData getAssignmentDetailsData() {
		return assignmentDetailsData;
	}

	public void setAssignmentDetailsData(AssignmentDetailData assignmentDetailsData) {
		this.assignmentDetailsData = assignmentDetailsData;
	}
}
