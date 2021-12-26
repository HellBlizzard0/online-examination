package com.code.dal.orm.surveillance;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_SURV_REPORT_DETAILS")
public class SurveillanceReportDetail extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long surveillanceReportId;
	private Long evalPointDomainId;
	private Integer grade;
	private String remarks;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_SURV_REPORT_DETAILS_SEQ", sequenceName = "FIS_SURV_REPORT_DETAILS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_SURV_REPORT_DETAILS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "FIS_SURVEILLANCE_REPORTS_ID")
	public Long getSurveillanceReportId() {
		return surveillanceReportId;
	}

	public void setSurveillanceReportId(Long surveillanceReportId) {
		this.surveillanceReportId = surveillanceReportId;
	}

	@Basic
	@Column(name = "DOMAINS_ID_EVALUATION_POINT")
	public Long getEvalPointDomainId() {
		return evalPointDomainId;
	}

	public void setEvalPointDomainId(Long evalPointDomainId) {
		this.evalPointDomainId = evalPointDomainId;
	}

	@Basic
	@Column(name = "GRADE")
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}
