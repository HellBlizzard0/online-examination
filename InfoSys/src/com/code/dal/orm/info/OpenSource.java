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
	 @NamedQuery(name = "openSource_searchOpenSource", 
        	 	 query= " select openS " +
        	 			" from OpenSource openS " +
        	 			" where (:P_ID = -1 or openS.id = :P_ID) " +
        	 			" and (:P_NAME = '-1' or openS.name like :P_NAME ) " +
        	 			" order by openS.name "
			 ),
	 @NamedQuery(name = "openSource_searchInfoOpenSource", 
			 	 query= " select openS " +
			 			" from OpenSource openS, InfoSource infoS" +
			 			" where openS.id = infoS.openSourceId " +
			 			" and (:P_INFO_ID = -1 or infoS.infoId = :P_INFO_ID)" +
			 			" order by openS.name "
			 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_OPEN_SOURCES")
public class OpenSource extends AuditEntity implements Serializable, Cloneable, InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private String name;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_OPEN_SOURCES_SEQ", sequenceName = "FIS_OPEN_SOURCES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_OPEN_SOURCES_SEQ")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}
}
