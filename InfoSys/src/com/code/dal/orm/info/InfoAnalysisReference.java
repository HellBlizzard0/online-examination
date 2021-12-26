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
@Table(name = "FIS_INFO_ANALYSIS_REFERENCES")
public class InfoAnalysisReference extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity{
	private Long id;
	private Long infoAnalysisId;
	private Long domainReferenceId;
	
	@Id
	@SequenceGenerator(name = "FIS_INFO_ANALYSIS_REFS_SEQ", sequenceName = "FIS_INFO_ANALYSIS_REFS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_ANALYSIS_REFS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "INFO_ANALYSIS_ID")
	public Long getInfoAnalysisId() {
		return infoAnalysisId;
	}
	public void setInfoAnalysisId(Long infoAnalysisId) {
		this.infoAnalysisId = infoAnalysisId;
	}
	
	@Basic
	@Column(name = "DOMAINS_ID_REFERENCE")
	public Long getDomainReferenceId() {
		return domainReferenceId;
	}
	public void setDomainReferenceId(Long domainReferenceId) {
		this.domainReferenceId = domainReferenceId;
	}
	
	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
