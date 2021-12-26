package com.code.dal.orm.setup;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	 @NamedQuery(name = "entitySequenceGenerator_searchSequenceGenerator", 
          	 	 query= " select eSG " +
          	 			" from EntitySequenceGenerator eSG " +
          	 			" where (:P_ENTITY = -1 or eSG.entityId = :P_ENTITY) " +
          	 			" order by eSG.entityId"
 )
})

@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_ENTITY_SEQUENCE_GENERATORS")
public class EntitySequenceGenerator extends BaseEntity implements Serializable{
	private Long entityId;
	private Long sequenceNumber;
	private Long version;

	@Id
	@Column(name="ENTITY_ID")
	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	@Basic
	@Column(name = "SEQ_NUM")
	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	@Version
    @Column(name="VERSION")
    /*
     * This attribute handles the optimistic transaction management for the task entity.
     * */
    public Long getVersion() {
		return version;
    }
	
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public void setId(Long id) {
		this.entityId = id;
	}
}
