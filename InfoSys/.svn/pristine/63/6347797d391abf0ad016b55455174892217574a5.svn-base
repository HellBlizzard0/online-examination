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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_DEPARTMENTS_SUPPORT_DETALS")
public class FinDepSupportDetail extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long departmentId;
	private Double supportAmount;
	private Long departmentSupportId;
	private Double balance;
	private Double totalSpent;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_DEP_SUPPORT_DETALS_SEQ", sequenceName = "FIS_DEP_SUPPORT_DETALS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_DEP_SUPPORT_DETALS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "SUPPORT_AMOUNT")
	public Double getSupportAmount() {
		return supportAmount;
	}

	public void setSupportAmount(Double supportAmount) {
		this.supportAmount = supportAmount;
	}

	@Basic
	@Column(name = "DEPARTMENTS_SUPPORTS_ID")
	public Long getDepartmentSupportId() {
		return departmentSupportId;
	}

	public void setDepartmentSupportId(Long departmentSupportId) {
		this.departmentSupportId = departmentSupportId;
	}

	@Basic
	@Column(name = "BALANCE")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Basic
	@Column(name = "TOTAL_SPENT")
	public Double getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Double totalSpent) {
		this.totalSpent = totalSpent;
	}
	
	@Override
	public Long calculateContentId() {
		return id;
	}
}