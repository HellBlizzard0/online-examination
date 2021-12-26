package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.VisitorsEntranceService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "visitorsEntranceReport")
@ViewScoped
public class VisitorsEntranceReport extends BaseBacking implements Serializable {
	private Date fromDate;
	private Date toDate;
	private Long regionId;
	private String regionName;
	
	/**
	 * Default Constructor
	 */
	public VisitorsEntranceReport() {
		try {
			Long regionDepId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionDepId != null) {
				regionId = regionDepId;
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}
	
	/**
	 * Reset print parameters
	 */
	public void reset() {
		fromDate = null;
		toDate = null;
		regionId = null;
		regionName = "";
		try {
			Long regionDepId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (regionDepId != null) {
				regionId = regionDepId;
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}
	
	/**
	 * Print Report
	 */
	public void print() {
		if(fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		
		try {
			String depName = "";
			if (regionId != null) {
				depName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = VisitorsEntranceService.getVisitorEntranceNoExitReportBytes(fromDate, toDate, regionId == null ? FlagsEnum.ALL.getCode() : regionId, depName, loginEmpData.getFullName());
			super.print(bytes, "Visitors Entrance No Exit Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(VisitorsEntranceReport.class, e, "VisitorsEntranceReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	// setters and getters
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

	public String getDepartmentType() {
		return DepartmentTypeEnum.REGION.getCode() + "," + DepartmentTypeEnum.DIRECTORATE.getCode();
	}

}