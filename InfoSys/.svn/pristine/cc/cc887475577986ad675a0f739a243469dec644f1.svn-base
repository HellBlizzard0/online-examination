package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_GUIDANCE_REQUESTS")
public class GuidanceRequest extends BaseEntity implements Serializable {
	private Long id;
	private Long domainIdGuidanceTypes;
	private String guidanceDetails;
	private Long conversationId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_GUIDANCE_REQ_SEQ", sequenceName = "FIS_SC_GUIDANCE_REQ_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_GUIDANCE_REQ_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DOMAIN_ID_GUIDANCE_TYPES_ID")
	public Long getDomainIdGuidanceTypes() {
		return domainIdGuidanceTypes;
	}

	public void setDomainIdGuidanceTypes(Long domainIdGuidanceTypes) {
		this.domainIdGuidanceTypes = domainIdGuidanceTypes;
	}

	@Basic
	@Column(name = "GUIDANCE_DETAILS")
	public String getGuidanceDetails() {
		return guidanceDetails;
	}

	public void setGuidanceDetails(String guidanceDetails) {
		this.guidanceDetails = guidanceDetails;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
}
