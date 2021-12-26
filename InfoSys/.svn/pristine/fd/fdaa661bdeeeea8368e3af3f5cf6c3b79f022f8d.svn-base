package com.code.dal.orm.surveillance;

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
	@NamedQuery(name = "surveillanceActionData_searchEmployeeSurveillanceAction", 
				query = " select sa " +
						" from SurveillanceActionData sa " +
						" where (:P_EMPLOYEE_ID = -1 or sa.surveillanceEmpId = :P_EMPLOYEE_ID )" +
						" and (:P_START_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_START_DATE, 'MI/MM/YYYY') <= sa.eventDate)" + 
						" and (:P_END_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_END_DATE, 'MI/MM/YYYY') >= sa.eventDate)" +       
						" order by sa.eventDate desc"
				)
})
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_SURVEILLANCE_ACTIONS")
public class SurveillanceActionData extends BaseEntity implements Serializable {
	private Long id;
	private Long surveillanceEmpId;
	private Date eventDate;
	private String eventDateString;
	private String eventDetails;
	private Long evalPointDomainId;
	private String domainDescription;
	private String classDescription;
	private String classCode;

	private SurveillanceAction surveillanceAction;

	public SurveillanceActionData() {
		this.surveillanceAction = new SurveillanceAction();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return surveillanceAction.getId();
	}

	public void setId(Long id) {
		this.surveillanceAction.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "FIS_SURVEILLANCE_EMPLOYEES_ID")
	public Long getSurveillanceEmpId() {
		return surveillanceEmpId;
	}

	public void setSurveillanceEmpId(Long surveillanceEmpId) {
		this.surveillanceAction.setSurveillanceEmpId(surveillanceEmpId);
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
		this.surveillanceAction.setEventDate(eventDate);
		this.eventDate = eventDate;
	}

	@Transient
	public String getEventDateString() {
		return eventDateString;
	}

	public void setEventDateString(String eventDateString) {
		this.eventDateString = eventDateString;
	}

	@Basic
	@Column(name = "EVENT_DETAILS")
	public String getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(String eventDetails) {
		this.surveillanceAction.setEventDetails(eventDetails);
		this.eventDetails = eventDetails;
	}

	@Basic
	@Column(name = "EVALUATION_POINT_DOMAINS_ID")
	public Long getEvalPointDomainId() {
		return evalPointDomainId;
	}

	public void setEvalPointDomainId(Long evalPointDomainId) {
		this.surveillanceAction.setEvalPointDomainId(evalPointDomainId);
		this.evalPointDomainId = evalPointDomainId;
	}

	@Basic
	@Column(name = "EVAL_POINT_DOMAIN_DESCRIPTION")
	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	@Basic
	@Column(name = "EVAL_POINT_CLASS_DESCRIPTION")
	public String getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	@Transient
	public SurveillanceAction getSurveillanceAction() {
		return surveillanceAction;
	}

	public void setSurveillanceAction(SurveillanceAction surveillanceAction) {
		this.surveillanceAction = surveillanceAction;
	}

	@Basic
	@Column(name = "EVAL_POINT_CLASS_CODE")
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
}
