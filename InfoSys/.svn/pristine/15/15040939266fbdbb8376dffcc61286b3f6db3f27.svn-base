package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_CONVERSATION_PARTYS")
public class ConversationParty extends BaseEntity implements Serializable {
	private Long id;
	private String contactNumber;
	private String name;
	private String followUpCode;
	private Long domainIdNetworkRoles;
	private Long conversationId;
	private Date registrationDate;
	private String registrationDateString;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_CONVERSATION_PARTYS_SEQ", sequenceName = "FIS_SC_CONVERSATION_PARTYS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_CONVERSATION_PARTYS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "FOLLOW_UP_CODE")
	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
	}

	@Basic
	@Column(name = "DOMAIN_ID_NETWORK_ROLES_ID")
	public Long getDomainIdNetworkRoles() {
		return domainIdNetworkRoles;
	}

	public void setDomainIdNetworkRoles(Long domainIdNetworkRoles) {
		this.domainIdNetworkRoles = domainIdNetworkRoles;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONVERSATION_REGISTRATION_DATE")
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDateString = HijriDateService.getHijriDateString(registrationDate);
		this.registrationDate = registrationDate;
	}

	@Transient
	public String getRegistrationDateString() {
		return registrationDateString;
	}

}
