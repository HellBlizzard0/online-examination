package com.code.dal.orm.assignment;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "assignmentEvaluationPointData_searchAssignmentEvaluationPoint", 
	           	 query= " select aE " +
	           	 		" from AssignmentEvaluationPointData aE" +
	           	 	    " where (:P_ASSIGNMENT_EVAL_ID = -1 or aE.assignmentEvaluationId = :P_ASSIGNMENT_EVAL_ID) " +
	           			" order by aE.classDescription, aE.domainDescription "
			    ),
	 @NamedQuery(name = "assignmentEvaluationPointData_hasAssignmentEvaluationPoint", 
			   	 query= " select aE " +
			   	 		" from AssignmentEvaluationPointData aE, AssignmentEvaluation a " +
			   	 	    " where (:P_ASSIGNMENT_DETAIL_ID = -1 or a.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID) " +
			   	 	    " and (:P_ASSIGNMENT_EVALUATION_ID = -1 or aE.assignmentEvaluationId = :P_ASSIGNMENT_EVALUATION_ID)" +
			   	 		" and a.id = aE.assignmentEvaluationId" +
			   			" order by a.evaluationDate desc, aE.classDescription, aE.domainDescription"
			    )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_ASSIGNMT_EVAL_POINTS")
public class AssignmentEvaluationPointData extends BaseEntity implements Serializable {
	private Long id;
	private Integer percent;
	private Long domainEvaluationPointId;
	private Boolean available;
	private Long assignmentEvaluationId;
	private String domainDescription;
	private String classDescription;
	private AssignmentEvaluationPoint assignmentEvaluationPoint;

	public AssignmentEvaluationPointData() {
		assignmentEvaluationPoint = new AssignmentEvaluationPoint();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return assignmentEvaluationPoint.getId();
	}

	public void setId(Long id) {
		assignmentEvaluationPoint.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "PERCENT")
	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		assignmentEvaluationPoint.setPercent(percent);
		this.percent = percent;
	}

	@Basic
	@Column(name = "DOMAINS_ID_EVALUATION_POINT")
	public Long getDomainEvaluationPointId() {
		return domainEvaluationPointId;
	}

	public void setDomainEvaluationPointId(Long domainEvaluationPointId) {
		assignmentEvaluationPoint.setDomainEvaluationPointId(domainEvaluationPointId);
		this.domainEvaluationPointId = domainEvaluationPointId;
	}

	@Basic
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "AVAILABLE")
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		assignmentEvaluationPoint.setAvailable(available);
		this.available = available;
	}

	@Basic
	@Column(name = "ASSIGNMENT_EVALUATIONS_ID")
	public Long getAssignmentEvaluationId() {
		return assignmentEvaluationId;
	}

	public void setAssignmentEvaluationId(Long assignmentEvaluationId) {
		assignmentEvaluationPoint.setAssignmentEvaluationId(assignmentEvaluationId);
		this.assignmentEvaluationId = assignmentEvaluationId;
	}

	@Basic
	@Column(name = "DOMAIN_DESCRIPTION")
	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	@Basic
	@Column(name = "CLASS_DESCRIPTION")
	public String getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	@Transient
	public AssignmentEvaluationPoint getAssignmentEvaluationPoint() {
		return assignmentEvaluationPoint;
	}

	public void setAssignmentEvaluationPoint(AssignmentEvaluationPoint assignmentEvaluationPoint) {
		this.assignmentEvaluationPoint = assignmentEvaluationPoint;
	}
}
