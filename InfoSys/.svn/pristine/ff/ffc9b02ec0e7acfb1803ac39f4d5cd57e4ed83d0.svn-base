package com.code.dal.orm.finance;

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
	 @NamedQuery(name = "FinDepSupportData_searchFinDepSupportData", 
  	 			 query= " select s " +
	         	 		" from FinDepSupportData s " +
	         			" where (:P_WF_INSTANCE_ID = -1 or s.wfInstanceId = :P_WF_INSTANCE_ID)" +
	         			" order by s.id "
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_DEPARTMENTS_SUPPORTS")
public class FinDepSupportData extends BaseEntity implements Serializable{
	private Long id;
	private String requestNumber;
	private Date requestDate;
	private String requestDateString;
	private String remarks;
	private Long finYearApprovalId;
	private Long wfInstanceId;
	private Integer approved;
	private Integer finYear;
	private Double balance;
	
	private FinDepSupport finDepSupport;
	
	public FinDepSupportData(){
		finDepSupport = new FinDepSupport();
	}
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return finDepSupport.getId();
	}

	public void setId(Long id) {
		finDepSupport.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "REQUEST_NUMBER")
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		finDepSupport.setRequestNumber(requestNumber);
		this.requestNumber = requestNumber;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_DATE")
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		finDepSupport.setRequestDate(requestDate);
		this.requestDateString = HijriDateService.getHijriDateString(requestDate);
		this.requestDate = requestDate;
	}

	@Transient
	public String getRequestDateString() {
		return requestDateString;
	}

	@Basic
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		finDepSupport.setRemarks(remarks);
		this.remarks = remarks;
	}

	@Basic
	@Column(name = "FIS_FIN_YEAR_APPROVALS_ID")
	public Long getFinYearApprovalId() {
		return finYearApprovalId;
	}

	public void setFinYearApprovalId(Long finYearApprovalId) {
		finDepSupport.setFinYearApprovalId(finYearApprovalId);
		this.finYearApprovalId = finYearApprovalId;
	}

	@Basic
	@Column(name = "WF_INSTANCE_ID")
	public Long getWfInstanceId() {
		return wfInstanceId;
	}

	public void setWfInstanceId(Long wfInstanceId) {
		finDepSupport.setWfInstanceId(wfInstanceId);
		this.wfInstanceId = wfInstanceId;
	}

	@Basic
	@Column(name = "APPROVED")
	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		finDepSupport.setApproved(approved);
		this.approved = approved;
	}
	
	@Basic
	@Column(name = "FIN_YEAR")
	public Integer getFinYear() {
		return finYear;
	}

	public void setFinYear(Integer finYear) {
		this.finYear = finYear;
	}

	@Basic
	@Column(name = "BALANCE")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		finDepSupport.setBalance(balance);
		this.balance = balance;
	}

	@Transient
	public FinDepSupport getFinDepSupport() {
		return finDepSupport;
	}

	public void setFinDepSupport(FinDepSupport finDepSupport) {
		this.finDepSupport = finDepSupport;
	}
}