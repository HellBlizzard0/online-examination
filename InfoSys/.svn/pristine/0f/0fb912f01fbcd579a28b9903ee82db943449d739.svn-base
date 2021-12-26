package com.code.ui.backings.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.exceptions.BusinessException;
import com.code.services.infosys.finance.FinanceAccountsService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "financeSupportReport")
@ViewScoped
public class FinanceSupportReport extends BaseBacking implements Serializable {
	private List<Long> financalYears;
	private Date fromDate;
	private Date toDate;
	private Long selectedYear;

	public FinanceSupportReport() {
		super();
		init();
		try {
			financalYears = FinanceAccountsService.getAllFinYears();
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
		if (fromDate == null || toDate == null || selectedYear == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		try {
			byte[] bytes = FinanceAccountsService.getFinanceSupportReportBytes(selectedYear, fromDate, toDate, loginEmpData.getFullName());
			String reportName = "FinanceSupportReport";
			super.print(bytes, reportName);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FinanceSupportReport.class, e, "FinanceSupportReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Reset Search Fields
	 */
	public void reset() {
		try {
			fromDate = HijriDateService.getHijriSysDate();
			toDate = HijriDateService.getHijriSysDate();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public List<Long> getFinancalYears() {
		return financalYears;
	}

	public void setFinancalYears(List<Long> financalYears) {
		this.financalYears = financalYears;
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

	public Long getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(Long selectedYear) {
		this.selectedYear = selectedYear;
	}
}
