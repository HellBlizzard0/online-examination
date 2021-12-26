package com.code.ui.backings.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.info.InfoData;
import com.code.dal.orm.security.UserMenuActionData;
import com.code.enums.AssignmentDetailStatusEnum;
import com.code.enums.AssignmentStatusEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.UserMenuActionsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.security.SecurityService;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "employeeAssignmentSearch")
@ViewScoped
public class EmployeeAssignmentsSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private AssignmentDetailData assignmentDetailDataSearch;
	private List<AssignmentDetailData> assignmentDetailDataList;
	private List<InfoData> employeeInfosList;
	private Boolean regionFlag = null;
	private Integer active = -1;
	private Boolean editprivilege = false;
	private String depsList;

	/**
	 * Default Constructor and Initializer
	 */
	public EmployeeAssignmentsSearch() {
		super();
		this.init();
		try {
			UserMenuActionData assignmentEditAction = SecurityService.getAction(loginEmpData.getEmpId(), UserMenuActionsEnum.EMPLOYEE_ASSIGNMENT_EDIT.getCode(), FlagsEnum.ALL.getCode());
			if (assignmentEditAction != null) {
				editprivilege = true;
			}
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId == null) {
				regionFlag = false;
			} else {
				assignmentDetailDataSearch.setOfficerId(loginEmpData.getEmpId());
				assignmentDetailDataSearch.setOfficerName(loginEmpData.getFullName());
				regionFlag = true;
			}
			long regionDepId = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId()).getRegionId();
			updateDepsList(regionDepId, null, null);
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  } catch (Exception e) {
			   Log4j.traceErrorException(EmployeeAssignmentsSearch.class, e, "EmployeeAssignmentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Initialize Variables
	 */
	public void init() {
		assignmentDetailDataList = new ArrayList<AssignmentDetailData>();
		assignmentDetailDataSearch = new AssignmentDetailData();
		employeeInfosList = new ArrayList<InfoData>();
		resetSearchFields();
	}

	/**
	 * Reset Search Fields
	 */
	public void resetSearchFields() {
		try {
			assignmentDetailDataSearch = new AssignmentDetailData();
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				assignmentDetailDataSearch.setRegionId(regionId);
				assignmentDetailDataSearch.setRegionName(DepartmentService.getDepartment(regionId).getArabicName());
			}
			if (regionId != null && editprivilege != true) {
				assignmentDetailDataSearch.setOfficerId(loginEmpData.getEmpId());
				assignmentDetailDataSearch.setOfficerName(loginEmpData.getFullName());
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Search Employee Assignment Details
	 */
	public void searchEmployeeAssignmentDetails() {
		try {
			assignmentDetailDataList = AssignmentService.getLastAssignmentDetailsData(assignmentDetailDataSearch.getOfficerId(), assignmentDetailDataSearch.getAgentCode(), null, assignmentDetailDataSearch.getIdentity(), assignmentDetailDataSearch.getStartDate(), assignmentDetailDataSearch.getApprovedEndDate(), assignmentDetailDataSearch.getRegionId(), FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), active, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode(),
					assignmentDetailDataSearch.getSectorId(), assignmentDetailDataSearch.getUnitId());
			Date currDate = HijriDateService.getHijriSysDate();
			for (AssignmentDetailData assignmentDetailData : assignmentDetailDataList) {
				if (assignmentDetailData.getStartDate().compareTo(currDate) > 0 && assignmentDetailData.getApprovedEndDate().compareTo(currDate) > 0) {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.HELD_NOT_STARTED.getCode());
				} else if (assignmentDetailData.getApprovedEndDate().compareTo(currDate) <= 0) {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.NOT_HELD.getCode());
				} else {
					assignmentDetailData.setHeldFlag(AssignmentDetailStatusEnum.HELD.getCode());
				}
			}
			if (assignmentDetailDataList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
				return;
			}
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  } catch (Exception e) {
			   Log4j.traceErrorException(EmployeeAssignmentsSearch.class, e, "EmployeeAssignmentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Evaluate Agent
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String evaluateAgent(AssignmentDetailData assignmentDetailData) {
		getRequest().setAttribute("mode", assignmentDetailData);
		return NavigationEnum.EMPLOYEE_ASSIGNEMNT_EVALUATION.toString();
	}

	/**
	 * Get Agent Information
	 * 
	 * @param assignmentDetailData
	 */
	public void getEmployeeInfos(AssignmentDetailData assignmentDetailData) {
		try {
			employeeInfosList = InfoService.getInfoDataBySourceIdentityAndOfficerId(assignmentDetailData.getOfficerId(), assignmentDetailData.getIdentity(), loginEmpData);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * update department list when changing region and sector and unit
	 * 
	 * @param regionId
	 * @param sectorId
	 * @param unitId
	 */
	public void updateDepsList(Long regionId, Long sectorId, Long unitId) {
		try {
			depsList = "";
			List<Long> departmentList = new ArrayList<Long>();
			if (unitId != null && unitId != 0) {
				depsList = unitId.toString();
			} else if (sectorId != null && sectorId != 0) {
				departmentList = AssignmentService.getDepsIdsOfIntelligenceRegionDepartments(regionId, sectorId);
			} else if (regionId.equals(getHeadQuarterId())) {
				departmentList = AssignmentService.getDepsIdsOfIntelligenceRegionDepartments(null, null);
			}
			if (!departmentList.isEmpty()) {
				for (Long depId : departmentList) {
					depsList += depId + "" + ",";
				}
			} else if(depsList.isEmpty()) {
				depsList = FlagsEnum.ALL.getCode() + "";
			}
		  } catch (BusinessException e) {
			   this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		  } catch (Exception e) {
			   Log4j.traceErrorException(EmployeeAssignmentsSearch.class, e, "EmployeeAssignmentsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	/**
	 * Re Assign Agent
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String reAssign(AssignmentDetailData assignmentDetailData) {
		try {
			if (AssignmentService.getLastAssignmentDetailsData(loginEmpData.getEmpId(), null, null, assignmentDetailData.getIdentity(), null, null, null, FlagsEnum.OFF.getCode(), AssignmentStatusEnum.APPROVED.getCode(), null, FlagsEnum.ON.getCode(), FlagsEnum.ALL.getCode()).isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("label_canNotReAssignEmp"));
				return null;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		getRequest().setAttribute("mode", assignmentDetailData.getAssginmentId());
		getRequest().setAttribute("object", assignmentDetailData);
		return NavigationEnum.EMPLOYEE_ASSIGNEMNT.toString();
	}

	/**
	 * View Agent Assignment Details
	 * 
	 * @param assignmentDetailData
	 * @return
	 */
	public String viewAssignmentDetail(AssignmentDetailData assignmentDetailData) {
		getRequest().setAttribute("mode", assignmentDetailData.getAssginmentId());
		getRequest().setAttribute("assignmentId", assignmentDetailData.getAssginmentId());
		getRequest().setAttribute("object", assignmentDetailData);
		getRequest().setAttribute("pageMode", 1);
		return NavigationEnum.EMPLOYEE_ASSIGNEMNT.toString();
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public AssignmentDetailData getAssignmentDetailDataSearch() {
		return assignmentDetailDataSearch;
	}

	public void setAssignmentDetailDataSearch(AssignmentDetailData assignmentDetailDataSearch) {
		this.assignmentDetailDataSearch = assignmentDetailDataSearch;
	}

	public List<AssignmentDetailData> getAssignmentDetailDataList() {
		return assignmentDetailDataList;
	}

	public void setAssignmentDetailDataList(List<AssignmentDetailData> assignmentDetailDataList) {
		this.assignmentDetailDataList = assignmentDetailDataList;
	}

	public List<InfoData> getEmployeeInfosList() {
		return employeeInfosList;
	}

	public void setEmployeeInfosList(List<InfoData> employeeInfosList) {
		this.employeeInfosList = employeeInfosList;
	}

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

	public Integer getAssignmentDetailsApprovedStatus() {
		return AssignmentStatusEnum.APPROVED.getCode();
	}

	public Integer getAssignmentDetailsUnderApprovalStatus() {
		return AssignmentStatusEnum.UNDER_APPROVAL.getCode();
	}

	public int getEnumHeldStarted() {
		return AssignmentDetailStatusEnum.HELD.getCode();
	}

	public int getEnumNotHeld() {
		return AssignmentDetailStatusEnum.NOT_HELD.getCode();
	}

	public int getEnumHeldNotStarted() {
		return AssignmentDetailStatusEnum.HELD_NOT_STARTED.getCode();
	}

	public Boolean getRegionFlag() {
		return regionFlag;
	}

	public void setRegionFlag(Boolean regionFlag) {
		this.regionFlag = regionFlag;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Boolean getEditprivilege() {
		return editprivilege;
	}

	public void setEditprivilege(Boolean editprivilege) {
		this.editprivilege = editprivilege;
	}

	public String getDepsList() {
		return depsList;
	}

	public void setDepsList(String depsList) {
		this.depsList = depsList;
	}

	public Long getHeadQuarterId() throws BusinessException {
		return WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId();
	}
}
