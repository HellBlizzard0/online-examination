package com.code.dal.orm.info;

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

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFORMATION")
public class Info extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Date registerationDate;
	private String registerationDateString;
	private String infoNumber;
	private Date receiveDate;
	private String receiveDateString;
	private String receiveTime;
	private Long importance;
	private Long relatedInfoId;
	private Long infoSubjectId;
	private String incomingNumber;
	private Date incomingDate;
	private String incomingDateString;
	private Long domainIncomingSideId;
	private Long domainDealingTypeId;
	private Long domainDealingLevelId;
	private String incomingFileNumber;
	private Integer incomingFileYear;
	private Integer status;
	private Long wFInstanceId;
	private String letterCopies;
	private String letterNotes;
	private Boolean payReward;
	private Boolean agentEvaluation;
	private Boolean agentApprove;
	private Long regionId;
	private Long sectorId;
	private Long unitId;
	private String infoDetails;
	private Long confidentiality;
	private Long infoEvaluationId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INFORMATION_SEQ", sequenceName = "FIS_INFORMATION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFORMATION_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTERATION_DATE")
	public Date getRegisterationDate() {
		return registerationDate;
	}

	public void setRegisterationDate(Date registerationDate) {
		if (registerationDate != null) {
			this.registerationDateString = HijriDateService.getHijriDateString(registerationDate);
		}
		this.registerationDate = registerationDate;
	}

	@Basic
	@Column(name = "INFO_NUMBER")
	public String getInfoNumber() {
		return infoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
	}

	@Basic
	@Column(name = "RECEIVE_TIME")
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DATE")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		if (receiveDate != null) {
			this.receiveDateString = HijriDateService.getHijriDateString(receiveDate);
		}
		this.receiveDate = receiveDate;
	}

	@Basic
	@Column(name = "IMPORTANCE")
	public Long getImportance() {
		return importance;
	}

	public void setImportance(Long importance) {
		this.importance = importance;
	}

	@Basic
	@Column(name = "RELATED_INFORMATION_ID")
	public Long getRelatedInfoId() {
		return relatedInfoId;
	}

	public void setRelatedInfoId(Long relatedInfoId) {
		this.relatedInfoId = relatedInfoId;
	}

	@Basic
	@Column(name = "INFO_SUBJECTS_ID")
	public Long getInfoSubjectId() {
		return infoSubjectId;
	}

	public void setInfoSubjectId(Long infoSubjectId) {
		this.infoSubjectId = infoSubjectId;
	}

	@Basic
	@Column(name = "INCOMING_NUMBER")
	public String getIncomingNumber() {
		return incomingNumber;
	}

	public void setIncomingNumber(String incomingNumber) {
		this.incomingNumber = incomingNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INCOMING_DATE")
	public Date getIncomingDate() {
		return incomingDate;
	}

	public void setIncomingDate(Date incomingDate) {
		if (incomingDate != null) {
			this.incomingDateString = HijriDateService.getHijriDateString(incomingDate);
		}
		this.incomingDate = incomingDate;
	}

	@Basic
	@Column(name = "DOMAINS_ID_INCOMING_SIDES")
	public Long getDomainIncomingSideId() {
		return domainIncomingSideId;
	}

	public void setDomainIncomingSideId(Long domainIncomingSideId) {
		this.domainIncomingSideId = domainIncomingSideId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_DEALING_TYPES")
	public Long getDomainDealingTypeId() {
		return domainDealingTypeId;
	}

	public void setDomainDealingTypeId(Long domainDealingTypeId) {
		this.domainDealingTypeId = domainDealingTypeId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_DEALING_LEVELS")
	public Long getDomainDealingLevelId() {
		return domainDealingLevelId;
	}

	public void setDomainDealingLevelId(Long domainDealingLevelId) {
		this.domainDealingLevelId = domainDealingLevelId;
	}

	@Basic
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Basic
	@Column(name = "LETTER_COPIES")
	public String getLetterCopies() {
		return letterCopies;
	}

	public void setLetterCopies(String letterCopies) {
		this.letterCopies = letterCopies;
	}

	@Basic
	@Column(name = "LETTERS_NOTES")
	public String getLetterNotes() {
		return letterNotes;
	}

	public void setLetterNotes(String letterNotes) {
		this.letterNotes = letterNotes;
	}

	@Basic
	@Column(name = "PAY_REWARD")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getPayReward() {
		return payReward;
	}

	public void setPayReward(Boolean payReward) {
		this.payReward = payReward;
	}

	@Basic
	@Column(name = "AGENT_EVALUATION")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getAgentEvaluation() {
		return agentEvaluation;
	}

	public void setAgentEvaluation(Boolean agentEvaluation) {
		this.agentEvaluation = agentEvaluation;
	}

	@Basic
	@Column(name = "AGENT_APPROVE")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getAgentApprove() {
		return agentApprove;
	}

	public void setAgentApprove(Boolean agentApprove) {
		this.agentApprove = agentApprove;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_REGION")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_SECTOR")
	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_UNIT")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Basic
	@Column(name = "INFO_DETAILS")
	public String getInfoDetails() {
		return infoDetails;
	}

	public void setInfoDetails(String infoDetails) {
		this.infoDetails = infoDetails;
	}
	
	@Basic
	@Column(name = "INCOMING_FILE_NUMBER")
	public String getIncomingFileNumber() {
		return incomingFileNumber;
	}

	public void setIncomingFileNumber(String incomingFileNumber) {
		this.incomingFileNumber = incomingFileNumber;
	}

	@Basic
	@Column(name = "INCOMING_FILE_YEAR")
	public Integer getIncomingFileYear() {
		return incomingFileYear;
	}

	public void setIncomingFileYear(Integer incomingFileYear) {
		this.incomingFileYear = incomingFileYear;
	}
	
	@Transient
	public String getRegisterationDateString() {
		return registerationDateString;
	}

	

	@Transient
	public String getReceiveDateString() {
		return receiveDateString;
	}

	@Transient
	public String getIncomingDateString() {
		return incomingDateString;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "CONFIDENTIALITY")
	public Long getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(Long confidentiality) {
		this.confidentiality = confidentiality;
	}

	@Basic
	@Column(name = "INFO_EVALUATION_ID")
	public Long getInfoEvaluationId() {
		return infoEvaluationId;
	}

	public void setInfoEvaluationId(Long infoEvaluationId) {
		this.infoEvaluationId = infoEvaluationId;
	}
}
