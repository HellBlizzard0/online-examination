package com.code.dal.orm.securitycheck;

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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SECURITY_CHECKS")
public class SecurityCheck extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long infoId;
	private Date requestDate;
	private String requestDateString;
	private String requestNumber;
	private Long departmentOrderSrcId;
	private String reason;
	private Integer checkReason;
	private String incomingNumber;
	private Date incomingDate;
	private String incomingDateString;
	private Long wFInstanceId;
	private Long domainIncomingSideId;
	private Long domainReasonId;
	private Integer status;
	private Long regionId;
	private String requestSource;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SECURITY_CHECKS_SEQ", sequenceName = "FIS_SECURITY_CHECKS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SECURITY_CHECKS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID_ORDR_SRC")
	public Long getDepartmentOrderSrcId() {
		return departmentOrderSrcId;
	}

	public void setDepartmentOrderSrcId(Long departmentOrderSrcId) {
		this.departmentOrderSrcId = departmentOrderSrcId;
	}

	@Basic
	@Column(name = "REASON")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Basic
	@Column(name = "INCOMMING_NUMBER")
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
		this.incomingDateString = HijriDateService.getHijriDateString(incomingDate);
		this.incomingDate = incomingDate;
	}

	@Transient
	public String getIncomingDateString() {
		return incomingDateString;
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
	@Column(name = "DOMAINS_ID_INCOMING_SIDES")
	public Long getDomainIncomingSideId() {
		return domainIncomingSideId;
	}

	public void setDomainIncomingSideId(Long domainIncomingSideId) {
		this.domainIncomingSideId = domainIncomingSideId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_REASON")
	public Long getDomainReasonId() {
		return domainReasonId;
	}

	public void setDomainReasonId(Long domainReasonId) {
		this.domainReasonId = domainReasonId;
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
	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "CHECK_REASON")
	public Integer getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(Integer checkReason) {
		this.checkReason = checkReason;
	}

	@Basic
	@Column(name = "REQUEST_SOURCE")
	public String getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}

}