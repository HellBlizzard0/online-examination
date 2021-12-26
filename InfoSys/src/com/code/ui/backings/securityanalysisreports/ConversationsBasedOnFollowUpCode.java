package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationBasedOnCode")
@ViewScoped
public class ConversationsBasedOnFollowUpCode extends BaseBacking implements Serializable {
	private Date fromDate;
	private Date toDate;
	private String followUpCode;

	/**
	 * Reset report parameter
	 */
	public void reset() {
		followUpCode = null;
		fromDate = null;
		toDate = null;
	}

	/**
	 * Print report
	 */
	public void printReport() {
		try {
			if (fromDate == null)
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_fromDate", "ar") });
			if (toDate == null)
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_toDate", "ar") });
			if (followUpCode == null)
				throw new BusinessException("error_mandatoryFields", new Object[] { getParameterizedMessage("label_followUp", "ar") });
			FollowUpData followUpData = FollowUpService.getFollowUpDataByCode(followUpCode);
			byte[] bytes = ConversationService.getConversationBasedOnCodeWithinPeriodReportBytes(loginEmpData.getFullName(), followUpCode, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), followUpData.getContactNumber(), followUpData.getAliasName(), followUpData.getGhostType() == 0 ? followUpData.getNonEmployeeName() : followUpData.getEmployeeName(),
					followUpData.getGhostType() == 0 ? followUpData.getNonEmployeeSocialId() : followUpData.getEmployeeSocialId());
			super.print(bytes, "Conversation Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationsBasedOnFollowUpCode.class, e, "ConversationsBasedOnFollowUpCode");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
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

	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
	}

}
