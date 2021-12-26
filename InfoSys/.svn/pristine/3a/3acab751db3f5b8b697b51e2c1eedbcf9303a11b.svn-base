package com.code.ui.backings.securityanalysisreports;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationDetails")
@ViewScoped
public class ConversationDetails extends BaseBacking implements Serializable {
	private Long conversationId;
	private FollowUpData followUp;
	private boolean disableConversationButton;

	/**
	 * Default Constructor
	 */
	public ConversationDetails() {
		conversationId = null;
		disableConversationButton = true;
		followUp = new FollowUpData();
	}

	public void resetFormParameters() throws BusinessException {
		conversationId = null;
		followUp = new FollowUpData();
	}

	public void selectFollowUp() {
		if (followUp.getId() != null) {
			disableConversationButton = false;
			try {
				followUp = FollowUpService.getFollowUpDataById(followUp.getId());
			} catch (BusinessException e) {
				followUp = new FollowUpData();
				e.printStackTrace();
				this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			}catch (Exception e) {
				   Log4j.traceErrorException(ConversationDetails.class, e, "ConversationDetails");
				   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
			  }

		} else
			disableConversationButton = true;
	}

	/**
	 * print Car Rentals
	 */
	public void print() {
		if (conversationId == null || followUp.getId() == null) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_mandatory"));
			return;
		}

		try {
			byte[] bytes = FollowUpService.getConversationDetailsReportBytes(conversationId, followUp.getId(), loginEmpData.getFullName());
			super.print(bytes, "Conversation Details Report");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationDetails.class, e, "ConversationDetails");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public boolean isDisableConversationButton() {
		return disableConversationButton;
	}

	public void setDisableConversationButton(boolean disableConversationButton) {
		this.disableConversationButton = disableConversationButton;
	}

	public FollowUpData getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpData followUp) {
		this.followUp = followUp;
	}
}