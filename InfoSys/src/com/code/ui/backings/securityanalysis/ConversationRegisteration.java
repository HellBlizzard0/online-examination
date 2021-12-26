package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FlowEvent;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.ConversationPartyData;
import com.code.dal.orm.securityanalysis.ReferralData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.securityanalysis.ReminderData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ConversationResultsEnum;
import com.code.enums.ConversationTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.ReferralTypeValuesEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.enums.ServiceCodesEnum;
import com.code.enums.SessionAttributesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.attach.AttachmentServiceCallBack;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationRegisteration")
@ViewScoped
public class ConversationRegisteration extends BaseBacking implements Serializable {
	private int rowsCount = 10;
	private ConversationData conversationData;
	private ConversationPartyData conversationPartyData;
	private ReferralData referralData;
	private ReminderData reminderData;
	private int pageMode;
	private Boolean viewConversationSummary;
	private Set<Long> conversationPartyIdsSet;
	private List<ReferralData> referralDataList;
	private List<SetupDomain> actionTypeList;
	private List<ConversationPartyData> conversationPartyDataList;
	private List<ConversationPartyData> deletedConversationPartyDataList;
	private List<ReminderData> reminderDataList;
	private String fileArchivingParam;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private String selectedDownloadFileId;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String uploadFlag = getParameterizedMessage("label_no");
	private boolean allowFollowUpUpdate;
	private List<RegionData> regionsList;

	public ConversationRegisteration() {
		super();
		this.init();
		try {
			pageMode = 2;
			if (getRequest().getAttribute("mode") != null) {
				pageMode = Integer.parseInt(getRequest().getAttribute("mode").toString()); // view mode
			} else {
				allowFollowUpUpdate = true;
			}
			if (getRequest().getAttribute("conversationId") != null) {
				conversationData = ConversationService.getConversationsByConversationId((Long) getRequest().getAttribute("conversationId"));
				if (conversationData.getConversationSummary() != null) {
					viewConversationSummary = true;
				}
				conversationPartyDataList = ConversationService.getConversationsPartiesByConversationId(conversationData.getId());

				for (ConversationPartyData conversationPartyData : conversationPartyDataList) {
					conversationPartyIdsSet.add(Long.parseLong(conversationPartyData.getContactNumber()));
				}
				referralDataList = ConversationService.getReferralsByConversationId(conversationData.getId());
				reminderDataList = ConversationService.getRemindersByConversationId(conversationData.getId());
			} else {

			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationRegisteration.class, e, "ConversationRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	public void init() {
		try {
			conversationData = new ConversationData();
			conversationPartyData = new ConversationPartyData();
			conversationPartyDataList = new ArrayList<ConversationPartyData>();
			deletedConversationPartyDataList = new ArrayList<ConversationPartyData>();
			referralDataList = new ArrayList<ReferralData>();
			reminderDataList = new ArrayList<ReminderData>();
			reminderData = new ReminderData();
			conversationPartyIdsSet = new HashSet<Long>();
			referralData = new ReferralData();
			actionTypeList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CHAT_ACTIONS.getCode());
			conversationData.setConversationType(0);
			conversationData.setConversationResult(0);
			regionsList = FollowUpService.getAllRegionData();
			if (conversationData.getId() == null)
				uploadFlag = getParameterizedMessage("label_yes");
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Insert Conversation Party into conversationPartyDataList
	 */
	public void insertConversationParty() {

		try {
			String followUpContactNumber = FollowUpService.getFollowUpDataById(conversationData.getFollowUpId()).getContactNumber();
			if (followUpContactNumber.equals(conversationPartyData.getContactNumber().toString())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_addConversationPartyEqualFollowUpContactNumber"));
				return;
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));

		}
		String contactNum = conversationPartyData.getContactNumber();
		
		if (conversationPartyIdsSet.contains(Long.parseLong(contactNum)) ){ // if the employee was already added
			this.setServerSideErrorMessages(getParameterizedMessage("error_contactNumberIsExist"));
		} else {
			conversationPartyData.setContactNumber(contactNum);
			conversationPartyDataList.add(conversationPartyData);
			conversationPartyIdsSet.add(Long.parseLong(conversationPartyData.getContactNumber()));
			conversationPartyData = new ConversationPartyData();
			this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));

		}
	}

	public void insertReminder() {
		// reminderData.setReminderDate(HijriDateService.getHijriDate(reminderData.getReminderDateString()));
		reminderDataList.add(reminderData);
		reminderData = new ReminderData();
	}

	/**
	 * Insert Referral Data into referralDataList
	 * 
	 */
	public void insertReferral() {

		for (ReferralData ref : referralDataList) {
			if (ref.getEmployeeNumber() != null && referralData.getEmployeeNumber() != null && ref.getEmployeeNumber().equals(referralData.getEmployeeNumber())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_onlyOneReferralToSameEmployee"));
				return;
			}

			if (ref.getDomainIdReferralTypeDesc().equals(ReferralTypeValuesEnum.REDIRECT.getCode()) && referralData.getDomainIdReferralTypeDesc().equals(ReferralTypeValuesEnum.REDIRECT.getCode())) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_onlyOneRedirectPerConversation"));
				return;
			}
		}
		referralDataList.add(referralData);
		referralData = new ReferralData();
	}

	/**
	 * Delete ReferralData from the ReferralDataTable
	 * 
	 * @param referralData
	 */
	public void deleteReferralData(ReferralData referralData) {
		if (referralData.getId() == null) {
			referralDataList.remove(referralData);
		}
	}

	/**
	 * Delete ConversationParty from the table
	 * 
	 * @param ConversationPartyData
	 */
	public void deleteConversationParty(ConversationPartyData conversationPartyData) {
		conversationPartyDataList.remove(conversationPartyData);
		if (conversationPartyData.getId() != null) {
			deletedConversationPartyDataList.add(conversationPartyData);
		}
	}

	/**
	 * change conversation result view Conversation Summary in Positive Case
	 */
	public void changeConversationResult() {
		if (conversationData.getConversationResult() != null && conversationData.getConversationResult() == getConversationPositiveValue()) {
			viewConversationSummary = true;
		} else {
			viewConversationSummary = false;
			conversationData.setConversationSummary(null);
		}
	}

	/**
	 * next button press
	 * 
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) {
		try {
			if (event.getNewStep().equals("convDetailsDataTabId")) {
				ConversationService.validateConversation(conversationData);
			} else if (event.getNewStep().equals("convProcessDataTabId")) {
				if (conversationPartyDataList != null && conversationPartyDataList.isEmpty()) {
					throw new BusinessException("error_conversationPartiesMandatory");
				}
				if (conversationData.getConversationDetails() == null || conversationData.getConversationDetails().trim().isEmpty()) {
					throw new BusinessException("error_conversationDetailsMandatory");
				}
				if (conversationData.getConversationResult() == null) {
					throw new BusinessException("error_conversationResultMandatory");
				}
			}
			return event.getNewStep();
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return event.getOldStep();
		}
	}

	public String saveConversationData() {
		try {
			if (conversationData.getId() == null) {
				ConversationService.saveConversation(conversationData, loginEmpData, conversationPartyDataList, referralDataList, reminderDataList);
			} else {
				ConversationService.updateConversation(conversationData, loginEmpData, conversationPartyDataList, deletedConversationPartyDataList, referralDataList, reminderDataList);
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		if (uploadFlag.equals(getParameterizedMessage("label_yes"))) {
			getRequest().setAttribute(SessionAttributesEnum.FILE_ARCHIVING_PARAM.getCode(), EntityNameEnum.CONVERSATION.getCode() + "_" + conversationData.getId());
		}
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * Prepare Upload Parameters
	 * 
	 * @param conversationId
	 */
	public void getUploadParam(long conversationId) {
		try {
			String requestURL = getRequest().getRequestURL() + "";
			String url = requestURL.replace(getRequest().getServletPath(), "");
			String serviceURL = url + AttachmentServiceCallBack.SERVICE_URL;

			fileArchivingParam = EncryptionUtil.encryptSymmetrically("code=" + ServiceCodesEnum.INFO_SYS.getCode() + "&token=" + EntityNameEnum.CONVERSATION.getCode() + "_" + conversationId + "&serviceurl=" + serviceURL);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(ConversationRegisteration.class, e, "ConversationRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Prepare Download parameters
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(ConversationRegisteration.class, e, "ConversationRegisteration");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get Attachments Ids and prepare for downloading
	 * 
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long conversationId) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(EntityNameEnum.CONVERSATION.getCode(), conversationId);
			openDownloadPopupFlag = false;
			openDownloadDialogueFlag = false;
			if (attachmentList.isEmpty()) {
				this.setServerSideErrorMessages(getParameterizedMessage("error_noAttachments"));
			} else if (attachmentList.size() == 1) {
				openDownloadPopupFlag = true;
				downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentList.get(0).getAttachmentId());
			} else {
				openDownloadDialogueFlag = true;
			}
		} catch (Exception e) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	public int getConversationDirectType() {
		return ConversationTypeEnum.DIRECT.getCode();
	}

	public int getConversationInjectType() {
		return ConversationTypeEnum.INJECTION.getCode();
	}

	public int getConversationNoResultValue() {
		return ConversationResultsEnum.NO_RESULTS_EXISTS.getCode();
	}

	public int getConversationNegativeValue() {
		return ConversationResultsEnum.NEGATIVE.getCode();
	}

	public int getConversationPositiveValue() {
		return ConversationResultsEnum.POSITIVE.getCode();
	}

	public ConversationPartyData getConversationPartyData() {
		return conversationPartyData;
	}

	public void setConversationPartyData(ConversationPartyData conversationPartyData) {
		this.conversationPartyData = conversationPartyData;
	}

	public ConversationData getConversationData() {
		return conversationData;
	}

	public void setConversationData(ConversationData conversationData) {
		this.conversationData = conversationData;
	}

	public Set<Long> getConversationPartyIdsSet() {
		return conversationPartyIdsSet;
	}

	public void setConversationPartyIdsSet(Set<Long> conversationPartyIdsSet) {
		this.conversationPartyIdsSet = conversationPartyIdsSet;
	}

	public List<ConversationPartyData> getConversationPartyDataList() {
		return conversationPartyDataList;
	}

	public void setConversationPartyDataList(List<ConversationPartyData> conversationPartyDataList) {
		this.conversationPartyDataList = conversationPartyDataList;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public List<SetupDomain> getActionTypeList() {
		return actionTypeList;
	}

	public void setActionTypeList(List<SetupDomain> actionTypeList) {
		this.actionTypeList = actionTypeList;
	}

	public ReferralData getReferralData() {
		return referralData;
	}

	public void setReferralData(ReferralData referralData) {
		this.referralData = referralData;
	}

	public List<ReferralData> getReferralDataList() {
		return referralDataList;
	}

	public void setReferralDataList(List<ReferralData> referralDataList) {
		this.referralDataList = referralDataList;
	}

	public Boolean getViewConversationSummary() {
		return viewConversationSummary;
	}

	public void setViewConversationSummary(Boolean viewConversationSummary) {
		this.viewConversationSummary = viewConversationSummary;
	}

	public String getFileArchivingParam() {
		return fileArchivingParam;
	}

	public void setFileArchivingParam(String fileArchivingParam) {
		this.fileArchivingParam = fileArchivingParam;
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

	public String getSelectedDownloadFileId() {
		return selectedDownloadFileId;
	}

	public void setSelectedDownloadFileId(String selectedDownloadFileId) {
		this.selectedDownloadFileId = selectedDownloadFileId;
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

	public String getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public String getBoolServerUploadPath() {
		return InfoSysConfigurationService.getBoolServerUploadPath();
	}

	public String getBoolServerDownloadPath() {
		return InfoSysConfigurationService.getBoolServerDownloadPath();
	}

	public int getPageMode() {
		return pageMode;
	}

	public void setPageMode(int pageMode) {
		this.pageMode = pageMode;
	}

	public ReminderData getReminderData() {
		return reminderData;
	}

	public void setReminderData(ReminderData reminderData) {
		this.reminderData = reminderData;
	}

	public List<ReminderData> getReminderDataList() {
		return reminderDataList;
	}

	public void setReminderDataList(List<ReminderData> reminderDataList) {
		this.reminderDataList = reminderDataList;
	}

	public List<ConversationPartyData> getDeletedConversationPartyDataList() {
		return deletedConversationPartyDataList;
	}

	public void setDeletedConversationPartyDataList(List<ConversationPartyData> deletedConversationPartyDataList) {
		this.deletedConversationPartyDataList = deletedConversationPartyDataList;
	}

	public boolean isAllowFollowUpUpdate() {
		return allowFollowUpUpdate;
	}

	public void setAllowFollowUpUpdate(boolean allowFollowUpUpdate) {
		this.allowFollowUpUpdate = allowFollowUpUpdate;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}
}