package com.code.dal.orm.finance;

import java.io.Serializable;
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
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
		 @NamedQuery(name  = "finMonthlyRewDepDetail_searchFinMonthlyRewDepDetail", 
				 	 query = " select  mRwrdDtl " + 
		           	 		 " from FinMonthlyRewDepDetail mRwrdDtl" +
		           			 " where  (:P_FIN_MONTHLY_REWARD_ID = -1 or mRwrdDtl.monthlyRewardId = :P_FIN_MONTHLY_REWARD_ID )" +
		           			 " order by mRwrdDtl.id "
				 	),
		 @NamedQuery(name = "finMonthlyRewDepDetail_sumFinMonthlyRewardTotalSpent", 
	   	 			 query= " select sum(d.totalSpent) " +
		         	 		" from FinMonthlyRewDepDetail d, FinMonthlyReward r" +
		         			" where d.monthlyRewardId = r.id " +
		         	 		" and r.status = 2" +
		         	 		" and (:P_FIN_YEAR_APPROVAL_ID = -1 or r.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID)" 
					)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_MONTHLY_REWARD_DEPT_DETALS")
public class FinMonthlyRewDepDetail extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long monthlyRewardId;
	private Long departmentId;
	private String departmentName;
	private Double totalSpent;
	private Integer resourceNumber;

	private List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_MONTHLY_REWARD_DEP_DET_SEQ", sequenceName = "FIS_MONTHLY_REWARD_DEP_DET_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_MONTHLY_REWARD_DEP_DET_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "MONTHLY_REWARDS_ID")
	public Long getMonthlyRewardId() {
		return monthlyRewardId;
	}

	public void setMonthlyRewardId(Long monthlyRewardId) {
		this.monthlyRewardId = monthlyRewardId;
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
	@Column(name = "TOTAL_SPENT")
	public Double getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Double totalSpent) {
		this.totalSpent = totalSpent;
	}

	@Basic
	@Column(name = "RESOUCES_NUMBER")
	public Integer getResourceNumber() {
		return resourceNumber;
	}

	public void setResourceNumber(Integer resourceNumber) {
		this.resourceNumber = resourceNumber;
	}

	@Transient
	public List<FinMonthlyRewardResourceData> getFinMonthlyRewardResourceDataList() {
		return finMonthlyRewardResourceDataList;
	}

	public void setFinMonthlyRewardResourceDataList(List<FinMonthlyRewardResourceData> finMonthlyRewardResourceDataList) {
		this.finMonthlyRewardResourceDataList = finMonthlyRewardResourceDataList;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "DEPARTMENT_NAME")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
