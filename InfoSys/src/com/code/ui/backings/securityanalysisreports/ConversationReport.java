package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ConversationResultsEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.services.util.HijriDateService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationReport")
@ViewScoped
public class ConversationReport extends BaseBacking implements Serializable {
	private Long followUpId;
	private String selectedFollowUpCode;
	private String followUpCode;
	private Date fromDate;
	private Date toDate;
	private Integer conversationResult;
	private Long domainIdChatAction;
	private List<SetupDomain> chatActionsDomainList;

	/**
	 * Default Constructor
	 */
	public ConversationReport() {
		try {
			chatActionsDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CHAT_ACTIONS.getCode());
			reset();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Reset report parameter
	 */
	public void reset() {
		followUpId = null;
		selectedFollowUpCode = null;
		followUpCode = null;
		conversationResult = null;
		fromDate = null;
		toDate = null;
		domainIdChatAction = null;
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
			byte[] bytes = ConversationService.getConversationDuringPeriodReportBytes(followUpId, followUpCode, conversationResult, HijriDateService.getHijriDateString(fromDate), HijriDateService.getHijriDateString(toDate), domainIdChatAction, loginEmpData.getFullName());
			super.print(bytes, "Conversation Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationReport.class, e, "ConversationReport");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	public String getSelectedFollowUpCode() {
		return selectedFollowUpCode;
	}

	public void setSelectedFollowUpCode(String selectedFollowUpCode) {
		this.selectedFollowUpCode = selectedFollowUpCode;
	}

	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
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

	public Integer getConversationResult() {
		return conversationResult;
	}

	public void setConversationResult(Integer conversationResult) {
		this.conversationResult = conversationResult;
	}

	public Long getDomainIdChatAction() {
		return domainIdChatAction;
	}

	public void setDomainIdChatAction(Long domainIdChatAction) {
		this.domainIdChatAction = domainIdChatAction;
	}

	public List<SetupDomain> getChatActionsDomainList() {
		return chatActionsDomainList;
	}

	public void setChatActionsDomainList(List<SetupDomain> chatActionsDomainList) {
		this.chatActionsDomainList = chatActionsDomainList;
	}

	public Integer getConvNoResultType() {
		return ConversationResultsEnum.NO_RESULTS_EXISTS.getCode();
	}

	public Integer getConvPosResultType() {
		return ConversationResultsEnum.POSITIVE.getCode();
	}

	public Integer getConvNegResultType() {
		return ConversationResultsEnum.NEGATIVE.getCode();
	}
}