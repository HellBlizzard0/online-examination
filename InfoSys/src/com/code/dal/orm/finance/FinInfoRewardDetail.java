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
@Table(name = "FIS_INFORMATION_REWARD_DETAILS")
public class FinInfoRewardDetail extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long infoRewardId;
	private Long infoId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_PAYMENT_INFORMATION_SEQ", sequenceName = "FIS_PAYMENT_INFORMATION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_PAYMENT_INFORMATION_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "INFO_REWARD_ID")
	public Long getInfoRewardId() {
		return infoRewardId;
	}

	public void setInfoRewardId(Long infoRewardId) {
		this.infoRewardId = infoRewardId;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
