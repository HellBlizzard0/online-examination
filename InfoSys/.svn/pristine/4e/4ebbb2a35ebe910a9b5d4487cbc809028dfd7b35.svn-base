package com.code.dal.orm.securitymission;

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
@Table(name = "FIS_PENALTY_ARREST")
public class PenaltyArrest extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long requesterEmployeeId;
	private Long arrestedEmployeeId;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private Integer arrestPeriod;
	private String arrestLocation;
	private String remarks;
	private Date entryDate;
	private String entryDateString;
	private String entryTime;
	private Date exitDate;
	private String exitDateString;
	private String exitTime;
	private Long wFInstanceId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_PENALTY_ARREST_SEQ", sequenceName = "FIS_PENALTY_ARREST_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_PENALTY_ARREST_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "STP_EMPLOYEES_ID_REQUESTER")
	public Long getRequesterEmployeeId() {
		return requesterEmployeeId;
	}

	public void setRequesterEmployeeId(Long requesterEmployeeId) {
		this.requesterEmployeeId = requesterEmployeeId;
	}

	@Basic
	@Column(name = "STP_EMPLOYEES_ID_ARRESTED")
	public Long getArrestedEmployeeId() {
		return arrestedEmployeeId;
	}

	public void setArrestedEmployeeId(Long arrestedEmployeeId) {
		this.arrestedEmployeeId = arrestedEmployeeId;
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
		this.requestDate = requestDate;
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "ARREST_PERIOD")
	public Integer getArrestPeriod() {
		return arrestPeriod;
	}

	public void setArrestPeriod(Integer arrestPeriod) {
		this.arrestPeriod = arrestPeriod;
	}

	@Basic
	@Column(name = "ARREST_LOCATION")
	public String getArrestLocation() {
		return arrestLocation;
	}

	public void setArrestLocation(String arrestLocation) {
		this.arrestLocation = arrestLocation;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
		this.entryDateString = HijriDateService.getHijriDateString(entryDate);
	}

	@Transient
	public String getEntryDateString() {
		return entryDateString;
	}

	@Basic
	@Column(name = "ENTRY_TIME")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXIT_DATE")
	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
		this.exitDateString = HijriDateService.getHijriDateString(exitDate);
	}

	@Transient
	public String getExitDateString() {
		return exitDateString;
	}

	@Basic
	@Column(name = "EXIT_TIME")
	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	@Basic
	@Column(name = "WF_INSTANCES_ID")
	public Long getwFInstanceId() {
		return wFInstanceId;
	}

	public void setwFInstanceId(Long wFInstanceId) {
		this.wFInstanceId = wFInstanceId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
