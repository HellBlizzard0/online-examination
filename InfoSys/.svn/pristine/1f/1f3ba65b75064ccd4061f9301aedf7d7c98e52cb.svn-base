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
@Table(name = "FIS_SC_REFERRALS")
public class Referral extends BaseEntity implements Serializable {
	private Long id;
	private Long domainIdReferralType;
	private Long employeeNumber;
	private String details;
	private Long conversationId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_REFERRAL_SEQ", sequenceName = "FIS_SC_REFERRAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_REFERRAL_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DOMAIN_ID_REFERRAL_TYPE_ID")
	public Long getDomainIdReferralType() {
		return domainIdReferralType;
	}

	public void setDomainIdReferralType(Long domainIdReferralType) {
		this.domainIdReferralType = domainIdReferralType;
	}

	@Basic
	@Column(name = "EMPLOYEE_NUMBER")
	public Long getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Long employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Basic
	@Column(name = "DETAILS")
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	@Basic
	@Column(name = "CONVERSATION_ID")
	public Long getConversationId() {
		return conversationId;
	}

}
