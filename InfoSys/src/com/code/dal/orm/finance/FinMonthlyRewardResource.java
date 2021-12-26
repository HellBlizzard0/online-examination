package com.code.dal.orm.finance;

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
@Table(name = "FIS_MONTLY_REWARD_RESOURCES")
public class FinMonthlyRewardResource extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity{
	private Long id;
	private Long monthlyRewardDeptDetailId;
	private Long assignmentDetailId;
	private Boolean received;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MONTLY_REWARD_RES_SEQ", sequenceName = "FIS_MONTLY_REWARD_RES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MONTLY_REWARD_RES_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "MONTHLY_REWARD_DEPT_DETAILS_ID")
	public Long getMonthlyRewardDeptDetailId() {
		return monthlyRewardDeptDetailId;
	}

	public void setMonthlyRewardDeptDetailId(Long monthlyRewardDeptDetailId) {
		this.monthlyRewardDeptDetailId = monthlyRewardDeptDetailId;
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
	@Column(name = "RECEIVED")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getReceived() {
		return received;
	}

	public void setReceived(Boolean received) {
		this.received = received;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}

	
}