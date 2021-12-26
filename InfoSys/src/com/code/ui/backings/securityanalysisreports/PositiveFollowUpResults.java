package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.FlagsEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "positiveFollowUpResults")
@ViewScoped
public class PositiveFollowUpResults extends BaseBacking implements Serializable {
	private List<SetupDomain> followUpResultTypesList;
	private Long resultTypeId, followUpId;
	private Date fromDate, toDate;

	/**
	 * Default Constructor
	 */
	public PositiveFollowUpResults() {
		try {
			followUpResultTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
		this.resultTypeId = (long) FlagsEnum.ALL.getCode();
	}

	public void resetFormParameters() throws BusinessException {
		this.resultTypeId = (long) FlagsEnum.ALL.getCode();
		this.followUpId = null;
		this.fromDate = null;
		this.toDate = null;
	}

	/**
	 * print Car Rentals
	 */
	public void print() {
		if (fromDate == null || toDate == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}
		if (toDate.before(fromDate)) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_fromDateBeforeToDate"));
			return;
		}

		try {
			byte[] bytes = FollowUpService.getPositiveFollowUpResultsReportBytes(followUpId, resultTypeId, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), loginEmpData.getFullName());
			super.print(bytes, "Positive Follow Up Results Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(PositiveFollowUpResults.class, e, "PositiveFollowUpResults");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public List<SetupDomain> getFollowUpResultTypesList() {
		return followUpResultTypesList;
	}

	public void setFollowUpResultTypesList(List<SetupDomain> followUpResultTypesList) {
		this.followUpResultTypesList = followUpResultTypesList;
	}

	public Long getResultTypeId() {
		return resultTypeId;
	}

	public void setResultTypeId(Long resultTypeId) {
		this.resultTypeId = resultTypeId;
	}

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
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

}