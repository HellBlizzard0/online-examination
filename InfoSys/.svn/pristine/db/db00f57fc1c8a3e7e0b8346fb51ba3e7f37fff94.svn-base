package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.attach.Attachment;
import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.FollowUpResultData;
import com.code.dal.orm.securityanalysis.LetterData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ClassesEnum;
import com.code.enums.ConversationResultsEnum;
import com.code.enums.ConversationTypeEnum;
import com.code.enums.EntityNameEnum;
import com.code.enums.GhostTypeEnum;
import com.code.enums.LetterTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.attach.AttachmentService;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.infosys.securityanalysis.LetterService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;
import com.code.ui.util.EncryptionUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "followUpDetails")
@ViewScoped
public class FollowUpDetails extends BaseBacking implements Serializable {
	private FollowUpData followUp;
	private List<ConversationData> conversationList;
	private List<FollowUpResultData> followUpResultList;
	private List<LetterData> letterList;
	private List<SetupDomain> equipmentDomainList;
	private List<SetupDomain> dataSourceDomainList;
	private List<SetupDomain> casesTypeDomainList;
	private List<RegionData> regionsList;
	private List<DepartmentData> sectorsList;
	private List<SetupDomain> followUpResultTypesList;
	private List<SetupDomain> followUpTransTypesList;
	private String downloadFileParamId;
	private List<Attachment> attachmentList;
	private boolean openDownloadPopupFlag;
	private boolean openDownloadDialogueFlag;
	private String boolServerDownloadPath;

	/**
	 * Constructor
	 */
	public FollowUpDetails() {
		super();
		init();
		followUpInitilize();
	}

	/**
	 * followUpInitilize
	 */
	public void followUpInitilize() {
		try {
			if (getRequest().getAttribute("followUpId") != null) {
				boolServerDownloadPath = InfoSysConfigurationService.getBoolServerDownloadPath();
				followUp = FollowUpService.getFollowUpDataById((Long) getRequest().getAttribute("followUpId"));
				followUp.setContactNumber(followUp.getContactNumber().substring(3));
				conversationList = ConversationService.getConversationsByFollowUpId((Long) getRequest().getAttribute("followUpId"));
				followUpResultList = FollowUpService.getFollowUpResultsByFollowUpId((Long) getRequest().getAttribute("followUpId"));
				letterList = LetterService.getLetters((Long) getRequest().getAttribute("followUpId"), null);
				equipmentDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(ClassesEnum.EQUIPMENT_TYPES.getCode());
				dataSourceDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.DATA_SOURCES.getCode());
				casesTypeDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
				regionsList = FollowUpService.getAllRegionData();
				followUpTransTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.TRANS_TYPES.getCode());
				followUpResultTypesList = SetupService.getDomains(SecurityAnalysisClassesEnum.FOLLOW_RESULTS.getCode());
			}

		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(FollowUpDetails.class, e, "FollowUpDetails");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

	}

	/**
	 * View Conversation Details
	 * 
	 * @param conversationData
	 * @return
	 */
	public String viewConversationDetail(ConversationData conversationData) {
		getRequest().setAttribute("conversationId", conversationData.getId());
		getRequest().setAttribute("mode", 1);
		return NavigationEnum.VIEW_CONVERSATION.toString();
	}

	/**
	 * Edit Conversation Details
	 * 
	 * @param conversationData
	 * @return
	 */
	public String editConversationDetail(ConversationData conversationData) {
		getRequest().setAttribute("conversationId", conversationData.getId());
		getRequest().setAttribute("mode", 2);
		return NavigationEnum.EDIT_CONVERSATION.toString();
	}

	/**
	 * Constitute Download parameter which contains contentId (attachmentId)
	 * 
	 * @param attachmentId
	 */
	public void getDownloadParam(String attachmentId) {
		try {
			downloadFileParamId = EncryptionUtil.encryptSymmetrically("contentId=" + attachmentId);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		} catch (Exception e) {
			   Log4j.traceErrorException(FollowUpDetails.class, e, "FollowUpDetails");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get attachment list related to the entity id passed to the method then check if there's no records then show message indicating no daya else if it's only one record , open the download popup directly , otherwise open a dialogue shows file names and make the user select which one to download then open the download popup with the choosen contentId (attachmentId)
	 * 
	 * @param
	 * @throws BusinessException
	 */
	public void getDownloadedAttachmentsIdList(long id, String attachmentType) throws BusinessException {
		try {
			attachmentList = AttachmentService.getAttachments(attachmentType, id);
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

	public FollowUpData getFollowUp() {
		return followUp;
	}

	public void setFollowUp(FollowUpData followUp) {
		this.followUp = followUp;
	}

	public List<ConversationData> getConversationList() {
		return conversationList;
	}

	public void setConversationList(List<ConversationData> conversationList) {
		this.conversationList = conversationList;
	}

	public List<FollowUpResultData> getFollowUpResultList() {
		return followUpResultList;
	}

	public void setFollowUpResultList(List<FollowUpResultData> followUpResultList) {
		this.followUpResultList = followUpResultList;
	}

	public List<LetterData> getLetterList() {
		return letterList;
	}

	public void setLetterList(List<LetterData> letterList) {
		this.letterList = letterList;
	}

	public List<SetupDomain> getEquipmentDomainList() {
		return equipmentDomainList;
	}

	public void setEquipmentDomainList(List<SetupDomain> equipmentDomainList) {
		this.equipmentDomainList = equipmentDomainList;
	}

	public List<SetupDomain> getDataSourceDomainList() {
		return dataSourceDomainList;
	}

	public void setDataSourceDomainList(List<SetupDomain> dataSourceDomainList) {
		this.dataSourceDomainList = dataSourceDomainList;
	}

	public List<SetupDomain> getCasesTypeDomainList() {
		return casesTypeDomainList;
	}

	public void setCasesTypeDomainList(List<SetupDomain> casesTypeDomainList) {
		this.casesTypeDomainList = casesTypeDomainList;
	}

	public List<RegionData> getRegionsList() {
		return regionsList;
	}

	public void setRegionsList(List<RegionData> regionsList) {
		this.regionsList = regionsList;
	}

	public List<DepartmentData> getSectorsList() {
		return sectorsList;
	}

	public void setSectorsList(List<DepartmentData> sectorsList) {
		this.sectorsList = sectorsList;
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

	public Integer getEmployeeType() {
		return GhostTypeEnum.EMPLOYEE.getCode();
	}

	public Integer getNonEmployeeType() {
		return GhostTypeEnum.NON_EMPLOYEE.getCode();
	}

	public Integer getConvDirectType() {
		return ConversationTypeEnum.DIRECT.getCode();
	}

	public Integer getConvInjectType() {
		return ConversationTypeEnum.INJECTION.getCode();
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

	public String getConversationEntityType() {
		return EntityNameEnum.CONVERSATION.getCode();
	}

	public String getResultEntityType() {
		return EntityNameEnum.FOLLOW_UP_RESULT.getCode();
	}

	public String getLetterEntityType() {
		return EntityNameEnum.LETTER.getCode();
	}

	public Integer getLetterIncomingType() {
		return LetterTypeEnum.Incoming.getCode();
	}

	public Integer getLetterIssuedType() {
		return LetterTypeEnum.Issued.getCode();
	}

	public List<SetupDomain> getFollowUpTransTypesList() {
	    return followUpTransTypesList;
	}

	public void setFollowUpTransTypesList(List<SetupDomain> followUpTransTypesList) {
	    this.followUpTransTypesList = followUpTransTypesList;
	}

	public List<SetupDomain> getFollowUpResultTypesList() {
	    return followUpResultTypesList;
	}

	public void setFollowUpResultTypesList(List<SetupDomain> followUpResultTypesList) {
	    this.followUpResultTypesList = followUpResultTypesList;
	}

}
