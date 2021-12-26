package com.code.dal.orm.securityanalysis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.code.dal.orm.BaseEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SC_VW_FOLLOW_UP_DECISIONS")
public class DecisionData extends BaseEntity implements Serializable {
	private Long id;
	private Integer extensionDuration;
	private Integer decisionType;
	private Date resumeDate;
	private String resumeDateString;
	private Date referralDate;
	private String referralDateString;
	private Long domainIdExternalSides;
	private String domainIdExternalSidesDesc;
	private Long empId;
	private String notes;
	private Long domainIdStopReasons;
	private String domainIdStopReasonsDesc;
	private Long followUpId;
	private String employeeName;
	private Decision decision;

	public DecisionData() {
		this.decision = new Decision();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.decision.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "EXTENSION_DURATION")
	public Integer getExtensionDuration() {
		return extensionDuration;
	}

	public void setExtensionDuration(Integer extensionDuration) {
		this.decision.setExtensionDuration(extensionDuration);
		this.extensionDuration = extensionDuration;
	}

	@Basic
	@Column(name = "DECISION_TYPE")
	public Integer getDecisionType() {
		return decisionType;
	}

	public void setDecisionType(Integer decisionType) {
		this.decision.setDecisionType(decisionType);
		this.decisionType = decisionType;
	}

	@Basic
	@Column(name = "DOMAIN_EXTERNAL_SIDE_ID")
	public Long getDomainIdExternalSides() {
		return domainIdExternalSides;
	}

	public void setDomainIdExternalSides(Long domainIdExternalSides) {
		this.decision.setDomainIdExternalSides(domainIdExternalSides);
		this.domainIdExternalSides = domainIdExternalSides;
	}

	@Basic
	@Column(name = "DOMAIN_EXTERNAL_SIDE_DESC")
	public String getDomainIdExternalSidesDesc() {
		return domainIdExternalSidesDesc;
	}

	public void setDomainIdExternalSidesDesc(String domainIdExternalSidesDesc) {
		this.domainIdExternalSidesDesc = domainIdExternalSidesDesc;
	}

	@Basic
	@Column(name = "EMPLOYEE_ID")
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.decision.setEmpId(empId);
		this.empId = empId;
	}

	@Basic
	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.decision.setNotes(notes);
		this.notes = notes;
	}

	@Basic
	@Column(name = "DOMAIN_STOP_REASON_ID")
	public Long getDomainIdStopReasons() {
		return domainIdStopReasons;
	}

	public void setDomainIdStopReasons(Long domainIdStopReasons) {
		this.decision.setDomainIdStopReasons(domainIdStopReasons);
		this.domainIdStopReasons = domainIdStopReasons;
	}

	@Basic
	@Column(name = "DOMAIN_STOP_REASON_DESC")
	public String getDomainIdStopReasonsDesc() {
		return domainIdStopReasonsDesc;
	}

	public void setDomainIdStopReasonsDesc(String domainIdStopReasonsDesc) {
		this.domainIdStopReasonsDesc = domainIdStopReasonsDesc;
	}

	@Basic
	@Column(name = "FOLLOW_UP_ID")
	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.decision.setFollowUpId(followUpId);
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
		this.resumeDateString = HijriDateService.getHijriDateString(resumeDate);
		this.decision.setResumeDate(resumeDate);
	}

	@Transient
	public String getResumeDateString() {
		return resumeDateString;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REFERRAL_DATE")
	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
		this.referralDateString = HijriDateService.getHijriDateString(referralDate);
		this.decision.setReferralDate(referralDate);
	}

	public String getReferralDateString() {
		return referralDateString;
	}

	public void setReferralDateString(String referralDateString) {
		this.referralDateString = referralDateString;
		this.setReferralDate(HijriDateService.getHijriDate(referralDateString));
	}

	@Basic
	@Column(name = "DECISION_EMPLOYEE_NAME")
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Transient
	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}
}