package com.code.ui.backings.securityanalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.ConversationPartyData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.GuidanceRequestData;
import com.code.dal.orm.securityanalysis.ReferralData;
import com.code.dal.orm.securityanalysis.RegionData;
import com.code.dal.orm.securityanalysis.ReminderData;
import com.code.dal.orm.setup.DepartmentData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.ConversationTypeEnum;
import com.code.enums.DepartmentTypeEnum;
import com.code.enums.GhostTypeEnum;
import com.code.enums.NavigationEnum;
import com.code.enums.RegionTypeEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.services.setup.DepartmentService;
import com.code.services.setup.SetupService;
import com.code.services.workflow.securityanalysis.SecurityAnalysisWorkFlow;
import com.code.ui.backings.base.WFBaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "guidanceReq")
@ViewScoped
public class ConversationGuidanceRequest extends WFBaseBacking implements Serializable {
	private ConversationData conversation;
	private FollowUpData followUp;
	private GuidanceRequestData guidanceRequest;
	private List<ConversationPartyData> conversationPartyList;
	private List<ReminderData> reminderList;
	private List<ReferralData> referralList;
	private List<SetupDomain> equipmentDomainList;
	private List<SetupDomain> dataSourceDomainList;
	private List<SetupDomain> casesTypeDomainList;
	private List<SetupDomain> chatActionsDomainList;
	private List<SetupDomain> referralDomainList;
	private List<SetupDomain> guidanceTypesDomainList;
	private List<RegionData> regionsList;
	private List<DepartmentData> sectorsList;

	/**
	 * Constructor
	 */
	public ConversationGuidanceRequest() {
		super();
		init();
		guidanceInitilize();
	}

	/**
	 * followUpInitilize
	 */
	public void guidanceInitilize() {
		try {
			if (currentTask != null) {
				conversation = ConversationService.getConversationsByInstanceId(instance.getInstanceId());
				if (role.equals("Notification") || role.equals("History")) {
					guidanceRequest = ConversationService.getGuidanceRequestByConversationId(conversation.getId());
				} else {
					guidanceRequest = new GuidanceRequestData();
				}
				followUp = FollowUpService.getFollowUpDataById(conversation.getFollowUpId());
				followUp.setContactNumber(followUp.getContactNumber().substring(3));
				conversationPartyList = ConversationService.getConversationsPartiesByConversationId(conversation.getId());
				referralList = ConversationService.getReferralsByConversationId(conversation.getId());
				equipmentDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.EQUIPMENT_TYPES.getCode());
				dataSourceDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.DATA_SOURCES.getCode());
				casesTypeDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CASES_TYPES.getCode());
				chatActionsDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.CHAT_ACTIONS.getCode());
				referralDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.REFERRAL_TYPE.getCode());
				guidanceTypesDomainList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.GUIDANCE_TYPES.getCode());
				regionsList = FollowUpService.getAllRegionData();
				getRegionSectors();
			}
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationGuidanceRequest.class, e, "ConversationGuidanceRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Get Region sectors by RegionId
	 */
	public void getRegionSectors() {
		try {
			followUp.setSectorId(null);
			RegionData region = getRegionType(followUp.getRegionId());
			if (followUp.getRegionId() != null && region != null) {
				sectorsList = DepartmentService.getDepartmentsByRegionIdAndType(region.getRegionId(), null, null, DepartmentTypeEnum.SECTOR.getCode());
			} else {
				sectorsList = new ArrayList<DepartmentData>();
			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}catch (Exception e) {
			   Log4j.traceErrorException(ConversationGuidanceRequest.class, e, "ConversationGuidanceRequest");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }
	}

	/**
	 * Check internal region or external region
	 * 
	 * @param regionId
	 * @return
	 */
	private RegionData getRegionType(Long regionId) {
		for (RegionData region : regionsList) {
			if (region.getId().equals(regionId)) {
				if (region.getRegionType() == RegionTypeEnum.INTERNAL.getCode())
					return region;
				else
					return null;
			}
		}
		return null;
	}

	/**
	 * Open Reminder Dialog
	 */
	public void openReminderDialog() {
		try {
			reminderList = ConversationService.getRemindersByConversationId(conversation.getId());
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	/**
	 * Save Guidance Request
	 */
	public String saveGuidanceRequest() {
		try {
			SecurityAnalysisWorkFlow.doApproveGuidanceRequest(currentTask, guidanceRequest, conversation);
		} catch (BusinessException e) {
			super.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
			return null;
		}
		this.setServerSideSuccessMessages(getParameterizedMessage("notify_successOperation"));
		return NavigationEnum.INBOX.toString();
	}

	/**
	 * View Conversation Details
	 * 
	 * @param conversationData
	 * @return
	 */
	public String viewOtherConversations() {
		getRequest().setAttribute("followUp", followUp);
		getRequest().setAttribute("currentConversationId", conversation.getId());
		return NavigationEnum.VIEW_FOLLOW_UP_CONVERSATIONS.toString();
	}

	public ConversationData getConversation() {
		return conversation;
	}

	public void setConversation(ConversationData conversation) {
		this.conversation = conversation;
	}

	public FollowUpData getFollowUp() {
		return followUp;
	}

	public GuidanceRequestData getGuidanceRequest() {
		return guidanceRequest;
	}

	public void setGuidanceRequest(GuidanceRequestData guidanceRequest) {
		this.guidanceRequest = guidanceRequest;
	}

	public List<ConversationPartyData> getConversationPartyList() {
		return conversationPartyList;
	}

	public void setConversationPartyList(List<ConversationPartyData> conversationPartyList) {
		this.conversationPartyList = conversationPartyList;
	}

	public List<ReminderData> getReminderList() {
		return reminderList;
	}

	public void setReminderList(List<ReminderData> reminderList) {
		this.reminderList = reminderList;
	}

	public List<ReferralData> getReferralList() {
		return referralList;
	}

	public void setReferralList(List<ReferralData> referralList) {
		this.referralList = referralList;
	}

	public void setFollowUp(FollowUpData followUp) {
		this.followUp = followUp;
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

	public List<SetupDomain> getChatActionsDomainList() {
		return chatActionsDomainList;
	}

	public void setChatActionsDomainList(List<SetupDomain> chatActionsDomainList) {
		this.chatActionsDomainList = chatActionsDomainList;
	}

	public List<SetupDomain> getReferralDomainList() {
		return referralDomainList;
	}

	public void setReferralDomainList(List<SetupDomain> referralDomainList) {
		this.referralDomainList = referralDomainList;
	}

	public List<SetupDomain> getGuidanceTypesDomainList() {
		return guidanceTypesDomainList;
	}

	public void setGuidanceTypesDomainList(List<SetupDomain> guidanceTypesDomainList) {
		this.guidanceTypesDomainList = guidanceTypesDomainList;
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
}
