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
	 @NamedQuery(name  = "finMonthlyReward_searchFinMonthlyReward", 
			 	 query = " select  mRwrd " + 
	           	 		 " from FinMonthlyReward mRwrd" +
	           			 " where (:P_INSTANCE_ID = -1 or mRwrd.wFInstanceId = :P_INSTANCE_ID )" +
	           			 " and (:P_ID = -1 or mRwrd.id = :P_ID )" +
	           			 " and (:P_MONTH_NUMBER = -1 or mRwrd.monthNumber = :P_MONTH_NUMBER )" +
	           			 " and (:P_STATUS = -1 or mRwrd.status = :P_STATUS )" +
	           			 " and (:P_HIJRI_YEAR = -1 or mRwrd.hijriYear = :P_HIJRI_YEAR )" +
	           			 " and (:P_FIN_YEAR_APPROVAL_ID = -1 or mRwrd.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID )" +
	           			 " order by mRwrd.id "
			 	),
 })
@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_MONTHLY_REWARDS")
public class FinMonthlyReward extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private Integer monthNumber;
	private Integer hijriYear;
	private Long finYearApprovalId;
	private Long wFInstanceId;
	private Integer status;

	private Double accountBalance;
	private Double totalAmount;
	private Double totalSpent;
	private Integer totalAgentsNumbers;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MONTHLY_REWARDS_SEQ", sequenceName = "FIS_MONTHLY_REWARDS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MONTHLY_REWARDS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "MONTH_NUMBER")
	public Integer getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(Integer monthNumber) {
		this.monthNumber = monthNumber;
	}

	@Basic
	@Column(name = "FIN_YEAR_APPROVALS_ID")
	public Long getFinYearApprovalId() {
		return finYearApprovalId;
	}

	public void setFinYearApprovalId(Long finYearApprovalId) {
		this.finYearApprovalId = finYearApprovalId;
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
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Basic
	@Column(name = "ACCOUNT_BALANCE")
	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@Basic
	@Column(name = "TOTAL_AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Transient
	public Double getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Double totalSpent) {
		this.totalSpent = totalSpent;
	}

	@Transient
	public Integer getTotalAgentsNumbers() {
		return totalAgentsNumbers;
	}

	public void setTotalAgentsNumbers(Integer totalAgentsNumbers) {
		this.totalAgentsNumbers = totalAgentsNumbers;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "HIJRI_YEAR")
	public Integer getHijriYear() {
		return hijriYear;
	}

	public void setHijriYear(Integer hijriYear) {
		this.hijriYear = hijriYear;
	}

}