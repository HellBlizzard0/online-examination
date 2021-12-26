package com.code.dal.orm.finance;

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

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_DEPARTMENTS_SUPPORTS")
public class FinDepSupport extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private String remarks;
	private Long finYearApprovalId;
	private Long wfInstanceId;
	private Integer approved;
	private Double balance;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_DEPARTMENTS_SUPPORTS_SEQ", sequenceName = "FIS_DEPARTMENTS_SUPPORTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_DEPARTMENTS_SUPPORTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "FIS_FIN_YEAR_APPROVALS_ID")
	public Long getFinYearApprovalId() {
		return finYearApprovalId;
	}

	public void setFinYearApprovalId(Long finYearApprovalId) {
		this.finYearApprovalId = finYearApprovalId;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getWfInstanceId() {
		return wfInstanceId;
	}

	public void setWfInstanceId(Long wfInstanceId) {
		this.wfInstanceId = wfInstanceId;
	}

	@Basic
	@Column(name = "APPROVED")
	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	@Basic
	@Column(name = "BALANCE")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	@Override
	public Long calculateContentId() {
		return id;
	}	
}