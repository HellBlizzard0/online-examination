package com.code.dal.orm.assignment;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ASSIGNMT_EVALUATION_POINTS")
public class AssignmentEvaluationPoint extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Integer percent;
	private Long domainEvaluationPointId;
	private Boolean available;
	private Long assignmentEvaluationId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_ASSIGNMT_EVAL_POINTS_SEQ", sequenceName = "FIS_ASSIGNMT_EVAL_POINTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_ASSIGNMT_EVAL_POINTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PERCENT")
	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	@Basic
	@Column(name = "DOMAINS_ID_EVALUATION_POINT")
	public Long getDomainEvaluationPointId() {
		return domainEvaluationPointId;
	}

	public void setDomainEvaluationPointId(Long domainEvaluationPointId) {
		this.domainEvaluationPointId = domainEvaluationPointId;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "AVAILABLE")
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	@Basic
	@Column(name = "ASSIGNMENT_EVALUATIONS_ID")
	public Long getAssignmentEvaluationId() {
		return assignmentEvaluationId;
	}

	public void setAssignmentEvaluationId(Long assignmentEvaluationId) {
		this.assignmentEvaluationId = assignmentEvaluationId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
