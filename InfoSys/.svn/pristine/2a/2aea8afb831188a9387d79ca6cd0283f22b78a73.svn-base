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
	 @NamedQuery(name = "infoSubject_searchInfoSubject", 
          	 query= " select infoSub " +
          			" from InfoSubject infoSub " + 
          			" where (:P_INFO_TYPE_ID = -1 or infoSub.infoTypeId = :P_INFO_TYPE_ID) " + 
          			" and (:P_DESCRIPTION = '-1' or infoSub.description like :P_DESCRIPTION ) " + 
          			" and (:P_ID = -1 or infoSub.id = :P_ID ) " + 
          			" order by infoSub.description "
	)
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_INFO_SUBJECTS")
public class InfoSubject extends AuditEntity implements Serializable, Cloneable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String description;
	private Long infoTypeId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_INFO_SUBJECTS_SEQ", sequenceName = "FIS_INFO_SUBJECTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_INFO_SUBJECTS_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "INFO_TYPES_ID")
	public Long getInfoTypeId() {
		return infoTypeId;
	}

	public void setInfoTypeId(Long infoTypeId) {
		this.infoTypeId = infoTypeId;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
