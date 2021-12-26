package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.SecurityGuardMissionService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "securityGuardMissionReport")
@ViewScoped
public class SecurityGuardMissionReport extends BaseBacking implements Serializable {
	private String missionType;
	private Long employeeId;
	private String employeeName;
	private Long missionSrcId;
	private Date fromDate;
	private Date toDate;
	private List<SetupDomain> missionTypesList;
	private List<SetupDomain> orderSourcesList;

	/**
	 * Constructor
	 */
	public SecurityGuardMissionReport() {
		super();
		try {
			orderSourcesList = SetupService.getDomains(ClassesEnum.ORDER_SOURCES.getCode());
			missionTypesList = SetupService.getDomains(ClassesEnum.MISSION_TYPES.getCode());
			fromDate = HijriDateService.getHijriSysDate();
			toDate = fromDate;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Print Report Using Given Fields
	 */
	public void print() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			byte[] bytes = SecurityGuardMissionService.getSecurityGuardMissionBytes(missionType, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, missionSrcId == null ? FlagsEnum.ALL.getCode() : missionSrcId, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "Security Guard Mission Report";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(SecurityGuardMissionReport.class, e, "SecurityGuardMissionReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		missionType = null;
		employeeId = null;
		employeeName = null;
		missionSrcId = null;
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public String getMissionType() {
		return missionType;
	}

	public void setMissionType(String missionType) {
		this.missionType = missionType;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getMissionSrcId() {
		return missionSrcId;
	}

	public void setMissionSrcId(Long missionSrcId) {
		this.missionSrcId = missionSrcId;
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

	public List<SetupDomain> getMissionTypesList() {
		return missionTypesList;
	}

	public void setMissionTypesList(List<SetupDomain> missionTypesList) {
		this.missionTypesList = missionTypesList;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<SetupDomain> getOrderSourcesList() {
		return orderSourcesList;
	}

	public void setOrderSourcesList(List<SetupDomain> orderSourcesList) {
		this.orderSourcesList = orderSourcesList;
	}
}
