package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.ReportNamesEnum;
import com.code.enums.SecurityCheckReasonEnum;
import com.code.enums.WFPositionsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securitycheck.SecurityCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.WFPositionService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "securityCheckReport")
@ViewScoped
public class SecurityCheckReport extends BaseBacking implements Serializable {
	private Long domainIncomingSourceId;
	private Long departmentSourceId;
	private String departmentSourceName;
	private Date fromDate;
	private Date toDate;
	private List<SetupDomain> incomingSideList;
	private int notesExists;
	private Integer securityCheckReason;
	private Long regionId;
	private String employeeRegionName;
	private List<DepartmentData> regionDepartmentsList = new ArrayList<DepartmentData>();
	private boolean isRegion;
	private int viewMode = 1;

	/**
	 * Constructor
	 */
	public SecurityCheckReport() {
		try {
			if (getRequest().getParameter("viewMode") != null) {
				viewMode = (Integer.valueOf(getRequest().getParameter("viewMode")));
			}
			incomingSideList = SetupService.getDomains(ClassesEnum.INCOMING_SIDES.getCode());
			notesExists = FlagsEnum.ALL.getCode();
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
				regionDepartmentsList.add(DepartmentService.getDepartment(WFPositionService.getWFPosition(WFPositionsEnum.DIRECTORATE_DEPARTMENT.getCode()).getUnitId()));
				regionDepartmentsList.addAll(DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode()));
				regionId = (long) FlagsEnum.ALL.getCode();
				employeeRegionName = null;
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
				isRegion = true;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityCheckReport.class, e, "SecurityCheckReport");
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
			String depName = "";
			if (regionId.longValue() != FlagsEnum.ALL.getCode()) {
				depName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = SecurityCheckService.getSecurityCheckBytes(departmentSourceId, domainIncomingSourceId, viewMode, notesExists, fromDate, toDate, regionId, depName, loginEmpData.getFullName(), securityCheckReason);
			String reportName = "";
			if (viewMode == 1) {
				reportName = ReportNamesEnum.SECURITY_CHECK_EMP.toString().replace("_", " ");
			} else if (viewMode == 2) {
				reportName = ReportNamesEnum.SECURITY_CHECK_NON_EMP.toString().replace("_", " ");
			}
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityCheckReport.class, e, "SecurityCheckReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printStatisticalReport() {
		try {
			byte[] bytes = SecurityCheckService.getSecurityCheckStatisticalBytes(departmentSourceId, domainIncomingSourceId, viewMode, notesExists, fromDate, toDate, loginEmpData.getFullName(), securityCheckReason);
			String reportName = "";
			if (viewMode == 1) {
				reportName = ReportNamesEnum.SECURITY_CHECK_STATISTICAL_EMP.toString().replace("_", " ");
			} else if (viewMode == 2) {
				reportName = ReportNamesEnum.SECURITY_CHECK_STATISTICAL_NON_EMP.toString().replace("_", " ");
			}
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityCheckReport.class, e, "SecurityCheckReport");
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
		domainIncomingSourceId = null;
		departmentSourceId = null;
		departmentSourceName = null;
		notesExists = FlagsEnum.ALL.getCode();
		fromDate = null;
		toDate = null;
	}

	public Long getDomainIncomingSourceId() {
		return domainIncomingSourceId;
	}

	public void setDomainIncomingSourceId(Long domainIncomingSourceId) {
		this.domainIncomingSourceId = domainIncomingSourceId;
	}

	public Long getDepartmentSourceId() {
		return departmentSourceId;
	}

	public void setDepartmentSourceId(Long departmentSourceId) {
		this.departmentSourceId = departmentSourceId;
	}

	public String getDepartmentSourceName() {
		return departmentSourceName;
	}

	public void setDepartmentSourceName(String departmentSourceName) {
		this.departmentSourceName = departmentSourceName;
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

	public List<SetupDomain> getIncomingSideList() {
		return incomingSideList;
	}

	public void setIncomingSideList(List<SetupDomain> incomingSideList) {
		this.incomingSideList = incomingSideList;
	}

	public int getNotesExists() {
		return notesExists;
	}

	public void setNotesExists(int notesExists) {
		this.notesExists = notesExists;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}

	public List<DepartmentData> getRegionDepartmentsList() {
		return regionDepartmentsList;
	}

	public void setRegionDepartmentsList(List<DepartmentData> regionDepartmentsList) {
		this.regionDepartmentsList = regionDepartmentsList;
	}

	public Integer getSecurityCheckReason() {
		return securityCheckReason;
	}

	public void setSecurityCheckReason(Integer securityCheckReason) {
		this.securityCheckReason = securityCheckReason;
	}

	public List<SecurityCheckReasonEnum> getSecurityCheckReasonsEnum() {
		return SecurityCheckReasonEnum.getAllSecurityCheckReason();
	}

	public int getViewMode() {
		return viewMode;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}

}