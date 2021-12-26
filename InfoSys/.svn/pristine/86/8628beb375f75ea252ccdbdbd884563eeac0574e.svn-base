package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "guidanceRequestData_searchguidanceRequest", 
		         query= " select guidReq " +
		                " from GuidanceRequestData guidReq " +
		                " where (:P_CONVERSATION_ID = -1 or :P_CONVERSATION_ID = guidReq.conversationId)" + 
		                " order by guidReq.id "
			 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_GUIDANCE_REQUESTS")
public class GuidanceRequestData extends BaseEntity implements Serializable {
	private Long id;
	private Long domainIdGuidanceTypes;
	private String domainIdGuidanceTypesDesc;
	private String guidanceDetails;
	private Long conversationId;
	private GuidanceRequest guidanceRequest;

	public GuidanceRequestData() {
		this.guidanceRequest = new GuidanceRequest();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.guidanceRequest.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "DOMAIN_GUIDANCE_TYPES_ID")
	public Long getDomainIdGuidanceTypes() {
		return domainIdGuidanceTypes;
	}

	public void setDomainIdGuidanceTypes(Long domainIdGuidanceTypes) {
		this.guidanceRequest.setDomainIdGuidanceTypes(domainIdGuidanceTypes);
		this.domainIdGuidanceTypes = domainIdGuidanceTypes;
	}

	@Basic
	@Column(name = "DOMAIN_GUIDANCE_DESCRIPTION")
	public String getDomainIdGuidanceTypesDesc() {
		return domainIdGuidanceTypesDesc;
	}

	public void setDomainIdGuidanceTypesDesc(String domainIdGuidanceTypesDesc) {
		this.domainIdGuidanceTypesDesc = domainIdGuidanceTypesDesc;
	}

	@Basic
	@Column(name = "GUIDANCE_DETAILS")
	public String getGuidanceDetails() {
		return guidanceDetails;
	}

	public void setGuidanceDetails(String guidanceDetails) {
		this.guidanceRequest.setGuidanceDetails(guidanceDetails);
		this.guidanceDetails = guidanceDetails;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.guidanceRequest.setConversationId(conversationId);
		this.conversationId = conversationId;
	}

	@Transient
	public GuidanceRequest getGuidanceRequest() {
		return guidanceRequest;
	}

	public void setGuidanceRequest(GuidanceRequest guidanceRequest) {
		this.guidanceRequest = guidanceRequest;
	}
}
