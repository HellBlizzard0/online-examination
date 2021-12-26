package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securitymission.VisitorEntranceData;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.VisitorsEntranceService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;
import com.code.services.setup.DepartmentService;

@SuppressWarnings("serial")
@ManagedBean(name = "visitorsEntranceSearch")
@ViewScoped
public class VisitorsEntranceSearch extends BaseBacking implements Serializable {
	private VisitorEntranceData visitorEntranceData;
	private String employeeName;
	private Boolean entryExitFlag;
	private Date fromDate;
	private Date toDate;
	private String fromTime;
	private String toTime;
	private List<VisitorEntranceData> visitorEntranceList;
	private Long loginEmpRegionId;
	private String loginEmpRegionName;

	/**
	 * Default Constructor
	 */
	public VisitorsEntranceSearch() {
		visitorEntranceData = new VisitorEntranceData();
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				loginEmpRegionId = regionId;
				loginEmpRegionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset search parameters
	 */
	public void reset() {
		visitorEntranceData = new VisitorEntranceData();
		fromDate = null;
		toDate = null;
		fromTime = null;
		toTime = null;
		entryExitFlag = null;
		employeeName = null;
		loginEmpRegionId = null;
		loginEmpRegionName = "";
		
		try {
			Long regionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionId != null) {
				loginEmpRegionId = regionId;
				loginEmpRegionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Search 
	 */
	public void search() {
		try {
			visitorEntranceList = VisitorsEntranceService.getVisitorEntrances(visitorEntranceData.getDepartmentId() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getDepartmentId(), visitorEntranceData.getEmployeeId() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getEmployeeId(), visitorEntranceData.getVisitorIdentity() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getVisitorIdentity(), visitorEntranceData.getVisitorCardNumber(), visitorEntranceData.getVisitorName(),
					fromDate, toDate, fromTime, toTime, entryExitFlag, loginEmpRegionId == null ? FlagsEnum.ALL.getCode() : loginEmpRegionId);
			if (visitorEntranceList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noData"));
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void print() {
		try {
			String depName = "";
			if (loginEmpRegionId != null) {
				depName = DepartmentService.getDepartment(loginEmpRegionId).getArabicName();
			}
			byte[] bytes = VisitorsEntranceService.getVisitorEntranceReportBytes(visitorEntranceData.getDepartmentId() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getDepartmentId(), visitorEntranceData.getEmployeeId() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getEmployeeId(), visitorEntranceData.getVisitorIdentity() == null ? FlagsEnum.ALL.getCode() : visitorEntranceData.getVisitorIdentity(), visitorEntranceData.getVisitorCardNumber(),
					visitorEntranceData.getVisitorName(), fromDate, toDate, fromTime, toTime, entryExitFlag, loginEmpData.getFullName(), loginEmpRegionId == null ? FlagsEnum.ALL.getCode() : loginEmpRegionId, depName);
			String reportName = "Visitor Entrance Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(VisitorsEntranceSearch.class, e, "VisitorsEntranceSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
	public VisitorEntranceData getVisitorEntranceData() {
		return visitorEntranceData;
	}

	public void setVisitorEntranceData(VisitorEntranceData visitorEntranceData) {
		this.visitorEntranceData = visitorEntranceData;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Boolean getEntryExitFlag() {
		return entryExitFlag;
	}

	public void setEntryExitFlag(Boolean entryExitFlag) {
		this.entryExitFlag = entryExitFlag;
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

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public List<VisitorEntranceData> getVisitorEntranceList() {
		return visitorEntranceList;
	}

	public void setVisitorEntranceList(List<VisitorEntranceData> visitorEntranceList) {
		this.visitorEntranceList = visitorEntranceList;
	}

	public Long getLoginEmpRegionId() {
		return loginEmpRegionId;
	}

	public void setLoginEmpRegionId(Long loginEmpRegionId) {
		this.loginEmpRegionId = loginEmpRegionId;
	}

	public String getLoginEmpRegionName() {
		return loginEmpRegionName;
	}

	public void setLoginEmpRegionName(String loginEmpRegionName) {
		this.loginEmpRegionName = loginEmpRegionName;
	}

}
