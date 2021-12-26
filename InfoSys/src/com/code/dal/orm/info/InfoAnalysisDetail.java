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
@Table(name = "FIS_INFO_ANALYSIS_DETAILS")
public class InfoAnalysisDetail extends AuditEntity implements Serializable, DeleteableAuditEntity, InsertableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Long infoAnalysisId;
	private Long classificationId;

	@Id
	@SequenceGenerator(name = "FIS_INFO_ANALYSIS_DETAILS_SEQ", sequenceName = "FIS_INFO_ANALYSIS_DETAILS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_ANALYSIS_DETAILS_SEQ")
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
	@Column(name = "CLASSFICATIONS_ID")
	public Long getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Long classificationId) {
		this.classificationId = classificationId;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
