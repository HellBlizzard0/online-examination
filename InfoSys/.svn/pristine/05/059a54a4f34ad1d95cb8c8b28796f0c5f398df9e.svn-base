package com.code.dal.orm.attach;

import java.io.Serializable;

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

import com.code.dal.orm.AuditEntity;

@NamedQueries({
	   @NamedQuery(name  = "attachment_searchAttachment", 
			   	   query = " select a " +
			   			   " from Attachment a " +
			   			   " where a.entityId = :P_ENTITY_ID " +
			   			   " and a.entityName = :P_ENTITY_NAME " +
			   			   " order by a.id "
	   )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ATTACHMENTS")
public class Attachment extends AuditEntity implements Serializable {
	private Long id;
	private Long entityId;
	private String entityName;
	private String attachmentId;
	private String fileName;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_ATTACHMENTS_SEQ", sequenceName = "FIS_ATTACHMENTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_ATTACHMENTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ENTITY_ID")
	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	@Basic
	@Column(name = "ENTITY_NAME")
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Basic
	@Column(name = "ATTACHMENT_ID")
	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	@Basic
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public Long calculateContentId() {
		return id;
	}
}