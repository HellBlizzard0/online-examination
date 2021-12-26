package com.code.dal.orm.finance;

import java.io.Serializable;
import java.util.Date;

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
  @NamedQuery(name = "finInfoReward_sumFinInfoReward", 
			   query= " select sum(iRwrd.amount) " +
				 	  " from FinInfoReward iRwrd" +
				 	  " where (:P_DEPARTMENT_ID = -1 or iRwrd.departmentId = :P_DEPARTMENT_ID ) " +
				 	  " and (:P_FIN_YEAR_APPROVAL_ID = -1 or iRwrd.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID ) "
  ),
  @NamedQuery(name = "finInfoReward_searchFinInfoReward", 
			  	query= " select iRwrd " +
				 	   " from FinInfoReward iRwrd  , AssignmentDetailData assignmentDetail" +
				 	   " where assignmentDetail.id = iRwrd.assignmentDetailId " +
				 	   " and (:P_AGENT_ID = -1 or iRwrd.assignmentDetailId = :P_AGENT_ID ) " +
				 	   " and (:P_IDENTITY = -1 or assignmentDetail.identity = :P_IDENTITY ) " +
				 	   " and  (:P_OFFICER_ID = -1 or assignmentDetail.officerId = :P_OFFICER_ID ) " +
				 	   " and (:P_FROM_DATE_NULL = 1 or iRwrd.paymentDate >= PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') ) " +
				 	   " and (:P_TO_DATE_NULL = 1 or iRwrd.paymentDate <= PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') ) " 
			)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFORMATION_REWARDS")
public class FinInfoReward extends AuditEntity implements Serializable , InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Date paymentDate;
	private String paymentDateString;
	private Long paymentSerial;
	private Long assignmentDetailId;
	private String paymentReason;
	private Double amount;
	private Long departmentId;
	private Long finYearApprovalId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INFORMATION_REWARDS_SEQ", sequenceName = "FIS_INFORMATION_REWARDS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFORMATION_REWARDS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDateString = HijriDateService.getHijriDateString(paymentDate);
		this.paymentDate = paymentDate;
	}

	@Transient
	public String getPaymentDateString() {
		return paymentDateString;
	}

	@Basic
	@Column(name = "PAYMENT_SERIAL")
	public Long getPaymentSerial() {
		return paymentSerial;
	}

	public void setPaymentSerial(Long paymentSerial) {
		this.paymentSerial = paymentSerial;
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
	@Column(name = "PAYMENT_REASON")
	public String getPaymentReason() {
		return paymentReason;
	}

	public void setPaymentReason(String paymentReason) {
		this.paymentReason = paymentReason;
	}

	@Basic
	@Column(name = "AMOUNT")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENT_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "FIN_YEAR_APPROVAL_ID")
	public Long getFinYearApprovalId() {
		return finYearApprovalId;
	}

	public void setFinYearApprovalId(Long finYearApprovalId) {
		this.finYearApprovalId = finYearApprovalId;
	}
}
