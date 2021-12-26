package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.code.dal.orm.securityanalysis.ConversationPartyData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.setup.SetupDomain;
import com.code.enums.GhostTypeEnum;
import com.code.enums.SecurityAnalysisClassesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.setup.SetupService;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "conversationPartyMiniSearch")
@ViewScoped
public class ConversationPartyMiniSearch extends BaseBacking implements Serializable {

	private ConversationPartyData conversationPartyData;
	private List<SetupDomain> networkRoleList;
	private Boolean enable;

	public ConversationPartyMiniSearch() {
		super();

		conversationPartyData = new ConversationPartyData();
		try {
			networkRoleList = SetupService.getSecurityAnalysisDomainsByClassCode(SecurityAnalysisClassesEnum.NETWORK_ROLES.getCode());
			conversationPartyData.setDomainIdNetworkRoles(null);
			conversationPartyData.setDomainIdNetworkRolesDesc(null);
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage(), e.getParams()));
		}
	}

	public void retriveParties() {
		if (conversationPartyData.getContactNumber() != null) {
			enable = true;
			List<FollowUpData> followUpDataList = FollowUpService.getFollowUpDataByContactNumber(conversationPartyData.getContactNumber());
			List<ConversationPartyData> conversationPartyDataList = ConversationService.getConversationParties(conversationPartyData.getContactNumber(), null);

			if (!followUpDataList.isEmpty() && !conversationPartyDataList.isEmpty()) {
				if (followUpDataList.get(0).getFollowUpStartDate().after(conversationPartyDataList.get(0).getRegistraionDate()) || followUpDataList.get(0).getFollowUpStartDate().equals(conversationPartyDataList.get(0).getRegistraionDate())) {
					conversationPartyData.setName(followUpDataList.get(0).getGhostType() == GhostTypeEnum.EMPLOYEE.getCode() ? followUpDataList.get(0).getEmployeeName() : followUpDataList.get(0).getNonEmployeeName());
					conversationPartyData.setFollowUpCode(followUpDataList.get(0).getCode());
				} else {
					conversationPartyData.setName(conversationPartyDataList.get(0).getName());
					conversationPartyData.setFollowUpCode(conversationPartyDataList.get(0).getFollowUpCode());
					conversationPartyData.setDomainIdNetworkRoles(conversationPartyDataList.get(0).getDomainIdNetworkRoles());
					conversationPartyData.setDomainIdNetworkRolesDesc(conversationPartyDataList.get(0).getDomainIdNetworkRolesDesc());
				}
			} else if (!followUpDataList.isEmpty()) {
				conversationPartyData.setName(followUpDataList.get(0).getGhostType() == GhostTypeEnum.EMPLOYEE.getCode() ? followUpDataList.get(0).getEmployeeName() : followUpDataList.get(0).getNonEmployeeName());
				conversationPartyData.setFollowUpCode(followUpDataList.get(0).getCode());
			} else if (!conversationPartyDataList.isEmpty()) {
				conversationPartyData.setName(conversationPartyDataList.get(0).getName());
				conversationPartyData.setFollowUpCode(conversationPartyDataList.get(0).getFollowUpCode());
				conversationPartyData.setDomainIdNetworkRoles(conversationPartyDataList.get(0).getDomainIdNetworkRoles());
				conversationPartyData.setDomainIdNetworkRolesDesc(conversationPartyDataList.get(0).getDomainIdNetworkRolesDesc());
			} else {
				conversationPartyData.setName(null);
				conversationPartyData.setFollowUpCode(null);
			}

		} else {
			this.setServerSideErrorMessages(getParameterizedMessage("error_contactNumberMandatory"));
		}
	}

	public void collectNetworkDescription() {
		if (conversationPartyData.getDomainIdNetworkRoles() != null) {

			for (SetupDomain domain : networkRoleList) {
				if (domain.getId().equals(conversationPartyData.getDomainIdNetworkRoles())) {
					conversationPartyData.setDomainIdNetworkRolesDesc(domain.getDescription());
					break;
				}
			}
		}
	}

	public ConversationPartyData getConversationPartyData() {
		return conversationPartyData;
	}

	public void setConversationPartyData(ConversationPartyData conversationPartyData) {
		this.conversationPartyData = conversationPartyData;
	}

	public List<SetupDomain> getNetworkRoleList() {
		return networkRoleList;
	}

	public void setNetworkRoleList(List<SetupDomain> networkRoleList) {
		this.networkRoleList = networkRoleList;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
