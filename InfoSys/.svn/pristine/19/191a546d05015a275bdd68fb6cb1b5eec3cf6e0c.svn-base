package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.surveillance.SurveillanceOrdersService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "surveillanceEmployeesReport")
@ViewScoped
public class SurveillanceEmployeesReport extends BaseBacking implements Serializable {
	private List<DepartmentData> regions = new ArrayList<DepartmentData>();
	private Long regionId;
	private String employeeRegionName;
	private int surveillanceStatus;
	private Date fromDate;
	private Date toDate;
	private Map<Long, String> regionsMap;
	private Integer lastReportFromPercentage;
	private Integer lastReportToPercentage;
	private boolean isRegion;

	/**
	 * Constructor
	 */
	public SurveillanceEmployeesReport() {
		try {
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regions.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regions.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
				isRegion = true;
			}
			regionsMap = new HashMap<Long, String>();
			for (DepartmentData region : regions) {
				regionsMap.put(region.getId(), region.getArabicName());
			}
			surveillanceStatus = FlagsEnum.ON.getCode();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceEmployeesReport.class, e, "SurveillanceEmployeesReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void print() {
		try {
			if (fromDate == null || toDate == null) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
				return;
			}
			byte[] bytes = SurveillanceOrdersService.getSurveillanceEmployeesBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, employeeRegionName == null || employeeRegionName.isEmpty() ? regionsMap.get(regionId) : employeeRegionName, surveillanceStatus, fromDate, toDate, loginEmpData.getFullName(), lastReportFromPercentage == null ? FlagsEnum.ALL.getCode() : lastReportFromPercentage, lastReportToPercentage == null ? FlagsEnum.ALL.getCode() : lastReportToPercentage);
			String reportName = ReportNamesEnum.SURVEILLANCE_EMPLOYEES.toString().replace("_", " ");
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceEmployeesReport.class, e, "SurveillanceEmployeesReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printStatisticalReport() {
		try {
			byte[] bytes = SurveillanceOrdersService.getSurveillanceEmployeesStatisticalBytes(regionId == null ? FlagsEnum.ALL.getCode() : regionId, employeeRegionName == null || employeeRegionName.isEmpty() ? regionsMap.get(regionId) : employeeRegionName, surveillanceStatus, fromDate, toDate, loginEmpData.getFullName(), lastReportFromPercentage == null ? FlagsEnum.ALL.getCode() : lastReportFromPercentage, lastReportToPercentage == null ? FlagsEnum.ALL.getCode() : lastReportToPercentage);
			String reportName = ReportNamesEnum.SURVEILLANCE_EMPLOYEES_STATISTICAL.toString().replace("_", " ");
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SurveillanceEmployeesReport.class, e, "SurveillanceEmployeesReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		if (!isRegion) {
			regionId = null;
		}
		surveillanceStatus = FlagsEnum.ON.getCode();
		fromDate = null;
		toDate = null;
		lastReportFromPercentage = null;
		lastReportToPercentage = null;
	}

	public List<DepartmentData> getRegions() {
		return regions;
	}

	public void setRegions(List<DepartmentData> regions) {
		this.regions = regions;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public int getSurveillanceStatus() {
		return surveillanceStatus;
	}

	public void setSurveillanceStatus(int surveillanceStatus) {
		this.surveillanceStatus = surveillanceStatus;
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

	public Integer getLastReportFromPercentage() {
		return lastReportFromPercentage;
	}

	public void setLastReportFromPercentage(Integer lastReportFromPercentage) {
		this.lastReportFromPercentage = lastReportFromPercentage;
	}

	public Integer getLastReportToPercentage() {
		return lastReportToPercentage;
	}

	public void setLastReportToPercentage(Integer lastReportToPercentage) {
		this.lastReportToPercentage = lastReportToPercentage;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}

}