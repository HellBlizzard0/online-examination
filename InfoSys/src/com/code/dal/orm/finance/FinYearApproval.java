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

import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.services.util.HijriDateService;

@NamedQueries({
	 @NamedQuery(name = "FinYearApproval_searchFinYearApproval", 
    	 		query = " select a " +
	         	 		" from FinYearApproval a " +
	         			" where (:P_YEAR = -1 or a.finYear = :P_YEAR)" +
	         	 		" and (:P_ID = -1 or  a.id = :P_ID)" + 
	         			" order by a.id "
				),
	@NamedQuery(name = "FinYearApproval_searchFinYears", 
    	 		query = " select a.finYear " +
	         	 		" from FinYearApproval a " +
	         			" order by a.finYear desc "
				)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_FIN_YEAR_APPROVALS")
public class FinYearApproval extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Integer finYear;
	private Double currentYearBalance;
	private Integer paymentSerial;
	private Date paymentDate;
	private String paymentDateString;
	private Double rewardInitialBalance;
	private Double regionsInitialBalance;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_FIN_YEAR_APPROVALS_SEQ", sequenceName = "FIS_FIN_YEAR_APPROVALS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_FIN_YEAR_APPROVALS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@Column(name = "CURRENT_YEAR_BALANCE")
	public Double getCurrentYearBalance() {
		return currentYearBalance;
	}

	public void setCurrentYearBalance(Double currentYearBalance) {
		this.currentYearBalance = currentYearBalance;
	}

	@Basic
	@Column(name = "PAYMENT_SERIAL")
	public Integer getPaymentSerial() {
		return paymentSerial;
	}

	public void setPaymentSerial(Integer paymentSerial) {
		this.paymentSerial = paymentSerial;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		if (paymentDate != null) {
			this.paymentDateString = HijriDateService.getHijriDateString(paymentDate);
		}
		this.paymentDate = paymentDate;
	}

	@Transient
	public String getPaymentDateString() {
		return paymentDateString;
	}

	@Basic
	@Column(name = "REWARD_INITIAL_BALANCE")
	public Double getRewardInitialBalance() {
		return rewardInitialBalance;
	}

	public void setRewardInitialBalance(Double rewardInitialBalance) {
		this.rewardInitialBalance = rewardInitialBalance;
	}

	@Basic
	@Column(name = "REGIONS_INITIAL_BALANCE")
	public Double getRegionsInitialBalance() {
		return regionsInitialBalance;
	}

	public void setRegionsInitialBalance(Double regionsInitialBalance) {
		this.regionsInitialBalance = regionsInitialBalance;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}