package com.code.dal.orm.audit;

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

import com.code.dal.orm.BaseEntity;

@SuppressWarnings("serial")
@NamedQueries({
	   @NamedQuery(name  = "auditLog_getLogByUser", 
			   	   query = " select l " +
			   			   " from AuditLog l " +
			   			   " where l.systemUser = :P_SYSTEM_USER " +
			   			   " and l.contentId = :P_CONTENT_ID " +
			   			   " order by l.id "
	   )
})

@Entity
@Table(name = "FIS_AUDITS")
public class AuditLog extends BaseEntity implements Serializable {
	private Long id;
	private String systemUser;
	private String operation;
	private Date operationDate;
	private String contentEntity;
	private Long contentId;
	private String content;

	@SequenceGenerator(name = "EresAuditsSeq", sequenceName = "FIS_AUDITS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "EresAuditsSeq")
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long auditLogId) {
		this.id = auditLogId;
	}

	@Basic
	@Column(name = "SYSTEM_USER_NAME")
	public String getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(String systemUser) {
		this.systemUser = systemUser;
	}

	@Basic
	@Column(name = "OPERATION")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Basic
	@Column(name = "OPERATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	@Basic
	@Column(name = "CONTENT_ENTITY")
	public String getContentEntity() {
		return contentEntity;
	}

	public void setContentEntity(String contentEntity) {
		this.contentEntity = contentEntity;
	}

	@Basic
	@Column(name = "CONTENT_ID")
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Basic
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
