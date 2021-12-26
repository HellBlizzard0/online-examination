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
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "departmentAccountSupportReport")
@ViewScoped
public class DepartmentAccountSupportReport extends BaseBacking implements Serializable {
	private Date startDate;
	private Date endDate;
	private Long regionId;
	private String employeeRegionName;
	private Long sectorId;
	private List<DepartmentData> regionList = new ArrayList<DepartmentData>();
	private List<DepartmentData> sectorList;
	private boolean isRegion;

	/**
	 * Default Constructor
	 */
	public DepartmentAccountSupportReport() {
		sectorId = (long) FlagsEnum.ALL.getCode();
		try {
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regionList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
				sectorList = new ArrayList<DepartmentData>();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
				sectorList = DepartmentService.getAssignmentDepartments(regionId);
				isRegion = true;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupportReport.class, e, "DepartmentAccountSupportReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get sector at selected region
	 */
	public void getSectors() {
		if (regionId.equals((long) FlagsEnum.ALL.getCode())) {
			sectorList = new ArrayList<DepartmentData>();
		} else {
			try {
				sectorList = DepartmentService.getAssignmentDepartments(regionId);
			} catch (BusinessException e) {
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}
		}
	}

	/**
	 * Reset report parameter
	 */
	public void reset() {
		if (!isRegion) {
			regionId = null;
		}
		sectorId = null;
		startDate = null;
		endDate = null;
	}

	/**
	 * Print report
	 */
	public void printReport() {
		if (startDate == null || endDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = FinanceAccountsService.getDepartmentSupportReportBytes(HijriDateService.getHijriDateString(startDate), HijriDateService.getHijriDateString(endDate), regionId, regionName, sectorId, loginEmpData.getFullName());
			super.print(bytes, "Department_Support_Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupportReport.class, e, "DepartmentAccountSupportReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Statistical Report
	 */
	public void printStatisticalReport() {
		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = FinanceAccountsService.getStatisticalDepartmentSupportReportBytes(HijriDateService.getHijriDateString(startDate), HijriDateService.getHijriDateString(endDate), regionId, regionName, sectorId, loginEmpData.getFullName());
			super.print(bytes, "Statistical_Department_Support_Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(DepartmentAccountSupportReport.class, e, "DepartmentAccountSupportReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public List<DepartmentData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}

	public List<DepartmentData> getSectorList() {
		return sectorList;
	}

	public void setSectorList(List<DepartmentData> sectorList) {
		this.sectorList = sectorList;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}

}