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
	 @NamedQuery(name = "classificationTypes_searchClassificationTypes", 
            	 query= " select cT " +
            	 		" from ClassificationType cT " +
            			" where (:P_ID = -1 or cT.id = :P_ID ) " +
            			" and (:P_DESCRIPTION = '-1' or cT.description like :P_DESCRIPTION ) " +
            			" order by cT.id "
   )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_CLASSIFICATION_TYPES")
public class ClassificationType extends AuditEntity implements Serializable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String description;

	public ClassificationType(){
		
	}
	/*
	 * Copy constructor
	 */
	public ClassificationType(ClassificationType classificationType) {
		this.id = classificationType.getId();
		this.description = classificationType.getDescription();
	}
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_CLASSIFICATION_TYPES_SEQ", sequenceName = "FIS_CLASSIFICATION_TYPES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_CLASSIFICATION_TYPES_SEQ")
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
	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
