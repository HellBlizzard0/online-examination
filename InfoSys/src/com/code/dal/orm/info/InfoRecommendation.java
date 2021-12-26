package com.code.dal.orm.info;

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
@Table(name = "FIS_INFO_RECOMENDATIONS")
public class InfoRecommendation extends AuditEntity implements Serializable, DeleteableAuditEntity, UpdateableAuditEntity, InsertableAuditEntity {
	private Long id;
	private Long infoId;
	private Long employeeId;
	private Boolean surveillance;
	private Boolean securityCheck;
	private Boolean labCheck;

	@Id
	@SequenceGenerator(name = "FIS_INFO_RECOMENDATIONS_SEQ", sequenceName = "FIS_INFO_RECOMENDATIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_RECOMENDATIONS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "INFORMATION_ID")
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	@Basic
	@Column(name = "STP_VW_EMPLOYEES_ID")
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Basic
	@Column(name = "SURVEILLANCE")
	public Boolean getSurveillance() {
		return surveillance;
	}

	public void setSurveillance(Boolean surveillance) {
		this.surveillance = surveillance;
	}

	@Basic
	@Column(name = "SECURITY_CHECK")
	public Boolean getSecurityCheck() {
		return securityCheck;
	}

	public void setSecurityCheck(Boolean securityCheck) {
		this.securityCheck = securityCheck;
	}

	@Basic
	@Column(name = "LAB_CHECK")
	public Boolean getLabCheck() {
		return labCheck;
	}

	public void setLabCheck(Boolean labCheck) {
		this.labCheck = labCheck;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
