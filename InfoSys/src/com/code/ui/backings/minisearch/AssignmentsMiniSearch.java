package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.assignment.AssignmentDetailData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.FlagsEnum;
import com.code.enums.InfoSourceTypeEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "assignmentsMiniSearch")
@ViewScoped
public class AssignmentsMiniSearch extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int mode;
	private Long regionId;
	private String sectorName;
	private String agentCode;
	private List<AssignmentDetailData> assignmentDetailsDataList;
	private List<DepartmentData> regionList;
	private Integer assignmentType;

	public AssignmentsMiniSearch() {
		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = Integer.parseInt(getRequest().getParameter("mode"));
		}
		if (!getRequest().getParameter("regionId").equals("null") && !getRequest().getParameter("regionId").isEmpty() && !getRequest().getParameter("regionId").equals("undefined")) {
			regionId = Long.parseLong(getRequest().getParameter("regionId"));
		}
		assignmentDetailsDataList = new ArrayList<AssignmentDetailData>();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		assignmentType = FlagsEnum.ALL.getCode();
		sectorName = null;
		agentCode = null;
	}

	/**
	 * Search Domains
	 */
	public void searchAssignments() {
		try {
			if (mode == 1) {
				// getting only active assignments , cooperator
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					assignmentDetailsDataList = AssignmentService.getAssignmentDetailsData(FlagsEnum.ALL.getCode(), agentCode, sectorName, assignmentType);
				} else {
					assignmentDetailsDataList = AssignmentService.getAssignmentDetailsData(loginEmpData.getEmpId(), agentCode, sectorName, assignmentType);
				}
			} else if (mode == 2) {
				// getting only active cooperator
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					assignmentDetailsDataList = AssignmentService.getActiveAssignmentDetailsData(FlagsEnum.ALL.getCode(), agentCode, sectorName, InfoSourceTypeEnum.COOPERATOR.getCode());
				} else {
					assignmentDetailsDataList = AssignmentService.getActiveAssignmentDetailsData(loginEmpData.getEmpId(), agentCode, sectorName, InfoSourceTypeEnum.COOPERATOR.getCode());
				}
			} else if (mode == 3) {
				// getting only active assignments
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					assignmentDetailsDataList = AssignmentService.getActiveAssignmentDetailsData(FlagsEnum.ALL.getCode(), agentCode, sectorName, InfoSourceTypeEnum.ASSIGNMENT.getCode());
				} else {
					assignmentDetailsDataList = AssignmentService.getActiveAssignmentDetailsData(loginEmpData.getEmpId(), agentCode, sectorName, InfoSourceTypeEnum.ASSIGNMENT.getCode());
				}
			} else if (mode == 4) {
				// getting only active assignments by regionId
				assignmentDetailsDataList = AssignmentService.getActiveAssignmentDetailsDataByRegionId(regionId, agentCode, sectorName, assignmentType);

			} else if (mode == 5) {
				// getting all assignments and cooperators by regionId
				DepartmentData loginDep = DepartmentService.getDepartment(loginEmpData.getActualDepartmentId());
				if (loginDep.getRegionId().equals(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId())) {
					assignmentDetailsDataList = AssignmentService.getAssignmentDetailsData(FlagsEnum.ALL.getCode(), agentCode, sectorName, InfoSourceTypeEnum.COOPERATOR.getCode());
					assignmentDetailsDataList.addAll(AssignmentService.getAssignmentDetailsData(FlagsEnum.ALL.getCode(), agentCode, sectorName, InfoSourceTypeEnum.ASSIGNMENT.getCode()));
				} else {
					assignmentDetailsDataList = AssignmentService.getAssignmentDetailsData(loginEmpData.getEmpId(), agentCode, sectorName, InfoSourceTypeEnum.COOPERATOR.getCode());
					assignmentDetailsDataList.addAll(AssignmentService.getAssignmentDetailsData(loginEmpData.getEmpId(), agentCode, sectorName, InfoSourceTypeEnum.ASSIGNMENT.getCode()));
				}

			}

			if (assignmentDetailsDataList.isEmpty()) {
				this.setServerSideSuccessMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(AssignmentsMiniSearch.class, e, "AssignmentsMiniSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public List<AssignmentDetailData> getAssignmentDetailsDataList() {
		return assignmentDetailsDataList;
	}

	public void setAssignmentDetailsDataList(List<AssignmentDetailData> assignmentDetailsDataList) {
		this.assignmentDetailsDataList = assignmentDetailsDataList;
	}

	public List<DepartmentData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public Integer getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(Integer assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Integer getAssignment() {
		return InfoSourceTypeEnum.ASSIGNMENT.getCode();
	}

	public Integer getCooperetor() {
		return InfoSourceTypeEnum.COOPERATOR.getCode();
	}
}