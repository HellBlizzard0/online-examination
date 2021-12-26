package com.code.dal.orm.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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

@NamedQueries({
	 @NamedQuery(name = "assignmentEvaluation_searchAssignmentEvaluation", 
	           	 query= " select a " +
	           	 		" from AssignmentEvaluation a" +
	           	 	    " where (:P_ASSIGNMENT_DETAIL_ID = -1 or a.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID) " +
	           	   	    " and (:P_INSTANCE_ID = -1 or a.wFInstanceId = :P_INSTANCE_ID) " +
	           	   	    " and ( :P_EVAL_ACTION = -1 or a.evaluationAction = :P_EVAL_ACTION ) " +
	           			" order by a.evaluationDate desc"
	),
	@NamedQuery(name = "assignmentEvaluation_searchAssignmentEvaluationWFInstances", 
	            query= " select a " +
	            	   " from AssignmentEvaluation a, WFInstance w" +
            	 	   " where ( :P_ASSIGNMENT_DETAIL_ID = -1 or a.assignmentDetailId = :P_ASSIGNMENT_DETAIL_ID ) " +
	            	   " and ( :P_INSTANCE_STATUS = '-1' or w.status = :P_INSTANCE_STATUS ) " +
	            	   " and ( :P_EVAL_ACTION = -1 or a.evaluationAction = :P_EVAL_ACTION ) " +
	            	   " and ( :P_EVAL_DATE_NULL = 1 or a.evaluationDate < PKG_CHAR_TO_DATE (:P_EVAL_DATE, 'MI/MM/YYYY') ) " +
	            	   " and w.id = a.wFInstanceId" +
            	 	   " order by a.id "
 	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ASSIGNMENT_EVALUATIONS")
public class AssignmentEvaluation extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Date evaluationDate;
	private String evaluationDateString;
	private Date evaluationStartDate;
	private String evaluationStartDateString;
	private Long assignmentDetailId;
	private Integer evaluationAction;
	private Integer extendPeriod;
	private Double totalEvaluation;
	private Integer approvedExtendPeriod;
	private String agentClass;
	private Double monthlyReward;
	private Long wFInstanceId;
	private Integer status;
	private String reasons;
	private String remarks;
	private List<String> assignmentEvalReasonsList;

	public AssignmentEvaluation() {
		assignmentEvalReasonsList = new ArrayList<String>();
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_ASSIGNMENT_EVALUATIONS_SEQ", sequenceName = "FIS_ASSIGNMENT_EVALUATIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_ASSIGNMENT_EVALUATIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVALUATION_DATE")
	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDateString = HijriDateService.getHijriDateString(evaluationDate);
		this.evaluationDate = evaluationDate;
	}

	@Transient
	public String getEvaluationDateString() {
		return evaluationDateString;
	}

	@Basic
	@Column(name = "ASSIGNMENT_DETAILS_ID")
	public Long getAssignmentDetailId() {
		return assignmentDetailId;
	}

	public void setAssignmentDetailId(Long assignmentDetailId) {
		this.assignmentDetailId = assignmentDetailId;
	}

	@Basic
	@Column(name = "EVALUATION_ACTION")
	public Integer getEvaluationAction() {
		return evaluationAction;
	}

	public void setEvaluationAction(Integer evaluationAction) {
		this.evaluationAction = evaluationAction;
	}

	@Basic
	@Column(name = "EXTEND_PERIOD")
	public Integer getExtendPeriod() {
		return extendPeriod;
	}

	public void setExtendPeriod(Integer extendPeriod) {
		this.extendPeriod = extendPeriod;
	}

	@Basic
	@Column(name = "TOTAL_EVALUATION")
	public Double getTotalEvaluation() {
		return totalEvaluation;
	}

	public void setTotalEvaluation(Double totalEvaluation) {
		this.totalEvaluation = totalEvaluation;
	}

	@Basic
	@Column(name = "APPROVED_EXTEND_PERIOD")
	public Integer getApprovedExtendPeriod() {
		return approvedExtendPeriod;
	}

	public void setApprovedExtendPeriod(Integer approvedExtendPeriod) {
		this.approvedExtendPeriod = approvedExtendPeriod;
	}

	@Basic
	@Column(name = "AGENT_CLASS")
	public String getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD")
	public Double getMonthlyReward() {
		return monthlyReward;
	}

	public void setMonthlyReward(Double monthlyReward) {
		this.monthlyReward = monthlyReward;
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
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "REASONS")
	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	@Transient
	public List<String> getAssignmentEvalReasonsList() {
		return assignmentEvalReasonsList;
	}

	public void setAssignmentEvalReasonsList(List<String> assignmentEvalReasonsList) {
		this.reasons = "";
		for (int i = 0; i < assignmentEvalReasonsList.size(); i++) {
			if (i != 0) {
				this.reasons = reasons.concat(",");
			}
			this.reasons += assignmentEvalReasonsList.get(i);
		}
		setReasons(this.reasons);
		this.assignmentEvalReasonsList = assignmentEvalReasonsList;
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
	@Column(name = "EVALUATION_START_DATE")
	public Date getEvaluationStartDate() {
		return evaluationStartDate;
	}

	public void setEvaluationStartDate(Date evaluationStartDate) {
		this.evaluationStartDate = evaluationStartDate;
		this.evaluationStartDateString = HijriDateService.getHijriDateString(evaluationStartDate);
	}

	@Transient
	public String getEvaluationStartDateString() {
		return evaluationStartDateString;
	}
}
