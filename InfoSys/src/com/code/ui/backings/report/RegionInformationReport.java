package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.assignment.AssignmentService;
import com.code.services.infosys.info.InfoService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "regionInformationReport")
@ViewScoped
public class RegionInformationReport extends BaseBacking implements Serializable {
	private Long regionId;
	private String regionName;
	private String employeeRegionName;
	private Long sectorId;
	private String sectorName;
	private List<DepartmentData> regionDepartmentsList = new ArrayList<DepartmentData>();
	private List<DepartmentData> sectorDepartmentsList;
	private Date fromDate;
	private Date toDate;
	private String agentCode;
	private Long agentId;
	private boolean isRegion;

	/**
	 * Constructor
	 */
	public RegionInformationReport() {
		super();
		try {
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regionDepartmentsList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionDepartmentsList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionName = employeeRegionName;
				regionId = empRegionId;
				sectorDepartmentsList = DepartmentService.getDepartmentsByRegionIdAndType(regionId, null, null, DepartmentTypeEnum.SECTOR.getCode());
				isRegion = true;
			}
			fromDate = HijriDateService.getHijriSysDate();
			toDate = fromDate;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(RegionInformationReport.class, e, "RegionInformationReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Update Sector List
	 */
	public void updateSectorList() {
		try {
			sectorId = null;
			sectorName = null;
			sectorDepartmentsList = regionId == null ? new ArrayList<DepartmentData>() : DepartmentService.getDepartmentsByRegionIdAndType(regionId, null, null, DepartmentTypeEnum.SECTOR.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		if (/*fromDate == null || */ toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			List<Long> visibleList = new ArrayList<Long>();
			Long departmentId = loginEmpData.getActualDepartmentId();
			Long empRegionId = DepartmentService.isRegionDepartment(departmentId);
			if (empRegionId == null ){
				if (WFPositionService.getWFPosition(WFPositionsEnum.GENERAL_INTELLIGENCE_DIRECTORATE.getCode()).getUnitId().equals(departmentId) || WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_INTELLIGENCE_DEPARTMENT.getCode()).getUnitId().equals(departmentId) || WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_SECURITY_ANALYSIS_DEPARTMENT.getCode()).getUnitId().equals(departmentId))
				visibleList.add(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId());
			} else {
				visibleList.add(empRegionId);
			}
			Long officerId = null;
			String identity = null;
			if(agentId != null){
			 officerId = AssignmentService.getAssignmentDetailsDataByAssignmentId(agentId).get(0).getOfficerId();
			 identity = AssignmentService.getAssignmentDetailsDataByAssignmentId(agentId).get(0).getIdentity();
			}
			byte[] bytes = InfoService.getRegionInformationBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, visibleList, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, null, toDate, regionName, loginEmpData.getFullName(), agentCode , identity , officerId == null ? FlagsEnum.ALL.getCode() : officerId);
			String reportName = "Region Information Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(RegionInformationReport.class, e, "RegionInformationReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReportStatistics() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			byte[] bytes = InfoService.getRegionInformationStatisticsBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, sectorId == null ? FlagsEnum.ALL.getCode() : sectorId, fromDate, toDate, regionName, loginEmpData.getFullName(), agentCode);
			String reportName = "Region Information Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(RegionInformationReport.class, e, "RegionInformationReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		if (!isRegion) {
			regionId = null;
			regionName = null;
		}
		sectorId = null;
		sectorName = null;
		agentCode = null;
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public List<DepartmentData> getRegionDepartmentsList() {
		return regionDepartmentsList;
	}

	public void setRegionDepartmentsList(List<DepartmentData> regionDepartmentsList) {
		this.regionDepartmentsList = regionDepartmentsList;
	}

	public List<DepartmentData> getSectorDepartmentsList() {
		return sectorDepartmentsList;
	}

	public void setSectorDepartmentsList(List<DepartmentData> sectorDepartmentsList) {
		this.sectorDepartmentsList = sectorDepartmentsList;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}
}
