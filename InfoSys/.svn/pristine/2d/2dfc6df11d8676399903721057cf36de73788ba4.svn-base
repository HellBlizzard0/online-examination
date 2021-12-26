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
import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.MonthlyRewardPaymentService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "monthlyRewardReport")
@ViewScoped
public class MonthlyRewardReport extends BaseBacking implements Serializable {
	private Date fromDate;
	private Date toDate;
	private List<DepartmentData> regionsList = new ArrayList<DepartmentData>();
	private List<DepartmentData> sectorsList;
	private Long regionId;
	private Long sectorId;

	/**
	 * Constructor
	 */
	public MonthlyRewardReport() {
		super();
		init();
		try {
			regionsList = DepartmentService.getDepartmentsBydepartmentType(DepartmentTypeEnum.REGION.getCode());
			sectorsList = new ArrayList<DepartmentData>();
			fromDate = HijriDateService.getHijriSysDate();
			toDate = fromDate;
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * getRegionSectors
	 */
	public void getRegionSectors() {
		try {
			sectorId = null;
			if (regionId != null) {
				sectorsList = DepartmentService.getAssignmentDepartments(regionId);
			} else {
				sectorsList = new ArrayList<DepartmentData>();
			}
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
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = MonthlyRewardPaymentService.getMonthlyRewardReportBytes(regionName, regionId, sectorId, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "MonthlyRewardReport";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardReport.class, e, "MonthlyRewardReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * printStatistics
	 */
	public void printStatistics() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			String regionName = "";
			if (!regionId.equals((long) FlagsEnum.ALL.getCode())) {
				regionName = DepartmentService.getDepartment(regionId).getArabicName();
			}
			byte[] bytes = MonthlyRewardPaymentService.getMonthlyRewardStatisticsReportBytes(regionName, regionId, sectorId, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "MonthlyRewardReportStatstics";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(MonthlyRewardReport.class, e, "MonthlyRewardReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		try {
			regionId = null;
			sectorId = null;
			sectorsList = new ArrayList<DepartmentData>();
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
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

	public List<DepartmentData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<DepartmentData> regionsList) {
		this.regionsList = regionsList;
	}

	public List<DepartmentData> getSectorsList() {
		return sectorsList;
	}

	public void setSectorsList(List<DepartmentData> sectorsList) {
		this.sectorsList = sectorsList;
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
}
