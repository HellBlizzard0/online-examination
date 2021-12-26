package com.code.dal.orm.surveillance;

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
@Table(name = "FIS_SURVEILLANCE_ACTIONS")
public class SurveillanceAction extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long surveillanceEmpId;
	private Date eventDate;
	private String eventDateString;
	private String eventDetails;
	private Long evalPointDomainId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SURVEILLANCE_ACTIONS_SEQ", sequenceName = "FIS_SURVEILLANCE_ACTIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SURVEILLANCE_ACTIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "FIS_SURVEILLANCE_EMPLOYEES_ID")
	public Long getSurveillanceEmpId() {
		return surveillanceEmpId;
	}

	public void setSurveillanceEmpId(Long surveillanceEmpId) {
		this.surveillanceEmpId = surveillanceEmpId;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDateString = HijriDateService.getHijriDateString(eventDate);
		this.eventDate = eventDate;
	}

	@Transient
	public String getEventDateString() {
		return eventDateString;
	}

	@Basic
	@Column(name = "EVENT_DETAILS")
	public String getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}

	@Basic
	@Column(name = "EVALUATION_POINT_DOMAINS_ID")
	public Long getEvalPointDomainId() {
		return evalPointDomainId;
	}

	public void setEvalPointDomainId(Long evalPointDomainId) {
		this.evalPointDomainId = evalPointDomainId;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
