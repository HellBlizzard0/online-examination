package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name = "conversationPartyData_searchConversationPartyData", 
		         query= " select conversationPartyData " +
		                " from ConversationPartyData conversationPartyData " +
						" where (:P_CONTACT_NUMBER = '-1' or conversationPartyData.contactNumber  = :P_CONTACT_NUMBER )" +
						" and (:P_FOLLOW_CODE = '-1' or conversationPartyData.followUpCode = :P_FOLLOW_CODE) " +
						" and (:P_CONVERSATION_ID = -1 or  conversationPartyData.conversationId = :P_CONVERSATION_ID ) " +
						" order by conversationPartyData.registraionDate desc ")
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_CONVERSATION_PARTYS")
public class ConversationPartyData extends BaseEntity implements Serializable {
	private Long id;
	private String contactNumber;
	private String name;
	private String followUpCode;
	private Long domainIdNetworkRoles;
	private String domainIdNetworkRolesDesc;
	private Long conversationId;
	private Date registraionDate;
	private String registraionDateString;
	private ConversationParty conversationParty;

	public ConversationPartyData() {
		this.conversationParty = new ConversationParty();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.conversationParty.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.conversationParty.setContactNumber(contactNumber);
		this.contactNumber = contactNumber;
	}

	@Basic
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.conversationParty.setName(name);
		this.name = name;
	}

	@Basic
	@Column(name = "FOLLOW_UP_CODE")
	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.conversationParty.setFollowUpCode(followUpCode);
		this.followUpCode = followUpCode;
	}

	@Basic
	@Column(name = "DOMAIN_CONVERSATION_TYPES_ID")
	public Long getDomainIdNetworkRoles() {
		return domainIdNetworkRoles;
	}

	public void setDomainIdNetworkRoles(Long domainIdNetworkRoles) {
		this.conversationParty.setDomainIdNetworkRoles(domainIdNetworkRoles);
		this.domainIdNetworkRoles = domainIdNetworkRoles;
	}

	@Basic
	@Column(name = "DOMAIN_CONVERSATION_DESC")
	public String getDomainIdNetworkRolesDesc() {
		return domainIdNetworkRolesDesc;
	}

	public void setDomainIdNetworkRolesDesc(String domainIdNetworkRolesDesc) {
		this.domainIdNetworkRolesDesc = domainIdNetworkRolesDesc;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationParty.setConversationId(conversationId);
		this.conversationId = conversationId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONVERSATION_REGISTRATION_DATE")
	public Date getRegistraionDate() {
		return registraionDate;
	}

	public void setRegistraionDate(Date registraionDate) {
		this.registraionDate = registraionDate;
		this.registraionDateString = HijriDateService.getHijriDateString(registraionDate);
		conversationParty.setRegistrationDate(registraionDate);
	}

	@Transient
	public String getRegistraionDateString() {
		return registraionDateString;
	}
	
	@Transient
	public ConversationParty getConversationParty() {
		return conversationParty;
	}

	public void setConversationParty(ConversationParty conversationParty) {
		this.conversationParty = conversationParty;
	}

}
