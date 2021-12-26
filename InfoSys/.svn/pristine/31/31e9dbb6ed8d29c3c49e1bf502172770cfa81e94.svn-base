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
	 @NamedQuery(name = "finDepSupportDetailData_sumFinDepSupportAmount", 
  	 			 query= " select sum(d.supportAmount) " +
	         	 		" from FinDepSupportDetailData d, FinDepSupportData s" +
	         			" where d.departmentSupportId = s.id " +
	         	 		" and s.approved = 1" +
	         			" and (:P_DEPARTMENT_ID = -1 or d.departmentId = :P_DEPARTMENT_ID)" + 
	         	 		" and (:P_FIN_YEAR_APPROVAL_ID = -1 or s.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID)"
				),
	 @NamedQuery(name = "finDepSupportDetailData_searchFinDepSupportDetailData", 
  	 			 query= " select d " +
	         	 		" from FinDepSupportDetailData d" +
	         	 		" where (:P_FIN_DEP_SUPPORT_ID = -1 or d.departmentSupportId = :P_FIN_DEP_SUPPORT_ID)" +
	         	 		" order by d.id "
				),
	 @NamedQuery(name = "finDepSupportDetailData_searchFinDepSupportDetailDataByDepartment", 
  	 			 query= " select d " +
	         	 		" from FinDepSupportDetailData d, FinDepSupportData s" +
	         	 		" where d.departmentSupportId = s.id " +
	         	 		" and (:P_DEPARTMENT_ID = -1 or d.departmentId = :P_DEPARTMENT_ID)" +
	         	 		" and (:P_FIN_YEAR_APPROVAL_ID = -1 or s.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID)" +
	         	 		" and s.approved = 1" +
	         	 		" order by d.id "
				)	
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_VW_DEPS_SUPPORT_DETAILS")
public class FinDepSupportDetailData extends BaseEntity implements Serializable {
	private Long id;
	private Long departmentId;
	private Double supportAmount;
	private Long departmentSupportId;
	private String departmentName;
	private Double balance;
	private Double totalSpent;
	private Date requestDate;
	private String requestDateString;
	
	private FinDepSupportDetail finDepSupportDetail;
	
	public FinDepSupportDetailData(){
		finDepSupportDetail = new FinDepSupportDetail();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return finDepSupportDetail.getId();
	}

	public void setId(Long id) {
		finDepSupportDetail.setId(id);
		this.id = id;
	}

	@Basic
	@Column(name = "STP_VW_DEPARTMENTS_ID")
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		finDepSupportDetail.setDepartmentId(departmentId);
		this.departmentId = departmentId;
	}

	@Basic
	@Column(name = "SUPPORT_AMOUNT")
	public Double getSupportAmount() {
		return supportAmount;
	}

	public void setSupportAmount(Double supportAmount) {
		finDepSupportDetail.setSupportAmount(supportAmount);
		this.supportAmount = supportAmount;
	}

	@Basic
	@Column(name = "DEPARTMENTS_SUPPORTS_ID")
	public Long getDepartmentSupportId() {
		return departmentSupportId;
	}

	public void setDepartmentSupportId(Long departmentSupportId) {
		finDepSupportDetail.setDepartmentSupportId(departmentSupportId);
		this.departmentSupportId = departmentSupportId;
	}

	@Basic
	@Column(name = "DEPARTMENT_NAME")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Basic
	@Column(name = "BALANCE")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		finDepSupportDetail.setBalance(balance);
		this.balance = balance;
	}

	@Basic
	@Column(name = "TOTAL_SPENT")
	public Double getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Double totalSpent) {
		finDepSupportDetail.setTotalSpent(totalSpent);
		this.totalSpent = totalSpent;
	}

	@Transient
	public FinDepSupportDetail getFinDepSupportDetail() {
		return finDepSupportDetail;
	}

	public void setFinDepSupportDetail(FinDepSupportDetail finDepSupportDetail) {
		this.finDepSupportDetail = finDepSupportDetail;
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
}