package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.labcheck.MissionReportService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckMissionReport")
@ViewScoped
public class LabCheckMissionReport extends BaseBacking implements Serializable {
	private List<DepartmentData> regionList;
	private Long missionTypeId;
	private Long employeeId;
	private String employeeName;
	private Long missionDstId;
	private Date fromDate;
	private Date toDate;
	private List<SetupDomain> missionTypesList;

	public LabCheckMissionReport() {
		super();
		try {
			missionTypesList = SetupService.getDomains(ClassesEnum.LAB_CHECK_MISSIONS.getCode());
			regionList = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
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
			byte[] bytes = MissionReportService.getMissionReportBytes(missionTypeId == null ? FlagsEnum.ALL.getCode() : missionTypeId, employeeId == null ? FlagsEnum.ALL.getCode() : employeeId, missionDstId == null ? FlagsEnum.ALL.getCode() : missionDstId, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "LabCheckMissionReport";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckMissionReport.class, e, "LabCheckMissionReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		missionTypeId = null;
		employeeId = null;
		employeeName = null;
		missionDstId = null;
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getMissionDstId() {
		return missionDstId;
	}

	public void setMissionDstId(Long missionDstId) {
		this.missionDstId = missionDstId;
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

	public Long getMissionTypeId() {
		return missionTypeId;
	}

	public void setMissionTypeId(Long missionTypeId) {
		this.missionTypeId = missionTypeId;
	}

	public List<DepartmentData> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<DepartmentData> regionList) {
		this.regionList = regionList;
	}
}
