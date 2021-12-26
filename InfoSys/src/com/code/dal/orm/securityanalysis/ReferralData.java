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
	 @NamedQuery(name = "referralData_searchReferrals", 
		         query= " select referral " +
		                " from ReferralData referral " +
		                " where (:P_CONVERSATION_ID = -1 or :P_CONVERSATION_ID = referral.conversationId)" + 
		                " order by referral.id "
 	 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_REFERRALS")
public class ReferralData extends BaseEntity implements Serializable {
	private Long domainIdReferralType;
	private String domainIdReferralTypeDesc;
	private Long employeeNumber;
	private String employeeName;
	private String details;
	private Long conversationId;
	private Referral referral;

	public ReferralData() {
		this.referral = new Referral();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return referral.getId();
	}

	public void setId(Long id) {
		this.referral.setId(id);
	}

	@Basic
	@Column(name = "DOMAIN_REFERRAL_TYPES_ID")
	public Long getDomainIdReferralType() {
		return domainIdReferralType;
	}

	public void setDomainIdReferralType(Long domainIdReferralType) {
		this.referral.setDomainIdReferralType(domainIdReferralType);
		this.domainIdReferralType = domainIdReferralType;
	}

	@Basic
	@Column(name = "DOMAIN_REFERRAL_DESCRIPTION")
	public String getDomainIdReferralTypeDesc() {
		return domainIdReferralTypeDesc;
	}

	public void setDomainIdReferralTypeDesc(String domainIdReferralTypeDesc) {
		this.domainIdReferralTypeDesc = domainIdReferralTypeDesc;
	}

	@Basic
	@Column(name = "EMPLOYEE_NUMBER")
	public Long getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Long employeeNumber) {
		this.referral.setEmployeeNumber(employeeNumber);
		this.employeeNumber = employeeNumber;
	}

	@Basic
	@Column(name = "DETAILS")
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.referral.setDetails(details);
		this.details = details;
	}

	public void setConversationId(Long conversationId) {
		this.referral.setConversationId(conversationId);
		this.conversationId = conversationId;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

	@Transient
	public Referral getReferral() {
		return referral;
	}

	public void setReferral(Referral referral) {
		this.referral = referral;
	}

	@Basic
	@Column(name = "REFERRAL_EMPLOYEE_NAME")
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}