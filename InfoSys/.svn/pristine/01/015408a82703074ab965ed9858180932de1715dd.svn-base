package com.code.dal.orm.securitymission;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_PERMITS_PATCHS")
public class PermitsPatch extends AuditEntity implements Serializable, InsertableAuditEntity, DeleteableAuditEntity, UpdateableAuditEntity {
	private Long id;
	private Date patchDate;
	private String fileName;
	private String patchType;
	private String status;
	private Long successCount;
	private Long failureCount;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_PERMITS_PATCHS_SEQ", sequenceName = "FIS_PERMITS_PATCHS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_PERMITS_PATCHS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PATCH_DATE")
	public Date getPatchDate() {
		return patchDate;
	}

	public void setPatchDate(Date patchDate) {
		this.patchDate = patchDate;
	}

	@Basic
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Basic
	@Column(name = "PATCH_TYPE")
	public String getPatchType() {
		return patchType;
	}

	public void setPatchType(String patchType) {
		this.patchType = patchType;
	}

	@Basic
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "SUCCESS_COUNT")
	public Long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Long successCount) {
		this.successCount = successCount;
	}

	@Basic
	@Column(name = "FAILURE_COUNT")
	public Long getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}
}
