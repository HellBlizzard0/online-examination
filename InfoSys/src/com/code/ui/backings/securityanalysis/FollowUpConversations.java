package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.enums.EntityNameEnum;
import com.code.enums.NavigationEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpConversations")
@ViewScoped
public class FollowUpConversations extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private int page = 1;
	private FollowUpData followUpData;
	private List<ConversationData> conversationData;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;

	/**
	 * Constructor
	 */
	public FollowUpConversations() {
		super();
		try {
			if (getRequest().getAttribute("followUp") != null) {
				followUpData = (FollowUpData) getRequest().getAttribute("followUp");
				conversationData = ConversationService.getConversationsByFollowUpId(followUpData.getId());
				Long currentConversationId = (Long) getRequest().getAttribute("currentConversationId");
				if (currentConversationId != null) {
					for (int i = 0; i < conversationData.size(); i++) {
						if (currentConversationId.equals(conversationData.get(i).getId())) {
							conversationData.remove(i);
						}
					}
				}
			}
		} catch (Exception e) {
			conversationData = new ArrayList<ConversationData>();
		}
	}

	/**
	 * Search for information
	 */
	public void searchInfo() {
		try {
			conversationData.clear();
			conversationData = ConversationService.getConversationsByFollowUpId(followUpData.getId());

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * show Conversation
	 * 
	 * @return
	 */
	public String showConversationDetails(Long conversationDataId) {
		getRequest().setAttribute("conversationId", conversationDataId);
		getRequest().setAttribute("mode", 1);
		return NavigationEnum.VIEW_CONVERSATION.toString();
	}

	/**
	 * edit Conversation
	 * 
	 * @return
	 */
	public String editConversationDetails(Long conversationDataId) {
		getRequest().setAttribute("conversationId", conversationDataId);
		getRequest().setAttribute("mode", 2);
		return NavigationEnum.EDIT_CONVERSATION.toString();
	}

	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(FollowUpConversations.class, e, "FollowUpConversations");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long conversationId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.CONVERSATION.getCode(), conversationId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	// setters and getters
	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public FollowUpData getFollowUpData() {
		return followUpData;
	}

	public void setFollowUpData(FollowUpData followUpData) {
		this.followUpData = followUpData;
	}

	public List<ConversationData> getConversationData() {
		return conversationData;
	}

	public void setConversationData(List<ConversationData> conversationData) {
		this.conversationData = conversationData;
	}

	public String getDownloadFileParamId() {
		return downloadFileParamId;
	}

	public void setDownloadFileParamId(String downloadFileParamId) {
		this.downloadFileParamId = downloadFileParamId;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public boolean isOpenDownloadPopupFlag() {
		return openDownloadPopupFlag;
	}

	public void setOpenDownloadPopupFlag(boolean openDownloadPopupFlag) {
		this.openDownloadPopupFlag = openDownloadPopupFlag;
	}

	public boolean isOpenDownloadDialogueFlag() {
		return openDownloadDialogueFlag;
	}

	public void setOpenDownloadDialogueFlag(boolean openDownloadDialogueFlag) {
		this.openDownloadDialogueFlag = openDownloadDialogueFlag;
	}

	public String getBoolServerDownloadPath() {
		return boolServerDownloadPath;
	}

	public void setBoolServerDownloadPath(String boolServerDownloadPath) {
		this.boolServerDownloadPath = boolServerDownloadPath;
	}
}