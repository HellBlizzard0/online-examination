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

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_FOLLOW_UP_DECISIONS")
public class Decision extends BaseEntity implements Serializable {
	private Long id;
	private Integer extensionDuration;
	private Integer decisionType;
	private Date resumeDate;
	private Date referralDate;
	private Long domainIdExternalSides;
	private Long empId;
	private String notes;
	private Long domainIdStopReasons;
	private Long followUpId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SC_DECISIONS_SEQ", sequenceName = "FIS_SC_DECISIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SC_DECISIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "EXTENSION_DURATION")
	public Integer getExtensionDuration() {
		return extensionDuration;
	}

	public void setExtensionDuration(Integer extensionDuration) {
		this.extensionDuration = extensionDuration;
	}

	@Basic
	@Column(name = "DECISION_TYPE")
	public Integer getDecisionType() {
		return decisionType;
	}

	public void setDecisionType(Integer decisionType) {
		this.decisionType = decisionType;
	}

	@Basic
	@Column(name = "DOMAIN_ID_EXTERNAL_SIDES_ID")
	public Long getDomainIdExternalSides() {
		return domainIdExternalSides;
	}

	public void setDomainIdExternalSides(Long domainIdExternalSides) {
		this.domainIdExternalSides = domainIdExternalSides;
	}

	@Basic
	@Column(name = "EMPLOYEE_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Basic
	@Column(name = "DOMAIN_ID_STOP_REASONS_ID")
	public Long getDomainIdStopReasons() {
		return domainIdStopReasons;
	}

	public void setDomainIdStopReasons(Long domainIdStopReasons) {
		this.domainIdStopReasons = domainIdStopReasons;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESUME_DATE")
	public Date getResumeDate() {
		return resumeDate;
	}

	public void setResumeDate(Date resumeDate) {
		this.resumeDate = resumeDate;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REFERRAL_DATE")
	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}
}
