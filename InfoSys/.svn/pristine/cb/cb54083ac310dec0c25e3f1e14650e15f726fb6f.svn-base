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
import javax.persistence.Transient;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name = "finYearAccountSupport_sumFinYearSupport", 
     	 		query = " select sum(s.rewardSupportBalance), sum(s.regionsSupportBalance) " +
	         	 		" from FinYearAccountSupport s " +
	         			" where s.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID"
				),
	@NamedQuery(name = "finYearAccountSupport_searchFinYearSupport", 
     	 		query = " select s " +
	         	 		" from FinYearAccountSupport s " +
	         			" where s.finYearApprovalId = :P_FIN_YEAR_APPROVAL_ID" +
	         			" and (:P_FROM_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_FROM_DATE, 'MI/MM/YYYY') <= s.supportDate)" + 
	           			" and (:P_TO_DATE_NULL = 1 or PKG_CHAR_TO_DATE (:P_TO_DATE, 'MI/MM/YYYY') >= s.supportDate) " +
	         			" order by s.supportDate desc"
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_FIN_YEAR_ACCOUNT_SUPPORT")
public class FinYearAccountSupport extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity ,Cloneable {
	private Long id;
	private String supportNumber;
	private Date supportDate;
	private String supportDateString;
	private Long finYearApprovalId;
	private Double rewardSupportBalance;
	private Double regionsSupportBalance;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_FIN_YEAR_ACC_SUPPORT_SEQ", sequenceName = "FIS_FIN_YEAR_ACC_SUPPORT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_FIN_YEAR_ACC_SUPPORT_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "SUPPORT_NUMBER")
	public String getSupportNumber() {
		return supportNumber;
	}

	public void setSupportNumber(String supportNumber) {
		this.supportNumber = supportNumber;
	}

	@Basic
	@Column(name = "SUPPORT_DATE")
	public Date getSupportDate() {
		return supportDate;
	}

	public void setSupportDate(Date supportDate) {
		this.supportDateString = HijriDateService.getHijriDateString(supportDate);
		this.supportDate = supportDate;
	}

	@Transient
	public String getSupportDateString() {
		return supportDateString;
	}

	@Basic
	@Column(name = "FIN_YEAR_APPROVAL_ID")
	public Long getFinYearApprovalId() {
		return finYearApprovalId;
	}

	public void setFinYearApprovalId(Long finYearApprovalId) {
		this.finYearApprovalId = finYearApprovalId;
	}

	@Basic
	@Column(name = "REWARD_SUPPORT_BALANCE")
	public Double getRewardSupportBalance() {
		return rewardSupportBalance;
	}

	public void setRewardSupportBalance(Double rewardSupportBalance) {
		this.rewardSupportBalance = rewardSupportBalance;
	}

	@Basic
	@Column(name = "REGIONS_SUPPORT_BALANCE")
	public Double getRegionsSupportBalance() {
		return regionsSupportBalance;
	}

	public void setRegionsSupportBalance(Double regionsSupportBalance) {
		this.regionsSupportBalance = regionsSupportBalance;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}