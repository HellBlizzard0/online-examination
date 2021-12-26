package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.LabCheckService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "exemptsReport")
@ViewScoped
public class ExemptsReport extends BaseBacking implements Serializable {
	private long regionId;
	private String regionName;
	private String employeeRegionName;
	private Date fromDate;
	private Date toDate;
	private boolean isRegion;

	public ExemptsReport() {
		super();
		try {
			Long empRegionId = DepartmentService.isRegionDepartment(loginEmpData.getActualDepartmentId());
			if (empRegionId == null) {
				employeeRegionName = null;
			} else {
				employeeRegionName = DepartmentService.getDepartment(empRegionId).getArabicName();
				regionId = empRegionId;
				isRegion = true;
				regionName = employeeRegionName;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ExemptsReport.class, e, "ExemptsReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
		regionId = FlagsEnum.ALL.getCode();
		regionName = null;
		fromDate = null;
		toDate = null;
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void printReport() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			byte[] bytes = LabCheckService.getExemptsReportBytes(regionId, regionName, fromDate, toDate, loginEmpData.getFullName());
			super.print(bytes, "Exempts Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ExemptsReport.class, e, "ExemptsReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		if (!isRegion) {
			regionId = FlagsEnum.ALL.getCode();
			regionName = null;
		}
		fromDate = null;
		toDate = null;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
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

	public String getEmployeeRegionName() {
		return employeeRegionName;
	}

	public void setEmployeeRegionName(String employeeRegionName) {
		this.employeeRegionName = employeeRegionName;
	}
}