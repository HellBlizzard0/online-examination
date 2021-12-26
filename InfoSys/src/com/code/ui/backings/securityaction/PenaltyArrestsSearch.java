package com.code.ui.backings.securityaction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securitymission.PenaltyArrestData;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityaction.PenaltyArrestService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "penaltyArrestsSearch")
@ViewScoped
public class PenaltyArrestsSearch extends BaseBacking implements Serializable {
	private Date fromDate;
	private Date toDate;
	private Date arrestEndDate;
	private Long employeeId;
	private String employeeName;
	private Integer period;
	private List<PenaltyArrestData> penaltyArrestList;

	/**
	 * 
	 */
	public PenaltyArrestsSearch() {
		init();
	}

	/**
	 * Initialize Search Parameters
	 */
	public void init() {
		fromDate = null;
		toDate = null;
		arrestEndDate = null;
		employeeId = null;
		period = null;
		employeeName = null;
	}

	/**
	 * Search penalty arrest
	 */
	public void searchPenaltyArrests() {
		try {
			penaltyArrestList = PenaltyArrestService.getPenaltyArrest(null, employeeId, fromDate, toDate, arrestEndDate, period);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Start arrest
	 * 
	 * @param penaltyArrest
	 */
	public void startArrest(PenaltyArrestData penaltyArrest) {
		try {
			PenaltyArrestService.startPenaltyArrest(penaltyArrest, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PenaltyArrestsSearch.class, e, "PenaltyArrestsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Validate End Penalty Arrest to know if the end date comes or not
	 * 
	 * @param penaltyArrestData
	 */
	public boolean checkArrestEndDate(PenaltyArrestData penaltyArrestData) {
		Date gregEntryDate = HijriDateService.hijriToGregDate(penaltyArrestData.getEntryDate());
		Calendar c = Calendar.getInstance();
		c.setTime(gregEntryDate);
		c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(penaltyArrestData.getEntryTime().substring(0, 2)));
		c.set(Calendar.MINUTE, Integer.valueOf(penaltyArrestData.getEntryTime().substring(3, 5)));
		c.add(Calendar.HOUR_OF_DAY, penaltyArrestData.getArrestPeriod());
		Date now = new Date();
		if (c.getTime().after(now)) {
			return false;
		}
		return true;
	}

	/**
	 * End arrest
	 * 
	 * @param penaltyArrest
	 */
	public void endArrest(PenaltyArrestData penaltyArrest) {
		try {
			PenaltyArrestService.endPenaltyArrest(penaltyArrest, loginEmpData);
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PenaltyArrestsSearch.class, e, "PenaltyArrestsSearch");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * View Penalty Arrest
	 * 
	 * @return page to be directed to
	 */
	public String viewPenaltyArrest(PenaltyArrestData penaltyArrest) {
		getRequest().setAttribute("mode", penaltyArrest);
		return NavigationEnum.PENALTY_ARREST.toString();
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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getArrestEndDate() {
		return arrestEndDate;
	}

	public void setArrestEndDate(Date arrestEndDate) {
		this.arrestEndDate = arrestEndDate;
	}

	public List<PenaltyArrestData> getPenaltyArrestList() {
		return penaltyArrestList;
	}

	public void setPenaltyArrestList(List<PenaltyArrestData> penaltyArrestList) {
		this.penaltyArrestList = penaltyArrestList;
	}
}
