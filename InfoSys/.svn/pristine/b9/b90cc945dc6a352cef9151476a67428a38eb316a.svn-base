package com.code.dal.orm.info;

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

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
	 @NamedQuery(name = "classification_searchClassification", 
           	 	 query= " select c " +
           	 			" from Classification c " +
           	 			" where (:P_CLASSIFICATION_TYPE_ID = -1 or c.classificationTypeId = :P_CLASSIFICATION_TYPE_ID) " +
           	 			" and (:P_DESCRIPTION = '-1' or c.description like :P_DESCRIPTION ) " +
           	 			" and (:P_ID = -1 or c.id = :P_ID ) " +
           	 			" order by c.id "
  )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CLASSIFICATIONS")
public class Classification extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String description;
	private Long classificationTypeId;

	public Classification(){
		
	}
	/*
	 * Copy constructor
	 */
	public Classification(Classification classification) {
		this.id = classification.getId();
		this.description = classification.getDescription();
		this.classificationTypeId = classification.getClassificationTypeId();
	}
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CLASSIFICATIONS_SEQ", sequenceName = "FIS_CLASSIFICATIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CLASSIFICATIONS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DECRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "CLASSFICATION_TYPES_ID")
	public Long getClassificationTypeId() {
		return classificationTypeId;
	}

	public void setClassificationTypeId(Long classificationTypeId) {
		this.classificationTypeId = classificationTypeId;
	}
	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
