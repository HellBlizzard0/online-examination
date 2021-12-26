package com.code.dal.orm.setup;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;

@NamedQueries({
		@NamedQuery(name  = "setupDomain_searchDomain", 
				    query = " select d " +
						    " from SetupDomain d , SetupClass c " +
				    		" where d.classId = c.id " +
				    		" and (:P_DESCRIPTION = '-1' or d.description like :P_DESCRIPTION) " +
						    " and (:P_ACTIVE = -1 or d.active = :P_ACTIVE) " +
						    " and (:P_CLASS_DESCRIPTION = '-1' or c.description like :P_CLASS_DESCRIPTION) " +
//						    " and (:P_CLASS_CODE = '-1' or c.code = :P_CLASS_CODE) " +
						    " and (:P_CLASS_ID = -1 or c.id = :P_CLASS_ID)" +
	                    	" and (:P_CLASS_CODE_LIST_SIZE = 0 or c.code IN (:P_CLASS_CODE_LIST)) "+
//	                    	"and (:P_SECURITY_ANALYSIS_FLAG = -1 or d.securityAnalysisFlag= :P_SECURITY_ANALYSIS_FLAG)" +
						    " order by c.description, d.description "
				    ), 
	    @NamedQuery(name  = "setupDomain_searchInfoRelatedDomains", 
			      	 query = " select s " +
			      			 "from SetupDomain s, InfoRelatedEntity infoRel" +
			      			 " where ( s.id = infoRel.domainId)" +
			      			 " and (:P_INFO_ID = '-1' or infoRel.infoId = :P_INFO_ID )" +
			             	 " order by s.id "
			         )
})


@SuppressWarnings("serial")
@Entity
@Table(name = "FIS_DOMAINS")
public class SetupDomain extends AuditEntity implements Serializable, Cloneable ,InsertableAuditEntity, UpdateableAuditEntity, DeleteableAuditEntity {
	private Long id;
	private Long classId;
	private Integer active;
	private Boolean activeFlag = true ;
	private String description;
	private Boolean securityAnalysisFlag;

	public void setId(Long Id) {
		this.id = Id;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "FIS_DOMAINS_SEQ", sequenceName = "FIS_DOMAINS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIS_DOMAINS_SEQ")
	public Long getId() {
		return id;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	@Basic
	@Column(name = "CLASSES_ID")
	public Long getClassId() {
		return classId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setActive(Integer active) {
		this.active = active;
		activeFlag = (active == 1) ? true : false;
	}

	@Basic
	@Column(name = "ACTIVE")
	public Integer getActive() {
		return active;
	}
	
	@Transient
	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
		active = activeFlag ? 1 : 0;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public Long calculateContentId() {
		return this.id;
	}

	@Basic
	@Column(name = "SECURITY_ANALYSIS_FLAG")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSecurityAnalysisFlag() {
		return securityAnalysisFlag;
	}

	public void setSecurityAnalysisFlag(Boolean securityAnalysisFlag) {
		this.securityAnalysisFlag = securityAnalysisFlag;
	}
}