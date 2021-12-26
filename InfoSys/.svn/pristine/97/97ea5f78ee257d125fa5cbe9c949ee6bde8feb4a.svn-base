package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.enums.FlagsEnum;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.util.CommonService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "labCheckStatsReport")
@ViewScoped
public class LabCheckStatsReport extends BaseBacking implements Serializable {
	private Date startDate;
	private Date endDate;
	private int pageMode;

	public LabCheckStatsReport() {
		super();
		init();
		if (getRequest().getParameter("pageMode") != null) {
			pageMode = (Integer.valueOf(getRequest().getParameter("pageMode")));
		}
	}

	/**
	 * Reset report parameter
	 */
	public void reset() {
		startDate = null;
		endDate = null;
	}

	/**
	 * Print report
	 */
	public void printReport() {
		String startDateFlag = FlagsEnum.OFF.getCode() + "";
		String endDateFlag = FlagsEnum.OFF.getCode() + "";
		if (startDate == null) {
			startDateFlag = FlagsEnum.ALL.getCode() + "";
		}
		if (endDate == null) {
			endDateFlag = FlagsEnum.ALL.getCode() + "";
		}
		try {
			byte[] bytes = CommonService.getLabCheckStatsReportBytes(HijriDateService.getHijriDateString(startDate), HijriDateService.getHijriDateString(endDate), startDateFlag, endDateFlag, pageMode, loginEmpData.getFullName());
			super.print(bytes, "LabCheckStatsReport");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(LabCheckStatsReport.class, e, "LabCheckStatsReport");
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

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

}